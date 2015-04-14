# SQLLite join lab

## Goal

In this lab, you're going to download the [Chinook data set](https://chinookdatabase.codeplex.com/). To see what the data looks like, take a look at the [Chinook schema](https://chinookdatabase.codeplex.com/wikipage?title=Chinook_Schema&referringTitle=Home).

## A warm-up exercise

Before we get started though, let's lock in your understanding of joins where they trivial example. Here are two tables `A` and `B` with some data where table `B` has rows without matching `ID`s and also has some repeated `ID`s:

```sql
create table A(ID INTEGER, letter STRING);
create table B(ID INTEGER, number STRING);

insert into A VALUES (1, 'a');
insert into A VALUES (2, 'b');
insert into A VALUES (3, 'c');

insert into B VALUES (1, '100');
insert into B VALUES (1, '111'); -- repeated ID
insert into B VALUES (2, '200');
insert into B VALUES (3, '300');
insert into B VALUES (4, '400');
insert into B VALUES (5, '500');
```

Visually, those tables look like this:

<table>
<tr>
<td>
| ID | letter |
|--------|--------|
|  1      | a       |
| 2 | b|
| 3 | c|
</td>
<td>
| ID | number |
|--------|--------|
| 1 | 100 |
| 1 | 111 |
| 2 | 200 |
| 3 | 300 |
| 4 | 400 |
| 5 | 500 |
</td>
</tr>
</table>

Type in the following queries to see what they do:

* Cross product
```sql
select * from A,B;
```

* Match up the rows from A and B whose IDs are the same
```sql
select * from A inner join B on A.ID=B.ID;
```

* List all rows from A and that have matching counterparts from B
```sql
select * from A left join B on A.ID=B.ID;
```

* List all rows from B and that have matching counterparts from A
```sql
select * from B left join A on A.ID=B.ID;
```

## Setup

To get things set up, do the following:
 
1. I have compressed and stored the Chinook data in an [SQL file suitable for sqlite3](https://github.com/parrt/cs601/blob/master/labs/resources/Chinook_Sqlite.sql.zip?raw=true).  Download that and unzip it.
2. Load the database from the SQL:
```bash
$ sqlite3 /tmp/chinook.db < Chinook_Sqlite.sql
Error: near line 1: near "": syntax error
```

The SQL file starts out with the schema and then has a number of `INSERT` statements that fill the tables. For example, here are the first two tables:

```sql
CREATE TABLE [Album]
(
    [AlbumId] INTEGER  NOT NULL,
    [Title] NVARCHAR(160)  NOT NULL,
    [ArtistId] INTEGER  NOT NULL,
    CONSTRAINT [PK_Album] PRIMARY KEY  ([AlbumId]),
    FOREIGN KEY ([ArtistId]) REFERENCES [Artist] ([ArtistId]) 
		ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE [Artist]
(
    [ArtistId] INTEGER  NOT NULL,
    [Name] NVARCHAR(120),
    CONSTRAINT [PK_Artist] PRIMARY KEY  ([ArtistId])
);
```

## Questions

Now that we have the data in there, you must answer the following questions:

1. How many unique albums are there and how many customers are there?
1. Total amount from all invoices.
1. How long is the longest track in seconds? Include the name of the track in your answer.
1. How many customers do not list their company?
1. Which artists did not make any albums at all?
