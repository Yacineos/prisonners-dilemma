package fr.uga.l3miage.pc.prisonersdilemma.StrategiesTest;

import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import fr.uga.l3miage.pc.prisonersdilemma.enums.PlayerNumber;
import fr.uga.l3miage.pc.prisonersdilemma.game.Game;
import fr.uga.l3miage.pc.prisonersdilemma.strategies.GradualStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class GradualStrategyTest {
    private GradualStrategy strategy;
    private Game game;
    private WebSocketSession mockSession;
    private PlayerNumber opponent;

    @BeforeEach
    public void setup() {
        mockSession = mock(WebSocketSession.class);
        game = new Game(5, mockSession);
        opponent = PlayerNumber.PLAYER_ONE;
        strategy = new GradualStrategy();
    }
    @Test
    public void testPlayWithEmptyHistory() {
        Action action = strategy.play(game, opponent);
        assertEquals(Action.COOPERATE, action, "TitforTat should cooperate on the first move.");
    }

    @Test
    public void testPlayWithOpponentLastActionCooperate() {
        game.playTurn(Action.COOPERATE, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Action.COOPERATE, action, "TitforTat should cooperate if the opponent cooperated last.");
    }

    @Test
    public void testPlayWithOpponentLastActionBetray() {
        game.playTurn(Action.BETRAY, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);


        Action action = strategy.play(game, opponent);
        assertEquals(Action.COOPERATE, action, "GradualStrategy should betray if the opponent betrayed last.");
    }

    @Test
    public void testPlayWithOpponentLastActionTwoTimesInARow() {
        game.playTurn(Action.BETRAY, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);
        game.playTurn(Action.BETRAY, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Action.COOPERATE, action, "GradualStrategy should betray if the opponent betrayed last.");
    }

    @Test
    public void testPlayWithOpponentLastActionThreeTimesInARow() {
        game.playTurn(Action.BETRAY, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);
        game.playTurn(Action.BETRAY, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);
        game.playTurn(Action.BETRAY, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Action.BETRAY, action, "GradualStrategy should betray if the opponent betrayed last.");
    }

    @Test
    public void testPlayWithOpponentLastActionThreeTimesInARowAndCooperatedLast() {
        game.playTurn(Action.BETRAY, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);
        game.playTurn(Action.BETRAY, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);
        game.playTurn(Action.BETRAY, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);
        game.playTurn(Action.COOPERATE, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Action.BETRAY, action, "GradualStrategy should betray if the opponent betrayed last.");
    }

    @Test
    public void testPlayWithOpponentLastActionThreeTimesInARowAndCooperated2Last() {
        game.playTurn(Action.BETRAY, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);
        game.playTurn(Action.BETRAY, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);
        game.playTurn(Action.BETRAY, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);
        game.playTurn(Action.COOPERATE, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);
        game.playTurn(Action.COOPERATE, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Action.COOPERATE, action, "GradualStrategy should betray if the opponent betrayed last.");
    }

    @Test
    public void testPlayWithOpponentMixedTurns() {
        game.playTurn(Action.BETRAY, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);
        game.playTurn(Action.COOPERATE, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);
        game.playTurn(Action.BETRAY, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);
        game.playTurn(Action.COOPERATE, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Action.BETRAY, action, "GradualStrategy should betray if the opponent betrayed last.");
    }
    @Test
    public void testPlayWithOpponentAlternatingPattern() {
        game.playTurn(Action.BETRAY, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);
        game.playTurn(Action.BETRAY, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);
        game.playTurn(Action.COOPERATE, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);
        game.playTurn(Action.COOPERATE, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);
        game.playTurn(Action.BETRAY, opponent);
        game.playTurn(strategy.play(game, opponent), PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Action.BETRAY, action, "GradualStrategy should betray after opponent's mixed pattern.");
    }


}