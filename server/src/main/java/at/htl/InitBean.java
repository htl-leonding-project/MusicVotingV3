package at.htl;


import at.htl.control.ArtistRepository;
import at.htl.control.SongRepository;
import at.htl.entity.Artist;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InitBean {
    @Inject
    SongRepository songRepository;

    @Inject
    ArtistRepository artistRepository;

    public void main(@Observes StartupEvent startupEvent) {//@Observes StartupEvent startupEvent
        readCsv();
        //artistRepository.saveArtistsToCsv();
        //songRepository.addRandomSong();

        //Artist artist = artistRepository.getArtist("the weekend");
        //artistRepository.persist(artist);

        //List<Song> songsBySearch = songRepository.getSongsWithSearch(artist, "Hill");

        //List<Song> songs = songRepository.getSongsWithSearch(artist, null);

        //songRepository.persist(songsBySearch.get(0));
        //songRepository.persist(songs.get(3));

        Log.info(songRepository.getPlaylist());
    }

    @Transactional
    public void readCsv() {
        artistRepository.deleteAll();
        String line = "";
        String splitBy = ",";
        try {
            BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/artists.csv"));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] artist = line.split(splitBy);
                Artist a = new Artist();

                a.setStrArtist(artist[0]);
                if (artist.length > 1)
                    a.setGenre(artist[1]);
                artistRepository.persist(a);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
