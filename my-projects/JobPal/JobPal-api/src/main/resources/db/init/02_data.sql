DELETE FROM NOTIFICATION_CERTIFICATE_ISSUANCE_T;
DELETE FROM NOTIFICATION_JOB_SEARCH_T;
DELETE FROM NOTIFICATION_T;
DELETE FROM CERTIFICATE_ISSUANCE_DETAIL_T;
DELETE FROM MAILING_CERTIFICATE_ISSUANCE_T;
DELETE FROM PAPER_CERTIFICATE_ISSUANCE_T;
DELETE FROM ELECTRONIC_CERTIFICATE_ISSUANCE_T;
DELETE FROM CERTIFICATE_ISSUANCE_T;
DELETE FROM EXAM_REPORT_T;
DELETE FROM JOB_SEARCH_REPORT_T;
DELETE FROM JOB_SEARCH_APPLICATION_T;
DELETE FROM JOB_SEARCH_T;
DELETE FROM NOTIFICATION_CLASS_M;
DELETE FROM DELETED_TEACHER_STUDENT_M;
DELETE FROM DELETED_USER_M;
DELETE FROM TEACHER_STUDENT_USER_M;
--
--
DELETE FROM USER_M;
DELETE FROM CERTIFICATE_M;
DELETE FROM MAILING_M;
--
--
INSERT INTO
  USER_M (
    user_id,
    encrypted_password,
    user_name,
    user_status,
    user_type,
    created_by_user_id,
    created_at,
    updated_at
  )
VALUES
  (
    'admin@hcs.ac.jp',
    '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa',
    '佐藤 太郎',
    '0',
    '2',
    NULL,
    '2024-01-01 10:00:00',
    '2024-01-01 10:00:00'
  ),(
    'teacher@hcs.ac.jp',
    '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa',
    '鈴木 次郎',
    '0',
    '1',
    'admin@hcs.ac.jp',
    '2024-01-02 10:00:00',
    '2024-01-02 10:00:00'
  ), (
    'student@hcs.ac.jp',
    '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa',
    '高橋 三郎',
    '0',
    '0',
    'admin@hcs.ac.jp',
    '2024-01-03 10:00:00',
    '2024-01-03 10:00:00'
  ),(
    'office@hcs.ac.jp',
    '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa',
    '田中 四郎',
    '0',
    '3',
    'admin@hcs.ac.jp',
    '2024-01-04 10:00:00',
    '2024-01-04 10:00:00'
  ),
  (
    'teacher2@hcs.ac.jp',
    '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa',
    '渡辺 五郎',
    '0',
    '1',
    'admin@hcs.ac.jp',
    '2024-01-05 10:00:00',
    '2024-01-05 10:00:00'
  );
--
INSERT INTO
  TEACHER_STUDENT_USER_M (
    user_id,
    department,
    grade,
    class_name,
    attendance_number,
    student_id
  )
VALUES
  (
    'teacher@hcs.ac.jp',
    'S',
    '3',
    'A2',
    NULL,
    2345678
  ),
  (
    'student@hcs.ac.jp',
    'S',
    '3',
    'A2',
    '01',
    30456789
  ),
  (
    'teacher2@hcs.ac.jp',
    'T',
    '0',
    'A0',
    NULL,
    5678901
  );
--
INSERT INTO
  NOTIFICATION_CLASS_M (
    notification_department,
    notification_grade,
    notification_class,
    assigned_teacher_user_id
  )
VALUES
  ('S', '3', 'A2', 'teacher2@hcs.ac.jp');
--
INSERT INTO
  JOB_SEARCH_T (job_search_id, student_user_id, job_search_status)
VALUES
  ('JS_2024_00001', 'student@hcs.ac.jp', '33'),
  ('JS_2024_00002', 'student@hcs.ac.jp', '31'),
  ('JS_2024_00003', 'student@hcs.ac.jp', '11'),
  ('JS_2024_00004', 'student@hcs.ac.jp', '12'),
  ('JS_2024_00005', 'student@hcs.ac.jp', '00'),
  ('JS_2024_00006', 'student@hcs.ac.jp', '23');
