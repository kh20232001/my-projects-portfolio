import ArrowDownwardIcon from '@mui/icons-material/ArrowDownward';
import ArrowUpwardIcon from '@mui/icons-material/ArrowUpward';
import SearchIcon from '@mui/icons-material/Search';
import { Box, Button, IconButton, InputAdornment, MenuItem, Pagination, Select } from '@mui/material';
import { SelectChangeEvent } from '@mui/material';
import Alert from '@mui/material/Alert';
import Grid from '@mui/material/Grid';
import Switch from '@mui/material/Switch';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { FC, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { useLoginUser } from '../_wheel/security/LoginUserProvider';
import { Authority } from '../common/types/AuthorityTypes';
import type { HomeParamsType } from '../home/HomeParamsType';
import { CertificateType } from './CertificateTypes';
import { CertificateCard } from './components/CertificateCard';
import { useCertificateApi } from './useCertificateApi';

export const CertificatePage: FC = () => {
  const navigate = useNavigate();
  const { getAuthority, getUserId } = useLoginUser();
  const { getCertificate } = useCertificateApi();

  // 状態管理
  const [certificate, setCertificate] = useState<Array<CertificateType>>([]);
  const [sortOrder, setSortOrder] = useState('');
  const [ascending, setAscending] = useState<boolean>(true);
  const [filteredCertificate, setFilteredCertificate] = useState(
    certificate.filter((cert) => cert.certificateStateName !== '完了')
  );
  const [alertCount, setAlertCount] = useState(0);
  const [userName, setUserName] = useState('');
  const [isChecked, setIsChecked] = useState(true);
  const [keyword, setKeyword] = useState<string>('');
  const [currentPage, setCurrentPage] = useState(1);
  const PerPage = 10;

  // ユーザー情報取得
  const getValues: HomeParamsType = {
    userId: getUserId(),
    grant: getAuthority(),
  };

  // 初期データ取得

  useEffect(() => {
    getCertificate(getValues, setCertificate, setAlertCount, setUserName);
  }, []);

  // ソート順変更時の処理
  useEffect(() => {
    handleSortChange();
  }, [sortOrder, ascending]);

  // ジョブリスト更新時の処理
  useEffect(() => {
    setFilteredCertificate(certificate.filter((cert) => cert.certificateStateName !== '完了'));
  }, [certificate]);

  // 昇順/降順切り替え
  const handleOrderChange = () => {
    setAscending(!ascending);
  };

  // ソート順選択
  const handleSortOrderChange = (event: SelectChangeEvent<string>) => {
    setSortOrder(event.target.value);
  };

  // ソート処理
  const handleSortChange = () => {
    const sortedCertificate = [...filteredCertificate].sort((a: any, b: any) => {
      if (a[sortOrder] < b[sortOrder]) return ascending ? -1 : 1;
      if (a[sortOrder] > b[sortOrder]) return ascending ? 1 : -1;
      return 0;
    });
    setFilteredCertificate(sortedCertificate);
  };

  // 進行中のみ表示切り替え
  const handleSwitchChange = () => {
    setIsChecked(!isChecked);
    if (!isChecked) {
      setFilteredCertificate(certificate.filter((cert) => cert.certificateStateName !== '完了'));
    } else {
      setFilteredCertificate(certificate);
    }
  };

  // ジョブ詳細ページへの遷移
  const handleGoToCert = (id: string) => {
    navigate(`/certificatedetail/${id}`, { state: { id } });
  };

  // アラートページへの遷移
  const handleGoToAlert = () => navigate('/alert');

  // 検索キーワード変更
  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setKeyword(event.target.value);
  };

  // ジョブフィルタリング
  const handleFilterJobs = () => {
    if (!keyword) return setFilteredCertificate(certificate);

    const filteredCert = filteredCertificate.filter((cert) =>
      Object.values(cert)
        .filter((value) => value !== null && value !== undefined)
        .some((value) => value.toString().toLowerCase().includes(keyword.toLowerCase()))
    );
    setFilteredCertificate(filteredCert);
  };

  // ページ変更
  const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setCurrentPage(value);
  };

  // ページネーション処理
  const paginatedCertificate = filteredCertificate.slice((currentPage - 1) * PerPage, currentPage * PerPage);
  // レンダリング
  return getUserId() ? (
    <>
      <Grid container spacing={2} alignItems="center">
        <Grid item xs={12} sm={12}>
          <Typography
            sx={{
              margin: '16px 0 32px',
              fontSize: {
                xs: '1.5rem',
                sm: '2rem',
              },
            }}
          >
            {userName}の証明書ダッシュボード
          </Typography>
        </Grid>
      </Grid>

      {alertCount !== 0 && (
        <Alert color="error" style={{ marginBottom: '16px' }} onClick={handleGoToAlert}>
          確認が必要な申請が{alertCount}件あります。
        </Alert>
      )}
      <Grid
        container
        spacing={2}
        alignItems="center"
        justifyContent="space-between"
        sx={{
          marginBottom: '40px',
          flexDirection: {
            xs: 'row',
            sm: 'row',
          },
        }}
      >
        <Grid item xs={5} sm={2} sx={{ order: { xs: 1, sm: 1 } }}>
          <Grid container alignItems="center">
            <Switch color="primary" checked={isChecked} onChange={handleSwitchChange} />
            <Typography variant="body2">進行中のみ</Typography>
          </Grid>
        </Grid>
        <Grid item xs={12} sm={7} sx={{ order: { xs: 2, sm: 2 } }}>
          <TextField
            variant="standard"
            placeholder="検索キーワード"
            fullWidth
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton onClick={handleFilterJobs}>
                    <SearchIcon />
                  </IconButton>
                </InputAdornment>
              ),
            }}
            style={{ width: '100%' }}
            onChange={handleSearchChange}
          />
        </Grid>
        <Grid
          item
          xs={7}
          sm={3}
          container
          alignItems="center"
          justifyContent="center"
          spacing={1}
          sx={{ order: { xs: 1, sm: 3 } }}
        >
          <Grid item>
            <Select
              value={sortOrder}
              onChange={handleSortOrderChange}
              displayEmpty
              variant="outlined"
              id="select"
              sx={{
                minWidth: 110,
                fontSize: '13px',
                '& .MuiSelect-select': {
                  padding: '7px 10px !important',
                },
              }}
            >
              <MenuItem value="" disabled>
                並び替え
              </MenuItem>
              <MenuItem value="stateIdPriority">状態</MenuItem>
              <MenuItem value="startTimePriority">開始日時</MenuItem>
            </Select>
          </Grid>
          <Grid item>
            <Button
              onClick={handleOrderChange}
              variant="outlined"
              color="inherit"
              sx={{ padding: '3px', minWidth: '50px' }}
            >
              {ascending ? <ArrowUpwardIcon /> : <ArrowDownwardIcon />}
            </Button>
          </Grid>
        </Grid>
      </Grid>
      <Grid container spacing={2}>
        {paginatedCertificate.map((cert, index) => (
          <Grid item xs={12} key={index}>
            <CertificateCard {...cert} handle={() => handleGoToCert(cert.certificateIssueId)} />
          </Grid>
        ))}
      </Grid>
      <Grid container justifyContent="space-between" sx={{ marginTop: '20px' }}>
        {getAuthority() === Authority.user ? (
          <Button
            onClick={() => navigate('/certificate/new')}
            variant="contained"
            color="primary"
            style={{ marginRight: '1rem' }}
          >
            発行
          </Button>
        ) : (
          <Box></Box>
        )}
        <Pagination
          count={Math.ceil(filteredCertificate.length / PerPage)}
          page={currentPage}
          onChange={handlePageChange}
          color="primary"
          shape="rounded"
        />
      </Grid>
    </>
  ) : (
    <></>
  );
};
