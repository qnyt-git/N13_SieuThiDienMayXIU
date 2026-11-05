package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrangChuGUI extends JFrame {
    
    // Khai báo các thành phần chính
    private CardLayout cardLayout;
    private JPanel panelNoiDung;
    private JLabel lblTieuDeTrang; 
    
    // Biến lưu thông tin nhân viên đăng nhập từ CSDL truyền qua
    private String tenNhanVien;
    private String chucVu;

    // Constructor nhận vào tên nhân viên và quyền từ LoginGUI
    public TrangChuGUI(String tenNhanVien, String chucVu) {
        this.tenNhanVien = tenNhanVien;
        this.chucVu = chucVu;

        // Cấu hình cửa sổ chính
        setTitle("XIU - Hệ thống quản lý siêu thị điện máy");
        setSize(1280, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ================= 1. THANH MENU BAR PHÍA TRÊN =================
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(12, 68, 136)); // Màu xanh đậm đồng bộ
        
        // --- Menu Hệ thống ---
        JMenu mnuHeThong = new JMenu("Hệ thống");
        mnuHeThong.setForeground(Color.WHITE);
        JMenuItem itemDoiMatKhau = new JMenuItem("Đổi mật khẩu");
        JMenuItem itemDangXuat = new JMenuItem("Đăng xuất");
        JMenuItem itemThoat = new JMenuItem("Thoát chương trình");
        mnuHeThong.add(itemDoiMatKhau);
        mnuHeThong.addSeparator();
        mnuHeThong.add(itemDangXuat);
        mnuHeThong.add(itemThoat);

        // --- Menu Danh mục ---
        JMenu mnuDanhMuc = new JMenu("Danh mục");
        mnuDanhMuc.setForeground(Color.WHITE);
        JMenuItem itemNhanVien = new JMenuItem("Nhân viên");
        JMenuItem itemKhachHang = new JMenuItem("Khách hàng");
        JMenuItem itemNhaCungCap = new JMenuItem("Nhà cung cấp");
        JMenuItem itemSanPham = new JMenuItem("Sản phẩm");
        JMenuItem itemCaLamViec = new JMenuItem("Ca làm việc");
        JMenuItem itemKhuyenMai = new JMenuItem("Khuyến mãi");
        
        mnuDanhMuc.add(itemNhanVien);
        mnuDanhMuc.add(itemKhachHang);
        mnuDanhMuc.add(itemNhaCungCap);
        mnuDanhMuc.add(itemSanPham);
        mnuDanhMuc.add(itemCaLamViec);
        mnuDanhMuc.add(itemKhuyenMai);

        // --- Menu Cập nhật ---
        JMenu mnuCapNhat = new JMenu("Cập nhật");
        mnuCapNhat.setForeground(Color.WHITE);
        JMenuItem itemCapNhatTrong = new JMenuItem("(Chưa có chức năng)");
        itemCapNhatTrong.setEnabled(false);
        mnuCapNhat.add(itemCapNhatTrong);

        // --- Menu Xử lý ---
        JMenu mnuXuLy = new JMenu("Xử lý");
        mnuXuLy.setForeground(Color.WHITE);
        JMenuItem itemBanHang = new JMenuItem("Bán hàng & Thanh toán");
        JMenuItem itemBaoHanh = new JMenuItem("Bảo hành & Đổi trả");
        JMenuItem itemNhapKho = new JMenuItem("Nhập kho");
        JMenuItem itemPhanCa = new JMenuItem("Phân ca");
        
        mnuXuLy.add(itemBanHang);
        mnuXuLy.add(itemBaoHanh);
        mnuXuLy.add(itemNhapKho);
        mnuXuLy.add(itemPhanCa);

        // --- Menu Tìm kiếm ---
        JMenu mnuTimKiem = new JMenu("Tìm kiếm");
        mnuTimKiem.setForeground(Color.WHITE);
        JMenuItem itemTKHoaDon = new JMenuItem("Hóa đơn");
        JMenuItem itemTKSanPham = new JMenuItem("Sản phẩm & tồn kho");
        JMenuItem itemTKBaoHanh = new JMenuItem("Phiếu bảo hành & đổi trả");
        JMenuItem itemTKLichLam = new JMenuItem("Lịch làm việc");
        
        mnuTimKiem.add(itemTKHoaDon);
        mnuTimKiem.add(itemTKSanPham);
        mnuTimKiem.add(itemTKBaoHanh);
        mnuTimKiem.add(itemTKLichLam);

        // --- Menu Báo biểu ---
        JMenu mnuBaoBieu = new JMenu("Báo biểu");
        mnuBaoBieu.setForeground(Color.WHITE);
        JMenuItem itemBaoCaoDoanhThu = new JMenuItem("Báo cáo doanh thu");
        mnuBaoBieu.add(itemBaoCaoDoanhThu);

        // Thêm các menu vào MenuBar
        menuBar.add(mnuHeThong);
        menuBar.add(mnuDanhMuc);
        menuBar.add(mnuCapNhat);
        menuBar.add(mnuTimKiem);
        menuBar.add(mnuXuLy);
        menuBar.add(mnuBaoBieu);
        setJMenuBar(menuBar);


        // ================= 2. HEADER PHỤ (HIỂN THỊ TÊN TRANG & TÀI KHOẢN) =================
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(Color.WHITE);
        panelHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(210, 215, 220)));
        panelHeader.setPreferredSize(new Dimension(getWidth(), 45));

        // Bên trái: Chữ XIU + tên trang hiện tại
        JPanel panelLeftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelLeftHeader.setOpaque(false);
        
        JLabel lblLogoXIU = new JLabel("XIU");
        lblLogoXIU.setFont(new Font("Arial", Font.BOLD, 18));
        lblLogoXIU.setForeground(new Color(12, 68, 136));
        
        lblTieuDeTrang = new JLabel("Trang chủ");
        lblTieuDeTrang.setFont(new Font("Arial", Font.PLAIN, 15));
        lblTieuDeTrang.setForeground(new Color(80, 90, 100));

        panelLeftHeader.add(lblLogoXIU);
        panelLeftHeader.add(lblTieuDeTrang);

        // Bên phải: Tên nhân viên & Quyền (Hiển thị thông tin thực tế từ database truyền vào)
        JPanel panelRightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        panelRightHeader.setOpaque(false);
        
        JLabel lblThongTinUser = new JLabel(tenNhanVien + " | " + chucVu);
        lblThongTinUser.setFont(new Font("Arial", Font.BOLD, 13));
        lblThongTinUser.setForeground(new Color(12, 68, 136));
        panelRightHeader.add(lblThongTinUser);

        panelHeader.add(panelLeftHeader, BorderLayout.WEST);
        panelHeader.add(panelRightHeader, BorderLayout.EAST);
        add(panelHeader, BorderLayout.NORTH);


        // ================= 3. KHUNG NỘI DUNG CHÍNH (CARDLAYOUT) =================
        cardLayout = new CardLayout();
        panelNoiDung = new JPanel(cardLayout);
        panelNoiDung.setBackground(new Color(240, 243, 246));

        // Tạo màn hình Trang Chủ
        JPanel panelTrangChu = taoManHinhTrangChu();
        
        // Tạo các màn hình chức năng mẫu
        JPanel panelNhanVienGUI = taoManHinhGiaoDienTam("Quản lý Nhân viên");
        JPanel panelKhachHangGUI = taoManHinhGiaoDienTam("Quản lý Khách hàng");
        JPanel panelSanPhamGUI = taoManHinhGiaoDienTam("Quản lý Sản phẩm");
        JPanel panelBanHangGUI = taoManHinhGiaoDienTam("Bán hàng & Thanh toán");
        JPanel panelBaoCaoGUI = taoManHinhGiaoDienTam("Báo cáo doanh thu");

        // Đưa các panel vào CardLayout kèm theo Key định danh
        panelNoiDung.add(panelTrangChu, "TRANG_CHU");
        panelNoiDung.add(panelNhanVienGUI, "NHAN_VIEN");
        panelNoiDung.add(panelKhachHangGUI, "KHACH_HANG");
        panelNoiDung.add(panelSanPhamGUI, "SAN_PHAM");
        panelNoiDung.add(panelBanHangGUI, "BAN_HANG");
        panelNoiDung.add(panelBaoCaoGUI, "BAO_CAO");

        add(panelNoiDung, BorderLayout.CENTER);


        // ================= 4. THANH TRẠNG THÁI (STATUS BAR) PHÍA DƯỚI =================
        JPanel panelStatus = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelStatus.setBackground(new Color(240, 240, 240));
        panelStatus.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 215, 220)));
        JLabel lblStatusText = new JLabel("Tài khoản: " + tenNhanVien + "  |  Phím tắt: Ctrl+số/chữ theo từng mục menu, Alt+chữ gạch chân để mở nhóm");
        lblStatusText.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStatusText.setForeground(new Color(100, 100, 100));
        panelStatus.add(lblStatusText);
        add(panelStatus, BorderLayout.SOUTH);


        // ================= 5. BẮT SỰ KIỆN CLICK MENU (DÙNG ANONYMOUS CLASS, KHÔNG DÙNG ->) =================
        
        // Nhóm Hệ thống
        itemDangXuat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int chon = JOptionPane.showConfirmDialog(TrangChuGUI.this, "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (chon == JOptionPane.YES_OPTION) {
                    dispose();
                    new LoginGUI().setVisible(true);
                }
            }
        });

        itemThoat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Nhóm Danh mục & Xử lý (Bấm chuyển card và đổi tên tiêu đề ở header)
        itemNhanVien.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelNoiDung, "NHAN_VIEN");
                lblTieuDeTrang.setText("›   Quản lý nhân viên");
            }
        });

        itemKhachHang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelNoiDung, "KHACH_HANG");
                lblTieuDeTrang.setText("›   Quản lý khách hàng");
            }
        });

        itemSanPham.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelNoiDung, "SAN_PHAM");
                lblTieuDeTrang.setText("›   Quản lý sản phẩm");
            }
        });

        itemBanHang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelNoiDung, "BAN_HANG");
                lblTieuDeTrang.setText("›   Bán hàng & thanh toán");
            }
        });

        itemBaoCaoDoanhThu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelNoiDung, "BAO_CAO");
                lblTieuDeTrang.setText("›   Báo cáo doanh thu");
            }
        });
        
        // Các mục khác tạm cho về Trang chủ
        itemCaLamViec.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelNoiDung, "TRANG_CHU");
                lblTieuDeTrang.setText("Trang chủ");
            }
        });

        itemKhuyenMai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelNoiDung, "TRANG_CHU");
                lblTieuDeTrang.setText("Trang chủ");
            }
        });
    }

    // ================= CÁC HÀM HỖ TRỢ TẠO GIAO DIỆN =================

    private JPanel taoManHinhTrangChu() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 247, 250));

        JLabel lblTongQuan = new JLabel("Tổng quan hoạt động");
        lblTongQuan.setFont(new Font("Arial", Font.BOLD, 20));
        lblTongQuan.setForeground(new Color(20, 40, 70));
        panel.add(lblTongQuan, BorderLayout.NORTH);

        JPanel panelGridThongKe = new JPanel(new GridLayout(2, 4, 15, 15));
        panelGridThongKe.setOpaque(false);

        panelGridThongKe.add(taoTheThongKe("Doanh thu hôm nay", "0đ"));
        panelGridThongKe.add(taoTheThongKe("Đơn hàng hôm nay", "0"));
        panelGridThongKe.add(taoTheThongKe("Tổng khách hàng", "0"));
        panelGridThongKe.add(taoTheThongKe("Sản phẩm sắp hết (<10)", "0"));
        
        panelGridThongKe.add(taoTheThongKe("Phiếu đặt hàng chờ xác nhận", "0"));
        panelGridThongKe.add(taoTheThongKe("Phiếu nhập kho chờ duyệt", "0"));
        panelGridThongKe.add(taoTheThongKe("Phiếu bảo hành đang xử lý", "0"));
        panelGridThongKe.add(taoTheThongKe("Khách hàng VIP", "0"));

        panel.add(panelGridThongKe, BorderLayout.CENTER);

        JPanel panelDuoi = new JPanel(new GridLayout(1, 2, 15, 0));
        panelDuoi.setPreferredSize(new Dimension(getWidth(), 280));
        panelDuoi.setOpaque(false);

        JPanel panelBieuDo = new JPanel(new BorderLayout());
        panelBieuDo.setBackground(Color.WHITE);
        panelBieuDo.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));
        JLabel lblTieuDeBieuDo = new JLabel("   Doanh thu theo tháng (triệu đồng)");
        lblTieuDeBieuDo.setFont(new Font("Arial", Font.BOLD, 13));
        lblTieuDeBieuDo.setBorder(new EmptyBorder(10, 0, 10, 0));
        panelBieuDo.add(lblTieuDeBieuDo, BorderLayout.NORTH);
        panelBieuDo.add(new JLabel("<html><center><br><br><i>[Khu vực hiển thị biểu đồ]</i></center></html>", JLabel.CENTER), BorderLayout.CENTER);

        JPanel panelTopSP = new JPanel(new BorderLayout());
        panelTopSP.setBackground(Color.WHITE);
        panelTopSP.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));
        JLabel lblTieuDeTop = new JLabel("   Top sản phẩm bán chạy");
        lblTieuDeTop.setFont(new Font("Arial", Font.BOLD, 13));
        lblTieuDeTop.setBorder(new EmptyBorder(10, 0, 10, 0));
        panelTopSP.add(lblTieuDeTop, BorderLayout.NORTH);
        panelTopSP.add(new JLabel("Chưa có dữ liệu", JLabel.CENTER), BorderLayout.CENTER);

        panelDuoi.add(panelBieuDo);
        panelDuoi.add(panelTopSP);
        panel.add(panelDuoi, BorderLayout.SOUTH);

        return panel;
    }
    
    private JPanel taoTheThongKe(String tieuDe, String giaTriMacDinh) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230)),
            new EmptyBorder(12, 15, 12, 15)
        ));

        JLabel lblTieuDe = new JLabel(tieuDe);
        lblTieuDe.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTieuDe.setForeground(new Color(110, 120, 130));

        JLabel lblGiaTri = new JLabel(giaTriMacDinh);
        lblGiaTri.setFont(new Font("Arial", Font.BOLD, 22));
        lblGiaTri.setForeground(new Color(20, 30, 40));

        card.add(lblTieuDe, BorderLayout.NORTH);
        card.add(lblGiaTri, BorderLayout.CENTER);
        return card;
    }

    private JPanel taoManHinhGiaoDienTam(String tenChucNang) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("Đang phát triển giao diện: " + tenChucNang);
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        lbl.setForeground(new Color(100, 100, 100));
        panel.add(lbl);
        return panel;
    }
}