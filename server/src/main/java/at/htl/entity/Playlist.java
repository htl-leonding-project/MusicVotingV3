package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MV_PLAYLIST")
public class Playlist  extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "P_ID")
    Long id;
    @Column(name = "P_TIMESTAMP")
    LocalDateTime timeStamp;

    public Playlist() {
    }

    public Playlist(Long id, LocalDateTime timeStamp) {
        this.id = id;
        this.timeStamp = timeStamp;
    }
}
