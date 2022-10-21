package at.htl.boundary;

import at.htl.control.ArtistRepository;
import at.htl.control.SongRepository;
import at.htl.entity.Artist;
import at.htl.entity.Song;
import org.apache.http.ContentTooLongException;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;

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
        try {
            songRepository.insert(s);
        } catch (ContentTooLongException e) {
            return Response.serverError().build();
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
}
