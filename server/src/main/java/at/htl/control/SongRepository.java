package at.htl.control;

import at.htl.boundary.SongWebSocket;
import at.htl.entity.Song;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SongRepository implements PanacheRepository<Song> {

    @Inject
    SongWebSocket songWebSocket;

    @Transactional
    public void insert(Song song) throws Exception {
        Song existingSong = find("songName", song.getSongName()).firstResult();
        if (existingSong != null) {
            existingSong.setVoteCount(existingSong.getVoteCount() + 1);
        } else {
            song.setTimeAdded(LocalDateTime.now());
            persist(song);
            songWebSocket.songAdded(song); // Notify WebSocket
        }
    }

    @Transactional
    public boolean removeById(Long id) { // Renamed method to avoid conflict
        Song song = findById(id);
        if (song != null) {
            delete(song);
            songWebSocket.songRemoved(song); // Notify WebSocket
            return true;
        }
        return false;
    }

    public List<Song> getPlaylist() {
        return listAll().stream().sorted().collect(Collectors.toList());
    }

    public List<Song> getSongs(String query) {
        return list("songName like ?1", "%" + query + "%");
    }
}
