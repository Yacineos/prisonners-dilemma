// SingletonGameWebSocket.ts
import {server_url} from "../models/Server";
import {GameWebSocketsGateway} from "./GameWebSocketsGateway";

class SingletonGameWebSocket extends GameWebSocketsGateway {
  private static instance: SingletonGameWebSocket;

  private constructor() {
    super(server_url);
  }

  public static getInstance(): SingletonGameWebSocket {
    if (!SingletonGameWebSocket.instance) {
      SingletonGameWebSocket.instance = new SingletonGameWebSocket();
      SingletonGameWebSocket.instance.connect();
    }
    return SingletonGameWebSocket.instance;
  }
}

const gateway = SingletonGameWebSocket.getInstance();
export default gateway;