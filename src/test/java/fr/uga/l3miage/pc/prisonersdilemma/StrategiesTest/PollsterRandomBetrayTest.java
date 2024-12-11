package fr.uga.l3miage.pc.prisonersdilemma.StrategiesTest;

import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import contract.*;
import fr.uga.l3miage.pc.prisonersdilemma.strategies.PollsterRandomBetray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.socket.WebSocketSession;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class PollsterRandomBetrayTest {
    private Random mockRandom;
    private PollsterRandomBetray strategy;
    private Game game;
    private WebSocketSession mockSession;
    private PlayerNumber opponent;

    @BeforeEach
     void setup() {
        mockSession = mock(WebSocketSession.class);
        game = new Game(5, mockSession);
        mockRandom = Mockito.mock(Random.class);
        strategy = new PollsterRandomBetray(mockRandom);
        opponent = PlayerNumber.PLAYER_ONE;
    }

    @Test
     void testPlayWithEmptyHistory() {
        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "PollsterRandomBetray should cooperate if the opponent's history is empty.");
    }

    @Test
     void testPlayWithRandomBetray() {
        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.COOPERATE, PlayerNumber.PLAYER_TWO);
        when(mockRandom.nextInt(2)).thenReturn(1);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.BETRAY, action, "PollsterRandomBetray should betray when isNextActionBetray is true.");
    }

    @Test
     void testPlayWithTitForTatBehavior() {
        game.playTurn(Choice.BETRAY, opponent);
        game.playTurn(Choice.COOPERATE, PlayerNumber.PLAYER_TWO);

        when(mockRandom.nextInt(2)).thenReturn(0);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.BETRAY, action, "PollsterRandomBetray should mimic the last action if isNextActionBetray is false.");
    }

    @Test
     void testPlayWithOpponentCooperateAndRandomCooperate() {
        game.playTurn(Choice.COOPERATE, opponent);

        when(mockRandom.nextInt(2)).thenReturn(0);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "PollsterRandomBetray should mimic the opponent's last action when isNextActionBetray is false.");
    }
}
