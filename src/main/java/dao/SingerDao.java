package dao;

import entities.Singer;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SingerDao {

    private static final Logger logger = Logger.getRootLogger();

    private static final String FIND_ALL_SINGERS = "SELECT * FROM SINGER";
    private static final String INSERT_SINGER = "INSERT INTO SINGER (FIRST_NAME, LAST_NAME, BIRTH_DATE) VALUES (?, ?, ?)";
    private static final String DELETE_SINGER = "DELETE FROM SINGER WHERE ID = ?";

    public SingerDao() {
    }

    public static List<Singer> findAllSingers() {
        List<Singer> singers = new ArrayList<>();
        try(Connection connection = Datasource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SINGERS)
        ){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Singer singer = new Singer();
                singer.setId(resultSet.getLong(1));
                singer.setFirstName(resultSet.getString(2));
                singer.setLastName(resultSet.getString(3));
                singer.setBirthDate(resultSet.getDate(4));
                singers.add(singer);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return singers;
    }

    public static void insertSinger(Singer singer) {
        try(Connection connection = Datasource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SINGER, Statement.RETURN_GENERATED_KEYS)
        ){
            preparedStatement.setString(1, singer.getFirstName());
            preparedStatement.setString(2, singer.getFirstName());
            preparedStatement.setDate(3, singer.getBirthDate());

            if(preparedStatement.execute()) {
                logger.info(singer.getFirstName() + " " + singer.getLastName() + " - singer added to the db");
            }
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                singer.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static void deleteSinger(Long singerId) {
        try(Connection connection = Datasource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SINGER)
        ){
            preparedStatement.setLong(1, singerId);
            preparedStatement.execute();

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }


}
