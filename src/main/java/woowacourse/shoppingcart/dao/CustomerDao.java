package woowacourse.shoppingcart.dao;

import java.util.Locale;
import javax.sql.DataSource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import woowacourse.shoppingcart.domain.Customer;
import woowacourse.shoppingcart.exception.DuplicateUsernameException;
import woowacourse.shoppingcart.exception.InvalidCustomerException;

@Repository
public class CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private RowMapper<Customer> customerRowMapper = (rs, rowNum) -> new Customer(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password")
    );

    public CustomerDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("CUSTOMER")
                .usingGeneratedKeyColumns("id");
    }

    public Customer save(Customer customer) {
        String username = customer.getUsername();
        String email = customer.getEmail();
        String password = customer.getPassword();

        SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("username", username.toLowerCase(Locale.ROOT))
                    .addValue("email", email)
                    .addValue("password", password);

        long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Customer(id, username, email, password);
    }

    public Customer findByUsername(String username) {
        try {
            final String query = "SELECT id, username, email, password FROM customer WHERE username = ?";
            return jdbcTemplate.queryForObject(query, customerRowMapper, username.toLowerCase(Locale.ROOT));
        } catch (final EmptyResultDataAccessException e) {
            throw new InvalidCustomerException();
        }
    }

    public boolean existByUserName(String username) {
        try {
            String query = "SELECT EXISTS (SELECT * FROM customer WHERE username = ?)";
            return jdbcTemplate.queryForObject(query, Boolean.class, username);
        } catch (final EmptyResultDataAccessException e) {
            throw new InvalidCustomerException();
        }
    }

    public boolean existByEmail(String email) {
        try {
            String query = "SELECT EXISTS (SELECT * FROM customer WHERE email = ?)";
            return jdbcTemplate.queryForObject(query, Boolean.class, email);
        } catch (final EmptyResultDataAccessException e) {
            throw new InvalidCustomerException();
        }
    }

    public boolean isValidPasswordByUsername(String username, String password) {
        try {
            final String query = "SELECT EXISTS (SELECT * FROM customer WHERE username = ? AND password = ?)";
            return jdbcTemplate.queryForObject(query, Boolean.class, username, password);
        } catch (final EmptyResultDataAccessException e) {
            throw new InvalidCustomerException();
        }
    }

    public boolean isValidPasswordByEmail(String email, String password) {
        try {
            final String query = "SELECT EXISTS (SELECT * FROM customer WHERE email = ? AND password = ?)";
            return jdbcTemplate.queryForObject(query, Boolean.class, email, password);
        } catch (final EmptyResultDataAccessException e) {
            throw new InvalidCustomerException();
        }
    }

    public void updatePassword(Long id, String password) {
        final String query = "UPDATE CUSTOMER SET password = ? WHERE id = ?";
        jdbcTemplate.update(query, password, id);
    }

    public void deleteByUserName(String username) {
        final String query = "DELETE FROM customer WHERE username = ?";
        jdbcTemplate.update(query, username);
    }

    public Customer findByEmail(String email) {
        try {
            final String query = "SELECT id, username, email, password FROM customer WHERE email = ?";
            return jdbcTemplate.queryForObject(query, customerRowMapper, email);
        } catch (final EmptyResultDataAccessException e) {
            throw new InvalidCustomerException();
        }
    }
}
