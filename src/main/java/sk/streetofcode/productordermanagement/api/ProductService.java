package sk.streetofcode.productordermanagement.api;

import sk.streetofcode.productordermanagement.api.request.ProductAddAmountRequest;
import sk.streetofcode.productordermanagement.api.request.ProductAddRequest;
import sk.streetofcode.productordermanagement.api.request.ProductEditRequest;
import sk.streetofcode.productordermanagement.domain.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAll();
    Product get(long id);
    long getAmount(long id);
    double getPrice(long id);
    Product edit(long id, ProductEditRequest request);
    Product add(ProductAddRequest request);
    long addAmount(long id, ProductAddAmountRequest request);
    void transferToShoppingList(long id, long amount);
    void delete(long id);
}
