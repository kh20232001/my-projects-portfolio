import MuiAlert, { AlertColor, AlertProps } from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import { ReactNode, createContext, forwardRef, useContext, useState } from 'react';

type MessageContextType = {
  outMessage: (message: string, isError: boolean) => void;
};

const Alert = forwardRef<HTMLDivElement, AlertProps>(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

const MessageContext = createContext<MessageContextType>({} as MessageContextType);

export const MessageProvider = (props: { children: ReactNode }) => {
  const { children } = props;

  const [message, setMessage] = useState<string>('');
  const [color, setColor] = useState<AlertColor>('info');
  const [open, setOpen] = useState<boolean>(false);

  const outMessage = (message: string, isError: boolean = false) => {
    setOpen(true);
    setMessage(message);
    const messageColor = isError ? 'error' : 'success';
    setColor(messageColor);
  };

  const handleClose = (event?: React.SyntheticEvent | Event, reason?: string) => {
    if (reason === 'clickaway') {
      return;
    }
    setOpen(false);
  };

  const value: MessageContextType = {
    outMessage: outMessage,
  };

  return (
    <MessageContext.Provider value={value}>
      {children}
      <Snackbar
        sx={{ marginBottom: '40px' }}
        open={open}
        autoHideDuration={3000}
        onClose={handleClose}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert severity={color} onClose={handleClose} sx={{ whiteSpace: 'pre-wrap' }}>
          {message}
        </Alert>
      </Snackbar>
    </MessageContext.Provider>
  );
};

export const useMessage = (): MessageContextType => useContext(MessageContext);
