 "use client";

import { useState } from "react";
import gateway from "../core/adapters/SingletonGameWebSocket";
import Link from "next/link";

export default function JoinGame() {
    const gameService = gateway;
    const [gameId, setGameId] = useState<string>("");

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 p-4">
            <div className="w-full max-w-sm">
                <input
                    type="text"
                    value={gameId}
                    onChange={(e) => setGameId(e.target.value)}
                    placeholder="Enter Game ID"
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent text-gray-700"
                />
                <div
                    className="mt-4"
                    onClick={() => gameService.joinGame(gameId)}
                >
                    <Link
                        href="/multi"
                        className="inline-block w-full px-4 py-2 text-white bg-blue-500 rounded-lg shadow-lg text-center hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:ring-offset-2"
                    >
                        JOIN
                    </Link>
                </div>
            </div>
        </div>
    );
}
