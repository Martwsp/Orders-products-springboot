package sk.streetofcode.productordermanagement.implementation.service;

import org.springframework.stereotype.Service;
import sk.streetofcode.productordermanagement.api.OrderService;
import sk.streetofcode.productordermanagement.api.exception.BadRequestException;
import sk.streetofcode.productordermanagement.api.exception.ResourceNotFoundException;
import sk.streetofcode.productordermanagement.api.request.OrderAddShoppingListRequest;
import sk.streetofcode.productordermanagement.domain.Order;
import sk.streetofcode.productordermanagement.domain.ShoppingList;
import sk.streetofcode.productordermanagement.implementation.repository.OrderRepository;
import sk.streetofcode.productordermanagement.implementation.repository.ProductRepository;
import sk.streetofcode.productordermanagement.implementation.repository.ShoppingListRepository;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;
    private final ShoppingListRepository shoppingListRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository repository, ShoppingListRepository shoppingListRepository, ProductRepository productRepository) {
        this.repository = repository;
        this.shoppingListRepository = shoppingListRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = repository.getAll();
        for (Order order : orders) {
            List<ShoppingList> shoppingLists = shoppingListRepository.getByOrder(order.getId());
            for (ShoppingList shoppingList : shoppingLists) {
                order.addShoppingList(shoppingList);
            }
        }
        return orders;
    }

    @Override
    public Order getById(long id) {
        Order order = repository.getById(id);
        List<ShoppingList> shoppingLists = shoppingListRepository.getByOrder(order.getId());
        for (ShoppingList shoppingList : shoppingLists) {
            order.addShoppingList(shoppingList);
        }
        return order;
    }

    @Override
    public Order add() {
        return repository.add();
    }

    @Override
    public double pay(long id) {
        if (repository.getById(id).getPaid()) {
            throw new BadRequestException("Order already paid");
        }
        repository.updatePaid(id);
        return totalPrice(id);
    }

    @Override
    public void addShoppingList(long id, OrderAddShoppingListRequest request) {
        if (getById(id).getPaid()) {
            throw new BadRequestException("Order is already paid and can't be updated.");
        }
        long available = productRepository.getAmount(request.getProductId());
        if (available < request.getAmount()) {
            throw new BadRequestException("Insufficient product");
        }
        if (getById(id).getShoppingList().stream().map(ShoppingList::getProductId).noneMatch(n -> n == request.getProductId())) {
            productRepository.transferToShoppingList(request.getProductId(), request.getAmount());
            shoppingListRepository.insertShoppingList(id, request);
        } else {
            for (ShoppingList shoppingList : getById(id).getShoppingList()) {
                if (request.getProductId() == shoppingList.getProductId()) {
                    shoppingListRepository.updateAmount(shoppingList.getId(), shoppingList.getAmount() + request.getAmount());
                    productRepository.transferToShoppingList(request.getProductId(), request.getAmount());
                }
            }
        }
    }

    @Override
    public void delete(long id) {
        if (repository.getById(id) == null) {
            throw new ResourceNotFoundException("Order " + id + " not found.");
        }
        if (this.getById(id) != null) {
            for (ShoppingList shoppinglist : shoppingListRepository.getByOrder(id)) {
                shoppingListRepository.delete(shoppinglist.getId());
            }
            repository.delete(id);
        }
    }

    public double totalPrice(long id) {
        List<ShoppingList> shoppingLists = shoppingListRepository.getByOrder(id);
        double totalPrice = 0;
        for (ShoppingList shoppingList : shoppingLists) {
            double listPrice = shoppingList.getAmount() * productRepository.getPrice(shoppingList.getProductId());
            totalPrice = totalPrice + listPrice;
        }
        return totalPrice;
    }
}
