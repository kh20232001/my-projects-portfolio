import type { AuthorityType } from '../common/types/AuthorityTypes';


export type AddUserParamsType = {
  userId: string; // ユーザID
  password: string; // パスワード
  userName: string; // ユーザ名
  userClass: string; // クラス
  classNumber: number; // 番号
  schoolNumber: number; // 学籍番号
  grant: AuthorityType; // 権限
  status: string; // ステータス
  createUserId: string; // 作成者ID
};

export type UpdateUserParamsType = {
  userId: string; // ユーザID
  password: string; // パスワード
  userName: string; // ユーザ名
  userClass: string; // クラス
  classNumber: number; // 番号
  schoolNumber: number; // 学籍番号
  grant: AuthorityType; // 権限
  status: string; // ステータス
};

export type DeleteUserParamsType = {
  userId: string; // ユーザID
};

export type DeleteUserCSVParamsType = {
  userId: string;
  userName: string;
  job: {
    jobHuntId: string;
  }[];
  certificate: {
    certificateIssueId: string;
  }[];
};
