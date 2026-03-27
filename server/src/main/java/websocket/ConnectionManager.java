package websocket;

import org.eclipse.jetty.websocket.api.Session;

import websocket.messages.ServerMessage;


import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Session> connections = new ConcurrentHashMap<>();


    public void add(Session session, Integer gameId) {
        connections.put(gameId, session);
    }

    public void remove(Session session, Integer gameID) {
        connections.remove(gameID,session);
    }

    public void broadcast(Session excludeSession, ServerMessage message) throws IOException {
        String msg = message.toString();
        for (Session c : connections.values()) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}
