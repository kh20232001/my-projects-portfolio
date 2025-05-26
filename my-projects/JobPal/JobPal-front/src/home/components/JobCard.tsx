import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import { useTheme } from '@mui/material/styles';
import useMediaQuery from '@mui/material/useMediaQuery';
import { FC, MouseEventHandler, memo } from 'react';

import { useLoginUser } from '../../_wheel/security/LoginUserProvider';
import { Authority } from '../../common/types/AuthorityTypes';

interface JobCardProps {
  userName: string;
  jobStateId: string;
  company: string;
  eventCategory: string;
  result: string;
  date: string;
  startTime: string;
  finishTime: string;
  reNotifyFlag: boolean;
  handle: MouseEventHandler<HTMLButtonElement> | undefined;
}

// JobCardコンポーネントを定義
export const JobCard: FC<JobCardProps> = memo((props: JobCardProps) => {
  const { getAuthority } = useLoginUser();
  const theme = useTheme();
  const isSmallScreen = useMediaQuery(theme.breakpoints.down('sm'));

  const { userName, jobStateId, company, eventCategory, result, date, startTime, finishTime, reNotifyFlag, handle } =
    props;

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
          <Grid container spacing={2} sx={{ height: '70px' }}>
            <Grid item xs={2} sm={1} container alignItems="flex-end" sx={{ padding: '0 0 4px 2px' }}>
              <Typography variant="h6">{date}</Typography>
            </Grid>
            <Grid item xs={4.3} sm={3} container alignItems="center" sx={{ paddingLeft: '25px!important' }}>
              <div>
                <Typography variant="body1" gutterBottom>
                  <span style={{ fontSize: '14px' }}>{startTime} </span> &gt;{' '}
                  <span style={{ fontSize: '12px' }}>{finishTime} </span>
                </Typography>
                <Typography variant="body1">{eventCategory}</Typography>
              </div>
            </Grid>
            <Grid item xs={5.7} sm={5} container alignItems="center">
              <div>
                {getAuthority() !== Authority.user && (
                  <Typography variant="h5" sx={{ fontSize: { xs: '16px', sm: '20px' } }}>
                    {userName}
                  </Typography>
                )}
                <Typography variant="h5" sx={{ fontSize: { xs: '16px', sm: '20px' } }}>
                  {company}
                </Typography>
              </div>
            </Grid>
            {!isSmallScreen && (
              <Grid item sm={2} container alignItems="center">
                <Typography variant="body1">{result}</Typography>
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
        <Typography variant="body1">{jobStateId}</Typography>
      </Card>
    </div>
  );
});
