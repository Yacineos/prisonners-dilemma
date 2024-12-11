package fr.uga.l3miage.pc.prisonersdilemma.StrategiesTest;

import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import contract.*;
import fr.uga.l3miage.pc.prisonersdilemma.strategies.NaivePeacemaker;
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
class NaivePeacemakerTest {

    private Random mockRandom;
    private NaivePeacemaker strategy;
    private Game game;
    private WebSocketSession mockSession;
    private PlayerNumber opponent;

    private PlayerNumber strategyPlayernumber;

    @BeforeEach
     void setup() {
        mockSession = mock(WebSocketSession.class);
        game = new Game(5, mockSession);
        mockRandom = Mockito.mock(Random.class);
        strategy = new NaivePeacemaker(mockRandom);
        opponent = PlayerNumber.PLAYER_ONE;
        strategyPlayernumber = PlayerNumber.PLAYER_TWO;
    }

    @Test
     void testPlayWithEmptyOpponentHistory() {
        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "NaivePassificator should start with COOPERATE on an empty opponent history.");
    }

    @Test
     void testPlayWhenOpponentBetrayedAndNextActionIsCooperate() {
        game.playTurn(Choice.BETRAY, opponent);
        when(mockRandom.nextInt(2)).thenReturn(1);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "NaivePassificator should cooperate after opponent's betrayal if random check allows.");
    }

    @Test
     void testPlayWhenOpponentBetrayedAndNextActionIsNotCooperate() {
        game.playTurn(Choice.BETRAY, opponent);
        game.playTurn(Choice.COOPERATE, strategyPlayernumber);
        when(mockRandom.nextInt(2)).thenReturn(0);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.BETRAY, action, "NaivePassificator should mimic the opponent's betrayal if random check disallows cooperation.");
    }

    @Test
     void testPlayMimicsLastOpponentActionWhenNoBetrayal() {
        game.playTurn(Choice.COOPERATE, opponent);
        game.playTurn(Choice.COOPERATE, strategyPlayernumber);

        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "NaivePassificator should mimic the last opponent action when the last action was cooperate.");

        game.playTurn(Choice.COOPERATE, strategyPlayernumber);
        game.playTurn(Choice.BETRAY, opponent);

        action = strategy.play(game, opponent);
        assertEquals(Choice.BETRAY, action, "NaivePassificator should mimic the last opponent action when opponent betrays.");
    }
}

