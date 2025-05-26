import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Container from '@mui/material/Container';
import Grid from '@mui/material/Grid';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { FC } from 'react';
import { Controller, useForm } from 'react-hook-form';



// タイプ定義とカスタムフックのインポート
import type { LoginParamsType } from './LoginTypes';
// フォーム管理用のフック
import img from './img/logo.png';
import { useLoginApi } from './useLoginApi';


// アプリケーションのバージョン情報を環境変数から取得
const appVersion: string = import.meta.env.VITE_REACT_APP_VERSION ?? '';

// ログインページコンポーネント
export const LoginPage: FC = () => {
  // ログインAPIを使用するためのカスタムフック
  const { login } = useLoginApi();

  // フォームの状態管理用のフック
  const { control, getValues, formState } = useForm<LoginParamsType>({
    defaultValues: {
      userId: '',
      password: '',
    },
    mode: 'onChange',
  });

  // ログインボタンが押された時の処理
  const handleLogin = () => {
    const loginValues = getValues();
    login(loginValues);
  };

  // コンポーネントのレンダリング
  return (
    <Container
      maxWidth="sm"
      sx={{
        display: 'flex',
        justifyContent: 'center',
      }}
    >
      <Stack textAlign={'center'} sx={{ maxWidth: 330, marginTop: 10 }}>
        <Grid></Grid>
        <Grid sx={{ marginTop: 5 }}>
          <Card>
            <Typography
              component="h1"
              sx={{ padding: 2, backgroundColor: '#1976d2', color: '#fff', fontSize: '1.5rem' }}
            >
              JobPal
            </Typography>
            <CardContent sx={{ marginTop: 4, padding: '0 15px 10px 15px!important' }}>
              <Box onSubmit={handleLogin}>
                <Controller
                  control={control}
                  name="userId"
                  rules={{ required: 'ユーザID(メールアドレス)を入力してください' }}
                  render={({ field, fieldState }) => (
                    <TextField
                      label="メールアドレス"
                      variant="outlined"
                      {...field}
                      placeholder="xxxxxx@xxx.xx.xx"
                      sx={{ mb: 2 }}
                      fullWidth
                      autoFocus
                      error={fieldState.error?.message !== undefined}
                      helperText={fieldState.error?.message}
                      onKeyDown={(e) => {
                        if (e.key === 'Enter' && formState.isValid) {
                          handleLogin();
                        }
                      }}
                    />
                  )}
                />
                <Controller
                  control={control}
                  name="password"
                  rules={{ required: 'パスワードを入力してください' }}
                  render={({ field, fieldState }) => (
                    <TextField
                      label="パスワード"
                      variant="outlined"
                      {...field}
                      placeholder="パスワード"
                      type="password"
                      fullWidth
                      onKeyDown={(e) => {
                        if (e.key === 'Enter' && formState.isValid) {
                          handleLogin();
                        }
                      }}
                      error={fieldState.error?.message !== undefined}
                      helperText={fieldState.error?.message}
                    />
                  )}
                />
                <Button
                  type="submit"
                  variant="contained"
                  sx={{ marginTop: 7, padding: 1 }}
                  fullWidth
                  disabled={!formState.isValid}
                  onClick={handleLogin}
                >
                  ログイン
                </Button>
            </Box>
            </CardContent>
          </Card>
        </Grid>
        <Grid sx={{ marginTop: 2 }}>{appVersion}</Grid>
        <img src={img} alt="logo" style={{ width: "150px", margin: "0 auto" }}/>
      </Stack>
    </Container>
  );
};
