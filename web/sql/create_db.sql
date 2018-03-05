CREATE TABLE Departments (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    title VARCHAR(30) NOT NULL
);

CREATE TABLE Notes (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    contents TEXT,
    note_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    department_id INT REFERENCES Departments(id),
    title TEXT,
    importance INT,
    img_url TEXT
);

CREATE TABLE Permissions (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    permission TEXT NOT NULL
);

CREATE TABLE Jobs(
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    title VARCHAR(30) NOT NULL,
    department_id INT REFERENCES Departments(id)
);

CREATE TABLE Users (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(30) NOT NULL,
    pw_hash CHAR(60),
    username VARCHAR(30) UNIQUE,
    email TEXT,
    phone_number VARCHAR(20),
    permissions_id INT REFERENCES Permissions(id),
    job_id INT REFERENCES Jobs(id),
    img_url TEXT
);

CREATE TABLE Note_Receivers (
    note_id INT NOT NULL REFERENCES Notes(id),
    user_id INT NOT NULL REFERENCES Users(id),
    PRIMARY KEY (note_id, user_id)
);