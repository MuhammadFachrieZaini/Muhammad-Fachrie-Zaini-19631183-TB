package krskampus.view;

import krskampus.config.DatabaseConnection;
import krskampus.model.Mahasiswa;
import krskampus.model.Matakuliah;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel untuk Transaksi KRS
 */
public class KRSPanel extends JPanel {
    
    private JComboBox<Mahasiswa> cmbMahasiswa;
    private JComboBox<Matakuliah> cmbMatakuliah;
    private JTextField txtSemesterAmbil, txtTanggalInput;
    private JTable tblKRS;
    private DefaultTableModel tableModel;
    private JButton btnSimpan, btnUpdate, btnHapus, btnClear, btnRefresh;
    private int selectedId = -1;
    
    public KRSPanel() {
        initComponents();
        loadComboData();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Input Transaksi KRS"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Mahasiswa
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mahasiswa:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbMahasiswa = new JComboBox<>();
        formPanel.add(cmbMahasiswa, gbc);
        
        // Matakuliah
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Mata Kuliah:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbMatakuliah = new JComboBox<>();
        formPanel.add(cmbMatakuliah, gbc);
        
        // Semester Ambil
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Semester Ambil:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtSemesterAmbil = new JTextField(20);
        txtSemesterAmbil.setToolTipText("Contoh: Ganjil 2024/2025");
        formPanel.add(txtSemesterAmbil, gbc);
        
        // Tanggal Input
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Tanggal Input:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTanggalInput = new JTextField(20);
        txtTanggalInput.setToolTipText("Format: yyyy-MM-dd (contoh: 2024-08-20)");
        // Set default to today
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        txtTanggalInput.setText(sdf.format(new java.util.Date()));
        formPanel.add(txtTanggalInput, gbc);
        
        // Panel Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnSimpan = new JButton("Simpan");
        btnSimpan.setBackground(new Color(40, 167, 69));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.addActionListener(e -> simpanData());
        
        btnUpdate = new JButton("Update");
        btnUpdate.setBackground(new Color(0, 123, 255));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.addActionListener(e -> updateData());
        
        btnHapus = new JButton("Hapus");
        btnHapus.setBackground(new Color(220, 53, 69));
        btnHapus.setForeground(Color.WHITE);
        btnHapus.addActionListener(e -> hapusData());
        
        btnClear = new JButton("Clear");
        btnClear.addActionListener(e -> clearForm());
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(108, 117, 125));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.addActionListener(e -> {
            loadComboData();
            loadData();
        });
        
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnRefresh);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        add(formPanel, BorderLayout.NORTH);
        
        // Tabel
        String[] columns = {"ID", "NPM", "Nama Mahasiswa", "Kode MK", "Nama MK", "SKS", "Semester Ambil", "Tanggal Input"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblKRS = new JTable(tableModel);
        tblKRS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblKRS.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblKRS.getSelectedRow();
                if (row >= 0) {
                    selectedId = (int) tableModel.getValueAt(row, 0);
                    // Set combo box selections based on NPM and Kode MK
                    String npm = (String) tableModel.getValueAt(row, 1);
                    String kodeMk = (String) tableModel.getValueAt(row, 3);
                    selectMahasiswaByNpm(npm);
                    selectMatakuliahByKode(kodeMk);
                    txtSemesterAmbil.setText((String) tableModel.getValueAt(row, 6));
                    txtTanggalInput.setText(String.valueOf(tableModel.getValueAt(row, 7)));
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblKRS);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Transaksi KRS"));
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadComboData() {
        // Load Mahasiswa
        cmbMahasiswa.removeAllItems();
        String sqlMhs = "SELECT * FROM mahasiswa ORDER BY nama_mahasiswa";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlMhs)) {
            
            while (rs.next()) {
                Mahasiswa m = new Mahasiswa();
                m.setIdMahasiswa(rs.getInt("id_mahasiswa"));
                m.setNpm(rs.getString("npm"));
                m.setNamaMahasiswa(rs.getString("nama_mahasiswa"));
                cmbMahasiswa.addItem(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Load Matakuliah
        cmbMatakuliah.removeAllItems();
        String sqlMk = "SELECT * FROM matakuliah ORDER BY nama_mk";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlMk)) {
            
            while (rs.next()) {
                Matakuliah mk = new Matakuliah();
                mk.setIdMatakuliah(rs.getInt("id_matakuliah"));
                mk.setKodeMk(rs.getString("kode_mk"));
                mk.setNamaMk(rs.getString("nama_mk"));
                mk.setSks(rs.getInt("sks"));
                cmbMatakuliah.addItem(mk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void loadData() {
        tableModel.setRowCount(0);
        String sql = """
            SELECT k.id_krs, m.npm, m.nama_mahasiswa, mk.kode_mk, mk.nama_mk, mk.sks, 
                   k.semester_ambil, k.tanggal_input
            FROM krs k
            JOIN mahasiswa m ON k.id_mahasiswa = m.id_mahasiswa
            JOIN matakuliah mk ON k.id_matakuliah = mk.id_matakuliah
            ORDER BY k.id_krs DESC
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_krs"),
                    rs.getString("npm"),
                    rs.getString("nama_mahasiswa"),
                    rs.getString("kode_mk"),
                    rs.getString("nama_mk"),
                    rs.getInt("sks"),
                    rs.getString("semester_ambil"),
                    rs.getDate("tanggal_input")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void simpanData() {
        if (!validateInput()) return;
        
        Mahasiswa mhs = (Mahasiswa) cmbMahasiswa.getSelectedItem();
        Matakuliah mk = (Matakuliah) cmbMatakuliah.getSelectedItem();
        
        String sql = "INSERT INTO krs (id_mahasiswa, id_matakuliah, semester_ambil, tanggal_input) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, mhs.getIdMahasiswa());
            pstmt.setInt(2, mk.getIdMatakuliah());
            pstmt.setString(3, txtSemesterAmbil.getText().trim());
            pstmt.setDate(4, Date.valueOf(txtTanggalInput.getText().trim()));
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Transaksi KRS berhasil disimpan!");
            clearForm();
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Format tanggal tidak valid! Gunakan format: yyyy-MM-dd", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateData() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diupdate!");
            return;
        }
        if (!validateInput()) return;
        
        Mahasiswa mhs = (Mahasiswa) cmbMahasiswa.getSelectedItem();
        Matakuliah mk = (Matakuliah) cmbMatakuliah.getSelectedItem();
        
        String sql = "UPDATE krs SET id_mahasiswa=?, id_matakuliah=?, semester_ambil=?, tanggal_input=? WHERE id_krs=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, mhs.getIdMahasiswa());
            pstmt.setInt(2, mk.getIdMatakuliah());
            pstmt.setString(3, txtSemesterAmbil.getText().trim());
            pstmt.setDate(4, Date.valueOf(txtTanggalInput.getText().trim()));
            pstmt.setInt(5, selectedId);
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data KRS berhasil diupdate!");
            clearForm();
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Format tanggal tidak valid! Gunakan format: yyyy-MM-dd", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusData() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus transaksi KRS ini?", 
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM krs WHERE id_krs=?";
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, selectedId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Transaksi KRS berhasil dihapus!");
                clearForm();
                loadData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearForm() {
        if (cmbMahasiswa.getItemCount() > 0) cmbMahasiswa.setSelectedIndex(0);
        if (cmbMatakuliah.getItemCount() > 0) cmbMatakuliah.setSelectedIndex(0);
        txtSemesterAmbil.setText("");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        txtTanggalInput.setText(sdf.format(new java.util.Date()));
        selectedId = -1;
        tblKRS.clearSelection();
    }
    
    private boolean validateInput() {
        if (cmbMahasiswa.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih mahasiswa!");
            return false;
        }
        if (cmbMatakuliah.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih mata kuliah!");
            return false;
        }
        if (txtSemesterAmbil.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semester ambil harus diisi!");
            return false;
        }
        if (txtTanggalInput.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tanggal input harus diisi!");
            return false;
        }
        return true;
    }
    
    private void selectMahasiswaByNpm(String npm) {
        for (int i = 0; i < cmbMahasiswa.getItemCount(); i++) {
            Mahasiswa m = cmbMahasiswa.getItemAt(i);
            if (m.getNpm().equals(npm)) {
                cmbMahasiswa.setSelectedIndex(i);
                break;
            }
        }
    }
    
    private void selectMatakuliahByKode(String kodeMk) {
        for (int i = 0; i < cmbMatakuliah.getItemCount(); i++) {
            Matakuliah mk = cmbMatakuliah.getItemAt(i);
            if (mk.getKodeMk().equals(kodeMk)) {
                cmbMatakuliah.setSelectedIndex(i);
                break;
            }
        }
    }
}
