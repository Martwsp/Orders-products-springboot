package sk.streetofcode.productordermanagement.domain;

import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Value
public class Order {
    long id;
    List<ShoppingList> shoppingList = new ArrayList<>(Collections.emptyList());
    Boolean Paid;

    public void addShoppingList(ShoppingList shoppingList) {
        this.shoppingList.add(shoppingList);
    }
}

