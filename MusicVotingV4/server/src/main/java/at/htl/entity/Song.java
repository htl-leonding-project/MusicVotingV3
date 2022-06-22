package at.htl.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MV_Song")
public class Song {
    //TODO artist verbinden

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String songName;
    private String videoUrl;
    private String thumbnail;

    private String songId;

    private LocalDateTime timeAdded;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Artist artist;

    public Song(String songName,
                String videoUrl,
                String thumbnail,
                String songId,
                Artist artist) {
        this.songName = songName;
        this.videoUrl = videoUrl;
        this.thumbnail = thumbnail;
        this.songId = songId;
        this.artist = artist;
    }

    public Song() {
    }

    @Override
    public String toString() {
        return "Song{" +
                "songName='" + songName + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }


    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public LocalDateTime getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(LocalDateTime timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
