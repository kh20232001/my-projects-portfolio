import { Dispatch, SetStateAction, useCallback } from 'react';

import { useMessage } from '../_wheel/common/MessageProvider';
import { useAxios } from '../_wheel/security/ApiClient';
import type { ZipType } from './ZipTypes';

const URL_GET_ZIP = import.meta.env.VITE_REACT_APP_URL_API_ZIP;

export const useZipApi = () => {
  const { outMessage } = useMessage();
  const { apiClient } = useAxios();

  const getZipList = useCallback(async (zipCode: string, setList: Dispatch<SetStateAction<Array<ZipType>>>) => {
    let userDataList: Array<ZipType> = [];

    await apiClient(true, true)
      .get(URL_GET_ZIP + '?zipCode=' + zipCode)
      .then((res) => {
        userDataList = res.data.body.list;
        if (res.data.body.status != '200') {
          outMessage(res.data.body.message ?? 'API Access Error', true);
        }
      });

    if (userDataList.length != 0) {
      setList(userDataList);
    }
  }, []);

  return { getZipList };
};
