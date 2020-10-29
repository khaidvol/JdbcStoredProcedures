package procedures;

import dao.Datasource;
import org.apache.log4j.Logger;

import java.sql.*;

public class ReportProcedures {

    private static final Logger logger = Logger.getRootLogger();

    private static final String DROP_PROC_SHOW_ALL_DATA = "DROP PROCEDURE IF EXISTS SHOW_ALL_DATA";
    private static final String DROP_PROC_SHOW_COUNTED_DATA = "DROP PROCEDURE IF EXISTS SHOW_COUNTED_DATA";
    private static final String DROP_PROC_SHOW_COUNTED_DATA_FOR_SINGER = "DROP PROCEDURE IF EXISTS SHOW_COUNTED_DATA_FOR_SINGER";

    private static final String CRETE_PROC_SHOW_ALL_DATA =
            "create procedure SHOW_ALL_DATA() " +
            "begin " +
            "SELECT * FROM SINGER\n" +
                    "LEFT JOIN ALBUM ON SINGER.ID = ALBUM.SINGER_ID\n" +
                    "LEFT JOIN TRACK ON ALBUM.ID = TRACK.ALBUM_ID; " +
            "end";

    private static final String CRETE_PROC_SHOW_COUNTED_DATA =
            "create procedure SHOW_COUNTED_DATA() " +
                    "begin " +
                    "SELECT SINGER.ID, SINGER.FIRST_NAME, SINGER.LAST_NAME, SINGER.BIRTH_DATE\n" +
                    ", (SELECT COUNT(ALBUM.ID) FROM ALBUM WHERE ALBUM.SINGER_ID = SINGER.ID) AS NUMBER_OF_ALBUMS\n" +
                    ", COUNT(TRACK.TITLE) AS NUMBER_OF_TRACKS \n" +
                    "FROM SINGER\n" +
                    "LEFT JOIN ALBUM ON SINGER.ID = ALBUM.SINGER_ID\n" +
                    "LEFT JOIN TRACK ON ALBUM.ID = TRACK.ALBUM_ID\n" +
                    "GROUP BY SINGER.ID; " +
                    "end";

    private static final String CRETE_PROC_SHOW_COUNTED_DATA_FOR_SINGER =
            "create procedure SHOW_COUNTED_DATA_FOR_SINGER(IN id INT) " +
                    "begin " +
                    "SELECT SINGER.ID, SINGER.FIRST_NAME, SINGER.LAST_NAME, SINGER.BIRTH_DATE\n" +
                    ", (SELECT COUNT(ALBUM.ID) FROM ALBUM WHERE ALBUM.SINGER_ID = SINGER.ID) AS NUMBER_OF_ALBUMS\n" +
                    ", COUNT(TRACK.TITLE) AS NUMBER_OF_TRACKS \n" +
                    "FROM SINGER\n" +
                    "LEFT JOIN ALBUM ON SINGER.ID = ALBUM.SINGER_ID\n" +
                    "LEFT JOIN TRACK ON ALBUM.ID = TRACK.ALBUM_ID\n" +
                    "WHERE SINGER.ID = id\n" +
                    "GROUP BY SINGER.ID; " +
                    "end";

    private static final String RUN_PROC_SHOW_ALL_DATA = "{CALL SHOW_ALL_DATA}";
    private static final String RUN_PROC_SHOW_COUNTED_DATA = "{CALL SHOW_COUNTED_DATA}";
    private static final String RUN_PROC_SHOW_COUNTED_DATA_FOR_SINGER = "{CALL SHOW_COUNTED_DATA_FOR_SINGER(?)}";

    private ReportProcedures() {
    }

    public static void createProcedureShowAllData() {
        executeSQL(DROP_PROC_SHOW_ALL_DATA);
        executeSQL(CRETE_PROC_SHOW_ALL_DATA);
    }

    public static void createProcedureShowCountedData() {
        executeSQL(DROP_PROC_SHOW_COUNTED_DATA);
        executeSQL(CRETE_PROC_SHOW_COUNTED_DATA);
    }

    public static void createProcedureShowCountedDataForSinger() {
        executeSQL(DROP_PROC_SHOW_COUNTED_DATA_FOR_SINGER);
        executeSQL(CRETE_PROC_SHOW_COUNTED_DATA_FOR_SINGER);
    }

    public static void dropProcedureShowAllData() {
        executeSQL(DROP_PROC_SHOW_ALL_DATA);
    }

    public static void dropProcedureShowCountedData() {
        executeSQL(DROP_PROC_SHOW_COUNTED_DATA);
    }

    public static void dropProcedureShowCountedDataForSinger() {
        executeSQL(DROP_PROC_SHOW_COUNTED_DATA_FOR_SINGER);
    }


    public static void executeSQL(String executeSQL) {

        try(Connection connection = Datasource.getConnection();
            Statement statement = connection.createStatement()
        ) {
            statement.execute(executeSQL);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void runProcedureShowAllData() {
        try(Connection connection = Datasource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(RUN_PROC_SHOW_ALL_DATA);
            ResultSet resultSet = callableStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                logger.info("Singer ID: " + resultSet.getInt(1) + ", " +
                        "First name: " + resultSet.getString(2) + ", " +
                        "Last name: " + resultSet.getString(3) + ", " +
                        "Birthdate: " + resultSet.getDate(4) + ", " +
                        "Album title: " + resultSet.getString(7) + ", " +
                        "Release date: " + resultSet.getDate(8) + ", " +
                        "Track title: " + resultSet.getString(9) + ", " +
                        "Track number: " + resultSet.getInt(11) + ", " +
                        "Track duration: " + resultSet.getInt(12)
                );
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void runProcedureShowCountedData() {
        try(Connection connection = Datasource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(RUN_PROC_SHOW_COUNTED_DATA);
            ResultSet resultSet = callableStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                logger.info("Singer ID: " + resultSet.getInt(1) + ", " +
                        "First name: " + resultSet.getString(2) + ", " +
                        "Last name: " + resultSet.getString(3) + ", " +
                        "Birthdate: " + resultSet.getDate(4) + ", " +
                        "Number of Albums: " + resultSet.getInt(5) + ", " +
                        "Number of Tracks: " + resultSet.getInt(6)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void runProcedureShowCountedDataForSinger(int id) {
        try(Connection connection = Datasource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(RUN_PROC_SHOW_COUNTED_DATA_FOR_SINGER);
        ) {
            callableStatement.setInt(1, id);
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                logger.info("Singer ID: " + resultSet.getInt(1) + ", " +
                        "First name: " + resultSet.getString(2) + ", " +
                        "Last name: " + resultSet.getString(3) + ", " +
                        "Birthdate: " + resultSet.getDate(4) + ", " +
                        "Number of Albums: " + resultSet.getInt(5) + ", " +
                        "Number of Tracks: " + resultSet.getInt(6)
                );
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
