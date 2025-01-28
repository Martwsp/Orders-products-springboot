package sk.streetofcode.productordermanagement.implementation.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import sk.streetofcode.productordermanagement.api.exception.InternalErrorException;
import sk.streetofcode.productordermanagement.api.exception.ResourceNotFoundException;
import sk.streetofcode.productordermanagement.domain.Order;
import sk.streetofcode.productordermanagement.implementation.mapper.OrderRowMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Statement;
import java.util.List;

@Repository
public class OrderRepository {
    private final JdbcTemplate jdbcTemplate;
    private final OrderRowMapper orderRowMapper;

    private static final Logger logger;
    private static final String GET_ALL;
    private static final String GET_BY_ID;
    private static final String INSERT;
    private static final String UPDATE_PAID;
    private static final String DELETE;

    static {
        logger = LoggerFactory.getLogger(OrderRepository.class);
        GET_ALL = "SELECT * FROM order";
        GET_BY_ID = "SELECT * FROM order WHERE id = ?";
        INSERT = "INSERT INTO order(id, paid) VALUES (next value for order_id_seq, false)";
        UPDATE_PAID = "UPDATE order SET paid = true WHERE id = ?";
        DELETE = "DELETE FROM order WHERE id = ?";
    }


    public OrderRepository(JdbcTemplate jdbcTemplate, OrderRowMapper orderRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderRowMapper = orderRowMapper;
    }

    public List<Order> getAll() {
        try {
            return jdbcTemplate.query(GET_ALL, orderRowMapper);
        } catch (DataAccessException e) {
            logger.error("Error while getting all orders", e);
            throw new InternalErrorException("Error while getting all orders");
        }
    }

    public Order getById(long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, orderRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Order with id " + id + " not found");
        } catch (DataAccessException e) {
            logger.error("Error while getting order", e);
            throw new InternalErrorException("Error while getting order");
        }
    }


    public Order add() {
        try {
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS), keyHolder);

            if (keyHolder.getKey() == null) {
                logger.error("Error while adding new order, keyHolder.getKey() is null");
                throw new InternalErrorException("Error while adding new order");
            }

            return getById(keyHolder.getKey().longValue());
        } catch (DataAccessException e) {
            logger.error("Error while adding new order", e);
            throw new InternalErrorException("Error while adding new order");
        }
    }

    public void updatePaid(long id) {
        try {
            jdbcTemplate.update(UPDATE_PAID, id);
        } catch (DataAccessException e) {
            logger.error("Error while setting oder as paid", e);
            throw new InternalErrorException("Error while setting oder as paid");
        }
    }


    public void delete(long id) {
        try {
            jdbcTemplate.update(DELETE, id);
        } catch (DataAccessException e) {
            logger.error("Error while deleting order", e);
            throw new InternalErrorException("Error while deleting order");
        }
    }
}