--
INSERT INTO
  JOB_SEARCH_APPLICATION_T (
    job_application_id,
    start_time,
    company_name,
    event_category,
    location_type,
    LOCATION,
    school_check_flag,
    school_checked_flag,
    tardiness_absence_type,
    tardy_leave_time,
    end_time,
    remarks,
    created_at,
    updated_at,
    job_search_id
  )
VALUES
  (
    'JA_2024_00001',
    '2024-03-01 09:00:00',
    '〇〇式会社',
    '2',
    '1',
    '東京都港区',
    FALSE,
    NULL,
    '0',
    NULL,
    '2024-03-01 17:00:00',
    NULL,
    '2024-02-28 10:00:00',
    '2024-02-28 10:00:00',
    'JS_2024_00001'
  ),
  (
    'JA_2024_00002',
    '2024-03-02 10:00:00',
    '△△株式会社',
    '3',
    '2',
    '大阪府大阪市',
    TRUE,
    TRUE,
    '1',
    '10:00:00',
    '2024-03-02 18:00:00',
    '試験面接',
    '2024-03-01 10:00:00',
    '2024-03-01 10:00:00',
    'JS_2024_00002'
  ),
  (
    'JA_2024_00003',
    '2024-03-03 11:00:00',
    '株式会社□□□',
    '3',
    '2',
    '神奈川県横浜市',
    TRUE,
    FALSE,
    '2',
    '13:30:00',
    '2024-03-03 15:00:00',
    '試験適正',
    '2024-03-02 10:00:00',
    '2024-03-02 10:00:00',
    'JS_2024_00003'
  ),
  (
    'JA_2024_00004',
    '2024-03-04 12:00:00',
    '株式会社×××',
    '8',
    '0',
    '北海道札幌市',
    FALSE,
    NULL,
    '3',
    NULL,
    '2024-03-04 16:00:00',
    '研修',
    '2024-03-03 10:00:00',
    '2024-03-03 10:00:00',
    'JS_2024_00004'
  ),
  (
    'JA_2024_00005',
    '2024-03-05 13:00:00',
    '〇〇〇株式会社',
    '5',
    '2',
    '愛知県名古屋市',
    FALSE,
    NULL,
    '2',
    '10:30:00',
    '2024-03-05 14:30:00',
    'インターン',
    '2024-03-04 10:00:00',
    '2024-03-04 10:00:00',
    'JS_2024_00005'
  ),
  (
    'JA_2024_00006',
    '2024-03-05 13:00:00',
    '〇〇〇株式会社',
    '5',
    '2',
    '愛知県名古屋市',
    FALSE,
    NULL,
    '2',
    '11:30:00',
    '2024-03-05 15:30:00',
    'その他',
    '2024-03-05 10:00:00',
    '2024-03-05 10:00:00',
    'JS_2024_00006'
  );
--
INSERT INTO
  JOB_SEARCH_REPORT_T (
    job_report_id,
    report_content,
    result,
    created_at,
    updated_at,
    job_search_id
  )
VALUES
  (
    'JR_2024_00001',
    '面接結果: 合格',
    '2',
    '2024-03-02 10:00:00',
    '2024-03-02 10:00:00',
    'JS_2024_00001'
  );
--
INSERT INTO
  EXAM_REPORT_T (
    exam_report_id,
    exam_opponent_count,
    exam_opponent_position,
    exam_count,
    exam_type,
    exam_content,
    impressions,
    created_at,
    updated_at,
    job_search_id
  )
VALUES
  (
    'ER_2024_00001',
    5,
    'マネージャー',
    '1',
    '1',
    '適性検査をしました。',
    '難しかったが良い経験だった。',
    '2024-02-01 10:00:00',
    '2024-02-01 10:00:00',
    'JS_2024_00001'
  ),
  (
    'ER_2024_00002',
    5,
    'マネージャー',
    '1',
    '1',
    '適性検査をしました。',
    '難しかったが良い経験だった。',
    '2024-02-01 10:00:00',
    '2024-02-01 10:00:00',
    'JS_2024_00002'
  ),
  (
    'ER_2024_00003',
    5,
    'マネージャー',
    '1',
    '1',
    '適性検査をしました。',
    '難しかったが良い経験だった。',
    '2024-02-01 10:00:00',
    '2024-02-01 10:00:00',
    'JS_2024_00006'
  );
