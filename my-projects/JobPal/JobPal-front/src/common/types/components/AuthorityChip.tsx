import Chip from '@mui/material/Chip';
import { FC } from 'react';

import { Authority, AuthorityType } from '../types/AuthorityTypes';

type Props = {
  authority: AuthorityType;
};

export const AuthorityChip: FC<Props> = (props: Props) => {
  const { authority } = props;

  switch (authority) {
    case Authority.admin:
      return <Chip label="管理者" color="error" />;
    case Authority.user:
      return <Chip label="ユーザ" color="success" />;
    case Authority.teacher:
      return <Chip label="担任" color="primary" />;
    default:
      return <Chip label="none" color="error" />;
  }
};
