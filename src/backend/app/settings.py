import psycopg2
import random, string

conn = psycopg2.connect(
    dbname="postgres",
    user="postgres",
    password="1234",
    host="localhost"
)

SECRET_KEY = "sdghkghgkjshdfkjghsdkghsdlkghdkghsdkghdjkghsdlghsdghjdlkghdfh"

alg = 'HS256'