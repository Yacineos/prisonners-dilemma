package fr.uga.l3miage.pc.prisonersdilemma.strategies;

import contract.Choice;
import contract.CommonStrategy;
import contract.Game;
import contract.PlayerNumber;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;

import static fr.uga.l3miage.pc.prisonersdilemma.Utils.convertActionToChoice;
import static fr.uga.l3miage.pc.prisonersdilemma.Utils.convertIntToOpponentNumber;


public abstract class Strategy implements CommonStrategy {
    public abstract Action play(Game game, PlayerNumber opponent);
    public Choice makeChoice(Game game, int playerReplaced){
        PlayerNumber currentPlayer = convertIntToOpponentNumber(playerReplaced);
        Action action = play(game, currentPlayer) ;
        return convertActionToChoice(action);
    }

}
