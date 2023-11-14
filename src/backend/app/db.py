# app/db.py
import psycopg2

conn = psycopg2.connect(
    dbname="postgres",
    user="postgres",
    password="1234",
    host="localhost"
)

cur = conn.cursor()

def create_user(login, password):
    cur.execute("INSERT INTO Users (login, password) VALUES (%s, %s) RETURNING userId", (login, password))
    user_id = cur.fetchone()[0]
    conn.commit()
    return user_id

def delete_event_from_db(event_id):
    cur.execute("delete from events where eventId = %s", (event_id,))
    conn.commit()

def check_user_rights(channel_id, user_id):
    cur.execute("select createdBy from Channels where channelId = %s", (channel_id,))
    create_user = cur.fetchone()
    return create_user is not None and create_user[0] == user_id

def check_event_exists(event_id):
    cur.execute("select count(*) from Events where eventId = %s", (event_id,))
    return cur.fetchone()[0] == 1