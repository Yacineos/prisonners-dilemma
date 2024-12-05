package fr.uga.l3miage.pc.prisonersdilemma.StrategiesTest;

import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import fr.uga.l3miage.pc.prisonersdilemma.enums.PlayerNumber;
import fr.uga.l3miage.pc.prisonersdilemma.game.Game;
import fr.uga.l3miage.pc.prisonersdilemma.strategies.TitForTatSuspicious;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
@SpringBootTest
public class TitForTatSuspiciousTest {
    private TitForTatSuspicious strategy;
    private Game game;
    private WebSocketSession mockSession;
    private PlayerNumber opponent;

    @BeforeEach
    public void setup() {
        mockSession = mock(WebSocketSession.class);
        game = new Game(5, mockSession);
        opponent = PlayerNumber.PLAYER_ONE;
        strategy = new TitForTatSuspicious();
    }
    @Test
    public void testPlayWithEmptyHistory() {
        Action action = strategy.play(game, opponent);
        assertEquals(Action.BETRAY, action, "TitForTatSuspicious should cooperate on the first move.");
    }

    @Test
    public void testPlayWithOpponentLastActionCooperate() {
        game.playTurn(Action.COOPERATE, opponent);
        game.playTurn(Action.COOPERATE, PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Action.COOPERATE, action, "TitForTatSuspicious should cooperate if the opponent cooperated last.");
    }

    @Test
    public void testPlayWithOpponentLastActionBetray() {
        game.playTurn(Action.BETRAY, opponent);
        game.playTurn(Action.COOPERATE, PlayerNumber.PLAYER_TWO);


        Action action = strategy.play(game, opponent);
        assertEquals(Action.BETRAY, action, "TitForTatSuspicious should betray if the opponent betrayed last.");
    }
}
