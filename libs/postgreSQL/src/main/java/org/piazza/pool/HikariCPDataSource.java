package org.piazza.pool;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class HikariCPDataSource {

    @Autowired
    private HikariDataSource hikariDataSource;

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    public void releaseConnection(Connection conn){
        hikariDataSource.evictConnection(conn);
    }

    public void setConnections(int maxCount){
        hikariDataSource.setMaximumPoolSize(maxCount);
    }

    public void setMinimumIdle(int idle) {
        hikariDataSource.setMinimumIdle(idle);
    }
}