import ArrowDownwardIcon from '@mui/icons-material/ArrowDownward';
import ArrowUpwardIcon from '@mui/icons-material/ArrowUpward';
import SearchIcon from '@mui/icons-material/Search';
import { Button, IconButton, InputAdornment, MenuItem, Pagination, Select } from '@mui/material';
import { SelectChangeEvent } from '@mui/material';
import Grid from '@mui/material/Grid';
import Switch from '@mui/material/Switch';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { FC, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { useLoginUser } from '../_wheel/security/LoginUserProvider';
import { CertificateType } from '../certificate/CertificateTypes';
import { CertificateCard } from '../certificate/components/CertificateCard';
import { HomeParamsType } from './HomeParamsType';
import { HomeType } from './HomeTypes';
import { JobCard } from './components/JobCard';
import { useHomeApi } from './useHomeApi';

export const AlertPage: FC = () => {
  const navigate = useNavigate();
  const { getAuthority, getUserId } = useLoginUser();
  const { getAlerts } = useHomeApi();

  // 状態管理
  const [jobs, setJobs] = useState<Array<HomeType>>([]);
  const [certificate, setCertificate] = useState<Array<CertificateType>>([]);
  const [sortOrder, setSortOrder] = useState('');
  const [ascending, setAscending] = useState<boolean>(true);
  const [filteredJobs, setFilteredJobs] = useState(jobs.filter((job) => job.jobStateId !== '完了'));
  const [filteredCertificate, setFilteredCertificate] = useState(
    certificate.filter((certificate) => certificate.certificateStateName !== '完了')
  );
  const [isChecked, setIsChecked] = useState(true);
  const [userName, setUserName] = useState('');
  const [keyword, setKeyword] = useState<string>('');
  const [currentPage, setCurrentPage] = useState(1);
  const [certCurrentPage, setCertCurrentPage] = useState(1);
  const jobsPerPage = 5;
  const PerPage = 5;

  const getValues: HomeParamsType = {
    userId: getUserId(),
    grant: getAuthority(),
  };

  // アラートジョブの取得
  useEffect(() => {
    const homeValues = getValues;
    getAlerts(homeValues, setJobs, setCertificate, setUserName);
  }, []);

  // ソート順の変更
  useEffect(() => {
    handleSortChange();
  }, [sortOrder, ascending]);

  // ジョブフィルターの更新
  useEffect(() => {
    setFilteredJobs(jobs.filter((job) => job.jobStateId !== '完了'));
    setFilteredCertificate(certificate.filter((certificate) => certificate.certificateStateName !== '完了'));
  }, [jobs, certificate]);

  // 昇順/降順の切り替え
  const handleOrderChange = () => {
    setAscending(!ascending);
  };

  // ソート順の変更
  const handleSortOrderChange = (event: SelectChangeEvent<string>) => {
    setSortOrder(event.target.value);
  };

  // ジョブのソート
  const handleSortChange = () => {
    const sortedJobs = [...filteredJobs].sort((a: any, b: any) => {
      if (a[sortOrder] < b[sortOrder]) return ascending ? -1 : 1;
      if (a[sortOrder] > b[sortOrder]) return ascending ? 1 : -1;
      return 0;
    });
    setFilteredJobs(sortedJobs);
    const sortedCertificate = [...filteredCertificate].sort((a: any, b: any) => {
      if (a[sortOrder] < b[sortOrder]) return ascending ? -1 : 1;
      if (a[sortOrder] > b[sortOrder]) return ascending ? 1 : -1;
      return 0;
    });
    setFilteredCertificate(sortedCertificate);
  };

  // スイッチの状態変更
  const handleSwitchChange = () => {
    setIsChecked(!isChecked);
    if (!isChecked) {
      setFilteredJobs(jobs.filter((job) => job.jobStateId !== '完了'));
      setFilteredCertificate(certificate.filter((certificate) => certificate.certificateStateName !== '完了'));
    } else {
      setFilteredJobs(jobs);
      setFilteredCertificate(certificate);
    }
  };

  // ジョブ詳細ページへのナビゲーション
  const handleGoToJob = (id: string) => {
    navigate(`/homedetail/${id}`, { state: { id } });
  };

  const handleGoToCertificate = (id: string) => {
    navigate(`/certificatedetail/${id}`, { state: { id } });
  };

  // 検索キーワードの変更
  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setKeyword(event.target.value);
  };

  // ジョブのフィルタリング
  const handleFilterJobs = () => {
    if (!keyword) return setFilteredJobs(jobs);

    const filtered = filteredJobs.filter((job) =>
      Object.values(job)
        .filter((value) => value !== null && value !== undefined)
        .some((value) => value.toString().toLowerCase().includes(keyword.toLowerCase()))
    );
    setFilteredJobs(filtered);
  };

  // ページ変更
  const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setCurrentPage(value);
  };
  const handleCertPageChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setCertCurrentPage(value);
  };

  // ページネーションによるジョブの表示
  const paginatedJobs = filteredJobs.slice((currentPage - 1) * jobsPerPage, currentPage * jobsPerPage);
  const paginatedCertificate = filteredCertificate.slice((certCurrentPage - 1) * PerPage, certCurrentPage * PerPage);

  // UIのレンダリング
  return getUserId() ? (
    <>
      <Grid container spacing={2} alignItems="center">
        <Grid item xs={12} sm={5}>
          <Typography
            sx={{
              margin: '16px 0 32px',
              fontSize: {
                xs: '1.5rem',
                sm: '2rem',
              },
            }}
          >
            {userName}通知
          </Typography>
        </Grid>
      </Grid>

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
              <MenuItem value="startTimePriority">日時</MenuItem>
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
      {filteredJobs.length !== 0 && (
        <>
          <Typography sx={{ paddingBottom: '30px', fontSize: '20px' }}>就職活動</Typography>
          <Grid container spacing={2}>
            {paginatedJobs.map((job, index) => (
              <Grid item xs={12} key={index}>
                <JobCard {...job} handle={() => handleGoToJob(job.jobHuntId)} />
              </Grid>
            ))}
          </Grid>
          <Grid container justifyContent="flex-end" sx={{ marginTop: '20px' }}>
            <Pagination
              count={Math.ceil(filteredJobs.length / jobsPerPage)}
              page={currentPage}
              onChange={handlePageChange}
              color="primary"
              shape="rounded"
            />
          </Grid>
          {filteredCertificate.length !== 0 && <hr style={{ margin: '30px 0' }} />}
        </>
      )}

      {filteredCertificate.length !== 0 && (
        <>
          <Typography sx={{ paddingBottom: '30px', fontSize: '20px' }}>証明書</Typography>
          <Grid container spacing={2}>
            {paginatedCertificate.map((certificate, index) => (
              <Grid item xs={12} key={index}>
                <CertificateCard
                  {...certificate}
                  handle={() => handleGoToCertificate(certificate.certificateIssueId)}
                />
              </Grid>
            ))}
          </Grid>
          <Grid container justifyContent="flex-end" sx={{ marginTop: '20px' }}>
            <Pagination
              count={Math.ceil(filteredCertificate.length / PerPage)}
              page={certCurrentPage}
              onChange={handleCertPageChange}
              color="primary"
              shape="rounded"
            />
          </Grid>
        </>
      )}
    </>
  ) : (
    <></>
  );
};
