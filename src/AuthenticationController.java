/**
 * Handles authentication logic for both login and registration.
 * Communicates with the DataManager for credential validation
 * and delegates view changes to the MainApplication.
 * 
 */
public class AuthenticationController {
    private DataManager dataManager;
    private MainApplication mainApp;
    
    private LoginView loginView;
    private RegisterView registerView;

    /**
     * Constructs an AuthenticationController with references to
     * the data manager and main application handler.
     *
     * @param dataManager the data manager responsible for data operations
     * @param mainApp the main application managing scene transitions
     */
    public AuthenticationController(DataManager dataManager, MainApplication mainApp) {
        this.dataManager = dataManager;
        this.mainApp = mainApp;
    }
    
     /**
     * Sets the login view for displaying login-related messages.
     *
     * @param loginView the login view to connect to this controller
     */
    public void setView(LoginView loginView) {
        this.loginView = loginView;
    }
    
    /**
     * Sets the registration view for displaying registration-related messages.
     *
     * @param registerView the register view to connect to this controller
     */
    public void setRegisterView(RegisterView registerView) {
        this.registerView = registerView;
    }
    
     /**
     * Processes a login attempt by validating credentials and determining
     * whether the user is a customer or an employee.
     *
     * @param username the entered username
     * @param password the entered password
     * @param userType the type of user ("Customer" or "Employee")
     */
    public void handleLogin(String username, String password, String userType) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            if (loginView != null) {
                loginView.showStatus("Please fill in all fields", "error");
            }
            return;
        }
        
        boolean isEmployee = "Employee".equals(userType);
        
        if (isEmployee) {
            Employee employee = dataManager.authenticateEmployee(username, password);
            if (employee != null) {
                mainApp.onLoginSuccess(null, employee);
            } else {
                if (loginView != null) {
                    loginView.showStatus("Invalid employee credentials", "error");
                }
            }
        } else {
            Customer customer = dataManager.authenticateCustomer(username, password);
            if (customer != null) {
                mainApp.onLoginSuccess(customer, null);
            } else {
                if (loginView != null) {
                    loginView.showStatus("Invalid customer credentials", "error");
                }
            }
        }
    }
    
    /**
     * Handles the registration process by validating input fields,
     * checking for existing usernames, and creating new customer
     * or employee accounts accordingly.
     *
     * @param username the desired username
     * @param password the chosen password
     * @param confirmPassword the password confirmation
     * @param name the full name of the user
     * @param userType indicates if the account is for a customer or employee
     * @param employeeId the employee ID (required only for employee accounts)
     */
    public void handleRegister(String username, String password, String confirmPassword,
                               String name, String userType, String employeeId) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty() ||
            name == null || name.trim().isEmpty()) {
            if (registerView != null) {
                registerView.showStatus("Please fill in all required fields", "error");
            }
            return;
        }
        
        if (password.length() < 4) {
            if (registerView != null) {
                registerView.showStatus("Password must be at least 4 characters", "error");
            }
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            if (registerView != null) {
                registerView.showStatus("Passwords do not match", "error");
            }
            return;
        }
        
        if (dataManager.usernameExists(username)) {
            if (registerView != null) {
                registerView.showStatus("Username already exists", "error");
            }
            return;
        }
        
        boolean isEmployee = "Employee".equals(userType);
        
        if (isEmployee) {
            if (employeeId == null || employeeId.trim().isEmpty()) {
                if (registerView != null) {
                    registerView.showStatus("Employee ID is required", "error");
                }
                return;
            }
            
            Employee newEmployee = new Employee(name, username, password, employeeId);
            if (dataManager.registerEmployee(newEmployee)) {
                if (registerView != null) {
                    registerView.showStatus("Account created! Please login.", "success");
                    registerView.clearFields();
                }
                mainApp.showLoginView();
            } else {
                if (registerView != null) {
                    registerView.showStatus("Error creating account", "error");
                }
            }
        } else {
            Customer newCustomer = new Customer(name, username, password);
            if (dataManager.registerCustomer(newCustomer)) {
                if (registerView != null) {
                    registerView.showStatus("Account created! Please login.", "success");
                    registerView.clearFields();
                }
                mainApp.showLoginView();
            } else {
                if (registerView != null) {
                    registerView.showStatus("Error creating account", "error");
                }
            }
        }
    }
    
    /**
     * Navigates to registration screen.
     */
    public void handleShowRegister() {
        mainApp.showRegisterView();
    }
    
    /**
     * Navigates to login screen.
     */
    public void handleShowLogin() {
        mainApp.showLoginView();
    }
}