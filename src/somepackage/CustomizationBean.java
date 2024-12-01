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

/**
 * Managed bean for handling the customization of user portfolios in a JSF application.
 * This bean allows users to load and save their customization settings such as font,
 * font size, font color, background color, and background image.
 */
@ManagedBean(name = "customizationBean")
@SessionScoped
public class CustomizationBean {
    private String font;
    private int fontSize;
    private String fontColor;
    private String backgroundColor;
    private String backgroundImage;

    // Getters and setters for customization properties

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    /**
     * Loads the customization settings for the current user from the database.
     */
    public void loadCustomization() {
        FacesContext context = FacesContext.getCurrentInstance();
        UserBean userBean = context.getApplication().evaluateExpressionGet(context, "#{userBean}", UserBean.class);
        int userId = userBean.getUserId();

        try (Connection conn = DriverManager.getConnection("jdbc:derby:/usr/local/DerbyDB;create=true");
             PreparedStatement stmt = conn.prepareStatement("SELECT font, font_size, font_color, background_color, background_image FROM portfolios WHERE user_id = ?")) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    font = rs.getString("font");
                    fontSize = rs.getInt("font_size");
                    fontColor = rs.getString("font_color");
                    backgroundColor = rs.getString("background_color");
                    backgroundImage = rs.getString("background_image");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the customization settings for the current user to the database.
     *
     * @return Navigation outcome to the portfolio page.
     */
    public String saveCustomization() {
        FacesContext context = FacesContext.getCurrentInstance();
        UserBean userBean = context.getApplication().evaluateExpressionGet(context, "#{userBean}", UserBean.class);
        int userId = userBean.getUserId();

        try (Connection conn = DriverManager.getConnection("jdbc:derby:/usr/local/DerbyDB;create=true");
             PreparedStatement stmt = conn.prepareStatement("UPDATE portfolios SET font = ?, font_size = ?, font_color = ?, background_color = ?, background_image = ? WHERE user_id = ?")) {
            stmt.setString(1, font);
            stmt.setInt(2, fontSize);
            stmt.setString(3, fontColor);
            stmt.setString(4, backgroundColor);
            stmt.setString(5, backgroundImage);
            stmt.setInt(6, userId);

            stmt.executeUpdate();

            FacesMessage message = new FacesMessage("Customization settings saved successfully.");
            context.addMessage(null, message);

            return "portfolio?faces-redirect=true"; // Navigate back to the portfolio page
        } catch (SQLException e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage("Failed to save customization settings.");
            context.addMessage(null, message);
            return null;
        }
    }
}