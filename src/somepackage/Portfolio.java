package somepackage;

/**
 * Portfolio class represents the portfolio information for a user.
 * It contains personal details, career goals, and customization settings.
 */
public class Portfolio {
    private int id;
    private int userId;
    private String fullname;
    private String email;
    private String linkedin;
    private String interests; // Interests stored as a single string
    private String experience;
    private String careerGoals;
    private String font;
    private String fontSize;
    private String fontColor;
    private String backgroundColor;
    private String backgroundImage;

    /**
     * Constructor to initialize the Portfolio object.
     *
     * @param id           the unique identifier of the portfolio
     * @param userId       the unique identifier of the user
     * @param fullname     the full name of the user
     * @param email        the email address of the user
     * @param linkedin     the LinkedIn profile URL of the user
     * @param interests    the interests of the user
     * @param experience   the experience details of the user
     * @param careerGoals  the career goals of the user
     */
    public Portfolio(int id, int userId, String fullname, String email, String linkedin, String interests, String experience, String careerGoals) {
        this.id = id;
        this.userId = userId;
        this.fullname = fullname;
        this.email = email;
        this.linkedin = linkedin;
        this.interests = interests;
        this.experience = experience;
        this.careerGoals = careerGoals;
    }

    // Getters and Setters for all fields

    /**
     * Gets the portfolio ID.
     *
     * @return the portfolio ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the portfolio ID.
     *
     * @param id the portfolio ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the user ID associated with this portfolio.
     *
     * @return the user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with this portfolio.
     *
     * @param userId the user ID
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the full name of the user.
     *
     * @return the full name of the user
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * Sets the full name of the user.
     *
     * @param fullname the full name of the user
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the email address of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the LinkedIn profile URL of the user.
     *
     * @return the LinkedIn profile URL
     */
    public String getLinkedin() {
        return linkedin;
    }

    /**
     * Sets the LinkedIn profile URL of the user.
     *
     * @param linkedin the LinkedIn profile URL
     */
    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    /**
     * Gets the interests of the user.
     *
     * @return the interests of the user
     */
    public String getInterests() {
        return interests;
    }

    /**
     * Sets the interests of the user.
     *
     * @param interests the interests of the user
     */
    public void setInterests(String interests) {
        this.interests = interests;
    }

    /**
     * Gets the experience details of the user.
     *
     * @return the experience details of the user
     */
    public String getExperience() {
        return experience;
    }

    /**
     * Sets the experience details of the user.
     *
     * @param experience the experience details of the user
     */
    public void setExperience(String experience) {
        this.experience = experience;
    }

    /**
     * Gets the career goals of the user.
     *
     * @return the career goals of the user
     */
    public String getCareerGoals() {
        return careerGoals;
    }

    /**
     * Sets the career goals of the user.
     *
     * @param careerGoals the career goals of the user
     */
    public void setCareerGoals(String careerGoals) {
        this.careerGoals = careerGoals;
    }

    /**
     * Gets the font used in the portfolio.
     *
     * @return the font used in the portfolio
     */
    public String getFont() {
        return font;
    }

    /**
     * Sets the font used in the portfolio.
     *
     * @param font the font used in the portfolio
     */
    public void setFont(String font) {
        this.font = font;
    }

    /**
     * Gets the font size used in the portfolio.
     *
     * @return the font size used in the portfolio
     */
    public String getFontSize() {
        return fontSize;
    }

    /**
     * Sets the font size used in the portfolio.
     *
     * @param fontSize the font size used in the portfolio
     */
    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * Gets the font color used in the portfolio.
     *
     * @return the font color used in the portfolio
     */
    public String getFontColor() {
        return fontColor;
    }

    /**
     * Sets the font color used in the portfolio.
     *
     * @param fontColor the font color used in the portfolio
     */
    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    /**
     * Gets the background color of the portfolio.
     *
     * @return the background color of the portfolio
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Sets the background color of the portfolio.
     *
     * @param backgroundColor the background color of the portfolio
     */
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Gets the background image of the portfolio.
     *
     * @return the background image of the portfolio
     */
    public String getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * Sets the background image of the portfolio.
     *
     * @param backgroundImage the background image of the portfolio
     */
    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }
}