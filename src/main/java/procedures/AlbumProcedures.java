package procedures;

import dao.Datasource;
import entities.Album;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumProcedures {

    private static final Logger logger = Logger.getRootLogger();

    private static final String DROP_PROC_INSERT_ALBUM = "DROP PROCEDURE IF EXISTS INSERT_ALBUM";
    private static final String DROP_PROC_DELETE_ALBUM = "DROP PROCEDURE IF EXISTS DELETE_ALBUM";
    private static final String DROP_PROC_SHOW_ALL_ALBUMS = "DROP PROCEDURE IF EXISTS SHOW_ALL_ALBUMS";

    private static final String CREATE_PROC_INSERT_ALBUM =
            "create procedure INSERT_ALBUM(IN singerId INT, IN title VARCHAR(255), IN date DATE) " +
                    "begin " +
                    "INSERT INTO ALBUM (SINGER_ID, TITLE, RELEASE_DATE) VALUES (singerId, title, date); " +
                    "end";

    private static final String CREATE_PROC_DELETE_ALBUM =
            "create procedure DELETE_ALBUM(IN id INT) " +
                    "begin " +
                    "DELETE FROM ALBUM WHERE ID = id; " +
                    "end";

    private static final String CREATE_PROC_SHOW_ALL_ALBUMS =
            "create procedure SHOW_ALL_ALBUMS() " +
                    "begin " +
                    "SELECT * FROM ALBUM; " +
                    "end";

    private static final String RUN_PROC_INSERT_ALBUM = "{CALL INSERT_ALBUM(?, ?, ?)}";
    private static final String RUN_PROC_DELETE_ALBUM = "{CALL DELETE_ALBUM(?)}";
    private static final String RUN_PROC_SHOW_ALL_ALBUMS = "{CALL SHOW_ALL_ALBUMS()}";

    private AlbumProcedures() {
    }

    public static void createProcedureInsertAlbum() {
        executeSQL(DROP_PROC_INSERT_ALBUM);
        executeSQL(CREATE_PROC_INSERT_ALBUM);

    }

    public static void createProcedureDeleteAlbum() {
        executeSQL(DROP_PROC_DELETE_ALBUM);
        executeSQL(CREATE_PROC_DELETE_ALBUM);
    }

    public static void createProcedureShowAllAlbums() {
        executeSQL(DROP_PROC_SHOW_ALL_ALBUMS);
        executeSQL(CREATE_PROC_SHOW_ALL_ALBUMS);
    }

    public static void dropProcedureInsertAlbum() {
        executeSQL(DROP_PROC_INSERT_ALBUM);
    }

    public static void dropProcedureDeleteAlbum() {
        executeSQL(DROP_PROC_DELETE_ALBUM);
    }

    public static void dropProcedureShowAllAlbums() {
        executeSQL(DROP_PROC_SHOW_ALL_ALBUMS);
    }

    public static void executeSQL(String executeSQL) {

        try (Connection connection = Datasource.getConnection();
             Statement statement = connection.createStatement()
        ) {
            statement.execute(executeSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Album> runProcedureShowAllAlbums() {
        List<Album> albums = new ArrayList<>();
        try (Connection connection = Datasource.getConnection();
             CallableStatement callableStatement = connection.prepareCall(RUN_PROC_SHOW_ALL_ALBUMS);
             ResultSet resultSet = callableStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                Album album = new Album();
                album.setId(resultSet.getLong(1));
                album.setSingerId(resultSet.getLong(2));
                album.setTitle(resultSet.getString(3));
                album.setReleaseDate(resultSet.getDate(4));
                logger.info(album.toString());
                albums.add(album);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return albums;
    }

    public static void runProcedureInsertAlbum(Album album) {
        try (Connection connection = Datasource.getConnection();
             CallableStatement callableStatement = connection.prepareCall(RUN_PROC_INSERT_ALBUM);
        ) {
            callableStatement.setLong(1, album.getSingerId());
            callableStatement.setString(2, album.getTitle());
            callableStatement.setDate(3, album.getReleaseDate());
            callableStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void runProcedureDeleteAlbum(long albumId) {
        try (Connection connection = Datasource.getConnection();
             CallableStatement callableStatement = connection.prepareCall(RUN_PROC_DELETE_ALBUM);
        ) {
            callableStatement.setLong(1, albumId);
            callableStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
