CREATE TABLE Customers(
	CustomerID INT PRIMARY KEY NOT NULL, -- needed for FOREIGN KEY reference
	FirstName TEXT NOT NULL,
	LastName TEXT NULL
);
CREATE TABLE Orders(
	OrderID INT NOT NULL,
	CustomerID INT NULL,
	OrderDate DATE NULL,
	OrderAmount DOUBLE NULL
);
CREATE TABLE Refunds(
	RefundID INT NOT NULL,
	OrderID INT NULL,
	RefundDate DATE NULL,
	RefundAmount DOUBLE NULL
);
