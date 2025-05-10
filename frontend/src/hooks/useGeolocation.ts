import { useState, useEffect } from 'react';

interface GeolocationState {
  loading: boolean;
  error: string | null;
  position: {
    latitude: number | null;
    longitude: number | null;
    accuracy: number | null;
  };
}

interface UseGeolocationOptions {
  enableHighAccuracy?: boolean;
  timeout?: number;
  maximumAge?: number;
}

function useGeolocation(options: UseGeolocationOptions = {}): GeolocationState {
  const [state, setState] = useState<GeolocationState>({
    loading: true,
    error: null,
    position: {
      latitude: null,
      longitude: null,
      accuracy: null,
    },
  });

  useEffect(() => {
    if (!navigator.geolocation) {
      setState((prev) => ({
        ...prev,
        loading: false,
        error: 'Trình duyệt không hỗ trợ Geolocation',
      }));
      return;
    }

    const onSuccess = (position: GeolocationPosition) => {
      setState({
        loading: false,
        error: null,
        position: {
          latitude: position.coords.latitude,
          longitude: position.coords.longitude,
          accuracy: position.coords.accuracy,
        },
      });
    };

    const onError = (error: GeolocationPositionError) => {
      setState((prev) => ({
        ...prev,
        loading: false,
        error: error.message,
      }));
    };

    const geolocationOptions: PositionOptions = {
      enableHighAccuracy: options.enableHighAccuracy || false,
      timeout: options.timeout || 5000,
      maximumAge: options.maximumAge || 0,
    };

    const watchId = navigator.geolocation.watchPosition(
      onSuccess,
      onError,
      geolocationOptions
    );

    return () => {
      navigator.geolocation.clearWatch(watchId);
    };
  }, [options.enableHighAccuracy, options.timeout, options.maximumAge]);

  return state;
}

export default useGeolocation; 