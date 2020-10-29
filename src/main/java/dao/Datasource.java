package dao;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Datasource {
    private static final Logger logger = Logger.getRootLogger();

    private Datasource() {
    }

    public static Connection getConnection() throws SQLException {

        Properties prop = new Properties();

        try (InputStream input = new FileInputStream("src/main/resources/jdbc.properties")) {
            prop.load(input);

        } catch (FileNotFoundException e) {
            logger.error(("File not found " + e.getMessage()));
            e.printStackTrace();

        } catch (IOException e) {
            logger.error((e.getMessage()));
            e.printStackTrace();
        }

        return DriverManager.getConnection(
                prop.getProperty("jdbc.url"),
                prop.getProperty("jdbc.username"),
                prop.getProperty("jdbc.password")
        );
    }
}
