import { Box, Button, Container, Grid, Typography } from '@mui/material';
import { FC, useEffect, useRef, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';



import { useLoginUser } from '../_wheel/security/LoginUserProvider';
import { Authority } from '../common/types/AuthorityTypes';
import { ChangeStateParamsType, DetailPageParamsType, PostalParamsType } from './CertificateTypes';
import { useCertificateApi } from './useCertificateApi';


export const Certificatedetail: FC = () => {
  // 必要なフックやカスタムフック
  const { getAuthority, getUserId } = useLoginUser();
  const navigate = useNavigate();
  const location = useLocation();
  const { getCertificateDetail, deleteCertificate, changeState, getPostal } = useCertificateApi();
  const isFirstRender = useRef(true);

  // ステート管理
  const [certificatedetail, setCertificatedetail] = useState<DetailPageParamsType>();
  const [buttonId, setButtonId] = useState(8);
  const [postal, setPostal] = useState<PostalParamsType>();

  // 証明書代と送料の計算
  let certificateFee = 0;
  let weight = 0;
  certificatedetail?.certificateList.forEach((cert, index) => {
    if (postal?.certificateList[index]) {
      certificateFee += postal.certificateList[index].fee * cert.count;
      weight += postal.certificateList[index].weight * cert.count;
    }
  });
  const postalFee = postal ? Math.ceil(weight / postal.postal.postalMaxWeight) * postal.postal.postalFee : 0;
  const shippingFee = certificatedetail?.mediaName === '郵送' ? postalFee : 0;
  const totalAmount = certificateFee + shippingFee;

  // 状態変更用のパラメータ
  const getValue: ChangeStateParamsType = {
    certificateIssueId: certificatedetail?.certificateIssueId ?? '',
    certificateStateName: certificatedetail?.certificateStateName ?? '',
    mediaName: certificatedetail?.mediaName ?? '',
    buttonId: buttonId,
    userId: getUserId() ?? '',
  };

  // 初回レンダリング時に詳細データを取得
  useEffect(() => {
    getCertificateDetail(location.state.id, setCertificatedetail);
    getPostal(setPostal);
  }, []);

  // 状態変更が必要なときの処理
  useEffect(() => {
    if (isFirstRender.current) {
      isFirstRender.current = false;
      return;
    }
    handleGoToStateChange();
  }, [buttonId]);

  // 証明書ページへの遷移
  const handleGoToCertificatePage = () => {
    navigate(`/certificate/new`, { state: { certificatedetail: certificatedetail } });
  };

  // 証明書削除処理
  const handleGoToCertificateDelete = async () => {
    if (window.confirm('取り消しますか？')) {
      await deleteCertificate(location.state.id);
      navigate(`/certificate`);
    }
  };

  // ボタン操作で状態を変更
  const handleGoToButtonIdChange = (id: number): void => {
    const messages = [
      '承認しますか？',
      '取り下げますか？',
      '差し戻しますか？',
      '受領しますか？',
      '発行しますか？',
      '送信しますか？',
      '郵送しますか？',
      '完了しますか？',
    ];
    if (window.confirm(messages[id])) {
      setButtonId(id);
    }
  };

  // 状態変更の実行
  const handleGoToStateChange = async () => {
    await changeState(getValue);
    getCertificateDetail(location.state.id, setCertificatedetail);
    navigate(`/certificatedetail/${location.state.id}`, { state: { id: location.state.id } });
  };

  // ジョブの詳細情報が存在する場合に表示するコンテンツ
  return certificatedetail ? (
    <Container>
      <Typography gutterBottom sx={{ padding: { xs: '8px 0', sm: '16px 0' }, fontSize: { xs: '18px', sm: '20px' } }}>
        証明書詳細
      </Typography>
      <Grid container spacing={2} sx={{ padding: { xs: '20px 5px!important', sm: '20px 0 40px 20px' } }}>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>申請番号</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{certificatedetail.certificateIssueId}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>状態</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>
            {certificatedetail.certificateStateName}
          </Typography>
        </Grid>
        <Grid item xs={4}></Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>学籍番号</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{certificatedetail.schoolClassNumber}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>クラス番号</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>
            {certificatedetail.userClass}
            {certificatedetail.classNumber}
          </Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>名前</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{certificatedetail.userName}</Typography>
        </Grid>
      </Grid>
      <hr />
      <Grid container spacing={2} sx={{ padding: { xs: '20px 5px!important', sm: '20px 0 40px 20px' } }}>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>媒体</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{certificatedetail.mediaName}</Typography>
        </Grid>
        <Grid item xs={4}></Grid>
        <Grid item xs={4}>
          <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>担当</Typography>
          <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{certificatedetail.officeUserName}</Typography>
        </Grid>

        <Grid item xs={8}>
          <Grid container spacing={2} sx={{ paddingBottom: { xs: '30px' } }}>
            <Grid item xs={5.5}>
              <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>
                在学証明書{certificatedetail.certificateList[0].count}枚
              </Typography>
            </Grid>
            <Grid item xs={5.5}>
              <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>
                成績証明書{certificatedetail.certificateList[1].count}枚
              </Typography>
            </Grid>
          </Grid>
          <Grid container spacing={2} sx={{ paddingBottom: { xs: '30px' } }}>
            <Grid item xs={5.5}>
              <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>
                卒業見込み証明書{certificatedetail.certificateList[2].count}枚
              </Typography>
            </Grid>
            <Grid item xs={5.5}>
              <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>
                健康診断証明書{certificatedetail.certificateList[3].count}枚
              </Typography>
            </Grid>
          </Grid>
        </Grid>
        <Grid item xs={4}>
          <Box width="150px">
            {['証明書代', '送料', '計'].map((label, index) => (
              <Box
                key={label}
                display="flex"
                justifyContent="space-between"
                borderBottom={index === 2 ? 'solid 1px' : 'none'}
                marginBottom={index === 1 ? '10px' : '5px'}
              >
                <Typography>{label}</Typography>
                <Typography>
                  {index === 0 ? `${certificateFee}円` : index === 1 ? `${shippingFee}円` : `${totalAmount}円`}
                </Typography>
              </Box>
            ))}
          </Box>
        </Grid>
      </Grid>
      {certificatedetail.mediaName === '郵送' ? (
        <>
          <Grid container spacing={2} sx={{ padding: { xs: '20px 5px!important', sm: '20px 0 40px 20px' } }}>
            <Grid item xs={5}>
              <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>宛先人</Typography>
              <Grid container spacing={2} sx={{ paddingBottom: { xs: '30px' } }}>
                <Grid item xs={5.5}>
                  <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>姓</Typography>
                  <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{certificatedetail.lastName}</Typography>
                </Grid>
                <Grid item xs={5.5}>
                  <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>名</Typography>
                  <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{certificatedetail.firstName}</Typography>
                </Grid>
              </Grid>
              <Grid container spacing={2} sx={{ paddingBottom: { xs: '30px' } }}>
                <Grid item xs={5.5}>
                  <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>セイ</Typography>
                  <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>
                    {certificatedetail.lastNameKana}
                  </Typography>
                </Grid>
                <Grid item xs={5.5}>
                  <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>メイ</Typography>
                  <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>
                    {certificatedetail.firstNameKana}
                  </Typography>
                </Grid>
              </Grid>
            </Grid>
          </Grid>
          <Grid container spacing={2} sx={{ padding: { xs: '20px 5px!important', sm: '20px 0 40px 20px' } }}>
            <Grid item xs={4}>
              <Grid container spacing={2} sx={{ paddingBottom: { xs: '10px' } }}>
                <Grid item xs={5.5}>
                  <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>住所</Typography>
                </Grid>
              </Grid>
              <Grid container spacing={2} sx={{ paddingBottom: { xs: '30px' } }}>
                <Grid item xs={12}>
                  <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{certificatedetail.zipCode}</Typography>
                </Grid>
              </Grid>
              <Grid container spacing={2} sx={{ paddingBottom: { xs: '30px' } }}>
                <Grid item xs={12}>
                  <Typography sx={{ fontSize: { xs: '13px', sm: '15px' }, paddingBottom: { xs: '10px' } }}>
                    {certificatedetail.address}
                  </Typography>
                  <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>
                    {certificatedetail.afterAddress}
                  </Typography>
                </Grid>
              </Grid>
            </Grid>
          </Grid>
        </>
      ) : null}

      <hr />

      <Box display="flex" justifyContent="center" mt={3}>
        {getAuthority() === Authority.user && certificatedetail.certificateStateName.includes('承認待ち') ? (
          <>
            <Button onClick={() => handleGoToButtonIdChange(1)} variant="contained" color="primary">
              取り下げ
            </Button>
          </>
        ) : getAuthority() === Authority.user && certificatedetail.certificateStateName.includes('差戻し') ? (
          <>
            <Button
              onClick={() => handleGoToCertificatePage()}
              variant="contained"
              color="primary"
              style={{ marginRight: '1rem' }}
            >
              変更
            </Button>
            <Button onClick={() => handleGoToCertificateDelete()} variant="contained" color="primary">
              取り消し
            </Button>
          </>
        ) : null}
        {getAuthority() === Authority.teacher && certificatedetail.certificateStateName.includes('承認待ち') ? (
          <>
            <Button
              onClick={() => handleGoToButtonIdChange(0)}
              variant="contained"
              color="primary"
              style={{ marginRight: '1rem' }}
            >
              承認
            </Button>
            <Button onClick={() => handleGoToButtonIdChange(2)} variant="contained" color="primary">
              差戻し
            </Button>
          </>
        ) : null}
        {getAuthority() === Authority.clerk && certificatedetail.certificateStateName.includes('支払待ち') ? (
          <>
            <Button onClick={() => handleGoToButtonIdChange(3)} variant="contained" color="primary">
              受領
            </Button>
          </>
        ) : getAuthority() === Authority.clerk && certificatedetail.certificateStateName.includes('発行待ち') ? (
          <>
            <Button onClick={() => handleGoToButtonIdChange(4)} variant="contained" color="primary">
              発行
            </Button>
          </>
        ) : getAuthority() === Authority.clerk && certificatedetail.certificateStateName.includes('発行済み') ? (
          <>
            {certificatedetail.mediaName === '電子' ? (
              <Button onClick={() => handleGoToButtonIdChange(5)} variant="contained" color="primary">
                送信
              </Button>
            ) : (
              <Button onClick={() => handleGoToButtonIdChange(6)} variant="contained" color="primary">
                郵送
              </Button>
            )}
          </>
        ) : getAuthority() === Authority.clerk && certificatedetail.certificateStateName.includes('受取待ち') ? (
          <>
            <Button onClick={() => handleGoToButtonIdChange(7)} variant="contained" color="primary">
              完了
            </Button>
          </>
        ) : null}
      </Box>
    </Container>
  ) : null;
};
