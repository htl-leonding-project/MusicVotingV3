package at.htl.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "MV_Artist")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String strArtist;

    private String genre;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getStrArtist() {
        return strArtist;
    }

    public void setStrArtist(String strArtist) {
        this.strArtist = strArtist;
    }

    public Artist(String strArtist, String genre) {
        this.strArtist = strArtist;
        this.genre = genre;
    }

    public Artist() {
    }

    @Override
    public String toString() {
        return "Artist{" +
                ", strArtist='" + strArtist + '\'' +
                '}';
    }
}
