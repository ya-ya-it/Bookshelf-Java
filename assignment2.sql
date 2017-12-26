DROP DATABASE fictionBookshelf;

CREATE DATABASE fictionBookshelf;

USE fictionBookshelf;

-- DROP TABLE fictionBooks;

CREATE TABLE fictionBooks (
	bookId int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	title VARCHAR(50),
    authorName VARCHAR(50),
    fictionGenre VARCHAR(20),
    mainCharacters VARCHAR(50),
    price dec(9,2),
    dateOfPublication DATE,
    bookCover VARCHAR(300)
 );

-- DROP TABLE USERS;
 
 CREATE TABLE users (
	userId int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    phoneNum VARCHAR(50),
    password VARCHAR(300),
    salt BLOB,
    isAdmin BOOLEAN
 );
 
 -- DROP TABLE inventory;
 
 CREATE TABLE inventory (
	inventoryId int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    bookId int,
    amountInStock int,
    amountSold int,
    FOREIGN KEY (bookId) REFERENCES fictionBooks (bookId)
 );
 
-- DROP TABLE SALES; 
 
 CREATE TABLE sales (
	saleId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    amountSold INT,
    dateSold DATE,
    userId int,
    bookId int,
    FOREIGN KEY (userId) REFERENCES users (userId),
    FOREIGN KEY (bookId) REFERENCES fictionBooks (bookId)
 );
 
 INSERT INTO fictionBooks (title, authorName, fictionGenre, mainCharacters, price, dateOfPublication) VALUES
 ('Alice in Wonderland', 'Lewis Carroll', 'ADVENTURE', 'Alice', 19.99, '1865-11-26');
 INSERT INTO fictionBooks (title, authorName, fictionGenre, mainCharacters, price, dateOfPublication) VALUES
 ('The Adventures of Sherlock Holmes', 'Sir Arthur Conan Doyle', 'ADVENTURE', 'Scherlock Holmes', 29.99, '1891-10-12');
 INSERT INTO fictionBooks (title, authorName, fictionGenre, mainCharacters, price, dateOfPublication) VALUES
 ('Harry Potter and the Philosophers Stone', 'J. K. Rowling.', 'ADVENTURE', 'Harry Potter', 9.99, '1997-06-26');
 
 INSERT INTO fictionBooks (title, authorName, fictionGenre, mainCharacters, price, dateOfPublication) VALUES
 ('Alice in Wonderland2', 'Lewis Carroll', 'ADVENTURE', 'Alice', 19.99, '1865-11-26');
 INSERT INTO fictionBooks (title, authorName, fictionGenre, mainCharacters, price, dateOfPublication) VALUES
 ('The Adventures of Sherlock Holmes2', 'Sir Arthur Conan Doyle', 'ADVENTURE', 'Scherlock Holmes', 29.99, '1891-10-12');
 INSERT INTO fictionBooks (title, authorName, fictionGenre, mainCharacters, price, dateOfPublication) VALUES
 ('Harry Potter and the Philosophers Stone2', 'J. K. Rowling.', 'ADVENTURE', 'Harry Potter', 9.99, '1997-06-26');
 
 SELECT * FROM fictionBooks;
 
 INSERT INTO users (username, phoneNum, password, isAdmin) VALUES
 ('Dasha', '705-222-2244', 'password', true);
 INSERT INTO users (username, phoneNum, password, isAdmin) VALUES
 ('User', '705-222-2255', 'password', false);
 
 SELECT * FROM users;
 
 INSERT INTO inventory (bookId, amountInStock, amountSold) VALUES
 (1, 11, 1);
  INSERT INTO inventory (bookId, amountInStock, amountSold) VALUES
 (2, 9, 8);
  INSERT INTO inventory (bookId, amountInStock, amountSold) VALUES
 (3, 1, 4);
  INSERT INTO inventory (bookId, amountInStock, amountSold) VALUES
 (4, 20, 10);
  INSERT INTO inventory (bookId, amountInStock, amountSold) VALUES
 (5, 5, 1);
  INSERT INTO inventory (bookId, amountInStock, amountSold) VALUES
 (6, 4, 3);
 
  INSERT INTO inventory (bookId, amountInStock, amountSold) VALUES  ((SELECT MAX(bookId) FROM fictionBooks), 2, 1);
 
 SELECT * FROM inventory;
 
 INSERT INTO SALES (amountSold, dateSold, userId, bookId) VALUES
 (1, '2016-05-01', 1, 1);
  INSERT INTO SALES (amountSold, dateSold, userId, bookId) VALUES
 (1, '2016-05-02', 1, 2);
  INSERT INTO SALES (amountSold, dateSold, userId, bookId) VALUES
 (1, '2016-07-10', 1, 1);
  INSERT INTO SALES (amountSold, dateSold, userId, bookId) VALUES
 (1, '2017-05-01', 1, 4);
  INSERT INTO SALES (amountSold, dateSold, userId, bookId) VALUES
 (1, '2017-05-10', 1, 5);
  INSERT INTO SALES (amountSold, dateSold, userId, bookId) VALUES
 (4, '2017-08-11', 1, 1);
 INSERT INTO SALES (amountSold, dateSold, userId, bookId) VALUES
 (6, '2017-012-01', 1, 2);
  INSERT INTO SALES (amountSold, dateSold, userId, bookId) VALUES
 (13, '2017-03-10', 1, 6);
  INSERT INTO SALES (amountSold, dateSold, userId, bookId) VALUES
 (5, '2017-01-11', 1, 1);
 
 SELECT * FROM sales;
