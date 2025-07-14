CREATE TABLE user_profiles (
user_id BIGINT PRIMARY KEY,
name VARCHAR(255) NOT NULL,
bio TEXT,
avatar_url TEXT,
github_link TEXT,
linkedin_link TEXT);