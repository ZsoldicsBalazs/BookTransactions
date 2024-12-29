# Library Management System
This project is a Library Management System that manages clients, books, and transactions. It is implemented using the Controller Service Repository pattern , with options for multiple storage methods, including file, XML, in-memory, and SQL database.

Table of Contents:
      Overview
      Technologies Used
      Project Structure
      Key design patterns
      
The Library Management System provides a way to manage library operations such as:

Adding, updating, and retrieving Client information.
Managing Book inventory.
Recording Transactions related to book sales and client interactions.
The project uses the Repository Pattern to abstract data access and storage, allowing multiple storage backends for flexibility and scalability.

## Technologies Used
Java: Main programming language.

Maven: Project build and dependency management.

HikariCP: Connection pool for efficient SQL database connections.

SQL Database: PostgreSQL, configured with a pooled connection.

XML: For alternative data storage.

JUnit: For unit testing.

### Project Structure
The project follows the Controller Service Repository structure. Here’s a breakdown of the main components:

Model: Contains entity classes (Client, Book, Transaction) that represent the data structure.

View: Interfaces or classes (e.g., CLI) to display data and interact with the user.

Controller: Manages the communication between the view and the model.

Repository: Abstracts data storage and retrieval, with implementations for:

File: Stores data in text files.

XML: Stores data in XML files.

In-Memory: For temporary, volatile storage during runtime.

SQL Database: For persistent storage in a SQL database with HikariCP for efficient connection pooling.


### Key Design Patterns

Controller Service Repository Pattern: Separates application logic, user interface, and data to allow easy maintenance and scalability.

Repository Pattern: Provides a flexible data access layer, enabling multiple storage options.

Factory Pattern: Enables dynamic selection of storage types (File/XML/Memory/SQL) in the repository.

Features
CRUD Operations for Clients, Books, and Transactions.

Filtering operations for transactions.

Flexible Data Storage: Choose between file, XML, in-memory, and SQL for storing entity data.

Connection Pooling: SQL connections are managed using HikariCP for efficient database interaction.

