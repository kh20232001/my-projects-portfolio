import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import { FC, memo } from 'react';

import type { UserType } from '../types/UserTypes';
import { AuthorityChip } from './AuthorityChip';

type Props = {
  user: UserType;
  managed: boolean;
};

export const UserCard: FC<Props> = memo((props: Props) => {
  const { user, managed } = props;

  const cardTheme =
    managed && String(user.status) === 'false'
      ? {
          margin: '20px 0px',
          backgroundColor: '#cfcfcf',
        }
      : {
          margin: '20px 0px',
        };

  return (
    <Card sx={cardTheme}>
      <CardContent>
        <Grid container direction="row" justifyContent="space-between" alignItems="center">
          <Box>
            <Typography variant="h5">
              {user.userName}({user.userId})
            </Typography>
          </Box>
          {managed && <AuthorityChip authority={user.grant} />}
        </Grid>
        {managed && (
          <Box sx={{ padding: '0px 10px' }}>
            <Typography sx={{ marginTop: 1 }} variant="body1">
              状態:
              {user.status === 'VALID' && '有効'}
              {user.status === 'INVALID' && '無効'}
              {user.status === 'LOCKED' && 'ロック'}
              {user.status === 'GRADUATION' && '卒業'}
              {user.status === 'DELETE' && '削除'}
            </Typography>
          </Box>
        )}
      </CardContent>
    </Card>
  );
});
