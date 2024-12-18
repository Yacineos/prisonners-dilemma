package fr.uga.l3miage.pc.prisonersdilemma.StrategiesTest;

import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import fr.uga.l3miage.pc.prisonersdilemma.enums.PlayerNumber;
import fr.uga.l3miage.pc.prisonersdilemma.game.Game;
import fr.uga.l3miage.pc.prisonersdilemma.strategies.AlwaysCooperate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@SpringBootTest
class AlwaysCooperateTest {

    private AlwaysCooperate strategy;
    private Game game;
    private WebSocketSession mockSession1;
    private PlayerNumber opponent;

    @BeforeEach
     void setup() {
        mockSession1 = mock(WebSocketSession.class);
        game = new Game(5, mockSession1);
        strategy = new AlwaysCooperate();
        opponent = PlayerNumber.PLAYER_ONE;

    }

    @Test
     void testPlayWithEmptyOpponentHistory() {
        Action action = strategy.play(game, opponent);
        assertEquals(Action.COOPERATE, action, "AlwaysBetray should always return BETRAY, even with empty opponent history.");
    }

    @Test
     void testPlayWithSingleActionInOpponentHistory() {
        game.playTurn(Action.COOPERATE, opponent);

        Action action = strategy.play(game, opponent);
        assertEquals(Action.COOPERATE, action, "AlwaysBetray should always return BETRAY, regardless of opponent's history.");
    }

    @Test
     void testPlayWithMultipleActionsInOpponentHistory() {
        game.playTurn(Action.COOPERATE, opponent);
        game.playTurn(Action.BETRAY, opponent);

        Action action = strategy.play(game, opponent);
        assertEquals(Action.COOPERATE, action, "AlwaysBetray should always return BETRAY, regardless of opponent's history.");
    }
}

