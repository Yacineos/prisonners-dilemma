package fr.uga.l3miage.pc.prisonersdilemma;

import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import fr.uga.l3miage.pc.prisonersdilemma.enums.PlayerNumber;
import lombok.Getter;

public class Turn {
    private Action[] actions;
    @Getter
    private Score scores;
    public Turn(Action actionPlayerOne, Action actionPlayerTwo){
        this.actions =  new Action[2];
        actions[0] = actionPlayerOne;
        actions[1] = actionPlayerTwo;
        this.scores = new Score(0,0);
    }

    public void updateTurn(Action actionPlayer, PlayerNumber playerNumber){
        this.actions[playerNumber.getIndex()] = actionPlayer;
    }

    public Action getChoiceByPlayerNumber(PlayerNumber playerNumber){
        return this.actions[playerNumber.getIndex()];
    }

    public Action getPlayerOneAction() {
        return this.actions[0];
    }

    public Action getPlayerTwoAction() {
        return this.actions[1];
    }

    public int getScorePlayerOne(){
        return this.scores.getScorePlayerOne();
    }

    public int getScorePlayerTwo() {
        return this.scores.getScorePlayerTwo();
    }

    public void calculateScore(){
        if(isTurnFinished()){
            if(isTurnEvenCooperate()){
                this.scores.setScorePlayerOne(3);
                this.scores.setScorePlayerTwo(3);
            }
            else if(hasPlayerOneWon()){
                this.scores.setScorePlayerOne(5);
                this.scores.setScorePlayerTwo(0);
            }
            else if(hasPlayerTwoWon()){
                this.scores.setScorePlayerOne(0);
                this.scores.setScorePlayerTwo(5);
            }else{
                this.scores.setScorePlayerOne(1);
                this.scores.setScorePlayerTwo(1);
            }
        }
    }

    private boolean isTurnFinished(){
        return getPlayerOneAction()!= Action.NONE && getPlayerTwoAction() != Action.NONE;
    }

    private boolean isTurnEvenCooperate(){
        return getPlayerOneAction() == getPlayerTwoAction() && getPlayerOneAction() == Action.COOPERATE;
    }

    private boolean hasPlayerOneWon(){
        return getPlayerOneAction()== Action.BETRAY && getPlayerTwoAction() == Action.COOPERATE;
    }

    private boolean hasPlayerTwoWon(){
        return getPlayerOneAction()== Action.COOPERATE && getPlayerTwoAction() == Action.BETRAY;
    }


}
