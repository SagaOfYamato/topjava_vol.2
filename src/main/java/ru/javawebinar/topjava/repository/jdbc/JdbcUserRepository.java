package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository extends AbstractJdbcRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        validate(user);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            addRoles(user);
        } else if (namedParameterJdbcTemplate.update("""
               UPDATE users SET name=:name, email=:email, password=:password, 
               registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
            """, parameterSource) == 0) {
            return null;
        } else {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id = ?", user.getId());
            addRoles(user);
        }
        return user;
    }

    private void addRoles(User user) {
        Set<Role> roles = user.getRoles();
        if (roles != null && !roles.isEmpty()) {
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, user.getId());
                            int j = 0;
                            for (Role role : roles) {
                                if (i == j++) {
                                    ps.setString(2, role.name());
                                    break;
                                }
                            }
                        }

                        @Override
                        public int getBatchSize() {
                            return roles.size();
                        }
                    });
        }
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        final String sql = "SELECT u.id, u.name, u.email, u.password, u.enabled, u.registered, u.calories_per_day, " +
                "r.role " +
                "FROM users u LEFT JOIN user_roles r ON u.id = r.user_id " +
                "WHERE u.id = ?";
        User user = jdbcTemplate.query(sql, new Object[]{id}, this::getMappedUser);
        return user;
    }

    @Override
    public User getByEmail(String email) {
        final String sql = "SELECT u.id, u.name, u.email, u.password, u.enabled, u.registered, u.calories_per_day, " +
                "r.role " +
                "FROM users u LEFT JOIN user_roles r ON u.id = r.user_id " +
                "WHERE u.email = ?";
        User user = jdbcTemplate.query(sql, new Object[]{email}, this::getMappedUser);

        return user;
    }

    public User getMappedUser (ResultSet rs) throws SQLException {
        User user = null;
        Set<Role> roles = new HashSet<>();

        while (rs.next()) {
            if (user == null) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setRegistered(rs.getTimestamp("registered"));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
            }

            String roleName = rs.getString("role");
            if (roleName != null) {
                Role role = Role.valueOf(roleName);
                roles.add(role);
            }
        }

        if (user != null) {
            user.setRoles(roles);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        final String sql = "SELECT u.id, u.name, u.email, u.password, u.enabled, u.registered, u.calories_per_day, " +
                "r.role " +
                "FROM users u LEFT JOIN user_roles r ON u.id = r.user_id " +
                "ORDER BY u.name, u.email";

        Map<Integer, User> users = new LinkedHashMap<>();
        jdbcTemplate.query(sql, rs -> {
            int id = rs.getInt("id");
            User user = users.get(id);
            if (user == null) {
                user = new User();
                user.setId(id);
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setRegistered(rs.getTimestamp("registered"));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                user.setRoles(new HashSet<>());
                users.put(id, user);
            }
            String role = rs.getString("role");
            if (role != null) {
                user.getRoles().add(Role.valueOf(role));
            }
        });
        return new ArrayList<>(users.values());
    }
}
