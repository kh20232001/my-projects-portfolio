import { Dispatch, SetStateAction, useCallback } from 'react';

import { useMessage } from '../_wheel/common/MessageProvider';
import { useAxios } from '../_wheel/security/ApiClient';
import type { HomeParamsType } from '../home/HomeParamsType';
import type { CertificateType, DetailPageParamsType, PostalParamsType } from './CertificateTypes';
import type { ChangeStateParamsType, NewPageParamsType } from './CertificateTypes';

// APIのエンドポイントURL
const URL_NEW = import.meta.env.VITE_REACT_APP_URL_CERTIFICATE_NEW;
const URL_CERTIFICATE = import.meta.env.VITE_REACT_APP_URL_CERTIFICATE;
const URL_DETAIL = import.meta.env.VITE_REACT_APP_URL_CERTIFICATE_DETAIL;

// 証明書関連のAPIを管理するカスタムフック
export const useCertificateApi = () => {
  const { apiClient } = useAxios();
  const { outMessage } = useMessage();

  // ダッシュボード用の証明書一覧を取得するAPI
  const getCertificate = useCallback(
    async (
      params: HomeParamsType,
      setCertificate: Dispatch<SetStateAction<Array<CertificateType>>>,
      setAlertCount: Dispatch<SetStateAction<number>>,
      setUserName: Dispatch<SetStateAction<string>>
    ) => {
      let certificateDataList: Array<CertificateType> = [];
      let alertCnt: number = 0;
      let userName: string = '';
      await apiClient(true)
        .post(URL_CERTIFICATE, params)
        .then((res) => {
          certificateDataList = res.data.body.dashBoardList;
          alertCnt = res.data.body.alertCnt;
          userName = res.data.body.userName;
        });
      if (certificateDataList.length !== 0) {
        setCertificate(certificateDataList);
      }
      setAlertCount(alertCnt);
      setUserName(userName);
    },
    []
  );

  // 新規証明書申請のAPI
  const newCertificate = useCallback(async (params: NewPageParamsType) => {
    await apiClient(true)
      .post(URL_NEW, params)
      .then((res) => {
        if (res.data.responseCode === '200') {
          outMessage('申請しました', false);
        } else {
          outMessage(res.data.message, true);
        }
      });
  }, []);

  // 証明書変更のAPI
  const editCertificate = useCallback(async (params: NewPageParamsType) => {
    await apiClient(true)
      .put(URL_NEW, params)
      .then((res) => {
        if (res.data.responseCode === '200') {
          outMessage('申請しました', false);
        } else {
          outMessage(res.data.message, true);
        }
      });
  }, []);

  // 郵便料金などの情報を取得するAPI
  const getPostal = useCallback(async (setPostal: Dispatch<SetStateAction<PostalParamsType | undefined>>) => {
    await apiClient(true)
      .get(URL_NEW)
      .then((res) => {
        setPostal(res.data.body);
      });
  }, []);

  // 証明書詳細情報を取得するAPI
  const getCertificateDetail = useCallback(
    async (id: string, setCertificatedetail: Dispatch<SetStateAction<DetailPageParamsType | undefined>>) => {
      await apiClient(true)
        .get(URL_DETAIL + '/' + id)
        .then((res) => {
          setCertificatedetail(res.data.body);
        });
    },
    []
  );

  // 証明書の削除を行うAPI
  const deleteCertificate = useCallback(async (certificateIssueId: string) => {
    await apiClient(true)
      .delete(URL_DETAIL + '?certificateIssueId=' + certificateIssueId)
      .then((res) => {
        if (res.data.responseCode === '200') {
          outMessage('取り消しました', false);
        } else {
          outMessage(res.data.message, true);
        }
      });
  }, []);

  // 証明書の状態を変更するAPI
  const changeState = useCallback(async (params: ChangeStateParamsType) => {
    await apiClient(true)
      .put(URL_DETAIL, params)
      .then((res) => {
        if (res.data.responseCode === '200') {
          outMessage(res.data.message, false);
        } else {
          outMessage(res.data.message, true);
        }
      });
  }, []);

  return {
    getCertificate,
    newCertificate,
    getCertificateDetail,
    deleteCertificate,
    changeState,
    getPostal,
    editCertificate,
  };
};
