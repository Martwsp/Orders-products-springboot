package sk.streetofcode.productordermanagement.implementation.service;

import org.springframework.stereotype.Service;
import sk.streetofcode.productordermanagement.api.ProductService;
import sk.streetofcode.productordermanagement.api.exception.ResourceNotFoundException;
import sk.streetofcode.productordermanagement.api.request.ProductAddAmountRequest;
import sk.streetofcode.productordermanagement.api.request.ProductAddRequest;
import sk.streetofcode.productordermanagement.api.request.ProductEditRequest;
import sk.streetofcode.productordermanagement.domain.Product;
import sk.streetofcode.productordermanagement.implementation.repository.ProductRepository;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Product> getAll() {
        return repository.getAll();
    }

    @Override
    public Product get(long id) {
        return repository.getById(id);
    }

    @Override
    public long getAmount(long id) {
        return repository.getAmount(id);
    }

    @Override
    public double getPrice(long id) {
        return repository.getPrice(id);
    }

    @Override
    public Product edit(long id, ProductEditRequest request) {
        return repository.update(id, request);
    }

    @Override
    public Product add(ProductAddRequest request) {
        return repository.add(request);
    }

    @Override
    public long addAmount(long id, ProductAddAmountRequest request) {
        return repository.addAmount(id, request);
    }

    @Override
    public void transferToShoppingList(long id, long amount) {
        repository.transferToShoppingList(id, amount);
    }

    @Override
    public void delete(long id) {
        if (repository.getById(id) == null) {
            throw new ResourceNotFoundException("Order " + id + " not found.");
        }
        repository.delete(id);
    }
}
