package at.htl.control;

import at.htl.entity.Artist;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@ApplicationScoped
public class ArtistRepository implements PanacheRepository<Artist> {

}
