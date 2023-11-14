# app/db.py
from app import conn

cur = conn.cursor()

def create_user(login, password):
    cur.execute("INSERT INTO Users (login, password) VALUES (%s, %s) RETURNING userId", (login, password))
    user_id = cur.fetchone()[0]
    conn.commit()
    return user_id
