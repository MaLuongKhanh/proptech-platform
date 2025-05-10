import { useEffect, useRef } from 'react';

type EventHandler<T extends Event> = (event: T) => void;

function useEventListener<K extends keyof WindowEventMap>(
  eventName: K,
  handler: EventHandler<WindowEventMap[K]>,
  element: Window | HTMLElement | null = window,
  options?: boolean | AddEventListenerOptions
): void {
  const savedHandler = useRef<EventHandler<WindowEventMap[K]>>();

  useEffect(() => {
    savedHandler.current = handler;
  }, [handler]);

  useEffect(() => {
    if (!element) return;

    const eventListener = (event: Event) => {
      if (savedHandler.current) {
        savedHandler.current(event as WindowEventMap[K]);
      }
    };

    element.addEventListener(eventName, eventListener, options);

    return () => {
      element.removeEventListener(eventName, eventListener, options);
    };
  }, [eventName, element, options]);
}

export default useEventListener; 