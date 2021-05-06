package edu.wpi.cs3733.D21.teamE.database;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// where do we change the embedded driver to the client driver
/**
 * This is a Guice Module config class. This module forces guice to provide an instance of the
 * DatabaseService as a singleton.
 */
public class DatabaseServiceProvider extends AbstractModule {

    private final String realDbUrl = "jdbc:derby://localhost:1527/BWDB";

    @Override
    protected void configure() {}

    /**
     * Provide single connection for database access.
     *
     * @throws SQLException if connection cannot be made
     */
    @Provides
    @Singleton
    public Connection provideConnection() throws SQLException {
        return DriverManager.getConnection(realDbUrl);
    }
}
