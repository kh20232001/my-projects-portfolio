import { AxiosError, AxiosRequestConfig, AxiosResponse } from 'axios';



import type { MessageType } from '../types/ApiTypes';


const prodFlag = import.meta.env.VITE_REACT_APP_ACTIVE === 'prod';

const requestLog = (request: AxiosRequestConfig) => {
  if (prodFlag) return;
  console.info(`[REQUEST]: ${request.method} ${request.url}`, request.data ? request.data : '');
};

const responseInfoLog = (response: AxiosResponse<any>) => {
  if (prodFlag) return;
  const responseValue = response.data;
  console.info(`[RESPONSE(${response.status})]: ${response.config.method} ${response.config.url}`, responseValue);
};

const responseErrorLog = (response: AxiosError<MessageType>) => {
  if (prodFlag) return;
  console.error(
    `[RESPONSE(${response.response?.status})]: ${response.config?.method} ${response.config?.url} [${response.response?.data.message}]`
  );
};

const logDict = { requestLog, responseInfoLog, responseErrorLog };

export const accesslog = () => logDict;
