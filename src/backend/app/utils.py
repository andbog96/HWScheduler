# app/jwt.py
import jwt
from .settings import alg, SECRET_KEY


def generate_token(user_id):
    payload = {'userId': user_id}
    token = jwt.encode(payload, SECRET_KEY, algorithm=alg)
    return token

def verify_token(token):
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[alg])
        return payload['userId']
    except jwt.ExpiredSignatureError:
        return 'Token is expired'
    except jwt.InvalidTokenError:
        return 'Invalid token'
