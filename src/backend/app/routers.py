# app/routes.py
from flask import Blueprint, jsonify, make_response, request, Flask
from app.db import create_user, check_user_rights, check_event_exists, delete_event_from_db
from .utils import generate_token, verify_token

main_blueprint = Blueprint('main', __name__)

# User Management
@main_blueprint.route('/user', methods=['POST'])
def create_user_route():
    try:
        data = request.get_json()
        login = data['login']
        password = data['password']
        user_id = create_user(login, password)
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
    token = request.args.get('token')
    if not token:
        return jsonify({'message': 'token is missing'}), 401
    user_id = verify_token(token)
    if user_id == 'token is expired' or user_id == 'invalid token':
        return jsonify({'message': 'invalid token'}), 401
    if not check_user_rights(channel_id, user_id):
        return jsonify({'message': 'user has not access rights to delete event'}), 403
    if not check_event_exists(event_id):
        return jsonify({'message': 'event does not exist'}), 400
    delete_event_from_db(event_id)
    return jsonify({'message': 'event deleted successfully'})