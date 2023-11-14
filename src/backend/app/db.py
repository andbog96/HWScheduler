# app/db.py
from .settings import conn

cur = conn.cursor()

def getUserByLogin(login):
    cur.execute("SELECT userId, password FROM Users WHERE login = %s", (login,))
    return cur.fetchone()

def createUser(login: str, password:str) -> int:
    cur.execute("INSERT INTO Users (login, password) VALUES (%s, %s) RETURNING userId", (login, password))
    userId = cur.fetchone()[0]
    conn.commit()
    return userId

def getChannelIdByName(name:str):
    cur.execute("SELECT channelId FROM Channels WHERE name = %s", (name,))
    return cur.fetchone()

def createChannel(name :str, createdBy: str) -> int:
    cur.execute("INSERT INTO Channels (name, createdBy) VALUES (%s, %s) RETURNING channelId",(name, createdBy))
    channelId = cur.fetchone()[0]
    conn.commit()
    return channelId

def existsSubcribe(userId: int, channelId :int):
    cur.execute("SELECT * FROM Subscriptions  WHERE userId = %s and channelId = %s",(userId, channelId))
    return cur.fetchone() is not None

def createSubcribe(userId: int, channelId :int):
    cur.execute("INSERT INTO Subscriptions (userId, channelId) VALUES (%s, %s)",(userId, channelId))
    conn.commit()

def check_channel_exists(channel_id):
    cur.execute(
        "select count(*) from Channels where channelId = %s",
        (channel_id,)
    )
    return cur.fetchone()[0] == 1

def check_user_rights(channel_id, user_id):
    cur.execute("select createdBy from Channels where channelId = %s", (channel_id,))
    create_user = cur.fetchone()
    return create_user is not None and create_user[0] == user_id

def add_event(channel_id, name, description, deadline):
    cur.execute(
        "insert into events (channelId, name, description, deadline) values (%s, %s, %s, %s)",
        (channel_id, name, description, deadline)
    )
    conn.commit()
    