package fr.uga.l3miage.pc.prisonersdilemma.StrategiesTest;

import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import contract.*;
import fr.uga.l3miage.pc.prisonersdilemma.strategies.TitforTwoTats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@SpringBootTest
class TitforTwoTatsTest {

    private TitforTwoTats strategy;
    private Game game;
    private WebSocketSession mockSession;
    private PlayerNumber opponent;

    @BeforeEach
     void setup() {
        mockSession = mock(WebSocketSession.class);
        game = new Game(5, mockSession);
        opponent = PlayerNumber.PLAYER_ONE;
        strategy = new TitforTwoTats();
    }

    @Test
     void testPlayWithInsufficientHistory() {
        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "TitforTwoTats should cooperate if the opponent's history is less than 2 actions.");

    }
    @Test
     void testPlayWhenOpponentHasMixedLastActions() {
        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.COOPERATE, PlayerNumber.PLAYER_TWO);

        game.playTurn(Choice.BETRAY, opponent);
        game.playTurn(Choice.BETRAY, PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.BETRAY, action, "TitforTwoTats should BETRAY when the opponent's last two actions are mixed, and reciprocity has not started.");
    }

    @Test
     void testPlayWithTwoIdenticalLastActionsAndTriggerReciprocity() {
        game.playTurn(Choice.BETRAY, opponent);
        game.playTurn(Choice.BETRAY, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.BETRAY, opponent);
        game.playTurn(Choice.BETRAY, PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.BETRAY, action, "TitforTwoTats should BETRAY and trigger reciprocity when the opponent's last two actions are identical.");

        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.BETRAY, PlayerNumber.PLAYER_TWO);
        action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "TitforTwoTats should mimic the opponent's last action after reciprocity has started.");
    }

    @Test
     void testPlayWhenReciprocityAlreadyStarted() {
        game.playTurn(Choice.BETRAY, opponent);
        game.playTurn(Choice.BETRAY, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.BETRAY, opponent);
        game.playTurn(Choice.BETRAY, PlayerNumber.PLAYER_TWO);

        strategy.play(game, opponent);

        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.BETRAY, PlayerNumber.PLAYER_TWO);
        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "TitforTwoTats should continue to mimic the opponent's actions after reciprocity has started.");

        game.playTurn(Choice.BETRAY, opponent);
        game.playTurn(Choice.COOPERATE, PlayerNumber.PLAYER_TWO);
        action = strategy.play(game, opponent);
        assertEquals(Choice.BETRAY, action, "TitforTwoTats should continue to mimic the opponent's betrayal after reciprocity has started.");
    }

    @Test
     void testPlayWhenOpponentCooperatesTwice() {
        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.BETRAY, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.BETRAY, PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "TitforTwoTats should COOPERATE when the opponent cooperates twice.");
    }

    @Test
     void testPlayWithLongHistoryAndMixedActions() {
        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.BETRAY, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.BETRAY, opponent);
        game.playTurn(Choice.BETRAY, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.BETRAY, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.BETRAY, opponent);
        game.playTurn(Choice.BETRAY, PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.BETRAY, action, "TitforTwoTats should BETRAY when the opponent's last action is BETRAY, regardless of earlier history.");
    }

}
