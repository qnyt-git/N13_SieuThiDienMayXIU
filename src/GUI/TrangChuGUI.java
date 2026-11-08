
package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import ENTITY.NhanVien;

public class TrangChuGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelNoiDung;
    private JLabel lblTieuDeTrang;

    private String tenNhanVien;
    private String chucVu;
    private NhanVien nhanVienDangNhap;

    // Constructor
    public TrangChuGUI(NhanVien nhanVienDangNhap,
                       String tenNhanVien,
                       String chucVu) {

        if (nhanVienDangNhap == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "Không tìm thấy thông tin nhân viên đăng nhập!"
            );
            return;
        }

        this.nhanVienDangNhap = nhanVienDangNhap;
        this.tenNhanVien = tenNhanVien;
        this.chucVu = chucVu;

        setTitle("XIU - Hệ thống quản lý siêu thị điện máy");
        setSize(1280, 780);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        taoMenuBar();
        taoHeader();
        taoNoiDung();
        taoThanhTrangThai();
    }

    // ================= 1. MENU BAR =================

    private JMenu taoMenu(String ten) {
        JMenu menu = new JMenu(ten);
        menu.setForeground(Color.WHITE);
        return menu;
    }

    // Tao item menu va xu ly chuyen man hinh
    private JMenuItem taoItem(JMenu menu, String ten,
                              String card, String tieuDe) {

        JMenuItem item = new JMenuItem(ten);
        menu.add(item);

        item.addActionListener(e -> {
            chuyenManHinh(card, tieuDe);
        });

        return item;
    }

    // Chuyen man hinh bang CardLayout
    private void chuyenManHinh(String card, String tieuDe) {
        cardLayout.show(panelNoiDung, card);
        lblTieuDeTrang.setText("›    " + tieuDe);
    }

    // Tao thanh menu
    private void taoMenuBar() {

        JMenuBar bar = new JMenuBar();
        bar.setBackground(new Color(12, 68, 136));

        // Menu He thong
        JMenu mnuHeThong = taoMenu("Hệ thống");

        JMenuItem doiMatKhau = new JMenuItem("Đổi mật khẩu");
        JMenuItem dangXuat = new JMenuItem("Đăng xuất");
        JMenuItem thoat = new JMenuItem("Thoát chương trình");

        mnuHeThong.add(doiMatKhau);
        mnuHeThong.addSeparator();
        mnuHeThong.add(dangXuat);
        mnuHeThong.add(thoat);

        dangXuat.addActionListener(e -> xuLyDangXuat());
        thoat.addActionListener(e -> xuLyThoat());

        // Menu Danh muc
        JMenu mnuDanhMuc = taoMenu("Danh mục");

        taoItem(mnuDanhMuc, "Nhân viên",
                "NHAN_VIEN", "Quản lý nhân viên");

        taoItem(mnuDanhMuc, "Tài khoản nhân viên",
                "TAI_KHOAN", "Quản lý tài khoản nhân viên");

        taoItem(mnuDanhMuc, "Khách hàng",
                "KHACH_HANG", "Quản lý khách hàng");

        taoItem(mnuDanhMuc, "Nhà cung cấp",
                "NHA_CUNG_CAP", "Quản lý nhà cung cấp");

        taoItem(mnuDanhMuc, "Sản phẩm",
                "SAN_PHAM", "Quản lý sản phẩm");

        taoItem(mnuDanhMuc, "Ca làm việc",
                "CA_LAM_VIEC", "Quản lý ca làm việc");

        taoItem(mnuDanhMuc, "Khuyến mãi",
                "KHUYEN_MAI", "Quản lý khuyến mãi");

        // Menu Cap nhat
        JMenu mnuCapNhat = taoMenu("Cập nhật");

        taoItem(mnuCapNhat, "Chức vụ",
                "CHUC_VU", "Quản lý chức vụ");

        // Menu Tim kiem
        JMenu mnuTimKiem = taoMenu("Tìm kiếm");

        taoItem(mnuTimKiem, "Tìm kiếm sản phẩm",
                "TIM_KIEM_SAN_PHAM", "Tìm kiếm sản phẩm");

        taoItem(mnuTimKiem, "Tra cứu hóa đơn",
                "TIM_KIEM_HOA_DON", "Tra cứu hóa đơn");

        taoItem(mnuTimKiem, "Lịch sử mua hàng",
                "LICH_SU_MUA_HANG", "Lịch sử mua hàng");

        taoItem(mnuTimKiem, "Lịch làm việc",
                "LICH_LAM_VIEC", "Lịch làm việc");

        // Menu Xu ly
        JMenu mnuXuLy = taoMenu("Xử lý");

        taoItem(mnuXuLy, "Bán hàng & thanh toán",
                "BAN_HANG", "Bán hàng & thanh toán");

        taoItem(mnuXuLy, "Bảo hành",
                "BAO_HANH", "Quản lý bảo hành");

        taoItem(mnuXuLy, "Nhập kho",
                "NHAP_KHO", "Quản lý nhập kho");

        taoItem(mnuXuLy, "Phân ca",
                "PHAN_CA", "Quản lý phân ca");

        // Menu Bao bieu
        JMenu mnuBaoBieu = taoMenu("Báo biểu");

        taoItem(mnuBaoBieu, "Thống kê tổng hợp",
                "THONG_KE", "Thống kê tổng hợp");

        // Them menu theo dung thu tu giao dien
        bar.add(mnuHeThong);
        bar.add(mnuDanhMuc);
        bar.add(mnuCapNhat);
        bar.add(mnuTimKiem);
        bar.add(mnuXuLy);
        bar.add(mnuBaoBieu);

        setJMenuBar(bar);
    }

    // ================= 2. XU LY HE THONG =================

    private void xuLyDangXuat() {

        int chon = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION
        );

        if (chon == JOptionPane.YES_OPTION) {
            dispose();
            new LoginGUI().setVisible(true);
        }
    }

    private void xuLyThoat() {

        int chon = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn thoát chương trình?",
                "Xác nhận thoát",
                JOptionPane.YES_NO_OPTION
        );

        if (chon == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // ================= 3. HEADER =================

    private void taoHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(Color.WHITE);

        header.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0,
                new Color(210, 215, 220)
        ));

        header.setPreferredSize(new Dimension(getWidth(), 45));

        // Ben trai
        JPanel trai = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 15, 10)
        );

        trai.setOpaque(false);

        JLabel logo = new JLabel("XIU");
        logo.setFont(new Font("Arial", Font.BOLD, 18));
        logo.setForeground(new Color(12, 68, 136));

        lblTieuDeTrang = new JLabel("Trang chủ");
        lblTieuDeTrang.setFont(new Font("Arial", Font.PLAIN, 15));
        lblTieuDeTrang.setForeground(new Color(80, 90, 100));

        trai.add(logo);
        trai.add(lblTieuDeTrang);

        // Ben phai
        JLabel user = new JLabel(tenNhanVien + " | " + chucVu);

        user.setFont(new Font("Arial", Font.BOLD, 13));
        user.setForeground(new Color(12, 68, 136));

        JPanel phai = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 15, 12)
        );

        phai.setOpaque(false);
        phai.add(user);

        header.add(trai, BorderLayout.WEST);
        header.add(phai, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    // ================= 4. NOI DUNG CHINH =================

    private void taoNoiDung() {

        cardLayout = new CardLayout();
        panelNoiDung = new JPanel(cardLayout);

        panelNoiDung.setBackground(new Color(240, 243, 246));

        // Trang chu
        panelNoiDung.add(
                taoManHinhTrangChu(),
                "TRANG_CHU"
        );

        // Danh muc
        panelNoiDung.add(new NhanVienGUI(), "NHAN_VIEN");
        panelNoiDung.add(new ChucVuGUI(), "CHUC_VU");
        panelNoiDung.add(new TaiKhoanNhanVienGUI(), "TAI_KHOAN");
        panelNoiDung.add(new KhachHangGUI(), "KHACH_HANG");
        panelNoiDung.add(new NhaCungCapGUI(), "NHA_CUNG_CAP");
        panelNoiDung.add(new SanPhamGUI(), "SAN_PHAM");
        panelNoiDung.add(new CaLamViecGUI(), "CA_LAM_VIEC");
        panelNoiDung.add(new KhuyenMaiGUI(), "KHUYEN_MAI");

        // Xu ly
        panelNoiDung.add(
                new BanHangGUI(nhanVienDangNhap),
                "BAN_HANG"
        );

        panelNoiDung.add(new PhieuDatHangGUI(), "PHIEU_DAT_HANG");
        panelNoiDung.add(new HoaDonGUI(), "HOA_DON");
        panelNoiDung.add(new BaoHanhGUI(nhanVienDangNhap), "BAO_HANH");
        panelNoiDung.add(new NhapKhoGUI(nhanVienDangNhap), "NHAP_KHO");
        panelNoiDung.add(new PhanCaGUI(), "PHAN_CA");

        // Tim kiem
        panelNoiDung.add(
                new TimKiemSanPhamGUI(),
                "TIM_KIEM_SAN_PHAM"
        );

        panelNoiDung.add(
                new TimKiemHoaDonGUI(),
                "TIM_KIEM_HOA_DON"
        );

        panelNoiDung.add(
                new LichSuMuaHangGUI(),
                "LICH_SU_MUA_HANG"
        );

        panelNoiDung.add(
                new LichLamViecGUI(),
                "LICH_LAM_VIEC"
        );

        // Bao bieu
        panelNoiDung.add(
                new ThongKeGUI(),
                "THONG_KE"
        );

        add(panelNoiDung, BorderLayout.CENTER);

        cardLayout.show(panelNoiDung, "TRANG_CHU");
    }

    // ================= 5. GIAO DIEN TRANG CHU =================

    private JPanel taoManHinhTrangChu() {

        JPanel panel = new JPanel(
                new BorderLayout(15, 15)
        );

        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 247, 250));

        // Tieu de
        JLabel title = new JLabel("Tổng quan hoạt động");

        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(20, 40, 70));

        panel.add(title, BorderLayout.NORTH);

        // Cac the thong ke
        JPanel cards = new JPanel(
                new GridLayout(2, 4, 15, 15)
        );

        cards.setOpaque(false);

        String[] names = {
            "Doanh thu hôm nay",
            "Đơn hàng hôm nay",
            "Tổng khách hàng",
            "Sản phẩm sắp hết (<10)",
            "Phiếu đặt hàng chờ xác nhận",
            "Phiếu nhập kho chờ duyệt",
            "Phiếu bảo hành đang xử lý",
            "Khách hàng VIP"
        };

        for (String name : names) {

            JPanel card = new JPanel(new BorderLayout());

            card.setBackground(Color.WHITE);

            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(
                            new Color(220, 225, 230)
                    ),
                    new EmptyBorder(12, 15, 12, 15)
            ));

            JLabel label = new JLabel(name);

            label.setForeground(new Color(110, 120, 130));
            label.setFont(new Font("Arial", Font.PLAIN, 12));

            JLabel value = new JLabel("0");

            value.setFont(new Font("Arial", Font.BOLD, 22));
            value.setForeground(new Color(20, 30, 40));

            if (name.equals("Doanh thu hôm nay")) {
                value.setText("0đ");
            }

            card.add(label, BorderLayout.NORTH);
            card.add(value, BorderLayout.CENTER);

            cards.add(card);
        }

        panel.add(cards, BorderLayout.CENTER);

        // Khu vuc bieu do
        JPanel bottom = new JPanel(
                new GridLayout(1, 2, 15, 0)
        );

        bottom.setOpaque(false);
        bottom.setPreferredSize(new Dimension(getWidth(), 280));

        bottom.add(
                taoOTrangChu("Doanh thu theo tháng (triệu đồng)")
        );

        bottom.add(
                taoOTrangChu("Top sản phẩm bán chạy")
        );

        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    // ================= 6. CAC O BIEU DO =================

    private JPanel taoOTrangChu(String title) {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createLineBorder(
                new Color(220, 225, 230)
        ));

        JLabel label = new JLabel("   " + title);

        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setBorder(new EmptyBorder(10, 0, 10, 0));

        panel.add(label, BorderLayout.NORTH);

        JLabel noiDung = new JLabel(
                "Chưa có dữ liệu",
                JLabel.CENTER
        );

        noiDung.setFont(new Font("Arial", Font.BOLD, 12));

        panel.add(noiDung, BorderLayout.CENTER);

        return panel;
    }

    // ================= 7. THANH TRANG THAI =================

    private void taoThanhTrangThai() {

        JPanel status = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5)
        );

        status.setBackground(new Color(240, 240, 240));

        status.setBorder(BorderFactory.createMatteBorder(
                1, 0, 0, 0,
                new Color(210, 215, 220)
        ));

        JLabel label = new JLabel(
                "Tài khoản: " + tenNhanVien
                + "  |  Phím tắt: Ctrl+số/chữ theo từng mục menu, "
                + "Alt+chữ gạch chân để mở nhóm"
        );

        label.setFont(new Font("Arial", Font.PLAIN, 11));
        label.setForeground(new Color(100, 100, 100));

        status.add(label);

        add(status, BorderLayout.SOUTH);
    }
}
