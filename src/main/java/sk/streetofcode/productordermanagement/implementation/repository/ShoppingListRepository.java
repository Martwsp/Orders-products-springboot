package sk.streetofcode.productordermanagement.implementation.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import sk.streetofcode.productordermanagement.api.exception.InternalErrorException;
import sk.streetofcode.productordermanagement.api.exception.ResourceNotFoundException;
import sk.streetofcode.productordermanagement.api.request.OrderAddShoppingListRequest;
import sk.streetofcode.productordermanagement.domain.ShoppingList;
import sk.streetofcode.productordermanagement.implementation.mapper.ShoppingListRowMapper;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ShoppingListRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ShoppingListRowMapper shoppingListRowMapper;

    private static final Logger logger;
    private static final String GET_BY_ORDER;
    private static final String INSERT;
    private static final String UPDATE_AMOUNT;
    private static final String DELETE;

    static {
        logger = LoggerFactory.getLogger(ShoppingListRepository.class);
        GET_BY_ORDER = "SELECT * FROM shopping_list WHERE order_id = ?";
        INSERT = "INSERT INTO shopping_list (id, order_id, product_id, amount) VALUES (next value for shopping_list_id_seq, ?, ?, ?)";
        UPDATE_AMOUNT = "UPDATE shopping_list SET amount = ? WHERE id = ?";
        DELETE = "DELETE FROM shopping_list WHERE id = ?";

    }

    public ShoppingListRepository(JdbcTemplate jdbcTemplate, ShoppingListRowMapper shoppingListRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.shoppingListRowMapper = shoppingListRowMapper;
    }

    public List<ShoppingList> getByOrder(long orderId) {
        try {
            return jdbcTemplate.query(GET_BY_ORDER, shoppingListRowMapper, orderId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Order with id " + orderId + " not found");
        } catch (DataAccessException e) {
            logger.error("Error while getting shopping lists", e);
            throw new InternalErrorException("Error while getting shopping lists");
        }
    }

    public long insertShoppingList(long id, OrderAddShoppingListRequest request) {
        try {
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                final PreparedStatement ps = connection.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setLong(1, id);
                ps.setLong(2, request.getProductId());
                ps.setLong(3, request.getAmount());
                return ps;
            }, keyHolder);

            if (keyHolder.getKey() == null) {
                logger.error("Error while adding shopping list, keyHolder.getKey() is null");
                throw new InternalErrorException("Error while adding shopping list");
            }

            return keyHolder.getKey().longValue();
        } catch (DataAccessException e) {
            logger.error("Error while adding shopping list");
            throw new InternalErrorException("Error while adding shopping list");
        }
    }

    public void updateAmount(long id, long newAmount){
        try {
            jdbcTemplate.update(UPDATE_AMOUNT, newAmount, id);
        } catch (DataAccessException e) {
            logger.error("Error while updating product", e);
            throw new InternalErrorException("Error while updating product");
        }
    }

    public void delete(long id) {
        try {

            jdbcTemplate.update(DELETE, id);
        } catch (DataAccessException e) {
            logger.error("Error while deleting shopping list", e);
            throw new InternalErrorException("Error while deleting shopping list");
        }
    }
}
