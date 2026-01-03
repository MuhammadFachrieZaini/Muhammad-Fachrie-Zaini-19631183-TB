package krskampus.model;

/**
 * Model class untuk tabel mahasiswa
 */
public class Mahasiswa {
    
    private int idMahasiswa;
    private String npm;
    private String namaMahasiswa;
    private String jurusan;
    private String angkatan;
    
    public Mahasiswa() {
    }
    
    public Mahasiswa(int idMahasiswa, String npm, String namaMahasiswa, String jurusan, String angkatan) {
        this.idMahasiswa = idMahasiswa;
        this.npm = npm;
        this.namaMahasiswa = namaMahasiswa;
        this.jurusan = jurusan;
        this.angkatan = angkatan;
    }
    
    // Getters and Setters
    public int getIdMahasiswa() {
        return idMahasiswa;
    }
    
    public void setIdMahasiswa(int idMahasiswa) {
        this.idMahasiswa = idMahasiswa;
    }
    
    public String getNpm() {
        return npm;
    }
    
    public void setNpm(String npm) {
        this.npm = npm;
    }
    
    public String getNamaMahasiswa() {
        return namaMahasiswa;
    }
    
    public void setNamaMahasiswa(String namaMahasiswa) {
        this.namaMahasiswa = namaMahasiswa;
    }
    
    public String getJurusan() {
        return jurusan;
    }
    
    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }
    
    public String getAngkatan() {
        return angkatan;
    }
    
    public void setAngkatan(String angkatan) {
        this.angkatan = angkatan;
    }
    
    @Override
    public String toString() {
        return npm + " - " + namaMahasiswa;
    }
}
