package at.htl.control;

import at.htl.boundary.TrackService;
import at.htl.entity.Artist;
import at.htl.entity.Song;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SongRepository implements PanacheRepository<Song> {
    @RestClient
    TrackService trackService;

    @Inject
    ArtistRepository artistRepository;

    public List<Song> getPlaylist() {
        //TODO ist es richtig sortiert
        return this.listAll();
    }

    @Override
    public void persist(Song song) {
        song.setTimeAdded(LocalDateTime.now());
        PanacheRepository.super.persist(song);
    }

    public List<Song> getSongsWithSearch(Artist artist, String trackName) {
        String songStr = trackService.getMusicVideos(artist.getIdArtist());

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
