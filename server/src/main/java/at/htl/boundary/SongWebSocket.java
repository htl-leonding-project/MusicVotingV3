package at.htl.boundary;

import at.htl.entity.Song;
import at.htl.control.SongRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ServerEndpoint("/songs")
@ApplicationScoped
public class SongWebSocket {

    private final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @Inject
    SongRepository songRepository;

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        sendAllSongs(session); // Send all songs to the new client
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Handle received messages from clients if needed
    }

    public void notifyAllSongsChanged() {
        sendMessageToAll(getAllSongsAsJson());
    }

    private void sendAllSongs(Session session) {
        String message = getAllSongsAsJson();
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToAll(String message) {
        for (Session session : sessions) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getAllSongsAsJson() {
        List<Song> songs = songRepository.getPlaylist();
        return convertToJson(songs); // Implement a method to convert songs list to JSON string
    }

    private String convertToJson(List<Song> songs) {
        // Implement this method to convert songs list to JSON
        // You can use a library like Jackson or Gson for this
        // Example using Gson:
        // return new Gson().toJson(songs);
        return "[]"; // Replace with actual implementation
    }
}
