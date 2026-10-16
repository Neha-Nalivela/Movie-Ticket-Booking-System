# 🎬 Movie Ticket Booking System

## 📌 Project Overview

The **Movie Ticket Booking System** is a Java-based desktop application developed using Java Swing and MySQL. It allows users to select movies, choose theaters, select available seats, calculate ticket prices, simulate payments, and generate transaction bills.

The application includes user registration and login functionality, along with database management for movies, theaters, bookings, and payments.

## ✨ Features

- **User Authentication:** Login and registration for users and administrators.
- **Movie Selection:** Choose from the available movies stored in the database.
- **Theater Selection:** Select a theater from the available locations.
- **Seat Selection:** Interactive 10 × 10 seat layout for selecting seats.
- **Seat Availability:** Previously booked seats are disabled to prevent reselection.
- **Automatic Price Calculation:** Calculates the total ticket cost based on the number of selected seats.
- **Payment Simulation:** Accepts card details and simulates payment processing.
- **Booking Management:** Stores booking details in the MySQL database.
- **Payment Records:** Saves payment method, amount, and payment status.
- **Transaction Bill:** Generates a bill containing the customer name, movie, theater, seats, date, total price, and payment status.
- **Database Integration:** Stores and retrieves information using MySQL.

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java | Core application development |
| Java Swing | Graphical user interface |
| AWT | GUI layouts and colors |
| MySQL | Database management |
| JDBC | Java–MySQL connectivity |
| IntelliJ IDEA / Eclipse / NetBeans | Development environment |

## 📋 Prerequisites

Before running the application, ensure that you have installed:

- Java JDK 15 or later (the code uses text blocks).
- MySQL Server 8.0 or a compatible version.
- MySQL Connector/J JDBC driver.
- A Java IDE such as Eclipse, IntelliJ IDEA, or NetBeans.

## 🗄️ Database Configuration

The application connects to a MySQL database named `movie_db`.

Create the database using the following SQL command:

```sql
CREATE DATABASE movie_db;
```

Update the database connection details in the Java source code according to your local MySQL configuration:

```java
conn = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/movie_db",
    "root",
    "YOUR_MYSQL_PASSWORD"
);
```

Replace `YOUR_MYSQL_PASSWORD` with your own MySQL password.

The application automatically creates the following tables when it starts:

1. **Movies** – Stores movie titles, genres, and durations.
2. **Theaters** – Stores theater locations and capacities.
3. **Bookings** – Stores customer details, selected seats, and total prices.
4. **Payments** – Stores payment methods, amounts, and payment statuses.
5. **Users** – Stores usernames, passwords, and user roles.

Sample movies, theaters, and login accounts are inserted automatically when their respective tables are empty.

## ▶️ How to Run the Project

1. Clone or download this repository.
2. Open the project in your preferred Java IDE.
3. Install and configure MySQL Server.
4. Create the `movie_db` database.
5. Add the MySQL Connector/J driver to the project's classpath.
6. Update the JDBC connection URL, username, and password.
7. Open `MovieTicketBookingSystem.java`.
8. Run the application using the IDE or the following command, provided the compiled classes and JDBC driver are configured correctly:

```bash
java -cp ".;mysql-connector-j.jar" MovieTicketBookingSystem
```

On Linux or macOS, use `:` instead of `;` in the classpath separator.

## 🔐 Sample Login Credentials

The following sample accounts are inserted when the Users table is empty.

| Role | Username | Password |
|---|---|---|
| Admin | `admin` | `admin123` |
| User | `user` | `user123` |

These are demonstration credentials, not secure production accounts. The current application authenticates users but does not implement separate admin and user dashboards.

## 🎟️ Application Workflow

1. Launch the application.
2. Register an account or log in.
3. Select a movie and theater.
4. Enter the candidate's name.
5. Select one or more available seats.
6. View the automatically calculated ticket price.
7. Enter payment details in the payment dialog.
8. Confirm the simulated payment.
9. Save the booking and payment details to MySQL.
10. View the generated transaction bill.

## 💰 Ticket Pricing

The ticket price is fixed at ₹150 per seat.

**Formula:**

`Total Price = Number of Selected Seats × ₹150`

For example, booking three seats costs ₹450.

## 🧾 Sample Transaction Bill

```text
---------- Transaction Bill ----------
Candidate Name: Rahul
Movie: Inception
Theater: Downtown Cinema
Date: [Transaction Date]
Seats: 1-1, 1-2
Total Price: ₹300
Payment Method: Credit/Debit Card
Payment Status: Paid
-------------------------------------
Thank you for booking with us!
```

## 📁 Project Structure

```text
MovieTicketBookingSystem/
│
├── MovieTicketBookingSystem.java
└── README.md
```

The project uses a single Java source file containing the GUI, database operations, authentication, seat selection, payment simulation, and bill generation.

## 🔮 Future Enhancements

- Implement secure password hashing.
- Introduce separate administrator and user dashboards.
- Add movie posters, showtimes, and multiple pricing categories.
- Integrate a real payment gateway.
- Add booking history and ticket cancellation.
- Generate downloadable PDF tickets.
- Improve seat booking concurrency and transaction handling.
- Add input validation and stronger error handling.
- Store payment status and booking information using database transactions.
- Add automated testing and improve application security.

## ⚠️ Limitations

- Payments are simulated; no actual money is transferred.
- The current implementation uses plain-text passwords and should not be deployed as-is.
- User roles are stored but are not used to enforce separate permissions.
- Theater capacity is stored, but the seat layout is fixed at 100 seats.
- Seat availability is checked against existing bookings, but simultaneous bookings are not protected by database-level seat constraints.
- Card details are validated only for empty fields and are not processed by a payment provider.

## 👩‍💻 Author

Developed as a Java desktop application project demonstrating GUI programming, JDBC connectivity, relational database operations, and basic ticket booking workflows.

## 📄 License

This project is available for educational and learning purposes. Add a specific open-source license if you intend to distribute or reuse it under formal licensing terms.
