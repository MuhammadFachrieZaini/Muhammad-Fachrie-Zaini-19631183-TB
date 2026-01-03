package krskampus.view;

import krskampus.config.DatabaseConnection;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

/**
 * Panel untuk Laporan dan Export CSV
 */
public class LaporanPanel extends JPanel {
    
    private JButton btnLaporanKRS, btnLaporanSKS;
    private JTable tblPreview;
    private DefaultTableModel tableModel;
    private JLabel lblStatus;
    
    public LaporanPanel() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel Tombol Laporan
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Pilih Laporan"));
        
        btnLaporanKRS = new JButton("Laporan KRS per Mahasiswa");
        btnLaporanKRS.setBackground(new Color(23, 162, 184));
        btnLaporanKRS.setForeground(Color.WHITE);
        btnLaporanKRS.setPreferredSize(new Dimension(220, 40));
        btnLaporanKRS.addActionListener(e -> generateLaporanKRS());
        
        btnLaporanSKS = new JButton("Laporan Total SKS per Semester");
        btnLaporanSKS.setBackground(new Color(102, 16, 242));
        btnLaporanSKS.setForeground(Color.WHITE);
        btnLaporanSKS.setPreferredSize(new Dimension(220, 40));
        btnLaporanSKS.addActionListener(e -> generateLaporanSKS());
        
        buttonPanel.add(btnLaporanKRS);
        buttonPanel.add(btnLaporanSKS);
        
        add(buttonPanel, BorderLayout.NORTH);
        
        // Tabel Preview
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPreview = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblPreview);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Preview Data Laporan"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Status Label
        lblStatus = new JLabel("Pilih salah satu laporan untuk melihat preview dan export ke CSV");
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblStatus, BorderLayout.SOUTH);
    }
    
    private void generateLaporanKRS() {
        String sql = """
            SELECT m.npm AS 'NPM', m.nama_mahasiswa AS 'Nama Mahasiswa', 
                   m.jurusan AS 'Jurusan', mk.kode_mk AS 'Kode MK', 
                   mk.nama_mk AS 'Nama Mata Kuliah', mk.sks AS 'SKS',
                   k.semester_ambil AS 'Semester Ambil', k.tanggal_input AS 'Tanggal Input'
            FROM krs k
            JOIN mahasiswa m ON k.id_mahasiswa = m.id_mahasiswa
            JOIN matakuliah mk ON k.id_matakuliah = mk.id_matakuliah
            ORDER BY m.nama_mahasiswa, k.semester_ambil, mk.nama_mk
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Clear table
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            
            // Set columns
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columns = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columns[i-1] = metaData.getColumnLabel(i);
            }
            tableModel.setColumnIdentifiers(columns);
            
            // Add rows
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i-1] = rs.getObject(i);
                }
                tableModel.addRow(row);
            }
            
            lblStatus.setText("Preview Laporan KRS per Mahasiswa - " + tableModel.getRowCount() + " baris data");
            
            // Ask to export
            int option = JOptionPane.showConfirmDialog(this, 
                "Data berhasil dimuat. Apakah Anda ingin export ke file CSV?", 
                "Export CSV", JOptionPane.YES_NO_OPTION);
            
            if (option == JOptionPane.YES_OPTION) {
                exportToCSV("Laporan_KRS_Mahasiswa");
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void generateLaporanSKS() {
        String sql = """
            SELECT k.semester_ambil AS 'Semester Ambil', 
                   m.npm AS 'NPM', 
                   m.nama_mahasiswa AS 'Nama Mahasiswa',
                   COUNT(mk.id_matakuliah) AS 'Jumlah MK',
                   SUM(mk.sks) AS 'Total SKS'
            FROM krs k
            JOIN mahasiswa m ON k.id_mahasiswa = m.id_mahasiswa
            JOIN matakuliah mk ON k.id_matakuliah = mk.id_matakuliah
            GROUP BY k.semester_ambil, m.id_mahasiswa
            ORDER BY k.semester_ambil, m.nama_mahasiswa
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Clear table
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            
            // Set columns
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columns = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columns[i-1] = metaData.getColumnLabel(i);
            }
            tableModel.setColumnIdentifiers(columns);
            
            // Add rows
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i-1] = rs.getObject(i);
                }
                tableModel.addRow(row);
            }
            
            lblStatus.setText("Preview Laporan Total SKS per Semester - " + tableModel.getRowCount() + " baris data");
            
            // Ask to export
            int option = JOptionPane.showConfirmDialog(this, 
                "Data berhasil dimuat. Apakah Anda ingin export ke file CSV?", 
                "Export CSV", JOptionPane.YES_NO_OPTION);
            
            if (option == JOptionPane.YES_OPTION) {
                exportToCSV("Laporan_SKS_Semester");
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exportToCSV(String defaultFileName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan File CSV");
        fileChooser.setSelectedFile(new java.io.File(defaultFileName + ".csv"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files (*.csv)", "csv"));
        
        int result = fileChooser.showSaveDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".csv")) {
                filePath += ".csv";
            }
            
            try (FileWriter writer = new FileWriter(filePath)) {
                // Write header
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    writer.append(escapeCSV(tableModel.getColumnName(i)));
                    if (i < tableModel.getColumnCount() - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
                
                // Write data
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    for (int col = 0; col < tableModel.getColumnCount(); col++) {
                        Object value = tableModel.getValueAt(row, col);
                        writer.append(escapeCSV(value != null ? value.toString() : ""));
                        if (col < tableModel.getColumnCount() - 1) {
                            writer.append(",");
                        }
                    }
                    writer.append("\n");
                }
                
                JOptionPane.showMessageDialog(this, 
                    "File berhasil disimpan:\n" + filePath, 
                    "Export Berhasil", JOptionPane.INFORMATION_MESSAGE);
                lblStatus.setText("File CSV berhasil disimpan: " + filePath);
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saat menyimpan file: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private String escapeCSV(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
