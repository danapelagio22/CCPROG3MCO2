/**
 * User represents a system user with authentication credentials.
 * It stores basic user information such as name, username, and password.
 */
public class User {
    private String name;
    private String username;
    private String password;

    /**
     * Constructs a User with the specified name, username, and password.
     *
     * @param name The full name of the user.
     * @param username The unique username used for login.
     * @param password The user's account password.
     */
    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the user's full name.
     *
     * @return The user's name.
     */
    public String getName() { 
        return name; 
    }
    
    /**
    * Returns the user's username.
    *
    * @return The user's username.
    */
    public String getUsername() { 
        return username; 
    }

    /**
     * Returns the user's password.
     *
     * @return The user's password.
     */
    public String getPassword() { 
        return password; 
    }
}
