# app/db.py
import psycopg2

conn = psycopg2.connect(
    dbname="postgres",
    user="postgres",
    password="1234",
    host="localhost"
)

def create_user(login, password):
    cur.execute("INSERT INTO Users (login, password) VALUES (%s, %s) RETURNING userId", (login, password))
    user_id = cur.fetchone()[0]
    conn.commit()
    return user_id
