
package GUI;

import BUS.BanHangBUS;

import ENTITY.ChiTietHoaDon;
import ENTITY.HoaDon;
import ENTITY.KhachHang;
import ENTITY.KhuyenMai;
import ENTITY.NhaCungCap;
import ENTITY.NhanVien;
import ENTITY.SanPham;

import ENUM.PhuongThucThanhToan;
import ENUM.TrangThaiHoaDon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BanHangGUI extends JPanel {

    private final BanHangBUS banHangBUS = new BanHangBUS();

    // Nhan vien dang dang nhap
    private final NhanVien nhanVienDangNhap;

    // Bang du lieu
    private DefaultTableModel modelSanPham;
    private DefaultTableModel modelGioHang;

    private JTable tblSanPham;
    private JTable tblGioHang;

    // Cac thanh phan giao dien
    private JTextField txtTimKiem;
    private JLabel lblTongTien;

    private JComboBox<KhachHang> cboKhachHang;
    private JComboBox<PhuongThucThanhToan> cboThanhToan;
    private JComboBox<KhuyenMai> cboKhuyenMai;

    // Danh sach san pham
    private List<SanPham> danhSachSanPham = new ArrayList<>();
    private List<SanPham> danhSachSanPhamHienThi = new ArrayList<>();

    // Gio hang
    private final List<ChiTietHoaDon> gioHang = new ArrayList<>();

    public BanHangGUI(NhanVien nhanVienDangNhap) {

        if (nhanVienDangNhap == null) {
            throw new IllegalArgumentException(
                    "Nhan vien dang nhap khong hop le!"
            );
        }

        this.nhanVienDangNhap = nhanVienDangNhap;

        setLayout(new BorderLayout(12, 0));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        khoiTaoGiaoDien();

        // Tai du lieu khi mo man hinh
        taiDanhSachSanPham();
    }

    // ==================== KHOI TAO GIAO DIEN ====================

    private void khoiTaoGiaoDien() {

        // ==================== BEN TRAI: SAN PHAM ====================

        JPanel panelTrai = new JPanel(new BorderLayout(0, 10));
        panelTrai.setOpaque(false);
        panelTrai.setPreferredSize(new Dimension(500, 0));

        // Thanh tim kiem
        JPanel panelTimKiem = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 0)
        );
        panelTimKiem.setOpaque(false);

        txtTimKiem = new JTextField(18);
        txtTimKiem.setPreferredSize(new Dimension(200, 32));

        JButton btnTim = taoNut(
                "Tìm kiếm",
                new Color(14, 98, 195),
                Color.WHITE
        );

        panelTimKiem.add(new JLabel("Sản phẩm:"));
        panelTimKiem.add(txtTimKiem);
        panelTimKiem.add(btnTim);

        panelTrai.add(panelTimKiem, BorderLayout.NORTH);

        // Bang san pham
        modelSanPham = new DefaultTableModel(
                new Object[]{
                    "Mã SP",
                    "Tên sản phẩm",
                    "Giá bán",
                    "Tồn kho"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblSanPham = new JTable(modelSanPham);
        tblSanPham.setRowHeight(26);
        tblSanPham.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        panelTrai.add(
                new JScrollPane(tblSanPham),
                BorderLayout.CENTER
        );

        // Nut them vao gio hang
        JButton btnThemVaoGH = taoNut(
                "Thêm vào giỏ hàng ➔",
                new Color(14, 98, 195),
                Color.WHITE
        );

        JPanel panelNutThem = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );
        panelNutThem.setOpaque(false);
        panelNutThem.add(btnThemVaoGH);

        panelTrai.add(panelNutThem, BorderLayout.SOUTH);

        add(panelTrai, BorderLayout.WEST);

        // ==================== BEN PHAI: GIO HANG ====================

        JPanel panelPhai = new JPanel(new BorderLayout(0, 10));
        panelPhai.setOpaque(false);

        JLabel lblTieuDeGH = new JLabel(
                "Giỏ hàng / Lập hóa đơn bán hàng"
        );

        lblTieuDeGH.setFont(
                new Font("Arial", Font.BOLD, 16)
        );
        lblTieuDeGH.setForeground(
                new Color(20, 40, 70)
        );

        panelPhai.add(lblTieuDeGH, BorderLayout.NORTH);

        // Bang gio hang
        modelGioHang = new DefaultTableModel(
                new Object[]{
                    "Mã SP",
                    "Tên sản phẩm",
                    "Đơn giá",
                    "Số lượng",
                    "Thành tiền"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblGioHang = new JTable(modelGioHang);
        tblGioHang.setRowHeight(26);
        tblGioHang.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        panelPhai.add(
                new JScrollPane(tblGioHang),
                BorderLayout.CENTER
        );

        // ==================== THONG TIN THANH TOAN ====================

        JPanel panelThanhToan = new JPanel(
                new GridLayout(4, 2, 8, 8)
        );

        panelThanhToan.setOpaque(false);
        panelThanhToan.setBorder(
                new EmptyBorder(10, 0, 10, 0)
        );

        // Lay danh sach khach hang
        cboKhachHang = new JComboBox<>();

        taiDanhSachKhachHang();

        // Hien thi thong tin khach hang
        cboKhachHang.setRenderer(
                new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {

                super.getListCellRendererComponent(
                        list,
                        value,
                        index,
                        isSelected,
                        cellHasFocus
                );

                if (value instanceof KhachHang) {

                    KhachHang kh = (KhachHang) value;

                    setText(
                            kh.getMaKhachHang()
                            + " - "
                            + kh.getHoTenKhachHang()
                            + " ("
                            + kh.getSoDienThoai()
                            + ")"
                    );
                }

                return this;
            }
        });

        // Khuyen mai con hieu luc
        cboKhuyenMai = new JComboBox<>();
        taiDanhSachKhuyenMai();
        cboKhuyenMai.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof KhuyenMai) {
                    KhuyenMai km = (KhuyenMai) value;
                    setText(km.getMaKhuyenMai() + " - " + km.getTenKhuyenMai()
                            + " - Giảm " + String.format("%.0f", km.getGiaTriGiam()) + "%");
                } else {
                    setText("Không áp dụng");
                }
                return this;
            }
        });
        cboKhuyenMai.addActionListener(e -> capNhatTongTien());

        // Phuong thuc thanh toan
        cboThanhToan = new JComboBox<>(PhuongThucThanhToan.values());

        // Tong tien
        lblTongTien = new JLabel("0 VNĐ");
        lblTongTien.setFont(
                new Font("Arial", Font.BOLD, 16)
        );
        lblTongTien.setForeground(
                new Color(220, 38, 38)
        );

        panelThanhToan.add(
                new JLabel("Chọn khách hàng:")
        );
        panelThanhToan.add(cboKhachHang);

        panelThanhToan.add(new JLabel("Khuyến mãi:"));
        panelThanhToan.add(cboKhuyenMai);

        panelThanhToan.add(
                new JLabel("Phương thức thanh toán:")
        );
        panelThanhToan.add(cboThanhToan);

        panelThanhToan.add(
                new JLabel("Tổng tiền thanh toán:")
        );
        panelThanhToan.add(lblTongTien);

        // ==================== CAC NUT CHUC NANG ====================

        JButton btnXoaKhoiGH = taoNut(
                "Xóa sản phẩm chọn",
                new Color(220, 38, 38),
                Color.WHITE
        );

        JButton btnThanhToan = taoNut(
                "Thanh toán & Lập hóa đơn",
                new Color(16, 185, 129),
                Color.WHITE
        );

        JPanel panelNutChucNang = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );
        panelNutChucNang.setOpaque(false);

        panelNutChucNang.add(btnXoaKhoiGH);
        panelNutChucNang.add(btnThanhToan);

        JPanel panelDuoiPhai = new JPanel(
                new BorderLayout()
        );
        panelDuoiPhai.setOpaque(false);

        panelDuoiPhai.add(
                panelThanhToan,
                BorderLayout.CENTER
        );

        panelDuoiPhai.add(
                panelNutChucNang,
                BorderLayout.SOUTH
        );

        panelPhai.add(
                panelDuoiPhai,
                BorderLayout.SOUTH
        );

        add(panelPhai, BorderLayout.CENTER);

        // ==================== GAN SU KIEN ====================

        btnTim.addActionListener(
                e -> timKiemSanPham()
        );

        txtTimKiem.addActionListener(
                e -> timKiemSanPham()
        );

        btnThemVaoGH.addActionListener(
                e -> themVaoGioHang()
        );

        btnXoaKhoiGH.addActionListener(
                e -> xoaKhoiGioHang()
        );

        btnThanhToan.addActionListener(
                e -> xuLyThanhToan()
        );
    }

    // ==================== TAI DANH SACH KHACH HANG ====================

    private void taiDanhSachKhachHang() {

        cboKhachHang.removeAllItems();

        List<KhachHang> danhSachKH =
                banHangBUS.getDanhSachKhachHang();

        if (danhSachKH == null) {
            return;
        }

        for (KhachHang kh : danhSachKH) {

            if (kh != null) {
                cboKhachHang.addItem(kh);
            }
        }
    }

    private void taiDanhSachKhuyenMai() {
        cboKhuyenMai.removeAllItems();
        cboKhuyenMai.addItem(null);
        LocalDateTime now = LocalDateTime.now();
        List<KhuyenMai> danhSach = banHangBUS.getDanhSachKhuyenMai();
        if (danhSach == null) return;

        for (KhuyenMai km : danhSach) {
            if (km == null || !km.isTrangThai()
                    || km.getThoiGianBatDau() == null
                    || km.getThoiGianKetThuc() == null) {
                continue;
            }

            // Cho phep ap dung trong ca ngay bat dau va ngay ket thuc.
            boolean dungThoiGian = !now.isBefore(km.getThoiGianBatDau())
                    && !now.isAfter(km.getThoiGianKetThuc());
            if (dungThoiGian && km.getGiaTriGiam() > 0
                    && km.getGiaTriGiam() <= 100) {
                cboKhuyenMai.addItem(km);
            }
        }
    }

    private double tinhTienGiam() {
        KhuyenMai km = cboKhuyenMai == null
                ? null : (KhuyenMai) cboKhuyenMai.getSelectedItem();
        if (km == null) return 0;

        // giaTriGiam trong bang KhuyenMai duoc luu theo phan tram.
        double phanTram = Math.max(0, Math.min(100, km.getGiaTriGiam()));
        return tinhTongTien() * phanTram / 100.0;
    }

    private void capNhatTongTien() {
        if (lblTongTien == null) return;
        lblTongTien.setText(String.format("%,.0f VNĐ",
                Math.max(0, tinhTongTien() - tinhTienGiam())));
    }

    // ==================== TAI DANH SACH SAN PHAM ====================

    private void taiDanhSachSanPham() {

        danhSachSanPham = banHangBUS.getDanhSachSanPham();

        if (danhSachSanPham == null) {
            danhSachSanPham = new ArrayList<>();
        }

        danhSachSanPhamHienThi = new ArrayList<>(
                danhSachSanPham
        );

        hienThiDanhSachSanPham(
                danhSachSanPhamHienThi
        );
    }

    // ==================== HIEN THI SAN PHAM ====================

    private void hienThiDanhSachSanPham(
            List<SanPham> danhSach
    ) {

        modelSanPham.setRowCount(0);

        for (SanPham sp : danhSach) {

            if (sp == null) {
                continue;
            }

            modelSanPham.addRow(new Object[]{
                sp.getMaSanPham(),
                sp.getTenSanPham(),
                String.format("%,.0f", sp.getGiaBan()),
                sp.getSoLuongTon()
            });
        }
    }

    // ==================== TIM KIEM SAN PHAM ====================

    private void timKiemSanPham() {

        String tuKhoa = txtTimKiem.getText()
                .trim()
                .toLowerCase();

        danhSachSanPhamHienThi = new ArrayList<>();

        for (SanPham sp : danhSachSanPham) {

            if (sp == null) {
                continue;
            }

            String maSP = sp.getMaSanPham() == null
                    ? ""
                    : sp.getMaSanPham().toLowerCase();

            String tenSP = sp.getTenSanPham() == null
                    ? ""
                    : sp.getTenSanPham().toLowerCase();

            if (maSP.contains(tuKhoa)
                    || tenSP.contains(tuKhoa)) {

                danhSachSanPhamHienThi.add(sp);
            }
        }

        hienThiDanhSachSanPham(
                danhSachSanPhamHienThi
        );
    }

    // ==================== THEM SAN PHAM VAO GIO HANG ====================

    private void themVaoGioHang() {

        int row = tblSanPham.getSelectedRow();

        if (row < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn sản phẩm cần mua.",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        // Chuyen dong duoc chon ve dong trong model
        int modelRow = tblSanPham.convertRowIndexToModel(row);

        if (modelRow < 0
                || modelRow >= danhSachSanPhamHienThi.size()) {
            return;
        }

        SanPham sp = danhSachSanPhamHienThi.get(modelRow);

        if (sp == null) {
            return;
        }

        // Kiem tra ton kho
        if (sp.getSoLuongTon() <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Sản phẩm đã hết hàng trong kho!",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        boolean daCo = false;

        // Kiem tra san pham da co trong gio hang chua
        for (ChiTietHoaDon ct : gioHang) {

            if (ct.getMaSanPham() != null
                    && ct.getMaSanPham().getMaSanPham()
                            .equals(sp.getMaSanPham())) {

                // Kiem tra so luong
                if (ct.getSoLuong() + 1 > sp.getSoLuongTon()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Số lượng trong giỏ vượt quá tồn kho!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE
                    );

                    return;
                }

                ct.setSoLuong(ct.getSoLuong() + 1);

                daCo = true;
                break;
            }
        }

        // Neu chua co san pham thi them moi
        if (!daCo) {

            HoaDon hoaDonTam = new HoaDon();

            ChiTietHoaDon chiTietMoi = new ChiTietHoaDon(
                    hoaDonTam,
                    sp,
                    1,
                    sp.getGiaBan(),
                    0.10f
            );

            gioHang.add(chiTietMoi);
        }

        capNhatBangGioHang();
    }

    // ==================== XOA SAN PHAM KHOI GIO HANG ====================

    private void xoaKhoiGioHang() {

        int row = tblGioHang.getSelectedRow();

        if (row < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn sản phẩm trong giỏ hàng để xóa.",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        int modelRow = tblGioHang.convertRowIndexToModel(row);

        if (modelRow < 0 || modelRow >= gioHang.size()) {
            return;
        }

        gioHang.remove(modelRow);

        capNhatBangGioHang();
    }

    // ==================== CAP NHAT BANG GIO HANG ====================

    private void capNhatBangGioHang() {

        modelGioHang.setRowCount(0);

        double tongTien = 0;

        for (ChiTietHoaDon ct : gioHang) {

            if (ct == null || ct.getMaSanPham() == null) {
                continue;
            }

            double thanhTien =
                    ct.getSoLuong()
                    * ct.getDonGia()
                    * (1 + ct.getThueVAT());

            tongTien += thanhTien;

            modelGioHang.addRow(new Object[]{
                ct.getMaSanPham().getMaSanPham(),
                ct.getMaSanPham().getTenSanPham(),
                String.format("%,.0f", ct.getDonGia()),
                ct.getSoLuong(),
                String.format("%,.0f", thanhTien)
            });
        }

        capNhatTongTien();
    }

    // ==================== TINH TONG TIEN ====================

    private double tinhTongTien() {

        double tongTien = 0;

        for (ChiTietHoaDon ct : gioHang) {

            if (ct == null) {
                continue;
            }

            tongTien += ct.getSoLuong()
                    * ct.getDonGia()
                    * (1 + ct.getThueVAT());
        }

        return tongTien;
    }

    // ==================== KIEM TRA NHA CUNG CAP ====================

    private boolean cungNhaCungCap() {

        if (gioHang.isEmpty()) {
            return false;
        }

        SanPham spDauTien = gioHang.get(0).getMaSanPham();

        if (spDauTien == null) {
            return false;
        }

        NhaCungCap nhaCungCapDauTien = spDauTien.getMaNCC();

        String maNCCDauTien = nhaCungCapDauTien == null
                ? null
                : nhaCungCapDauTien.getMaNCC();

        for (ChiTietHoaDon ct : gioHang) {

            if (ct == null || ct.getMaSanPham() == null) {
                return false;
            }

            NhaCungCap ncc = ct.getMaSanPham().getMaNCC();

            String maNCC = ncc == null
                    ? null
                    : ncc.getMaNCC();

            if (maNCCDauTien == null) {

                if (maNCC != null) {
                    return false;
                }

            } else if (!maNCCDauTien.equals(maNCC)) {
                return false;
            }
        }

        return true;
    }

    // ==================== THANH TOAN VA LAP HOA DON ====================

    private void xuLyThanhToan() {

        // Kiem tra gio hang
        if (gioHang.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Giỏ hàng đang trống!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Kiem tra khach hang
        KhachHang kh = (KhachHang) cboKhachHang.getSelectedItem();

        if (kh == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn khách hàng!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Kiem tra phuong thuc thanh toan
        PhuongThucThanhToan phuongThuc =
                (PhuongThucThanhToan) cboThanhToan.getSelectedItem();

        if (phuongThuc == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn phương thức thanh toán!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Kiem tra nhan vien
        if (nhanVienDangNhap == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Không xác định được nhân viên đang đăng nhập!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Kiem tra nha cung cap
        if (!cungNhaCungCap()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Các sản phẩm trong giỏ phải cùng một nhà cung cấp "
                    + "để lập chung một hóa đơn!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Hien thi xac nhan thanh toan
        int xacNhan = JOptionPane.showConfirmDialog(
                this,
                "Tiền hàng: " + String.format("%,.0f VNĐ", tinhTongTien())
                + "\nTiền giảm: " + String.format("%,.0f VNĐ", tinhTienGiam())
                + "\nTổng thanh toán: "
                + String.format("%,.0f VNĐ", Math.max(0, tinhTongTien() - tinhTienGiam()))
                + "\nBạn có chắc chắn muốn thanh toán không?",
                "Xác nhận thanh toán",
                JOptionPane.YES_NO_OPTION
        );

        if (xacNhan != JOptionPane.YES_OPTION) {
            return;
        }

        // Tao ma hoa don
        String maHDMoi = banHangBUS.taoMaHoaDon();

        if (maHDMoi == null || maHDMoi.trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tạo mã hóa đơn mới!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Lay nha cung cap tu san pham dau tien
        NhaCungCap nhaCungCap = gioHang.get(0)
                .getMaSanPham()
                .getMaNCC();

        // Ban hang truc tiep nen khong co phieu dat hang
        // Luu khuyen mai da chon vao hoa don de in va tra cuu sau nay.
        HoaDon hd = new HoaDon(
                maHDMoi,
                null,
                kh,
                nhanVienDangNhap,
                nhaCungCap,
                (KhuyenMai) cboKhuyenMai.getSelectedItem(),
                phuongThuc,
                LocalDateTime.now(),
                "Bán hàng trực tiếp tại quầy",
                TrangThaiHoaDon.DA_THANH_TOAN
        );

        // Goi BUS xu ly thanh toan
        boolean thanhCong = banHangBUS.thanhToan(hd,gioHang);

        if (thanhCong) {

            JOptionPane.showMessageDialog(
                    this,
                    "Thanh toán thành công!\n"
                    + "Mã hóa đơn: " + maHDMoi,
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Hien thi hoa don va cho phep nguoi dung in ra giay.
            List<ChiTietHoaDon> chiTietIn = new ArrayList<>(gioHang);
            int chonIn = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có muốn xem và in hóa đơn không?",
                    "In hóa đơn",
                    JOptionPane.YES_NO_OPTION
            );
            if (chonIn == JOptionPane.YES_OPTION) {
                HoaDonInGUI dialogIn = new HoaDonInGUI(
                        SwingUtilities.getWindowAncestor(this), hd, chiTietIn);
                dialogIn.setVisible(true);
            }

            // Xoa gio hang sau khi thanh toan thanh cong
            gioHang.clear();

            capNhatBangGioHang();

            // Tai lai danh sach san pham
            taiDanhSachSanPham();

        } else {

            String loi = banHangBUS.getLastError();

            if (loi == null || loi.trim().isEmpty()) {
                loi = "Không có thông tin lỗi từ DAO.";
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Lập hóa đơn thất bại!\n" + loi,
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==================== TAO NUT ====================

    private JButton taoNut(
            String text,
            Color bg,
            Color fg
    ) {

        JButton button = new JButton(text);

        button.setBackground(bg);
        button.setForeground(fg);

        button.setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        button.setFocusPainted(false);

        return button;
    }
}
