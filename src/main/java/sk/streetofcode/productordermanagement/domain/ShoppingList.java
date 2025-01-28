package sk.streetofcode.productordermanagement.domain;

import lombok.Value;

@Value
public class ShoppingList {
    long id;
    long orderId;
    long productId;
    long amount;
}
