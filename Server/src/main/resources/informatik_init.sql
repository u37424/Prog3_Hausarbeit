DROP TABLE IF EXISTS Book_Authors;
DROP TABLE IF EXISTS Book_Categories;
DROP TABLE IF EXISTS Books;
DROP TABLE IF EXISTS Authors;
DROP TABLE IF EXISTS Publishers;
DROP TABLE IF EXISTS Categories;

CREATE TABLE Categories (
  Category_ID INTEGER PRIMARY KEY AUTO_INCREMENT,
  Name VARCHAR(255) NOT NULL
);

INSERT INTO Categories (Category_ID, Name)
VALUES (1, 'Algorithms');

INSERT INTO Categories (Category_ID, Name)
VALUES (2, 'Theory');

INSERT INTO Categories (Category_ID, Name)
VALUES (3, 'History');

INSERT INTO Categories (Category_ID, Name)
VALUES (4, 'Interviews');

INSERT INTO Categories (Category_ID, Name)
VALUES (5, 'Design Patterns');

INSERT INTO Categories (Category_ID, Name)
VALUES (6, 'Software Development');

INSERT INTO Categories (Category_ID, Name)
VALUES (7, 'Web Development');

INSERT INTO Categories (Category_ID, Name)
VALUES (8, 'Programming');

INSERT INTO Categories (Category_ID, Name)
VALUES (9, 'Computer Science');

INSERT INTO Categories (Category_ID, Name)
VALUES (10, 'Artificial Intelligence');

CREATE TABLE Publishers (
  Publisher_ID INTEGER PRIMARY KEY AUTO_INCREMENT,
  Name VARCHAR(255) NOT NULL,
  Year_of_Foundation INTEGER,
  Main_Country VARCHAR(255) NOT NULL
);

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (1, 'Addison-Wesley', 1807, 'United States');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (2, 'O''Reilly Media', 1978, 'United States');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (3, 'Prentice Hall', 1844, 'United States');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (4, 'Wiley', 1807, 'United States');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (5, 'Springer', 1842, 'Germany');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (6, 'Morgan Kaufmann', 1984, 'United States');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (7, 'Manning Publications', 1983, 'United States');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (8, 'Cengage Learning', 1994, 'United States');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (9, 'Pearson Education', 1844, 'United Kingdom');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (10, 'McGraw-Hill Education', 1888, 'United States');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (11, 'Hachette Livre', 1826, 'France');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (12, 'Cambridge University Press', 1534, 'United Kingdom');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (13, 'Gruyter', 1749, 'Germany');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (14, 'Oldenbourg Verlag', 1848, 'Germany');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (15, 'Hanser Verlag', 1950, 'Germany');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (16, 'Vieweg+Teubner Verlag', 1891, 'Germany');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (17, 'Bonnier Technology Group', 2002, 'Germany');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (18, 'Eyrolles', 1961, 'France');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (19, 'O''Reilly Media France', 2001, 'France');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (20, 'Vuibert', 1995, 'France');

INSERT INTO Publishers (Publisher_ID, Name, Year_of_Foundation, Main_Country)
VALUES (21, 'Dunod', 1848, 'France');