--
INSERT INTO
  CERTIFICATE_ISSUANCE_T (
    certificate_issue_id,
    status,
    student_user_id,
    application_date,
    media_type,
    teacher_user_id,
    teacher_check_flag,
    office_user_id,
    approval_date
  )
VALUES
  (
    'CI_2024_00001',
    '0',
    'teacher@hcs.ac.jp',
    '2024-01-01',
    '0',
    'teacher@hcs.ac.jp',
    NULL,
    NULL,
    NULL
  ),
  (
    'CI_2024_00002',
    '1',
    'student@hcs.ac.jp',
    '2024-01-03',
    '1',
    'teacher@hcs.ac.jp',
    FALSE,
    NULL,
    '2024-01-06'
  ),
  (
    'CI_2024_00003',
    '4',
    'student@hcs.ac.jp',
    '2024-01-05',
    '2',
    'teacher@hcs.ac.jp',
    TRUE,
    'office@hcs.ac.jp',
    '2024-01-06'
  ),
  (
    'CI_2024_00004',
    '5',
    'student@hcs.ac.jp',
    '2024-01-07',
    '1',
    'teacher2@hcs.ac.jp',
    TRUE,
    'office@hcs.ac.jp',
    '2024-01-08'
  ),
  (
    'CI_2024_00005',
    '6',
    'student@hcs.ac.jp',
    '2024-01-09',
    '0',
    'teacher@hcs.ac.jp',
    TRUE,
    'office@hcs.ac.jp',
    '2024-01-09'
  );
--
INSERT INTO
  MAILING_M (postal_payment_id, postal_fee, postal_max_weight)
VALUES
  ('0', 110, 50);
--
INSERT INTO
  CERTIFICATE_M (
    certificate_id,
    certificate_name,
    certificate_fee,
    certificate_weight
  )
VALUES
  ('0', '在学証明書', 200, 7),
  ('1', '成績証明書', 200, 7),
  ('2', '卒業見込み証明書', 200, 7),
  ('3', '健康証明書', 200, 7);
--
INSERT INTO
  ELECTRONIC_CERTIFICATE_ISSUANCE_T (certificate_issue_id, recipient_email_address)
VALUES
  ('CI_2024_00005', 'student@hcs.ac.jp');
--
INSERT INTO
  PAPER_CERTIFICATE_ISSUANCE_T (
    certificate_issue_id,
    delivery_due_date,
    delivery_date
  )
VALUES
  ('CI_2024_00004', '2024-01-09', NULL),
  ('CI_2024_00005', '2024-01-10', '2024-01-11');
--
INSERT INTO
  MAILING_CERTIFICATE_ISSUANCE_T (
    certificate_issue_id,
    postal_payment_id,
    recipient_name,
    recipient_furigana,
    recipient_address,
    post_date
  )
VALUES
  (
    'CI_2024_00003',
    '0',
    '佐藤 三郎',
    'サトウ サブロウ',
    '220-0012 神奈川県横浜市3丁目 3-3',
    NULL
  );
--
INSERT INTO
  CERTIFICATE_ISSUANCE_DETAIL_T (
    certificate_issue_id,
    certificate_id,
    certificate_quantity
  )
VALUES
  ('CI_2024_00001', '0', 2),
  ('CI_2024_00002', '1', 1),
  ('CI_2024_00003', '2', 3),
  ('CI_2024_00004', '0', 2),
  ('CI_2024_00004', '2', 2),
  ('CI_2024_00004', '3', 4),
  ('CI_2024_00005', '1', 2);
--
INSERT INTO
  NOTIFICATION_T (
    notification_id,
    assigned_user_id,
    resend_flag,
    job_search_certificate_category,
    notification_timestamp
  )
