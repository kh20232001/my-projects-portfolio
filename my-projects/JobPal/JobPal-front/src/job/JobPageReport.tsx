import { Box, Button, Container, Grid, MenuItem, Select, TextField, Typography } from '@mui/material';
import FormControl from '@mui/material/FormControl';
import FormLabel from '@mui/material/FormLabel';
import { FC, useState } from 'react';
import { Controller, useForm } from 'react-hook-form';
import { useLocation, useNavigate } from 'react-router-dom';

import { useLoginUser } from '../_wheel/security/LoginUserProvider';
import { Authority } from '../common/types/AuthorityTypes';
import { ReportPageParamsType } from './JobTypesForManage';
import { useJobApi } from './useJobApi';

export const JobPageReport: FC = () => {
  // ページ遷移とAPI呼び出しのためのフック
  const location = useLocation();
  const navigate = useNavigate();
  const { reportJob } = useJobApi();
  // ログインユーザー情報取得
  const { getAuthority } = useLoginUser();

  // ジョブの詳細情報を保持するステート
  const [jobDetail, setJobDetail] = useState(location.state.jobDetail);

  // フォームの状態管理
  const { control, getValues, formState } = useForm<ReportPageParamsType>({
    defaultValues: {
      jobHuntId: jobDetail.jobHuntId,
      reportContent: jobDetail.reportContent,
      result: jobDetail.result === null ? '' : jobDetail.result,
    },
    mode: 'onChange',
  });

  // 報告処理を実行する関数
  const handleGoToJobReport = async () => {
    const result = window.confirm('報告しますか？');
    if (result) {
      await reportJob(getValues());
      navigate(`/homedetail/${jobDetail.jobHuntId}`, { state: { id: jobDetail.jobHuntId } });
    }
  };

  // ジョブの詳細情報が存在する場合に表示するコンテンツ
  return jobDetail ? (
    <Container>
      <Typography gutterBottom sx={{ padding: { xs: '8px 0', sm: '16px 0' }, fontSize: { xs: '18px', sm: '20px' } }}>
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
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>場所</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.place}</Typography>
        </Grid>
        <Grid item xs={4}></Grid>

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
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.attendanceStatus}</Typography>
        </Grid>
        <Grid item xs={8} sm={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>報告内容</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.reportContent}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>結果</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.result}</Typography>
        </Grid>
      </Grid>
      <hr />
      <Grid container spacing={2} sx={{ padding: { xs: '20px 5px', sm: '20px 0 40px 20px' } }}>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>対応人数</Typography>
          {jobDetail.cnt !== null ? (
            <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.cnt}人</Typography>
          ) : null}
        </Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>対応職</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.jobTitle}</Typography>
        </Grid>
        <Grid item xs={4}></Grid>

        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>試験区分</Typography>
          {jobDetail.examCategory !== null ? (
            <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.examCategory}次試験</Typography>
          ) : null}
        </Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>試験内容</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.examContent}</Typography>
        </Grid>
        <Grid item xs={4}></Grid>

        <Grid item xs={8} sm={5}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>受験内容</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.ecaminationContent}</Typography>
        </Grid>
        <Grid item xs={4} sm={7}></Grid>

        <Grid item xs={8} sm={5}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>感想</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.thoughts}</Typography>
        </Grid>
        <Grid item xs={4} sm={7}></Grid>
      </Grid>
      <hr />
      <Grid container spacing={2} sx={{ padding: { xs: '20px 5px', sm: '20px 0 20px 20px' } }}>
        <Grid item xs={12}>
          <FormControl fullWidth>
            <FormLabel sx={{ fontSize: { xs: '13px', sm: '16px' } }}>報告内容</FormLabel>
            <Controller
              control={control}
              name="reportContent"
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
        <Grid item xs={12}>
          <FormControl fullWidth>
            <FormLabel sx={{ fontSize: { xs: '13px', sm: '16px' } }}>結果</FormLabel>
            <Controller
              control={control}
              name="result"
              rules={{ required: '結果を選択してください' }}
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
                      結果
                    </MenuItem>
                    <MenuItem value="結果待ち">結果待ち</MenuItem>
                    <MenuItem value="辞退">辞退</MenuItem>
                    <MenuItem value="継続（合格）">継続（合格）</MenuItem>
                    <MenuItem value="不合格">不合格</MenuItem>
                    <MenuItem value="内定">内定</MenuItem>
                    <MenuItem value="内定承諾">内定承諾</MenuItem>
                    <MenuItem value="内定辞退">内定辞退</MenuItem>
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
      <Box display="flex" justifyContent="center" mt={3}>
        {getAuthority() === Authority.user && (
          <Button
            onClick={handleGoToJobReport}
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
  ) : null;
};
