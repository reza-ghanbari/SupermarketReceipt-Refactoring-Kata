package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>();
    Map<Product, Double> productQuantities = new HashMap<>();


    List<ProductQuantity> getItems() {
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
        if (productQuantities.containsKey(product)) {
            productQuantities.put(product, productQuantities.get(product) + quantity);
        } else {
            productQuantities.put(product, quantity);
        }
    }

    double calculateDiscountForAmounts(double quantity, double unitPrice, double amount, int FreeItemsPerAmount) {
        int roundedQuantity = (int) quantity;
        double numberOfPaidItems = amount * (roundedQuantity / FreeItemsPerAmount) + roundedQuantity % FreeItemsPerAmount;
        return unitPrice * (quantity - numberOfPaidItems);
    }

    void handleOffers(Receipt receipt, Map<Product, Offer> offers, SupermarketCatalog catalog) {
        for (Product p: productQuantities().keySet()) {
            double quantity = productQuantities.get(p);
            if (offers.containsKey(p)) {
                Offer offer = offers.get(p);
                double unitPrice = catalog.getUnitPrice(p);
                int quantityAsInt = (int) quantity;
                Discount discount = null;
                double discountAmount = 0;
                if (offer.getOfferType() == SpecialOfferType.TwoForAmount && quantityAsInt >= 2) {
                    discountAmount = calculateDiscountForAmounts(quantity, unitPrice, offer.getArgument(), 2);
                    discount = new Discount(p, offer.getDescription(), discountAmount);
                }
                if (offer.getOfferType() == SpecialOfferType.ThreeForTwo && quantityAsInt >= 3) {
                    discountAmount = calculateDiscountForAmounts(quantity, unitPrice, 2, 3);
                    discount = new Discount(p, offer.getDescription(), discountAmount);
                }
                if (offer.getOfferType() == SpecialOfferType.FiveForAmount && quantityAsInt >= 5) {
                    discountAmount = calculateDiscountForAmounts(quantity, unitPrice, offer.getArgument(), 5);
                    discount = new Discount(p, offer.getDescription(), discountAmount);
                }
                if (offer.getOfferType() == SpecialOfferType.TenPercentDiscount) {
                    discount = new Discount(p, offer.getDescription(), quantity * unitPrice * offer.getArgument() / 100.0);
                }
                if (discount != null)
                    receipt.addDiscount(discount);
            }

        }
    }
}
