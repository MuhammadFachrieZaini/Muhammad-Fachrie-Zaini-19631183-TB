package krskampus.model;

/**
 * Model class untuk tabel matakuliah
 */
public class Matakuliah {
    
    private int idMatakuliah;
    private String kodeMk;
    private String namaMk;
    private int sks;
    private int semesterPaket;
    
    public Matakuliah() {
    }
    
    public Matakuliah(int idMatakuliah, String kodeMk, String namaMk, int sks, int semesterPaket) {
        this.idMatakuliah = idMatakuliah;
        this.kodeMk = kodeMk;
        this.namaMk = namaMk;
        this.sks = sks;
        this.semesterPaket = semesterPaket;
    }
    
    // Getters and Setters
    public int getIdMatakuliah() {
        return idMatakuliah;
    }
    
    public void setIdMatakuliah(int idMatakuliah) {
        this.idMatakuliah = idMatakuliah;
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
    
    public int getSemesterPaket() {
        return semesterPaket;
    }
    
    public void setSemesterPaket(int semesterPaket) {
        this.semesterPaket = semesterPaket;
    }
    
    @Override
    public String toString() {
        return kodeMk + " - " + namaMk + " (" + sks + " SKS)";
    }
}
