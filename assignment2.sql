DROP DATABASE fictionBookshelf;

CREATE DATABASE fictionBookshelf;

USE fictionBookshelf;

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
    bookCover VARCHAR(300)
 );
 
 CREATE TABLE users (
	userId int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    contactInfo VARCHAR(50),
    password VARCHAR(100),
    salt BLOB,
    isAdmin BOOLEAN
 );
 
 INSERT INTO fictionBooks (title, authorName, fictionGenre, mainCharacters, price, dateOfPublication, amountInStock, amountSold) VALUES
 ('Alice in Wonderland', 'Lewis Carroll', 'ADVENTURE', 'Alice', 19.99, '1865-11-26', 11, 2);
 INSERT INTO fictionBooks (title, authorName, fictionGenre, mainCharacters, price, dateOfPublication, amountInStock, amountSold) VALUES
 ('The Adventures of Sherlock Holmes', 'Sir Arthur Conan Doyle', 'ADVENTURE', 'Scherlock Holmes', 29.99, '1891-10-12', 12, 4);
 INSERT INTO fictionBooks (title, authorName, fictionGenre, mainCharacters, price, dateOfPublication, amountInStock, amountSold) VALUES
 ('Harry Potter and the Philosophers Stone', 'J. K. Rowling.', 'ADVENTURE', 'Harry Potter', 9.99, '1997-06-26', 8, 4);
 
 SELECT * FROM fictionBooks;
 
 INSERT INTO users (username, contactInfo, password, isAdmin) VALUES
 ('Dasha', 'ya-ya-it@yandex.com', 'password', true);
 INSERT INTO users (username, contactInfo, password, isAdmin) VALUES
 ('User', 'test@test.com', 'password', false);
 
 SELECT * FROM users;