--ユーザマスタ
CREATE TABLE IF NOT EXISTS
  USER_M (
    user_id VARCHAR(254) PRIMARY KEY, -- ユーザID。メールアドレスが格納される。
    encrypted_password VARCHAR(100) NOT NULL, -- 暗号化されたパスワード。
    user_name VARCHAR(50) NOT NULL, -- ユーザの名前。
    user_status CHAR(1) NOT NULL, -- ユーザの状態を示すコード。1文字の固定長。
    user_type CHAR(1) NOT NULL, -- ユーザの区分を示すコード。1文字の固定長。
    created_by_user_id VARCHAR(254), -- 作成者のユーザID。ユーザマスタを参照。
    created_at TIMESTAMP NOT NULL, -- 作成日時。
    updated_at TIMESTAMP NOT NULL, -- 更新日時。
    FOREIGN KEY (created_by_user_id) REFERENCES USER_M (user_id) ON DELETE SET NULL -- 自身の参照キー（参照先が消えるとNULLをセットする）
  );

--担任・学生ユーザマスタ
CREATE TABLE IF NOT EXISTS
  TEACHER_STUDENT_USER_M (
    user_id VARCHAR(254) PRIMARY KEY, -- ユーザID。メールアドレスが格納される。
    department CHAR(1) NOT NULL, -- 学科。1文字の固定長。
    grade CHAR(1) NOT NULL, -- 学年。1文字の固定長。
    class_name CHAR(2) NOT NULL, -- クラス。2文字の固定長。
    attendance_number CHAR(2), -- 出席番号。2文字の固定長。
    student_id INT NOT NULL, -- 学籍番号。最小7桁・最大8桁。
    FOREIGN KEY (user_id) REFERENCES USER_M (user_id) ON DELETE CASCADE
  );

--通知クラスマスタ
CREATE TABLE IF NOT EXISTS
  NOTIFICATION_CLASS_M (
    notification_department CHAR(1) NOT NULL, -- 通知する学科。1文字の固定長。
    notification_grade CHAR(1) NOT NULL, -- 通知する学年。1文字の固定長。
    notification_class CHAR(2) NOT NULL, -- 通知するクラス。2文字の固定長。
    assigned_teacher_user_id VARCHAR(254) NOT NULL, -- 担任のユーザID。ユーザマスタを参照。
    PRIMARY KEY (
      notification_department,
      notification_grade,
      notification_class,
      assigned_teacher_user_id
    ),
    FOREIGN KEY (assigned_teacher_user_id) REFERENCES USER_M (user_id) ON DELETE CASCADE-- ユーザマスタへの外部キー参照
  );

--就職活動トラン
CREATE TABLE IF NOT EXISTS
  JOB_SEARCH_T (
    job_search_id CHAR(13) PRIMARY KEY, -- 就職活動に割り振られるID。13文字の固定長。
    student_user_id VARCHAR(254), -- 学生のユーザID。ユーザマスタを参照。
    job_search_status CHAR(2) NOT NULL, -- 就職活動の状態を示すコード。2文字の固定長。
    FOREIGN KEY (student_user_id) REFERENCES USER_M (user_id)  ON DELETE SET NULL-- ユーザマスタへの外部キー参照
  );

--就職活動申請トラン
CREATE TABLE IF NOT EXISTS
  JOB_SEARCH_APPLICATION_T (
    job_application_id CHAR(13) PRIMARY KEY, -- 就職活動申請に割り振られるID。13文字の固定長。
    start_time TIMESTAMP NOT NULL, -- イベントや活動の開始日時。
    company_name VARCHAR(100) NOT NULL, -- 会社名。100文字の可変長。
    event_category CHAR(1) NOT NULL, -- イベントの種類を示すコード。1文字の固定長。
    location_type CHAR(1) NOT NULL, -- 場所の種類を示すコード。1文字の固定長。
    location VARCHAR(200) NOT NULL, -- イベントや活動の場所の住所。200文字の可変長。
    school_check_flag BOOLEAN NOT NULL, -- 学校とりまとめ名簿の登録の要否。
    school_checked_flag BOOLEAN, -- 学校名簿に担任が登録済みか。
    tardiness_absence_type CHAR(1) NOT NULL, -- 出欠を示すコード。1文字の固定長。
    tardy_leave_time TIME, -- 遅刻または早退の時間。
    end_time TIMESTAMP, -- イベントや活動の終了日時。
    remarks TEXT, -- 備考。文字数制限なし。
    created_at TIMESTAMP NOT NULL, -- レコードの作成日時。
    updated_at TIMESTAMP NOT NULL, -- レコードの更新日時。
    job_search_id CHAR(13), -- 就職活動に割り振られるID。13文字の固定長。
    FOREIGN KEY (job_search_id) REFERENCES JOB_SEARCH_T (job_search_id) -- 就職活動トランへの外部キー参照
  );

