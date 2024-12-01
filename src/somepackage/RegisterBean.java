package somepackage;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Managed bean for handling user registration.
 * This bean is session-scoped, retaining its state across multiple HTTP requests in the same user session.
 */
@ManagedBean(name = "registerBean")
@SessionScoped
public class RegisterBean {
    private static final Logger LOGGER = Logger.getLogger(RegisterBean.class.getName());

    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private String fullname;

    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * Registers a new user. If the passwords do not match, an error message is displayed.
     * If the registration is successful, the user is redirected to the login page.
     *
     * @return the navigation outcome string for JSF navigation
     */
    public String register() {
        FacesContext context = FacesContext.getCurrentInstance();

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            FacesMessage message = new FacesMessage("Passwords do not match.");
            context.addMessage(null, message);
            return null;
        }

        // Register the user in the database
        try (Connection conn = DriverManager.getConnection("jdbc:derby:/usr/local/DerbyDB;create=true");
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO users (username, password, email, fullname, isProfileComplete) VALUES (?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, fullname);
            pstmt.setBoolean(5, false);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            // Retrieve the generated user ID and create an empty portfolio for the new user
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);

                    // Create an empty portfolio for the new user
                    try (PreparedStatement portfolioStmt = conn.prepareStatement(
                            "INSERT INTO portfolios (user_id, fullname, email) VALUES (?, ?, ?)")) {
                        portfolioStmt.setInt(1, userId);
                        portfolioStmt.setString(2, fullname);
                        portfolioStmt.setString(3, email);
                        portfolioStmt.executeUpdate();
                    }
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

            FacesMessage message = new FacesMessage("Registration successful. Please log in.");
            context.addMessage(null, message);
            return "login?faces-redirect=true"; // Redirect to login page
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Registration failed", e);
            FacesMessage message = new FacesMessage("Registration failed. Please try again.");
            context.addMessage(null, message);
            return null;
        }
    }
}