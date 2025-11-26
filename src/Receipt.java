import java.time.format.DateTimeFormatter;

/**
 * Receipt represents a transaction receipt.
 * It generates a formatted text receipt and handles saving
 * the receipt data to persistent storage.
 */
public class Receipt {
    private Transaction transaction;
    private DataManager dataManager;

    /**
     * Constructs a Receipt for the given transaction.
     *
     * @param transaction The transaction associated with this receipt.
     */
    public Receipt(Transaction transaction) {
        this.transaction = transaction;
    }
    
    /**
     * Injects the DataManager responsible for saving receipt files.
     *
     * @param dataManager The DataManager used to persist receipt data.
     */
    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * Returns the transaction associated with this receipt.
     *
     * @return The Transaction linked to this receipt.
     */
    public Transaction getTransaction() {
        return transaction;
    }

    /**
     * Generates the receipt text and saves it to a file.
     * If no DataManager is set, the receipt will not be saved.
     */
    public void saveToFile() {
        if (dataManager != null) {
            String receiptContent = generateReceiptText();
            dataManager.saveReceipt(transaction.getTransactionID(), receiptContent);
        } else {
            System.err.println("DataManager not set. Cannot save receipt.");
        }
    }

    /**
     * Generates a formatted receipt as a string.
     * Includes transaction details, purchased items,
     * discounts, taxes, and payment information.
     *
     * @return A formatted receipt string.
     */
    private String generateReceiptText() {
        StringBuilder receipt = new StringBuilder();

        receipt.append("\n========================================\n");
        receipt.append("          CONVENIENCE STORE\n");
        receipt.append("             RECEIPT\n");
        receipt.append("========================================\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        receipt.append("Date: ").append(transaction.getTimeStamp().format(formatter)).append("\n");
        receipt.append("Transaction ID: ").append(transaction.getTransactionID()).append("\n");
        receipt.append("Customer: ").append(transaction.getCustomer().getName()).append("\n");

        if (transaction.getCustomer().hasMembershipCard()) {
            MembershipCard card = transaction.getCustomer().getMembershipCard();
            receipt.append("Member Card: ").append(card.getCardNumber()).append("\n");
            receipt.append("Points Balance: ").append(card.getPoints()).append("\n");
        }

        receipt.append("========================================\n");
        receipt.append("ITEMS:\n");
        receipt.append("----------------------------------------\n");

        double subtotal = 0.0;
        for (CartItem item : transaction.getPurchasedItems()) {
            Product product = item.getProduct();
            double lineTotal = item.computeLineTotal();
            subtotal += lineTotal;

            receipt.append(String.format("%-20s x%-3d  P%8.2f\n",
                    product.getName(),
                    item.getQuantity(),
                    lineTotal));
        }

        receipt.append("----------------------------------------\n");
        receipt.append(String.format("Subtotal:               P%8.2f\n", subtotal));

        double afterDiscount = transaction.applyDiscounts();
        double discount = subtotal - afterDiscount;
        if (discount > 0) {
            receipt.append(String.format("Discount:              -P%8.2f\n", discount));
            receipt.append(String.format("After Discount:         P%8.2f\n", afterDiscount));
        }

        double vat = afterDiscount * 0.12;
        receipt.append(String.format("VAT (12%%):              P%8.2f\n", vat));

        receipt.append("========================================\n");
        receipt.append(String.format("TOTAL:                  P%8.2f\n", transaction.getTotalCost()));
        receipt.append(String.format("Amount Received:        P%8.2f\n",
                transaction.getPayment().getAmountReceived()));
        receipt.append(String.format("Change:                 P%8.2f\n",
                transaction.getPayment().computeChange()));
        receipt.append("========================================\n");
        receipt.append("     Thank you for shopping with us!\n");
        receipt.append("========================================\n\n");

        return receipt.toString();
    }
}