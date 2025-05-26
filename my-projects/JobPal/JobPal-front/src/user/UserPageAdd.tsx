import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Container from '@mui/material/Container';
import FormControl from '@mui/material/FormControl';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormLabel from '@mui/material/FormLabel';
import Grid from '@mui/material/Grid';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { FC } from 'react';
import { Controller, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';

import { useLoginUser } from '../_wheel/security/LoginUserProvider';
import { BackLink } from '../common/components/BackLink';
import { authorityOptions } from '../common/types/AuthorityTypes';
import type { AddUserParamsType } from './UserTypesForManage';
import { useUserApi } from './useUserApi';

export const UserPageAdd: FC = () => {
  const navigate = useNavigate();
  const { getAuthority, getUserId } = useLoginUser();
  const { addUser } = useUserApi();

  const { control, getValues, formState } = useForm<AddUserParamsType>({
    defaultValues: {
      userId: '',
      password: '',
      userName: '',
      userClass: '',
      classNumber: 0,
      schoolNumber: 0,
      grant: '0',
      status: 'VALID',
      createUserId: getUserId() ?? '',
    },
    mode: 'onChange',
  });

  // ユーザ追加処理
  const handleAdd = async () => {
    const result = window.confirm('入力した内容でユーザを追加しますか？');
    if (result) {
      await addUser(getValues());
      navigate('/user');
    }
  };

  return (
    <Container component="main" maxWidth="md">
      <BackLink />

      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Typography component="h1" variant="h4">
          ユーザ追加
        </Typography>
      </Box>

      <Card sx={{ marginTop: 2, marginBottom: 2 }}>
        <CardContent>
          <Grid container spacing={2} alignItems="flex-start">
            <Grid item xs={12}>
              <FormControl fullWidth>
                <FormLabel>ユーザID(メールアドレス)</FormLabel>
                <Controller
                  control={control}
                  name="userId"
                  rules={{ required: 'ユーザIDを入力してください' }}
                  render={({ field, fieldState }) => (
                    <TextField
                      {...field}
                      type="email"
                      placeholder="ユーザID(メールアドレス)"
                      size="small"
                      fullWidth
                      autoFocus
                      error={fieldState.error?.message !== undefined}
                      helperText={fieldState.error?.message}
                    />
                  )}
                />
              </FormControl>
            </Grid>
            <Grid item xs={12} md={6}>
              <FormControl fullWidth>
                <FormLabel>パスワード</FormLabel>
                <Controller
                  control={control}
                  name="password"
                  rules={{ required: false }}
                  render={({ field, fieldState }) => (
                    <TextField
                      {...field}
                      type="password"
                      placeholder="パスワード"
                      size="small"
                      fullWidth
                      error={fieldState.error?.message !== undefined}
                      helperText={fieldState.error?.message}
                    />
                  )}
                />
              </FormControl>
            </Grid>
            <Grid item xs={12} md={6}>
              <FormControl fullWidth>
                <FormLabel>ユーザ名</FormLabel>
                <Controller
                  control={control}
                  name="userName"
                  rules={{ required: 'ユーザ名を入力してください' }}
                  render={({ field, fieldState }) => (
                    <TextField
                      {...field}
                      placeholder="ユーザ名"
                      size="small"
                      fullWidth
                      error={fieldState.error?.message !== undefined}
                      helperText={fieldState.error?.message}
                    />
                  )}
                />
              </FormControl>
            </Grid>
            <Grid item xs={12} md={6}>
              <FormControl fullWidth>
                <FormLabel>クラス</FormLabel>
                <Controller
                  control={control}
                  name="userClass"
                  rules={{ required: 'クラスを入力してください' }}
                  render={({ field, fieldState }) => (
                    <TextField
                      {...field}
                      placeholder="クラス"
                      size="small"
                      fullWidth
                      error={fieldState.error?.message !== undefined}
                      helperText={fieldState.error?.message}
                    />
                  )}
                />
              </FormControl>
            </Grid>
            <Grid item xs={12} md={6}>
              <FormControl fullWidth>
                <FormLabel>出席番号</FormLabel>
                <Controller
                  control={control}
                  name="classNumber"
                  rules={{ required: '出席番号を入力してください' }}
                  render={({ field, fieldState }) => (
                    <TextField
                      {...field}
                      placeholder="出席番号"
                      size="small"
                      fullWidth
                      error={fieldState.error?.message !== undefined}
                      helperText={fieldState.error?.message}
                    />
                  )}
                />
              </FormControl>
            </Grid>
            <Grid item xs={12} md={6}>
              <FormControl fullWidth>
                <FormLabel>学籍番号</FormLabel>
                <Controller
                  control={control}
                  name="schoolNumber"
                  rules={{ required: '学籍番号を入力してください' }}
                  render={({ field, fieldState }) => (
                    <TextField
                      {...field}
                      placeholder="学籍番号"
                      size="small"
                      fullWidth
                      error={fieldState.error?.message !== undefined}
                      helperText={fieldState.error?.message}
                    />
                  )}
                />
              </FormControl>
            </Grid>
            <Grid item xs={12} md={6}>
              <FormControl fullWidth>
                <FormLabel>権限</FormLabel>
                <Controller
                  name="grant"
                  control={control}
                  render={({ field }) => (
                    <RadioGroup {...field} row>
                      {authorityOptions
                        .filter((v) => getAuthority() === '2' || v.value !== '2')
                        .map((v) => (
                          <FormControlLabel key={v.value} label={v.label} value={v.value} control={<Radio />} />
                        ))}
                    </RadioGroup>
                  )}
                />
              </FormControl>
            </Grid>
          </Grid>

          <Button
            type="submit"
            variant="contained"
            color="warning"
            sx={{ marginTop: 1 }}
            fullWidth
            disabled={!formState.isValid}
            onClick={handleAdd}
          >
            追加
          </Button>
        </CardContent>
      </Card>
    </Container>
  );
};
