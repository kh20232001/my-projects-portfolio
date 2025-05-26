import SearchIcon from '@mui/icons-material/Search';
import { IconButton, InputAdornment } from '@mui/material';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Container from '@mui/material/Container';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { DataGrid, GridColDef, GridEventListener } from '@mui/x-data-grid';
import { FC, useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { useLoginUser } from '../_wheel/security/LoginUserProvider';
import { Authority } from '../common/types/AuthorityTypes';
import type { UserType, UserTypeForView } from '../common/types/UserTypes';
import { useUserApi } from './useUserApi';

const columns: GridColDef[] = [
  {
    field: 'id',
    headerName: 'ID',
    width: 50,
  },
  {
    field: 'status',
    headerName: '状態',
    width: 50,
  },
  {
    field: 'authority',
    headerName: '権限',
    width: 80,
  },
  {
    field: 'userName',
    headerName: 'ユーザ名',
    width: 120,
  },
  {
    field: 'userClass',
    headerName: 'クラス',
    width: 100,
  },
  {
    field: 'classNumber',
    headerName: '出席番号',
    width: 100,
  },
  {
    field: 'schoolNumber',
    headerName: '学籍番号',
    width: 100,
  },
  {
    field: 'userId',
    headerName: 'ユーザID',
    width: 200,
  },
];

export const UserPage: FC = () => {
  const navigate = useNavigate();
  const { getUsers } = useUserApi();
  const { getAuthority, getUserId } = useLoginUser();

  const [users, setUsers] = useState<Array<UserType>>([]);
  const [keyword, setKeyword] = useState<string>('');
  const [filteredUsers, setFilteredUsers] = useState<Array<UserTypeForView>>([]);

  useEffect(() => {
    if (getAuthority() !== Authority.user) {
      getUsers(setUsers);
    }
  }, []);

  const viewUsers = useMemo<Array<UserTypeForView>>(() => {
    const viewUsers: Array<UserTypeForView> = [];
    users.map((value, index) => {
      const view = { ...value } as UserTypeForView;
      view.id = index + 1;
      if (view.status === 'VALID') {
        view.status = '有効';
      } else if (view.status === 'INVALID') {
        view.status = '無効';
      } else if (view.status === 'LOCKED') {
        view.status = 'ロック';
      } else if (view.status === 'GRADUATION') {
        view.status = '卒業';
      } else if (view.status === 'DELETE') {
        view.status = '削除';
      }
      if (view.grant === '0') {
        view.authority = '学生';
      } else if (view.grant === '1') {
        view.authority = '担任';
      } else if (view.grant === '2') {
        view.authority = '管理者';
      }else if (view.grant === '3') {
        view.authority = '事務';
      }
      viewUsers.push(view);
    });
    return viewUsers;
  }, [users]);

  useEffect(() => {
    setFilteredUsers(viewUsers);
  }, [viewUsers]);

  const handleGoToHome = () => navigate('/home');

  const handleGoToUserAdd = () => navigate('/user/add');

  // 行クリック時の遷移処理
  const handleRowClick: GridEventListener<'rowClick'> = (params) => {
    navigate(`/user/${params.row.userId}`, { state: params.row });
  };

  // 検索キーワード変更時の処理
  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setKeyword(event.target.value);
  };

  // ユーザ情報のフィルタリング処理
  const handleFilterUser = () => {
    const filtered = viewUsers.filter((user) =>
      Object.values(user).some(
        (value) => value != null && value.toString().toLowerCase().includes(keyword.toLowerCase())
      )
    );
    setFilteredUsers(filtered);
  };

  // コンポーネントのレンダリング部分
  return (
    <Container component="main" maxWidth="md" sx={{ padding: { xs: '0px', sm: '16px 0' } }}>
      <Typography
        variant="subtitle1"
        sx={{
          marginTop: 2,
          marginBottom: 2,
          cursor: 'pointer',
          '&:hover': { color: '#707070' },
        }}
        onClick={handleGoToHome}
      >
        {'<< ホームへ'}
      </Typography>

      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Typography component="h1" variant="h4">
          ユーザ情報管理
        </Typography>
      </Box>
      <TextField
        variant="standard"
        placeholder="検索キーワード"
        fullWidth
        InputProps={{
          endAdornment: (
            <InputAdornment position="end">
              <IconButton onClick={handleFilterUser}>
                <SearchIcon />
              </IconButton>
            </InputAdornment>
          ),
        }}
        style={{ width: '100%' }}
        onChange={handleSearchChange}
      />
      <Box sx={{ marginTop: 3, width: '100%', maxWidth: { xs: '380px', sm: '100%' }, overflowX: 'auto' }}>
        <DataGrid
          rows={filteredUsers}
          columns={columns}
          autoHeight
          initialState={{
            pagination: {
              paginationModel: {
                pageSize: 5,
              },
            },
          }}
          pageSizeOptions={[5]}
          onRowClick={handleRowClick}
        />
        <Typography variant="subtitle2">※編集するユーザの行をクリック</Typography>
      </Box>
      {getAuthority() === Authority.admin && (
        <Button variant="contained" color="warning" sx={{ marginTop: 2, marginBottom: 2 }} onClick={handleGoToUserAdd}>
          追加
        </Button>
      )}
    </Container>
  );
};
