package somepackage;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Managed bean for handling the retrieval and storage of projects specific to a user.
 * This bean is session-scoped, retaining its state across multiple HTTP requests in the same user session.
 */
@ManagedBean(name = "userProjectsBean")
@SessionScoped
public class UserProjectsBean {
    private List<Project> userProjects;

    /**
     * Initializes the bean and loads user projects from the database.
     * This method is called after the bean's construction.
     */
    @PostConstruct
    public void init() {
        loadUserProjects();
    }

    /**
     * Returns the list of projects associated with the current user.
     *
     * @return List of user projects
     */
    public List<Project> getUserProjects() {
        return userProjects;
    }

    /**
     * Loads the projects associated with the current user from the database.
     * This method connects to the database, executes a query to retrieve the projects,
     * and populates the userProjects list with the results.
     */
    private void loadUserProjects() {
        FacesContext context = FacesContext.getCurrentInstance();
        UserBean userBean = context.getApplication().evaluateExpressionGet(context, "#{userBean}", UserBean.class);
        int userId = userBean.getUserId();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        userProjects = new ArrayList<>();
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection("jdbc:derby:/usr/local/DerbyDB;create=true");
            stmt = conn.prepareStatement("SELECT * FROM projects WHERE user_id = ?");
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Project project = new Project(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("summary"),
                        rs.getString("description"),
                        Arrays.asList(rs.getString("keywords").split(",")),
                        rs.getString("type"),
                        Arrays.asList(rs.getString("collaborators").split(",")),
                        rs.getString("link"),
                        rs.getString("learned"),
                        rs.getString("accomplishments"),
                        Arrays.asList(rs.getString("enhancements").split(",")),
                        rs.getString("downloadLink")
                );
                userProjects.add(project);
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
}