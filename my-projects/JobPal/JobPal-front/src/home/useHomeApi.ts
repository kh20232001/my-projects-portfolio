import { Dispatch, SetStateAction, useCallback } from 'react';

import { useAxios } from '../_wheel/security/ApiClient';
import { CertificateType } from '../certificate/CertificateTypes';
import type { HomeParamsType } from './HomeParamsType';
import type { CSVType, HomeType } from './HomeTypes';

const URL_HOME = import.meta.env.VITE_REACT_APP_URL_HOME;
const URL_ALERT = import.meta.env.VITE_REACT_APP_URL_ALERT;

export const useHomeApi = () => {
  const { apiClient } = useAxios();

  // ジョブ一覧を取得するAPI
  const getJobs = useCallback(
    async (
      params: HomeParamsType,
      setJobs: Dispatch<SetStateAction<Array<HomeType>>>,
      setAlertCount: Dispatch<SetStateAction<number>>,
      setUserName: Dispatch<SetStateAction<string>>,
      setCSV: Dispatch<SetStateAction<Array<CSVType>>>
    ) => {
      let jobDataList: Array<HomeType> = [];
      let alertCnt: number = 0;
      let userName: string = '';
      let CSVList: Array<CSVType> = [];

      await apiClient(true)
        .post(URL_HOME, params)
        .then((res) => {
          jobDataList = res.data.body.dashBoardList ?? [];
          alertCnt = res.data.body.alertCnt ?? 0;
          userName = res.data.body.userName;
          CSVList = res.data.body.csvList ?? [];
        });
      if (jobDataList.length !== 0) {
        setJobs(jobDataList);
      }
      if (CSVList.length !== 0) {
        setCSV(CSVList);
      }
      setAlertCount(alertCnt);
      setUserName(userName);
    },
    []
  );

  //アラートジョブ一覧を取得するAPI
  const getAlerts = useCallback(
    async (
      params: HomeParamsType,
      setJobs: Dispatch<SetStateAction<Array<HomeType>>>,
      setCertificate: Dispatch<SetStateAction<Array<CertificateType>>>,
      setUserName: Dispatch<SetStateAction<string>>
    ) => {
      let jobDataList: Array<HomeType> = [];
      let certificateList: Array<CertificateType> = [];
      let userName: string = '';
      await apiClient(true)
        .post(URL_ALERT, params)
        .then((res) => {
          jobDataList = res.data.body.dashBoardList ?? [];
          certificateList = res.data.body.certificateList ?? [];
          userName = res.data.body.userName;
        });
      if (jobDataList.length !== 0) {
        setJobs(jobDataList);
      }
      if (certificateList.length !== 0) {
        setCertificate(certificateList);
      }
      setUserName(userName);
    },
    []
  );

  return { getJobs, getAlerts };
};
