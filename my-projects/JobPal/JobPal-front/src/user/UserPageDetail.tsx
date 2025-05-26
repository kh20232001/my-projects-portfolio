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
import { FC, useEffect, useRef, useState } from 'react';
import { Controller, useForm } from 'react-hook-form';
import { useLocation, useNavigate } from 'react-router-dom';

import { useLoginUser } from '../_wheel/security/LoginUserProvider';
import { BackLink } from '../common/components/BackLink';
import { UserCard } from '../common/components/UserCard';
import { authorityOptions } from '../common/types/AuthorityTypes';
import type { UserType } from '../common/types/UserTypes';
import type { DeleteUserCSVParamsType, UpdateUserParamsType } from './UserTypesForManage';
import { useUserApi } from './useUserApi';

export const UserPageDetail: FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { getAuthority } = useLoginUser();

  const { getUserDetail, updateUser, deleteUser } = useUserApi();

  const { control, getValues, formState } = useForm<UpdateUserParamsType>({
    defaultValues: {
      userId: location.state.userId || '',
      password: '',
      userName: location.state.userName || '',
      userClass: location.state.userClass || '',
      classNumber: location.state.classNumber || '',
      schoolNumber: location.state.schoolNumber || '',
      grant: location.state.grant || '',
      status: location.state.status || '',
    },
    mode: 'onChange',
  });

  const [user, setUser] = useState<UserType>();
  const [csv, setCSV] = useState<Array<DeleteUserCSVParamsType>>([]);
  const isFirstRender = useRef(true);

  // ユーザー詳細の取得
  useEffect(() => {
    getUserDetail(location.state.userId, setUser);
  }, []);

  useEffect(() => {
    if (isFirstRender.current) {
      isFirstRender.current = false;
      return;
    }
    handleCSV();
    navigate('/user');
  }, [csv]);

  // ユーザー更新処理
  const handleUpdate = async () => {
    const result = window.confirm('入力した内容でユーザを更新しますか？');
    if (result) {
      await updateUser(getValues());
      navigate('/user');
    }
  };

  // ユーザー削除処理
  const handleDelete = async () => {
    const result = window.confirm('このユーザを削除しますか？');
    if (result) {
      await deleteUser(
        {
          userId: location.state.userId,
        },
        setCSV
      );
    }
  };

  const handleCSV = () => {
    const csvContent = [
      ['userId', ...csv.map((c) => c.userId)],
      ['名前', ...csv.map((c) => c.userName)],
      ['就職活動ID', ...csv.flatMap((c) => c.job.map((j) => j.jobHuntId))],
      ['証明書ID', ...csv.flatMap((c) => c.certificate.map((cert) => cert.certificateIssueId))],
    ]
      .map((e) => e.join(','))
      .join('\n');

    // BOMを追加
    const bom = '\uFEFF'; // UTF-8 BOM
    const blob = new Blob([bom + csvContent], { type: 'text/csv;charset=utf-8;' });

    const link = document.createElement('a');
    const url = URL.createObjectURL(blob);
    link.setAttribute('href', url);

    console.log(csv);
    link.setAttribute('download', `${csv[0].userId}_${csv[0].userName}.csv`);
    link.style.visibility = 'hidden';

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  return user ? (
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
          ユーザ情報詳細
        </Typography>
      </Box>

      <UserCard user={user} managed={true} />

      <Card sx={{ marginTop: 2, marginBottom: 2 }}>
        <CardContent>
          <Grid container spacing={2} alignItems="flex-start">
            <Grid item xs={12} md={6}>
              <FormControl fullWidth>
                <FormLabel>新しいパスワード(更新しない場合は未入力)</FormLabel>
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
                      size="small"
                      placeholder="ユーザ名"
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
                      size="small"
                      placeholder="クラス"
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
                      size="small"
                      placeholder="出席番号"
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
                      size="small"
                      placeholder="学籍番号"
                      fullWidth
                      InputProps={{
                        readOnly: true,
                      }}
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
            <Grid item xs={12} md={7}>
              <FormControl fullWidth>
                <FormLabel>状態</FormLabel>
                <Controller
                  name="status"
                  control={control}
                  render={({ field }) => (
                    <RadioGroup value={field.value} row>
                      <FormControlLabel {...field} label="無効" value="INVALID" control={<Radio />} />
                      <FormControlLabel {...field} label="有効" value="VALID" control={<Radio />} />
                      <FormControlLabel {...field} label="ロック" value="LOCKED" control={<Radio />} />
                      <FormControlLabel {...field} label="卒業" value="GRADUATION" control={<Radio />} />
                    </RadioGroup>
                  )}
                />
              </FormControl>
            </Grid>
          </Grid>

          <Button
            type="submit"
            variant="contained"
            color="secondary"
            sx={{ marginTop: 1 }}
            fullWidth
            disabled={!formState.isValid}
            onClick={handleUpdate}
          >
            更新
          </Button>
        </CardContent>
      </Card>

      <Button variant="contained" color="error" sx={{ marginTop: 2, marginBottom: 2 }} onClick={handleDelete}>
        削除
      </Button>
    </Container>
  ) : (
    <></>
  );
};
