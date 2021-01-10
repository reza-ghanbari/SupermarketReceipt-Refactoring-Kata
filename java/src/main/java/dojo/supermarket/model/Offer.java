package dojo.supermarket.model;

public class Offer {
    private final SpecialOfferType offerType;
    private final Product product;
    private final double argument;
    private String description;

    public Offer(SpecialOfferType offerType, Product product, double argument) {
        this.offerType = offerType;
        this.argument = argument;
        this.product = product;
        setOfferDescription();
    }

    public SpecialOfferType getOfferType() {
        return offerType;
    }

    public Product getProduct() {
        return this.product;
    }

    public double getArgument() {
        return argument;
    }

    public String getDescription() {
        return description;
    }

    private void setOfferDescription() {
        switch (offerType) {
            case ThreeForTwo:
                description = "3 for 2";
                break;
            case TenPercentDiscount:
                description = "10% off";
                break;
            case TwoForAmount:
                description = "2 for " + argument;
                break;
            case FiveForAmount:
                description = "5 for " + argument;
                break;
        }
    }

}
