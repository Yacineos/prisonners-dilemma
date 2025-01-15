"use client";

import Link from "next/link";
import gateway from "./core/adapters/SingletonGameWebSocket";

export default function Page() {
    const gameService = gateway;
    console.log(gameService);

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 p-4">
            <div className="w-full max-w-sm space-y-4">
                <Link
                    href="/join-game"
                    className="block w-full px-4 py-2 text-center text-white bg-blue-500 rounded-lg shadow-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:ring-offset-2"
                >
                    Rejoindre Partie
                </Link>
                <Link
                    href="/game-setup"
                    className="block w-full px-4 py-2 text-center text-white bg-blue-500 rounded-lg shadow-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:ring-offset-2"
                >
                    Créer Partie
                </Link>
            </div>
        </div>
    );
}
