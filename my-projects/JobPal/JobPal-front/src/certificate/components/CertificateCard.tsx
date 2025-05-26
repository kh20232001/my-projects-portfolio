import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import { useTheme } from '@mui/material/styles';
import useMediaQuery from '@mui/material/useMediaQuery';
import { FC, MouseEventHandler, memo } from 'react';



import { useLoginUser } from '../../_wheel/security/LoginUserProvider';
import { Authority } from '../../common/types/AuthorityTypes';


interface CertificateCardProps {
  certificateStateName: string;
  date: string;
  certificateList: {
    certificateId: string;
    certificateName: string;
    count: number;
  }[];
  totalAmount: string;
  mediaName: string;
  userName: string;
  reNotifyFlag: boolean;
  handle: MouseEventHandler<HTMLButtonElement> | undefined;
}

// CertificateCardコンポーネントを定義
export const CertificateCard: FC<CertificateCardProps> = memo((props: CertificateCardProps) => {
  const { getAuthority } = useLoginUser();
  const theme = useTheme();
  const isSmallScreen = useMediaQuery(theme.breakpoints.down('sm'));

  const {
    certificateStateName,
    date,
    certificateList,
    totalAmount,
    mediaName,
    userName,
    reNotifyFlag,
    handle,
  } = props;

  // カードがクリックされたときのハンドラー
  const handleClick = () => {
    if (handle) {
      handle({} as React.MouseEvent<HTMLButtonElement>);
    }
  };

  return (
    <div style={{ position: 'relative' }}>
      <Card
        onClick={handleClick}
        sx={{
          cursor: 'pointer',
          marginBottom: '16px',
          boxShadow: '4px 4px 8px rgba(0, 0, 0, 0.25)',
          backgroundColor: reNotifyFlag ? 'rgba(178,34,34,0.25)' : '#fff',
          '&:hover': {
            backgroundColor: reNotifyFlag ? 'rgba(178,34,34,0.3)' : '#f0f0f0',
          },
        }}
      >
        <CardContent style={{ padding: '16px' }}>
          <Grid container spacing={2} sx={{height:'70px'}}>
            <Grid item xs={2} sm={1} container alignItems="flex-end" sx={{ padding: '0 0 4px 2px' }}>
              <Typography variant="h6">{date}</Typography>
            </Grid>
            <Grid item xs={7} sm={3} container alignItems="center" sx={{ paddingLeft: '25px!important' }}>
              <div>
                {certificateList?.length > 0 && (
                  <Typography variant="body1" gutterBottom>
                    <span style={{ fontSize: '14px' }}>
                      {certificateList[0]?.certificateName} {certificateList[0]?.count}枚
                    </span>
                    {'　'}
                    <span style={{ fontSize: '14px' }}>
                      {certificateList[1] && `${certificateList[1].certificateName} ${certificateList[1].count}枚`}
                    </span>
                  </Typography>
                )}
                {certificateList?.length > 2 && (
                  <Typography variant="body1">
                    <span style={{ fontSize: '14px' }}>
                      {certificateList[2]?.certificateName} {certificateList[2]?.count}枚
                    </span>
                    {'　'}
                    <span style={{ fontSize: '14px' }}>
                      {certificateList[3] && `${certificateList[3].certificateName} ${certificateList[3].count}枚`}
                    </span>
                  </Typography>
                )}
              </div>
            </Grid>
            <Grid item xs={3} sm={2} container alignItems="center">
              <div>
                {getAuthority() !== Authority.user && (
                  <Typography variant="h5" sx={{ fontSize: { xs: '16px', sm: '20px' } }}>
                    {userName}
                  </Typography>
                )}
                <Typography variant="h5" sx={{ fontSize: { xs: '16px', sm: '20px' } }}>
                  {totalAmount}円
                </Typography>
              </div>
            </Grid>
            {!isSmallScreen && (
              <Grid item sm={2} container alignItems="center">
                <Typography variant="body1">{mediaName}</Typography>
              </Grid>
            )}
          </Grid>
        </CardContent>
      </Card>
      <Card
        sx={{
          position: 'absolute',
          top: -8,
          left: -8,
          backgroundColor: 'red',
          color: 'white',
          padding: '4px 10px',
          borderRadius: '0px',
          boxShadow: '3px 3px 3px rgba(0, 0, 0, 0.25)',
        }}
      >
        <Typography variant="body1">{certificateStateName}</Typography>
      </Card>
    </div>
  );
});
