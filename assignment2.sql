DROP DATABASE fictionBookshelf;

CREATE DATABASE fictionBookshelf;

USE fictionBookshelf;

DROP TABLE fictionBooks;

CREATE TABLE fictionBooks (
	bookId int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	title VARCHAR(50),
    authorName VARCHAR(50),
    fictionGenre VARCHAR(20),
    mainCharacters VARCHAR(50),
    price dec(9,2),
    dateOfPublication DATE,
    amountInStock INT,
    amountSold INT,
    dateSold DATE,
    bookCover VARCHAR(300),
    userId INT NOT NULL,
    FOREIGN KEY (userId) REFERENCES users (userId)
 );
 
 DROP TABLE USERS;
 
 CREATE TABLE users (
	userId int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    phoneNum VARCHAR(50),
    password VARCHAR(300),
    salt BLOB,
    isAdmin BOOLEAN
 );
 
 INSERT INTO fictionBooks (title, authorName, fictionGenre, mainCharacters, price, dateOfPublication, amountInStock, amountSold, dateSold,  userId) VALUES
 ('Alice in Wonderland', 'Lewis Carroll', 'ADVENTURE', 'Alice', 19.99, '1865-11-26',11, 2, '2016-05-24' , 1);
 INSERT INTO fictionBooks (title, authorName, fictionGenre, mainCharacters, price, dateOfPublication, amountInStock, amountSold, dateSold, userId) VALUES
 ('The Adventures of Sherlock Holmes', 'Sir Arthur Conan Doyle', 'ADVENTURE', 'Scherlock Holmes', 29.99, '1891-10-12', 12, 4, '2016-07-21',  1);
 INSERT INTO fictionBooks (title, authorName, fictionGenre, mainCharacters, price, dateOfPublication, amountInStock, amountSold, dateSold, userId) VALUES
 ('Harry Potter and the Philosophers Stone', 'J. K. Rowling.', 'ADVENTURE', 'Harry Potter', 9.99, '1997-06-26', 8, 4, '2016-03-18', 1);
 
 SELECT * FROM fictionBooks;
 
 INSERT INTO users (username, phoneNum, password, isAdmin) VALUES
 ('Dasha', '705-222-2244', 'password', true);
 INSERT INTO users (username, phoneNum, password, isAdmin) VALUES
 ('User', '705-222-2255', 'password', false);
 
 SELECT * FROM users;
 
 DELETE FROM users
 WHERE userId = 3;
 
 UPDATE users
 SET isAdmin = 1
 WHERE userId = 1;
