package at.htl.youtube;

import at.htl.control.SongRepository;
import at.htl.entity.Song;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import io.quarkus.logging.Log;
import io.vertx.core.json.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

@ApplicationScoped
public class Search {

    //TODO in config file geben
    private static final String PROPERTIES_FILENAME = "youtube.properties";

    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

    private static YouTube youtube;

    public List<Song> getSearchFromYoutube(String queryTerm) {
        queryTerm += " Lyrics";
        List<Song> songs = new ArrayList<>();
        String baseUrl = "https://www.youtube.com/results?search_query=";

        Document doc = null;
        try {
            doc = Jsoup.connect(baseUrl+queryTerm).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Element body = doc.body();

        String javascript = body.child(15).html();
        JsonObject json = new JsonObject(javascript.substring(19, javascript.length()-1));

        var videoArray = json.getJsonObject("contents").getJsonObject("twoColumnSearchResultsRenderer")
                .getJsonObject("primaryContents").getJsonObject("sectionListRenderer")
                .getJsonArray("contents").getJsonObject(0).getJsonObject("itemSectionRenderer").getJsonArray("contents");

        for (int i = 0; i < videoArray.size()-1; i++) {
            var video = videoArray.getJsonObject(i).getJsonObject("videoRenderer");
            if(video != null){
                String title = video.getJsonObject("title").getJsonArray("runs").getJsonObject(0).getString("text");
                String thumbnail = video.getJsonObject("thumbnail").getJsonArray("thumbnails").getJsonObject(0).getString("url");
                String videoUrl = "https://www.youtube.com/watch?v="+ video.getString("videoId");
                String durationString = video.getJsonObject("lengthText").getString("simpleText");
                int duration = convertString(durationString);
                Song newSong = new Song(title,videoUrl, thumbnail, "", null);
                newSong.setDuration(duration);
                songs.add(newSong);
            }
        }
        return songs;
    }

    private int convertString(String duration) {
        String[] splitted = duration.split(":");
        int result = 0;
        //Stunden
        if(splitted.length == 3){
            result+= 60*60*Integer.parseInt(splitted[0])+ 60*Integer.parseInt(splitted[1]) + Integer.parseInt(splitted[2]);
        }else if(splitted.length == 2){
            result+= 60*Integer.parseInt(splitted[0]) + Integer.parseInt(splitted[1]);
        }
        return result*1000;
    }

    /**
     * Initialize a YouTube object to search for videos on YouTube. Then
     * display the name and thumbnail image of each video in the result set.
     *
     *
     */
    public List<Song> getSearchFromYoutube2(String queryTerm) {
    	queryTerm += " Lyrics";

        // API Key auslesen
        Properties properties = new Properties();
        try {
            InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
            properties.load(in);

        } catch (IOException e) {
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
                    + " : " + e.getMessage());
            System.exit(1);
        }

        try {
            // wird dazu verwendet die Request zu machen
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("MusicVoting").build();//youtube-cmdline-search-sample
            
            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");

            String apiKey = properties.getProperty("youtube.apikey");
            search.setKey(apiKey);
            search.setQ(queryTerm);

            // Nur Videos zurückgeben
            search.setType("video");
            //search.setPart("contentDetails");

            // To increase efficiency, only retrieve the fields that the
            // application uses.
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            // Call the API and print results.
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();

            if (searchResultList != null) {
                return getSongsForResult(searchResultList.iterator());
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    private List<Song> getSongsForResult(Iterator<SearchResult> iteratorSearchResults) {
        List<Song> songs = new ArrayList<>();
        if (!iteratorSearchResults.hasNext()) {
            System.out.println("There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()) {
            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
/*

                System.out.println(" Video Id:" + rId.getVideoId());
                System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
                System.out.println(" Thumbnail: " + thumbnail.getUrl());
                System.out.println("\n-------------------------------------------------------------\n");
*/

                Song s = new Song();
                s.setSongName(singleVideo.getSnippet().getTitle());
                s.setThumbnail(thumbnail.getUrl());
                s.setVideoUrl("https://www.youtube.com/watch?v="+rId.getVideoId());
                songs.add(s);
            }
        }
        return songs;
    }
}
