# app/routes.py
from flask import Blueprint, jsonify, make_response, request, Flask
from app.db import *
from .utils import generate_token, verify_token

main_blueprint = Blueprint('main', __name__)

# User Management
@main_blueprint.route('/user', methods=['POST'])
def createUserRoute():
    try:
        data = request.get_json()
        login = data['login']
        password = data['password']
        user_data = getUserByLogin(login)

        if user_data is None : 
            user_id = createUser(login, password)
        
        else:
            user_id = user_data[0]
            passwordActual = user_data[1]
            if password != passwordActual:
                raise Exception("error password")

        token = generate_token(user_id)
        response_data = {'token': token}
        status_code = 200
        message = 'User registered successfully'
    except Exception as e:
        response_data = {'error': str(e)}
        status_code = 400
        message = 'Bad request'
    return make_response(jsonify(response_data), status_code, {'message': message})

# Channel Management
@main_blueprint.route('/channel', methods=['POST'])
def createChannelRoute():
    try:
        data = request.get_json()
        name = data['name']
        token = data['token'] 

        if not token:
            return jsonify({'message': 'Token is missing'}), 404

        userId = verify_token(token)
        if userId == 'Token is expired' or userId == 'Invalid token':
            return jsonify({'message': 'Invalid token'}), 404

        channel = getChannelIdByName(name)

        if channel is not None:
            raise Exception("channel Exists")

        channelId = createChannel(name, userId)

        channel = getChannelIdByName(name)
        response_data = {'name': name, 'channel_id' : channelId}
        status_code = 200
        message = "Channel created successfully"

    except Exception as e:
        response_data = {'error': str(e)}
        status_code = 400
        message = 'Bad request'

    return make_response(jsonify(response_data), status_code, {'message': message})

@main_blueprint.route('/user/channel', methods=['POST'])
def subcribeChannel():

    data = request.get_json()
    token = data['token']
    shortname = data['shortname']

    userId = verify_token(token)
    if userId == 'Token is expired' or userId == 'Invalid token':
        return jsonify({'message': 'Invalid token'}), 404
    
    channel = getChannelIdByName(shortname)

    if channel is None:
        return jsonify({'message': 'Invalid channel id'}), 404
    
    channelId = channel[0]

    if existsSubcribe(userId, channelId):
        return jsonify({'message': 'subscribe exists'}), 404

    createSubcribe(userId, channelId)
    return jsonify({'message': 'Subscribed to the channel. Get /user/info'}), 200
    
@main_blueprint.route('/user/channel/<int:ch_id>', methods=['DELETE'])
def deleteChannelRouter(ch_id):
    data = request.get_json()
    token = data['token']

    userId = verify_token(token)
    if userId == 'Token is expired' or userId == 'Invalid token':
        return jsonify({'message': 'Invalid token'}), 404

    if not channelExistsById(ch_id):
        return jsonify({'message': 'Invalid channel id'}), 404
    
    deleteSubcribe(userId, ch_id)
    return jsonify({'message': 'Unsubscribed from the channel successfully'}), 200

@main_blueprint.route('/user/info', methods=['GET'])
def userInfoRouter():
    data = request.get_json()
    token = data['token']

    userId = verify_token(token)
    if userId == 'Token is expired' or userId == 'Invalid token':
        return jsonify({'message': 'Invalid token'}), 404

    (channelsData,eventsData) =  getAllDataByUser(userId)

    result = {
        "channels": channelsData,
        "events": eventsData
    }
    
    return make_response(jsonify(result), 200, {'message': "Returns information related to user"})


@main_blueprint.route('/event/<int:event_id>', methods=['PUT'])
def change_event(event_id):
    if not check_event_exists(event_id):
        return jsonify({'message': 'Event does not exist'}), 404
    data = request.get_json()
    token = data['token']
    if not token:
        return jsonify({'message': 'Token is missing'}), 401
    user_id = verify_token(token)
    if user_id == 'Token is expired' or user_id == 'Invalid token':
        return jsonify({'message': 'Invalid token'}), 401
    update_event(event_id, data['name'], data['description'], data['deadline'])
    return jsonify({'message': 'Event was updated successfully'})