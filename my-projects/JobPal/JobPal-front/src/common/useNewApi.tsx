import { Dispatch, SetStateAction, useCallback } from 'react';

import { useMessage } from '../_wheel/common/MessageProvider';
import { useAxios } from '../_wheel/security/ApiClient';

const URL_NEW = import.meta.env.VITE_REACT_APP_URL_NEW;

export const useNewApi = () => {
  const { outMessage } = useMessage();
  const { apiClient } = useAxios();

 // 新規就職活動申請追加API
  const newJob = useCallback(async (userId: string | null) => {
    await apiClient(true)
      .post(
        URL_NEW,
        { userId: userId },
        {
          headers: {
            'Content-Type': 'application/json',
          },
        }
      )
      .then((res) => {
        if (res.data.responseCode === '200') {
          outMessage('追加しました', false);
        } else {
          outMessage(res.data.message, true);
        }
      });
  }, []);

  return { newJob };
};
