import { Dispatch, SetStateAction, useCallback } from 'react';

import { useMessage } from '../_wheel/common/MessageProvider';
import { useAxios } from '../_wheel/security/ApiClient';
import type { AddPageParamsType, NewPageParamsType } from './JobTypesForManage';
import { DetailPageParamsType } from './JobTypesForManage';
import { ChangeStateParamsType } from './JobTypesForManage';
import { ReportPageParamsType } from './JobTypesForManage';
import { UserDetailParamsType } from './JobTypesForManage';

// APIのエンドポイントURL
const URL_HOME_DETAIL = import.meta.env.VITE_REACT_APP_URL_HOME_DETAIL;
const URL_HOME_DETAIL_NEW = import.meta.env.VITE_REACT_APP_URL_NEW;

// 就職活動関連のAPIを管理するカスタムフック
export const useJobApi = () => {
  const { apiClient } = useAxios();
  const { outMessage } = useMessage();

  //ジョブの詳細を取得するAPI
  const getJobDetail = useCallback(async (id: string, setJobDetail: Dispatch<SetStateAction<DetailPageParamsType>>) => {
    await apiClient(true)
      .get(URL_HOME_DETAIL + '/' + id)
      .then((res) => {
        setJobDetail(res.data.body);
      });
  }, []);

  //新しいジョブを追加するAPI
  const addJob = useCallback(async (params: AddPageParamsType) => {
    await apiClient(true)
      .post(URL_HOME_DETAIL, params)
      .then((res) => {
        if (res.data.responseCode === '200') {
          outMessage('申請しました', false);
        } else {
          outMessage(res.data.message, true);
        }
      });
  }, []);

  //ジョブの状態を変更するAPI
  const changeState = useCallback(async (params: ChangeStateParamsType) => {
    await apiClient(true)
      .put(URL_HOME_DETAIL, params)
      .then((res) => {
        if (res.data.responseCode === '200') {
          outMessage(res.data.message, false);
        } else {
          outMessage(res.data.message, true);
        }
      });
  }, []);

  //ジョブを削除するAPI
  const deleteJob = useCallback(async (jobHuntId: string) => {
    await apiClient(true)
      .delete(URL_HOME_DETAIL + '?jobHuntId=' + jobHuntId)
      .then((res) => {
        if (res.data.responseCode === '200') {
          outMessage('取り消しました', false);
        } else {
          outMessage(res.data.message, true);
        }
      });
  }, []);

  // ジョブの報告を行うAPI
  const reportJob = useCallback(async (params: ReportPageParamsType) => {
    await apiClient(true)
      .post(URL_HOME_DETAIL + '/report', params)
      .then((res) => {
        if (res.data.responseCode === '200') {
          outMessage('報告しました', false);
        } else {
          outMessage(res.data.message, true);
        }
      });
  }, []);

  // ユーザー詳細情報を取得するAPI
  const getUserDetail = useCallback(
    async (id: string | null, setUserDetail: Dispatch<SetStateAction<UserDetailParamsType | undefined>>) => {
      await apiClient(true)
        .get(URL_HOME_DETAIL_NEW + '/' + id)
        .then((res) => {
          setUserDetail(res.data.body);
        });
    },
    []
  );

  // 新しいジョブ申請を行うAPI
  const newJob = useCallback(async (params: NewPageParamsType) => {
    await apiClient(true)
      .post(URL_HOME_DETAIL_NEW, params)
      .then((res) => {
        if (res.data.responseCode === '200') {
          outMessage('申請しました', false);
        } else {
          outMessage(res.data.message, true);
        }
      });
  }, []);
  // 新しいジョブ変更を行うAPI
  const editJob = useCallback(async (params: NewPageParamsType) => {
    await apiClient(true)
      .put(URL_HOME_DETAIL_NEW, params)
      .then((res) => {
        if (res.data.responseCode === '200') {
          outMessage('申請しました', false);
        } else {
          outMessage(res.data.message, true);
        }
      });
  }, []);

  return { getJobDetail, addJob, deleteJob, changeState, reportJob, getUserDetail, newJob, editJob };
};
