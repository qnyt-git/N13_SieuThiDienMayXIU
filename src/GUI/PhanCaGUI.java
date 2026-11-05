package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PhanCaGUI extends JPanel {
    private JTextField txtMaPhanCa, txtNgayLam, txtTimKiem;
    private JComboBox<String> cboNhanVien, cboCaLam, cboTrangThai;
    private JTable tblPhanCa;
    private JButton btnThem, btnSua, btnXoa;

    public PhanCaGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel lblTieuDe = new JLabel("Phân ca làm việc");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        add(lblTieuDe, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(3, 4, 12, 8));
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230)),
            new EmptyBorder(14, 16, 14, 16)
        ));

        txtMaPhanCa = new JTextField(); txtMaPhanCa.setEnabled(false);
        cboNhanVien = new JComboBox<>();
        cboCaLam = new JComboBox<>();
        txtNgayLam = new JTextField("dd/MM/yyyy");
        cboTrangThai = new JComboBox<>(new String[]{"CHUA_LAM", "DA_LAM", "VANG_MAT"});

        panelForm.add(new JLabel("Mã phân ca (tự động):")); panelForm.add(txtMaPhanCa);
        panelForm.add(new JLabel("Nhân viên *:")); panelForm.add(cboNhanVien);
        panelForm.add(new JLabel("Ca làm việc *:")); panelForm.add(cboCaLam);
        panelForm.add(new JLabel("Ngày làm *:")); panelForm.add(txtNgayLam);
        panelForm.add(new JLabel("Trạng thái *:")); panelForm.add(cboTrangThai);
        panelForm.add(new JLabel("")); panelForm.add(new JLabel(""));

        JPanel panelBar = new JPanel(new BorderLayout());
        panelBar.setOpaque(false);
        JPanel pTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pTim.setOpaque(false);
        txtTimKiem = new JTextField(24);
        pTim.add(new JLabel("Tìm kiếm:")); pTim.add(txtTimKiem);

        JPanel pNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pNut.setOpaque(false);
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa"); btnXoa.setBackground(new Color(220, 38, 38)); btnXoa.setForeground(Color.WHITE);
        btnThem = new JButton("+ Thêm mới"); btnThem.setBackground(new Color(14, 98, 195)); btnThem.setForeground(Color.WHITE);
        pNut.add(btnSua); pNut.add(btnXoa); pNut.add(btnThem);
        panelBar.add(pTim, BorderLayout.WEST); panelBar.add(pNut, BorderLayout.EAST);

        tblPhanCa = new JTable(new DefaultTableModel(
            new Object[]{"Mã phân ca", "Nhân viên", "Ca làm việc", "Ngày làm", "Trạng thái"}, 0));
        tblPhanCa.setRowHeight(28);

        JPanel panelTren = new JPanel(new BorderLayout(0, 12));
        panelTren.setOpaque(false);
        panelTren.add(panelForm, BorderLayout.NORTH);
        panelTren.add(panelBar, BorderLayout.CENTER);

        JPanel panelThan = new JPanel(new BorderLayout(0, 10));
        panelThan.setOpaque(false);
        panelThan.add(panelTren, BorderLayout.NORTH);
        panelThan.add(new JScrollPane(tblPhanCa), BorderLayout.CENTER);

        add(panelThan, BorderLayout.CENTER);
    }
}