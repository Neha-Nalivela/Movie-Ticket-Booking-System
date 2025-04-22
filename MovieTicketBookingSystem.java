import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieTicketBookingSystem {
    private JFrame frame;
    private JComboBox<String> movieDropdown;
    private JComboBox<String> theaterDropdown;
    private JPanel seatSelectionPanel;
    private JTextField candidateNameField;
    private JLabel totalPriceLabel;
    private JButton bookButton;
    private Connection conn;

    private List<JToggleButton> seatButtons = new ArrayList<>();
    private final int COST_PER_SEAT = 150;

    public MovieTicketBookingSystem() {
        initializeDatabase();
        createTables();
        initializeGUI();
    }

    // Initialize database connection
    private void initializeDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_db", "root", "Ne@ha#123");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage());
            System.exit(1);
        }
    }

    // Create tables and add sample data
    private void createTables() {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Movies (
                        MovieID INT PRIMARY KEY AUTO_INCREMENT,
                        Title VARCHAR(100) NOT NULL,
                        Genre VARCHAR(50) NOT NULL,
                        Duration INT NOT NULL
                    );
            """);

            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Theaters (
                        TheaterID INT PRIMARY KEY AUTO_INCREMENT,
                        Location VARCHAR(100) NOT NULL,
                        Capacity INT NOT NULL
                    );
            """);

            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Bookings (
                        BookingID INT PRIMARY KEY AUTO_INCREMENT,
                        CandidateName VARCHAR(100) NOT NULL,
                        MovieID INT NOT NULL,
                        TheaterID INT NOT NULL,
                        SeatNumbers VARCHAR(255) NOT NULL,
                        TotalPrice INT NOT NULL,
                        FOREIGN KEY (MovieID) REFERENCES Movies(MovieID),
                        FOREIGN KEY (TheaterID) REFERENCES Theaters(TheaterID)
                    );
            """);
            stmt.executeUpdate("""
            	    CREATE TABLE IF NOT EXISTS Payments (
            	        PaymentID INT PRIMARY KEY AUTO_INCREMENT,
            	        BookingID INT NOT NULL,
            	        PaymentMethod VARCHAR(50) NOT NULL,
            	        AmountPaid INT NOT NULL,
            	        PaymentStatus VARCHAR(50) NOT NULL,
            	        FOREIGN KEY (BookingID) REFERENCES Bookings(BookingID)
            	    );
            	""");
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Users (
                        UserID INT PRIMARY KEY AUTO_INCREMENT,
                        Username VARCHAR(50) UNIQUE NOT NULL,
                        Password VARCHAR(50) NOT NULL,
                        Role VARCHAR(10) NOT NULL
                    );
                """);

            addSampleData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error creating tables: " + e.getMessage());
        }
    }

    // Add sample data if tables are empty
    private void addSampleData() {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Movies");
            rs.next();
            if (rs.getInt(1) == 0) {
                stmt.executeUpdate("""
                        INSERT INTO Movies (Title, Genre, Duration)
                        VALUES ('Inception', 'Sci-Fi', 148),
                               ('Titanic', 'Romance', 195),
                               ('The Dark Knight', 'Action', 152);
                """);
            }

            rs = stmt.executeQuery("SELECT COUNT(*) FROM Theaters");
            rs.next();
            if (rs.getInt(1) == 0) {
                stmt.executeUpdate("""
                        INSERT INTO Theaters (Location, Capacity)
                        VALUES ('Downtown Cinema', 100),
                               ('Uptown Theater', 100),
                               ('Central Plaza', 100);
                """);
            }
                    // Insert movies, theaters...

                    rs = stmt.executeQuery("SELECT COUNT(*) FROM Users");
                    rs.next();
                    if (rs.getInt(1) == 0) {
                        stmt.executeUpdate("""
                            INSERT INTO Users (Username, Password, Role)
                            VALUES ('admin', 'admin123', 'admin'),
                                   ('user', 'user123', 'user');
                        """);
               }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error inserting sample data: " + e.getMessage());
        }
    }
    private void showLoginScreen() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(400, 250);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (authenticateUser(username, password)) {
                loginFrame.dispose(); // Close login screen
                initializeGUI();      // Open main application
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            loginFrame.dispose(); // Close login screen
            showRegisterScreen(); // Open registration screen
        });

        loginFrame.add(usernameLabel);
        loginFrame.add(usernameField);
        loginFrame.add(passwordLabel);
        loginFrame.add(passwordField);
        loginFrame.add(loginButton);
        loginFrame.add(registerButton);

        loginFrame.setLocationRelativeTo(null); // Center the frame
        loginFrame.setVisible(true);
    }
    private void showRegisterScreen() {
        JFrame registerFrame = new JFrame("Register");
        registerFrame.setSize(400, 250);
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerFrame.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel usernameLabel = new JLabel("New Username:");
        JTextField usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("New Password:");
        JPasswordField passwordField = new JPasswordField();

        JLabel roleLabel = new JLabel("Role (user/admin):");
        JTextField roleField = new JTextField();

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String role = roleField.getText().trim().toLowerCase();

            if (registerUser(username, password, role)) {
                JOptionPane.showMessageDialog(registerFrame, "Registration successful! Please log in.");
                registerFrame.dispose();
                showLoginScreen();
            }
        });

        registerFrame.add(usernameLabel);
        registerFrame.add(usernameField);
        registerFrame.add(passwordLabel);
        registerFrame.add(passwordField);
        registerFrame.add(roleLabel);
        registerFrame.add(roleField);
        registerFrame.add(new JLabel()); // Spacer
        registerFrame.add(registerButton);

        registerFrame.setLocationRelativeTo(null); // Center the frame
        registerFrame.setVisible(true);
    }
       private boolean registerUser(String username, String password, String role) {
        if (username.isEmpty() || password.isEmpty() || (!role.equals("user") && !role.equals("admin"))) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please ensure all fields are filled correctly.");
            return false;
        }

        try (PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO Users (Username, Password, Role) VALUES (?, ?, ?)")) {
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, role);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Registration failed: " + e.getMessage());
            return false;
        }
    }

    private boolean authenticateUser(String username, String password) {
        try (PreparedStatement pst = conn.prepareStatement(
                "SELECT Role FROM Users WHERE Username = ? AND Password = ?")) {
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String role = rs.getString("Role");
                JOptionPane.showMessageDialog(null, "Welcome, " + role + "!");
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Authentication error: " + e.getMessage());
        }
        return false;
    }

    // Initialize GUI components
    private void initializeGUI() {
        frame = new JFrame("Movie Ticket Booking System");
        frame.setSize(600, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(4, 2));
        JLabel movieLabel = new JLabel("Select Movie:");
        movieDropdown = new JComboBox<>(loadMovies());
        movieDropdown.addActionListener(e -> initializeSeatSelection());

        JLabel theaterLabel = new JLabel("Select Theater:");
        theaterDropdown = new JComboBox<>(loadTheaters());
        theaterDropdown.addActionListener(e -> initializeSeatSelection());

        JLabel candidateLabel = new JLabel("Candidate Name:");
        candidateNameField = new JTextField();

        JLabel totalPriceTextLabel = new JLabel("Total Price:");
        totalPriceLabel = new JLabel("0");

        bookButton = new JButton("Book Tickets");
        bookButton.addActionListener(e -> bookTickets());

        topPanel.add(movieLabel);
        topPanel.add(movieDropdown);
        topPanel.add(theaterLabel);
        topPanel.add(theaterDropdown);
        topPanel.add(candidateLabel);
        topPanel.add(candidateNameField);
        topPanel.add(totalPriceTextLabel);
        topPanel.add(totalPriceLabel);

        seatSelectionPanel = new JPanel(new GridLayout(10, 10, 5, 5));
        initializeSeatSelection();

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(seatSelectionPanel), BorderLayout.CENTER);
        frame.add(bookButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    // Initialize seat selection buttons
    private void initializeSeatSelection() {
        seatSelectionPanel.removeAll();
        seatButtons.clear();

        String movie = (String) movieDropdown.getSelectedItem();
        String theater = (String) theaterDropdown.getSelectedItem();

        List<String> bookedSeats = getBookedSeats(movie, theater);

        for (int row = 1; row <= 10; row++) {
            for (int col = 1; col <= 10; col++) {
                String seatLabel = row + "-" + col;
                JToggleButton seatButton = new JToggleButton(seatLabel);

                if (bookedSeats.contains(seatLabel)) {
                    seatButton.setEnabled(false);
                    seatButton.setBackground(Color.RED);
                } else {
                    seatButton.setBackground(Color.GREEN);
                    seatButton.addActionListener(e -> updateTotalPrice());
                }

                seatButtons.add(seatButton);
                seatSelectionPanel.add(seatButton);
            }
        }

        seatSelectionPanel.revalidate();
        seatSelectionPanel.repaint();
    }

    // Update total price based on selected seats
    private void updateTotalPrice() {
        int selectedCount = 0;
        for (JToggleButton seatButton : seatButtons) {
            if (seatButton.isSelected()) {
                selectedCount++;
            }
        }
        int totalPrice = selectedCount * COST_PER_SEAT;
        totalPriceLabel.setText(String.valueOf(totalPrice));
    }

    // Load movies from database
    private String[] loadMovies() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT Title FROM Movies")) {
            List<String> movieList = new ArrayList<>();
            while (rs.next()) {
                movieList.add(rs.getString("Title"));
            }
            return movieList.toArray(new String[0]);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading movies: " + e.getMessage());
            return new String[0];
        }
    }

    // Load theaters from database
    private String[] loadTheaters() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT Location FROM Theaters")) {
            List<String> theaterList = new ArrayList<>();
            while (rs.next()) {
                theaterList.add(rs.getString("Location"));
            }
            return theaterList.toArray(new String[0]);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading theaters: " + e.getMessage());
            return new String[0];
        }
    }

        private boolean processPayment(int totalPrice) {
        JPanel paymentPanel = new JPanel(new GridLayout(4, 2));
        JTextField cardNumberField = new JTextField();
        JTextField expiryField = new JTextField();
        JTextField cvvField = new JTextField();

        paymentPanel.add(new JLabel("Card Number:"));
        paymentPanel.add(cardNumberField);
        paymentPanel.add(new JLabel("Expiry Date (MM/YY):"));
        paymentPanel.add(expiryField);
        paymentPanel.add(new JLabel("CVV:"));
        paymentPanel.add(cvvField);
        paymentPanel.add(new JLabel("Total Amount: ₹" + totalPrice));
        paymentPanel.add(new JLabel());

        int option = JOptionPane.showConfirmDialog(null, paymentPanel, "Enter Payment Details", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String cardNumber = cardNumberField.getText().trim();
            String expiryDate = expiryField.getText().trim();
            String cvv = cvvField.getText().trim();

            // Simulate basic validation
            if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Payment failed: Invalid payment details.");
                return false;
            }

            // Simulate successful payment
            JOptionPane.showMessageDialog(null, "Payment successful!");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Payment cancelled.");
            return false;
        }
    }

    private String generateTransactionBill(String candidateName, String movie, String theater, String seatNumbers, int totalPrice, String paymentMethod) {
    	java.util.Date date=new java.util.Date(); 
        return "---------- Transaction Bill ----------\n" +
               "Candidate Name: " + candidateName + "\n" +
               "Movie: " + movie + "\n" +
               "Theater: " + theater + "\n"+"Date: "+date+"\n"+
               "Seats: " + seatNumbers + "\n" +
               "Total Price: ₹" + totalPrice + "\n" +
               "Payment Method: " + paymentMethod + "\n" +
               "Payment Status: Paid\n" +
               "-------------------------------------\n" +
               "Thank you for booking with us!";
    }
    private void bookTickets() {
        String candidateName = candidateNameField.getText().trim();
        if (candidateName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter the candidate's name.");
            return;
        }

        String movie = (String) movieDropdown.getSelectedItem();
        String theater = (String) theaterDropdown.getSelectedItem();

        List<String> selectedSeats = new ArrayList<>();
        for (JToggleButton seatButton : seatButtons) {
            if (seatButton.isSelected()) {
                selectedSeats.add(seatButton.getText());
            }
        }

        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select at least one seat.");
            return;
        }

        String seatNumbers = String.join(", ", selectedSeats);
        int totalPrice = selectedSeats.size() * COST_PER_SEAT;

        // Process payment
        if (!processPayment(totalPrice)) {
            return; // Exit if payment fails
        }

        // Payment successful, proceed with booking
        try {
            int movieID = fetchMovieID(movie);
            int theaterID = fetchTheaterID(theater);

            String insertBookingQuery = "INSERT INTO Bookings (CandidateName, MovieID, TheaterID, SeatNumbers, TotalPrice) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pst = conn.prepareStatement(insertBookingQuery, Statement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, candidateName);
                pst.setInt(2, movieID);
                pst.setInt(3, theaterID);
                pst.setString(4, seatNumbers);
                pst.setInt(5, totalPrice);
                pst.executeUpdate();

                // Retrieve generated BookingID
                ResultSet rs = pst.getGeneratedKeys();
                int bookingID = rs.next() ? rs.getInt(1) : -1;

                // Insert payment details
                String insertPaymentQuery = "INSERT INTO Payments (BookingID, PaymentMethod, AmountPaid, PaymentStatus) VALUES (?, ?, ?, ?)";
                try (PreparedStatement paymentPst = conn.prepareStatement(insertPaymentQuery)) {
                    paymentPst.setInt(1, bookingID);
                    paymentPst.setString(2, "Credit/Debit Card"); // Static for now
                    paymentPst.setInt(3, totalPrice);
                    paymentPst.setString(4, "Paid");
                    paymentPst.executeUpdate();
                }

                // Show transaction bill
                JOptionPane.showMessageDialog(null, generateTransactionBill(candidateName, movie, theater, seatNumbers, totalPrice, "Credit/Debit Card"));
            }

            // Reset UI
            initializeSeatSelection();
            candidateNameField.setText("");
            totalPriceLabel.setText("0");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Booking failed: " + e.getMessage());
        }
    }


    // Fetch MovieID by movie title
    private int fetchMovieID(String movieTitle) {
        try (PreparedStatement pst = conn.prepareStatement("SELECT MovieID FROM Movies WHERE Title = ?")) {
            pst.setString(1, movieTitle);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("MovieID");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching MovieID: " + e.getMessage());
        }
        return -1; // Return invalid ID if not found
    }

    // Fetch TheaterID by theater location
    private int fetchTheaterID(String theaterLocation) {
        try (PreparedStatement pst = conn.prepareStatement("SELECT TheaterID FROM Theaters WHERE Location = ?")) {
            pst.setString(1, theaterLocation);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("TheaterID");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching TheaterID: " + e.getMessage());
        }
        return -1; // Return invalid ID if not found
    }

    // Get booked seats for the selected movie and theater
    private List<String> getBookedSeats(String movieTitle, String theaterLocation) {
        List<String> bookedSeats = new ArrayList<>();
        try {
            int movieID = fetchMovieID(movieTitle);
            int theaterID = fetchTheaterID(theaterLocation);

            String query = "SELECT SeatNumbers FROM Bookings WHERE MovieID = ? AND TheaterID = ?";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setInt(1, movieID);
                pst.setInt(2, theaterID);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                	
                    String seatNumbers = rs.getString("SeatNumbers");
                    bookedSeats.addAll(Arrays.asList(seatNumbers.split(", ")));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching booked seats: " + e.getMessage());
        }
        
        
        return bookedSeats;
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MovieTicketBookingSystem::new);
        SwingUtilities.invokeLater(() -> new MovieTicketBookingSystem().showLoginScreen());
    }
}

