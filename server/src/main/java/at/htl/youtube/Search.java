package at.htl.youtube;

import at.htl.entity.Song;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class Search {
    public List<Song> getSearchFromYoutube(String queryTerm) {
        queryTerm += " Lyrics";
        queryTerm = queryTerm.replace(' ', '+');
        List<Song> songs = new ArrayList<>();
        String baseUrl = "https://www.youtube.com/results?search_query=";

        Document doc;
        try {
            doc = Jsoup.connect(baseUrl + queryTerm).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Element body = doc.body();

        final int INDEX_OF_HTML_RESPONSE = 15;
        String javascript = body.child(INDEX_OF_HTML_RESPONSE).html();
        int endOfSubstringIndex = Math.max(javascript.length() - 1, 0);
        JsonObject json = new JsonObject(javascript.substring(19, endOfSubstringIndex));

        JsonArray videoArray = json.getJsonObject("contents").getJsonObject("twoColumnSearchResultsRenderer")
                .getJsonObject("primaryContents").getJsonObject("sectionListRenderer")
                .getJsonArray("contents").getJsonObject(0).getJsonObject("itemSectionRenderer").getJsonArray("contents");

        for (int i = 0; i < videoArray.size() - 1; i++) {
            JsonObject video = videoArray.getJsonObject(i).getJsonObject("videoRenderer");
            if (video != null) {
                String title = video.getJsonObject("title").getJsonArray("runs").getJsonObject(0).getString("text");
                String thumbnail = video.getJsonObject("thumbnail").getJsonArray("thumbnails").getJsonObject(0).getString("url");
                String videoUrl = "https://www.youtube.com/watch?v=" + video.getString("videoId");

                if (video.getJsonObject("lengthText") != null) //Live Video
                {
                    String durationString = video.getJsonObject("lengthText").getString("simpleText");
                    int duration = convertStringToDuration(durationString);
                    Song newSong = new Song(title, videoUrl, thumbnail, "", null);
                    newSong.setDuration(duration);
                    songs.add(newSong);
                }
            }
        }
        return songs;
    }

    private int convertStringToDuration(String duration) {
        String[] splitted = duration.split(":");
        int result = 0;
        //Stunden
        if (splitted.length == 3) {
            result += 60 * 60 * Integer.parseInt(splitted[0]) + 60 * Integer.parseInt(splitted[1]) + Integer.parseInt(splitted[2]);
        } else if (splitted.length == 2) {
            result += 60 * Integer.parseInt(splitted[0]) + Integer.parseInt(splitted[1]);
        }
        return result * 1000;
    }
}
