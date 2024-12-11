package fr.uga.l3miage.pc.prisonersdilemma.StrategiesTest;

import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import contract.*;
import fr.uga.l3miage.pc.prisonersdilemma.strategies.Pavlov;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class PavlovTest {
    private Pavlov strategy;
    private Game game;
    private PlayerNumber opponent;
    private PlayerNumber strategyPlayernumber;

    @BeforeEach
     void setup() {
        game = new Game(5, null);
        strategy = new Pavlov();
        opponent = PlayerNumber.PLAYER_ONE;
        strategyPlayernumber = PlayerNumber.PLAYER_TWO;
    }

    @Test
    void testEmptyHistory(){
        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action);
    }

    @Test
    void testSufficientScoreBetray(){
        game.playTurn(Choice.COOPERATE,opponent);
        game.playTurn(Choice.BETRAY, strategyPlayernumber);
        Action action = strategy.play(game, opponent);
        assertEquals( Choice.BETRAY, action);
    }

    @Test
    void testSufficientScoreCooperate(){
        game.playTurn(Choice.COOPERATE,opponent);
        game.playTurn(Choice.COOPERATE, strategyPlayernumber);
        Action action = strategy.play(game, opponent);
        assertEquals( Choice.COOPERATE,action);
    }

    @Test
    void testInsufficientSufficientScoreCooperate(){
        game.playTurn(Choice.BETRAY,opponent);
        game.playTurn(Choice.COOPERATE, strategyPlayernumber);
        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action);
    }

    @Test
    void testInsufficientSufficientScoreBetray(){
        game.playTurn(Choice.BETRAY,opponent);
        game.playTurn(Choice.BETRAY, strategyPlayernumber);
        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action);
    }


    @Test
    void testMultipleInstances(){
        game.playTurn(Choice.COOPERATE,opponent);
        game.playTurn(Choice.COOPERATE, strategyPlayernumber);
        Action action = strategy.play(game, opponent);
        assertEquals( Choice.COOPERATE,action);
        game.playTurn(Choice.COOPERATE,opponent);
        game.playTurn(Choice.BETRAY, strategyPlayernumber);
        Action actionTurn1 = strategy.play(game, opponent);
        assertEquals( Choice.BETRAY,actionTurn1);
        game.playTurn(Choice.BETRAY,opponent);
        game.playTurn(Choice.BETRAY, strategyPlayernumber);
        Action actionTurn2 = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, actionTurn2);
    }
}
