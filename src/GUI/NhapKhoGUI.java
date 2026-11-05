package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NhapKhoGUI extends JPanel {
    private JComboBox<String> cboNhaCungCap, cboSanPham;
    private JTextField txtNgayNhap, txtNhanVien, txtLyDoNhap, txtDonGiaNhap;
    private JSpinner spnSoLuong;
    private JTable tblChiTietTam, tblDanhSachPhieuNhap, tblChiTietPhieuNhap;
    private JLabel lblTongTien;
    private JButton btnThemDong, btnGoiYSapHet, btnXoaDong, btnLuuChoDuyet, btnLuuVaXacNhan;
    private JButton btnXacNhanNhapKho, btnHuyPhieu;

    public NhapKhoGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel lblTieuDe = new JLabel("Nhập kho");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        add(lblTieuDe, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 13));
        tabs.addTab("Lập phiếu nhập kho", taoTabLapPhieu());
        tabs.addTab("Danh sách phiếu nhập", taoTabDanhSach());

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel taoTabLapPhieu() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(14, 16, 14, 16));

        JPanel panelThongTin = new JPanel(new GridLayout(2, 4, 12, 8));
        panelThongTin.setBackground(Color.WHITE);

        cboNhaCungCap = new JComboBox<>();
        txtNgayNhap = new JTextField("Tự động"); txtNgayNhap.setEditable(false);
        txtLyDoNhap = new JTextField();
        txtNhanVien = new JTextField("Nhân viên hiện tại"); txtNhanVien.setEditable(false);

        panelThongTin.add(new JLabel("Nhà cung cấp *:")); panelThongTin.add(cboNhaCungCap);
        panelThongTin.add(new JLabel("Ngày nhập:")); panelThongTin.add(txtNgayNhap);
        panelThongTin.add(new JLabel("Lý do nhập *:")); panelThongTin.add(txtLyDoNhap);
        panelThongTin.add(new JLabel("Nhân viên:")); panelThongTin.add(txtNhanVien);

        JPanel panelThemSp = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        panelThemSp.setBackground(Color.WHITE);
        cboSanPham = new JComboBox<>();
        cboSanPham.setPreferredSize(new Dimension(220, 30));
        spnSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
        spnSoLuong.setPreferredSize(new Dimension(80, 30));
        txtDonGiaNhap = new JTextField(10);

        btnThemDong = new JButton("+ Thêm dòng");
        btnThemDong.setBackground(new Color(14, 98, 195)); btnThemDong.setForeground(Color.WHITE);
        btnGoiYSapHet = new JButton("Gợi ý: SP sắp hết hàng");

        panelThemSp.add(new JLabel("Sản phẩm:")); panelThemSp.add(cboSanPham);
        panelThemSp.add(new JLabel("SL:")); panelThemSp.add(spnSoLuong);
        panelThemSp.add(new JLabel("Đơn giá nhập:")); panelThemSp.add(txtDonGiaNhap);
        panelThemSp.add(btnThemDong);
        panelThemSp.add(btnGoiYSapHet);

        JPanel panelTren = new JPanel(new BorderLayout(0, 8));
        panelTren.setBackground(Color.WHITE);
        panelTren.add(panelThongTin, BorderLayout.NORTH);
        panelTren.add(panelThemSp, BorderLayout.CENTER);

        tblChiTietTam = new JTable(new DefaultTableModel(
            new Object[]{"Mã SP", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"}, 0));
        tblChiTietTam.setRowHeight(28);

        JPanel panelDuoi = new JPanel(new BorderLayout());
        panelDuoi.setBackground(Color.WHITE);
        btnXoaDong = new JButton("Xóa dòng");
        lblTongTien = new JLabel("0đ");
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        lblTongTien.setForeground(new Color(14, 98, 195));
        btnLuuChoDuyet = new JButton("Lưu chờ duyệt");
        btnLuuVaXacNhan = new JButton("Lưu & xác nhận nhập kho");
        btnLuuVaXacNhan.setBackground(new Color(22, 163, 74)); btnLuuVaXacNhan.setForeground(Color.WHITE);

        JPanel panelPhai = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelPhai.setBackground(Color.WHITE);
        panelPhai.add(new JLabel("Tổng tiền:"));
        panelPhai.add(lblTongTien);
        panelPhai.add(btnLuuChoDuyet);
        panelPhai.add(btnLuuVaXacNhan);

        panelDuoi.add(btnXoaDong, BorderLayout.WEST);
        panelDuoi.add(panelPhai, BorderLayout.EAST);

        panel.add(panelTren, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblChiTietTam), BorderLayout.CENTER);
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
        btnXacNhanNhapKho = new JButton("Xác nhận nhập kho");
        btnXacNhanNhapKho.setBackground(new Color(22, 163, 74)); btnXacNhanNhapKho.setForeground(Color.WHITE);
        panelNut.add(btnHuyPhieu);
        panelNut.add(btnXacNhanNhapKho);

        tblDanhSachPhieuNhap = new JTable(new DefaultTableModel(
            new Object[]{"Mã phiếu", "Ngày nhập", "Nhà cung cấp", "Nhân viên", "Lý do nhập", "Tổng tiền", "Trạng thái"}, 0));
        tblDanhSachPhieuNhap.setRowHeight(28);

        tblChiTietPhieuNhap = new JTable(new DefaultTableModel(
            new Object[]{"Mã SP", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"}, 0));
        tblChiTietPhieuNhap.setRowHeight(28);

        JPanel panelBang = new JPanel(new GridLayout(2, 1, 0, 10));
        panelBang.setOpaque(false);
        panelBang.add(new JScrollPane(tblDanhSachPhieuNhap));
        panelBang.add(new JScrollPane(tblChiTietPhieuNhap));

        panel.add(panelNut, BorderLayout.NORTH);
        panel.add(panelBang, BorderLayout.CENTER);
        return panel;
    }
}