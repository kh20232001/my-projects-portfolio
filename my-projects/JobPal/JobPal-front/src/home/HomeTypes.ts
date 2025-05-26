export type HomeType = {
  jobHuntId: string; // 就職活動ID
  userId: string; // ユーザID
  userName: string; // ユーザ名
  schoolClassNumber: string; // 学籍番号
  jobStateId: string; // 就職活動状態
  company: string; // 会社名
  eventCategory: string; // イベント区分
  result: string; // 結果
  schoolCheck: boolean; // 学校申込
  date: string; // 期日
  startTime: string; // 開始時間
  finishTime: string; // 終了時間
  eventCategoryPriority: number; // イベント区分の優先度
  stateIdPriority: number; // 就職活動の状態の優先度
  startTimePriority: number; // 開始時間の優先度
  reNotifyFlag: boolean; // 再通知フラグ
};

export type CSVType = {
  userClass: string;
  studentsInAction: number;
  studentsOfEnd: number;
  activityLocationInTokyo: number;
  activityLocationInSapporo: number;
  activityLocationInOther: number;
  activityFormInOnline: number;
  activityFormInLocal: number;
  activityFormInOther: number;
};
