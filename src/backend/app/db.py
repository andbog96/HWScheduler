# app/db.py
import datetime
from .settings import init_base

class DB:
    def __init__(self) -> None:
        self.conn=  init_base()
        self.cur = self.conn.cursor()

    def __del__(self):
        self.cur.close()
        self.conn.close()

    def create_user(self, login, password):
        self.cur.execute("INSERT INTO Users (login, password) VALUES (%s, %s) RETURNING userId", (login, password))
        user_id =self.cur.fetchone()[0]
        self.conn.commit()
        return user_id

    def delete_event_from_db(self, event_id):
        self.cur.execute("delete from events where eventId = %s", (event_id,))
        self.conn.commit()

    def check_user_rights(self, channel_id, user_id):
        self.cur.execute("select createdBy from Channels where channelId = %s", (channel_id,))
        create_user = self.cur.fetchone()
        return create_user is not None and create_user[0] == user_id

    def check_event_exists(self, event_id):
        self.cur.execute("select count(*) from Events where eventId = %s", (event_id,))
        return self.cur.fetchone()[0] == 1

    def event_exists(self,channel_id, name, description, deadline):
        self.cur.execute(
            "select count(*) from Events where channelId = %s and name = %s and description = %s and deadline = %s", 
            (channel_id, name, description, deadline))
        return self.cur.fetchone()[0] == 1

    def getUserByLogin(self, login):
        self.cur.execute("SELECT userId, password FROM Users WHERE login = %s", (login,))
        return self.cur.fetchone()

    def createUser(self, login: str, password:str) -> int:
        self.cur.execute("INSERT INTO Users (login, password) VALUES (%s, %s) RETURNING userId", (login, password))
        userId =self.cur.fetchone()[0]
        self.conn.commit()
        return userId

    def getChannelIdByName(self, name:str):
        self.cur.execute("SELECT channelId FROM Channels WHERE name = %s", (name,))
        return self.cur.fetchone()

    def channelExists(self, name: str) -> bool:
        self.cur.execute("SELECT EXISTS(SELECT 1 FROM Channels WHERE name = %s)", (name,))
        return self.cur.fetchone()[0]

    def channelExistsById(self, ch_id: int) -> bool:
        self.cur.execute("SELECT EXISTS(SELECT 1 FROM Channels WHERE channelId = %s)", (ch_id,))
        return self.cur.fetchone()[0]

    def createChannel(self, name :str, createdBy: str) -> int:
        self.cur.execute("INSERT INTO Channels (name, createdBy) VALUES (%s, %s) RETURNING channelId",(name, createdBy))
        channelId = self.cur.fetchone()[0]
        self.conn.commit()
        return channelId

    def existsSubcribe(self, userId: int, channelId :int):
        self.cur.execute("SELECT * FROM Subscriptions  WHERE userId = %s and channelId = %s",(userId, channelId))
        return self.cur.fetchone() is not None

    def createSubcribe(self, userId: int, channelId :int):
        self.cur.execute("INSERT INTO Subscriptions (userId, channelId) VALUES (%s, %s)",(userId, channelId))
        self.conn.commit()


    def check_channel_exists(self,channel_id):
        self.cur.execute(
            "select count(*) from Channels where channelId = %s",
            (channel_id,)
        )
        return self.cur.fetchone()[0] == 1

    # check if user is creator of the channel
    def check_user_rights(self,channel_id, user_id):
        self.cur.execute("select createdBy from Channels where channelId = %s", (channel_id,))
        create_user = self.cur.fetchone()
        return create_user is not None and create_user[0] == user_id

    def add_event(self,channel_id, name, description, deadline):
        self.cur.execute(
            "insert into events (channelId, name, description, deadline) values (%s, %s, %s, %s)",
            (channel_id, name, description, deadline)
        )
        self.conn.commit()
        

    def deleteSubcribe(self, userId: int, channelId :int):
        # delete the channel if user is creator of the channel
        if self.check_user_rights(channel_id=channelId, user_id=userId):
            self.cur.execute("DELETE FROM Channels WHERE channelId = %s", (channelId,))
        else:
            self.cur.execute("DELETE FROM Subscriptions WHERE userId = %s and channelId = %s",
                        (userId, channelId))
        self.conn.commit()
        
    def getAllDataByUser(self,userId: int):
        
        # Получение данных для каналов с сортировкой по deadline
        self.cur.execute(f"""
            SELECT DISTINCT 
                Channels.channelId AS channel_id,
                Channels.name AS name,
                CASE WHEN Channels.createdBy = {userId} THEN TRUE ELSE FALSE END AS is_admin
            FROM
                Channels
            FULL JOIN Subscriptions ON Subscriptions.channelId = Channels.channelId
            WHERE Subscriptions.userId = {userId} OR Channels.createdBy = {userId}""")
        channelsData = self.cur.fetchall()

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
        self.cur.execute(f"""SELECT DISTINCT 
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
            LEFT join DoneJobs 
            on Subscriptions.userId = DoneJobs.userId AND Events.eventId = DoneJobs.eventId 
            WHERE 
                Subscriptions.userId = {userId} AND Events.deadline > '{current_time}' AND (DoneJobs.userId is NULL OR DoneJobs.eventId is NULL) 
            ORDER BY 
                Events.deadline;""")
        eventsData =self.cur.fetchall()

        resEventsData = []

        for i in eventsData:
            print(i)
            resEventsData.append(
                dict({
                    "event_id": i[0],
                    "channel_id": i[1],
                    "name": i[2],
                    "description": i[3],
                    "deadline": i[4],
                    "estimated": 300 if i[5] == 0  else i[6]/i[5]
                })
            )
        
        return (resChannelsData, resEventsData)


    def update_event(self,event_id, name, description, deadline):
        self.cur.execute(
            """
            UPDATE Events
            SET name = %s, description = %s, deadline = %s
            WHERE eventId = %s
            """,
            (name, description, deadline, event_id)
        )
        self.conn.commit()


    def check_done(self,user_id, event_id):
        self.cur.execute("SELECT COUNT(*) FROM DoneJobs WHERE userId = %s AND eventId = %s", (user_id, event_id))
        return self.cur.fetchone()[0] == 0


    def perform_event(self, user_id, event_id, time):
        self.cur.execute(
            """
            UPDATE Events 
            SET userCompletedTask = userCompletedTask + 1, sumTime = sumTime + %s
            WHERE eventId = %s
            """,
            (time, event_id)
        )
        self.conn.commit()
        self.cur.execute(
                "INSERT INTO DoneJobs (userId, eventId, time) VALUES (%s, %s, %s)",
                (user_id, event_id, time)
            )
        self.conn.commit()