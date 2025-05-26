import {
  Box,
  Button,
  Checkbox,
  Container,
  FormControl,
  FormControlLabel,
  FormLabel,
  Grid,
  MenuItem,
  Select,
  TextField,
  Typography,
} from '@mui/material';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import dayjs, { Dayjs } from 'dayjs';
import { FC, useEffect, useState } from 'react';
import { Controller, useForm } from 'react-hook-form';
import { useLocation, useNavigate } from 'react-router-dom';

import { useLoginUser } from '../_wheel/security/LoginUserProvider';
import { Authority } from '../common/types/AuthorityTypes';
import { UserDetailParamsType } from './JobTypesForManage';
import { useJobApi } from './useJobApi';

// 新規就職活動申請追加
export const JobPageNew: FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { getUserId } = useLoginUser();
  const { getUserDetail, newJob, editJob } = useJobApi();
  // ログインユーザー情報取得
  const { getAuthority } = useLoginUser();
  const [jobDetail, setJobDetail] = useState(
    location.state?.jobDetail || {
      userId: getUserId(),
      eventCategory: null,
      schoolCheck: false,
      company: '',
      location: null,
      locationType: null,
      startTime: null,
      finishTime: null,
      tardinessAbsenceType: null,
      tardyLeaveTime: null,
      remarks: '',
    }
  );

  // ユーザー詳細情報のステート
  const [userDetail, setUserDetail] = useState<UserDetailParamsType>();

  // React Hook Formでフォーム状態を管理
  const { control, getValues, formState, watch, setValue } = useForm({
    defaultValues: {
      userId: getUserId() || '',
      jobHuntId: jobDetail.jobHuntId || '',
      eventCategory: jobDetail.eventCategory === null ? '' : jobDetail.eventCategory,
      schoolCheck: jobDetail.schoolCheck,
      company: jobDetail.company === null ? '' : jobDetail.company,
      location: jobDetail.location === null ? '' : jobDetail.location,
      locationType: jobDetail.locationType === null ? '' : jobDetail.locationType,
      startTime: jobDetail.startTime === null ? '' : jobDetail.startTime,
      finishTime: jobDetail.finishTime === null ? '' : jobDetail.finishTime,
      tardinessAbsenceType: jobDetail.tardinessAbsenceType === null ? '' : jobDetail.tardinessAbsenceType,
      tardyLeaveTime: jobDetail.tardyLeaveTime === null ? '' : jobDetail.tardyLeaveTime,
      remarks: jobDetail.remarks === null ? '' : jobDetail.remarks,
    },
    mode: 'all',
  });

  // ユーザー詳細情報を取得
  useEffect(() => {
    getUserDetail(getUserId(), setUserDetail);
  }, []);

  // 新規申請処理
  const handleGoToJobNew = async () => {
    const isEdit = !!location.state?.jobDetail;
    console.log(location.state?.jobDetail);
    const result = window.confirm('申請しますか？');
    if (result) {
      if (isEdit) {
        // 編集の場合
        setValue('jobHuntId', jobDetail.jobHuntId);
        await editJob(getValues());
        navigate(`/home`);
      } else {
        await newJob(getValues());
        navigate(`/home`);
      }
    }
  };

  return (
    <>
      <Container>
        <Typography gutterBottom sx={{ padding: { xs: '8px 0', sm: '16px 0' }, fontSize: { xs: '18px', sm: '20px' } }}>
          就職活動申請
        </Typography>
        <Grid container spacing={2} sx={{ padding: { xs: '20px 5px!important', sm: '20px 0 40px 20px' } }}>
          <Grid item xs={4}>
            <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>学籍番号</Typography>
            <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{userDetail?.schoolNumber}</Typography>
          </Grid>
          <Grid item xs={4}>
            <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>クラス番号</Typography>
            <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>
              {userDetail?.userClass}
              {userDetail?.classNumber}
            </Typography>
          </Grid>
          <Grid item xs={4}>
            <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>名前</Typography>
            <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{userDetail?.userName}</Typography>
          </Grid>
        </Grid>
        <hr />
        <Grid container spacing={2} sx={{ padding: { xs: '20px 5px!important', sm: '20px 0 40px 20px' } }}>
          <Grid item xs={6}>
            <FormControl fullWidth>
              <FormLabel sx={{ fontSize: { xs: '13px', sm: '16px' } }}>イベント区分</FormLabel>
              <Controller
                control={control}
                name="eventCategory"
                rules={{ required: 'イベント区分を選択してください' }}
                render={({ field, fieldState }) => (
                  <>
                    <Select
                      {...field}
                      displayEmpty
                      variant="outlined"
                      id="select"
                      fullWidth
                      sx={{
                        minWidth: 120,
                        '& .MuiSelect-select': {
                          padding: '16px 14px !important',
                          fontSize: { xs: '13px', sm: '16px' },
                        },
                      }}
                    >
                      <MenuItem value="" disabled>
                        イベント区分
                      </MenuItem>
                      <MenuItem value="説明会_単">説明会_単</MenuItem>
                      <MenuItem value="説明会_合">説明会_合</MenuItem>
                      <MenuItem value="試験_面接">試験_面接</MenuItem>
                      <MenuItem value="試験_適正">試験_適正</MenuItem>
                      <MenuItem value="試験_他">試験_他</MenuItem>
                      <MenuItem value="インターン">インターン</MenuItem>
                      <MenuItem value="セミナー">セミナー</MenuItem>
                      <MenuItem value="内定式">内定式</MenuItem>
                      <MenuItem value="研修">研修</MenuItem>
                      <MenuItem value="他">他</MenuItem>
                    </Select>
                    {fieldState.error && (
                      <Typography color="error" variant="caption">
                        {fieldState.error.message}
                      </Typography>
                    )}
                  </>
                )}
              />
            </FormControl>
          </Grid>
          <Grid item xs={6}>
            <FormControl fullWidth>
              <FormLabel sx={{ fontSize: { xs: '13px', sm: '16px' } }}>場所区分</FormLabel>
              <Controller
                control={control}
                name="locationType"
                rules={{ required: '場所区分を選択してください' }}
                render={({ field, fieldState }) => (
                  <>
                    <Select
                      {...field}
                      displayEmpty
                      variant="outlined"
                      id="select"
                      fullWidth
                      sx={{
                        minWidth: 120,
                        '& .MuiSelect-select': {
                          padding: '16px 14px !important',
                          fontSize: { xs: '13px', sm: '16px' },
                        },
                      }}
                    >
                      <MenuItem value="" disabled>
                        場所区分
                      </MenuItem>
                      <MenuItem value="札幌">札幌</MenuItem>
                      <MenuItem value="東京">東京</MenuItem>
                      <MenuItem value="その他">その他</MenuItem>
                    </Select>
                    {fieldState.error && (
                      <Typography color="error" variant="caption">
                        {fieldState.error.message}
                      </Typography>
                    )}
                  </>
                )}
              />
            </FormControl>
          </Grid>
        </Grid>
        <Grid container spacing={2} sx={{ padding: { xs: '1px 5px!important', sm: '10px 0 20px 10px' } }}>
          <Grid item xs={6}>
            <Controller
              control={control}
              name="schoolCheck"
              render={({ field }) => (
                <FormControlLabel
                  control={
                    <Checkbox {...field} checked={field.value} onChange={(e) => field.onChange(e.target.checked)} />
                  }
                  label="学校申込"
                />
              )}
            />
          </Grid>
        </Grid>
        <Grid container spacing={2} sx={{ padding: { xs: '20px 5px!important', sm: '20px 0 40px 20px' } }}>
          <Grid item xs={6}>
            <FormControl fullWidth>
              <FormLabel sx={{ fontSize: { xs: '13px', sm: '16px' } }}>会社名 (正式名称、省略不可)</FormLabel>
              <Controller
                control={control}
                name="company"
                rules={{ required: '会社名を入力してください' }}
                render={({ field, fieldState }) => (
                  <TextField
                    {...field}
                    placeholder="会社名"
                    size="small"
                    fullWidth
                    error={fieldState.error?.message !== undefined}
                    helperText={fieldState.error?.message}
                    sx={{
                      minWidth: 120,
                      '& .MuiInputBase-input': {
                        padding: '16px 14px !important',
                        fontSize: { xs: '13px', sm: '16px' },
                      },
                    }}
                  />
                )}
              />
            </FormControl>
          </Grid>
          <Grid item xs={6}>
            <FormControl fullWidth>
              <FormLabel sx={{ fontSize: { xs: '13px', sm: '16px' }, marginBottom: { xs: '19px', sm: 0 } }}>
                場所
              </FormLabel>
              <Controller
                control={control}
                name="location"
                rules={{ required: '場所を入力してください' }}
                render={({ field, fieldState }) => (
                  <TextField
                    {...field}
                    placeholder="場所"
                    size="small"
                    fullWidth
                    autoFocus
                    error={fieldState.error?.message !== undefined}
                    helperText={fieldState.error?.message}
                    sx={{
                      minWidth: 120,
                      '& .MuiInputBase-input': {
                        padding: '16px 14px !important',
                        fontSize: { xs: '13px', sm: '16px' },
                      },
                    }}
                  />
                )}
              />
            </FormControl>
          </Grid>
        </Grid>
        <Grid container spacing={2} sx={{ padding: { xs: '10px 5px!important', sm: '20px 0 40px 20px' } }}>
          <Grid item xs={6}>
            <FormControl fullWidth>
              <FormLabel sx={{ fontSize: { xs: '13px', sm: '16px' } }}>開始時間</FormLabel>
              <Controller
                control={control}
                name="startTime"
                rules={{
                  required: '開始時間を入力してください',
                  validate: (value) => {
                    const finishTime = getValues('finishTime');
                    if (finishTime && dayjs(value).isAfter(dayjs(finishTime))) {
                      return '終了時間より前でなければなりません';
                    }
                    return true;
                  },
                }}
                render={({ field, fieldState }) => (
                  <LocalizationProvider dateAdapter={AdapterDayjs}>
                    <DateTimePicker
                      value={field.value ? dayjs(field.value) : null}
                      onChange={(value: Dayjs | null) => {
                        const formattedValue = value ? value.format('YYYY-MM-DD HH:mm:ss') : null;
                        field.onChange(formattedValue);
                      }}
                    />
                    {fieldState.error && (
                      <Typography color="error" variant="caption">
                        {fieldState.error.message}
                      </Typography>
                    )}
                  </LocalizationProvider>
                )}
              />
            </FormControl>
          </Grid>
          <Grid item xs={6}>
            <FormControl fullWidth>
              <FormLabel sx={{ fontSize: { xs: '13px', sm: '16px' } }}>終了時間</FormLabel>
              <Controller
                control={control}
                name="finishTime"
                rules={{
                  required: '終了時間を入力してください',
                  validate: (value) => {
                    const startTime = getValues('startTime');
                    if (startTime && dayjs(value).isBefore(dayjs(startTime))) {
                      return '開始時間より後でなければなりません';
                    }
                    return true;
                  },
                }}
                render={({ field, fieldState }) => (
                  <LocalizationProvider dateAdapter={AdapterDayjs}>
                    <DateTimePicker
                      value={field.value ? dayjs(field.value) : null}
                      onChange={(value: Dayjs | null) => {
                        const formattedValue = value ? value.format('YYYY-MM-DD HH:mm:ss') : null;
                        field.onChange(formattedValue);
                      }}
                    />
                    {fieldState.error && (
                      <Typography color="error" variant="caption">
                        {fieldState.error.message}
                      </Typography>
                    )}
                  </LocalizationProvider>
                )}
              />
            </FormControl>
          </Grid>
        </Grid>
        <Grid container spacing={2} sx={{ padding: { xs: '20px 5px!important', sm: '20px 0 40px 20px' } }}>
          <Grid item xs={12}>
            <FormControl fullWidth>
              <FormLabel sx={{ fontSize: { xs: '13px', sm: '16px' } }}>遅刻・欠席・早退</FormLabel>
              <Controller
                control={control}
                name="tardinessAbsenceType"
                rules={{ required: '出欠区分を選択してください' }}
                render={({ field, fieldState }) => (
                  <>
                    <Select
                      {...field}
                      displayEmpty
                      variant="outlined"
                      id="select"
                      fullWidth
                      sx={{
                        minWidth: 120,
                        '& .MuiSelect-select': {
                          padding: '16px 14px !important',
                          fontSize: { xs: '13px', sm: '16px' },
                        },
                      }}
                    >
                      <MenuItem value="" disabled>
                        出欠区分
                      </MenuItem>
                      <MenuItem value="なし">なし</MenuItem>
                      <MenuItem value="遅刻">遅刻</MenuItem>
                      <MenuItem value="欠席">欠席</MenuItem>
                      <MenuItem value="早退">早退</MenuItem>
                    </Select>
                    {fieldState.error && (
                      <Typography color="error" variant="caption">
                        {fieldState.error.message}
                      </Typography>
                    )}
                  </>
                )}
              />
            </FormControl>
          </Grid>
        </Grid>
        {watch('tardinessAbsenceType') !== '' &&
          (watch('tardinessAbsenceType') === '遅刻' || watch('tardinessAbsenceType') === '早退') && (
            <Grid container spacing={2} sx={{ padding: { xs: '20px 5px!important', sm: '20px 0 40px 20px' } }}>
              <Grid item xs={6}>
                <FormControl fullWidth>
                  <FormLabel sx={{ fontSize: { xs: '13px', sm: '16px' } }}>遅刻・早退時刻</FormLabel>
                  <Controller
                    control={control}
                    name="tardyLeaveTime"
                    rules={{
                      required: '遅刻・早退時刻を入力してください',
                      validate: (value) => {
                        const startTime = getValues('startTime');
                        const finishTime = getValues('finishTime');
                        if (
                          startTime &&
                          finishTime &&
                          (dayjs(value).isBefore(dayjs(startTime)) || dayjs(value).isAfter(dayjs(finishTime)))
                        ) {
                          return '開始時間と終了時間の間でなければなりません';
                        }
                        return true;
                      },
                    }}
                    render={({ field, fieldState }) => (
                      <LocalizationProvider dateAdapter={AdapterDayjs}>
                        <DateTimePicker
                          value={field.value ? dayjs(field.value) : null}
                          onChange={(value: Dayjs | null) => {
                            const formattedValue = value ? value.format('YYYY-MM-DD HH:mm:ss') : null;
                            field.onChange(formattedValue);
                          }}
                        />
                        {fieldState.error && (
                          <Typography color="error" variant="caption">
                            {fieldState.error.message}
                          </Typography>
                        )}
                      </LocalizationProvider>
                    )}
                  />
                </FormControl>
              </Grid>
            </Grid>
          )}
        <Grid container spacing={2} sx={{ padding: { xs: '20px 5px!important', sm: '20px 0 40px 20px' } }}>
          <Grid item xs={12}>
            <FormControl fullWidth>
              <FormLabel sx={{ fontSize: { xs: '13px', sm: '16px' } }}>備考</FormLabel>
              <Controller
                control={control}
                name="remarks"
                render={({ field, fieldState }) => (
                  <TextField
                    {...field}
                    size="small"
                    placeholder="備考"
                    fullWidth
                    multiline
                    rows={4}
                    sx={{
                      '& .MuiInputBase-input': {
                        fontSize: { xs: '13px', sm: '16px' },
                      },
                    }}
                  />
                )}
              />
            </FormControl>
          </Grid>
        </Grid>
        <hr />
        <Box display="flex" justifyContent="center" mt={3}>
          {getAuthority() === Authority.user && (
            <Button
              onClick={handleGoToJobNew}
              variant="contained"
              disabled={!formState.isValid}
              color="primary"
              style={{ marginRight: '1rem' }}
            >
              申請
            </Button>
          )}
        </Box>
      </Container>
    </>
  );
};
