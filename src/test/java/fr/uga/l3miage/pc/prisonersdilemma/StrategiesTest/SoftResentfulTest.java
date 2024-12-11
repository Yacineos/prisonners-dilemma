package fr.uga.l3miage.pc.prisonersdilemma.StrategiesTest;

import fr.uga.l3miage.pc.prisonersdilemma.Utils;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import contract.*;
import fr.uga.l3miage.pc.prisonersdilemma.strategies.SoftResentful;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class SoftResentfulTest {
    private SoftResentful strategy;
    private Game game;
    private PlayerNumber opponent;

    @BeforeEach
     void setup() {
        game = new Game(15, null);
        opponent = PlayerNumber.PLAYER_ONE;
        strategy = new SoftResentful();
    }

    @Test
     void testInitialCooperation() {
        Action action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "SoftResentful should cooperate on the first move.");
    }

    @Test
     void testCooperationIfOpponentCooperates() {
        game.playTurn(Choice.COOPERATE, opponent);
        Action action = strategy.play(game, opponent);
        Choice choice = Utils.convertActionToChoice(action);
        game.playTurn(choice, PlayerNumber.PLAYER_TWO);

        action = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action, "SoftResentful should cooperate if the opponent cooperated.");
    }

    @Test
     void testPunishmentSequenceTriggered() {
        game.playTurn(Choice.BETRAY, opponent);

        Action action = strategy.play(game, opponent);
        Choice choice = Utils.convertActionToChoice(action);
        game.playTurn(choice, PlayerNumber.PLAYER_TWO);

        for (int i = 0; i < 4; i++) {
            Action action2 = strategy.play(game, opponent);
            assertEquals(Choice.BETRAY, action2, "SoftResentful should betray during the punishment sequence.");
            game.playTurn(Utils.convertActionToChoice(action2), PlayerNumber.PLAYER_TWO);
        }

        for (int i = 0; i < 2; i++) {
            Action action3 = strategy.play(game, opponent);
            assertEquals(Choice.COOPERATE, action3, "SoftResentful should cooperate after 4 betrayals.");
            game.playTurn(Utils.convertActionToChoice(action3), PlayerNumber.PLAYER_TWO);
        }

        game.playTurn(Choice.COOPERATE, opponent);
        Action action4 = strategy.play(game, opponent);
        assertEquals(Choice.COOPERATE, action4, "SoftResentful should return to cooperation after punishment sequence.");
    }

    @Test
     void testMultiplePunishments() {
        game.playTurn(Choice.BETRAY, opponent);
        game.playTurn(Utils.convertActionToChoice(strategy.play(game, opponent)), PlayerNumber.PLAYER_TWO);

        for (int i = 0; i < 6; i++) {
            game.playTurn(Choice.COOPERATE, opponent);
            game.playTurn(Utils.convertActionToChoice(strategy.play(game, opponent)), PlayerNumber.PLAYER_TWO);
        }

        game.playTurn(Choice.BETRAY, opponent);
        game.playTurn(Utils.convertActionToChoice(strategy.play(game, opponent)), PlayerNumber.PLAYER_TWO);

        for (int i = 0; i < 4; i++) {
            game.playTurn(Choice.BETRAY, opponent);
            Choice action = Utils.convertActionToChoice(strategy.play(game, opponent));
            assertEquals(Choice.BETRAY, action, "SoftResentful should betray again during the second punishment sequence.");
            game.playTurn(action, PlayerNumber.PLAYER_TWO);
        }

        for (int i = 0; i < 2; i++) {
            game.playTurn(Choice.BETRAY, opponent);
            Choice action = Utils.convertActionToChoice(strategy.play(game, opponent));
            assertEquals(Choice.COOPERATE, action, "SoftResentful should cooperate after the second punishment sequence.");
            game.playTurn(action, PlayerNumber.PLAYER_TWO);
        }
    }

    @Test
     void testNoPunishmentIfOpponentCooperates() {
        for (int i = 0; i < 5; i++) {
            game.playTurn(Choice.COOPERATE, opponent);
            Choice action = Utils.convertActionToChoice(strategy.play(game, opponent));
            assertEquals(Choice.COOPERATE, action, "SoftResentful should continue cooperating if opponent cooperates.");
            game.playTurn(action, PlayerNumber.PLAYER_TWO);
        }
    }

    @Test
     void testPunishmentNotRestartedDuringSequence() {
        game.playTurn(Choice.BETRAY, opponent);
        game.playTurn(Utils.convertActionToChoice(strategy.play(game, opponent)), PlayerNumber.PLAYER_TWO);

        for (int i = 0; i < 3; i++) {
            strategy.play(game, opponent);
        }

        game.playTurn(Choice.BETRAY, opponent);
        Action action = strategy.play(game, opponent);

        assertEquals(Choice.BETRAY, action, "SoftResentful should not restart punishment sequence during punishment.");
    }
}
