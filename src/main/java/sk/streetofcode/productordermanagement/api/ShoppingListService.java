package sk.streetofcode.productordermanagement.api;

import sk.streetofcode.productordermanagement.api.request.OrderAddShoppingListRequest;
import sk.streetofcode.productordermanagement.domain.ShoppingList;

import java.util.List;

public interface ShoppingListService {
    List<ShoppingList> getByOrder(long orderId);
    void insertShoppingList(long id, OrderAddShoppingListRequest request);
    void delete(long id);
}
