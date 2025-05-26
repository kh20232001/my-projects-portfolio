import { Box, Button, Container, Grid, MenuItem, Select, TextField, Typography } from '@mui/material';
import FormControl from '@mui/material/FormControl';
import FormLabel from '@mui/material/FormLabel';
import { FC } from 'react';
import { useState } from 'react';
import { Controller, useForm } from 'react-hook-form';
import { useLocation, useNavigate } from 'react-router-dom';

import { useLoginUser } from '../_wheel/security/LoginUserProvider';
import { Authority } from '../common/types/AuthorityTypes';
import type { AddPageParamsType } from './JobTypesForManage';
import { useJobApi } from './useJobApi';

export const JobPageAdd: FC = () => {
  // ページ遷移とAPI呼び出しのためのフック
  const location = useLocation();
  const navigate = useNavigate();
  const { addJob } = useJobApi();
  // ログインユーザー情報取得
  const { getAuthority } = useLoginUser();

  // ページ遷移時に渡されたstateから初期値を設定
  const [jobDetail, setJobDetail] = useState(location.state.jobDetail);

  // フォームの状態管理
  const { control, getValues, formState } = useForm<AddPageParamsType>({
    defaultValues: {
      jobHuntId: jobDetail.jobHuntId,
      supportedCnt: jobDetail.supportedCnt,
      jobTitle: jobDetail.jobTitle,
      examCategory: jobDetail.examCategory,
      examContentId: jobDetail.examContent === null ? '' : jobDetail.examContent,
      examinationContent: jobDetail.ecaminationContent,
      thoughts: jobDetail.thoughts,
    },
    mode: 'onChange',
  });

  // ジョブ追加処理
  const handleGoToJobAdd = async () => {
    const result = window.confirm('報告しますか？');
    if (result) {
      await addJob(getValues());
      navigate(`/homedetail/${jobDetail.jobHuntId}`, { state: { id: jobDetail.jobHuntId } });
    }
  };

  return (
    <Container>
      <Typography gutterBottom sx={{ padding: { xs: '8px', sm: '16px 0' }, fontSize: { xs: '18px', sm: '20px' } }}>
        就職活動詳細
      </Typography>
      <Grid container spacing={2} sx={{ padding: { xs: '20px 5px!important', sm: '20px 0 40px 20px' } }}>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>申請番号</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.jobHuntId}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>状態</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.jobStateId}</Typography>
        </Grid>
        <Grid item xs={4}></Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>学籍番号</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.schoolNumber}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>クラス番号</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>
            {jobDetail.userClass}
            {jobDetail.classNumber}
          </Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>名前</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.userName}</Typography>
        </Grid>
      </Grid>
      <hr />
      <Grid container spacing={2} sx={{ padding: { xs: '20px 5px', sm: '20px 0 40px 20px' } }}>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>イベント区分</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.eventCategory}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>学校申込</Typography>
          {jobDetail.schoolCheck ? <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>✅</Typography> : ''}
        </Grid>
        <Grid item xs={4}></Grid>

        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>会社名</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.company}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>場所区分</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.locationType}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>場所</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.location}</Typography>
        </Grid>

        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>開始時間</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.startTime}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>終了時間</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.finishTime}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>遅刻・欠席・早退</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.tardinessAbsenceType}</Typography>
        </Grid>
        <Grid item xs={8} sm={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>報告内容</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.reportContent}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>結果</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.result}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>遅刻・早退時刻</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.tardyLeaveTime}</Typography>
        </Grid>
        <Grid item xs={8} sm={8}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>備考</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}> {jobDetail.remarks}</Typography>
        </Grid>
      </Grid>
      <hr />
      <Grid container spacing={2} sx={{ padding: { xs: '20px 5px', sm: '20px 0 40px 20px' } }}>
        <Grid item xs={6}>
          <FormControl fullWidth>
            <FormLabel sx={{ fontSize: { xs: '13px', sm: '16px' } }}>対応人数</FormLabel>
            <Controller
              control={control}
              name="supportedCnt"
              rules={{ required: '対応人数を入力してください' }}
              render={({ field, fieldState }) => (
                <TextField
                  {...field}
                  type="number"
                  placeholder="対応人数(1など)"
                  size="small"
                  fullWidth
                  autoFocus
                  error={fieldState.error?.message !== undefined}
                  helperText={fieldState.error?.message}
                  onChange={(e) => {
                    const value = Math.max(1, Math.min(10, Number(e.target.value)));
                    field.onChange(value);
                  }}
                  sx={{
                    minWidth: 120,
                    '& .MuiInputBase-input': {
                      padding: '16px 14px !important',
                      fontSize: { xs: '13px', sm: '16px' },
                    },
                  }}
                  inputProps={{
                    min: 1,
                    max: 10,
                  }}
                />
              )}
            />
          </FormControl>
        </Grid>
        <Grid item xs={6}>
          <FormControl fullWidth>
            <FormLabel sx={{ fontSize: { xs: '13px', sm: '16px' } }}>対応職</FormLabel>
            <Controller
              control={control}
              name="jobTitle"
              rules={{ required: '対応職を入力してください' }}
              render={({ field, fieldState }) => (
                <TextField
                  {...field}
                  placeholder="対応職"
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
            <FormLabel sx={{ fontSize: { xs: '13px', sm: '16px' } }}>試験区分(何次試験)</FormLabel>
            <Controller
              control={control}
              name="examCategory"
              rules={{ required: '試験区分を入力してください' }}
              render={({ field, fieldState }) => (
                <TextField
                  {...field}
                  type="number"
                  placeholder="試験区分(1など)"
                  size="small"
                  fullWidth
                  autoFocus
                  error={fieldState.error?.message !== undefined}
                  helperText={fieldState.error?.message}
                  onChange={(e) => {
                    const value = Math.max(1, Math.min(10, Number(e.target.value)));
                    field.onChange(value);
                  }}
                  sx={{
                    minWidth: 120,
                    '& .MuiInputBase-input': {
                      padding: '16px 14px !important',
                      fontSize: { xs: '13px', sm: '16px' },
                    },
                  }}
                  inputProps={{
                    min: 1,
                    max: 10,
                  }}
                />
              )}
            />
          </FormControl>
        </Grid>
        <Grid item xs={6}>
          <FormControl fullWidth>
            <FormLabel sx={{ fontSize: { xs: '13px', sm: '16px' } }}>試験内容</FormLabel>
            <Controller
              control={control}
              name="examContentId"
              rules={{ required: '試験内容を選択してください' }}
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
                      試験内容
                    </MenuItem>
                    <MenuItem value="適性検査">適性検査</MenuItem>
                    <MenuItem value="筆記・作文">筆記・作文</MenuItem>
                    <MenuItem value="ディスカッション">ディスカッション</MenuItem>
                    <MenuItem value="グループワーク">グループワーク</MenuItem>
                    <MenuItem value="個人面接">個人面接</MenuItem>
                    <MenuItem value="集団面接">集団面接</MenuItem>
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
        <Grid item xs={12}>
          <FormControl fullWidth>
            <FormLabel sx={{ fontSize: { xs: '13px', sm: '16px' } }}>受験内容</FormLabel>
            <Controller
              control={control}
              name="examinationContent"
              rules={{ required: '受験内容を入力してください' }}
              render={({ field, fieldState }) => (
                <TextField
                  {...field}
                  size="small"
                  placeholder="受験内容"
                  fullWidth
                  multiline
                  rows={4}
                  error={fieldState.error?.message !== undefined}
                  helperText={fieldState.error?.message}
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
        <Grid item xs={12}>
          <FormControl fullWidth>
            <FormLabel sx={{ fontSize: { xs: '13px', sm: '16px' } }}>感想</FormLabel>
            <Controller
              control={control}
              name="thoughts"
              rules={{ required: '感想を入力してください' }}
              render={({ field, fieldState }) => (
                <TextField
                  {...field}
                  size="small"
                  placeholder="感想"
                  fullWidth
                  multiline
                  rows={4}
                  error={fieldState.error?.message !== undefined}
                  helperText={fieldState.error?.message}
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
            onClick={handleGoToJobAdd}
            variant="contained"
            disabled={!formState.isValid}
            color="primary"
            style={{ marginRight: '1rem' }}
          >
            報告
          </Button>
        )}
      </Box>
    </Container>
  );
};
