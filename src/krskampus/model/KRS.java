package krskampus.model;

import java.sql.Date;

/**
 * Model class untuk tabel krs (transaksi)
 */
public class KRS {
    
    private int idKrs;
    private int idMahasiswa;
    private int idMatakuliah;
    private String semesterAmbil;
    private Date tanggalInput;
    
    // Field tambahan untuk display
    private String npmMahasiswa;
    private String namaMahasiswa;
    private String kodeMk;
    private String namaMk;
    private int sks;
    
    public KRS() {
    }
    
    public KRS(int idKrs, int idMahasiswa, int idMatakuliah, String semesterAmbil, Date tanggalInput) {
        this.idKrs = idKrs;
        this.idMahasiswa = idMahasiswa;
        this.idMatakuliah = idMatakuliah;
        this.semesterAmbil = semesterAmbil;
        this.tanggalInput = tanggalInput;
    }
    
    // Getters and Setters
    public int getIdKrs() {
        return idKrs;
    }
    
    public void setIdKrs(int idKrs) {
        this.idKrs = idKrs;
    }
    
    public int getIdMahasiswa() {
        return idMahasiswa;
    }
    
    public void setIdMahasiswa(int idMahasiswa) {
        this.idMahasiswa = idMahasiswa;
    }
    
    public int getIdMatakuliah() {
        return idMatakuliah;
    }
    
    public void setIdMatakuliah(int idMatakuliah) {
        this.idMatakuliah = idMatakuliah;
    }
    
    public String getSemesterAmbil() {
        return semesterAmbil;
    }
    
    public void setSemesterAmbil(String semesterAmbil) {
        this.semesterAmbil = semesterAmbil;
    }
    
    public Date getTanggalInput() {
        return tanggalInput;
    }
    
    public void setTanggalInput(Date tanggalInput) {
        this.tanggalInput = tanggalInput;
    }
    
    public String getNpmMahasiswa() {
        return npmMahasiswa;
    }
    
    public void setNpmMahasiswa(String npmMahasiswa) {
        this.npmMahasiswa = npmMahasiswa;
    }
    
    public String getNamaMahasiswa() {
        return namaMahasiswa;
    }
    
    public void setNamaMahasiswa(String namaMahasiswa) {
        this.namaMahasiswa = namaMahasiswa;
    }
    
    public String getKodeMk() {
        return kodeMk;
    }
    
    public void setKodeMk(String kodeMk) {
        this.kodeMk = kodeMk;
    }
    
    public String getNamaMk() {
        return namaMk;
    }
    
    public void setNamaMk(String namaMk) {
        this.namaMk = namaMk;
    }
    
    public int getSks() {
        return sks;
    }
    
    public void setSks(int sks) {
        this.sks = sks;
    }
}
