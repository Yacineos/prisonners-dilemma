package fr.uga.l3miage.pc.prisonersdilemma.strategies;

import fr.uga.l3miage.pc.prisonersdilemma.Utils;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import contract.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Peacemaker extends Strategy{

    private final Random random;

    public Peacemaker(Random random) {
        this.random = random;
    }

    @Override
    public Action play(Game game, PlayerNumber opponent){
        if (isOpponentHistoryEmpty(game)) {
            return Action.COOPERATE;
        }
        if(hasOpponentBetrayed2TimesInARow(game,opponent)&& !isNextTurnARandomPeaceTurn()){
            return Action.BETRAY;
        }
        return Action.COOPERATE;
    }


    private boolean isNextTurnARandomPeaceTurn(){
        int randomInt = random.nextInt(2);
        return randomInt == 1;
    }

    private boolean isOpponentHistoryEmpty(Game game){
        return game.getTurnThatJustEnded() == null;
    }

    private List<Turn> last2opponentTurns(Game game){
        List<Turn> lastTwoTurns = new ArrayList<>();
        int currentTurn = game.getCurrentTurn();
        if (currentTurn == 0) {
            return lastTwoTurns;
        }
        int start = Math.max(0, currentTurn - 2);
        lastTwoTurns.addAll(Arrays.asList(game.getTurns()).subList(start, currentTurn));

        return lastTwoTurns;
    }

    public boolean hasOpponentBetrayed2TimesInARow(Game game, PlayerNumber opponent){
        List<Turn> lastTwoTurns = last2opponentTurns(game);
        if(lastTwoTurns.isEmpty() || lastTwoTurns.size() == 1){
            return false;
        }else{
            Choice choice1 = lastTwoTurns.get(0).getChoiceByPlayerNumber(opponent);
            Action action1 = Utils.convertChoiceToAction(choice1);
            Choice choice2 = lastTwoTurns.get(1).getChoiceByPlayerNumber(opponent);
            Action action2 = Utils.convertChoiceToAction(choice2);
            return  action1 == Action.BETRAY && action2 == Action.BETRAY;
        }

    }
}
