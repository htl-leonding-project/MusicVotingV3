package at.htl.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("")
@RegisterRestClient
public interface ArtistService {

    @GET
    String getArtistInfo(@QueryParam("s") String artistName);
}
