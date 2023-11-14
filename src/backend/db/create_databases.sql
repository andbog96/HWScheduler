-- Users table
CREATE TABLE Users (
    userId SERIAL PRIMARY KEY,
    login VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Channels table
CREATE TABLE Channels (
    channelId SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    createdBy INT REFERENCES Users(userId)
);

-- Events table
CREATE TABLE Events (
    event_id SERIAL PRIMARY KEY,
    channel_id INT REFERENCES Channels(channelId),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    deadline TIMESTAMP,
    user_completed_task INT DEFAULT 0,
    sum_time INt DEFAULT 0
);

-- Subscriptions table
CREATE TABLE Subscriptions (
    user_id INT REFERENCES Users(userId),
    channel_id INT REFERENCES Channels(channelId),
    PRIMARY KEY (user_id, channel_id)
);

-- DoneJobs table
CREATE TABLE DoneJobs ( 
    userId INT REFERENCES Users(userId),
    eventId INT REFERENCES Events(event_id),
    time INT
);