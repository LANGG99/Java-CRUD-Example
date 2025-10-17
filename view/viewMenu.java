package view;

import java.awt.EventQueue;

import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.dao_menu;
import model.model_menu;
import java.awt.event.*;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;


public class viewMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tidMenu;
	private JTextField tnmMenu;
	private JTextField tHarga;
	private JTextField bCari;
	private JTable table;
	
	private dao_menu dao = new dao_menu();

	
	/**Launch the application.**/
	public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
        public void run() {
            try {
                loginView login = new loginView(); // ini buka form login
                login.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
	}


	private void tampilData() {
	    String[] kolom = {"ID Menu", "Nama Menu", "Harga"};
	    DefaultTableModel model = new DefaultTableModel(kolom, 0);

	    List<model_menu> list = dao.getAll();
	    for (model_menu m : list) {
	        Object[] data = {m.getIdMenu(), m.getNamaMenu(), m.getHarga()};
	        model.addRow(data);
	    }
	    table.setModel(model);
	}

	// ===== Method Tambah Data =====
	private void tambahData() {
	    try {
	        model_menu m = new model_menu();
	        m.setIdMenu(tidMenu.getText());
	        m.setNamaMenu(tnmMenu.getText());
	        m.setHarga(Double.parseDouble(tHarga.getText()));

	        dao.insert(m);
	        JOptionPane.showMessageDialog(this, "Data berhasil ditambah!");
	        tampilData();
	        batal();
	        autoNumber(); // tampilkan kode berikutnya
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(this, "Error tambah data: " + e.getMessage());
	    }
	}

	// ===== Method Ubah Data =====
	private void ubahData() {
	    try {
	        model_menu m = new model_menu();
	        m.setIdMenu(tidMenu.getText());
	        m.setNamaMenu(tnmMenu.getText());
	        m.setHarga(Double.parseDouble(tHarga.getText()));

	        dao.update(m);
	        JOptionPane.showMessageDialog(this, "Data berhasil diubah!");
	        tampilData();
	        batal();
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(this, "Error ubah data: " + e.getMessage());
	    }
	}

	// ===== Method Hapus Data =====
	private void hapusData() {
	    try {
	        String id = tidMenu.getText();
	        dao.delete(id);
	        JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
	        tampilData();
	        batal();
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(this, "Error hapus data: " + e.getMessage());
	    }
	}

	// ===== Method Batal =====
	private void batal() {
	    tidMenu.setText("");
	    tnmMenu.setText("");
	    tHarga.setText("");
	    bCari.setText("");
	    autoNumber();
	}
	
	// ===== Method CetakPdf====
	private void generatePDF() {
    try {
        // Create PDF document
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("Laporan_Menu.pdf"));
        document.open();

        // Add title
        com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
				Paragraph title = new Paragraph("LAPORAN DATA MENU");
				title.setAlignment(Element.ALIGN_CENTER);
				title.setSpacingAfter(20f);
				document.add(title);

        // Create table
        PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
        pdfTable.setWidthPercentage(100);
        pdfTable.setSpacingBefore(10f);

        // Add table headers
        for (int i = 0; i < table.getColumnCount(); i++) {
    		com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
    		Phrase header = new Phrase(table.getColumnName(i));
    		PdfPCell cell = new PdfPCell(header);
    		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
    		pdfTable.addCell(cell);
				}

        // Add table data
        for (int row = 0; row < table.getRowCount(); row++) {
            for (int col = 0; col < table.getColumnCount(); col++) {
                String value = table.getValueAt(row, col) != null ? 
                               table.getValueAt(row, col).toString() : "";
                pdfTable.addCell(value);
            }
        }

        document.add(pdfTable);
        document.close();

        JOptionPane.showMessageDialog(this, 
            "PDF berhasil dicetak!\nFile: Laporan_Menu.pdf", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Error mencetak PDF: " + e.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}
	// ===== Generate Code =====
	private void autoNumber() {
	    String newKode = dao.generateKode();
	    tidMenu.setText(newKode);
	    tidMenu.setEnabled(false); // supaya user tidak ubah manual
	}
	
	// Method untuk menampilkan hasil pencarian
	private void cariData() {
	    String keyword = bCari.getText().trim(); // ambil teks dari kolom pencarian
	    dao_menu dao = new dao_menu();
	    List<model_menu> hasil = dao.cariData(keyword);

	    DefaultTableModel model = new DefaultTableModel();
	    model.addColumn("ID Menu");
	    model.addColumn("Nama Menu");
	    model.addColumn("Harga");

	    for (model_menu m : hasil) {
	        model.addRow(new Object[]{
	            m.getIdMenu(),
	            m.getNamaMenu(),
	            m.getHarga()
	        });
	    }

	    table.setModel(model);
	}

	
	public viewMenu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 570, 398);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(0, 0, 554, 359);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Kode Menu");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblNewLabel_1.setBounds(10, 52, 72, 14);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel = new JLabel("Form Menu");
		lblNewLabel.setBounds(10, 11, 72, 17);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
		panel.add(lblNewLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 39, 534, 2);
		panel.add(separator);
		
		JLabel lblNewLabel_1_1 = new JLabel("Nama Menu");
		lblNewLabel_1_1.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblNewLabel_1_1.setBounds(10, 77, 72, 14);
		panel.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Harga");
		lblNewLabel_1_1_1.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblNewLabel_1_1_1.setBounds(10, 102, 72, 14);
		panel.add(lblNewLabel_1_1_1);
		
		tidMenu = new JTextField();
		tidMenu.setEditable(false);
		tidMenu.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		tidMenu.setBounds(99, 49, 86, 20);
		panel.add(tidMenu);
		tidMenu.setColumns(10);
		
		tnmMenu = new JTextField();
		tnmMenu.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		tnmMenu.setBounds(99, 74, 157, 20);
		panel.add(tnmMenu);
		tnmMenu.setColumns(10);
		
		tHarga = new JTextField();
		tHarga.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		tHarga.setBounds(99, 99, 86, 20);
		panel.add(tHarga);
		tHarga.setColumns(10);
		
		JButton bTambah = new JButton("Tambah");
		bTambah.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		bTambah.setBounds(10, 143, 89, 23);
		panel.add(bTambah);
		
		JButton bUbah = new JButton("Ubah");
		bUbah.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		bUbah.setBounds(109, 143, 89, 23);
		panel.add(bUbah);
		
		JButton bHapus = new JButton("Hapus");
		bHapus.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		bHapus.setBounds(208, 143, 89, 23);
		panel.add(bHapus);
		
		JButton bBatal = new JButton("Batal");
		bBatal.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		bBatal.setBounds(307, 143, 89, 23);
		panel.add(bBatal);
		
		JButton bCetakPDF = new JButton("Cetak PDF");
		bCetakPDF.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		bCetakPDF.setBounds(406, 143, 89, 23);
		panel.add(bCetakPDF);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 177, 534, 97);
		panel.add(scrollPane);
		
		table = new JTable();
		table.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		scrollPane.setViewportView(table);
		
		table.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		        int row = table.getSelectedRow();
		        tidMenu.setText(table.getValueAt(row, 0).toString());
		        tnmMenu.setText(table.getValueAt(row, 1).toString());
		        tHarga.setText(table.getValueAt(row, 2).toString());
		    }
		});
		
		JLabel lblNewLabel_1_2 = new JLabel("Cari Data");
		lblNewLabel_1_2.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblNewLabel_1_2.setBounds(10, 288, 72, 14);
		panel.add(lblNewLabel_1_2);
		
		bCari = new JTextField();
		bCari.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				cariData();
			}
		});
		bCari.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		bCari.setColumns(10);
		bCari.setBounds(78, 285, 466, 20);
		panel.add(bCari);
		
		bTambah.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        tambahData();
		    }
		});
		
		bUbah.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        ubahData();
		    }
		});

		bHapus.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        hapusData();
		    }
		});

		bBatal.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        batal();
		    }
		});
		
		bCetakPDF.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
        generatePDF();
    		}
		});
		tampilData();
		autoNumber();
	}
}
