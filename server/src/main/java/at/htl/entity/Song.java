package at.htl.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "MV_Song")
public class Song implements Comparable<Song> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String songName;
    private String videoUrl;
    private String thumbnail;

    private String songId;

    private int voteCount = 1;

    private int duration;

    public Song(String songName,
                String videoUrl,
                String thumbnail,
                String songId,
                Artist artist) {
        this.songName = songName;
        this.videoUrl = videoUrl;
        this.thumbnail = thumbnail;
        this.songId = songId;
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

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
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

    @Override
    public int compareTo(Song o) {
        if (this.getVoteCount() == o.getVoteCount()) {
            return this.id.compareTo(o.getId());
        }

        return this.getVoteCount() - o.getVoteCount();
    }

    public String toJson() {
        return "{" +
                "\"id\":" + id +
                ",\"songName\":\"" + songName + '\"' +
                ",\"videoUrl\":\"" + videoUrl + '\"' +
                ",\"thumbnail\":\"" + thumbnail + '\"' +
                ",\"songId\":\"" + songId + '\"' +
                ",\"voteCount\":" + voteCount +
                ",\"duration\":" + duration +
                "}";
    }
}
