import { FC } from 'react';
import { BrowserRouter } from 'react-router-dom';

import './App.css';
import { LoadingProvider } from './_wheel/common/LoadingProvider';
import { MessageProvider } from './_wheel/common/MessageProvider';
import { LoginUserProvider } from './_wheel/security/LoginUserProvider';
import { AppRouter } from './common/AppRouter';

export const App: FC = () => {
  return (
    <BrowserRouter>
      <LoadingProvider>
        <MessageProvider>
          <LoginUserProvider>
            <AppRouter />
          </LoginUserProvider>
        </MessageProvider>
      </LoadingProvider>
    </BrowserRouter>
  );
};
