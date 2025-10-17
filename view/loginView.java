package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class loginView extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;

    public loginView() {
        setTitle("Login");
        setSize(300, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel lblUser = new JLabel("Username:");
        JLabel lblPass = new JLabel("Password:");
        txtUser = new JTextField();
        txtPass = new JPasswordField();
        btnLogin = new JButton("Login");

        add(lblUser);
        add(txtUser);
        add(lblPass);
        add(txtPass);
        add(new JLabel(""));
        add(btnLogin);

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginAction();
            }
        });
    }

    private void loginAction() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());

        // login sederhana (belum ke database)
        if (user.equals("admin") && pass.equals("123")) {
            JOptionPane.showMessageDialog(this, "Login Berhasil!");
            new viewMenu().setVisible(true); // buka form menu
            dispose(); // tutup form login
        } else {
            JOptionPane.showMessageDialog(this, "Username atau Password salah!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
