"use client";

import gateway from "@/app/core/adapters/SingletonGameWebSocket";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function Page() {
    const router = useRouter();
    const [gameId, setGameId] = useState<string>("");
    const [isGameFull, setIsGameFull] = useState<boolean>(false);

    // Initial setup and websocket connection check
    useEffect(() => {
        if (!gateway.isSocketOpen()) {
            gateway.connect();
        }

        if (!gateway.getGameId()) {
            try {
                gateway.createGame(10);
            } catch (error) {
                console.error("Error creating game:", error);
            }
        }
    }, []);

    // Poll for game state
    useEffect(() => {
        const interval = setInterval(() => {
            const currentGameId = gateway.getGameId();
            const gameFull = gateway.getGameFull();

            setGameId(currentGameId);
            setIsGameFull(gameFull);

            if (gameFull) {
                clearInterval(interval);
                router.push("/multi");
            }
        }, 1000);

        return () => clearInterval(interval);
    }, [router]);

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
            <div className="w-full max-w-md bg-white shadow-lg rounded-lg p-6 text-center">
                <p className="text-sm text-gray-500 mb-4">
                    {gameId ? "Game ID:" : "Creating game..."}
                </p>
                <h1 className="text-lg font-semibold text-gray-800">
                    {gameId || "Generating game..."}
                </h1>

                <div className="mt-8">
                    <p className="text-xl text-gray-700 font-bold mb-2">
                        {isGameFull
                            ? "Game is full!"
                            : "Waiting for Player 2 to join"}
                    </p>
                    {!isGameFull && (
                        <p className="text-sm text-gray-500">
                            Share the Game ID with Player 2 to join.
                        </p>
                    )}
                </div>
            </div>
        </div>
    );
}
