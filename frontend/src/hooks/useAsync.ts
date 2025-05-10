import { useState, useCallback } from 'react';

interface AsyncState<T> {
  loading: boolean;
  error: Error | null;
  data: T | null;
}

type AsyncFunction<T> = (...args: any[]) => Promise<T>;

function useAsync<T>(asyncFunction: AsyncFunction<T>, immediate = false) {
  const [state, setState] = useState<AsyncState<T>>({
    loading: false,
    error: null,
    data: null,
  });

  const execute = useCallback(
    async (...args: any[]) => {
      setState((prev) => ({ ...prev, loading: true }));
      try {
        const response = await asyncFunction(...args);
        setState({
          loading: false,
          error: null,
          data: response,
        });
        return response;
      } catch (error: any) {
        // Xử lý lỗi API
        const errorMessage = error.response?.data?.message || error.message || 'Đã có lỗi xảy ra';
        console.error('API Error:', {
          status: error.response?.status,
          message: errorMessage,
          data: error.response?.data
        });

        setState({
          loading: false,
          error: new Error(errorMessage),
          data: null,
        });

        // Không throw lỗi nữa mà trả về null
        return null;
      }
    },
    [asyncFunction]
  );

  return { execute, ...state };
}

export default useAsync; 