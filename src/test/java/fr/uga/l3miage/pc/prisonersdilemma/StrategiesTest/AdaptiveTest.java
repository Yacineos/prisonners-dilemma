package fr.uga.l3miage.pc.prisonersdilemma.StrategiesTest;
import contract.Choice;
import contract.Game;
import contract.PlayerNumber;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import fr.uga.l3miage.pc.prisonersdilemma.strategies.Adaptive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
class AdaptiveTest {


    private Adaptive strategy;
    private Game game;
    private PlayerNumber opponent;

    @BeforeEach
    void setup() {
        game = mock(Game.class);
        strategy = new Adaptive();
        opponent = PlayerNumber.PLAYER_ONE;
    }

    @Test
    void testInitialCooperationSequence() {
        for (int turn = 0; turn < 6; turn++) {
            when(game.getCurrentTurn()).thenReturn(turn);
            Action action = strategy.play(game, opponent);
            assertEquals(Choice.COOPERATE, action, "Adaptive should cooperate during the initial sequence (turn " + turn + ").");
        }
    }

    @Test
     void testInitialBetraySequence() {
        for (int turn = 6; turn < 10; turn++) {
            when(game.getCurrentTurn()).thenReturn(turn);
            Action action = strategy.play(game, opponent);
            assertEquals(Action.BETRAY, action, "Adaptive should betray during the initial betrayal sequence (turn " + turn + ").");
        }
    }

    @Test
     void testActionBasedOnBestMean_CooperatePreferred() {
        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.COOPERATE, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.COOPERATE, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.COOPERATE, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.COOPERATE, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.COOPERATE, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.COOPERATE, PlayerNumber.PLAYER_TWO);
        for(int i = 0; i < 4; i++){
            game.playTurn(Choice.BETRAY,opponent);
            game.playTurn(Choice.BETRAY, PlayerNumber.PLAYER_TWO);
        }

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "Adaptive should prefer cooperation if it has a better mean score.");
    }

    @Test
     void testActionBasedOnBestMean_BetrayPreferred() {
        when(game.getCurrentTurn()).thenReturn(10);

        when(game.getScoreByTurnNumberAndByPlayerNumber(anyInt(), any())).thenAnswer(invocation -> {
            int turn = invocation.getArgument(0);
            return (turn < 6) ? 2 : 5;
        });

        Action action = strategy.play(game, opponent);
        assertEquals(Action.BETRAY, action, "Adaptive should prefer betrayal if it has a better mean score.");
    }

    @Test
     void testEdgeCase_InsufficientTurns() {
        when(game.getCurrentTurn()).thenReturn(5);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "Adaptive should cooperate if the current turn is within the initial sequence.");
    }


}
