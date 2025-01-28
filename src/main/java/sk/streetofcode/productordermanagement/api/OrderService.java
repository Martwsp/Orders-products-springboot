package sk.streetofcode.productordermanagement.api;

import sk.streetofcode.productordermanagement.api.request.OrderAddShoppingListRequest;
import sk.streetofcode.productordermanagement.domain.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAll();
    Order getById(long id);
    Order add();
    double pay(long id);
    void addShoppingList(long id, OrderAddShoppingListRequest request);
    void delete(long id);
}
