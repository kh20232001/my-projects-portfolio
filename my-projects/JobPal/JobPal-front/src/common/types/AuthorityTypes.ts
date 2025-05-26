export const authorityOptions = [
  { value: '0', label: 'ユーザ' },
  { value: '1', label: '担任' },
  { value: '2', label: '管理者' },
  { value: '3', label: '事務' },
];

export const Authority = {
  user: '0',
  teacher: '1',
  admin: '2',
  clerk: '3',
} as const;

export type AuthorityType = (typeof Authority)[keyof typeof Authority];
