"use client";

import gateway from "@/app/core/adapters/SingletonGameWebSocket";
import { Action } from "@/app/core/models/Game";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function Page() {
    const router = useRouter();
    const [playerOne] = useState(gateway.getIsPlayerOne());
    const [tSummary, setTurnSummary] = useState<{
        playerOneAction: Action | null;
        playerTwoAction: Action | null;
    }>({
        playerOneAction: null,
        playerTwoAction: null,
    });
    const [opponentPlayed, setOpponentPlayed] = useState(false);
    const [opponentChoice, setOpponentChoice] = useState<Action | null>(null);

    useEffect(() => {
        if (!gateway.isSocketOpen()) {
            gateway.connect();
        }
    }, []);

    useEffect(() => {
        const checkTurnSummary = () => {
            const summary = gateway.getTurnSummary();

            setTurnSummary(summary);

            const hasPlayed = playerOne
                ? summary.playerTwoAction !== null
                : summary.playerOneAction !== null;
            const choice = playerOne
                ? summary.playerTwoAction
                : summary.playerOneAction;

            setOpponentPlayed(hasPlayed);
            setOpponentChoice(choice);
            if(gateway.isGameEnded()){
              router.push("/recap/game");
            }
            if (summary.playerOneAction !== null && summary.playerTwoAction !== null) {
              router.push("/multi");
            }
        };

        // Initial check
        checkTurnSummary();

        // Set up polling
        const interval = setInterval(checkTurnSummary, 1000);

        return () => clearInterval(interval);
    }, [playerOne, router]);

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 p-4">
            <div className="w-full max-w-lg text-center bg-white shadow-lg rounded-lg p-6">
                <div className="mb-4 text-sm text-gray-600">
                    You are:{" "}
                    <span className="font-semibold text-gray-800">
                        {playerOne ? "Player 1" : "Player 2"}
                    </span>
                </div>

                <h1 className="text-xl font-bold text-gray-800 mb-6">
                    {!opponentPlayed
                        ? "Waiting for opponent action"
                        : "Opponent has played"}
                </h1>

                {opponentPlayed ? (
                    <div className="text-sm text-gray-700">
                        <p className="mb-2">Opponent has played</p>
                        <p className="font-semibold">
                            Opponent choice: {opponentChoice}
                        </p>
                    </div>
                ) : (
                    <p className="text-sm text-gray-500">
                        Opponent has not played yet
                    </p>
                )}

                <div className="mt-6 text-xs text-gray-500">
                    Game ID:{" "}
                    <span className="font-mono text-gray-700">
                        {gateway.getGameId()}
                    </span>
                </div>
            </div>
        </div>
    );
}
