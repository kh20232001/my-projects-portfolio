import { FC } from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';



import { useLoginUser } from '../_wheel/security/LoginUserProvider';
import { CertificatePage } from '../certificate/CertificatePage';
import { CertificatePageNew } from '../certificate/CertificatePageNew';
import { Certificatedetail } from '../certificate/Certificatedetail';
import { AlertPage } from '../home/AlertPage';
import { HomePage } from '../home/HomePage';
import { JobPageAdd } from '../job/JobPageAdd';
import { JobPageDetail } from '../job/JobPageDetail';
import { JobPageNew } from '../job/JobPageNew';
import { JobPageReport } from '../job/JobPageReport';
import { LoginPage } from '../login/LoginPage';
import { UserPage } from '../user/UserPage';
import { UserPageAdd } from '../user/UserPageAdd';
import { UserPageDetail } from '../user/UserPageDetail';
import { MainTemplate } from './MainTemplate';
import { Authority } from './types/AuthorityTypes';


type RouterType = {
  path: `/${string}`;
  children: JSX.Element;
};

// TODO 全員アクセス可
const AuthRouters: Array<RouterType> = [
  {
    path: '/home',
    children: <HomePage />,
  },
  {
    path: '/homedetail/:id',
    children: <JobPageDetail />,
  },
  {
    path: '/homedetail/add',
    children: <JobPageAdd />,
  },
  {
    path: '/alert',
    children: <AlertPage />,
  },
  {
    path: '/certificate',
    children: <CertificatePage />,
  },
  {
    path: '/certificatedetail/:id',
    children: <Certificatedetail />,
  },
  {
    path: '/certificate/new',
    children: <CertificatePageNew />,
  },
  {
    path: '/new',
    children: <JobPageNew />,
  },
  {
    path: '/homedetail/report',
    children: <JobPageReport />,
  },
];

// TODO 管理者のみアクセス可
const AdminRouters: Array<RouterType> = [
  {
    path: '/user',
    children: <UserPage />,
  },
  {
    path: '/user/add',
    children: <UserPageAdd />,
  },
  {
    path: '/user/:userId',
    children: <UserPageDetail />,
  },
];

export const AppRouter: FC = () => {
  const { isLogin, getAuthority } = useLoginUser();

  return (
    <Routes>
      <Route key={'/login'} path={'/login'} element={<LoginPage />} />

      {isLogin() && (
        <Route path="/" element={<MainTemplate />}>
          {AuthRouters.map((route) => (
            <Route key={route.path} path={route.path} element={route.children} />
          ))}

          {getAuthority() !== Authority.user &&
            AdminRouters.map((route) => <Route key={route.path} path={route.path} element={route.children} />)}
        </Route>
      )}

      <Route path="*" element={<Navigate to="/login" />} />
    </Routes>
  );
};
