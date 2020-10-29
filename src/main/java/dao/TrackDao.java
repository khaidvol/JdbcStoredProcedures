package dao;

import entities.Track;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrackDao {

    private static final Logger logger = Logger.getRootLogger();

    private static final String FIND_ALL_TRACKS = "SELECT * FROM TRACK";
    private static final String INSERT_TRACK = "INSERT INTO TRACK (TITLE, ALBUM_ID, TRACK_ID, DURATION) VALUES (?, ?, ?, ?)";
    private static final String DELETE_TRACK = "DELETE FROM TRACK WHERE TITLE = ?";

    private TrackDao() {
    }

    public static List<Track> findAllTracks() {
        List<Track> tracks = new ArrayList<>();
        try (Connection connection = Datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_TRACKS)
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Track track = new Track();
                track.setTitle(resultSet.getString(1));
                track.setAlbumId(resultSet.getLong(2));
                track.setTrackId(resultSet.getLong(3));
                track.setDuration(resultSet.getInt(4));
                tracks.add(track);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return tracks;
    }

    public static void insertAlbum(Track track) {
        try (Connection connection = Datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TRACK)
        ) {
            preparedStatement.setString(1, track.getTitle());
            preparedStatement.setLong(2, track.getAlbumId());
            preparedStatement.setLong(2, track.getTrackId());
            preparedStatement.setInt(3, track.getDuration());
            if (preparedStatement.execute()) {
                logger.info(track.getTitle() + " - track added to the db");
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static void deleteTrack(String trackTitle) {
        try (Connection connection = Datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TRACK)
        ) {
            preparedStatement.setString(1, trackTitle);
            if (preparedStatement.execute()) {
                logger.info(trackTitle + " - track deleted from the db");
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
