package sk.streetofcode.productordermanagement.implementation.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import sk.streetofcode.productordermanagement.domain.Order;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class OrderRowMapper implements RowMapper<Order> {


    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Order(
                rs.getLong("id"),
                rs.getBoolean("paid")
        );
    }
}
