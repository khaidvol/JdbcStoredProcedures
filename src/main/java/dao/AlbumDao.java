package dao;

import entities.Album;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumDao {

    private static final Logger logger = Logger.getRootLogger();

    private static final String FIND_ALL_ALBUMS = "SELECT * FROM ALBUM";
    private static final String INSERT_ALBUM = "INSERT INTO ALBUM (SINGER_ID, TITLE, RELEASE_DATE) VALUES (?, ?, ?)";
    private static final String DELETE_ALBUM = "DELETE FROM ALBUM WHERE ID = ?";

    private AlbumDao() {
    }

    public static List<Album> findAllAlbums() {
        List<Album> albums = new ArrayList<>();
        try (Connection connection = Datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_ALBUMS)
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Album album = new Album();
                album.setId(resultSet.getLong(1));
                album.setSingerId(resultSet.getLong(2));
                album.setTitle(resultSet.getString(3));
                album.setReleaseDate(resultSet.getDate(4));
                albums.add(album);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return albums;
    }

    public static void insertAlbum(Album album) {
        try (Connection connection = Datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ALBUM, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setLong(1, album.getSingerId());
            preparedStatement.setString(2, album.getTitle());
            preparedStatement.setDate(3, album.getReleaseDate());
            if (preparedStatement.execute()) {
                logger.info(album.getTitle() + " - album added to the db");
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                album.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static void deleteAlbum(Long albumId) {
        try (Connection connection = Datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALBUM)
        ) {
            preparedStatement.setLong(1, albumId);
            preparedStatement.execute();

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
