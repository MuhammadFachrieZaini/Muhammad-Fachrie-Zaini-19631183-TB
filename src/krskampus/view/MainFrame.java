package krskampus.view;

import javax.swing.*;
import java.awt.*;

/**
 * Frame utama aplikasi dengan navigasi tab
 */
public class MainFrame extends JFrame {
    
    private JTabbedPane tabbedPane;
    private MahasiswaPanel mahasiswaPanel;
    private MatakuliahPanel matakuliahPanel;
    private KRSPanel krsPanel;
    private LaporanPanel laporanPanel;
    
    public MainFrame() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Sistem Informasi KRS Kampus");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 58, 64));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel lblTitle = new JLabel("SISTEM INFORMASI KRS KAMPUS", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // Initialize panels
        mahasiswaPanel = new MahasiswaPanel();
        matakuliahPanel = new MatakuliahPanel();
        krsPanel = new KRSPanel();
        laporanPanel = new LaporanPanel();
        
        // Add tabs
        tabbedPane.addTab("Data Mahasiswa", mahasiswaPanel);
        tabbedPane.addTab("Data Mata Kuliah", matakuliahPanel);
        tabbedPane.addTab("Transaksi KRS", krsPanel);
        tabbedPane.addTab("Laporan", laporanPanel);
        
        // Refresh data when tab changes
        tabbedPane.addChangeListener(e -> {
            int index = tabbedPane.getSelectedIndex();
            switch (index) {
                case 0 -> mahasiswaPanel.loadData();
                case 1 -> matakuliahPanel.loadData();
                case 2 -> krsPanel.loadData();
            }
        });
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(248, 249, 250));
        footerPanel.setPreferredSize(new Dimension(getWidth(), 30));
        
        JLabel lblFooter = new JLabel("© 2024 Sistem KRS Kampus - Tugas Besar");
        lblFooter.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblFooter.setForeground(Color.GRAY);
        footerPanel.add(lblFooter);
        
        add(footerPanel, BorderLayout.SOUTH);
    }
}
