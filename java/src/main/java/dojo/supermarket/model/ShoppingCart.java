package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>();
    private final Map<Product, Double> productQuantities = new HashMap<>();


    public List<ProductQuantity> getItems() {
        return new ArrayList<>(items);
    }

    void addItem(Product product) {
        this.addItemQuantity(product, 1.0);
    }

    Map<Product, Double> productQuantities() {
        return productQuantities;
    }


    public void addItemQuantity(Product product, double quantity) {
        items.add(new ProductQuantity(product, quantity));
        productQuantities.put(product, productQuantities.getOrDefault(product, 0.0) + quantity);
    }

    private double calculateDiscountForAmounts(double quantity, double unitPrice, double argument, int freeItemsPerAmount) {
        int roundedQuantity = (int) quantity;
        if (roundedQuantity < freeItemsPerAmount) return 0;
        return unitPrice * quantity - (argument * (roundedQuantity / freeItemsPerAmount) + roundedQuantity % freeItemsPerAmount * unitPrice);
    }

    void handleOffers(Receipt receipt, Map<Product, Offer> offers, SupermarketCatalog catalog) {
        for (Product p: productQuantities().keySet()) {
            if (offers.containsKey(p)) {
                Offer offer = offers.get(p);
                double discountAmount = getDiscountAmount(productQuantities.get(p), offer, catalog.getUnitPrice(p));
                if (discountAmount != 0)
                    receipt.addDiscount(new Discount(p, offer.getDescription(), -discountAmount));
            }
        }
    }

    private double getDiscountAmount(double quantity, Offer offer, double unitPrice) {
        double discountAmount = 0;
        switch (offer.getOfferType()) {
            case TwoForAmount:
                discountAmount = calculateDiscountForAmounts(quantity, unitPrice, offer.getArgument(), 2);
                break;
            case ThreeForTwo:
                discountAmount = calculateDiscountForAmounts(quantity, unitPrice, 2 * unitPrice, 3);
                break;
            case TenPercentDiscount:
                discountAmount = quantity * unitPrice * offer.getArgument() / 100.0;
                break;
            case FiveForAmount:
                discountAmount = calculateDiscountForAmounts(quantity, unitPrice, offer.getArgument(), 5);
                break;
        }
        return discountAmount;
    }
}
