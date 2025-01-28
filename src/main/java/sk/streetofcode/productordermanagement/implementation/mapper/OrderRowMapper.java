package sk.streetofcode.productordermanagement.implementation.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import sk.streetofcode.productordermanagement.domain.Order;
import sk.streetofcode.productordermanagement.domain.ShoppingList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Component
public class OrderRowMapper implements RowMapper<Order> {


    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<ShoppingList> shoppingLists = Collections.emptyList();
        return new Order(
                rs.getLong("id"),
                shoppingLists,
                rs.getBoolean("paid")
        );
    }
}
