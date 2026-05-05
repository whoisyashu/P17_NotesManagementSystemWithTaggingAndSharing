import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

export function createNoteClient(noteId, onMessage) {
  const client = new Client({
    webSocketFactory: () => new SockJS("/ws"),
    reconnectDelay: 4000,
    onConnect: () => {
      client.subscribe(`/topic/notes/${noteId}`, (message) => {
        const payload = JSON.parse(message.body);
        onMessage(payload);
      });
    }
  });

  client.activate();
  return client;
}