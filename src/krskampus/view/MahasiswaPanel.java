package krskampus.view;

import krskampus.config.DatabaseConnection;
import krskampus.model.Mahasiswa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel untuk CRUD data Mahasiswa
 */
public class MahasiswaPanel extends JPanel {
    
    private JTextField txtNpm, txtNama, txtJurusan, txtAngkatan;
    private JTable tblMahasiswa;
    private DefaultTableModel tableModel;
    private JButton btnTambah, btnUpdate, btnHapus, btnClear;
    private int selectedId = -1;
    
    public MahasiswaPanel() {
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Input Data Mahasiswa"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // NPM
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("NPM:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNpm = new JTextField(20);
        formPanel.add(txtNpm, gbc);
        
        // Nama
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Nama Mahasiswa:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNama = new JTextField(20);
        formPanel.add(txtNama, gbc);
        
        // Jurusan
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Jurusan:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtJurusan = new JTextField(20);
        formPanel.add(txtJurusan, gbc);
        
        // Angkatan
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Angkatan:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtAngkatan = new JTextField(20);
        formPanel.add(txtAngkatan, gbc);
        
        // Panel Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnTambah = new JButton("Tambah");
        btnTambah.setBackground(new Color(40, 167, 69));
        btnTambah.setForeground(Color.WHITE);
        btnTambah.addActionListener(e -> tambahData());
        
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
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnClear);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        add(formPanel, BorderLayout.NORTH);
        
        // Tabel
        String[] columns = {"ID", "NPM", "Nama Mahasiswa", "Jurusan", "Angkatan"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblMahasiswa = new JTable(tableModel);
        tblMahasiswa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblMahasiswa.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblMahasiswa.getSelectedRow();
                if (row >= 0) {
                    selectedId = (int) tableModel.getValueAt(row, 0);
                    txtNpm.setText((String) tableModel.getValueAt(row, 1));
                    txtNama.setText((String) tableModel.getValueAt(row, 2));
                    txtJurusan.setText((String) tableModel.getValueAt(row, 3));
                    txtAngkatan.setText((String) tableModel.getValueAt(row, 4));
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblMahasiswa);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Mahasiswa"));
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void loadData() {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM mahasiswa ORDER BY id_mahasiswa";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_mahasiswa"),
                    rs.getString("npm"),
                    rs.getString("nama_mahasiswa"),
                    rs.getString("jurusan"),
                    rs.getString("angkatan")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void tambahData() {
        if (!validateInput()) return;
        
        String sql = "INSERT INTO mahasiswa (npm, nama_mahasiswa, jurusan, angkatan) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, txtNpm.getText().trim());
            pstmt.setString(2, txtNama.getText().trim());
            pstmt.setString(3, txtJurusan.getText().trim());
            pstmt.setString(4, txtAngkatan.getText().trim());
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
            clearForm();
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateData() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diupdate!");
            return;
        }
        if (!validateInput()) return;
        
        String sql = "UPDATE mahasiswa SET npm=?, nama_mahasiswa=?, jurusan=?, angkatan=? WHERE id_mahasiswa=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, txtNpm.getText().trim());
            pstmt.setString(2, txtNama.getText().trim());
            pstmt.setString(3, txtJurusan.getText().trim());
            pstmt.setString(4, txtAngkatan.getText().trim());
            pstmt.setInt(5, selectedId);
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
            clearForm();
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusData() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus data ini?", 
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM mahasiswa WHERE id_mahasiswa=?";
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, selectedId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                clearForm();
                loadData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage() + 
                    "\nPastikan tidak ada data KRS yang terkait dengan mahasiswa ini.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearForm() {
        txtNpm.setText("");
        txtNama.setText("");
        txtJurusan.setText("");
        txtAngkatan.setText("");
        selectedId = -1;
        tblMahasiswa.clearSelection();
    }
    
    private boolean validateInput() {
        if (txtNpm.getText().trim().isEmpty() || 
            txtNama.getText().trim().isEmpty() || 
            txtJurusan.getText().trim().isEmpty() || 
            txtAngkatan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return false;
        }
        return true;
    }
    
    public List<Mahasiswa> getAllMahasiswa() {
        List<Mahasiswa> list = new ArrayList<>();
        String sql = "SELECT * FROM mahasiswa ORDER BY nama_mahasiswa";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Mahasiswa m = new Mahasiswa();
                m.setIdMahasiswa(rs.getInt("id_mahasiswa"));
                m.setNpm(rs.getString("npm"));
                m.setNamaMahasiswa(rs.getString("nama_mahasiswa"));
                m.setJurusan(rs.getString("jurusan"));
                m.setAngkatan(rs.getString("angkatan"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
