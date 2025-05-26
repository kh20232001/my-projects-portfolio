import { jwtDecode } from "jwt-decode";
import { ReactNode, createContext, useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';



import { useMessage } from '../common/MessageProvider';
import { log } from '../common/log';


type LoginUserContextType = {
  getUserId: () => string | null;
  getAuthority: () => string | null;
  isLogin: () => boolean;
  logout: (timeout?: boolean) => void;
  tokenDecode: (token: string) => void;
};

type JwtDecodeType = {
  userId: string;
  grantId: string;
  exp: number;
};

const LoginUserContext = createContext<LoginUserContextType>({} as LoginUserContextType);

export const LoginUserProvider = (props: { children: ReactNode }) => {
  const { children } = props;

  const navigate = useNavigate();
  const { outMessage } = useMessage();

  const [decodeToken, setDecodeToken] = useState<JwtDecodeType>({} as JwtDecodeType);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decodedToken: JwtDecodeType = jwtDecode(token);
        setDecodeToken(decodedToken);
      } catch (e) {
        log().error(`トークンのデコード失敗 Token=${token}`);
      }
    }
  }, []);

  const logout = (timeout?: boolean) => {
    localStorage.removeItem('token');
    setDecodeToken({} as JwtDecodeType);

    if (timeout) {
      outMessage('タイムアウトしました。再度ログインしてください', true);
      navigate('/login');
      return;
    }

    outMessage('ログアウトしました', false);
    navigate('/login');
  };

  const tokenDecode = (token: string) => {
    try {
      const decodedToken: JwtDecodeType = jwtDecode(token);
      setDecodeToken(decodedToken);
    } catch (e) {
      log().error(`トークンのデコード失敗 Token=${token}`);
    }
  };

  const getUserId = () => {
    if (decodeToken.userId != undefined) {
      return decodeToken.userId;
    }
    const token = localStorage.getItem('token');
    if (token != null) {
      try {
        const decodedToken: JwtDecodeType = jwtDecode(token);
        const userId = decodedToken.userId;
        return userId;
      } catch (e) {
        log().error(`トークンのデコード失敗 Token=${token}`);
        return null;
      }
    } else {
      return null;
    }
  };

  const getAuthority = () => {
    if (decodeToken.grantId != undefined) {
      return decodeToken.grantId;
    }
    const token = localStorage.getItem('token');
    if (token != null) {
      try {
        const decodedToken: JwtDecodeType = jwtDecode(token);
        const role = decodedToken.grantId;
        return role;
      } catch (e) {
        log().error(`トークンのデコード失敗 Token=${token}`);
        return null;
      }
    } else {
      return null;
    }
  };

  const isLogin = (): boolean => {
    if (decodeToken.exp != undefined) {
      const dateStr = String(Date.now());
      const sliceDate = Number(dateStr.slice(0, -3));
      const checkExpire = decodeToken.exp - sliceDate;
      return checkExpire > 0;
    }
    return false;
  };

  const contextValue: LoginUserContextType = {
    getUserId: getUserId,
    getAuthority: getAuthority,
    isLogin: isLogin,
    logout: logout,
    tokenDecode: tokenDecode,
  };

  return <LoginUserContext.Provider value={contextValue}>{children}</LoginUserContext.Provider>;
};

export const useLoginUser = (): LoginUserContextType => useContext(LoginUserContext);
