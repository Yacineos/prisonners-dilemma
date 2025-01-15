package fr.uga.l3miage.pc.prisonersdilemma;

import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import fr.uga.l3miage.pc.prisonersdilemma.enums.PlayerNumber;
import fr.uga.l3miage.pc.prisonersdilemma.game.Game;
import fr.uga.l3miage.pc.prisonersdilemma.services.GameService;
import fr.uga.l3miage.pc.prisonersdilemma.strategies.Strategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class GameServiceTest {

	private GameService gameService;
	private WebSocketSession mockSession;

	@BeforeEach
	void setUp() {
		gameService = GameService.getInstance();
		mockSession = mock(WebSocketSession.class);
	}

	@Test
	void createGame_ShouldThrowAnException_notANumber() throws IOException {
		assertThrows(IOException.class, () -> gameService.createGame(mockSession,"CREATE_GAME:test"));
	}

	@Test
	void createGame_payload_should_start_with_CREATE_GAME() throws IOException {
		assertThrows(IOException.class, () -> gameService.createGame(mockSession,"test"));
	}

	@Test
	void createGame_ShouldAddSessionToNewGame() throws IOException {
		String gameId = gameService.createGame(mockSession, "CREATE_GAME:10");
		Set<WebSocketSession> playersInGame = gameService.getPlayers(gameId);

		assertNotNull(playersInGame, "Players set should be initialized for the new game");
		assertEquals(1, playersInGame.size(), "There should be exactly one player in the new game");
		assertTrue(playersInGame.contains(mockSession), "The player's session should be added to the game");
	}

	@Test
	void createGame_ShouldCreateUniqueGameIds() throws IOException {
		WebSocketSession mockSession2 = mock(WebSocketSession.class);

		String gameId1 = gameService.createGame(mockSession, "CREATE_GAME:10");
		String gameId2 = gameService.createGame(mockSession2, "CREATE_GAME:10");

		assertNotNull(gameId1, "Game ID 1 should not be null");
		assertNotNull(gameId2, "Game ID 2 should not be null");
		assertNotEquals(gameId1, gameId2, "Each game should have a unique ID");
	}

	@Test
	void testSendGameIdToPlayer() throws IOException {
		String gameId = "12345";
		String expectedMessage = "GAME_ID:" + gameId;

		gameService.sendGameIdToPlayer(mockSession, gameId);

		ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);

		verify(mockSession, times(1)).sendMessage(messageCaptor.capture());

		TextMessage sentMessage = messageCaptor.getValue();
		assertEquals(expectedMessage, sentMessage.getPayload());
	}

	@Test
	void testSuccessfullyJoiningGame() throws IOException {
		String gameId = gameService.createGame(mockSession, "CREATE_GAME:10");
		WebSocketSession mockSession2 = mock(WebSocketSession.class);
		gameService.joinGame(mockSession2, "JOIN_GAME:" + gameId);
		assertTrue(gameService.getPlayers(gameId).contains(mockSession2));
	}

	@Test
	void testJoiningFullGame() throws IOException {
		String gameId = gameService.createGame(mockSession, "CREATE_GAME:10");
		WebSocketSession mockSession2 = mock(WebSocketSession.class);
		WebSocketSession mockSession3 = mock(WebSocketSession.class);
		gameService.joinGame(mockSession2, "JOIN_GAME:" + gameId);
		assertThrows(IllegalArgumentException.class, () -> gameService.joinGame(mockSession3, "JOIN_GAME:" + gameId));
	}

	@Test
	void testJoiningNonExistentGame() throws IOException {
		WebSocketSession mockSession2 = mock(WebSocketSession.class);

		assertThrows(IllegalArgumentException.class, () -> gameService.joinGame(mockSession2, "JOIN_GAME:12345"));
	}

	@Test
	void testPlayerOneReceivesCorrectMessageWhenPlayerTwoJoins() throws IOException {
		WebSocketSession mockSession1 = mock(WebSocketSession.class);
		WebSocketSession mockSession2 = mock(WebSocketSession.class);

		String gameId = gameService.createGame(mockSession1, "CREATE_GAME:10");
		gameService.joinGame(mockSession2, "JOIN_GAME:" + gameId);

		ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);

		verify(mockSession1, times(2)).sendMessage(messageCaptor.capture());

		List<TextMessage> capturedMessages = messageCaptor.getAllValues();

		String expectedMessage = "PLAYER_TWO_JOINED";
		assertEquals(expectedMessage, capturedMessages.get(1).getPayload());
	}

	@Test
	void testGetPlayersShouldThrowExceptionIfGameNotFound() {
		assertThrows(IllegalArgumentException.class, () -> gameService.getPlayers("12345"));
	}

	@Test
	void testGetPlayersShouldReturnPlayers() throws IOException {
		String gameId = gameService.createGame(mockSession, "CREATE_GAME:10");
		WebSocketSession mockSession2 = mock(WebSocketSession.class);
		gameService.joinGame(mockSession2, "JOIN_GAME:" + gameId);

		Set<WebSocketSession> players = gameService.getPlayers(gameId);

		assertNotNull(players, "Players set should not be null");
		assertEquals(2, players.size(), "Players set should contain one player");
		assertTrue(players.contains(mockSession), "Players set should contain the player");
		assertTrue(players.contains(mockSession2), "Players set should contain the player");
	}

		@Test
		void testExtractPlayerActionFromPayloadWorking() {
			String payload = "ACTION:12345:COOPERATE";
			String payload2 = "ACTION:12345:BETRAY";

			String action = gameService.extractPlayerActionFromPayload(payload);
			String action2 = gameService.extractPlayerActionFromPayload(payload2);

			assertEquals("COOPERATE", action);
			assertEquals("BETRAY", action2);
		}

		@Test
		void testExtractPlayerActionFromPayloadNotWorking() {
			String payload = "ACTION:12345:COOPERATE";
			String payload2 = "ACTION:12345:BETRAY";

			String action = gameService.extractPlayerActionFromPayload(payload);
			String action2 = gameService.extractPlayerActionFromPayload(payload2);

			assertNotEquals("BETRAY", action);
			assertNotEquals("COOPERATE", action2);
		}

		@Test
		void testGetPlayerNumber(){
			WebSocketSession mockPlayerOne = mock(WebSocketSession.class);
			Game game = new Game(10, mockPlayerOne);

			PlayerNumber result = gameService.getPlayerNumber(mockPlayerOne, game);

			assertEquals(PlayerNumber.PLAYER_ONE, result, "Should return PLAYER_ONE for playerOne session");
		}

		@Test
		void testGetPlayerNumberPlayerTwo(){
			WebSocketSession mockPlayerOne = mock(WebSocketSession.class);
			WebSocketSession mockPlayerTwo = mock(WebSocketSession.class);
			Game game = new Game(10, mockPlayerOne);
			game.addSecondPlayerToTheGame(mockPlayerTwo);

			PlayerNumber result = gameService.getPlayerNumber(mockPlayerTwo, game);

			assertEquals(PlayerNumber.PLAYER_TWO, result, "Should return PLAYER_TWO for playerTwo session");
		}

		@Test
		void testGetPlayerNumberPlayerNotFound(){
			WebSocketSession mockPlayerOne = mock(WebSocketSession.class);
			WebSocketSession mockPlayerTwo = mock(WebSocketSession.class);
			WebSocketSession mockPlayerThree = mock(WebSocketSession.class);
			Game game = new Game(10, mockPlayerOne);
			game.addSecondPlayerToTheGame(mockPlayerTwo);

			assertThrows(IllegalArgumentException.class, () -> gameService.getPlayerNumber(mockPlayerThree, game));
		}

		@Test
		void actionShouldThrowExceptionIfGameNotFound() {
			assertThrows(IllegalArgumentException.class, () -> gameService.action(mockSession, "ACTION:12345:COOPERATE"));
		}

		@Test
		void actionShouldThrowExceptionIfPlayerNotInGame() throws IOException {
			String gameId = gameService.createGame(mockSession, "CREATE_GAME:10");
			WebSocketSession mockSession2 = mock(WebSocketSession.class);
			gameService.joinGame(mockSession2, "JOIN_GAME:" + gameId);
			WebSocketSession mockSession3 = mock(WebSocketSession.class);

			assertThrows(IllegalArgumentException.class, () -> gameService.action(mockSession3, "ACTION:" + gameId + ":COOPERATE"));
		}

		@Test
		void actionShouldThrowExceptionIfPlayerActionNotValid() throws IOException {
			String gameId = gameService.createGame(mockSession, "CREATE_GAME:10");

			assertThrows(IllegalArgumentException.class, () -> gameService.action(mockSession, "ACTION:" + gameId + ":INVALID_ACTION"));
		}

		@Test
		void testActionGameNotExist(){
			assertThrows(IllegalArgumentException.class, () -> gameService.getPlayerAction("12345", mockSession,0));
		}

		@Test
		void testActionPlayerNotExist() throws IOException {
			String gameId = gameService.createGame(mockSession, "CREATE_GAME:10");
			assertThrows(IllegalArgumentException.class, () -> gameService.getPlayerAction(gameId, mock(WebSocketSession.class),0));
		}

		@Test
		void testPlayerOneActionCooperate() throws IOException {

			WebSocketSession mockSession1 = mock(WebSocketSession.class);
			WebSocketSession mockSession2 = mock(WebSocketSession.class);
			String gameId = gameService.createGame(mockSession1, "CREATE_GAME:50000");
			gameService.joinGame(mockSession2, "JOIN_GAME:" + gameId);
			gameService.action(mockSession1, "ACTION:" + gameId + ":COOPERATE");

			Action playerOneAction = gameService.getPlayerAction(gameId, mockSession1,0);
			assertEquals(Action.COOPERATE, playerOneAction, "Player one's action should be COOPERATE.");
		}

		@Test
		void testPlayerTwoActionBetray() throws IOException {
			WebSocketSession mockSession1 = mock(WebSocketSession.class);
			WebSocketSession mockSession2 = mock(WebSocketSession.class);
			String gameId2 = gameService.createGame(mockSession1, "CREATE_GAME:50000");
			gameService.joinGame(mockSession2, "JOIN_GAME:" + gameId2);
			gameService.action(mockSession1, "ACTION:" + gameId2 + ":COOPERATE");
			gameService.action(mockSession2, "ACTION:" + gameId2 + ":BETRAY");

			Action playerTwoAction = gameService.getPlayerAction(gameId2, mockSession2,0);
			assertEquals(Action.BETRAY, playerTwoAction, "Player two's action should be BETRAY.");
		}

		@Test
		void testBothPlayersPerformActions() throws IOException {

			WebSocketSession mockSession = mock(WebSocketSession.class);
			WebSocketSession mockSession2 = mock(WebSocketSession.class);

			String gameId2 = gameService.createGame(mockSession, "CREATE_GAME:50000");
			gameService.joinGame(mockSession2, "JOIN_GAME:" + gameId2);

			gameService.action(mockSession,"ACTION:" + gameId2 + ":COOPERATE");
			gameService.action(mockSession2, "ACTION:" + gameId2 + ":BETRAY");

			Action playerOneAction = gameService.getPlayerAction(gameId2, mockSession,0);
			Action playerTwoAction = gameService.getPlayerAction(gameId2, mockSession2,0);

			assertEquals(Action.COOPERATE, playerOneAction, "Player one's action should be COOPERATE.");
			assertEquals(Action.BETRAY, playerTwoAction, "Player two's action should be BETRAY.");
		}

		@Test
		void testActionCallsSendTurnSummaryToBothPlayers() throws IOException {
			WebSocketSession mockSession1 = mock(WebSocketSession.class);
			WebSocketSession mockSession2 = mock(WebSocketSession.class);
			GameService gameService1 = Mockito.spy(gameService);
			String gameId = gameService1.createGame(mockSession1, "CREATE_GAME:50000");
			gameService1.joinGame(mockSession2, "JOIN_GAME:" + gameId);
			gameService1.action(mockSession1, "ACTION:" + gameId + ":COOPERATE");
			gameService1.action(mockSession2, "ACTION:" + gameId + ":BETRAY");

			verify(gameService1, times(1)).sendTurnSummaryToBothPlayers(gameId,0);
		}

		@Test
		void sendTurnSummaryToBothPlayers() throws IOException {
			WebSocketSession mockSession1 = mock(WebSocketSession.class);
			WebSocketSession mockSession2 = mock(WebSocketSession.class);
			String gameId = gameService.createGame(mockSession1, "CREATE_GAME:50000");
			gameService.joinGame(mockSession2, "JOIN_GAME:" + gameId);
			gameService.action(mockSession1, "ACTION:" + gameId + ":COOPERATE");
			gameService.action(mockSession2, "ACTION:" + gameId + ":BETRAY");
			String expectedMessage = "TURN_SUMMARY:COOPERATE:BETRAY";

			ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);

			verify(mockSession1, times(3)).sendMessage(messageCaptor.capture());
			verify(mockSession2, times(1)).sendMessage(messageCaptor.capture());

			TextMessage sentMessage = messageCaptor.getValue();
			assertEquals(expectedMessage, sentMessage.getPayload());

		}


	@Test
	void testStrategyJoinsAndPlaysGame() throws IOException {
		String gameId = gameService.createGame(mockSession, "CREATE_GAME:10");
		WebSocketSession mockSession2 = mock(WebSocketSession.class);
		gameService.joinGame(mockSession2, "JOIN_GAME:" + gameId);
		gameService.action(mockSession, "ACTION:" + gameId + ":COOPERATE");
		gameService.action(mockSession2, "ACTION:" + gameId + ":COOPERATE");
		gameService.leaveGames(mockSession2);
		gameService.action(mockSession, "ACTION:" + gameId + ":COOPERATE");

		Game game = gameService.getGameByPlayerSession(mockSession);
		Strategy strategy = game.getStrategy();
		assertNotNull(strategy);

		Action strategyAction = game.getTurnThatJustEnded().getPlayerTwoAction();
		assertNotEquals(Action.NONE, strategyAction);

		gameService.action(mockSession, "ACTION:" + gameId + ":BETRAY");

		Strategy strategy2 = game.getStrategy();
		assertEquals(strategy,strategy2);

		Action strategyAction2 = game.getTurnThatJustEnded().getPlayerTwoAction();
		assertNotEquals(Action.NONE, strategyAction2);

	}

	@Test
	void testGetGameByPlayerSessionError(){
		WebSocketSession mockSession2 = mock(WebSocketSession.class);

		NoSuchElementException exception = assertThrows(
				NoSuchElementException.class,
				() -> gameService.getGameByPlayerSession(mockSession2),
				"Expected getGameByPlayerSession to throw, but it didn't"
		);

		assertEquals("No game found for the given player session", exception.getMessage());
	}

	@Test
	void sendGameEndNotification_ShouldSendCorrectScores() throws IOException {
		// Create a game with 2 turns
		String gameId = gameService.createGame(mockSession, "CREATE_GAME:2");
		WebSocketSession mockSessionTwo = mock(WebSocketSession.class);
		gameService.joinGame(mockSessionTwo, "JOIN_GAME:" + gameId);

		// Mock message captures
		ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);

		// Simulate a full game where both players cooperate
		gameService.action(mockSession, "ACTION:" + gameId + ":COOPERATE");
		gameService.action(mockSessionTwo, "ACTION:" + gameId + ":COOPERATE");
		gameService.action(mockSession, "ACTION:" + gameId + ":COOPERATE");
		gameService.action(mockSessionTwo, "ACTION:" + gameId + ":COOPERATE");

		// Verify that messages were sent to both players
		verify(mockSession, atLeastOnce()).sendMessage(messageCaptor.capture());
		verify(mockSessionTwo, atLeastOnce()).sendMessage(any(TextMessage.class));

		// Get all messages sent to player one
		List<TextMessage> sentMessages = messageCaptor.getAllValues();
		Optional<String> gameEndMessage = sentMessages.stream()
				.map(TextMessage::getPayload)
				.filter(msg -> msg.startsWith("GAME_END:"))
				.findFirst();

		assertTrue(gameEndMessage.isPresent(), "Game end message should be sent");
		assertEquals("GAME_END:6:6", gameEndMessage.get(),
				"Both players should have 6 points (3 points per cooperative round)");
	}

	@Test
	void gameEndNotification_ShouldCalculateCorrectScoresForMixedPlay() throws IOException {
		// Create a game with 2 turns
		String gameId = gameService.createGame(mockSession, "CREATE_GAME:2");
		WebSocketSession mockSessionTwo = mock(WebSocketSession.class);
		gameService.joinGame(mockSessionTwo, "JOIN_GAME:" + gameId);

		// First turn: Player 1 cooperates, Player 2 betrays
		gameService.action(mockSession, "ACTION:" + gameId + ":COOPERATE");
		gameService.action(mockSessionTwo, "ACTION:" + gameId + ":BETRAY");

		// Second turn: Both cooperate
		gameService.action(mockSession, "ACTION:" + gameId + ":COOPERATE");
		gameService.action(mockSessionTwo, "ACTION:" + gameId + ":COOPERATE");

		// Capture messages
		ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);
		verify(mockSession, atLeastOnce()).sendMessage(messageCaptor.capture());

		// Get all messages sent to player one
		List<TextMessage> sentMessages = messageCaptor.getAllValues();
		Optional<String> gameEndMessage = sentMessages.stream()
				.map(TextMessage::getPayload)
				.filter(msg -> msg.startsWith("GAME_END:"))
				.findFirst();

		assertTrue(gameEndMessage.isPresent(), "Game end message should be sent");
		assertEquals("GAME_END:3:8", gameEndMessage.get(),
				"Player 1 should have 3 points (0 + 3), Player 2 should have 8 points (5 + 3)");
	}

	@Test
	void sendGameEndNotification_ShouldHandleIOException() throws IOException {
		// Create a game with 1 turn
		String gameId = gameService.createGame(mockSession, "CREATE_GAME:1");
		WebSocketSession mockSessionTwo = mock(WebSocketSession.class);
		gameService.joinGame(mockSessionTwo, "JOIN_GAME:" + gameId);

		// Mock IOException when sending message
		doThrow(new IOException()).when(mockSession).sendMessage(any(TextMessage.class));

		// Play the game
		assertDoesNotThrow(() -> {
			gameService.action(mockSession, "ACTION:" + gameId + ":COOPERATE");
			gameService.action(mockSessionTwo, "ACTION:" + gameId + ":COOPERATE");
		});

		// Verify that at least one attempt was made to send a message
		verify(mockSession, atLeastOnce()).sendMessage(any(TextMessage.class));
	}

}
