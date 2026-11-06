package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrangChuGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelNoiDung;
    private JLabel lblTieuDeTrang;
    private String tenNhanVien;
    private String chucVu;

    public TrangChuGUI(String tenNhanVien, String chucVu) {
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

    private JMenu taoMenu(String ten) {
        JMenu menu = new JMenu(ten);
        menu.setForeground(Color.WHITE);
        return menu;
    }

    private JMenuItem taoItem(JMenu menu, String ten, String card, String tieuDe) {
        JMenuItem item = new JMenuItem(ten);
        menu.add(item);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelNoiDung, card);
                lblTieuDeTrang.setText("›   " + tieuDe);
            }
        });
        return item;
    }

    private void taoMenuBar() {
        JMenuBar bar = new JMenuBar();
        bar.setBackground(new Color(12, 68, 136));

        JMenu mnuHeThong = taoMenu("Hệ thống");
        JMenuItem doiMatKhau = new JMenuItem("Đổi mật khẩu");
        JMenuItem dangXuat = new JMenuItem("Đăng xuất");
        JMenuItem thoat = new JMenuItem("Thoát chương trình");
        mnuHeThong.add(doiMatKhau);
        mnuHeThong.addSeparator();
        mnuHeThong.add(dangXuat);
        mnuHeThong.add(thoat);

        dangXuat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int chon = JOptionPane.showConfirmDialog(
                        TrangChuGUI.this, "Bạn có chắc muốn đăng xuất?",
                        "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (chon == JOptionPane.YES_OPTION) {
                    dispose();
                    new LoginGUI().setVisible(true);
                }
            }
        });

        thoat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JMenu mnuDanhMuc = taoMenu("Danh mục");
        mnuDanhMuc.setForeground(Color.WHITE);
        taoItem(mnuDanhMuc, "Nhân viên", "NHAN_VIEN", "Quản lý nhân viên");
        taoItem(mnuDanhMuc, "Tài khoản nhân viên", "TAI_KHOAN", "Quản lý tài khoản nhân viên");
        taoItem(mnuDanhMuc, "Khách hàng", "KHACH_HANG", "Quản lý khách hàng");
        taoItem(mnuDanhMuc, "Nhà cung cấp", "NHA_CUNG_CAP", "Quản lý nhà cung cấp");
        taoItem(mnuDanhMuc, "Sản phẩm", "SAN_PHAM", "Quản lý sản phẩm");
        taoItem(mnuDanhMuc, "Ca làm việc", "CA_LAM_VIEC", "Quản lý ca làm việc");
        taoItem(mnuDanhMuc, "Khuyến mãi", "KHUYEN_MAI", "Quản lý khuyến mãi");

        JMenu mnuCapNhat = taoMenu("Cập nhật");
        mnuCapNhat.setForeground(Color.WHITE);
        taoItem(mnuCapNhat, "Chức vụ", "CHUC_VU", "Quản lý chức vụ");
        

        JMenu mnuXuLy = taoMenu("Xử lý");
        mnuXuLy.setForeground(Color.WHITE);
        taoItem(mnuXuLy, "Bán hàng", "BAN_HANG", "Bán hàng & thanh toán");
        taoItem(mnuXuLy, "Bảo hành", "BAO_HANH", "Quản lý bảo hành");
        taoItem(mnuXuLy, "Nhập kho", "NHAP_KHO", "Quản lý nhập kho");
        taoItem(mnuXuLy, "Phân ca", "PHAN_CA", "Quản lý phân ca");

        JMenu mnuTimKiem = taoMenu("Tìm kiếm");
        mnuTimKiem.setForeground(Color.WHITE);
        taoItem(mnuTimKiem, "Tìm kiếm sản phẩm", "TIM_KIEM_SAN_PHAM", "Tìm kiếm sản phẩm");
        taoItem(mnuTimKiem, "Lịch sử mua hàng", "LICH_SU_MUA_HANG", "Lịch sử mua hàng");
        taoItem(mnuTimKiem, "Lịch làm việc", "LICH_LAM_VIEC", "Lịch làm việc");

        JMenu mnuBaoBieu = taoMenu("Báo biểu");
        mnuBaoBieu.setForeground(Color.WHITE);
        taoItem(mnuBaoBieu, "Thống kê tổng hợp", "THONG_KE", "Thống kê tổng hợp");

        bar.add(mnuHeThong);
        bar.add(mnuDanhMuc);
        bar.add(mnuCapNhat);
        bar.add(mnuXuLy);
        bar.add(mnuTimKiem);
        bar.add(mnuBaoBieu);
        setJMenuBar(bar);
    }

    private void taoHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, new Color(210, 215, 220)));
        header.setPreferredSize(new Dimension(getWidth(), 45));

        JPanel trai = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        trai.setOpaque(false);

        JLabel logo = new JLabel("XIU");
        logo.setFont(new Font("Arial", Font.BOLD, 18));
        logo.setForeground(new Color(12, 68, 136));

        lblTieuDeTrang = new JLabel("Trang chủ");
        lblTieuDeTrang.setFont(new Font("Arial", Font.PLAIN, 15));
        lblTieuDeTrang.setForeground(new Color(80, 90, 100));

        trai.add(logo);
        trai.add(lblTieuDeTrang);

        JLabel user = new JLabel(tenNhanVien + " | " + chucVu);
        user.setFont(new Font("Arial", Font.BOLD, 13));
        user.setForeground(new Color(12, 68, 136));

        JPanel phai = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        phai.setOpaque(false);
        phai.add(user);

        header.add(trai, BorderLayout.WEST);
        header.add(phai, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
    }

    private void taoNoiDung() {
        cardLayout = new CardLayout();
        panelNoiDung = new JPanel(cardLayout);
        panelNoiDung.setBackground(new Color(240, 243, 246));

        panelNoiDung.add(taoManHinhTrangChu(), "TRANG_CHU");

        panelNoiDung.add(new NhanVienGUI(), "NHAN_VIEN");
        panelNoiDung.add(new ChucVuGUI(), "CHUC_VU");
        panelNoiDung.add(new TaiKhoanNhanVienGUI(), "TAI_KHOAN");
        panelNoiDung.add(new KhachHangGUI(), "KHACH_HANG");
        panelNoiDung.add(new NhaCungCapGUI(), "NHA_CUNG_CAP");
        panelNoiDung.add(new SanPhamGUI(), "SAN_PHAM");
        panelNoiDung.add(new CaLamViecGUI(), "CA_LAM_VIEC");
        panelNoiDung.add(new KhuyenMaiGUI(), "KHUYEN_MAI");

        panelNoiDung.add(new BanHangGUI(), "BAN_HANG");
        panelNoiDung.add(new PhieuDatHangGUI(), "PHIEU_DAT_HANG");
        panelNoiDung.add(new HoaDonGUI(), "HOA_DON");
        panelNoiDung.add(new BaoHanhGUI(), "BAO_HANH");
        panelNoiDung.add(new NhapKhoGUI(), "NHAP_KHO");
        panelNoiDung.add(new PhanCaGUI(), "PHAN_CA");

        panelNoiDung.add(new TimKiemSanPhamGUI(), "TIM_KIEM_SAN_PHAM");
        panelNoiDung.add(new LichSuMuaHangGUI(), "LICH_SU_MUA_HANG");
        panelNoiDung.add(new LichLamViecGUI(), "LICH_LAM_VIEC");
        panelNoiDung.add(new ThongKeGUI(), "THONG_KE");

        add(panelNoiDung, BorderLayout.CENTER);
        cardLayout.show(panelNoiDung, "TRANG_CHU");
    }

    private JPanel taoManHinhTrangChu() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("Tổng quan hoạt động");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(20, 40, 70));
        panel.add(title, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(2, 4, 15, 15));
        cards.setOpaque(false);
        String[] names = {
            "Doanh thu hôm nay", "Đơn hàng hôm nay",
            "Tổng khách hàng", "Sản phẩm sắp hết (<10)",
            "Phiếu đặt hàng chờ xử lý", "Phiếu nhập kho chờ xác nhận",
            "Phiếu bảo hành đang xử lý", "Khách hàng VIP"
        };
        for (String name : names) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 225, 230)),
                    new EmptyBorder(12, 15, 12, 15)));
            JLabel label = new JLabel(name);
            label.setForeground(new Color(110, 120, 130));
            label.setFont(new Font("Arial", Font.PLAIN, 12));
            JLabel value = new JLabel("—");
            value.setFont(new Font("Arial", Font.BOLD, 22));
            card.add(label, BorderLayout.NORTH);
            card.add(value, BorderLayout.CENTER);
            cards.add(card);
        }
        panel.add(cards, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(1, 2, 15, 0));
        bottom.setOpaque(false);
        bottom.setPreferredSize(new Dimension(getWidth(), 230));
        bottom.add(taoOTrangChu("Doanh thu theo tháng"));
        bottom.add(taoOTrangChu("Top sản phẩm bán chạy"));
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel taoOTrangChu(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));
        JLabel l = new JLabel("   " + title);
        l.setFont(new Font("Arial", Font.BOLD, 13));
        l.setBorder(new EmptyBorder(10, 0, 10, 0));
        p.add(l, BorderLayout.NORTH);
        p.add(new JLabel("Chưa có dữ liệu", JLabel.CENTER), BorderLayout.CENTER);
        return p;
    }

    private void taoThanhTrangThai() {
        JPanel status = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        status.setBackground(new Color(240, 240, 240));
        status.setBorder(BorderFactory.createMatteBorder(
                1, 0, 0, 0, new Color(210, 215, 220)));
        JLabel label = new JLabel("Tài khoản: " + tenNhanVien);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        label.setForeground(new Color(100, 100, 100));
        status.add(label);
        add(status, BorderLayout.SOUTH);
    }
}
