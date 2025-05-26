import { Dispatch, SetStateAction, useCallback } from 'react';

import { useMessage } from '../_wheel/common/MessageProvider';
import { useAxios } from '../_wheel/security/ApiClient';
import type { UserType } from '../common/types/UserTypes';
import type { AddUserParamsType, DeleteUserCSVParamsType, DeleteUserParamsType, UpdateUserParamsType } from './UserTypesForManage';


const URL_USER = import.meta.env.VITE_REACT_APP_URL_USER;

export const useUserApi = () => {
  const { outMessage } = useMessage();
  const { apiClient } = useAxios();

  //ユーザ一覧を取得するAPI
  const getUsers = useCallback(async (setUsers: Dispatch<SetStateAction<Array<UserType>>>) => {
    let userDataList: Array<UserType> = [];

    await apiClient(true)
      .get(URL_USER)
      .then((res) => {
        userDataList = res.data.body;
      });

    if (userDataList.length != 0) {
      setUsers(userDataList);
    }
  }, []);

  //指定されたユーザIDの詳細を取得するAPI
  const getUserDetail = useCallback(async (userId: string, setUser: Dispatch<SetStateAction<UserType | undefined>>) => {
    await apiClient(true)
      .get(URL_USER + '/' + userId)
      .then((res) => {
        setUser(res.data.body);
      });
  }, []);

  //新しいユーザを追加するAPI
  const addUser = useCallback(async (params: AddUserParamsType) => {
    await apiClient(true)
      .post(URL_USER, params)
      .then((res) => {
        if (res.data.responseCode === '200') {
          outMessage('ユーザを追加しました', false);
        } else {
          outMessage('入力値が正しくありません', true);
        }
      });
  }, []);

  //ユーザ情報を更新するAPI
  const updateUser = useCallback(async (params: UpdateUserParamsType) => {
    await apiClient(true)
      .put(URL_USER, params)
      .then((res) => {
        if (res.data.responseCode === '200') {
          outMessage('ユーザを変更しました', false);
        } else {
          outMessage(res.data.message, true);
        }
      });
  }, []);

  //ユーザを削除するAPI
  const deleteUser = useCallback(
    async (params: DeleteUserParamsType, setCSV: Dispatch<SetStateAction<Array<DeleteUserCSVParamsType>>>) => {
      await apiClient(true)
        .delete(URL_USER + '?id=' + params.userId)
        .then((res) => {
          if (res.data.responseCode === '200') {
            outMessage('ユーザを削除しました', false);
            console.log(res.data.body.csvList);
            setCSV(res.data.body.csvList);
          } else {
            outMessage(res.data.message, true);
          }
        });
    },
    []
  );

  return { getUsers, getUserDetail, addUser, updateUser, deleteUser };
};
