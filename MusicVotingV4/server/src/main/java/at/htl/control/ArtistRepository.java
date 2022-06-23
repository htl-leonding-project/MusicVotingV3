package at.htl.control;

import at.htl.boundary.ArtistService;
import at.htl.entity.Artist;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class ArtistRepository implements PanacheRepository<Artist> {

    @RestClient
    ArtistService artistService;

    @Transactional
    public Artist getArtist(String name) {
        long count = this.getEntityManager()
                .createQuery(
                        "select count(a.id) from Artist a where lower(a.strArtist) = :name",
                        Long.class
                )
                .setParameter("name", name.toLowerCase())
                .getSingleResult();

        if (count == 0) {
            String artistStr = artistService.getArtistInfo(name);

            if(artistStr.equals("{\"artists\":null}")){
                return null;
            }

            JsonObject j = new JsonObject(artistStr);
            JsonArray artist = j.getJsonArray("artists");
            JsonObject o = artist.getJsonObject(0);
            //"112024", "The Weekend"
            Artist newArtist = new Artist(
                    o.getString("idArtist"),
                    o.getString("strArtist"));
            this.persist(newArtist);
            return newArtist;
        }

        return this.getEntityManager()
                .createQuery(
                        "select a from Artist a where lower(a.strArtist) = :name",
                        Artist.class
                )
                .setParameter("name", name.toLowerCase())
                .getSingleResult();
    }
}
