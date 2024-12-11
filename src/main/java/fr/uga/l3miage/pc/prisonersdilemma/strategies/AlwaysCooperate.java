package fr.uga.l3miage.pc.prisonersdilemma.strategies;

import contract.*;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;



public class AlwaysCooperate extends Strategy{

    @Override
    public Action play(Game game, PlayerNumber opponent) {
        return Action.COOPERATE;
    }
}
