# app/routes.py
from flask import jsonify, request
from app import app
from app.db import create_user
from app.jwt import generate_token, verify_token

@app.route('/user', methods=['POST'])
def create_user_route():
    data = request.get_json()
    login = data['login']
    password = data['password']
    user_id = create_user(login, password)
    token = generate_token(user_id)
    return jsonify({'token': token})
