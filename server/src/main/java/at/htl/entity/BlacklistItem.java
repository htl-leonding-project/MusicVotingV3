package at.htl.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "MV_Blacklist")
public class BlacklistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String phrase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public BlacklistItem() {
    }

    public BlacklistItem(String phrase) {
        this.phrase = phrase;
    }
}
