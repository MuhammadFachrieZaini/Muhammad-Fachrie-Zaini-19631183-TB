-- 1. Buat Database
CREATE DATABASE db_krs_kampus;
USE db_krs_kampus;

-- ==========================================
-- TABEL MASTER 1: MAHASISWA
-- Menyimpan data diri mahasiswa
-- ==========================================
CREATE TABLE mahasiswa (
    id_mahasiswa INT AUTO_INCREMENT PRIMARY KEY,
    npm VARCHAR(20) NOT NULL UNIQUE,
    nama_mahasiswa VARCHAR(100) NOT NULL,
    jurusan VARCHAR(50) NOT NULL,
    angkatan CHAR(4) NOT NULL
);

-- Isi data dummy Mahasiswa
INSERT INTO mahasiswa (npm, nama_mahasiswa, jurusan, angkatan) VALUES 
('231001', 'Ahmad Dani', 'Teknik Informatika', '2023'),
('231002', 'Bunga Citra', 'Sistem Informasi', '2023'),
('211055', 'Candra Wijaya', 'Teknik Informatika', '2021');

-- ==========================================
-- TABEL MASTER 2: MATA KULIAH
-- Menyimpan daftar pelajaran yang tersedia
-- ==========================================
CREATE TABLE matakuliah (
    id_matakuliah INT AUTO_INCREMENT PRIMARY KEY,
    kode_mk VARCHAR(10) NOT NULL,
    nama_mk VARCHAR(100) NOT NULL,
    sks INT NOT NULL,
    semester_paket INT -- Misal: Ini MK untuk semester 3
);

-- Isi data dummy Mata Kuliah
INSERT INTO matakuliah (kode_mk, nama_mk, sks, semester_paket) VALUES 
('TI01', 'Pemrograman Berorientasi Objek', 4, 3),
('TI02', 'Basis Data', 3, 3),
('SI01', 'Manajemen Bisnis', 2, 1);

-- ==========================================
-- TABEL TRANSAKSI: PENGAMBILAN KRS
-- Menyimpan data 'belanja' SKS mahasiswa
-- ==========================================
CREATE TABLE krs (
    id_krs INT AUTO_INCREMENT PRIMARY KEY,
    id_mahasiswa INT,
    id_matakuliah INT,
    semester_ambil VARCHAR(20) NOT NULL, -- Contoh: "Ganjil 2024/2025"
    tanggal_input DATE NOT NULL,
    FOREIGN KEY (id_mahasiswa) REFERENCES mahasiswa(id_mahasiswa),
    FOREIGN KEY (id_matakuliah) REFERENCES matakuliah(id_matakuliah)
);

-- Isi data dummy Transaksi KRS
-- Ahmad Dani mengambil PBO dan Basis Data
INSERT INTO krs (id_mahasiswa, id_matakuliah, semester_ambil, tanggal_input) VALUES 
(1, 1, 'Ganjil 2024/2025', '2024-08-20'),
(1, 2, 'Ganjil 2024/2025', '2024-08-20'),
-- Bunga Citra mengambil Manajemen Bisnis
(2, 3, 'Ganjil 2024/2025', '2024-08-21');