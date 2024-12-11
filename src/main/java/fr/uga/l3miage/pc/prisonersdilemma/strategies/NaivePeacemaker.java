package fr.uga.l3miage.pc.prisonersdilemma.strategies;

import contract.Choice;
import contract.Game;
import contract.PlayerNumber;
import fr.uga.l3miage.pc.prisonersdilemma.Utils;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;

import java.util.Random;

public class NaivePeacemaker extends Strategy{
    private final Random random;

    public NaivePeacemaker(Random random) {
        this.random = random;
    }

    @Override
    public Action play(Game game, PlayerNumber opponent){
        if (isOpponentHistoryEmpty(game)) {
            return Action.COOPERATE;
        }
        if(hasOpponentBetrayed(game, opponent) && isNextActionCooperate()) {
            return Action.COOPERATE;
        }
        return opponentLastAction(game, opponent);

    }

    private boolean hasOpponentBetrayed(Game game, PlayerNumber opponent){
        return opponentLastAction(game, opponent) == Action.BETRAY;
    }

    private boolean isNextActionCooperate() {
        int randomInt = random.nextInt(2);
        return randomInt == 1;
    }

    private Action opponentLastAction(Game game, PlayerNumber opponent){
        Choice choice = game.getTurnThatJustEnded().getChoiceByPlayerNumber(opponent);
        Action res = Utils.convertChoiceToAction(choice);
        return res;
    }

    private boolean isOpponentHistoryEmpty(Game game){
        return game.getTurnThatJustEnded() == null;
    }

}
