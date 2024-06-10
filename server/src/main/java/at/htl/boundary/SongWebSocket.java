package at.htl.boundary;

import at.htl.entity.Song;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/songs")
@ApplicationScoped
public class SongWebSocket {
    private final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        sendMessageToAll("New client connected: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        sendMessageToAll("Client disconnected: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Handle received messages from clients, e.g., command to add or remove a song
    }

    public void songAdded(Song song) {
        sendMessageToAll("Song added: " + song.getSongName());
    }

    public void songRemoved(Song song) {
        sendMessageToAll("Song removed: " + song.getSongName());
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
}
