package fr.uga.l3miage.pc.prisonersdilemma.StrategiesTest;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import contract.*;
import fr.uga.l3miage.pc.prisonersdilemma.strategies.TitforTat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@SpringBootTest
class TitforTatTest {
    private TitforTat strategy;
    private Game game;
    private WebSocketSession mockSession;
    private PlayerNumber opponent;

    @BeforeEach
     void setup() {
        mockSession = mock(WebSocketSession.class);
        game = new Game(5, mockSession);
        opponent = PlayerNumber.PLAYER_ONE;
        strategy = new TitforTat();
    }
    @Test
     void testPlayWithEmptyHistory() {
        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "TitforTat should cooperate on the first move.");
    }

    @Test
     void testPlayWithOpponentLastActionCooperate() {
        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.COOPERATE, PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "TitforTat should cooperate if the opponent cooperated last.");
    }

    @Test
     void testPlayWithOpponentLastActionBetray() {
        game.playTurn(Choice.BETRAY, opponent);
        game.playTurn(Choice.COOPERATE, PlayerNumber.PLAYER_TWO);


        Action action = strategy.play(game, opponent);
        assertEquals(Choice.BETRAY, action, "TitforTat should betray if the opponent betrayed last.");
    }
}
