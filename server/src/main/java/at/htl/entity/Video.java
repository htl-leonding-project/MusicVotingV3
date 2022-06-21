package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

@Entity
@Table(name = "MV_VIDEO")
public class Video extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "V_ID")
    Long id;
    @Column(name = "V_URL")
    String url;

    @ManyToOne
    @JoinColumn(name = "V_P_ID")
    Playlist playlist;
}
