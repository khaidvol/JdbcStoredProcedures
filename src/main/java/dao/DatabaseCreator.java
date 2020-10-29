package dao;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;

public class DatabaseCreator {

    private static final Logger logger = Logger.getRootLogger();
    public static final String SCHEMA = "src/main/resources/db/schema.sql";
    public static final String TEST_DATA = "src/main/resources/db/test-data.sql";


    public static void createTables(){
        executeSqlScript(SCHEMA);
    }

    public static void populateData(){
        executeSqlScript(TEST_DATA);
    }

    private static void executeSqlScript(String sqlScript) {
        try {
            ScriptRunner scriptRunner = new ScriptRunner(Datasource.getConnection());
            scriptRunner.runScript(new BufferedReader(new FileReader(sqlScript)));
        } catch (FileNotFoundException | SQLException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }
}
