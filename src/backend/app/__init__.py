# app/__init__.py
from flask import Flask
from .routers import main_blueprint

app = Flask(__name__)
app.register_blueprint(main_blueprint)