import { useState, useEffect } from 'react';

interface ImageLoadState {
  loaded: boolean;
  error: boolean;
  naturalWidth: number;
  naturalHeight: number;
}

function useImageLoad(src: string): ImageLoadState {
  const [state, setState] = useState<ImageLoadState>({
    loaded: false,
    error: false,
    naturalWidth: 0,
    naturalHeight: 0,
  });

  useEffect(() => {
    const image = new Image();

    const handleLoad = () => {
      setState({
        loaded: true,
        error: false,
        naturalWidth: image.naturalWidth,
        naturalHeight: image.naturalHeight,
      });
    };

    const handleError = () => {
      setState((prev) => ({
        ...prev,
        loaded: false,
        error: true,
      }));
    };

    image.addEventListener('load', handleLoad);
    image.addEventListener('error', handleError);

    image.src = src;

    return () => {
      image.removeEventListener('load', handleLoad);
      image.removeEventListener('error', handleError);
    };
  }, [src]);

  return state;
}

export default useImageLoad;