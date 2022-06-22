package at.htl;


import at.htl.boundary.ArtistService;
import at.htl.boundary.TrackService;
import at.htl.control.ArtistRepository;
import at.htl.control.SongRepository;
import at.htl.entity.Artist;
import at.htl.entity.Song;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DefaultValue;
import java.util.ArrayList;
import java.util.List;

public class InitBean {
    @Inject
    SongRepository songRepository;

    @Inject
    ArtistRepository artistRepository;

    @Transactional
    public void init(@Observes StartupEvent startupEvent) {

        //Artist artist = artistRepository.getArtist("the weekend");
        //artistRepository.persist(artist);

        //List<Song> songsBySearch = songRepository.getSongsWithSearch(artist, "Hill");

        //List<Song> songs = songRepository.getSongsWithSearch(artist, null);

        //songRepository.persist(songsBySearch.get(0));
        //songRepository.persist(songs.get(3));

        Log.info(songRepository.getPlaylist());
    }


}
