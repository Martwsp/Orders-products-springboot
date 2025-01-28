package sk.streetofcode.productordermanagement.implementation.service;

import org.springframework.stereotype.Service;
import sk.streetofcode.productordermanagement.api.ShoppingListService;
import sk.streetofcode.productordermanagement.api.request.OrderAddShoppingListRequest;
import sk.streetofcode.productordermanagement.domain.ShoppingList;
import sk.streetofcode.productordermanagement.implementation.repository.ShoppingListRepository;

import java.util.List;

@Service
public class ShoppingListServiceImpl implements ShoppingListService {
    private final ShoppingListRepository repository;

    public ShoppingListServiceImpl(ShoppingListRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ShoppingList> getByOrder(long orderId) {
        return repository.getByOrder(orderId);
    }

    @Override
    public void insertShoppingList(long id, OrderAddShoppingListRequest request) {
        repository.insertShoppingList(id, request);
    }

    @Override
    public void delete(long id) {
        repository.delete(id);
    }
}
