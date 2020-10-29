package procedures;

import dao.Datasource;
import entities.Singer;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SingerProcedures {

    private static final Logger logger = Logger.getRootLogger();

    private static final String DROP_PROC_INSERT_SINGER = "DROP PROCEDURE IF EXISTS INSERT_SINGER";
    private static final String DROP_PROC_DELETE_SINGER = "DROP PROCEDURE IF EXISTS DELETE_SINGER";
    private static final String DROP_PROC_SHOW_ALL_SINGERS = "DROP PROCEDURE IF EXISTS SHOW_ALL_SINGERS";


    private static final String CREATE_PROC_INSERT_SINGER  =
            "create procedure INSERT_SINGER(IN firstName VARCHAR(255), IN lastName VARCHAR(255), IN date DATE) " +
                    "begin " +
                    "INSERT INTO SINGER (FIRST_NAME, LAST_NAME, BIRTH_DATE) VALUES (firstName, lastName, date); " +
                    "end";

    private static final String CREATE_PROC_DELETE_SINGER  =
            "create procedure DELETE_SINGER(IN id INT) " +
                    "begin " +
                    "DELETE FROM SINGER WHERE ID = id; " +
                    "end";

    private static final String CREATE_PROC_SHOW_ALL_SIGNERS  =
            "create procedure SHOW_ALL_SINGERS() " +
                    "begin " +
                    "SELECT * FROM SINGER; " +
                    "end";

    private static final String RUN_PROC_INSERT_SINGER = "{CALL INSERT_SINGER(?, ?, ?)}";
    private static final String RUN_PROC_DELETE_SINGER = "{CALL DELETE_SINGER(?)}";
    private static final String RUN_PROC_SHOW_ALL_SINGERS = "{CALL SHOW_ALL_SINGERS()}";

    private SingerProcedures() {
    }

    public static void createProcedureInsertSinger() {
        executeSQL(DROP_PROC_INSERT_SINGER);
        executeSQL(CREATE_PROC_INSERT_SINGER);

    }

    public static void createProcedureDeleteSinger(){
        executeSQL(DROP_PROC_DELETE_SINGER);
        executeSQL(CREATE_PROC_DELETE_SINGER);
    }

    public static void createProcedureShowAllSingers(){
        executeSQL(DROP_PROC_SHOW_ALL_SINGERS);
        executeSQL(CREATE_PROC_SHOW_ALL_SIGNERS);
    }

    public static void dropProcedureInsertSinger(){
        executeSQL(DROP_PROC_INSERT_SINGER);
    }

    public static void dropProcedureDeleteSinger(){
        executeSQL(DROP_PROC_DELETE_SINGER);
    }

    public static void dropProcedureShowAllSingers(){
        executeSQL(DROP_PROC_SHOW_ALL_SINGERS);
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

    public static List<Singer> runProcedureShowAllSingers() {
        List<Singer> singers = new ArrayList<>();
        try(Connection connection = Datasource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(RUN_PROC_SHOW_ALL_SINGERS);
            ResultSet resultSet = callableStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                Singer singer = new Singer();
                singer.setId(resultSet.getLong(1));
                singer.setFirstName(resultSet.getString(2));
                singer.setLastName(resultSet.getString(3));
                singer.setBirthDate(resultSet.getDate(4));
                logger.info(singer.toString());
                singers.add(singer);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return singers;
    }

    public static void runProcedureInsertSinger(Singer singer) {
        try(Connection connection = Datasource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(RUN_PROC_INSERT_SINGER);
        ) {
            callableStatement.setString(1, singer.getFirstName());
            callableStatement.setString(2, singer.getLastName());
            callableStatement.setDate(3, singer.getBirthDate());
            callableStatement.execute();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void runProcedureDeleteSinger(long singerId) {
        try(Connection connection = Datasource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(RUN_PROC_DELETE_SINGER);
        ) {
            callableStatement.setLong(1, singerId);
            callableStatement.execute();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
