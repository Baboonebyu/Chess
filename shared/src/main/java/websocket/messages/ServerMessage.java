package websocket.messages;

import chess.ChessGame;
import com.google.gson.Gson;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    String message;
    String game;
    String errorMessage;

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;

    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage that)) {
            return false;
        }
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
    public String toString() {
        return new Gson().toJson(this);
    }
}

