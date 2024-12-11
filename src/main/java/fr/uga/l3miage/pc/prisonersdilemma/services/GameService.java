package fr.uga.l3miage.pc.prisonersdilemma.services;

import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import fr.uga.l3miage.pc.prisonersdilemma.enums.PlayerNumber;
import fr.uga.l3miage.pc.prisonersdilemma.game.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
public class GameService {

    private static GameService instance = null ;

    private final Map<String, Game> currentGames = new ConcurrentHashMap<>();

    private GameService() {}

    public static GameService getInstance() {
        if(instance == null) {
            GameService.instance= new GameService();
        }
        return instance;
    }

    public String  createGame(WebSocketSession playerOne, String payload) throws IOException {
        int maxTurns;

        if(!payload.startsWith("CREATE_GAME")) {
            throw new IOException("payload does not start with CREATE_GAME for create game request");
        }

        try{
            maxTurns = getMaxTurns(payload);
        } catch (Exception e) {
            throw new IOException("maxTurns not found in payload or not an integer");
        }

        Game game = new Game(maxTurns, playerOne);
        String gameId = game.getId();
        currentGames.put(gameId, game);

        sendGameIdToPlayer(playerOne, gameId);

        return gameId;
    }

    private int getMaxTurns(String payload){
        String maxTurnsInStringFormat = getMaxTurnsFromPayload(payload);
        return maxTurnsFromString(maxTurnsInStringFormat);
    }

    private int maxTurnsFromString(String maxTurnsStringFormat) {
        return Integer.parseInt(maxTurnsStringFormat);
    }

    private String getMaxTurnsFromPayload(String payload) {
        return payload.split(":")[1];
    }

    public void joinGame(WebSocketSession session, String payload) throws IOException {
        String gameId;

        if(!payload.startsWith("JOIN_GAME")) {
            throw new IOException("payload does not start with JOIN_GAME for join game request");
        }

        try {
            gameId = extractGameIdFromPayload(payload);
        } catch (Exception e) {
            throw new IOException("gameId not found in payload");
        }

        if(!doesGameExists(gameId) || isGameFull(gameId) ) {
            throw new IllegalArgumentException("Game is full or does not exist");
        }

        addPlayerTwoToGame(session, gameId);

        //get Player one session
        WebSocketSession playerOne = currentGames.get(gameId).getPlayerOne();

        informPlayerOneThatPlayerTwoJoined(playerOne);

    }

    private static void informPlayerOneThatPlayerTwoJoined(WebSocketSession session) throws IOException {
        session.sendMessage(new TextMessage("PLAYER_TWO_JOINED"));
    }

    private String extractGameIdFromPayload(String payload) {
        return payload.split(":")[1];
    }

    public String extractPlayerActionFromPayload(String payload) {
        return payload.split(":")[2];
    }

    private boolean isGameFull(String gameId) {
        Game game = currentGames.get(gameId);
        return game.isFull();
    }

    private boolean doesGameExists(String gameId){
        return currentGames.get(gameId) != null;
    }

    public Set<WebSocketSession> getPlayers(String gameId) {

        if(!doesGameExists(gameId)){
            throw new IllegalArgumentException("Game does not exist");
        }

        Set<WebSocketSession> players = new HashSet<>();
        Game game = currentGames.get(gameId);

        if (game.getPlayerOne() != null) {
            players.add(game.getPlayerOne());
        }
        if (game.getPlayerTwo() != null) {
            players.add(game.getPlayerTwo());
        }

        return players;
    }

    private void addPlayerTwoToGame(WebSocketSession session, String gameId) {
        this.currentGames.get(gameId).addSecondPlayerToTheGame(session);
    }

    public static void sendGameIdToPlayer(WebSocketSession session, String gameId) throws IOException {
        session.sendMessage(new TextMessage("GAME_ID:" + gameId));
    }

    public void leaveGames(WebSocketSession session) {
        currentGames.values().forEach(game -> game.removePLayer(session));
    }

    public void action(WebSocketSession player, String payload) {
        String gameId = extractGameIdFromPayload(payload);
        String playerActionStr = extractPlayerActionFromPayload(payload);

        if (!doesGameExists(gameId)) {
            throw new IllegalArgumentException("Game not found");
        }

        Action action;
        try {
            action = Action.valueOf(playerActionStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid action");
        }

        Game game = currentGames.get(gameId);
        PlayerNumber playerNumber = getPlayerNumber(player, game);

        game.play(action, playerNumber);

        if(game.bothPlayerTwoHavePlayedLastTurn()){
            sendTurnSummaryToBothPlayers(gameId, game.getCurrentTurn() - 1);
        }
    }

    public PlayerNumber getPlayerNumber(WebSocketSession player, Game game) {
        if(game.getPlayerOne() != player && game.getPlayerTwo() != player){
            throw new IllegalArgumentException("Player is not in the game");
        }
        return game.getPlayerOne() == player ? PlayerNumber.PLAYER_ONE : PlayerNumber.PLAYER_TWO;
    }

    public Action getPlayerAction(String gameId, WebSocketSession player, int turn) {
        if(!doesGameExists(gameId)){
            throw new IllegalArgumentException("Game does not exist");
        }
        if(!getPlayers(gameId).contains(player)){
            throw new IllegalArgumentException("Player is not in the game");
        }

        Game game = currentGames.get(gameId);
        PlayerNumber playerNumber = getPlayerNumber(player, game);

        return game.getTurns()[turn].getChoiceByPlayerNumber(playerNumber);
    }

    public void sendTurnSummaryToBothPlayers(String gameId, int i) {
        Game game = currentGames.get(gameId);
        Set<WebSocketSession> players = getPlayers(gameId);

        Action playerOneAction = game.getTurns()[i].getPlayerOneAction();
        Action playerTwoAction = game.getTurns()[i].getPlayerTwoAction();

        players.forEach(player -> {
            try {
                player.sendMessage(new TextMessage("TURN_SUMMARY:" + playerOneAction + ":" + playerTwoAction));
            } catch (IOException e) {
                log.debug(String.valueOf(e));
            }
        });
    }
}
