-- V1__init_schema.sql

CREATE TABLE business_partner
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    status     VARCHAR(30) DEFAULT 'ACTIVE',
    created_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE partner_schedule
(
    id         BIGSERIAL PRIMARY KEY,
    action     VARCHAR(30) NOT NULL,
    partner_id BIGINT      NOT NULL,
    frequency  VARCHAR(20) NOT NULL, -- 'WEEKLY', 'MONTHLY', etc.
    day        INT,
    is_active  BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (partner_id) REFERENCES business_partner (id) ON DELETE CASCADE
);

CREATE INDEX idx_partner_schedule_action ON partner_schedule (action);
