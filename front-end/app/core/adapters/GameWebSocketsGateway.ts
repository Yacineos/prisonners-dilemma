import { Action, createGameStartingConvention, TurnSummary } from "../models/Game";
import { IGameGateway } from "../ports/IGameGateway";

export class GameWebSocketsGateway implements IGameGateway {
    
    private socket: WebSocket | null = null;
    private gameId: string = "";
    private gameFull: boolean = false;
    private isPlayerOne: boolean = false;
    private turnSummary : TurnSummary = {
        playerOneAction: null,
        playerTwoAction: null
    }
    private gameEnded: boolean = false;
    private finalScores: { playerOne: number, playerTwo: number } | null = null;


    constructor(private serverUrl: string) {}

    public isGameEnded(): boolean {
        return this.gameEnded;
    }
    
    public getFinalScores() {
        return this.finalScores;
    }

    public getOtherPlayerChoice() {
        return this.isPlayerOne ? this.turnSummary.playerTwoAction : this.turnSummary.playerOneAction;
    }

    public getGameId(): string {
        return this.gameId;
    }

    public getGameFull(): boolean {
        return this.gameFull;
    }

    public getIsPlayerOne(): boolean {
        return this.isPlayerOne;
    }

    public getTurnSummary(): TurnSummary {
        return this.turnSummary;
    }

    public getPlayerChoice() {
        return this.isPlayerOne ? this.turnSummary.playerOneAction : this.turnSummary.playerTwoAction;
    }

    // In GameWebSocketsGateway.ts
public resetTurnSummary(): void {
    this.turnSummary = {
        playerOneAction: null,
        playerTwoAction: null
    };
}

    /**
     * Connects the player to the WebSocket server.
     */
    public connect(): void {
        if (this.socket) {
            console.warn("WebSocket is already connected.");
            return;
        }

        this.socket = new WebSocket(this.serverUrl);

        this.socket.onopen = () => {
            console.log("WebSocket connection established.");
        };

        this.socket.onmessage = (event) => {
            this.handleMessage(event.data);
        };

        this.socket.onerror = (error) => {
            console.error("WebSocket error:", error);
        };

        this.socket.onclose = () => {
            console.log("WebSocket connection closed.");
            this.socket = null;
        };
    }

    /**
     * Creates a new game for the current player.
     * @param turnsNumber The number of turns for the game.
     * @returns The game ID (empty if not received yet).
     */
    public createGame(turnsNumber: number): string {
        
        if (turnsNumber < 1) {
            throw new Error("turnsNumber should be greater than 0.");
        }

        if (!this.isSocketOpen()) {
            throw new Error("WebSocket is not open.");
        }

        this.socket!.send(`${createGameStartingConvention}:${turnsNumber}`);
        console.log("Game creation request sent.");
        this.isPlayerOne = true;
        return this.gameId; // Will be updated when the server responds
    }

    /**
     * Joins an existing game for the current player.
     * @param gameId The ID of the game to join.
     */
    public joinGame(gameId: string): void {
        if (!this.isSocketOpen()) {
            throw new Error("WebSocket is not open.");
        }
        this.gameId = gameId;
        this.isPlayerOne = false; // Make sure this is set for player 2
        this.socket!.send(`JOIN_GAME:${gameId}`);
        this.gameFull = true;
        console.log(`Join game request sent for game ID: ${gameId}`, {
            isPlayerOne: this.isPlayerOne,
            gameId: this.gameId
        });
    }

    /**
     * Sends an action for the current player in the game.
     * @param action The action to perform.
     */
    public sendAction(action: Action): void {
        if (!this.isSocketOpen()) {
            throw new Error("WebSocket is not open.");
        }

        if (!this.gameId) {
            throw new Error("Player is not in a game.");
        }

        this.socket!.send(`ACTION:${this.gameId}:${action}`);
        console.log(`Action sent: ${action}`);
    }

    /**
     * Disconnects the player from the WebSocket server.
     */
    public disconnect(): void {
        if (this.socket) {
            this.socket.close();
            this.socket = null;
        }
    }

    /**
     * Checks if the WebSocket is open.
     */
    public isSocketOpen(): boolean {
        return this.socket !== null && this.socket.readyState === WebSocket.OPEN;
    }

    coop() {
        this.sendAction(Action.COOPERATE);
    }

    betray() {
        this.sendAction(Action.BETRAY);
    }

    /**
     * Handles incoming WebSocket messages.
     * @param message The raw message data.
     */
    private handleMessage(message: string): void {
        console.log("Received message:", message);

        if (message.startsWith("GAME_ID:")) {
            this.gameId = message.split(":")[1];
            console.log(`Game created with ID: ${this.gameId}`);
        } else if (message.startsWith("PLAYER_TWO_JOINED")) {
            this.gameFull = true;
            console.log(`Joined game successfully: ${message.split(":")[1]}`);
        } else if (message.startsWith("TURN_SUMMARY:")) {
            this.updateTurnSummary(message);
        }else if (message.startsWith("GAME_END:")) {
            const [_, playerOneScore, playerTwoScore] = message.split(":");
            this.gameEnded = true;
            this.finalScores = {
                playerOne: parseInt(playerOneScore),
                playerTwo: parseInt(playerTwoScore)
            };
            console.log("Game ended with scores:", this.finalScores);
        } else if (message.startsWith("ERROR:")) {
            console.error("Error from server:", message.split(":")[1]);
        } else {
            console.warn("Unhandled message type:", message);
        }
    }

    private updateTurnSummary(message: string) {
        this.turnSummary.playerOneAction = message.split(":")[1] as unknown as Action;
        this.turnSummary.playerTwoAction = message.split(":")[2] as unknown as Action;
    }
}       