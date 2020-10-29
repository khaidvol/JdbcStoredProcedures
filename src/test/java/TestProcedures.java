import dao.DatabaseCreator;
import dao.Datasource;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import procedures.AlbumProcedures;
import procedures.ReportProcedures;
import procedures.SingerProcedures;
import procedures.TrackProcedures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestProcedures {

    private static final String DB_NAME = "jspdb";
    //Report procedures
    private static final String PROC_NAME_SHOW_ALL_DATA = "SHOW_ALL_DATA";
    private static final String PROC_NAME_SHOW_COUNTED_DATA = "SHOW_COUNTED_DATA";
    private static final String PROC_NAME_SHOW_COUNTED_DATA_FOR_SINGER = "SHOW_COUNTED_DATA_FOR_SINGER";

    //Singer procedures
    private static final String PROC_NAME_INSERT_SINGER = "INSERT_SINGER";
    private static final String PROC_NAME_DELETE_SINGER = "DELETE_SINGER";
    private static final String PROC_NAME_SHOW_ALL_SINGERS = "SHOW_ALL_SINGERS";

    //Album procedures
    private static final String PROC_NAME_INSERT_ALBUM = "INSERT_ALBUM";
    private static final String PROC_NAME_DELETE_ALBUM = "DELETE_ALBUM";
    private static final String PROC_NAME_SHOW_ALL_ALBUMS = "SHOW_ALL_ALBUMS";

    //Track procedures
    private static final String PROC_NAME_INSERT_TRACK = "INSERT_TRACK";
    private static final String PROC_NAME_DELETE_TRACK = "DELETE_TRACK";
    private static final String PROC_NAME_SHOW_ALL_TRACKS = "SHOW_ALL_TRACKS";


    private static final String SQL_QUERY_SHOW_PROCEDURE = "SHOW PROCEDURE STATUS WHERE DB = ? AND Name = ?";
    private static final String SQL_QUERY_SHOW_ALL_PROCEDURES = "SHOW PROCEDURE STATUS WHERE DB = ?";


    @BeforeClass
    public static void prepareDB() {
        DatabaseCreator.createTables();
        DatabaseCreator.populateData();
    }

    @Test
    public void dropAllProcedures() {
        ReportProcedures.dropProcedureShowAllData();
        ReportProcedures.dropProcedureShowCountedData();
        ReportProcedures.dropProcedureShowCountedDataForSinger();

        SingerProcedures.dropProcedureInsertSinger();
        SingerProcedures.dropProcedureDeleteSinger();
        SingerProcedures.dropProcedureShowAllSingers();

        AlbumProcedures.dropProcedureInsertAlbum();
        AlbumProcedures.dropProcedureDeleteAlbum();
        AlbumProcedures.dropProcedureShowAllAlbums();

        TrackProcedures.dropProcedureInsertTrack();
        TrackProcedures.dropProcedureDeleteTrack();
        TrackProcedures.dropProcedureShowAllTracks();

        Assert.assertEquals(0, showAllProcedures().size());
    }

    // test creation of Report procedures
    @Test
    public void testShowAllDataProcedure() {
        ReportProcedures.createProcedureShowAllData();
        Assert.assertEquals(PROC_NAME_SHOW_ALL_DATA, showProcedure(PROC_NAME_SHOW_ALL_DATA));
    }

    @Test
    public void testShowCounterDataProcedure() {
        ReportProcedures.createProcedureShowCountedData();
        Assert.assertEquals(PROC_NAME_SHOW_COUNTED_DATA, showProcedure(PROC_NAME_SHOW_COUNTED_DATA));
    }

    @Test
    public void testShowCountedDataForSingerProcedure() {
        ReportProcedures.createProcedureShowCountedDataForSinger();
        Assert.assertEquals(PROC_NAME_SHOW_COUNTED_DATA_FOR_SINGER, showProcedure(PROC_NAME_SHOW_COUNTED_DATA_FOR_SINGER));
    }

    // test creation of Singer procedures
    @Test
    public void testInsertSingerProcedure() {
        SingerProcedures.createProcedureInsertSinger();
        Assert.assertEquals(PROC_NAME_INSERT_SINGER, showProcedure(PROC_NAME_INSERT_SINGER));
    }

    @Test
    public void testDeleteSingerProcedure() {
        SingerProcedures.createProcedureDeleteSinger();
        Assert.assertEquals(PROC_NAME_DELETE_SINGER, showProcedure(PROC_NAME_DELETE_SINGER));
    }

    @Test
    public void testShowAllSingersProcedure() {
        SingerProcedures.createProcedureShowAllSingers();
        Assert.assertEquals(PROC_NAME_SHOW_ALL_SINGERS, showProcedure(PROC_NAME_SHOW_ALL_SINGERS));
    }

    // test creation of Album procedures
    @Test
    public void testInsertAlbumProcedure() {
        AlbumProcedures.createProcedureInsertAlbum();
        Assert.assertEquals(PROC_NAME_INSERT_ALBUM, showProcedure(PROC_NAME_INSERT_ALBUM));
    }

    @Test
    public void testDeleteAlbumProcedure() {
        AlbumProcedures.createProcedureDeleteAlbum();
        Assert.assertEquals(PROC_NAME_DELETE_ALBUM, showProcedure(PROC_NAME_DELETE_ALBUM));
    }

    @Test
    public void testShowAllAlbumsProcedure() {
        AlbumProcedures.createProcedureShowAllAlbums();
        Assert.assertEquals(PROC_NAME_SHOW_ALL_ALBUMS, showProcedure(PROC_NAME_SHOW_ALL_ALBUMS));
    }

    // test creation of Track procedures
    @Test
    public void testInsertTrackProcedure() {
        TrackProcedures.createProcedureInsertTrack();
        Assert.assertEquals(PROC_NAME_INSERT_TRACK, showProcedure(PROC_NAME_INSERT_TRACK));
    }

    @Test
    public void testDeleteTrackProcedure() {
        TrackProcedures.createProcedureDeleteTrack();
        Assert.assertEquals(PROC_NAME_DELETE_TRACK, showProcedure(PROC_NAME_DELETE_TRACK));
    }

    @Test
    public void testShowAllTracksProcedure() {
        TrackProcedures.createProcedureShowAllTracks();
        Assert.assertEquals(PROC_NAME_SHOW_ALL_TRACKS, showProcedure(PROC_NAME_SHOW_ALL_TRACKS));
    }

    //find procedure in the DB
    public static String showProcedure(String procedureName) {
        String procedure = null;
        try (Connection connection = Datasource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_QUERY_SHOW_PROCEDURE);
        ) {
            statement.setString(1, DB_NAME);
            statement.setString(2, procedureName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                procedure = resultSet.getString(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return procedure;
    }

    //find all procedure in the DB
    public static List<String> showAllProcedures() {
        List<String> listOfProcedures = new ArrayList<>();
        try (Connection connection = Datasource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_QUERY_SHOW_ALL_PROCEDURES);
        ) {
            statement.setString(1, DB_NAME);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                listOfProcedures.add(resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfProcedures;
    }

}
