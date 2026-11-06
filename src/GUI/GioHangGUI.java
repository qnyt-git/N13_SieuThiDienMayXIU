package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GioHangGUI extends JPanel {
    private JTextField txtTimSp, txtGhiChu;
    private JTable tblSanPham, tblGioHang;
    private JSpinner spnSoLuong;
    private JComboBox<String> cboKhachHang, cboKhuyenMai, cboPhieuDat, cboPhuongThuc;
    private JLabel lblTongTien, lblGiamGia, lblThanhTien;
    private JButton btnThemGio, btnXoaDong, btnXoaHet, btnXacNhan;

    
    public GioHangGUI(boolean laThanhToan) {
        setLayout(new GridLayout(1, 2, 12, 0));
        setBackground(new Color(235, 240, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // ================= BÊN TRÁI: DANH SÁCH SẢN PHẨM =================
        JPanel panelTrai = new JPanel(new BorderLayout(0, 8));
        panelTrai.setBackground(Color.WHITE);
        panelTrai.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 215, 220)),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JPanel panelTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelTim.setBackground(Color.WHITE);
        txtTimSp = new JTextField(18);
        panelTim.add(new JLabel("Tìm sản phẩm:"));
        panelTim.add(txtTimSp);

        DefaultTableModel modelSp = new DefaultTableModel(
            new Object[]{"Mã SP", "Tên sản phẩm", "Giá bán", "Tồn"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblSanPham = new JTable(modelSp);
        tblSanPham.setRowHeight(28);

        JPanel panelThem = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelThem.setBackground(Color.WHITE);
        spnSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spnSoLuong.setPreferredSize(new Dimension(70, 32));
        btnThemGio = new JButton("Thêm vào giỏ");
        btnThemGio.setBackground(new Color(14, 98, 195));
        btnThemGio.setForeground(Color.WHITE);
        btnThemGio.setFont(new Font("Arial", Font.BOLD, 13));
        btnThemGio.setFocusPainted(false);

        panelThem.add(new JLabel("Số lượng:"));
        panelThem.add(spnSoLuong);
        panelThem.add(btnThemGio);

        panelTrai.add(panelTim, BorderLayout.NORTH);
        panelTrai.add(new JScrollPane(tblSanPham), BorderLayout.CENTER);
        panelTrai.add(panelThem, BorderLayout.SOUTH);

        // ================= BÊN PHẢI: GIỎ HÀNG & THÔNG TIN =================
        JPanel panelPhai = new JPanel(new BorderLayout(0, 8));
        panelPhai.setBackground(Color.WHITE);
        panelPhai.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 215, 220)),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        DefaultTableModel modelGio = new DefaultTableModel(
            new Object[]{"Mã SP", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblGioHang = new JTable(modelGio);
        tblGioHang.setRowHeight(28);

        JPanel panelDuoi = new JPanel(new BorderLayout(0, 8));
        panelDuoi.setBackground(Color.WHITE);

        JPanel panelNutGio = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelNutGio.setBackground(Color.WHITE);
        btnXoaDong = new JButton("Xóa dòng");
        btnXoaHet = new JButton("Xóa hết");
        panelNutGio.add(btnXoaDong);
        panelNutGio.add(btnXoaHet);

        JPanel panelForm = new JPanel(new GridLayout(4, 2, 8, 6));
        panelForm.setBackground(Color.WHITE);
        cboKhachHang = new JComboBox<>();
        panelForm.add(new JLabel("Khách hàng *:"));
        panelForm.add(cboKhachHang);

        if (laThanhToan) {
            cboKhuyenMai = new JComboBox<>(new String[]{"Không áp dụng"});
            cboPhieuDat = new JComboBox<>(new String[]{"(Bán trực tiếp)"});
            cboPhuongThuc = new JComboBox<>(new String[]{
                "Tiền mặt",
                "Chuyển khoản ngân hàng",
                "Thẻ ngân hàng (ATM/Visa/Master)",
                "Ví điện tử (MoMo/ShopeePay)"
            });
            panelForm.add(new JLabel("Khuyến mãi:"));
            panelForm.add(cboKhuyenMai);
            panelForm.add(new JLabel("Phiếu đặt hàng:"));
            panelForm.add(cboPhieuDat);
            panelForm.add(new JLabel("Thanh toán bằng:"));
            panelForm.add(cboPhuongThuc);
        } else {
            txtGhiChu = new JTextField();
            panelForm.add(new JLabel("Ghi chú:"));
            panelForm.add(txtGhiChu);
            
            panelForm.add(new JLabel(""));
            panelForm.add(new JLabel(""));
            panelForm.add(new JLabel(""));
            panelForm.add(new JLabel(""));
        }

        JPanel panelTong = new JPanel(new GridLayout(3, 2, 8, 4));
        panelTong.setBackground(Color.WHITE);
        lblTongTien = new JLabel("0 đ", SwingConstants.RIGHT);
        lblGiamGia = new JLabel("-0 đ", SwingConstants.RIGHT);
        lblThanhTien = new JLabel("0 đ", SwingConstants.RIGHT);
        lblThanhTien.setFont(new Font("Arial", Font.BOLD, 18));
        lblThanhTien.setForeground(new Color(14, 98, 195));

        if (laThanhToan) {
            panelTong.add(new JLabel("Tổng tiền:")); panelTong.add(lblTongTien);
            panelTong.add(new JLabel("Giảm giá:")); panelTong.add(lblGiamGia);
            panelTong.add(new JLabel("Thành tiền:")); panelTong.add(lblThanhTien);
        } else {
            panelTong.add(new JLabel("")); panelTong.add(new JLabel(""));
            panelTong.add(new JLabel("")); panelTong.add(new JLabel(""));
            panelTong.add(new JLabel("Tổng tiền:")); panelTong.add(lblThanhTien);
        }

        JPanel panelGiua = new JPanel(new BorderLayout(16, 0));
        panelGiua.setBackground(Color.WHITE);
        panelGiua.add(panelForm, BorderLayout.CENTER);
        panelGiua.add(panelTong, BorderLayout.EAST);

        JPanel panelSubmit = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelSubmit.setBackground(Color.WHITE);
        btnXacNhan = new JButton(laThanhToan ? "Thanh toán & lập hóa đơn" : "Lập phiếu đặt hàng");
        btnXacNhan.setBackground(laThanhToan ? new Color(22, 163, 74) : new Color(14, 98, 195));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFont(new Font("Arial", Font.BOLD, 13));
        btnXacNhan.setFocusPainted(false);
        panelSubmit.add(btnXacNhan);

        panelDuoi.add(panelNutGio, BorderLayout.NORTH);
        panelDuoi.add(panelGiua, BorderLayout.CENTER);
        panelDuoi.add(panelSubmit, BorderLayout.SOUTH);

        panelPhai.add(new JScrollPane(tblGioHang), BorderLayout.CENTER);
        panelPhai.add(panelDuoi, BorderLayout.SOUTH);

        add(panelTrai);
        add(panelPhai);
    }
}