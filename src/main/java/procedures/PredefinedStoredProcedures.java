package procedures;

import org.apache.log4j.Logger;

import java.sql.*;

public class PredefinedStoredProcedures {

    private static final Logger logger = Logger.getRootLogger();
    private static final String CALL_PS_SETUP_SHOW_ENABLED_CONSUMERS = "{CALL ps_setup_show_enabled_consumers()}";
    private static final String CALL_PS_SETUP_SHOW_ENABLED_INSTRUMENTS = "{CALL ps_setup_show_enabled_instruments()}";

    private PredefinedStoredProcedures() {
    }

    public static void runProcedurePsSetupShowEnabledConsumers() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "jgmp2020");
             CallableStatement callableStatement = connection.prepareCall(CALL_PS_SETUP_SHOW_ENABLED_CONSUMERS);
             ResultSet resultSet = callableStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                logger.info("enabled_consumers: " + resultSet.getString(1)
                );
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void runProcedurePsSetupShowEnabledInstruments() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "jgmp2020");
             CallableStatement callableStatement = connection.prepareCall(CALL_PS_SETUP_SHOW_ENABLED_INSTRUMENTS);
             ResultSet resultSet = callableStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                logger.info("enabled_instruments: " + resultSet.getString(1) + ", timed: " + resultSet.getString(2)
                );
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
