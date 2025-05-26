export type DetailPageParamsType = {
  jobHuntId: string; // 就職活動ID
  userName: string; // ユーザ名
  jobStateId: string; // 就職活動状態
  userClass: string; // クラス
  classNumber: number; // 番号
  schoolNumber: number; // 学籍番号
  company: string; // 会社名
  eventCategory: string; // イベント区分
  location: string; // 場所
  locationType: string; // 場所区分
  schoolCheck: boolean; // 学校申込
  schoolCheckedFlag: boolean; // 名簿チェック
  startTime: number; // 開始時間
  finishTime: number; // 終了時間
  tardinessAbsenceType: string; // 出欠区分
  reportContent: string | null; // 報告内容
  result: string | null; // 結果
  predResult: string | null; // 予想結果
  supportedCnt: string | null; // 対応人数
  jobTitle: string | null; // 対応職
  examCategory: string | null; // 試験区分
  examContent: string | null; // 試験内容
  ecaminationContent: string | null; // 受験内容
  thoughts: string | null; // 感想
  tardyLeaveTime: number | null; // 遅刻欠席時間
  remarks: string | null; // 備考
};

export type AddPageParamsType = {
  jobHuntId: string; // 就職活動ID
  supportedCnt: number | null; // 対応人数
  jobTitle: string | null; // 対応職
  examCategory: number | null; // 試験区分
  examContentId: string | null; // 試験内容
  examinationContent: string | null; // 受験内容
  thoughts: string | null; // 感想
};

export type ChangeStateParamsType = {
  jobHuntId?: string; // 就職活動ID
  jobStateId?: string; // 就職活動状態
  buttonId: number; // ボタンID
  schoolCheck: boolean; // 名簿チェック
};

export type ReportPageParamsType = {
  jobHuntId: string; // 就職活動ID
  reportContent: string | null; // 報告内容
  result: boolean | null; // 結果
};

export type NewPageParamsType = {
  jobHuntId: string; // 就職活動ID
  userId: string; // ユーザID
  eventCategory: string; // イベント区分
  schoolCheck: boolean; // 学校申込
  company: string; // 会社名
  location: string; // 場所
  locationType: number; // 場所区分
  startTime: string; // 開始時間
  finishTime: string; // 終了時間
  tardinessAbsenceType: string; // 出欠区分
  tardyLeaveTime: string; // 遅刻欠席時間
  remarks: string; // 備考
};

export type UserDetailParamsType = {
  userName: string; // ユーザ名
  userClass: string; // クラス
  classNumber: number; // 番号
  schoolNumber: number; // 学籍番号
};