--就職活動報告トラン
CREATE TABLE IF NOT EXISTS
  JOB_SEARCH_REPORT_T (
    job_report_id CHAR(13) PRIMARY KEY, -- 就職活動報告に割り振られるID。13文字の固定長。
    report_content TEXT NOT NULL, -- 報告内容。文字数制限なし。
    result CHAR(1) NOT NULL, -- 就職活動の結果を示すコード。1文字の固定長。
    created_at TIMESTAMP NOT NULL, -- レコードの作成日時。
    updated_at TIMESTAMP NOT NULL, -- レコードの更新日時。
    job_search_id CHAR(13), -- 就職活動に割り振られるID。13文字の固定長。
    FOREIGN KEY (job_search_id) REFERENCES JOB_SEARCH_T (job_search_id) -- 就職活動トランへの外部キー参照
  );

--受験報告トラン
CREATE TABLE IF NOT EXISTS
  EXAM_REPORT_T (
    exam_report_id CHAR(13) PRIMARY KEY, -- 受験報告に割り振られるIDが格納される。13桁の固定長。
    exam_opponent_count INT NOT NULL, -- 受験をした際の人数を格納。最小1桁・最大2桁。
    exam_opponent_position VARCHAR(20) NOT NULL, -- 受験した際の相手の役職を格納。20文字の可変長。
    exam_count CHAR(1) NOT NULL, -- 受験した試験の回数を格納。1文字の固定長。
    exam_type CHAR(1) NOT NULL, -- 受験した試験の区分を示すコード。1文字の固定長。
    exam_content TEXT NOT NULL, -- 受験した試験の内容を格納。文字数制限なし。
    impressions TEXT NOT NULL, -- 受験した試験の感想を格納。文字数制限なし。
    created_at TIMESTAMP NOT NULL, -- レコードが作成された日時を格納。
    updated_at TIMESTAMP NOT NULL, -- レコードが最後に更新された日時を格納。
    job_search_id CHAR(13), -- 就職活動に割り振られるID。13文字の固定長。
    FOREIGN KEY (job_search_id) REFERENCES JOB_SEARCH_T (job_search_id) -- 就職活動トランへの外部キー参照
  );

--証明書発行トラン
CREATE TABLE IF NOT EXISTS
  CERTIFICATE_ISSUANCE_T (
    certificate_issue_id CHAR(13) PRIMARY KEY, -- 証明書発行に割り振られるID。13文字の固定長。
    status CHAR(1) NOT NULL, -- 証明書発行の状態を示すコード。1文字の固定長。
    student_user_id VARCHAR(254), -- 学生のユーザID。ユーザマスタを参照。
    application_date DATE NOT NULL, -- 証明書発行の申請日。
    media_type CHAR(1) NOT NULL, -- 証明書発行の媒体区分コード。1文字の固定長。
    teacher_user_id VARCHAR(254), -- 担任のユーザID。ユーザマスタを参照。
    teacher_check_flag BOOLEAN, -- 担任チェックフラグ。NULL=未チェック, TRUE=承認, FALSE=差戻し。
    office_user_id VARCHAR(254), -- 事務部のユーザID。ユーザマスタを参照。
    approval_date DATE　, -- 証明書発行の承認日。
    FOREIGN KEY (student_user_id) REFERENCES USER_M (user_id) ON DELETE SET NULL,
    FOREIGN KEY (teacher_user_id) REFERENCES USER_M (user_id) ON DELETE SET NULL,
    FOREIGN KEY (office_user_id) REFERENCES USER_M (user_id) ON DELETE SET NULL
  );

