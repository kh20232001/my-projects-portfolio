import { Box, Button, Checkbox, Container, FormControlLabel, Grid, Typography } from '@mui/material';
import { FC, useEffect, useRef, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';



import { useLoginUser } from '../_wheel/security/LoginUserProvider';
import { Authority } from '../common/types/AuthorityTypes';
import { ChangeStateParamsType, DetailPageParamsType } from './JobTypesForManage';
import { useJobApi } from './useJobApi';


export const JobPageDetail: FC = () => {
  // ログインユーザー情報を取得するためのフック
  const { getAuthority } = useLoginUser();
  // ナビゲーションを扱うためのフック
  const navigate = useNavigate();
  // 現在のロケーションを扱うためのフック
  const location = useLocation();
  // ジョブ関連のAPIを扱うためのカスタムフック
  const { getJobDetail, deleteJob, changeState } = useJobApi();
  // 初回レンダリングを判定するためのref
  const isFirstRender = useRef(true);

  // ジョブの詳細情報を保持するステート
  const [jobDetail, setJobDetail] = useState<DetailPageParamsType>({ schoolCheckedFlag: false } as DetailPageParamsType);
  // ボタンIDを保持するステート
  const [buttonId, setButtonId] = useState(4);

  // 状態変更時に使用するパラメータ
  const getValue: ChangeStateParamsType = {
    jobHuntId: jobDetail?.jobHuntId,
    jobStateId: jobDetail?.jobStateId,
    buttonId: buttonId,
    schoolCheck: jobDetail?.schoolCheck,
  };

  // コンポーネントマウント時にジョブの詳細情報を取得
  useEffect(() => {
    getJobDetail(location.state.id, setJobDetail);
  }, []);

  const handleGoToJobPage = () => {
    if (jobDetail?.jobStateId === '申請差戻し') {
      navigate(`/new`, { state: { jobDetail: jobDetail } });
    } else if (jobDetail?.jobStateId === '受験報告待ち' || jobDetail?.jobStateId === '受験報告差戻し') {
      navigate(`/homedetail/add`, { state: { jobDetail: jobDetail } });
    } else {
      navigate(`/homedetail/report`, { state: { jobDetail: jobDetail } });
    }
  };

  const handleCheckboxChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (jobDetail) {
      setJobDetail({ ...jobDetail, schoolCheckedFlag: event.target.checked });
    }
  };

  // ジョブ削除処理を扱う関数
  const handleGoToJobDelete = async () => {
    const result = window.confirm('取り消しますか？');
    if (result) {
      await deleteJob(location.state.id);
      navigate(`/home`);
    }
  };

  // buttonIdの状態を更新する関数
  const handleGoToButtonIdChange = (id: number): void => {

    const messages = ['承認しますか？', '取り下げますか？', '差し戻しますか？', '承認しますか？'];
    const result = window.confirm(messages[id]);
    if (result) {
      setButtonId(id);
    }
  };

  // buttonIdが変更された時に状態変更処理を実行
  useEffect(() => {
    if (isFirstRender.current) {
      isFirstRender.current = false;
      return;
    }
    handleGoToStateChange();
  }, [buttonId]);

  // ジョブの状態変更処理を扱う関数
  const handleGoToStateChange = async () => {
    await changeState(getValue);
    getJobDetail(location.state.id, setJobDetail);
    navigate(`/homedetail/${location.state.id}` , { state: { id: location.state.id } });
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
        <Grid item xs={4}>
          {getAuthority() === Authority.teacher &&
          jobDetail.jobStateId === '担任承認待ち' &&
          jobDetail.schoolCheck === true ? (
            <FormControlLabel
              control={<Checkbox checked={jobDetail.schoolCheckedFlag} onChange={handleCheckboxChange} size='small'/>}
              label={<Typography sx={{ fontSize: { xs: '10px', sm: '11px' } }}>名簿チェック</Typography>}
            />
          ) : getAuthority() === Authority.teacher ? (
            <>
              <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>名簿</Typography>
              {jobDetail.schoolCheckedFlag ? (
                <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>✅</Typography>
              ) : (
                ''
              )}
            </>
          ) : null}
        </Grid>

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
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>備考</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}> {jobDetail.remarks}</Typography>
        </Grid>
        {getAuthority() !== Authority.user && (
          <Grid item xs={4}>
            <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>予測結果</Typography>
            <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}> {jobDetail.predResult}</Typography>
          </Grid>
        )}
      </Grid>
      <hr />
      <Grid container spacing={2} sx={{ padding: { xs: '20px 5px', sm: '20px 0 40px 20px' } }}>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>対応人数</Typography>
          {jobDetail.supportedCnt !== null ? (
            <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{jobDetail.supportedCnt}人</Typography>
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
      <Box display="flex" justifyContent="center" mt={3}>
        {getAuthority() === Authority.user &&
        (jobDetail.jobStateId?.includes('承認待ち') ||
          jobDetail.jobStateId?.includes('報告待ち') ||
          jobDetail.jobStateId === '完了') ? (
          <>
            {jobDetail.jobStateId?.includes('報告待ち') && (
              <Button
                onClick={() => handleGoToJobPage()}
                variant="contained"
                color="primary"
                style={{ marginRight: '1rem' }}
              >
                報告
              </Button>
            )}
            {jobDetail.jobStateId === 'コース担当承認待ち' && (
              <Button
                onClick={() => handleGoToButtonIdChange(3)}
                variant="contained"
                color="primary"
                style={{ marginRight: '1rem' }}
              >
                コース担当
              </Button>
            )}
            <Button onClick={() => handleGoToButtonIdChange(1)} variant="contained" color="primary">
              取り下げ
            </Button>
          </>
        ) : getAuthority() === Authority.user && jobDetail.jobStateId?.includes('差戻し') ? (
          <>
            <Button
              onClick={() => handleGoToJobPage()}
              variant="contained"
              color="primary"
              style={{ marginRight: '1rem' }}
            >
              変更
            </Button>
            <Button onClick={() => handleGoToJobDelete()} variant="contained" color="primary">
              取り消し
            </Button>
          </>
        ) : null}
        {getAuthority() === Authority.teacher &&
        jobDetail.jobStateId?.includes('承認待ち') &&
        jobDetail.jobStateId !== 'コース担当承認待ち' ? (
          <>
            <Button
              onClick={() => handleGoToButtonIdChange(0)}
              variant="contained"
              color="primary"
              style={{ marginRight: '1rem' }}
              disabled={
                (jobDetail.schoolCheck && !jobDetail.schoolCheckedFlag) ||
                (jobDetail.schoolCheck && jobDetail.schoolCheckedFlag === null)
              }
            >
              承認
            </Button>
            <Button onClick={() => handleGoToButtonIdChange(2)} variant="contained" color="primary">
              差戻し
            </Button>
          </>
        ) : getAuthority() === Authority.teacher && jobDetail.jobStateId === 'コース担当承認待ち' ? (
          <>
            <Button onClick={() => handleGoToButtonIdChange(3)} variant="contained" color="primary">
              コース担当
            </Button>
          </>
        ) : null}
      </Box>
    </Container>
  ) : null;
};
