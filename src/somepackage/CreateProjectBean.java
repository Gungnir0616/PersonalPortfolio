package somepackage;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Managed bean for handling the creation of new projects in a JSF application.
 * This bean allows users to add, remove keywords and save new projects to the database.
 */
@ManagedBean(name = "createProjectBean")
@SessionScoped
public class CreateProjectBean {
    private static final Logger LOGGER = Logger.getLogger(CreateProjectBean.class.getName());

    private Project newProject;
    private String newKeyword;

    /**
     * Constructor that initializes the new project with empty lists for keywords, collaborators, and enhancements.
     */
    public CreateProjectBean() {
        newProject = new Project();
        newProject.setKeywords(new ArrayList<>());  // Initialize keywords list
        newProject.setCollaborators(new ArrayList<>());
        newProject.setEnhancements(new ArrayList<>());
    }

    // Getter and Setter methods
    public Project getNewProject() {
        return newProject;
    }

    public void setNewProject(Project newProject) {
        this.newProject = newProject;
    }

    public String getNewKeyword() {
        return newKeyword;
    }

    public void setNewKeyword(String newKeyword) {
        this.newKeyword = newKeyword;
    }

    /**
     * Adds a new keyword to the project's keyword list if it is not empty or already existing.
     * Logs a warning if the keyword is empty or already exists.
     */
    public void addKeyword() {
        if (newProject.getKeywords() == null) {
            newProject.setKeywords(new ArrayList<>());  // Initialize keywords list if null
        }
        if (newKeyword != null && !newKeyword.trim().isEmpty() && !newProject.getKeywords().contains(newKeyword)) {
            newProject.getKeywords().add(newKeyword);
            newKeyword = "";
        } else {
            LOGGER.log(Level.WARNING, "Keyword is empty or already exists: {0}", newKeyword);
        }
    }

    /**
     * Removes a keyword from the project's keyword list.
     * Logs a warning if the keyword list is null.
     *
     * @param keyword The keyword to remove.
     */
    public void removeKeyword(String keyword) {
        if (newProject.getKeywords() != null) {
            newProject.getKeywords().remove(keyword);
        } else {
            LOGGER.log(Level.WARNING, "Keyword list is null");
        }
    }

    /**
     * Saves the new project to the database.
     * Reloads the projects for the user after creating a new project.
     * Displays a success or failure message based on the result of the operation.
     *
     * @return Navigation outcome to the portfolio page.
     */
    public String saveNewProject() {
        FacesContext context = FacesContext.getCurrentInstance();
        UserBean userBean = context.getApplication().evaluateExpressionGet(context, "#{userBean}", UserBean.class);
        int userId = userBean.getUserId();

        LOGGER.log(Level.INFO, "Attempting to save new project for user ID: {0}", userId);

        try (Connection conn = DriverManager.getConnection("jdbc:derby:/usr/local/DerbyDB;create=true");
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO projects (title, summary, description, keywords, type, collaborators, link, learned, accomplishments, enhancements, downloadLink, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            pstmt.setString(1, newProject.getTitle());
            pstmt.setString(2, newProject.getSummary());
            pstmt.setString(3, newProject.getDescription());
            pstmt.setString(4, String.join(",", newProject.getKeywords() != null ? newProject.getKeywords() : new ArrayList<>()));
            pstmt.setString(5, newProject.getType());
            pstmt.setString(6, String.join(",", newProject.getCollaborators()));
            pstmt.setString(7, newProject.getLink());
            pstmt.setString(8, newProject.getLearned());
            pstmt.setString(9, newProject.getAccomplishments());
            pstmt.setString(10, String.join(",", newProject.getEnhancements()));
            pstmt.setString(11, newProject.getDownloadLink());
            pstmt.setInt(12, userId);

            int rowsAffected = pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "Rows affected: {0}", rowsAffected);

            FacesMessage message = new FacesMessage("Project created successfully.");
            context.addMessage(null, message);

            // Reload projects after creating a new project
            ProjectBean projectBean = context.getApplication().evaluateExpressionGet(context, "#{projectBean}", ProjectBean.class);
            projectBean.loadProjectsForUser(userId);

            return "portfolio?faces-redirect=true"; // Navigate back to the portfolio page
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to create project", e);
            FacesMessage message = new FacesMessage("Failed to create project: " + e.getMessage());
            context.addMessage(null, message);
            return null;
        }
    }
}