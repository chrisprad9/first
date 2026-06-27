-- V2__seed_data.sql

-- 1. Insert Data Master Business Partner
INSERT INTO business_partner (name, status) VALUES
        ('PT Teknologi Maju', 'ACTIVE'),
        ('CV Sumber Makmur', 'ACTIVE'),
        ('Global Trade Corp', 'ACTIVE'),
        ('Toko Kelontong Sukses', 'INACTIVE'); -- Partner non-aktif untuk ngetes filter

-- 2. Insert Data Schedule Invoice untuk masing-masing Partner
-- Kita pakai action 'SEND_INVOICE' sesuai kebutuhan index kamu
INSERT INTO partner_schedule (action, partner_id, frequency, day, is_active) VALUES
-- PT Teknologi Maju: Tagihan mingguan setiap hari Selasa (2)
('SEND_INVOICE', 1, 'WEEKLY', 2, true),

-- PT Teknologi Maju: Ada tagihan bulanan juga setiap tanggal 25
('SEND_INVOICE', 1, 'MONTHLY', 25, true),

-- CV Sumber Makmur: Tagihan bulanan setiap HARI TERAKHIR bulan (0)
('SEND_INVOICE', 2, 'MONTHLY', 0, true),

-- Global Trade Corp: Tagihan bulanan setiap 3 HARI SEBELUM AKHIR BULAN (-2)
('SEND_INVOICE', 3, 'MONTHLY', -2, true),

-- Toko Kelontong Sukses: Tagihan aktif tapi partner-nya INACTIVE (harus tersaring nanti)
('SEND_INVOICE', 4, 'MONTHLY', 10, true),

-- Global Trade Corp: Schedule-nya yang dimatikan (is_active = false)
('SEND_INVOICE', 3, 'WEEKLY', 5, false);
