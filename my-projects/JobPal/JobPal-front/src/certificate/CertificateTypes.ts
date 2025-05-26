// 証明書情報の型
export type CertificateType = {
  certificateIssueId: string; // 証明書発行ID
  userId: string; // ユーザーID
  userName: string; // ユーザー名
  schoolClassNumber: string; // 学籍番号
  certificateStateName: string; // 証明書の状態名
  date: string; // 申請日や発行日
  certificateList: {
    certificateId: string; // 証明書ID
    certificateName: string; // 証明書名
    count: number; // 発行枚数
  }[];
  totalAmount: string; // 合計金額
  mediaName: string; // 発行媒体名（例: 郵送、電子）
  stateIdPriority: number; // 証明書状態の優先度
  startTimePriority: number; // 発行開始の優先度
  reNotifyFlag: boolean; // 再通知が必要かどうかのフラグ
};

// 証明書詳細ページのパラメータ型
export type DetailPageParamsType = {
  certificateIssueId: string; // 証明書発行ID
  userId: string; // ユーザーID
  userClass: string; // ユーザークラス名
  classNumber: string; // クラス番号
  userName: string; // ユーザー名
  schoolClassNumber: string; // 学籍番号
  certificateStateName: string; // 証明書の状態名
  officeUserName: string; // 担当者名
  lastName: string | null; // ユーザー姓
  firstName: string | null; // ユーザー名
  lastNameKana: string | null; // ユーザー姓（カナ）
  firstNameKana: string | null; // ユーザー名（カナ）
  certificateList: {
    certificateId: string; // 証明書ID
    certificateName: string; // 証明書名
    count: number; // 発行枚数
  }[];
  mediaName: string; // 発行媒体名
  zipCode: string | null; // 郵便番号
  address: string | null; // 住所
  afterAddress: string | null; // 住所の詳細
};

// 新規証明書作成ページのパラメータ型
export type NewPageParamsType = {
  userId: string; // ユーザーID
  certificateIssueId: string; // 証明書ID
  lastName: string; // ユーザー姓
  firstName: string; // ユーザー名
  lastNameKana: string; // ユーザー姓（カナ）
  firstNameKana: string; // ユーザー名（カナ）
  certificateList: {
    certificateId: string; // 証明書ID
    certificateName: string; // 証明書名
    count: number; // 発行枚数
  }[];
  mediaName: string; // 発行媒体名
  zipCode: string; // 郵便番号
  address: string; // 住所
  afterAddress: string; // 住所の詳細
};

// 状態変更のリクエスト型
export type ChangeStateParamsType = {
  certificateIssueId: string; // 証明書発行ID
  certificateStateName: string; // 変更後の状態名
  mediaName: string; // 媒体名
  buttonId: number; // 操作ボタンID（変更内容を識別）
  userId: string; // ユーザーID
};

// 郵便料金や証明書詳細の型
export type PostalParamsType = {
  certificateList: {
    certificateId: string; // 証明書ID
    fee: number; // 証明書1枚あたりの金額
    weight: number; // 証明書1枚あたりの重量
  }[];
  postal: {
    postalMaxWeight: number; // 郵送の最大重量
    postalFee: number; // 郵送料金
  };
};
