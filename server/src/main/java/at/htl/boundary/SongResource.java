package at.htl.boundary;

import at.htl.control.ArtistRepository;
import at.htl.control.SongRepository;
import at.htl.entity.Artist;
import at.htl.entity.Song;
import org.apache.http.ContentTooLongException;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Path("songs")
public class SongResource {

    @Inject
    SongRepository songRepository;

    @GET
    public List<Song> getPlaylist(){
        return songRepository.getPlaylist();
    }

    @GET
    @Path("getNextSong")
    @Transactional
    public Song getNextSong(){
        List<Song> songs = songRepository.getPlaylist();

        if(songs.size() == 0){
            return null;
        }

        songRepository.deleteById(songs.get(0).getId());
        return songs.get(0);
    }


    @POST
    @Transactional
    @Path("/addSong")
    public Response addSong(@RequestBody Song newSong){
        System.out.println(newSong);
        Song s = new Song();
        s.setSongName(newSong.getSongName());
        s.setThumbnail(newSong.getThumbnail());
        s.setSongId(newSong.getSongId());
        s.setVideoUrl(newSong.getVideoUrl());
        s.setDuration(newSong.getDuration());
        try {
            songRepository.insert(s);
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .entity(e.getMessage())
                    .build();
        }
        return Response.ok().build();
    }

    @GET
    @Path("search/{query}")
    public Response getSongsWithSearch(
            @PathParam("query") String query){

//        Artist a = artistRepository.getArtist(artistName);
//        if(a == null){
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
        return Response.ok(
                songRepository.getSongs(query)).build();
    }

    @GET
    @Path("checkPassword/{password}")
    public Response checkPassword(@PathParam("password") String password){
        String adminPass = ConfigProvider.getConfig().getValue("admin.password", String.class);

        if(Objects.equals(adminPass, password)) {
            System.out.println("Pass: " + adminPass);
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @DELETE
    @Path("deleteSong/{id}")
    @Transactional
    public Response deleteSong(@PathParam("id") Long id){
        songRepository.deleteById(id);
        return Response.ok().build();
    }
}
