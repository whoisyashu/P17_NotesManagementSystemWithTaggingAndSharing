import { useEffect } from "react";
import { createNoteClient } from "../services/websocket";

export function useNoteCollaboration(noteId, onMessage) {
  useEffect(() => {
    if (!noteId) {
      return undefined;
    }

    const client = createNoteClient(noteId, onMessage);
    return () => client.deactivate();
  }, [noteId, onMessage]);
}