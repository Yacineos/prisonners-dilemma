package fr.uga.l3miage.pc.prisonersdilemma;

import contract.Choice;
import contract.PlayerNumber;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;

public class Utils {
    public static Choice convertActionToChoice(Action action){
        if(action == Action.COOPERATE)
            return Choice.COOPERATE;
        if(action == Action.BETRAY)
            return Choice.BETRAY;
        return Choice.NONE;
    }
    public static Action convertChoiceToAction(Choice choice){
        if(choice == Choice.COOPERATE)
            return Action.COOPERATE;
        if(choice == Choice.BETRAY)
            return Action.BETRAY;
        return Action.NONE;
    }
    public static PlayerNumber convertIntToOpponentNumber(int playerNumber)
    {
        if(playerNumber == 1){
            return PlayerNumber.PLAYER_TWO;
        }
        return PlayerNumber.PLAYER_ONE;
    }
}
