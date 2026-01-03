# Petunjuk Menjalankan Aplikasi KRS Kampus

## Prasyarat
1. **MySQL Server** sudah terinstall dan berjalan
2. **NetBeans IDE 25** sudah terinstall
3. **MySQL Connector/J** (JDBC Driver)

## Langkah-langkah Setup

### 1. Buat Database
Jalankan file `db_krs_kampus.sql` di MySQL:
```sql
source c:/Users/Saputra/Documents/NetBeansProjects/Muhammad Fachrie Zaini-19631183-TB/db_krs_kampus.sql;
```
Atau buka file tersebut di MySQL Workbench/phpMyAdmin dan jalankan seluruh isinya.

### 2. Tambahkan MySQL Connector ke NetBeans

**Cara 1: Menggunakan Library yang sudah ada di NetBeans**
1. Buka project di NetBeans
2. Klik kanan pada **Libraries** di Project Explorer
3. Pilih **Add Library...**
4. Cari dan pilih **MySQL JDBC Driver** atau **MySQL Connector**
5. Klik **Add Library**

**Cara 2: Download dan tambahkan manual**
1. Download MySQL Connector/J dari: https://dev.mysql.com/downloads/connector/j/
2. Pilih "Platform Independent" > Download ZIP atau TAR
3. Extract dan copy file `mysql-connector-j-8.x.x.jar` ke folder `lib/` project
4. Di NetBeans, klik kanan **Libraries** > **Add JAR/Folder...**
5. Pilih file JAR tersebut

### 3. Konfigurasi Database (jika perlu)
Jika MySQL Anda menggunakan password atau port berbeda, edit file:
`src/krskampus/config/DatabaseConnection.java`

```java
private static final String URL = "jdbc:mysql://localhost:3306/db_krs_kampus";
private static final String USER = "root";
private static final String PASSWORD = ""; // Isi password MySQL Anda
```

### 4. Jalankan Aplikasi
1. Buka project di NetBeans IP 25
2. Klik kanan project > **Run** atau tekan **F6**
3. Aplikasi akan terbuka dengan 4 tab:
   - **Data Mahasiswa**: CRUD data mahasiswa
   - **Data Mata Kuliah**: CRUD data mata kuliah  
   - **Transaksi KRS**: Input pengambilan KRS
   - **Laporan**: Export laporan ke CSV

## Fitur Aplikasi

### CRUD Mahasiswa
- Tambah, Edit, Hapus data mahasiswa
- Field: NPM, Nama, Jurusan, Angkatan

### CRUD Mata Kuliah
- Tambah, Edit, Hapus data mata kuliah
- Field: Kode MK, Nama MK, SKS, Semester Paket

### Transaksi KRS
- Pilih mahasiswa dan mata kuliah dari dropdown
- Input semester ambil dan tanggal
- Simpan, Update, Hapus transaksi

### Laporan (Export CSV)
1. **Laporan KRS per Mahasiswa**: Detail KRS setiap mahasiswa
2. **Laporan Total SKS per Semester**: Rekap total SKS per mahasiswa per semester

## Troubleshooting

### Error "MySQL JDBC Driver tidak ditemukan"
- Pastikan MySQL Connector JAR sudah ditambahkan ke Libraries

### Error "Gagal koneksi ke database"
- Pastikan MySQL Server sudah berjalan
- Periksa username dan password di DatabaseConnection.java
- Pastikan database `db_krs_kampus` sudah dibuat
