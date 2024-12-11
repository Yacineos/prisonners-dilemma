package fr.uga.l3miage.pc.prisonersdilemma.strategies;

import contract.*;
import fr.uga.l3miage.pc.prisonersdilemma.Utils;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TitforTwoTats extends Strategy{


    private boolean startReciprocity = false ;


    @Override
    public Action play(Game game, PlayerNumber opponent){
        if (isOpponentHistoryEmpty(game)) {
            return Action.COOPERATE;
        }
        if(startReciprocity){
            return getOpponentLastAction(game, opponent);
        }
        if (isBothLastOpponentActionsSame(game,opponent)){
            startReciprocity = true;
            return getOpponentLastAction(game, opponent);
        }
        return Action.BETRAY;
    }


    private boolean isBothLastOpponentActionsSame(Game game, PlayerNumber opponent){
        if(last2Turns(game).size() < 2){
            return false;
        }
        return last2Turns(game).get(0).getChoiceByPlayerNumber(opponent) == last2Turns(game).get(1).getChoiceByPlayerNumber(opponent);
    }

    private Action getOpponentLastAction(Game game, PlayerNumber opponent) {
        Choice choice = game.getTurnThatJustEnded().getChoiceByPlayerNumber(opponent) ;
        Action action = Utils.convertChoiceToAction(choice);

        return action;
    }

    private boolean isOpponentHistoryEmpty(Game game){
        return game.getTurnThatJustEnded() == null;
    }
    private List<Turn> last2Turns(Game game){
        List<Turn> lastTwoTurns = new ArrayList<>();
        int currentTurn = game.getCurrentTurn();
        if (currentTurn == 0) {
            return lastTwoTurns;
        }
        int start = Math.max(0, currentTurn - 2);
        lastTwoTurns.addAll(Arrays.asList(game.getTurns()).subList(start, currentTurn));

        return lastTwoTurns;
    }
}
