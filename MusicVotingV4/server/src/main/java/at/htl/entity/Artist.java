package at.htl.entity;

import javax.persistence.*;

@Entity
@Table(name = "MV_Artist")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idArtist;
    private String strArtist;

    public String getIdArtist() {
        return idArtist;
    }

    public void setIdArtist(String idArtist) {
        this.idArtist = idArtist;
    }

    public String getStrArtist() {
        return strArtist;
    }

    public void setStrArtist(String strArtist) {
        this.strArtist = strArtist;
    }

    public Artist(String idArtist, String strArtist) {
        this.idArtist = idArtist;
        this.strArtist = strArtist;
    }

    public Artist() {
    }

    @Override
    public String toString() {
        return "Artist{" +
                "idArtist=" + idArtist +
                ", strArtist='" + strArtist + '\'' +
                '}';
    }
}
