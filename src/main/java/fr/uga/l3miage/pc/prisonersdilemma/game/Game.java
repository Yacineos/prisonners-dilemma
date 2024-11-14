package fr.uga.l3miage.pc.prisonersdilemma.game;

import fr.uga.l3miage.pc.prisonersdilemma.Turn;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import fr.uga.l3miage.pc.prisonersdilemma.enums.PlayerNumber;
import fr.uga.l3miage.pc.prisonersdilemma.game.states.State;
import fr.uga.l3miage.pc.prisonersdilemma.game.states.WaitingState;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Game {

    private String id;
    private Turn[] turns;
    private State state;
    private int maxTurns;
    private int currentTurn;

    private WebSocketSession playerOne;
    private WebSocketSession playerTwo;

    public Game(int maxTurns, WebSocketSession playerOne) {
        generateGameId();
        initTurns(maxTurns);
        this.state = new WaitingState(this);
        this.maxTurns = maxTurns;
        this.currentTurn = 0;
        this.playerOne = playerOne;
    }

    public void changeState(State state){
        this.state = state;
    }

    private void incrementTurn(){
        this.currentTurn++;
    }

    private void initTurns(int maxTurns) {
        this.turns = new Turn[maxTurns];
        for(int i = 0; i < maxTurns; i++){
            this.turns[i] = new Turn(Action.NONE,Action.NONE);
        }
    }

    public void play(Action action, PlayerNumber playerNumber){
        this.state.play(action, playerNumber);
    }

    public void playTurn(Action action, PlayerNumber playerNumber){
        this.turns[this.currentTurn].updateTurn(action, playerNumber);

        if(bothPlayerTwoHavePlayedTheirTurn()){
            incrementTurn();
        }
    }

    private boolean bothPlayerTwoHavePlayedTheirTurn() {
        return this.turns[this.currentTurn].getPlayerTwoAction() != Action.NONE && this.turns[this.currentTurn].getPlayerOneAction() != Action.NONE;
    }

    public List<Action> getHistoryByPlayerNumber(PlayerNumber playerNumber){
        List<Action> history = new ArrayList<>();
        for(Turn turn : getTurns()){
            history.add(turn.getActionByPlayerNumber(playerNumber) );
        }
        return history;
    }

    private void generateGameId() {
        this.id = UUID.randomUUID().toString();
    }

    public void addSecondPlayerToTheGame(WebSocketSession playerTwo) {
        if(this.playerTwo != null){
            throw new IllegalArgumentException("Game is full");
        }
        this.playerTwo = playerTwo;
        this.state.getNextState();
    }

    public void removePLayer(WebSocketSession session) {
        if(this.playerOne == session){
            this.playerOne = null;
        } else if(this.playerTwo == session){
            this.playerTwo = null;
        }
    }

    public boolean isFull() {
        return this.playerOne != null && this.playerTwo != null;
    }
}
