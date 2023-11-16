# app/routes.py
from flask import Blueprint, jsonify, make_response, request
from app.db import *
from .utils import generate_token, verify_token

main_blueprint = Blueprint('main', __name__)

# User Management
@main_blueprint.route('/user', methods=['POST'])
def createUserRoute():
    db = DB()

    try:
        data = request.get_json()
        login = data['login']
        password = data['password']

        user_data = db.getUserByLogin(login)

        if user_data is None : 
            user_id = db.createUser(login, password)
        
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



# Delete an existing event
@main_blueprint.route('/channel/<int:channel_id>/event/<int:event_id>', methods=['delete'])
def delete_event(channel_id, event_id):

    db = DB()

    token = request.headers['token']
    if not token:
        return jsonify({'message': 'token is missing'}), 401
    user_id = verify_token(token)
    if user_id == 'token is expired' or user_id == 'invalid token':
        return jsonify({'message': 'invalid token'}), 401
    if not db.check_user_rights(channel_id, user_id):
        return jsonify({'message': 'user has not access rights to delete event'}), 403
    if not db.check_event_exists(event_id):
        return jsonify({'message': 'event does not exist'}), 400
    db.delete_event_from_db(event_id)
    return jsonify({'message': 'event deleted successfully'})

# Channel Management
@main_blueprint.route('/channel', methods=['POST'])
def createChannelRoute():

    db = DB()

    try:
        name = request.get_json()['name']
        token = request.headers['token']

        if not token:
            return jsonify({'message': 'Token is missing'}), 404

        userId = verify_token(token)
        if userId == 'Token is expired' or userId == 'Invalid token':
            return jsonify({'message': 'Invalid token'}), 404

        channel = db.getChannelIdByName(name)

        if channel is not None:
            raise Exception("channel Exists")

        channelId = db.createChannel(name, userId)

        db.createSubcribe(userId,channelId)

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

    db = DB()

    data = request.get_json()
    token = request.headers['token']
    shortname = data['shortname']

    userId = verify_token(token)
    if userId == 'Token is expired' or userId == 'Invalid token':
        return jsonify({'message': 'Invalid token'}), 404
    
    channel = db.getChannelIdByName(shortname)

    if channel is None:
        return jsonify({'message': 'Invalid channel id'}), 404
    
    channelId = channel[0]

    if db.existsSubcribe(userId, channelId):
        return jsonify({'message': 'subscribe exists'}), 404

    db.createSubcribe(userId, channelId)
    return jsonify({'message': 'Subscribed to the channel. Get /user/info'}), 200


@main_blueprint.route('/channel/<int:channel_id>/event', methods=['POST'])
def create_event(channel_id):

    db = DB()

    if not db.check_channel_exists(channel_id):
        return jsonify({'message': 'Channel does not exist'}), 404
    data = request.get_json()
    token = request.headers['token']
    if not token:
        return jsonify({'message': 'Token is missing'}), 401
    user_id = verify_token(token)
    if user_id == 'Token is expired' or user_id == 'Invalid token':
        return jsonify({'message': 'Invalid token'}), 401
    if not db.check_user_rights(channel_id, user_id):
        return jsonify({'message': 'User has not access rights to create event'}), 403
    name = data['name']
    description = data['description']
    deadline = data['deadline']
    if db.event_exists(channel_id, name, description, deadline):
        return jsonify({'message': 'Event already exists'}), 400
    db.add_event(channel_id, data['name'], data['description'], data['deadline'])
    return jsonify({'message': 'Event was created successfully'})
    
@main_blueprint.route('/user/channel/<int:ch_id>', methods=['DELETE'])
def deleteChannelRouter(ch_id):

    db = DB()

    token = request.headers['token']

    userId = verify_token(token)
    if userId == 'Token is expired' or userId == 'Invalid token':
        return jsonify({'message': 'Invalid token'}), 404

    if not db.channelExistsById(ch_id):
        return jsonify({'message': 'Invalid channel id'}), 404
    
    db.deleteSubcribe(userId, ch_id)
    return jsonify({'message': 'Unsubscribed from the channel successfully'}), 200

@main_blueprint.route('/user/info', methods=['GET'])
def userInfoRouter():

    db = DB()

    token = request.headers['token']

    userId = verify_token(token)
    if userId == 'Token is expired' or userId == 'Invalid token':
        return jsonify({'message': 'Invalid token'}), 404

    (channelsData,eventsData) =  db.getAllDataByUser(userId)

    result = {
        "channels": channelsData,
        "events": eventsData
    }
    
    return make_response(jsonify(result), 200, {'message': "Returns information related to user"})


@main_blueprint.route('/event/<int:event_id>', methods=['PUT'])
def change_event(event_id):

    db = DB()

    if not db.check_event_exists(event_id):
        return jsonify({'message': 'Event does not exist'}), 404
    
    data = request.get_json()
    token = request.headers['token']

    if not token:
        return jsonify({'message': 'Token is missing'}), 401
    
    user_id = verify_token(token)
    if user_id == 'Token is expired' or user_id == 'Invalid token':
        return jsonify({'message': 'Invalid token'}), 401
    
    db.update_event(event_id, data['name'], data['description'], data['deadline'])
    return jsonify({'message': 'Event was updated successfully'})


@main_blueprint.route('/user/event/<int:event_id>', methods=['POST'])
def mark_event_as_done(event_id):

    db = DB()

    if not db.check_event_exists(event_id):
        return jsonify({'message': 'Event does not exist'}), 404
    
    data = request.get_json()
    token = request.headers['token']
    if not token:
        return jsonify({'message': 'Token is missing'}), 401
    
    user_id = verify_token(token)
    if user_id == 'Token is expired' or user_id == 'Invalid token':
        return jsonify({'message': 'Invalid token'}), 401
    
    if not db.check_done(user_id, event_id):
        return jsonify({'message': 'Task is already done'}), 400
    
    db.perform_event(user_id, event_id, data['work_time'])
    return jsonify({'message': 'Event was marked as done'})