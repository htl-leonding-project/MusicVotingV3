package at.htl.control;

import at.htl.entity.Artist;
import at.htl.entity.BlacklistItem;
import at.htl.entity.Song;
import at.htl.youtube.Search;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import org.apache.http.ContentTooLongException;
import org.hibernate.mapping.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class BlacklistRepository implements PanacheRepository<BlacklistItem> {

    public boolean checkSong(Song song){
        //Big Time Rush - Nothing Even Matters (Lyrics)

        List<BlacklistItem> items = this.listAll();

        for (BlacklistItem item :
                items) {

            NormalizedLevenshtein l = new NormalizedLevenshtein();

            System.out.println("String Ähnlichkeit: "+l.distance(song.getSongName(), item.getPhrase())+
                    " von "+song.getSongName()+" und "+item.getPhrase());

             if(song.getSongName().contains(item.getPhrase())){
                 return false;
             }
        }

        return false;
    }

}
