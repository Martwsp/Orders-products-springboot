package sk.streetofcode.productordermanagement.implementation.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import sk.streetofcode.productordermanagement.api.exception.BadRequestException;
import sk.streetofcode.productordermanagement.api.exception.InternalErrorException;
import sk.streetofcode.productordermanagement.api.exception.ResourceNotFoundException;
import sk.streetofcode.productordermanagement.api.request.ProductAddAmountRequest;
import sk.streetofcode.productordermanagement.api.request.ProductAddRequest;
import sk.streetofcode.productordermanagement.api.request.ProductEditRequest;
import sk.streetofcode.productordermanagement.domain.Product;
import sk.streetofcode.productordermanagement.implementation.mapper.ProductRowMapper;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ProductRowMapper productRowMapper;

    private static final Logger logger;
    private static final String GET_ALL;
    private static final String GET_BY_ID;
    private static final String GET_AMOUNT_BY_ID;
    private static final String GET_PRICE_BY_ID;
    private static final String UPDATE;
    private static final String INSERT;
    private static final String UPDATE_AMOUNT;
    private static final String DELETE;

    static {
        logger = LoggerFactory.getLogger(ProductRepository.class);
        GET_ALL = "SELECT * FROM product";
        GET_BY_ID = "SELECT * FROM product WHERE id = ?";
        GET_AMOUNT_BY_ID = "SELECT amount FROM product WHERE id = ?";
        GET_PRICE_BY_ID = "SELECT price FROM product WHERE id = ?";
        UPDATE = "UPDATE product SET name = ?, description = ? WHERE id = ?";
        INSERT = "INSERT INTO product(id, name, description, amount, price) VALUES (next value for product_id_seq, ?, ?, ?, ?)";
        UPDATE_AMOUNT = "UPDATE product SET amount = ? WHERE id = ?";
        DELETE = "DELETE FROM product WHERE id = ?";
    }

    public ProductRepository(JdbcTemplate jdbcTemplate, ProductRowMapper productRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.productRowMapper = productRowMapper;
    }

    public List<Product> getAll() {
        try {
            return jdbcTemplate.query(GET_ALL, productRowMapper);
        } catch (DataAccessException e) {
            logger.error("Error while getting all products", e);
            throw new InternalErrorException("Error while getting all products");
        }
    }

    public Product getById(long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, productRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Product with id " + id + " not found");
        } catch (DataAccessException e) {
            logger.error("Error while getting product by id", e);
            throw new InternalErrorException("Error while getting product");
        }
    }

    public long getAmount(long id) {
        try {
            return jdbcTemplate.queryForObject(GET_AMOUNT_BY_ID, long.class, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Product with id " + id + " not found");
        } catch (DataAccessException e) {
            logger.error("Error while getting product amount", e);
            throw new InternalErrorException("Error while getting product amount");
        }
    }

    public double getPrice(long id) {
        try {
            return jdbcTemplate.queryForObject(GET_PRICE_BY_ID, double.class, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Product with id " + id + " not found");
        } catch (DataAccessException e) {
            logger.error("Error while getting product price", e);
            throw new InternalErrorException("Error while getting product price");
        }
    }

    public Product update(long id, ProductEditRequest request) {
        try {
            jdbcTemplate.update(UPDATE, request.getName(), request.getDescription(), id);
            return getById(id);
        } catch (DataAccessException e) {
            logger.error("Error while updating product", e);
            throw new InternalErrorException("Error while updating product");
        }
    }

    public Product add(ProductAddRequest request) {
        try {
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                final PreparedStatement ps = connection.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, request.getName());
                if (request.getDescription() != null) {
                    ps.setString(2, request.getDescription());
                } else {
                    ps.setNull(2, Types.VARCHAR);
                }
                ps.setLong(3, request.getAmount());
                ps.setDouble(4, request.getPrice());
                return ps;
            }, keyHolder);

            if (keyHolder.getKey() == null) {
                logger.error("Error while adding product, keyHolder.getKey() is null");
                throw new InternalErrorException("Error while adding product");
            }

            return getById(keyHolder.getKey().longValue());
        } catch (DataAccessException e) {
            logger.error("Error while adding product", e);
            throw new InternalErrorException("Error while adding product");
        }

    }

    public long addAmount(long id, ProductAddAmountRequest request) {
        try {
            long newAmount = getAmount(id) + request.getAmount();
            jdbcTemplate.update(UPDATE_AMOUNT, newAmount, id);
            return newAmount;
        } catch (DataAccessException e) {
            logger.error("Error while updating product amount", e);
            throw new InternalErrorException("Error while updating product amount");
        }
    }

    public void transferToShoppingList(long id, long amount) {
        try {
            long newAmount = getAmount(id) - amount;
            if (newAmount >= 0) {
                jdbcTemplate.update(UPDATE_AMOUNT, newAmount, id);
            } else {
                throw new BadRequestException("Insufficient product amount");
            }
        } catch (DataAccessException e) {
            logger.error("Error while transferring product", e);
            throw new InternalErrorException("Error while transferring product");
        }
    }

    public void delete(long id) {
        try {
            jdbcTemplate.update(DELETE, id);
        } catch (DataAccessException e) {
            logger.error("Error while deleting product", e);
            throw new InternalErrorException("Error while deleting product");
        }
    }
}
