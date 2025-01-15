"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import gateway from "../core/adapters/SingletonGameWebSocket";
import { useEffect, useState } from "react";

export default function Page() {
    const router = useRouter();
    useEffect(() => {
        // Reset turn summary when mounting the multi page
        gateway.resetTurnSummary();
    }, []);

    useEffect(() => {  
      const checkGameStatus = () => {
        if(gateway.isGameEnded()){
          router.push("/recap/game");
        }
      }

      // Check status periodically
      const interval = setInterval(checkGameStatus, 1000);

      return () => clearInterval(interval);
    }, []);

    const handleAction = (action: 'betray' | 'coop') => {
        if (action === 'betray') {
            gateway.betray();
        } else {
            gateway.coop();
        }
        router.push('/waiting/action');
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 p-4">
            <div className="w-full max-w-md text-center">
                <p className="text-lg text-gray-700 mb-4">Game ID: {gateway.getGameId()}</p>
                <p className="text-md text-gray-600 mb-2">Partie Multi</p>
                <div className="flex justify-center gap-4 mb-8">
                    <button
                        onClick={() => handleAction('betray')}
                        className="px-6 py-3 bg-red-500 text-white rounded-lg shadow-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-2"
                    >
                        BETRAY
                    </button>
                    <button
                        onClick={() => handleAction('coop')}
                        className="px-6 py-3 bg-green-500 text-white rounded-lg shadow-lg hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-green-400 focus:ring-offset-2"
                    >
                        COOP
                    </button>
                </div>
                <Link
                    href="/"
                    className="inline-block px-4 py-2 text-white bg-blue-500 rounded-lg shadow-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:ring-offset-2"
                >
                    Return to Home
                </Link>
            </div>
        </div>
    );
}
