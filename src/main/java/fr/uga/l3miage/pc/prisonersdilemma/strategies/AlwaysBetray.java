package fr.uga.l3miage.pc.prisonersdilemma.strategies;

import contract.Game;
import contract.PlayerNumber;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;


public class AlwaysBetray extends Strategy{
    @Override
    public Action play(Game game, PlayerNumber opponent) {
        return Action.BETRAY;
    }


}
