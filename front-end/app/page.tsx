'use client';

import Link from "next/link";
import gateway from "./core/adapters/SingletonGameWebSocket";

export default function Page() {
  
  const gameService = gateway ;
  console.log(gameService);
  
  return (
    <div className="flex flex-col my-[25%] text-center">
      <Link href="/join-game" className="bg-gray-400 mb-5 shadow-md">rejoindre partie</Link>
      <Link href="/game-setup" className="bg-gray-400 shadow-md">Créer partie</Link>
    </div>
  );
}
