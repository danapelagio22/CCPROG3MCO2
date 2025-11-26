/**
 * MembershipCard class represents a customer loyalty card.
 * Manages points accumulation and redemption for discounts.
 * Points are earned at a rate of 1 point per P50 spent.
 * 1 point can be redeemed for P1 discount.
 */
public class MembershipCard {
    private String cardNumber;
    private int points;

    /**
     * Constructs a MembershipCard with the specified card number.
     * Initializes points to 0.
     *
     * @param cardNumber the unique card number
     */
    public MembershipCard(String cardNumber) {
        this.cardNumber = cardNumber;
        this.points = 0;
    }

    /**
     * Adds points based on the purchase amount.
     * For every P50 spent, 1 point is awarded.
     * @param amount the purchase amount in pesos
     */
    public void addPoints(double amount) {
        if (amount > 0) {
            int earnedPoints = (int) (amount / 50);
            points += earnedPoints;
        }
    }

    /**
     * Redeems points for a discount amount.
     * Each point is worth P1.
     * @param pointsToUse the number of points to redeem
     * @return the discount amount in pesos
     */
    public double redeemPoints(int pointsToUse) {
        if (pointsToUse <= 0 || pointsToUse > points) {
            return 0.0;
        }

        points -= pointsToUse;
        return pointsToUse; 
    }

    /**
     * Gets the current discount available based on accumulated points.
     * Does not redeem the points.
     * @return the maximum discount amount available
     */
    public double getDiscount() {
        return points; 
    }

    /**
     * @return The current number of loyalty points on the card.
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return The unique membership card number.
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Sets the loyalty points on the card.
     *
     * @param points The number of points. Must be zero or positive.
     */
    public void setPoints(int points) {
        if (points >= 0) {
            this.points = points;
        }
    }
}