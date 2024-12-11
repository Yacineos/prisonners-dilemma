package fr.uga.l3miage.pc.prisonersdilemma.StrategiesTest;

import fr.uga.l3miage.pc.prisonersdilemma.Utils;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import contract.*;
import fr.uga.l3miage.pc.prisonersdilemma.strategies.GradualStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class GradualStrategyTest {
    private GradualStrategy strategy;
    private Game game;
    private WebSocketSession mockSession;
    private PlayerNumber opponent;

    @BeforeEach
     void setup() {
        mockSession = mock(WebSocketSession.class);
        game = new Game(5, mockSession);
        opponent = PlayerNumber.PLAYER_ONE;
        strategy = new GradualStrategy();
    }
    @Test
     void testPlayWithEmptyHistory() {
        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "TitforTat should cooperate on the first move.");
    }

    @Test
     void testPlayWithOpponentLastActionCooperate() {
        game.playTurn(Choice.COOPERATE, opponent);
        Action action1 =strategy.play(game, opponent);
        Choice choice = Utils.convertActionToChoice(action1);
        game.playTurn(choice, PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "TitforTat should cooperate if the opponent cooperated last.");
    }

    @Test
     void testPlayWithOpponentLastActionBetray() {
        game.playTurn(Choice.BETRAY, opponent);
        Action action1 =strategy.play(game, opponent);
        Choice choice = Utils.convertActionToChoice(action1);
        game.playTurn(choice, PlayerNumber.PLAYER_TWO);


        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "GradualStrategy should betray if the opponent betrayed last.");
    }

    @Test
     void testPlayWithOpponentLastActionTwoTimesInARow() {
        game.playTurn(Choice.BETRAY, opponent);
        Action action1 =strategy.play(game, opponent);
        Choice choice = Utils.convertActionToChoice(action1);
        game.playTurn(choice, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.BETRAY, opponent);
        Action action2 =strategy.play(game, opponent);
        Choice choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "GradualStrategy should betray if the opponent betrayed last.");
    }

    @Test
     void testPlayWithOpponentLastActionThreeTimesInARow() {
        game.playTurn(Choice.BETRAY, opponent);
        Action action1 = strategy.play(game, opponent);
        Choice choice = Utils.convertActionToChoice(action1);
        game.playTurn(choice, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.BETRAY, opponent);
        Action action2 = strategy.play(game, opponent);
        Choice choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.BETRAY, opponent);
        Action action3 = strategy.play(game, opponent);
        Choice choice3 = Utils.convertActionToChoice(action3);
        game.playTurn(choice3, PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.BETRAY, action, "GradualStrategy should betray if the opponent betrayed last.");
    }

    @Test
     void testPlayWithOpponentLastActionThreeTimesInARowAndCooperatedLast() {
        game.playTurn(Choice.BETRAY, opponent);
        Action action2 = strategy.play(game, opponent);
        Choice choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.BETRAY, opponent);
        action2 = strategy.play(game, opponent);
        choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.BETRAY, opponent);

        action2 = strategy.play(game, opponent);
        choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.COOPERATE, opponent);

        action2 = strategy.play(game, opponent);
        choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.BETRAY, action, "GradualStrategy should betray if the opponent betrayed last.");
    }

    @Test
     void testPlayWithOpponentLastActionThreeTimesInARowAndCooperated2Last() {
        game.playTurn(Choice.BETRAY, opponent);

        Action action2 = strategy.play(game, opponent);
        Choice choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.BETRAY, opponent);
        action2 = strategy.play(game, opponent);
        choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.BETRAY, opponent);

        action2 = strategy.play(game, opponent);
        choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.COOPERATE, opponent);

        action2 = strategy.play(game, opponent);
        choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.COOPERATE, opponent);

        action2 = strategy.play(game, opponent);
        choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "GradualStrategy should betray if the opponent betrayed last.");
    }

    @Test
     void testPlayWithOpponentMixedTurns() {
        game.playTurn(Choice.BETRAY, opponent);
        Action action2 = strategy.play(game, opponent);
        Choice choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.COOPERATE, opponent);
        action2 = strategy.play(game, opponent);
        choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.BETRAY, opponent);
        action2 = strategy.play(game, opponent);
        choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.COOPERATE, opponent);
        action2 = strategy.play(game, opponent);
        choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.BETRAY, action, "GradualStrategy should betray if the opponent betrayed last.");
    }
    @Test
     void testPlayWithOpponentAlternatingPattern() {

        game.playTurn(Choice.BETRAY, opponent);
        Action action2 = strategy.play(game, opponent);
        Choice choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.BETRAY, opponent);
        action2 = strategy.play(game, opponent);
        choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.COOPERATE, opponent);
        action2 = strategy.play(game, opponent);
        choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.COOPERATE, opponent);
        action2 = strategy.play(game, opponent);
        choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.BETRAY, opponent);
        action2 = strategy.play(game, opponent);
        choice2 = Utils.convertActionToChoice(action2);
        game.playTurn(choice2, PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.BETRAY, action, "GradualStrategy should betray after opponent's mixed pattern.");
    }


}
