DROP TABLE IF EXISTS users, channels, events, subscriptions, donejobs;

-- Users table
CREATE TABLE Users (
    userId SERIAL PRIMARY KEY,
    login VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Channels table
CREATE TABLE Channels (
    channelId SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    createdBy INT REFERENCES Users(userId) ON DELETE CASCADE
);

-- Events table
CREATE TABLE Events (
    eventId SERIAL PRIMARY KEY,
    channelId INT REFERENCES Channels(channelId) ON DELETE CASCADE,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    deadline TIMESTAMP,
    userCompletedTask INT DEFAULT 0,
    sumTime INt DEFAULT 0
);

-- Subscriptions table
CREATE TABLE Subscriptions (
    userId INT REFERENCES Users(userId) ON DELETE CASCADE,
    channelId INT REFERENCES Channels(channelId) ON DELETE CASCADE,
    PRIMARY KEY (userId, channelId)
);

-- DoneJobs table
CREATE TABLE DoneJobs ( 
    userId INT REFERENCES Users(userId) ON DELETE CASCADE,
    eventId INT REFERENCES Events(eventId) ON DELETE CASCADE,
    time INT
);