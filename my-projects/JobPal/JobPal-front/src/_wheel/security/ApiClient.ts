import axios, { InternalAxiosRequestConfig } from 'axios';
import { useNavigate } from 'react-router-dom';

import { useLoading } from '../common/LoadingProvider';
import { useMessage } from '../common/MessageProvider';
import type { BindingResultType } from '../types/ApiTypes';
import { useLoginUser } from './LoginUserProvider';
import { accesslog } from './accesslog';

const endpointURL = import.meta.env.VITE_REACT_APP_ENDPOINT_URL;
const timeOutMilliSeconds = import.meta.env.VITE_REACT_APP_TIMEOUT_TIME;

export const useAxios = () => {
  const { logout } = useLoginUser();
  const { outMessage } = useMessage();
  const { setLoading } = useLoading();
  const navigate = useNavigate();

  const apiClient = (noMessage = false, viewLoading = false) => {
    const axiosInstance = axios.create({
      baseURL: endpointURL,
      timeout: timeOutMilliSeconds,
    });

    axiosInstance.interceptors.request.use((request: InternalAxiosRequestConfig) => {
      if (viewLoading) setLoading(true);
      accesslog().requestLog(request);

      if (request?.headers) {
        request.headers['Authorization'] = localStorage.getItem('token') ?? '';
        return request;
      }
      return request;
    });

    axiosInstance.interceptors.response.use(
      (response) => {
        accesslog().responseInfoLog(response);
        setLoading(false);

        if (!noMessage) outMessage(response.data.message, false);
        return response;
      },

      async (err) => {
        accesslog().responseErrorLog(err);
        setLoading(false);

        const httpStatus: number = err.response?.status ?? 0;
        switch (httpStatus) {
          case 401: // UNAUTHORIZED
            logout(true);
            break;

          case 403: // UNAUTHORIZED
            outMessage('削除できません', true);
            break;

          case 400: // BAD REQUEST
            const error: Array<BindingResultType> = err.response.data ?? undefined;
            if (error[0]?.field) {
              let errorMessage = '';
              err.response.data.forEach((err: BindingResultType) => (errorMessage += `${err.message}, `));
              outMessage('入力値が正しくありません。', true);
              return;
            }
            outMessage('入力値が正しくありません', true);
            return;
          case 499:
            outMessage('アクセスできません。', true);
            logout(true);
            break;

          default: // INTERNAL SERVER ERROR
            outMessage(err.response.data.message, true);
            return;
        }
      }
    );
    return axiosInstance;
  };

  return { apiClient };
};
