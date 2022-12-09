package org.piazza.user;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    List<User> selectUsers() throws SQLException;
    int insertUser(User movie) throws SQLException;
    int deleteUser(String x);
    User getUser(String x) throws SQLException;
    void changePassword(String password, String email);
    void deleteAll();

    void testConn() throws SQLException;
}