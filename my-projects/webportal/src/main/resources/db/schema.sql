/* タスクテーブル */
CREATE TABLE IF NOT EXISTS t_task (
  id INT PRIMARY KEY,
  user_id VARCHAR(50),
  title VARCHAR(50),
  limit_day DATETIME,
  priority VARCHAR(10),
  complate BOOLEAN
);
/* プロフィールテーブル */
CREATE TABLE IF NOT EXISTS t_profile (
  p_id INT PRIMARY KEY,
  user_id VARCHAR(50),
  user_name VARCHAR(50),
  user_kana VARCHAR(50),
  class_number VARCHAR(10),
  image VARCHAR(1000),
  comment_text TEXT
);

/* ユーザマスタ */
CREATE TABLE IF NOT EXISTS m_user (
  user_id VARCHAR(50) PRIMARY KEY,
  encrypted_password VARCHAR(100),
  user_name VARCHAR(50),
  role VARCHAR(50),
 enabled BOOLEAN
);
