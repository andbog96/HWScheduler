-- Users
INSERT INTO Users (login, password) VALUES 
    ('user1', 'password1'),
    ('user2', 'password2'),
    ('user3', 'password3');

-- Channels
INSERT INTO Channels (name, createdBy) VALUES 
    ('Channel 1', 1),
    ('Channel 2', 2),
    ('Channel 3', 3),
    ('db_itmo', 2),
    ('fp_itmo', 1),
    ('ml-itmo', 1),
    ('cpp-itmo', 2);

-- Events
INSERT INTO Events (channelId, name, description, deadline, userCompletedTask, sumTime) VALUES 
    (1, 'Event 1', 'Description 1', '2023-01-01', 1, 30),
    (2, 'Event 2', 'Description 2', '2023-02-01', 1, 45),
    (3, 'Event 3', 'Description 3', '2023-03-01', 1, 60),
    (4, 'LR1', 'Do or Die', '2023-11-20', 0, 0),
    (5, 'LR1', 'Church', '2023-11-20', 0, 0),
    (5, 'LR2', 'Foldl', '2023-11-25', 0, 0),
    (6, 'LR1', 'K-nearest','2023-11-20', 1, 150),
    (6, 'LR2', 'Perceptron', '2023-11-23', 1, 100),
    (7, 'LR1', 'cls-07', '2023-11-20', 0, 0),
    (7, 'LR2', 'bignum', '2023-11-30', 1, 25);

-- Subscriptions
INSERT INTO Subscriptions VALUES 
    (1, 1),
    (2, 2),
    (3, 3),
    (1, 4),
    (1, 5),
    (1, 6),
    (1, 7);

-- DoneJobs
INSERT INTO DoneJobs (userId, eventId, time) VALUES 
    (1, 1, 10),
    (2, 2, 20),
    (3, 3, 30);