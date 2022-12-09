package org.piazza.user;

import org.piazza.pool.HikariCPDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsersDataAccessService implements UserDao{
//    private final JdbcTemplate jdbcTemplate;

//    public UsersDataAccessService(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//    private final BasicConnectionPool basicConnectionPool;
    @Autowired
    HikariCPDataSource hikariCPDataSource;
//    @Autowired
//    public  UsersDataAccessService(){
//        try {
//            this.basicConnectionPool = BasicConnectionPool.create("jdbc:postgresql://localhost:5432/piazza","postgres","postgres");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
    @Override
    public List<User> selectUsers(){
        var sql = "SELECT email, password, firstName, lastName, role FROM users LIMIT 100;";
        ArrayList<User> users= new ArrayList<>();
        Connection conn = null;
        try {
            conn = hikariCPDataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Statement statement = conn.createStatement();
            ResultSet x = statement.executeQuery(sql);
            while (x.next()) {
                User user = new User(x.getString("email"), x.getString("password"), x.getString("firstName"), x.getString("lastName"), x.getString("role"));
                users.add(user);
            }
            hikariCPDataSource.releaseConnection(conn);
            return users;
        }
        catch (Exception e){
            hikariCPDataSource.releaseConnection(conn);
            System.out.println("Error: "+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUser(String email) {
        var sql = "SELECT * FROM users WHERE email = '"+email+"';";
        Connection conn = null;
        try {
            conn = hikariCPDataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Statement statement = conn.createStatement();
            ResultSet x = statement.executeQuery(sql);
            if(x.next()) {
                User user = new User(x.getString("email"), x.getString("password"), x.getString("firstName"), x.getString("lastName"), x.getString("role"));
                hikariCPDataSource.releaseConnection(conn);
                return user;
            }
            else{
                hikariCPDataSource.releaseConnection(conn);
                return null;
            }

        }
        catch (Exception e){
            hikariCPDataSource.releaseConnection(conn);
            System.out.println("Error: "+e.getMessage());
            throw new RuntimeException(e);
        }
//        return  hikariCPDataSource.getJDBC().queryForObject(sql, new Object[]{email}, new UsersRowMapper());

//        return null;
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM users";
        Connection conn = null;
        try {
            conn = hikariCPDataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            hikariCPDataSource.releaseConnection(conn);
        } catch (SQLException e) {
            hikariCPDataSource.releaseConnection(conn);
            throw new RuntimeException(e);
        }
        return;
    }

    @Override
    public void testConn() throws SQLException {
        Connection conn = hikariCPDataSource.getConnection();
        System.out.println("here1");
        Connection conn2 = hikariCPDataSource.getConnection();
        System.out.println("here2");
        Connection conn3 = hikariCPDataSource.getConnection();
        System.out.println("here3");
        Connection conn4 = hikariCPDataSource.getConnection();
        System.out.println("here4");
        Connection conn5 = hikariCPDataSource.getConnection();
        System.out.println("here5");
//        hikariCPDataSource.setConnections(7);
        Connection conn6 = hikariCPDataSource.getConnection();
        System.out.println("here6");
    }

    @Override
    public int deleteUser(String userEmail) {
        var sql = "DELETE FROM users WHERE email = '" + userEmail + "';";
        Connection conn = null;
        try {
            conn = hikariCPDataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            hikariCPDataSource.releaseConnection(conn);
            return 1;
        } catch (SQLException e) {
            hikariCPDataSource.releaseConnection(conn);
            return 0;
        }
    }
    public void changePassword(String password, String email) {
        var sql = "UPDATE users SET password = '" + password + "' WHERE email = '" + email + "';";
        Connection conn = null;
        try {
            conn = hikariCPDataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            hikariCPDataSource.releaseConnection(conn);
        } catch (SQLException e) {
            hikariCPDataSource.releaseConnection(conn);
            throw new RuntimeException(e);
        }
        return;
    }

    @Override
    public int insertUser(User user) {
        var sql = " INSERT INTO users(email, password, firstName, lastName, role) VALUES ('" + user.getemail() + "', '" + user.getpassword() + "', '" + user.getfirstName() + "', '" + user.getlastName() + "', '" + user.getrole() + "'); ";

        Connection conn = null;
        try {
            conn = hikariCPDataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            Statement statement = conn.createStatement();
            int x = statement.executeUpdate(sql);
            hikariCPDataSource.releaseConnection(conn);

            return x;
        } catch (Exception e) {
            hikariCPDataSource.releaseConnection(conn);
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}