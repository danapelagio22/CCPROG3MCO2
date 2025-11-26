import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * ReceiptView displays a formatted receipt in a separate window.
 * Shows itemized purchases, discounts, totals, and payment information.
 * Receipt is automatically saved to file when created.
 */
public class ReceiptView extends Stage {
    private ReceiptController controller;
    private TextArea receiptTextArea;

    /**
     * Constructs a ReceiptView and initializes the receipt window.
     *
     * @param controller The ReceiptController that provides receipt data.
     */
    public ReceiptView(ReceiptController controller) {
        this.controller = controller;
        initializeUI();
    }

    /**
     * Initializes the user interface for displaying the receipt.
     * Sets up layout, text content, and action buttons.
     */
    private void initializeUI() {
        setTitle("Receipt - " + controller.getTransactionID());

        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-background-color: white;");

        Label headerLabel = new Label("CONVENIENCE STORE");
        headerLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 20));

        Label receiptLabel = new Label("RECEIPT");
        receiptLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 16));

        VBox headerBox = new VBox(5, headerLabel, receiptLabel);
        headerBox.setAlignment(Pos.CENTER);

        Separator sep1 = new Separator();

        receiptTextArea = new TextArea();
        receiptTextArea.setFont(Font.font("Courier New", 12));
        receiptTextArea.setEditable(false);
        receiptTextArea.setPrefRowCount(25);
        receiptTextArea.setPrefColumnCount(50);
        receiptTextArea.setStyle("-fx-control-inner-background: #f9f9f9;");

        String receiptText = generateReceiptText();
        receiptTextArea.setText(receiptText);

        Separator sep2 = new Separator();

        Label infoLabel = new Label("✓ Receipt automatically saved to file");
        infoLabel.setFont(Font.font("Arial", 12));
        infoLabel.setStyle("-fx-text-fill: #4CAF50;");

        Button printButton = new Button("Print Receipt");
        printButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        printButton.setPrefWidth(150);
        printButton.setOnAction(e -> handlePrint());

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-font-size: 14px;");
        closeButton.setPrefWidth(150);
        closeButton.setOnAction(e -> close());

        HBox buttonBox = new HBox(10, printButton, closeButton);
        buttonBox.setAlignment(Pos.CENTER);

        mainLayout.getChildren().addAll(headerBox, sep1, receiptTextArea, sep2, infoLabel, buttonBox);

        Scene scene = new Scene(mainLayout, 600, 700);
        setScene(scene);
    }

    /**
     * Generates the formatted receipt text.
     * All required data is retrieved through the controller.
     *
     * @return A formatted receipt string.
     */
    private String generateReceiptText() {
        StringBuilder sb = new StringBuilder();

        sb.append("========================================\n");
        sb.append("          CONVENIENCE STORE\n");
        sb.append("             RECEIPT\n");
        sb.append("========================================\n\n");

        sb.append("Date: ").append(controller.getFormattedDateTime()).append("\n");
        sb.append("Transaction ID: ").append(controller.getTransactionID()).append("\n");
        sb.append("Customer: ").append(controller.getCustomerName()).append("\n");

        if (controller.hasMembershipCard()) {
            sb.append("Member Card: ").append(controller.getMembershipCardNumber()).append("\n");
            sb.append("Points Balance: ").append(controller.getMembershipPoints()).append("\n");
        }

        sb.append("\n========================================\n");
        sb.append("ITEMS:\n");
        sb.append("----------------------------------------\n");

        for (CartItem item : controller.getPurchasedItems()) {
            sb.append(String.format("%-20s x%-3d  ₱%8.2f\n",
                    truncate(controller.getProductName(item), 20),
                    controller.getItemQuantity(item),
                    controller.getLineTotal(item)));
        }

        sb.append("----------------------------------------\n");
        sb.append(String.format("Subtotal:               ₱%8.2f\n", controller.computeSubtotal()));

        double discount = controller.getDiscount();
        if (discount > 0) {
            sb.append(String.format("Discount:              -₱%8.2f\n", discount));
            sb.append(String.format("After Discount:         ₱%8.2f\n", controller.getAfterDiscount()));
        }

        sb.append(String.format("VAT (12%%):              ₱%8.2f\n", controller.getVAT()));

        sb.append("========================================\n");
        sb.append(String.format("TOTAL:                  ₱%8.2f\n", controller.getTotal()));

        if (controller.hasPayment()) {
            sb.append(String.format("Amount Received:        ₱%8.2f\n", controller.getAmountReceived()));
            sb.append(String.format("Change:                 ₱%8.2f\n", controller.getChange()));
        }

        sb.append("========================================\n");
        sb.append("     Thank you for shopping with us!\n");
        sb.append("========================================\n");

        return sb.toString();
    }

    /**
     * Handles printing the receipt.
     * Currently displays a confirmation dialog simulating printing.
     */
    private void handlePrint() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Print");
        alert.setHeaderText("Print Receipt");
        alert.setContentText("Receipt sent to printer.\n(In a real app, this would print)");
        alert.showAndWait();
    }

    /**
     * Truncates a string to a specified maximum length.
     * Appends an ellipsis if the string exceeds the limit.
     *
     * @param str The string to truncate.
     * @param maxLength The maximum allowed length.
     * @return The truncated string.
     */
    private String truncate(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}