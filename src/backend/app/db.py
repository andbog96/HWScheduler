# app/db.py
from .settings import conn, cur
import datetime


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

def channelExists(name: str) -> bool:
    cur.execute("SELECT EXISTS(SELECT 1 FROM Channels WHERE name = %s)", (name,))
    return cur.fetchone()[0]

def channelExistsById(ch_id: int) -> bool:
    cur.execute("SELECT EXISTS(SELECT 1 FROM Channels WHERE channelId = %s)", (ch_id,))
    return cur.fetchone()[0]

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
    

def deleteSubcribe(userId: int, channelId :int):
    cur.execute("DELETE FROM Subscriptions WHERE userId = %s and channelId = %s",
                (userId, channelId))
    conn.commit()
    
def getAllDataByUser(userId: int):
    
    # Получение данных для каналов с сортировкой по deadline
    cur.execute(f"""
        SELECT
            Channels.channelId AS channel_id,
            Channels.name AS name,
            CASE WHEN Channels.createdBy = {userId} THEN TRUE ELSE FALSE END AS is_admin
        FROM
            Channels
        FULL JOIN Subscriptions ON Subscriptions.channelId = Channels.channelId
        WHERE Subscriptions.userId = {userId} OR Channels.createdBy = {userId}""")
    channelsData = cur.fetchall()

    resChannelsData = []

    for i in channelsData:
        resChannelsData.append(
            {
                "channel_id" : i[0],
                "name": i[1],
                "is_admin": i[2]
            }
        )

    current_time = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')

    # Получение данных для событий с сортировкой по deadline
    cur.execute(f"""
        SELECT
            Events.eventId AS event_id,
            Events.channelId AS channel_id,
            Events.name AS name,
            Events.description AS description,
            Events.deadline AS deadline,
            Events.userCompletedTask AS completedTask,
            Events.sumTime AS sumTime
        FROM
            Events
        INNER join Subscriptions
        on Subscriptions.channelId = Events.channelId
        WHERE
            Subscriptions.userId = {userId} AND Events.deadline > '{current_time}'
        ORDER BY
            Events.deadline;
    """)
    eventsData = cur.fetchall()

    resEventsData = []

    for i in eventsData:
        resEventsData.append(
            {
                "event_id": i[0],
                "channel_id": i[1],
                "name": i[2],
                "description": i[3],
                "deadline": i[4],
                "estimated": 5 if i[5] == 0  else i[6]/i[5]
            }
        )
    
    return (resChannelsData, eventsData)


def update_event(event_id, name, description, deadline):
    cur.execute(
        """
        UPDATE Events
        SET name = %s, description = %s, deadline = %s
        WHERE eventId = %s
        """,
        (name, description, deadline, event_id)
    )
    conn.commit()


def check_done(user_id, event_id):
    cur.execute("SELECT COUNT(*) FROM DoneJobs WHERE userId = %s AND eventId = %s", (user_id, event_id))
    return cur.fetchone()[0] == 0


def perform_event(user_id, event_id, time):
    cur.execute(
        """
        UPDATE Events 
        SET userCompletedTask = userCompletedTask + 1, sumTime = sumTime + %s
        WHERE eventId = %s
        """,
        (time, event_id)
    )
    conn.commit()
    cur.execute(
        "INSERT INTO DoneJobs (userId, eventId, time) VALUES (%s, %s, %s)",
        (user_id, event_id, time)
    )
    conn.commit()