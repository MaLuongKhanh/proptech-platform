import { useState, useEffect } from 'react';

type KeyPressHandler = (event: KeyboardEvent) => void;

interface UseKeyPressOptions {
  targetKey: string;
  handler: KeyPressHandler;
  event?: 'keydown' | 'keyup';
  ctrlKey?: boolean;
  shiftKey?: boolean;
  altKey?: boolean;
}

function useKeyPress({
  targetKey,
  handler,
  event = 'keydown',
  ctrlKey = false,
  shiftKey = false,
  altKey = false,
}: UseKeyPressOptions): void {
  useEffect(() => {
    const handleKeyPress = (event: KeyboardEvent) => {
      if (
        event.key.toLowerCase() === targetKey.toLowerCase() &&
        event.ctrlKey === ctrlKey &&
        event.shiftKey === shiftKey &&
        event.altKey === altKey
      ) {
        handler(event);
      }
    };

    window.addEventListener(event, handleKeyPress);

    return () => {
      window.removeEventListener(event, handleKeyPress);
    };
  }, [targetKey, handler, event, ctrlKey, shiftKey, altKey]);
}

export default useKeyPress; 