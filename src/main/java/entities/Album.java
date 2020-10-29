package entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Album {
    private Long id;
    private Long singerId;
    private String title;
    private Date releaseDate;
    private List<Track> tracks;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setSingerId(Long singerId) {
        this.singerId = singerId;
    }

    public Long getSingerId() {
        return this.singerId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getReleaseDate() {
        return this.releaseDate;
    }

    public boolean addTrack(Track track) {
        if (tracks == null) {
            tracks = new ArrayList<>();
            tracks.add(track);
            return true;
        } else {
            if (tracks.contains(track)) {
                return false;
            }
        }
        tracks.add(track);
        return true;
    }

    @Override
    public String toString() {
        return "Album - Id: " + id + ", Singer id: " + singerId
                + ", Title: " + title + ", Release Date: " + releaseDate;
    }

}
