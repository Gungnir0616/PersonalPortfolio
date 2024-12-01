package somepackage;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Managed bean for handling user authentication and session management.
 * This bean is session-scoped, retaining its state across multiple HTTP requests in the same user session.
 */
@ManagedBean(name = "userBean")
@SessionScoped
public class UserBean {
    private String username;
    private String password;
    private int userId;
    private boolean isProfileComplete;

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

    public int getUserId() {
        return userId;
    }

    public boolean isProfileComplete() {
        return isProfileComplete;
    }

    /**
     * Logs out the current user by invalidating the session and redirecting to the login page.
     *
     * @return navigation outcome string for JSF navigation
     */
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "login?faces-redirect=true";
    }

    /**
     * Authenticates the user by checking the provided username and password against the database.
     * If authentication is successful, it redirects to the appropriate page based on the user's profile status.
     *
     * @return navigation outcome string for JSF navigation
     */
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();

        // Authenticate the user in the database
        try (Connection conn = DriverManager.getConnection("jdbc:derby:/usr/local/DerbyDB;create=true");
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT id, isProfileComplete FROM users WHERE username = ? AND password = ?")) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("id");
                isProfileComplete = rs.getBoolean("isProfileComplete");

                // Redirect based on profile completion status
                if (isProfileComplete) {
                    return "portfolio?faces-redirect=true"; // Redirect to portfolio page
                } else {
                    return "newPortfolio?faces-redirect=true"; // Redirect to new portfolio page
                }
            } else {
                FacesMessage message = new FacesMessage("Invalid username or password.");
                context.addMessage(null, message);
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage("Login failed. Please try again.");
            context.addMessage(null, message);
            return null;
        }
    }
}