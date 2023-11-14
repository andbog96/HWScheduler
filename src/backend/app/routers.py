# app/routes.py
from flask import Blueprint, jsonify, make_response, request, Flask
from app.db import create_user
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