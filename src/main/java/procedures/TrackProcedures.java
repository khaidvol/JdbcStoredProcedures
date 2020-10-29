package procedures;

import dao.Datasource;
import entities.Track;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrackProcedures {

    private static final Logger logger = Logger.getRootLogger();

    private static final String DROP_PROC_INSERT_TRACK = "DROP PROCEDURE IF EXISTS INSERT_TRACK";
    private static final String DROP_PROC_DELETE_TRACK = "DROP PROCEDURE IF EXISTS DELETE_TRACK";
    private static final String DROP_PROC_SHOW_ALL_TRACKS = "DROP PROCEDURE IF EXISTS SHOW_ALL_TRACKS";


    private static final String CREATE_PROC_INSERT_TRACK  =
            "create procedure INSERT_TRACK(IN title VARCHAR(255), IN albumId INT, IN trackId INT, IN duration INT) " +
                    "begin " +
                    "INSERT INTO TRACK (TITLE, ALBUM_ID, TRACK_ID, DURATION) VALUES (title, albumId, trackId, duration); " +
                    "end";

    private static final String CREATE_PROC_DELETE_TRACK  =
            "create procedure DELETE_TRACK(IN id INT) " +
                    "begin " +
                    "DELETE FROM TRACK WHERE ID = id; " +
                    "end";

    private static final String CREATE_PROC_SHOW_ALL_TRACKS  =
            "create procedure SHOW_ALL_TRACKS() " +
                    "begin " +
                    "SELECT * FROM TRACK; " +
                    "end";

    private static final String RUN_PROC_INSERT_TRACK = "{CALL INSERT_TRACK(?, ?, ?, ?)}";
    private static final String RUN_PROC_DELETE_TRACK = "{CALL DELETE_TRACK(?)}";
    private static final String RUN_PROC_SHOW_ALL_TRACKS = "{CALL SHOW_ALL_TRACKS()}";

    private TrackProcedures() {
    }

    public static void createProcedureInsertTrack() {
        executeSQL(DROP_PROC_INSERT_TRACK);
        executeSQL(CREATE_PROC_INSERT_TRACK);

    }

    public static void createProcedureDeleteTrack(){
        executeSQL(DROP_PROC_DELETE_TRACK);
        executeSQL(CREATE_PROC_DELETE_TRACK);
    }

    public static void createProcedureShowAllTracks(){
        executeSQL(DROP_PROC_SHOW_ALL_TRACKS);
        executeSQL(CREATE_PROC_SHOW_ALL_TRACKS);
    }

    public static void dropProcedureInsertTrack(){
        executeSQL(DROP_PROC_INSERT_TRACK);
    }

    public static void dropProcedureDeleteTrack(){
        executeSQL(DROP_PROC_DELETE_TRACK);
    }

    public static void dropProcedureShowAllTracks(){
        executeSQL(DROP_PROC_SHOW_ALL_TRACKS);
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

    public static List<Track> runProcedureShowAllTracks() {
        List<Track> tracks = new ArrayList<>();
        try(Connection connection = Datasource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(RUN_PROC_SHOW_ALL_TRACKS);
            ResultSet resultSet = callableStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                Track track = new Track();
                track.setTitle(resultSet.getString(1));
                track.setAlbumId(resultSet.getLong(2));
                track.setTrackId(resultSet.getLong(3));
                track.setDuration(resultSet.getInt(4));
                logger.info(track.toString());
                tracks.add(track);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return tracks;
    }

    public static void runProcedureInsertTrack(Track track) {
        try(Connection connection = Datasource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(RUN_PROC_INSERT_TRACK);
        ) {
            callableStatement.setString(1, track.getTitle());
            callableStatement.setLong(2, track.getAlbumId());
            callableStatement.setLong(3, track.getTrackId());
            callableStatement.setInt(3, track.getDuration());
            callableStatement.execute();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void runProcedureDeleteTrack(String title) {
        try(Connection connection = Datasource.getConnection();
            CallableStatement callableStatement = connection.prepareCall(RUN_PROC_DELETE_TRACK);
        ) {
            callableStatement.setString(1, title);
            callableStatement.execute();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

}

