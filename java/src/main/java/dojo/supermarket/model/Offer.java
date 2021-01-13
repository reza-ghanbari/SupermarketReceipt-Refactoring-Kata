package dojo.supermarket.model;

import java.util.HashMap;

public class Offer {
    private final SpecialOfferType offerType;
    private final Product product;
    private final double amount;
    private String description;

    private HashMap<Product, Double> bundleList;

    public Offer(SpecialOfferType offerType, Product product, double amount) {
        this.offerType = offerType;
        this.amount = amount;
        this.product = product;
        setOfferDescription();
    }

    public Offer(SpecialOfferType offerType, HashMap<Product, Double> bundleList) {
        this.offerType = offerType;
        this.product = null;
        this.amount = 0;
        this.bundleList = bundleList;
        this.description = "10% off for buy all the bundle";
    }

    public HashMap<Product, Double> getBundleList() { return bundleList; }

    public SpecialOfferType getOfferType() {
        return offerType;
    }

    public Product getProduct() {
        return this.product;
    }

    public double getAmount() {
        return amount;
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
                description = "10.0% off";
                break;
            case TwoForAmount:
                description = "2 for " + amount;
                break;
            case FiveForAmount:
                description = "5 for " + amount;
                break;
        }
    }

}
