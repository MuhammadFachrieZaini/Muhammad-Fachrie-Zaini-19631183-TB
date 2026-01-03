package krskampus.view;

import krskampus.config.DatabaseConnection;
import krskampus.model.Matakuliah;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel untuk CRUD data Matakuliah
 */
public class MatakuliahPanel extends JPanel {
    
    private JTextField txtKodeMk, txtNamaMk, txtSks, txtSemester;
    private JTable tblMatakuliah;
    private DefaultTableModel tableModel;
    private JButton btnTambah, btnUpdate, btnHapus, btnClear;
    private int selectedId = -1;
    
    public MatakuliahPanel() {
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Input Data Mata Kuliah"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Kode MK
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Kode MK:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtKodeMk = new JTextField(20);
        formPanel.add(txtKodeMk, gbc);
        
        // Nama MK
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Nama Mata Kuliah:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNamaMk = new JTextField(20);
        formPanel.add(txtNamaMk, gbc);
        
        // SKS
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("SKS:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtSks = new JTextField(20);
        formPanel.add(txtSks, gbc);
        
        // Semester Paket
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Semester Paket:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtSemester = new JTextField(20);
        formPanel.add(txtSemester, gbc);
        
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
        String[] columns = {"ID", "Kode MK", "Nama Mata Kuliah", "SKS", "Semester Paket"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblMatakuliah = new JTable(tableModel);
        tblMatakuliah.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblMatakuliah.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblMatakuliah.getSelectedRow();
                if (row >= 0) {
                    selectedId = (int) tableModel.getValueAt(row, 0);
                    txtKodeMk.setText((String) tableModel.getValueAt(row, 1));
                    txtNamaMk.setText((String) tableModel.getValueAt(row, 2));
                    txtSks.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                    Object semesterVal = tableModel.getValueAt(row, 4);
                    txtSemester.setText(semesterVal != null ? String.valueOf(semesterVal) : "");
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblMatakuliah);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Mata Kuliah"));
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void loadData() {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM matakuliah ORDER BY id_matakuliah";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_matakuliah"),
                    rs.getString("kode_mk"),
                    rs.getString("nama_mk"),
                    rs.getInt("sks"),
                    rs.getObject("semester_paket")
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
        
        String sql = "INSERT INTO matakuliah (kode_mk, nama_mk, sks, semester_paket) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, txtKodeMk.getText().trim());
            pstmt.setString(2, txtNamaMk.getText().trim());
            pstmt.setInt(3, Integer.parseInt(txtSks.getText().trim()));
            String semester = txtSemester.getText().trim();
            if (!semester.isEmpty()) {
                pstmt.setInt(4, Integer.parseInt(semester));
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
            clearForm();
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "SKS dan Semester harus berupa angka!", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateData() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diupdate!");
            return;
        }
        if (!validateInput()) return;
        
        String sql = "UPDATE matakuliah SET kode_mk=?, nama_mk=?, sks=?, semester_paket=? WHERE id_matakuliah=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, txtKodeMk.getText().trim());
            pstmt.setString(2, txtNamaMk.getText().trim());
            pstmt.setInt(3, Integer.parseInt(txtSks.getText().trim()));
            String semester = txtSemester.getText().trim();
            if (!semester.isEmpty()) {
                pstmt.setInt(4, Integer.parseInt(semester));
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }
            pstmt.setInt(5, selectedId);
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
            clearForm();
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "SKS dan Semester harus berupa angka!", 
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
            String sql = "DELETE FROM matakuliah WHERE id_matakuliah=?";
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, selectedId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                clearForm();
                loadData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage() + 
                    "\nPastikan tidak ada data KRS yang terkait dengan mata kuliah ini.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearForm() {
        txtKodeMk.setText("");
        txtNamaMk.setText("");
        txtSks.setText("");
        txtSemester.setText("");
        selectedId = -1;
        tblMatakuliah.clearSelection();
    }
    
    private boolean validateInput() {
        if (txtKodeMk.getText().trim().isEmpty() || 
            txtNamaMk.getText().trim().isEmpty() || 
            txtSks.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kode MK, Nama MK, dan SKS harus diisi!");
            return false;
        }
        return true;
    }
    
    public List<Matakuliah> getAllMatakuliah() {
        List<Matakuliah> list = new ArrayList<>();
        String sql = "SELECT * FROM matakuliah ORDER BY nama_mk";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Matakuliah m = new Matakuliah();
                m.setIdMatakuliah(rs.getInt("id_matakuliah"));
                m.setKodeMk(rs.getString("kode_mk"));
                m.setNamaMk(rs.getString("nama_mk"));
                m.setSks(rs.getInt("sks"));
                m.setSemesterPaket(rs.getInt("semester_paket"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