CREATE TABLE Books (
  ISBN VARCHAR(255) PRIMARY KEY,
  Title VARCHAR(255) NOT NULL,
  Publisher_ID INTEGER,
  Release_Year INTEGER,
  Pages INTEGER NOT NULL,
  Rating FLOAT(2,1),
  Description VARCHAR(255),
  FOREIGN KEY (Publisher_ID) REFERENCES Publishers(Publisher_ID)
);

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-201-89683-4', 'The Art of Computer Programming', 1, 1968, 672, 4.5, 'A comprehensive guide to programming techniques and algorithms.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-262-03293-4', 'Introduction to the Theory of Computation', 2, 1997, 544, 4.0, 'An introduction to formal languages and automata theory.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-7356-4871-9', 'Code: The Hidden Language of Computer Hardware and Software', 3, 2000, 320, 4.5, 'A look at the fundamental principles of computers and how they work.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-9847828-6-7', 'Cracking the Coding Interview', 4, 2015, 664, 4.0, 'A guide to preparing for technical interviews in the software industry.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-201-63361-0', 'Design Patterns: Elements of Reusable Object-Oriented Software', 1, 1994, 395, 4.5, '	A collection of design patterns for creating object-oriented software.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0132350884', 'Clean Code: A Handbook of Agile Software Craftsmanship', 5, 2008, 464, 4.5, 'Best practices for writing clean and maintainable code.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-1449358063', 'JavaScript: The Good Parts', 4, 2008, 172, 4.5, 'A guide to the most useful features of the JavaScript language.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-596-52068-7', 'Head First Design Patterns', 4, 2004, 672, 4.5, 'A guide to using design patterns to create object-oriented software.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-596-10003-5', 'Learning Python', 4, 2006, 896, 4.5, 'A tutorial on the Python programming language.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-596-00128-3', 'Programming Python', 4, 1996, 1224, 4.0, 'A tutorial on the Python programming language.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-596-52069-4', 'Head First HTML with CSS & XHTML', 4, 2004, 672, 4.0, 'A guide to creating websites using HTML and CSS.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-596-15907-5', 'Java in a Nutshell', 1, 2006, 576, 3.5, 'A reference guide to the Java programming language.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-596-51517-6', 'CSS: The Missing Manual', 5, 2009, 504, 4.0, 'A guide to using Cascading Style Sheets (CSS) to style and layout web pages.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-1-491-91712-7', 'JavaScript and JQuery: Interactive Front-End Web Development', 5, 2014, 504, 4.5, 'Interactive Front-End Web Development using JavaScript and JQuery.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-13-235088-4', 'Computer Science: An Overview', 3, 2012, 1088, 4.5, 'An overview of computer science concepts.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-321-58499-2', 'Introduction to the Theory of Computation', 1, 2011, 704, 4.5, 'An introduction to the theory of computation.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-262-03384-6', 'Introduction to Artificial Intelligence', 2, 1992, 464, 4.0, 'An introduction to artificial intelligence.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-262-24029-6', 'Algorithms', 2, 2001, 992, 4.5, 'A guide to algorithms and data structures.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-262-05259-7', 'The Structure and Interpretation of Computer Programs', 2, 1984, 432, 4.5, 'A tutorial on the Scheme programming language and computer science concepts.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-1-492-03457-7', 'Python Crash Course', 5, 2015, 560, 4.5, 'A crash course in the Python programming language.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-13-602470-9', 'Effective Java', 3, 2008, 416, 4.5, 'A guide to best practices in Java programming.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-13-402951-8', 'Introduction to Computer Science Using Python', 3, 2017, 816, 4.0, 'An introduction to computer science concepts using the Python programming language.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-13-602471-6', 'Java Generics and Collections', 3, 2006, 312, 4.0, 'A guide to using generics and collections in Java.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-262-01063-6', 'Introduction to Computer Science', 2, 2011, 576, 4.0, 'An introduction to computer science concepts.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-1-491-91712-9', 'JavaScript and JQuery: Interactive Front-End Web Development', 5, 2014, 504, 4.5, 'Interactive Front-End Web Development using JavaScript and JQuery.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-596-10109-5', 'Python Pocket Reference', 4, 2009, 176, 3.5, 'A quick reference guide to the Python programming language.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-596-15806-1', 'Java Cookbook', 4, 2005, 880, 4.0, 'A cookbook of recipes for common tasks in the Java programming language.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-1-491-90766-7', 'JavaScript: The Definitive Guide', 5, 2011, 1088, 4.5, 'A comprehensive guide to the JavaScript programming language.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-262-53305-9', 'The Ethics of Artificial Intelligence', 2, 2020, 256, 4.5, 'A discussion on the ethical implications of artificial intelligence.');

INSERT INTO Books (ISBN, Title, Publisher_ID, Release_Year, Pages, Rating, Description)
VALUES ('978-0-262-58307-6', 'Superintelligence: Paths, Dangers, Strategies', 2, 2014, 512, 4.0, 'A discussion on the potential risks and benefits of artificial superintelligence and strategies to manage it.');

CREATE TABLE Authors (
  Author_ID INTEGER PRIMARY KEY AUTO_INCREMENT,
  First_Name VARCHAR(255) NOT NULL,
  Last_Name VARCHAR(255) NOT NULL,
  Alias VARCHAR(255),
  Birthday DATE NOT NULL,
  Age INTEGER
);

