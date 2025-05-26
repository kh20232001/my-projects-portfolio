import {
  Box,
  Button,
  Container,
  FormControl,
  FormLabel,
  Grid,
  MenuItem,
  Select,
  TextField,
  Typography,
} from '@mui/material';
import { FC, Key, useEffect, useState } from 'react';
import { Controller, useForm } from 'react-hook-form';
import { useLocation, useNavigate } from 'react-router-dom';

import { useLoginUser } from '../_wheel/security/LoginUserProvider';
import { Authority } from '../common/types/AuthorityTypes';
import { UserDetailParamsType } from '../job/JobTypesForManage';
import { useJobApi } from '../job/useJobApi';
import { PostalParamsType } from './CertificateTypes';
import { ZipType } from './ZipTypes';
import { useCertificateApi } from './useCertificateApi';
import { useZipApi } from './useZipApi';

// 新規証明書申請ページ
export const CertificatePageNew: FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { getUserId } = useLoginUser();
  const { getUserDetail } = useJobApi();
  const { getZipList } = useZipApi();
  const { newCertificate, getPostal, editCertificate } = useCertificateApi();

  // 初期データ取得やフォームの状態管理
  const { getAuthority } = useLoginUser();
  const [certificateDetail, setcertificateDetail] = useState(
    location.state?.certificatedetail || {
      userId: getUserId(),
      certificateIssueId: '',
      lastName: '',
      firstName: '',
      lastNameKana: '',
      firstNameKana: '',
      certificateList: Array(4).fill({ certificateId: null, certificateName: null, count: 0 }),
      mediaName: null,
      zipCode: '',
      address: '',
      afterAddress: '',
    }
  );
  const [userDetail, setUserDetail] = useState<UserDetailParamsType>();

  const certificateName = ['在学', '成績', '卒業見込', '健康診断'];

  // React Hook Formを利用したフォーム管理
  const { control, getValues, formState, watch, setValue } = useForm({
    defaultValues: {
      userId: getUserId() || '',
      certificateIssueId: certificateDetail.certificateIssueId === null ? '' : certificateDetail.certificateIssueId,
      lastName: certificateDetail.lastName === null ? '' : certificateDetail.lastName,
      firstName: certificateDetail.firstName === null ? '' : certificateDetail.firstName,
      lastNameKana: certificateDetail.lastNameKana === null ? '' : certificateDetail.lastNameKana,
      firstNameKana: certificateDetail.firstNameKana === null ? '' : certificateDetail.firstNameKana,
      zipCode: certificateDetail.zipCode === null ? '' : certificateDetail.zipCode,
      address: certificateDetail.address === null ? '' : certificateDetail.address,
      afterAddress: certificateDetail.afterAddress === null ? '' : certificateDetail.afterAddress,
      mediaName: certificateDetail.mediaName === null ? '' : certificateDetail.mediaName,
      certificateList: certificateDetail.certificateList.map(
        (cert: { certificateId: string; certificateName: string; count: number }, index: number) => ({
          certificateId: index,
          certificateName: certificateName[index],
          count: cert.count === null ? 0 : cert.count,
        })
      ),
    },
    mode: 'onChange',
  });

  // 料金や住所リスト管理用のステート
  const [certificateFee, setCertificateFee] = useState(0);
  const [shippingFee, setShippingFee] = useState(0);
  const [totalAmount, setTotalAmount] = useState(0);
  const [list, setList] = useState<Array<ZipType>>([]);
  const [postal, setPostal] = useState<PostalParamsType>();

  // 初期データ取得
  useEffect(() => {
    getUserDetail(getUserId(), setUserDetail);
    getPostal(setPostal);
  }, []);

  // 新規申請処理
  const handleGoToJobNew = async () => {
    const isEdit = certificateDetail.certificateIssueId === '' ? false : true;
    console.log(certificateDetail);
    const result = window.confirm('申請しますか？');
    if (result) {
      if (isEdit) {
        // 編集の場合
        await editCertificate(getValues());
        navigate(`/certificate`);
      } else {
        await newCertificate(getValues());
        navigate(`/certificate`);
      }
    }
  };

  // 郵便番号検索時の住所セット
  useEffect(() => {
    list?.map((data: { prefcode: Key | null | undefined; address1: any; address2: any; address3: any }) =>
      setcertificateDetail((prevState: any) => ({
        ...prevState,
        address: data.address1 + data.address2 + data.address3,
      }))
    );
  }, [list]);

  // 郵便番号検索
  const handleSearch = () => {
    getZipList(getValues().zipCode, setList);
  };

  // 証明書数の追加
  const handleAddSet = () => {
    setcertificateDetail((prevState: any) => ({
      ...prevState,
      certificateList: prevState.certificateList.map((cert: { count: number }, index: number) => {
        if (index === 0) return cert;
        const currentCount = getValues(`certificateList.${index}.count`);
        if (currentCount >= 10) return cert;
        setValue(`certificateList.${index}.count`, currentCount + 1);
        return cert;
      }),
    }));
  };

  // 手数料や送料の計算
  useEffect(() => {
    let price = 0;
    let weight = 0;
    certificateDetail.certificateList.forEach(
      (cert: { certificateId: string; certificateName: string; count: number }, index: number) => {
        watch(`certificateList.${index}.count`, cert.count === null ? 0 : cert.count);
        if (postal?.certificateList[index]) {
          price += postal.certificateList[index].fee * watch(`certificateList.${index}.count`);
          weight += postal.certificateList[index].weight * watch(`certificateList.${index}.count`);
        }
      }
    );
    const postalFee = postal ? Math.ceil(weight / postal.postal.postalMaxWeight) * postal.postal.postalFee : 0;
    setCertificateFee(price);
    setShippingFee(watch('mediaName') === '郵送' ? postalFee : 0);
  }, [
    certificateDetail,
    watch('certificateList.0.count'),
    watch('certificateList.1.count'),
    watch('certificateList.2.count'),
    watch('certificateList.3.count'),
    watch('mediaName'),
  ]);

  useEffect(() => {
    setTotalAmount(certificateFee + shippingFee);
  }, [certificateFee, shippingFee]);

  return (
    <>
      <Container>
        <Typography gutterBottom sx={{ padding: { xs: '8px 0', sm: '16px 0' }, fontSize: { xs: '18px', sm: '20px' } }}>
          証明書発行
        </Typography>
        <Grid container spacing={2} sx={{ padding: { xs: '20px 5px!important', sm: '20px 0 40px 20px' } }}>
          <Grid item xs={4}>
            <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>学籍番号</Typography>
            <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{userDetail?.schoolNumber}</Typography>
          </Grid>
          <Grid item xs={4}>
            <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>クラス番号</Typography>
            <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>
              {userDetail?.userClass}
              {userDetail?.classNumber}
            </Typography>
          </Grid>
          <Grid item xs={4}>
            <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>名前</Typography>
            <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{userDetail?.userName}</Typography>
          </Grid>
        </Grid>
        <hr />
        <Grid container spacing={2} sx={{ padding: { xs: '20px 5px!important', sm: '20px 0 40px 20px' } }}>
          <Grid item xs={6} sm={4}>
            <FormControl fullWidth>
              <FormLabel sx={{ fontSize: { xs: '13px', sm: '16px' } }}>媒体選択</FormLabel>
              <Controller
                control={control}
                name="mediaName"
                rules={{ required: '媒体を選択してください' }}
                render={({ field, fieldState }) => (
                  <>
                    <Select
                      {...field}
                      displayEmpty
                      variant="outlined"
                      id="select"
                      sx={{
                        width: '30px',
                        minWidth: 110,
                        fontSize: '13px',
                        '& .MuiSelect-select': {
                          padding: '7px 10px !important',
                        },
                      }}
                    >
                      <MenuItem value="" disabled>
                        媒体選択
                      </MenuItem>
                      <MenuItem value="原紙">原紙</MenuItem>
                      <MenuItem value="電子">電子</MenuItem>
                      <MenuItem value="郵送">郵送</MenuItem>
                    </Select>
                    {fieldState.error && (
                      <Typography color="error" variant="caption">
                        {fieldState.error.message}
                      </Typography>
                    )}
                  </>
                )}
              />
            </FormControl>
          </Grid>
          <Grid item xs={6} sm={4} sx={{ marginTop: 'auto' }}>
            <FormControl fullWidth>
              <Button
                onClick={handleAddSet}
                variant="contained"
                color="primary"
                style={{ width: '128px', fontSize: '12px' }}
              >
                就活3点セット
              </Button>
            </FormControl>
          </Grid>

          <Grid item xs={7.5} sm={8}>
            <Grid container spacing={2} sx={{ paddingBottom: { xs: '15px', sm: '30px' } }}>
              <Grid item xs={12} sm={6} display="flex" alignItems="center">
                <Typography sx={{ fontSize: { xs: '13px', sm: '15px' }, marginRight: 1 }}>在学証明書</Typography>
                <FormControl>
                  <Controller
                    control={control}
                    name={`certificateList.${0}.count`}
                    rules={{ required: '枚数を入力してください' }}
                    render={({ field, fieldState }) => (
                      <TextField
                        {...field}
                        type="number"
                        size="small"
                        autoFocus
                        error={fieldState.error?.message !== undefined}
                        helperText={fieldState.error?.message}
                        onChange={(e) => {
                          const value = Math.max(0, Math.min(10, Number(e.target.value)));
                          field.onChange(value);
                        }}
                        sx={{
                          width: { xs: '45px', sm: '65px' },
                          '& .MuiInputBase-input': {
                            fontSize: { xs: '13px', sm: '16px' },
                          },
                        }}
                        inputProps={{ min: 0, max: 10 }}
                      />
                    )}
                  />
                </FormControl>
                <Typography sx={{ fontSize: { xs: '13px', sm: '15px' }, marginLeft: 1 }}>枚</Typography>
              </Grid>
              <Grid item xs={12} sm={6} display="flex" alignItems="center">
                <Typography sx={{ fontSize: { xs: '13px', sm: '15px' }, marginRight: 1 }}>成績証明書</Typography>
                <FormControl>
                  <Controller
                    control={control}
                    name={`certificateList.${1}.count`}
                    rules={{ required: '枚数を入力してください' }}
                    render={({ field, fieldState }) => (
                      <TextField
                        {...field}
                        type="number"
                        size="small"
                        fullWidth
                        autoFocus
                        error={fieldState.error?.message !== undefined}
                        helperText={fieldState.error?.message}
                        onChange={(e) => {
                          const value = Math.max(0, Math.min(10, Number(e.target.value)));
                          field.onChange(value);
                        }}
                        sx={{
                          width: { xs: '45px', sm: '65px' },
                          '& .MuiInputBase-input': {
                            fontSize: { xs: '13px', sm: '16px' },
                          },
                        }}
                        inputProps={{ min: 0, max: 10 }}
                      />
                    )}
                  />
                </FormControl>
                <Typography sx={{ fontSize: { xs: '13px', sm: '15px' }, marginLeft: 1 }}>枚</Typography>
              </Grid>
            </Grid>
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6} display="flex" alignItems="center">
                <Typography sx={{ fontSize: { xs: '13px', sm: '15px' }, marginRight: 1 }}>卒業見込み証明書</Typography>
                <FormControl>
                  <Controller
                    control={control}
                    name={`certificateList.${2}.count`}
                    rules={{ required: '枚数を入力してください' }}
                    render={({ field, fieldState }) => (
                      <TextField
                        {...field}
                        fullWidth
                        type="number"
                        size="small"
                        autoFocus
                        error={fieldState.error?.message !== undefined}
                        helperText={fieldState.error?.message}
                        onChange={(e) => {
                          const value = Math.max(0, Math.min(10, Number(e.target.value)));
                          field.onChange(value);
                        }}
                        sx={{
                          width: { xs: '45px', sm: '65px' },
                          '& .MuiInputBase-input': {
                            fontSize: { xs: '13px', sm: '16px' },
                          },
                        }}
                        inputProps={{ min: 0, max: 10 }}
                      />
                    )}
                  />
                </FormControl>
                <Typography sx={{ fontSize: { xs: '13px', sm: '15px' }, marginLeft: 1 }}>枚</Typography>
              </Grid>
              <Grid item xs={12} sm={6} display="flex" alignItems="center">
                <Typography sx={{ fontSize: { xs: '13px', sm: '15px' }, marginRight: 1 }}>健康診断証明書</Typography>
                <FormControl>
                  <Controller
                    control={control}
                    name={`certificateList.${3}.count`}
                    rules={{ required: '枚数を入力してください' }}
                    render={({ field, fieldState }) => (
                      <TextField
                        {...field}
                        type="number"
                        size="small"
                        fullWidth
                        autoFocus
                        error={fieldState.error?.message !== undefined}
                        helperText={fieldState.error?.message}
                        onChange={(e) => {
                          const value = Math.max(0, Math.min(10, Number(e.target.value)));
                          field.onChange(value);
                        }}
                        sx={{
                          width: { xs: '45px', sm: '65px' },
                          '& .MuiInputBase-input': {
                            fontSize: { xs: '13px', sm: '16px' },
                          },
                        }}
                        inputProps={{ min: 0, max: 10 }}
                      />
                    )}
                  />
                </FormControl>
                <Typography sx={{ fontSize: { xs: '13px', sm: '15px' }, marginLeft: 1 }}>枚</Typography>
              </Grid>
            </Grid>
          </Grid>
          <Grid item xs={4.5} sm={4} sx={{ marginTop: 'auto' }}>
            <Box sx={{ width: { xs: '100px', sm: '150px' } }}>
              {['証明書代', '送料', '計'].map((label, index) => (
                <Box
                  key={label}
                  display="flex"
                  justifyContent="space-between"
                  borderBottom={index === 2 ? 'solid 1px' : 'none'}
                  marginBottom={index === 1 ? '10px' : '5px'}
                >
                  <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>{label}</Typography>
                  <Typography sx={{ fontSize: { xs: '13px', sm: '15px' } }}>
                    {index === 0 ? `${certificateFee}円` : index === 1 ? `${shippingFee}円` : `${totalAmount}円`}
                  </Typography>
                </Box>
              ))}
            </Box>
          </Grid>
        </Grid>
        {watch('mediaName') === '郵送' && (
          <>
            <Grid container spacing={2} sx={{ padding: { xs: '20px 5px!important', sm: '20px 0 40px 20px' } }}>
              <Grid item xs={12} sm={5}>
                <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>宛先人</Typography>
                <Grid container spacing={2} sx={{ paddingBottom: { xs: '30px' } }}>
                  <Grid item xs={5.5}>
                    <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>姓</Typography>
                    <FormControl fullWidth>
                      <Controller
                        control={control}
                        name="lastName"
                        rules={{ required: '姓を入力してください' }}
                        render={({ field, fieldState }) => (
                          <TextField
                            {...field}
                            placeholder="情報"
                            size="small"
                            fullWidth
                            error={fieldState.error?.message !== undefined}
                            helperText={fieldState.error?.message}
                            sx={{
                              minWidth: 120,
                              '& .MuiInputBase-input': {
                                padding: '9px !important',
                                fontSize: { xs: '13px', sm: '16px' },
                              },
                            }}
                          />
                        )}
                      />
                    </FormControl>
                  </Grid>
                  <Grid item xs={5.5}>
                    <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>名</Typography>
                    <FormControl fullWidth>
                      <Controller
                        control={control}
                        name="firstName"
                        rules={{ required: '名を入力してください' }}
                        render={({ field, fieldState }) => (
                          <TextField
                            {...field}
                            placeholder="太郎"
                            size="small"
                            fullWidth
                            error={fieldState.error?.message !== undefined}
                            helperText={fieldState.error?.message}
                            sx={{
                              minWidth: 120,
                              '& .MuiInputBase-input': {
                                padding: '9px !important',
                                fontSize: { xs: '13px', sm: '16px' },
                              },
                            }}
                          />
                        )}
                      />
                    </FormControl>
                  </Grid>
                </Grid>
                <Grid container spacing={2} sx={{ paddingBottom: { xs: '30px' } }}>
                  <Grid item xs={5.5}>
                    <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>セイ</Typography>
                    <FormControl fullWidth>
                      <Controller
                        control={control}
                        name="lastNameKana"
                        rules={{ required: 'セイを入力してください' }}
                        render={({ field, fieldState }) => (
                          <TextField
                            {...field}
                            placeholder="ジョウホウ"
                            size="small"
                            fullWidth
                            error={fieldState.error?.message !== undefined}
                            helperText={fieldState.error?.message}
                            sx={{
                              minWidth: 120,
                              '& .MuiInputBase-input': {
                                padding: '9px !important',
                                fontSize: { xs: '13px', sm: '16px' },
                              },
                            }}
                          />
                        )}
                      />
                    </FormControl>
                  </Grid>
                  <Grid item xs={5.5}>
                    <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>メイ</Typography>
                    <FormControl fullWidth>
                      <Controller
                        control={control}
                        name="firstNameKana"
                        rules={{ required: 'メイを入力してください' }}
                        render={({ field, fieldState }) => (
                          <TextField
                            {...field}
                            placeholder="タロウ"
                            size="small"
                            fullWidth
                            error={fieldState.error?.message !== undefined}
                            helperText={fieldState.error?.message}
                            sx={{
                              minWidth: 120,
                              '& .MuiInputBase-input': {
                                padding: '9px !important',
                                fontSize: { xs: '13px', sm: '16px' },
                              },
                            }}
                          />
                        )}
                      />
                    </FormControl>
                  </Grid>
                </Grid>
              </Grid>
            </Grid>
            <Grid container spacing={2} sx={{ padding: { xs: '20px 5px!important', sm: '20px 0 40px 20px' } }}>
              <Grid item xs={12} sm={4}>
                <Grid container spacing={2} display="flex" alignItems="center" sx={{ paddingBottom: { xs: '10px' } }}>
                  <Grid item xs={5.5}>
                    <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>郵便番号</Typography>
                    <Controller
                      control={control}
                      name="zipCode"
                      rules={{
                        required: '郵便番号を入力してください',
                        pattern: {
                          value: /^[0-9]{7}$/,
                          message: '郵便番号は7桁の数字で入力してください',
                        },
                      }}
                      render={({ field, fieldState }) => (
                        <TextField
                          {...field}
                          placeholder="郵便番号"
                          size="small"
                          fullWidth
                          autoFocus
                          error={fieldState.error?.message !== undefined}
                          helperText={fieldState.error?.message}
                          onKeyDown={(e) => {
                            if (e.key === 'Enter' && formState.isValid) {
                              handleSearch();
                            }
                          }}
                        />
                      )}
                    />
                  </Grid>
                  <Grid item xs={6.5} sx={{ marginTop: 'auto' }}>
                    <Button type="submit" variant="contained" sx={{ padding: '8px 16px' }} onClick={handleSearch}>
                      検索
                    </Button>
                  </Grid>
                </Grid>

                <Grid item xs={12} sx={{ paddingBottom: 2 }}>
                  <Typography sx={{ fontSize: { xs: '12px', sm: '13px' } }}>住所</Typography>
                  <FormControl fullWidth>
                    <Controller
                      control={control}
                      name="address"
                      rules={{ required: '住所を入力してください' }}
                      render={({ field, fieldState }) => (
                        <TextField
                          {...field}
                          placeholder="北海道札幌市白石区菊水6条"
                          size="small"
                          fullWidth
                          value={field.value || certificateDetail.address}
                          onChange={(e) => {
                            field.onChange(e);
                            setcertificateDetail((prevState: any) => ({
                              ...prevState,
                              address: e.target.value,
                            }));
                          }}
                          error={fieldState.error?.message !== undefined}
                          helperText={fieldState.error?.message}
                          sx={{
                            minWidth: 120,
                            '& .MuiInputBase-input': {
                              padding: '9px !important',
                              fontSize: { xs: '13px', sm: '16px' },
                            },
                          }}
                        />
                      )}
                    />
                  </FormControl>
                </Grid>
                <Grid item xs={12}>
                  <FormControl fullWidth>
                    <Controller
                      control={control}
                      name="afterAddress"
                      rules={{ required: '番地を入力してください' }}
                      render={({ field, fieldState }) => (
                        <TextField
                          {...field}
                          placeholder="3丁目4-28"
                          size="small"
                          fullWidth
                          error={fieldState.error?.message !== undefined}
                          helperText={fieldState.error?.message}
                          sx={{
                            minWidth: 120,
                            '& .MuiInputBase-input': {
                              padding: '9px !important',
                              fontSize: { xs: '13px', sm: '16px' },
                            },
                          }}
                        />
                      )}
                    />
                  </FormControl>
                </Grid>
              </Grid>
            </Grid>
          </>
        )}
        <hr />
        <Box display="flex" justifyContent="center" mt={3}>
          {getAuthority() === Authority.user && (
            <Button
              onClick={handleGoToJobNew}
              variant="contained"
              disabled={!formState.isValid || certificateFee === 0}
              color="primary"
              style={{ marginRight: '1rem' }}
            >
              申請
            </Button>
          )}
        </Box>
      </Container>
    </>
  );
};
