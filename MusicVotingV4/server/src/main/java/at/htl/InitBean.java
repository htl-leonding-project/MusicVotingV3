package at.htl;


import at.htl.control.ArtistRepository;
import at.htl.control.SongRepository;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

public class InitBean {
    @Inject
    SongRepository songRepository;

    @Inject
    ArtistRepository artistRepository;

    @Transactional
    public void init(@Observes StartupEvent startupEvent) {
        //songRepository.addRandomSong();

        //Artist artist = artistRepository.getArtist("the weekend");
        //artistRepository.persist(artist);

        //List<Song> songsBySearch = songRepository.getSongsWithSearch(artist, "Hill");

        //List<Song> songs = songRepository.getSongsWithSearch(artist, null);

        //songRepository.persist(songsBySearch.get(0));
        //songRepository.persist(songs.get(3));

        Log.info(songRepository.getPlaylist());
    }


}
