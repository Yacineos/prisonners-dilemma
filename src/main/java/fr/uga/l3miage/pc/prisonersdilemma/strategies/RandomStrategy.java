package fr.uga.l3miage.pc.prisonersdilemma.strategies;

import contract.Game;
import contract.PlayerNumber;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;




public class RandomStrategy extends Strategy{
    private final java.util.Random random;

    public RandomStrategy(java.util.Random random) {
        this.random = random;
    }
    @Override
    public Action play(Game game, PlayerNumber playerNumber){
        int randomInt = random.nextInt(2);
        if (randomInt == 1){
            return Action.COOPERATE;
        }
        return Action.BETRAY;
    }
}
