package at.htl.control;

import at.htl.boundary.ArtistService;
import at.htl.entity.Artist;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ArtistRepository implements PanacheRepository<Artist> {

    @RestClient
    ArtistService artistService;

    public Artist getArtist(String name) {
        //speichert zu oft hinein
        if (this.count("strArtist", name) == 0) {
            String artistStr = artistService.getArtistInfo(name);

            JsonObject j = new JsonObject(artistStr);


            JsonObject artist = j.getJsonArray("artists").getJsonObject(0);
            //"112024", "The Weekend"
            if(artist == null){
                return null;
            }
            return new Artist(artist.getString("idArtist"), artist.getString("strArtist"));
        }
        return this.find("strArtist", name).firstResult();
    }
}
