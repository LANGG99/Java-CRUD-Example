package dao;

import java.sql.*;
import java.util.*;
import config.koneksi;
import model.model_menu;

public class dao_menu {
	
	Connection conn = koneksi.getConnection();

    public void insert(model_menu m) {
        try {
            String sql = "INSERT INTO menu(id_menu, nama_menu, harga) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, m.getIdMenu());
            ps.setString(2, m.getNamaMenu());
            ps.setDouble(3, m.getHarga());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<model_menu> getAll() {
        List<model_menu> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM menu");
            while (rs.next()) {
                list.add(new model_menu(rs.getString("id_menu"), rs.getString("nama_menu"), rs.getDouble("harga")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void update(model_menu m) {
        try {
            String sql = "UPDATE menu SET nama_menu=?, harga=? WHERE id_menu=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, m.getNamaMenu());
            ps.setDouble(2, m.getHarga());
            ps.setString(3, m.getIdMenu());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String id_menu) {
        try {
            String sql = "DELETE FROM menu WHERE id_menu=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id_menu);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String generateKode() {
        String kode = "M001";
        try {
            String sql = "SELECT id_menu FROM menu ORDER BY id_menu DESC LIMIT 1";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                String lastKode = rs.getString("id_menu").substring(1); // ambil angka dari M001 → 001
                int nextKode = Integer.parseInt(lastKode) + 1;
                kode = String.format("M%03d", nextKode); // format jadi M002, M010, dst.
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kode;
    }
    
    public List<model_menu> cariData(String keyword) {
        List<model_menu> dataMenu = new ArrayList<>();

        try {
            Connection conn = koneksi.getConnection();
            String sql = "SELECT * FROM menu WHERE id_menu LIKE ? OR nama_menu LIKE ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + keyword + "%");
            pst.setString(2, "%" + keyword + "%");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model_menu menu = new model_menu();
                menu.setIdMenu(rs.getString("id_menu"));
                menu.setNamaMenu(rs.getString("nama_menu"));
                menu.setHarga(rs.getDouble("harga"));
                dataMenu.add(menu);
            }

        } catch (SQLException e) {
            System.err.println("Kesalahan saat mencari data: " + e.getMessage());
        }

        return dataMenu;
    }
}

