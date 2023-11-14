# app/__init__.py
from flask import Flask
import psycopg2

app = Flask(__name__)
conn = psycopg2.connect(
    dbname="postgres",
    user="postgres",
    password="1234",
    host="localhost"
)
