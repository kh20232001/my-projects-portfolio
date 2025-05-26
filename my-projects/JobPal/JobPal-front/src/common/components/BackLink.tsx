import Typography from '@mui/material/Typography';
import { FC, memo } from 'react';
import { useNavigate } from 'react-router-dom';

export const BackLink: FC = memo(() => {
  const navigate = useNavigate();

  const handleBack = () => navigate(-1);

  return (
    <Typography
      variant="subtitle1"
      sx={{
        marginBottom: 2,
        cursor: 'pointer',
        '&:hover': { color: '#707070' },
      }}
      onClick={handleBack}
    >
      {'<< 戻る'}
    </Typography>
  );
});
