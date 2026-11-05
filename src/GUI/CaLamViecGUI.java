package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CaLamViecGUI extends JPanel {
    private JTextField txtMaCa, txtTenCa, txtGioBatDau, txtGioKetThuc, txtTimKiem;
    private JTable tblCaLam;
    private JButton btnThem, btnSua, btnXoa;

    public CaLamViecGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel lblTieuDe = new JLabel("Quản lý ca làm việc");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        add(lblTieuDe, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(2, 4, 12, 8));
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230)),
            new EmptyBorder(14, 16, 14, 16)
        ));

        txtMaCa = new JTextField(); txtMaCa.setEnabled(false);
        txtTenCa = new JTextField();
        txtGioBatDau = new JTextField("06:00");
        txtGioKetThuc = new JTextField("14:00");

        panelForm.add(new JLabel("Mã ca (tự động):")); panelForm.add(txtMaCa);
        panelForm.add(new JLabel("Tên ca *:")); panelForm.add(txtTenCa);
        panelForm.add(new JLabel("Giờ bắt đầu *:")); panelForm.add(txtGioBatDau);
        panelForm.add(new JLabel("Giờ kết thúc *:")); panelForm.add(txtGioKetThuc);

        JPanel panelBar = new JPanel(new BorderLayout());
        panelBar.setOpaque(false);
        JPanel pTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pTim.setOpaque(false);
        txtTimKiem = new JTextField(22);
        pTim.add(new JLabel("Tìm kiếm:")); pTim.add(txtTimKiem);

        JPanel pNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pNut.setOpaque(false);
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa"); btnXoa.setBackground(new Color(220, 38, 38)); btnXoa.setForeground(Color.WHITE);
        btnThem = new JButton("+ Thêm mới"); btnThem.setBackground(new Color(14, 98, 195)); btnThem.setForeground(Color.WHITE);
        pNut.add(btnSua); pNut.add(btnXoa); pNut.add(btnThem);
        panelBar.add(pTim, BorderLayout.WEST); panelBar.add(pNut, BorderLayout.EAST);

        tblCaLam = new JTable(new DefaultTableModel(new Object[]{"Mã ca", "Tên ca", "Giờ bắt đầu", "Giờ kết thúc"}, 0));
        tblCaLam.setRowHeight(28);

        JPanel panelTren = new JPanel(new BorderLayout(0, 12));
        panelTren.setOpaque(false);
        panelTren.add(panelForm, BorderLayout.NORTH);
        panelTren.add(panelBar, BorderLayout.CENTER);

        JPanel panelThan = new JPanel(new BorderLayout(0, 10));
        panelThan.setOpaque(false);
        panelThan.add(panelTren, BorderLayout.NORTH);
        panelThan.add(new JScrollPane(tblCaLam), BorderLayout.CENTER);

        add(panelThan, BorderLayout.CENTER);
    }
}