package at.htl.control;

import at.htl.boundary.SongWebSocket;
import at.htl.entity.Song;
import at.htl.entity.Artist;
import at.htl.youtube.Search;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@ApplicationScoped
public class SongRepository implements PanacheRepository<Song> {
    @Inject
    Search search;

    @Inject
    SongWebSocket songWebSocket;

    @Inject
    ArtistRepository artistRepository; // Assuming you have an ArtistRepository

    @Transactional
    public void insert(Song song) throws Exception {
        Song existingSong = find("songName", song.getSongName()).firstResult();
        if (existingSong != null) {
            existingSong.setVoteCount(existingSong.getVoteCount() + 1);
        } else {
            persist(song);
            songWebSocket.notifyAllSongsChanged(); // Notify WebSocket
        }
    }

    @Transactional
    public boolean removeById(Long id) {
        Song song = findById(id);
        if (song != null) {
            delete(song);
            songWebSocket.notifyAllSongsChanged(); // Notify WebSocket
            if (getPlaylist().isEmpty()) {
                addRandomSong(); // Add random song if no songs are left
            }
            return true;
        }
        return false;
    }

    public List<Song> getPlaylist() {
        if (listAll().isEmpty()) {
            addRandomSong(); // Add random song if no songs are left
        }

        return listAll().stream()
                .sorted((s1, s2) -> s2.getVoteCount() - s1.getVoteCount())
                .collect(Collectors.toList());
    }

    public List<Song> getSongs(String query) {

        List<Song> songs = new ArrayList<>();
        return search.getSearchFromYoutube(query);
    }

    @Transactional
    public void addRandomSong() {
        // Get artists
        List<Artist> artists = artistRepository.listAll();

        // Get random artist
        Random random = new Random();
        Artist randomArtist = artists.get(random.nextInt(artists.size()));

        // Get random song from artist
        List<Song> songs = search.getSearchFromYoutube(randomArtist.getStrArtist());

        // Add random song
        if (!songs.isEmpty()) {
            Song randomSong = songs.get(random.nextInt(songs.size()));
            randomSong.setVoteCount(1);
            persist(randomSong);
        }

        songWebSocket.notifyAllSongsChanged(); // Notify WebSocket
    }
}
