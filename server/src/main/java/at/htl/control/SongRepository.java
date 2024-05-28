package at.htl.control;

import at.htl.entity.Artist;
import at.htl.entity.Song;
import at.htl.youtube.Search;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import org.apache.http.ContentTooLongException;
import org.eclipse.microprofile.config.ConfigProvider;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@ApplicationScoped
public class SongRepository implements PanacheRepository<Song> {

    @Inject
    ArtistRepository artistRepository;

    @Inject
    BlacklistRepository blacklistRepository;

    @Inject
    Search search;

    public List<Song> getPlaylist() {
        List<Song> songs = this.listAll().stream().sorted().collect(Collectors.toList());

        if (songs.isEmpty()) {
            addRandomSong();
            return this.listAll().stream().sorted().collect(Collectors.toList());
        }

        return songs;
    }

    public void insert(Song song) throws Exception {
        Song s = find("songName", song.getSongName()).firstResult();
        if (s != null) {
            int count = s.getVoteCount();
            count++;
            s.setVoteCount(count);
            System.out.println("Vote erhöht");
        } else {
            song.setTimeAdded(LocalDateTime.now());
            int minDuration = ConfigProvider.getConfig().getValue("song.minDuration", Integer.class);
            int maxDuration = ConfigProvider.getConfig().getValue("song.maxDuration", Integer.class);

            if (song.getDuration() > 60 * maxDuration * 1000) {
                throw new ContentTooLongException("Video zu lang (max. " + maxDuration + " Minuten)");
            } else if (song.getDuration() < 60 * minDuration * 1000) {
                throw new Exception("Lied ist zu kurz (min. " + minDuration + " Minuten)");
            } else if (song.getDuration() == 0) {
                throw new Exception("Lied hat keine Länge");
            }

            if (blacklistRepository.checkSong(song)) {
                throw new Exception("Lied darf aufgrund der Blacklist nicht hinzugefügt werden");
            }
            PanacheRepository.super.persist(song);
        }
    }


    @Transactional
    public void addRandomSong() {
        List<Artist> artists = artistRepository.listAll();
        Song song = null;
        boolean insertFailes;
        do {
            insertFailes = false;
            Random rand = new Random();
            int random_artist = rand.nextInt(artists.size());

            Artist a = artists.get(random_artist);

            List<Song> s = getSongs(a.getStrArtist());

            int random_song = rand.nextInt(s.size());

            song = s.get(random_song);

            try {
                insert(song);
            } catch (Exception e) {
                insertFailes = true;
            }
        } while (insertFailes);
    }

    public List<Song> getSongsWithSearch(String artist, String trackName) {

        List<Song> songs = new ArrayList<>();
        return search.getSearchFromYoutube(artist);
        /*String songStr = videoService.getMusicVideos(artist.getIdArtist());

        JsonObject songJson = new JsonObject(songStr);

        JsonArray songsArray = songJson.getJsonArray("mvids");

        List<Song> songs = new ArrayList<>();

        if (songsArray != null) {
            for (int i = 0; i < songsArray.size(); i++) {
                JsonObject video = songsArray.getJsonObject(i);

                if (trackName == null ||
                        video.getString("strTrack").toLowerCase()
                                .contains(trackName.toLowerCase())) {
                    songs.add(new Song(
                            video.getString("strTrack"),
                            video.getString("strMusicVid"),
                            video.getString("strTrackThumb"),
                            video.getString("idTrack"),
                            artist));
                }
            }
        }*/

    }

    public List<Song> getSongs(String artist) {
        return getSongsWithSearch(artist, null);
    }

    public int getDurationOfSong(String videoUrl) {
        try {
            ProcessBuilder pb = new ProcessBuilder(System.getProperty("user.dir") + "/getDuration.sh", videoUrl);
            Process p = pb.start();
            p.waitFor();
            StringBuilder output = new StringBuilder();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            String[] st = output.toString().split(":");
            int min = Integer.parseInt(st[0].split("\n")[0]);
            int sec = 0;
            if (st.length > 1)
                sec = Integer.parseInt(st[1].split("\n")[0]);
            System.out.println(min + "  " + sec);
            return (min * 60 + sec) * 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
