-- IMS database seed script
-- SysAdmin credentials:
-- Username: 24RP07206
-- Password: 24RP02943

CREATE TABLE IF NOT EXISTS app_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    username VARCHAR(60) NOT NULL UNIQUE,
    email VARCHAR(120) NOT NULL UNIQUE,
    password_hash VARCHAR(200) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

MERGE INTO app_users (
    full_name,
    username,
    email,
    password_hash,
    role,
    created_at
) KEY(username) VALUES (
    'System Admin',
    '24RP07206',
    'sysadmin@ims.local',
    '484e9083f02b548ebb2aef4f1f68ebd1a3eb2d4dfc37b251b28b1a1fa6838908',
    'ADMIN',
    CURRENT_TIMESTAMP
);
