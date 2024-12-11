package fr.uga.l3miage.pc.prisonersdilemma.StrategiesTest;

import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import contract.*;
import fr.uga.l3miage.pc.prisonersdilemma.strategies.RandomStrategy;
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
class RandomTest {

    private Random mockRandom;
    private RandomStrategy strategy;
    private Game game;
    private WebSocketSession mockSession;
    private PlayerNumber opponent;


    @BeforeEach
     void setup() {
        mockSession = mock(WebSocketSession.class);
        game = new Game(5, mockSession);
        opponent = PlayerNumber.PLAYER_ONE;
        mockRandom = Mockito.mock(Random.class);
        strategy = new RandomStrategy(mockRandom);
    }

    @Test
     void testPlayReturnsCooperateWhenRandomIsOne() {
        when(mockRandom.nextInt(2)).thenReturn(1);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "Random strategy should return COOPERATE when random is 1.");
    }

    @Test
     void testPlayReturnsBetrayWhenRandomIsZero() {
        when(mockRandom.nextInt(2)).thenReturn(0);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.BETRAY, action, "Random strategy should return BETRAY when random is 0.");
    }

    @Test
     void testPlayWithNonEmptyOpponentHistory() {
        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.BETRAY, opponent);

        when(mockRandom.nextInt(2)).thenReturn(1);
        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "Random strategy should return COOPERATE when random is 1, regardless of opponent history.");

        when(mockRandom.nextInt(2)).thenReturn(0);
        action = strategy.play(game, opponent);
        assertEquals(Choice.BETRAY, action, "Random strategy should return BETRAY when random is 0, regardless of opponent history.");
    }
}
