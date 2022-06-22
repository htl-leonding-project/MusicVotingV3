package at.htl.boundary;

import at.htl.control.ArtistRepository;
import at.htl.control.SongRepository;
import at.htl.entity.Artist;
import at.htl.entity.Song;

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

    @Inject
    ArtistRepository artistRepository;

    @GET
    public List<Song> getPlaylist(){
        return songRepository.getPlaylist();
    }

    @GET
    @Transactional
    @Path("/addSong/{artistName}/{songTitle}")
    //Es wird angenommen das der genaue Titel des Liedes übergeben wird
    public Response addSong(@PathParam("artistName") String artistName,
                            @PathParam("songTitle") String songTitle){
        Song newSong = songRepository.getSongsWithSearch(
                artistRepository.getArtist(artistName),
                songTitle
        ).get(0);

        if (newSong == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        songRepository.persist(newSong);
        return Response.ok().build();
    }

    @GET
    @Path("search/{artistName}/{songTitle}")
    public List<Song> getSongsWithSearch(
            @PathParam("artistName") String artistName,
            @PathParam("songTitle") String songTitle){

        return songRepository.
                getSongsWithSearch(
                        artistRepository.getArtist(artistName),
                        songTitle);
    }

    @GET
    @Path("search/{artistName}")
    public List<Song> getSongsWithSearch(
            @PathParam("artistName") String artistName){

        return songRepository.getSongs(artistRepository.getArtist(artistName));
    }
}
