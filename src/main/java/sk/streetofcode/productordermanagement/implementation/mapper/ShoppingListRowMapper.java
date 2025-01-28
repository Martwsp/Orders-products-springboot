package sk.streetofcode.productordermanagement.implementation.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import sk.streetofcode.productordermanagement.domain.ShoppingList;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ShoppingListRowMapper implements RowMapper<ShoppingList> {
    @Override
    public ShoppingList mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ShoppingList(
                rs.getLong("id"),
                rs.getLong("order_Id"),
                rs.getLong("product_Id"),
                rs.getLong("amount")
        );
    }
}
