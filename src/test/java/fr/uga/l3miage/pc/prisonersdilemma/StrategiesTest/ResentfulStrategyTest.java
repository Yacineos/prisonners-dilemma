package fr.uga.l3miage.pc.prisonersdilemma.StrategiesTest;

import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import contract.*;
import fr.uga.l3miage.pc.prisonersdilemma.strategies.ResentfulStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@SpringBootTest
class ResentfulStrategyTest {

    private ResentfulStrategy strategy;
    private Game game;
    private WebSocketSession mockSession;
    private PlayerNumber opponent;

    @BeforeEach
     void setup() {
        mockSession = mock(WebSocketSession.class);
        game = new Game(5, mockSession);
        opponent = PlayerNumber.PLAYER_ONE;
        strategy = new ResentfulStrategy();
    }

    @Test
     void testPlayWithEmptyOpponentHistory() {
        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "ResentfulStrategy should cooperate when opponent history is empty.");
    }

    @Test
     void testPlayWhenOpponentCooperatedLast() {
        game.playTurn(Choice.COOPERATE, opponent);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "ResentfulStrategy should cooperate if opponent's last action was COOPERATE.");
    }

    @Test
     void testPlayWhenOpponentBetrayedLast() {
        game.playTurn(Choice.BETRAY, opponent);
        game.playTurn(Choice.COOPERATE, PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.BETRAY, action, "ResentfulStrategy should betray if opponent's last action was BETRAY.");
    }

    @Test
     void testPlayWithMultipleActionsInOpponentHistory() {
        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.COOPERATE, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.COOPERATE, PlayerNumber.PLAYER_TWO);
        game.playTurn(Choice.BETRAY, opponent);
        game.playTurn(Choice.COOPERATE, PlayerNumber.PLAYER_TWO);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.BETRAY, action, "ResentfulStrategy should betray if the opponent betrayed in the last action, regardless of previous actions.");

        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.BETRAY, PlayerNumber.PLAYER_TWO);
        action = strategy.play(game, opponent);
        assertEquals(Choice.BETRAY, action, "ResentfulStrategy should reset to cooperate if opponent's latest action is COOPERATE.");
    }
}
