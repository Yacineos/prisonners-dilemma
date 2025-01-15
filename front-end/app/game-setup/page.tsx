"use client";

import Link from "next/link";
import gateway from "../core/adapters/SingletonGameWebSocket";
import { useState } from "react";

export default function GameSetup() {
    const gameService = gateway;

    const [turnsNumber, setTurnsNumber] = useState<number>(1);
    const handleTurnsChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setTurnsNumber(Number(e.target.value));
    };

    const createGame = () => gameService.createGame(turnsNumber);

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 p-4">
            <div className="w-full max-w-sm">
                <h1 className="text-2xl font-semibold text-gray-700 text-center mb-6">
                    Entrez le nombre de rounds :
                </h1>
                <input
                    type="number"
                    value={turnsNumber}
                    onChange={handleTurnsChange}
                    min={1}
                    placeholder="Number of rounds"
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent text-gray-700 mb-6"
                />
                <div className="space-y-4">
                    {/* <Link
                        href="/solo"
                        className="block w-full px-4 py-2 text-center text-white bg-blue-500 rounded-lg shadow-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:ring-offset-2"
                    >
                        SOLO
                    </Link> */}
                    <div onClick={createGame}>
                        <Link
                            href="/waiting/connection"
                            className="block w-full px-4 py-2 text-center text-white bg-blue-500 rounded-lg shadow-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:ring-offset-2"
                        >
                            MULTI
                        </Link>
                    </div>
                    <Link
                        href="/"
                        className="block w-full px-4 py-2 text-center text-white bg-gray-500 rounded-lg shadow-lg hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-offset-2"
                    >
                        HOME
                    </Link>
                </div>
            </div>
        </div>
    );
}
