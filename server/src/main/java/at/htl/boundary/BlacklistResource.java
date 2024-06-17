package at.htl.boundary;

import at.htl.control.BlacklistRepository;
import at.htl.entity.BlacklistItem;
import org.eclipse.microprofile.config.ConfigProvider;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Objects;

@Path("blacklist")
public class BlacklistResource {

    @Inject
    BlacklistRepository blacklistRepository;

    @GET
    public List<BlacklistItem> getBlacklist() {
        return blacklistRepository.listAll();
    }

    @POST
    @Transactional
    @Path("{phrase}/{password}")
    public Response addBlacklistItem(@PathParam("phrase") String phrase, @PathParam("password") String password) {
        String adminPass = ConfigProvider.getConfig().getValue("admin.password", String.class);
        if (Objects.equals(adminPass, password)) {
            blacklistRepository.persist(new BlacklistItem(phrase));
            return Response.ok().build();
        }

        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @DELETE
    @Transactional
    @Path("{id}/{password}")
    public Response getBlacklist(@PathParam("id") Long id, @PathParam("password") String password) {
        String adminPass = ConfigProvider.getConfig().getValue("admin.password", String.class);
        if (Objects.equals(adminPass, password)) {
            blacklistRepository.deleteById(id);
            return Response.ok().build();
        }

        return Response.status(Response.Status.FORBIDDEN).build();

    }
}
