package websocket;

import org.eclipse.jetty.websocket.api.Session;

import websocket.messages.ServerMessage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<Session>> connections = new ConcurrentHashMap<>();


    public void add(Session session, Integer gameId) {

        ArrayList<Session> sessions = new ArrayList<>();
              if(  connections.get(gameId) != null){
                   sessions = connections.get(gameId);
              }


        sessions.add(session);
        connections.put(gameId, sessions);
    }

    public void remove(Session session, Integer gameID) {
        connections.remove(gameID,session);
    }

    public void broadcast(Session excludeSession, ServerMessage message, Integer gameId) throws IOException {
        String msg = message.toString();

        ArrayList<Session> sessions = connections.get(gameId);

        for (Session c : sessions) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}
