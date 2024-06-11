package at.htl.control;

import at.htl.boundary.SongWebSocket;
import at.htl.entity.Song;
import at.htl.entity.Artist;
import at.htl.youtube.Search;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
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
            song.setTimeAdded(LocalDateTime.now());
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
        return listAll().stream().sorted().collect(Collectors.toList());
    }

    public List<Song> getSongs(String query) {

        List<Song> songs = new ArrayList<>();
        return search.getSearchFromYoutube(query);
    }

    @Transactional
    public Song addRandomSong() {
        List<Artist> artists = artistRepository.listAll();
        Song song;
        boolean insertFails;
        do {
            insertFails = false;
            Random rand = new Random();
            int randomArtist = rand.nextInt(artists.size());
            Artist artist = artists.get(randomArtist);

            List<Song> songs = getSongs(artist.getStrArtist());
            int randomSong = rand.nextInt(songs.size());
            song = songs.get(randomSong);

            try {
                insert(song);
            } catch (Exception e) {
                insertFails = true;
            }
        } while (insertFails);

        return song;
    }
}
