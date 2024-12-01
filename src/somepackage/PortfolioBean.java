package somepackage;

import javax.annotation.PostConstruct;
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
 * Managed bean for handling portfolio-related operations.
 * This bean is session-scoped, retaining its state across multiple HTTP requests in the same user session.
 */
@ManagedBean(name = "portfolioBean")
@SessionScoped
public class PortfolioBean {
    private static final Logger LOGGER = Logger.getLogger(PortfolioBean.class.getName());
    private Portfolio portfolio;

    /**
     * Initializes the bean and loads the portfolio from the database.
     * This method is called after the bean's construction.
     */
    @PostConstruct
    public void init() {
        loadPortfolioFromDatabase();
        FacesContext context = FacesContext.getCurrentInstance();
        CustomizationBean customizationBean = context.getApplication().evaluateExpressionGet(context, "#{customizationBean}", CustomizationBean.class);
        customizationBean.loadCustomization();
    }

    /**
     * Gets the portfolio object.
     *
     * @return the portfolio object
     */
    public Portfolio getPortfolio() {
        return portfolio;
    }

    /**
     * Sets the portfolio object.
     *
     * @param portfolio the portfolio object
     */
    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    /**
     * Loads the portfolio from the database for the current user.
     */
    private void loadPortfolioFromDatabase() {
        FacesContext context = FacesContext.getCurrentInstance();
        UserBean userBean = context.getApplication().evaluateExpressionGet(context, "#{userBean}", UserBean.class);
        int userId = userBean.getUserId();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection("jdbc:derby:/usr/local/DerbyDB;create=true");
            stmt = conn.prepareStatement("SELECT * FROM portfolios WHERE user_id = ?");
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                portfolio = new Portfolio(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("fullname"),
                        rs.getString("email"),
                        rs.getString("linkedin"),
                        rs.getString("interests"),
                        rs.getString("experience"),
                        rs.getString("career_goals")
                );

                // Load projects for this user
                ProjectBean projectBean = context.getApplication().evaluateExpressionGet(context, "#{projectBean}", ProjectBean.class);
                projectBean.loadProjectsForUser(userId);

                LOGGER.log(Level.INFO, "Existing portfolio loaded for user ID: {0}", userId);

            } else {
                portfolio = new Portfolio(0, userId, "", "", "", "", "", "");
                LOGGER.log(Level.INFO, "No existing portfolio found for user ID: {0}, initializing new portfolio", userId);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the portfolio to the database and updates the isProfileComplete field.
     *
     * @return the navigation outcome string for JSF navigation
     */
    public String savePortfolio() {
        FacesContext context = FacesContext.getCurrentInstance();
        UserBean userBean = context.getApplication().evaluateExpressionGet(context, "#{userBean}", UserBean.class);
        int userId = userBean.getUserId();

        try (Connection conn = DriverManager.getConnection("jdbc:derby:/usr/local/DerbyDB;create=true");
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE portfolios SET fullname=?, email=?, linkedin=?, interests=?, experience=?, career_goals=? WHERE user_id=?")) {

            pstmt.setString(1, portfolio.getFullname());
            pstmt.setString(2, portfolio.getEmail());
            pstmt.setString(3, portfolio.getLinkedin());
            pstmt.setString(4, portfolio.getInterests());
            pstmt.setString(5, portfolio.getExperience());
            pstmt.setString(6, portfolio.getCareerGoals());
            pstmt.setInt(7, userId);

            pstmt.executeUpdate();

            // Update isProfileComplete field to true
            try (PreparedStatement userStmt = conn.prepareStatement(
                    "UPDATE users SET isProfileComplete = true WHERE id = ?")) {
                userStmt.setInt(1, userId);
                userStmt.executeUpdate();
            }

            FacesMessage message = new FacesMessage("Portfolio updated successfully.");
            context.addMessage(null, message);

            return "portfolio?faces-redirect=true"; // Navigate back to the portfolio page
        } catch (SQLException e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage("Failed to update portfolio.");
            context.addMessage(null, message);
            return null;
        }
    }

    /**
     * Loads the portfolio data from the database.
     */
    public void loadPortfolio() {
        loadPortfolioFromDatabase();
    }
}