ALTER TABLE Authors
ADD CHECK (Birthday IS NOT NULL AND Birthday <= CURRENT_DATE);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (1, 'Bjarne', 'Stroustrup', 'B. Stroustrup', '1950-12-30', 71);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (2, 'James', 'Gosling', 'J. Gosling', '1955-05-19', 67);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (3, 'Guido', 'van Rossum', 'G. van Rossum', '1956-01-31', 66);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (4, 'Dennis', 'Ritchie', 'D. Ritchie', '1941-09-09', 81);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (5, 'Linus', 'Torvalds', 'L. Torvalds', '1969-12-28', 53);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (6, 'Alan', 'Turing', 'A. Turing', '1912-06-23', 110);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (7, 'John', 'Backus', 'J. Backus', '1924-12-03', 98);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (8, 'Eric', 'Freeman', 'E. Freeman', '1970-01-01', 51);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (9, 'Elisabeth', 'Freeman', 'E. Freeman', '1973-05-13', 48);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (10, 'Mark', 'Lutz', 'M. Lutz', '1968-03-02', 54);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (11, 'Elizabeth', 'Castro', 'E. Castro', '1953-07-29', 69);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (12, 'David', 'Flanagan', 'D. Flanagan', '1962-05-13', 60);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (13, 'David', 'Sawyer McFarland', 'D. S. McFarland', '1973-08-06', 48);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (14, 'John', 'Resig', 'J. Resig', '1984-08-20', 38);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (15, 'David', 'J. Malan', 'D. J. Malan', '1977-01-01', 45);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (16, 'Michael', 'Sipser', 'M. Sipser', '1953-07-01', 69);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (17, 'Stuart', 'Russell', 'S. Russell', '1962-09-23', 60);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (18, 'Robert', 'Sedgewick', 'R. Sedgewick', '1946-08-03', 76);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (19, 'Hal', 'Abelson', 'H. Abelson', '1947-05-01', 75);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (20, 'Eric', 'Matthes', 'E. Matthes', '1980-08-01', 42);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (21, 'Joshua', 'Bloch', 'J. Bloch', '1961-08-28', 61);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (22, 'John', 'Zelle', 'J. Zelle', '1958-07-01', 64);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (23, 'Maurice', 'Herlihy', 'M. Herlihy', '1958-01-01', 64);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (24, 'John', 'Impagliazzo', 'J. Impagliazzo', '1947-08-01', 75);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (25, 'Jon', 'Duckett', 'J. Duckett', '1983-01-01', 39);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (26, 'Mark', 'Lutz', 'M. Lutz', '1968-03-02', 54);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (27, 'Ian', 'F. Darwin', 'I. F. Darwin', '1955-08-01', 67);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (28, 'David', 'Flanagan', 'D. Flanagan', '1962-05-13', 60);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (29, 'Douglas', 'Crockford', 'D. Crockford', '1955-07-01', 67);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (30, 'Nick', 'Bostrom', 'N. Bostrom', '1973-03-09', 49);

INSERT INTO Authors (Author_ID, First_Name, Last_Name, Alias, Birthday, Age)
VALUES (31, 'Max', 'Tegmark', 'M. Tegmark', '1967-05-26', 55);

CREATE TABLE Book_Authors (
  ISBN CHAR(255) REFERENCES Books(ISBN),
  Author_ID INTEGER REFERENCES Authors(Author_ID),
  PRIMARY KEY (ISBN, Author_ID)
);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-201-89683-4', 1);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-262-03293-4', 2);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-7356-4871-9', 3);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-9847828-6-7', 4);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-201-63361-0', 5);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0132350884', 6); 

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-1449358063', 7); 

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-596-52068-7', 8);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-596-10003-5', 9);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-596-00128-3', 10);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-596-52069-4', 11);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-596-15907-5', 12);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-596-51517-6', 13);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-1-491-91712-7', 14);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-13-235088-4', 15);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-321-58499-2', 16);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-262-03384-6', 17);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-262-24029-6', 18);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-262-05259-7', 19);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-1-492-03457-7', 20);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-13-602470-9', 21);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-13-402951-8', 22);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-13-602471-6', 23);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-262-01063-6', 24);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-1-491-91712-9', 25);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-596-10109-5', 26);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-596-15806-1', 27);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-596-00671-3', 28);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-1-491-90766-7', 29);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-262-53305-9', 30);

INSERT INTO Book_Authors (ISBN, Author_ID)
VALUES ('978-0-262-58307-6', 31);

CREATE TABLE Book_Categories (
  ISBN CHAR(255) REFERENCES Books(ISBN),
  Category_ID INTEGER REFERENCES Categories(Category_ID),
  PRIMARY KEY (ISBN, Category_ID)
);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-201-89683-4', 1);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-262-03293-4', 2);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-7356-4871-9', 3);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-9847828-6-7', 4);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-201-63361-0', 5);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0132350884', 6); 

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-1449358063', 7); 

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-596-52068-7', 5);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-596-10003-5', 8);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-596-00128-3', 8);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-596-52069-4', 7);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-596-15907-5', 8);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-596-51517-6', 7);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-1-491-91712-7', 7);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-13-235088-4', 9);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-321-58499-2', 2);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-262-03384-6', 10);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-262-24029-6', 1);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-262-05259-7', 8);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-1-492-03457-7', 8);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-13-602470-9', 8);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-13-402951-8', 9);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-13-602471-6', 8);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-262-01063-6', 9);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-1-491-91712-9', 7);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-596-10109-5', 8);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-596-15806-1', 8);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-596-00671-3', 8);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-1-491-90766-7', 7);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-262-53305-9', 10);

INSERT INTO Book_Categories (ISBN, Category_ID)
VALUES ('978-0-262-58307-6', 2);