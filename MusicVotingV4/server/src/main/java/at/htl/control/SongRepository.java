package at.htl.control;

import at.htl.boundary.TrackService;
import at.htl.boundary.VideoService;
import at.htl.entity.Artist;
import at.htl.entity.Song;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Sort;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class SongRepository implements PanacheRepository<Song> {
    @RestClient
    VideoService videoService;

    @RestClient
    TrackService trackService;

    @Inject
    ArtistRepository artistRepository;

    public List<Song> getPlaylist() {
        List<Song> songs =  this.listAll(Sort.by("timeAdded").ascending());

        if(songs.size() == 0){

        }
        return songs;
    }

    public void addRandomSong(){
        List<Artist> artists = artistRepository.listAll();

        Random rand = new Random();
        int random_artist = rand.nextInt(artists.size());

        Artist a = artists.get(random_artist);

        List<Song> s = getSongs(a);

        int random_song = rand.nextInt(artists.size());

    }


    @Override
    public void persist(Song song) {
       String trackStr =  trackService.getTrack(song.getSongId());
        JsonObject songJson = new JsonObject(trackStr);
        JsonArray songsArray = songJson.getJsonArray("track");

        JsonObject o = songsArray.getJsonObject(0);
        String durationStr = o.getString("intDuration");

        Log.info(durationStr);

        song.setDuration(Integer.parseInt(durationStr));
        song.setTimeAdded(LocalDateTime.now());
        PanacheRepository.super.persist(song);
    }

    public List<Song> getSongsWithSearch(Artist artist, String trackName) {
        String songStr = videoService.getMusicVideos(artist.getIdArtist());

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
        }

        return songs;
    }

    public List<Song> getSongs(Artist artist) {
        return getSongsWithSearch(artist, null);
    }

}
