import axios from 'axios';
import { useCallback } from 'react';
import { useNavigate } from 'react-router-dom';

import { useLoading } from '../_wheel/common/LoadingProvider';
import { useMessage } from '../_wheel/common/MessageProvider';
import { log } from '../_wheel/common/log';
import { useLoginUser } from '../_wheel/security/LoginUserProvider';
import { Authority } from '../common/types/AuthorityTypes';
import type { LoginParamsType } from './LoginTypes';

const baseURL = import.meta.env.VITE_REACT_APP_ENDPOINT_URL;
const loginURL = import.meta.env.VITE_REACT_APP_URL_LOGIN;

export const useLoginApi = () => {
  const navigate = useNavigate();
  const { setLoading } = useLoading();
  const { outMessage } = useMessage();
  const { getAuthority } = useLoginUser();

  const { tokenDecode } = useLoginUser();

  //ログインAPI
  const login = useCallback((params: LoginParamsType) => {
    setLoading(true);

    axios
      .post(baseURL + loginURL, params)
      .then((res) => {
        const jwtToken = res.headers['authorization'];

        if (jwtToken) {
          localStorage.setItem('token', jwtToken);
          tokenDecode(jwtToken);
          log().info(jwtToken);

          outMessage('ログインに成功しました', false);
          if (getAuthority() === Authority.clerk) {
            navigate('/certificate');
            return;
          }
          navigate('/home');
          return;
        }
        outMessage('エラーが発生しました。システム管理者に確認してください。', true);
      })

      .catch((err) => {
        log().error(err);
        if (err.response?.status === 401) {
          outMessage('パスワードまたはメールアドレスが違うか、アカウントが無効になっています。', true);
          return;
        }
        outMessage('エラーが発生しました。システム管理者に確認してください。', true);
      })

      .finally(() => setLoading(false));
  }, []);

  return { login };
};
