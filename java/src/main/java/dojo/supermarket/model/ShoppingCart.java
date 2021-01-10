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

    public void addItem(Product product) {
        this.addItemQuantity(product, 1.0);
    }

    public Map<Product, Double> getProductQuantities() {
        return productQuantities;
    }


    public void addItemQuantity(Product product, double quantity) {
        items.add(new ProductQuantity(product, quantity));
        if (productQuantities.containsKey(product)) {
            productQuantities.put(product, productQuantities.get(product) + quantity);
        } else {
            productQuantities.put(product, quantity);
        }
    }

    private double calculateDiscountForAmounts(double quantity, double unitPrice, double amount, int FreeItemsPerAmount) {
        int roundedQuantity = (int) quantity;
        double numberOfPaidItems = amount * (roundedQuantity / FreeItemsPerAmount) + roundedQuantity % FreeItemsPerAmount;
        return unitPrice * (quantity - numberOfPaidItems);
    }

    public void handleOffers(Receipt receipt, Map<Product, Offer> offers, SupermarketCatalog catalog) {
        for (Product product : productQuantities.keySet()) {
            if (offers.containsKey(product)) {
                Offer offer = offers.get(product);
                double discountAmount = getDiscountAmount(productQuantities.get(product), offer, catalog.getUnitPrice(product));
                if (discountAmount != 0)
                    receipt.addDiscount(new Discount(product, offer.getDescription(), discountAmount));
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
                discountAmount = calculateDiscountForAmounts(quantity, unitPrice, 2, 3);
                break;
            case FiveForAmount:
                discountAmount = calculateDiscountForAmounts(quantity, unitPrice, offer.getArgument(), 5);
                break;
            case TenPercentDiscount:
                discountAmount = quantity * unitPrice * offer.getArgument() / 100.0;
                break;
        }
        return discountAmount;
    }
}