--郵送マスタ
CREATE TABLE IF NOT EXISTS
  MAILING_M (
    postal_payment_id CHAR(1) PRIMARY KEY, -- 郵便支払ID。1文字の固定長。
    postal_fee INT NOT NULL, -- 郵送料金。3桁の数値。
    postal_max_weight INT NOT NULL -- 郵送可能な重量。最小2桁・最大3桁の数値。
  );

--証明書マスタ
CREATE TABLE IF NOT EXISTS
  CERTIFICATE_M (
    certificate_id CHAR(1) PRIMARY KEY, -- 証明書ID。1文字の固定長。
    certificate_name VARCHAR(8) NOT NULL, -- 証明書の名前。8文字の可変長。
    certificate_fee INT NOT NULL, -- 証明書発行の料金。3桁の数値。
    certificate_weight INT NOT NULL -- 証明書の重量。最小1桁・最大2桁の数値。
  );

--下３つは、担任チェックフラグがTRUEの時に登録される。
--電子証明書発行トラン
CREATE TABLE IF NOT EXISTS
  ELECTRONIC_CERTIFICATE_ISSUANCE_T (
    certificate_issue_id CHAR(13) PRIMARY KEY, -- 証明書発行ID。13文字の固定長。
    recipient_email_address VARCHAR(254) NOT NULL, -- 送信先メールアドレス。254文字の可変長。
    FOREIGN KEY (certificate_issue_id) REFERENCES CERTIFICATE_ISSUANCE_T (certificate_issue_id) ON DELETE CASCADE -- 証明書発行トランへの外部キー
  );

--紙証明書発行トラン
CREATE TABLE IF NOT EXISTS
  PAPER_CERTIFICATE_ISSUANCE_T (
    certificate_issue_id CHAR(13) PRIMARY KEY, -- 証明書発行ID。13文字の固定長。
    delivery_due_date DATE NOT NULL, -- 受渡期限日。
    delivery_date DATE, -- 受渡日。
    FOREIGN KEY (certificate_issue_id) REFERENCES CERTIFICATE_ISSUANCE_T (certificate_issue_id) ON DELETE CASCADE-- 証明書発行トランへの外部キー
  );

--郵送証明書発行トラン
CREATE TABLE IF NOT EXISTS
  MAILING_CERTIFICATE_ISSUANCE_T (
    certificate_issue_id CHAR(13) PRIMARY KEY, -- 証明書発行ID。13文字の固定長。
    postal_payment_id CHAR(1), -- 郵便支払ID。郵送マスタを参照。
    recipient_name VARCHAR(50) NOT NULL, -- 宛先人の名前。50文字の可変長。
    recipient_furigana VARCHAR(50) NOT NULL, -- 宛先人のフリガナ。50文字の可変長。
    recipient_address VARCHAR(200) NOT NULL, -- 郵送先住所。200文字の可変長。
    post_date DATE, -- 証明書の郵送日。
    FOREIGN KEY (certificate_issue_id) REFERENCES CERTIFICATE_ISSUANCE_T (certificate_issue_id) ON DELETE CASCADE, -- 証明書発行トランへの外部キー
    FOREIGN KEY (postal_payment_id) REFERENCES MAILING_M (postal_payment_id) -- 郵送マスタへの外部キー
  );

--証明書発行詳細トラン
CREATE TABLE IF NOT EXISTS
  CERTIFICATE_ISSUANCE_DETAIL_T (
    certificate_issue_id CHAR(13) NOT NULL, -- 証明書発行ID。13文字の固定長。
    certificate_id CHAR(1) NOT NULL, -- 証明書ID。1文字の固定長。
    certificate_quantity INT NOT NULL, -- 証明書発行数。1桁の数値。
    PRIMARY KEY (certificate_issue_id, certificate_id),
    FOREIGN KEY (certificate_issue_id) REFERENCES CERTIFICATE_ISSUANCE_T (certificate_issue_id) ON DELETE CASCADE, -- 証明書発行トランへの外部キー
    FOREIGN KEY (certificate_id) REFERENCES CERTIFICATE_M (certificate_id) -- 証明書マスタへの外部キー
  );

