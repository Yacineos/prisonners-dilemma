'use client'

import gateway from "@/app/core/adapters/SingletonGameWebSocket";
import Link from "next/link";
import { useState, useEffect } from "react";

export default function Page() {
    const [gameId, setGameId] = useState<string>(gateway.getGameId());
    const [score, setScore] = useState<{ playerOne: number; playerTwo: number } | null>(null);
    const [isPlayerOne, setIsPlayerOne] = useState<boolean>(gateway.getIsPlayerOne());

    useEffect(() => {
        // Fetch final scores from gateway
        const finalScores = gateway.getFinalScores();
        setScore(finalScores);
    }, []);

    // Determine game outcome
    const outcome = score
        ? isPlayerOne
            ? score.playerOne > score.playerTwo
                ? "You Won!"
                : score.playerOne < score.playerTwo
                ? "You Lost!"
                : "It's a Draw!"
            : score.playerTwo > score.playerOne
            ? "You Won!"
            : score.playerTwo < score.playerOne
            ? "You Lost!"
            : "It's a Draw!"
        : "Loading...";

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
            <div className="w-full max-w-md bg-white shadow-lg rounded-lg p-6 text-center">
                <p className="text-sm text-gray-500 mb-4">Game ID:</p>
                <h1 className="text-lg font-semibold text-gray-800 mb-6">
                    {gameId || "Fetching game details..."}
                </h1>

                <h2 className="text-xl font-bold text-gray-700 mb-4">
                    The game has ended
                </h2>

                <p className="text-2xl font-semibold text-gray-800 mb-4">{outcome}</p>

                {score && (
                    <div className="text-lg font-medium text-gray-600 mb-6">
                        <p>Game Score:</p>
                        <p className="text-2xl font-bold text-gray-900">
                            {score.playerOne} - {score.playerTwo}
                        </p>
                    </div>
                )}

                <Link
                    href="/"
                    className="px-4 py-2 bg-blue-500 text-white rounded shadow hover:bg-blue-600 transition-colors"
                >
                    Return Home
                </Link>
            </div>
        </div>
    );
}
