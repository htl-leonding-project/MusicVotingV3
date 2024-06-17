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

        Document doc = null;
        int attempts = 0;
        while (doc == null && attempts < 5) {
            try {
                doc = Jsoup.connect(baseUrl + queryTerm)
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                        .header("Accept-Language", "en-US,en;q=0.5")
                        .get();
            } catch (IOException e) {
                attempts++;
                try {
                    Thread.sleep(150L * attempts); // Exponential backoff
                } catch (InterruptedException interruptedException) {
                    throw new RuntimeException(interruptedException);
                }
            }
        }

        if (doc == null) {
            throw new RuntimeException("Failed to fetch YouTube results after several attempts.");
        }

        Element scriptElement = doc.select("script:containsData(var ytInitialData)").first();
        if (scriptElement == null) {
            throw new RuntimeException("JSON object not found in response.");
        }

        String scriptContent = scriptElement.html();
        int startOfJsonObjectIndex = scriptContent.indexOf("{");
        int endOfJsonObjectIndex = scriptContent.lastIndexOf("}") + 1;

        if (startOfJsonObjectIndex == -1 || endOfJsonObjectIndex == -1) {
            System.out.println(scriptContent); // Print the script content for debugging
            throw new RuntimeException("JSON object not found in response.");
        }

        String jsonString = scriptContent.substring(startOfJsonObjectIndex, endOfJsonObjectIndex);
        JsonObject json = new JsonObject(jsonString);

        if (json == null) {
            throw new RuntimeException("Error parsing JSON object. Object was null");
        }

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
