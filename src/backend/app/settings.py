import psycopg2
import random, string

conn = psycopg2.connect(
    dbname="postgres",
    user="postgres",
    password="1234",
    host="localhost"
)

SECRET_KEY = ''.join(random.choice(string.ascii_lowercase) for i in range(100))

alg = 'HS256'