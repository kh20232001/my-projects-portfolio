import Backdrop from '@mui/material/Backdrop';
import Box from '@mui/material/Box';
import CircularProgress from '@mui/material/CircularProgress';
import { Dispatch, ReactNode, SetStateAction, createContext, useContext, useState } from 'react';

type LoadingContextType = {
  loading: boolean;
  setLoading: Dispatch<SetStateAction<boolean>>;
};

const LoadingContext = createContext<LoadingContextType>({} as LoadingContextType);

export const LoadingProvider = (props: { children: ReactNode }) => {
  const { children } = props;

  const [loading, setLoading] = useState<boolean>(false);

  const value: LoadingContextType = {
    loading,
    setLoading: setLoading,
  };

  return (
    <LoadingContext.Provider value={value}>
      <Backdrop
        sx={{
          color: '#fff',
          zIndex: (theme) => theme.zIndex.drawer + 1,
        }}
        open={loading}
      >
        <Box>
          <CircularProgress size={80} />
        </Box>
      </Backdrop>
      {children}
    </LoadingContext.Provider>
  );
};

export const useLoading = (): LoadingContextType => useContext(LoadingContext);
