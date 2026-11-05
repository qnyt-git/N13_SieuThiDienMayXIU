package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BaoHanhGUI extends JPanel {
    private JComboBox<String> cboKhachHang, cboSanPham;
    private JTextField txtNgayTiepNhan, txtNgayHenTra, txtNhanVien, txtTinhTrang, txtNoiDung;
    private JTable tblDongTiepNhan, tblDanhSachPhieu, tblChiTietBaoHanh;
    private JButton btnThemDong, btnXoaDong, btnTiepNhan, btnDaTraMay, btnHuyPhieu;

    public BaoHanhGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel lblTieuDe = new JLabel("Bảo hành");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        add(lblTieuDe, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 13));
        tabs.addTab("Tiếp nhận bảo hành", taoTabTiepNhan());
        tabs.addTab("Danh sách phiếu bảo hành", taoTabDanhSach());

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel taoTabTiepNhan() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(14, 16, 14, 16));

        JPanel panelThongTin = new JPanel(new GridLayout(2, 4, 10, 8));
        panelThongTin.setBackground(Color.WHITE);
        cboKhachHang = new JComboBox<>();
        txtNgayTiepNhan = new JTextField("Tự động"); txtNgayTiepNhan.setEditable(false);
        txtNgayHenTra = new JTextField("dd/MM/yyyy");
        txtNhanVien = new JTextField("Nhân viên hiện tại"); txtNhanVien.setEditable(false);

        panelThongTin.add(new JLabel("Khách hàng *:")); panelThongTin.add(cboKhachHang);
        panelThongTin.add(new JLabel("Ngày tiếp nhận:")); panelThongTin.add(txtNgayTiepNhan);
        panelThongTin.add(new JLabel("Ngày hẹn trả *:")); panelThongTin.add(txtNgayHenTra);
        panelThongTin.add(new JLabel("Nhân viên:")); panelThongTin.add(txtNhanVien);

        JPanel panelThemDong = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        panelThemDong.setBackground(Color.WHITE);
        cboSanPham = new JComboBox<>();
        cboSanPham.setPreferredSize(new Dimension(200, 30));
        txtTinhTrang = new JTextField(16);
        txtNoiDung = new JTextField(20);
        btnThemDong = new JButton("+ Thêm dòng");
        btnThemDong.setBackground(new Color(14, 98, 195));
        btnThemDong.setForeground(Color.WHITE);

        panelThemDong.add(new JLabel("Sản phẩm *:")); panelThemDong.add(cboSanPham);
        panelThemDong.add(new JLabel("Tình trạng *:")); panelThemDong.add(txtTinhTrang);
        panelThemDong.add(new JLabel("Nội dung *:")); panelThemDong.add(txtNoiDung);
        panelThemDong.add(btnThemDong);

        JPanel panelTren = new JPanel(new BorderLayout(0, 8));
        panelTren.setBackground(Color.WHITE);
        panelTren.add(panelThongTin, BorderLayout.NORTH);
        panelTren.add(panelThemDong, BorderLayout.CENTER);

        tblDongTiepNhan = new JTable(new DefaultTableModel(
            new Object[]{"Mã SP", "Tên sản phẩm", "Tình trạng sản phẩm", "Nội dung bảo hành"}, 0));
        tblDongTiepNhan.setRowHeight(28);

        JPanel panelDuoi = new JPanel(new BorderLayout());
        panelDuoi.setBackground(Color.WHITE);
        btnXoaDong = new JButton("Xóa dòng");
        btnTiepNhan = new JButton("Tiếp nhận bảo hành");
        btnTiepNhan.setBackground(new Color(22, 163, 74));
        btnTiepNhan.setForeground(Color.WHITE);
        panelDuoi.add(btnXoaDong, BorderLayout.WEST);
        panelDuoi.add(btnTiepNhan, BorderLayout.EAST);

        panel.add(panelTren, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblDongTiepNhan), BorderLayout.CENTER);
        panel.add(panelDuoi, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel taoTabDanhSach() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelNut.setOpaque(false);
        btnHuyPhieu = new JButton("Hủy phiếu");
        btnHuyPhieu.setBackground(new Color(220, 38, 38)); btnHuyPhieu.setForeground(Color.WHITE);
        btnDaTraMay = new JButton("Đã trả máy cho khách");
        btnDaTraMay.setBackground(new Color(22, 163, 74)); btnDaTraMay.setForeground(Color.WHITE);
        panelNut.add(btnHuyPhieu);
        panelNut.add(btnDaTraMay);

        tblDanhSachPhieu = new JTable(new DefaultTableModel(
            new Object[]{"Mã phiếu", "Khách hàng", "Ngày tiếp nhận", "Ngày hẹn trả", "Trạng thái"}, 0));
        tblDanhSachPhieu.setRowHeight(28);

        tblChiTietBaoHanh = new JTable(new DefaultTableModel(
            new Object[]{"Mã SP", "Tên sản phẩm", "Tình trạng sản phẩm", "Nội dung bảo hành"}, 0));
        tblChiTietBaoHanh.setRowHeight(28);

        JPanel panelBang = new JPanel(new GridLayout(2, 1, 0, 10));
        panelBang.setOpaque(false);
        panelBang.add(new JScrollPane(tblDanhSachPhieu));
        panelBang.add(new JScrollPane(tblChiTietBaoHanh));

        panel.add(panelNut, BorderLayout.NORTH);
        panel.add(panelBang, BorderLayout.CENTER);
        return panel;
    }
}