VALUES
  (
    'N_2024_000001',
    'student@hcs.ac.jp',
    FALSE,
    '0',
    '2024-02-01 08:00:00'
  ),
  (
    'N_2024_000002',
    'teacher@hcs.ac.jp',
    FALSE,
    '0',
    '2024-02-02 09:00:00'
  ),
  (
    'N_2024_000003',
    'student@hcs.ac.jp',
    FALSE,
    '0',
    '2024-02-03 10:00:00'
  ),
  (
    'N_2024_000004',
    'teacher@hcs.ac.jp',
    FALSE,
    '1',
    '2024-02-04 11:00:00'
  ),
  (
    'N_2024_000005',
    'student@hcs.ac.jp',
    FALSE,
    '1',
    '2024-02-05 12:00:00'
  ),
  (
    'N_2024_000006',
    'student@hcs.ac.jp',
    TRUE,
    '1',
    '2024-01-09 12:00:00'
  ),
  (
    'N_2024_000007',
    'student@hcs.ac.jp',
    FALSE,
    '1',
    '2024-01-11 12:00:00'
  ),
  (
    'N_2024_000008',
    'office@hcs.ac.jp',
    FALSE,
    '1',
    '2024-01-11 12:00:00'
  );
--
INSERT INTO
  NOTIFICATION_JOB_SEARCH_T (notification_id, job_search_id)
VALUES
  ('N_2024_000001', 'JS_2024_00002'),
  ('N_2024_000002', 'JS_2024_00003'),
  ('N_2024_000003', 'JS_2024_00004');
--
INSERT INTO
  NOTIFICATION_CERTIFICATE_ISSUANCE_T (notification_id, certificate_issue_id)
VALUES
  ('N_2024_000004', 'CI_2024_00001'),
  ('N_2024_000005', 'CI_2024_00002'),
  ('N_2024_000006', 'CI_2024_00004'),
  ('N_2024_000007', 'CI_2024_00004'),
  ('N_2024_000008', 'CI_2024_00004');
--
INSERT INTO
  DELETED_USER_M (
    user_id,
    deleted_at,
    encrypted_password,
    user_name,
    user_status,
    user_type,
    created_by_user_id,
    created_at,
    updated_at
  )
VALUES
  (
    'del_user1@hcs.ac.jp',
    '2024-04-01 12:00:00',
    '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa',
    '削除 太郎',
    '3',
    '0',
    'admin@hcs.ac.jp',
    '2024-01-01 10:00:00',
    '2024-01-01 10:00:00'
  ),
  (
    'del_user2@hcs.ac.jp',
    '2024-04-02 12:00:00',
    '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa',
    '削除 次郎',
    '3',
    '1',
    'admin@hcs.ac.jp',
    '2024-01-02 10:00:00',
    '2024-01-02 10:00:00'
  ),
  (
    'del_user3@hcs.ac.jp',
    '2024-04-03 12:00:00',
    '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa',
    '削除 三郎',
    '3',
    '2',
    'admin@hcs.ac.jp',
    '2024-01-03 10:00:00',
    '2024-01-03 10:00:00'
  ),
  (
    'del_user4@hcs.ac.jp',
    '2024-04-04 12:00:00',
    '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa',
    '削除 四郎',
    '3',
    '0',
    'admin@hcs.ac.jp',
    '2024-01-04 10:00:00',
    '2024-01-04 10:00:00'
  ),
  (
    'del_user5@hcs.ac.jp',
    '2024-04-05 12:00:00',
    '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa',
    '削除 五郎',
    '3',
    '3',
    'admin@hcs.ac.jp',
    '2024-01-05 10:00:00',
    '2024-01-05 10:00:00'
  );
--
INSERT INTO
  DELETED_TEACHER_STUDENT_M (
    user_id,
    deleted_at,
    department,
    grade,
    class_name,
    attendance_number,
    student_id
  )
VALUES
  (
    'del_user1@hcs.ac.jp',
    '2024-04-01 12:00:00',
    'R',
    '1',
    'A1',
    '01',
    12345167
  ),
  (
    'del_user2@hcs.ac.jp',
    '2024-04-02 12:00:00',
    'S',
    '3',
    'A2',
    NULL,
    2345678
  ),
  (
    'del_user4@hcs.ac.jp',
    '2024-04-04 12:00:00',
    'G',
    '1',
    'A1',
    '04',
    45671890
  );
