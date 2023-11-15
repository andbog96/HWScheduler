-- Users
INSERT INTO Users (login, password) VALUES 
    ('user1', 'password1'),
    ('user2', 'password2'),
    ('user3', 'password3');

-- Channels
INSERT INTO Channels VALUES 
    (1, 'Channel 1', 1),
    (2, 'Channel 2', 2),
    (3, 'Channel 3', 3);

-- Events
INSERT INTO Events VALUES 
    (1, 1, 'Event 1', 'Description 1', '2023-01-01', 1, 30),
    (2, 2, 'Event 2', 'Description 2', '2023-02-01', 1, 45),
    (3, 3, 'Event 3', 'Description 3', '2023-03-01', 1, 60);

-- Subscriptions
INSERT INTO Subscriptions VALUES 
    (1, 1),
    (2, 2),
    (3, 3);

-- DoneJobs
INSERT INTO DoneJobs (userId, eventId, time) VALUES 
    (1, 1, 10),
    (2, 2, 20),
    (3, 3, 30);
