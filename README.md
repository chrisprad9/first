# Dynamic Invoice Scheduler & Executor Engine

Proyek ini adalah implementasi sistem pemrosesan invoice berbasis *Event-Driven Microservices* dan *Polyglot Persistence*. Sistem dibagi menjadi dua layanan mandiri dalam struktur *Monorepo*:
1. **invoice-scheduler (Port 8080)**: Menggunakan PostgreSQL untuk mengelola kontrak jadwal partner dan Redis untuk optimasi *caching query*. Bertindak sebagai Kafka Producer.
2. **invoice-executor (Port 8081)**: Bertindak sebagai Kafka Consumer yang memproses invoice secara *asynchronous* dan mencatat *audit trail* ke MongoDB.

---

## Arsitektur Sistem & Komponen Ekosistem

* **Relational DB**: PostgreSQL 15 (Skema dikelola otomatis oleh Flyway Migration)
* **In-Memory Data Grid**: Redis 7 (Cache-Aside Strategy untuk optimasi API)
* **Message Broker**: Apache Kafka 3.8 (KRaft Mode - Tanpa Zookeeper)
* **NoSQL Document Store**: MongoDB 6.0 (Audit Trail / Invoice History)

---

## Prasyarat (Prerequisites)

Sebelum menjalankan aplikasi, pastikan perangkat Anda sudah terpasang:
* **Java 17** atau versi di atasnya
* **Docker & Docker Compose**

---

## Langkah Menjalankan Aplikasi (Lokal Terminal)

Ikuti langkah-langkah di bawah ini untuk menyalakan seluruh ekosistem di perangkat lokal Anda:

### Langkah 1: Jalankan Infrastruktur Kontainer (Docker)
Buka terminal di direktori *root* proyek (tempat file `compose.yaml` berada), lalu jalankan:
```bash
docker compose up -d
```
*Pastikan kontainer Postgres, Kafka, Redis, dan MongoDB sudah berstatus 'Running' sebelum lanjut ke langkah berikutnya.*

### Langkah 2: Jalankan Aplikasi 'invoice-scheduler'
Buka jendela atau tab terminal baru, masuk ke folder sub-proyek scheduler, lalu jalankan:
```bash
cd invoice-scheduler
./gradlew bootRun
```
*Layanan ini akan berjalan di port `8080` dan otomatis melakukan migrasi skema database via Flyway.*

### Langkah 3: Jalankan Aplikasi 'invoice-executor'
Buka jendela atau tab terminal baru lagi, masuk ke folder sub-proyek executor, lalu jalankan:
```bash
cd invoice-executor
./gradlew bootRun
```
*Layanan ini akan berjalan di port `8081` dan langsung mendengarkan antrean dari Kafka Topic.*

---

## Panduan Pengujian API & Verifikasi Sistem

Anda dapat melakukan pengujian fungsionalitas sistem menggunakan `cURL` langsung dari terminal Anda:

### 1. Uji Validasi Jadwal Dinamis & Redis Caching (Scheduler Layer)
Jalankan perintah `GET` berikut untuk mensimulasikan pencarian jadwal aktif pada tanggal akhir bulan (`2026-06-30`):
```bash
curl -X GET "http://localhost:8080/api/v1/schedules/valid?action=SEND_INVOICE&date=2026-06-30"
```
* **Verifikasi Cache**: Pada eksekusi pertama, aplikasi akan memicu *Native Query* ke PostgreSQL (`Cache Miss`). Pada eksekusi kedua dan seterusnya, respon akan kembali secara instan langsung dari Redis Cache (`Cache Hit`).

### 2. Uji Simulasi Batching Engine (End-to-End Event Driven)
Jalankan perintah `POST` berikut untuk memicu mesin *batch* mencari partner yang valid dan mendistribusikannya ke Kafka:
```bash
curl -X POST "http://localhost:8080/api/v1/schedules/run?action=SEND_INVOICE&date=2026-06-30"
```

**Hasil yang Dapat Dilihat di Log Terminal:**
* **Terminal Scheduler (8080)** akan memunculkan log keberhasilan push data ke broker:
  `========== [PRODUCER] Success push Partner ID X to Kafka Topic ==========`
* **Terminal Executor (8081)** secara otomatis akan menangkap event dari broker dan memproses dokumen ke MongoDB:
  `[MONGO] Success writing audit trail with Document ID: [Auto-Generated-UUID]`
