package at.htl.boundary;

import at.htl.control.SongRepository;
import at.htl.entity.Song;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Objects;

@Path("songs")
public class SongResource {

    @Inject
    SongRepository songRepository;

    @Inject
    SongWebSocket songWebSocket; // Inject WebSocket for notifications

    @GET
    public List<Song> getPlaylist() {
        return songRepository.getPlaylist();
    }

    @GET
    @Path("getNextSong")
    @Transactional
    public Song getNextSong() {
        List<Song> songs = songRepository.getPlaylist();

        if (songs.isEmpty()) {
            return null;
        }

        songRepository.removeById(songs.get(0).getId());

        return songRepository.getPlaylist().stream().findFirst().orElse(null);
    }

    @POST
    @Transactional
    public Response addSong(@RequestBody Song newSong) {
        Song song = new Song();
        song.setSongName(newSong.getSongName());
        song.setThumbnail(newSong.getThumbnail());
        song.setSongId(newSong.getSongId());
        song.setVideoUrl(newSong.getVideoUrl());
        song.setDuration(newSong.getDuration());
        try {
            songRepository.insert(song);
            songWebSocket.notifyAllSongsChanged(); // Notify WebSocket
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
        return Response.ok().build();
    }

    @GET
    @Path("search/{query}")
    public Response getSongsWithSearch(@PathParam("query") String query) {
        return Response.ok(songRepository.getSongs(query)).build();
    }

    @GET
    @Path("checkPassword/{password}")
    public Response checkPassword(@PathParam("password") String password) {
        String adminPass = ConfigProvider.getConfig().getValue("admin.password", String.class);
        if (Objects.equals(adminPass, password)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @DELETE
    @Path("{id}/{password}")
    @Transactional
    public Response deleteSong(@PathParam("id") Long id, @PathParam("password") String password) {
        String adminPass = ConfigProvider.getConfig().getValue("admin.password", String.class);
        if (Objects.equals(adminPass, password)) {
            boolean deleted = songRepository.removeById(id);
            if (deleted) {
                songWebSocket.notifyAllSongsChanged(); // Notify WebSocket
                return Response.ok().build();
            }
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
