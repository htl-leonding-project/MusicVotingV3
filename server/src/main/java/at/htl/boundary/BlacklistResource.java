package at.htl.boundary;

import at.htl.control.BlacklistRepository;
import at.htl.control.SongRepository;
import at.htl.entity.BlacklistItem;
import at.htl.entity.Song;
import org.apache.http.ContentTooLongException;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

@Path("blacklist")
public class BlacklistResource {

    @Inject
    BlacklistRepository blacklistRepository;

    @GET
    @Path("/getBlacklist")
    public List<BlacklistItem> getBlacklist(){
        return blacklistRepository.listAll();
    }

    @POST
    @Transactional
    @Path("/putOnBlackList/{phrase}")
    public Response addBlacklistItem(@PathParam("phrase") String phrase){
        blacklistRepository.persist(new BlacklistItem(phrase));
        return Response.ok().build();
    }

    @DELETE
    @Transactional
    @Path("/deleteFromBlacklist/{id}")
    public Response getBlacklist(@PathParam("id") Long id){
        blacklistRepository.deleteById(id);
        return Response.ok().build();
    }
}
