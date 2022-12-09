package org.piazza.user;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        return new User(
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("role")
        );
    }
}