-- 通知トラン
CREATE TABLE IF NOT EXISTS
  NOTIFICATION_T (
    notification_id CHAR(13) PRIMARY KEY, -- 通知に割り振られるID。13文字の固定長。
    assigned_user_id VARCHAR(254), -- 通知先のユーザID。ユーザマスタを参照。
    resend_flag BOOLEAN NOT NULL, -- 再通知かのフラグ。
    job_search_certificate_category CHAR(1) NOT NULL, -- 通知が就職活動か証明書かの区分コード。1文字の固定長。
    notification_timestamp TIMESTAMP NOT NULL, -- 通知の作成日時。
    FOREIGN KEY (assigned_user_id) REFERENCES USER_M (user_id) ON DELETE CASCADE-- ユーザマスタへの外部キー参照
  );

--通知就職活動トラン
CREATE TABLE IF NOT EXISTS
  NOTIFICATION_JOB_SEARCH_T (
    notification_id CHAR(13) PRIMARY KEY, -- 通知に割り振られるID。13文字の固定長。
    job_search_id CHAR(13), -- 就職活動に割り振られるID。13文字の固定長。
    FOREIGN KEY (notification_id) REFERENCES NOTIFICATION_T (notification_id) ON DELETE CASCADE , -- 通知トランへの外部キー参照
    FOREIGN KEY (job_search_id) REFERENCES JOB_SEARCH_T (job_search_id) -- 就職活動トランへの外部キー参照
  );

--通知証明書発行トラン
CREATE TABLE IF NOT EXISTS
  NOTIFICATION_CERTIFICATE_ISSUANCE_T (
    notification_id CHAR(13) PRIMARY KEY, -- 通知に割り振られるID。13文字の固定長。
    certificate_issue_id CHAR(13), -- 証明書発行に割り振られるID。13文字の固定長。
    FOREIGN KEY (notification_id) REFERENCES NOTIFICATION_T (notification_id) ON DELETE CASCADE , -- 通知トランへの外部キー参照
    FOREIGN KEY (certificate_issue_id) REFERENCES CERTIFICATE_ISSUANCE_T (certificate_issue_id) -- 証明書発行トランへの外部キー参照
  );

--削除ユーザマスタ
CREATE TABLE IF NOT EXISTS
  DELETED_USER_M (
    user_id VARCHAR(254) NOT NULL, -- ユーザID。メールアドレスが格納される。
    deleted_at TIMESTAMP NOT NULL, -- 削除時刻。
    encrypted_password VARCHAR(100) NOT NULL, -- 暗号化されたパスワード。
    user_name VARCHAR(50) NOT NULL, -- ユーザの名前。
    user_status CHAR(1) NOT NULL, -- ユーザの状態を示すコード。1文字の固定長。
    user_type CHAR(1) NOT NULL, -- ユーザの区分を示すコード。1文字の固定長。
    created_by_user_id VARCHAR(254), -- 作成者のユーザID。ユーザマスタを参照。
    created_at TIMESTAMP NOT NULL, -- 作成日時。
    updated_at TIMESTAMP NOT NULL,--更新日時。
    PRIMARY KEY (user_id, deleted_at)    -- 複合主キー
  );

--削除担任・学生ユーザ
CREATE TABLE IF NOT EXISTS
  DELETED_TEACHER_STUDENT_M (
    user_id VARCHAR(254) NOT NULL, -- ユーザID。メールアドレスが格納される。
    deleted_at TIMESTAMP NOT NULL, -- 削除時刻。
    department CHAR(1) NOT NULL, -- 学科。1文字の固定長。
    grade CHAR(1) NOT NULL, -- 学年。1文字の固定長。
    class_name CHAR(2) NOT NULL, -- クラス。2文字の固定長。
    attendance_number CHAR(2), -- 出席番号。2文字の固定長。
    student_id INT NOT NULL, -- 学籍番号。最小7桁・最大8桁。
    PRIMARY KEY (user_id, deleted_at),    -- 複合主キー
    FOREIGN KEY (user_id, deleted_at) REFERENCES DELETED_USER_M (user_id, deleted_at) -- 削除ユーザマスタへの外部キー参照
  
  );
  
