export enum Action {
    COOPERATE = "COOPERATE",
    BETRAY = "BETRAY",
} 

export type TurnSummary = {
    playerOneAction: Action | null,
    playerTwoAction: Action | null,
}

export const createGameStartingConvention = "CREATE_GAME";
export const joinGameStartingConvention = "JOIN_GAME";
export const actionStartingConvention = "ACTION";
export const gameCreationResponseStartingConvention = "GAME_ID";