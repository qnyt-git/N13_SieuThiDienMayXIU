package GUI;

import BUS.BanHangBUS;
import BUS.PhieuDatHangBUS;
import DAO.NhanVienDAO;

import ENTITY.ChiTietHoaDon;
import ENTITY.ChiTietPhieuDatHang;
import ENTITY.HoaDon;
import ENTITY.KhachHang;
import ENTITY.KhuyenMai;
import ENTITY.NhanVien;
import ENTITY.PhieuDatHang;
import ENTITY.SanPham;

import ENUM.PhuongThucThanhToan;
import ENUM.TrangThaiHoaDon;
import ENUM.TrangThaiPhieuDatHang;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BanHangGUI extends JPanel {

    private final BanHangBUS banHangBUS = new BanHangBUS();
    private final PhieuDatHangBUS phieuDatHangBUS = new PhieuDatHangBUS();

    // Nhan vien dang dang nhap
    private final NhanVien nhanVienDangNhap;

    // 4 Tab cua man hinh Ban hang & Thanh toan
    private JTabbedPane tabs;
    private PhieuDatHangGUI tabPhieuDatHang;
    private HoaDonGUI tabHoaDon;

    // ==================== THANH PHAN TAB 1: BAN HANG & THANH TOAN ====================
    private DefaultTableModel modelSanPham;
    private DefaultTableModel modelGioHang;

    private JTable tblSanPham;
    private JTable tblGioHang;

    private JTextField txtTimKiem;
    private JSpinner spnSoLuong;
    private JLabel lblTongTien;

    private JComboBox<KhachHang> cboKhachHang;
    private JComboBox<PhuongThucThanhToan> cboThanhToan;
    private JComboBox<KhuyenMai> cboKhuyenMai;
    private JComboBox<Object> cboPhieuDat;
    private boolean dangNapPhieuDat = false;

    // Danh sach san pham
    private List<SanPham> danhSachSanPham = new ArrayList<>();
    private List<SanPham> danhSachSanPhamHienThi = new ArrayList<>();

    // Gio hang ban hang
    private final List<ChiTietHoaDon> gioHang = new ArrayList<>();

    // ==================== THANH PHAN TAB 2: LAP PHIEU DAT HANG ====================
    private DefaultTableModel modelSanPhamDat;
    private DefaultTableModel modelGioHangDat;
    private JTable tblSanPhamDat;
    private JTable tblGioHangDat;
    private JTextField txtTimKiemDat;
    private JSpinner spnSoLuongDat;
    private JComboBox<KhachHang> cboKhachHangDat;
    private JTextField txtGhiChuDat;
    private JLabel lblTongTienDat;
    private List<SanPham> danhSachSanPhamDatHienThi = new ArrayList<>();
    private final List<ChiTietPhieuDatHang> gioHangDat = new ArrayList<>();

    // Constructor mac dinh (dung khi TrangChuGUI goi new BanHangGUI())
    public BanHangGUI() {
        this(layNhanVienMacDinh());
    }

    public BanHangGUI(NhanVien nhanVienDangNhap) {
        this.nhanVienDangNhap = nhanVienDangNhap != null ? nhanVienDangNhap : layNhanVienMacDinh();

        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel lblTieuDe = new JLabel("Bán hàng & thanh toán");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 22));
        lblTieuDe.setForeground(new Color(12, 68, 136));
        add(lblTieuDe, BorderLayout.NORTH);

        tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel panelTabBanHang = taoTabBanHang();
        JPanel panelTabLapPhieuDat = taoTabLapPhieuDat();
        tabPhieuDatHang = new PhieuDatHangGUI();
        tabHoaDon = new HoaDonGUI();

        tabs.addTab("Bán hàng & thanh toán", panelTabBanHang);
        tabs.addTab("Lập phiếu đặt hàng", panelTabLapPhieuDat);
        tabs.addTab("Phiếu đặt hàng", tabPhieuDatHang);
        tabs.addTab("Hóa đơn", tabHoaDon);

        // Khi bam "Thanh toan phieu nay" o Tab 3 -> chuyen sang Tab 1 va chon san phieu dat hang
        tabPhieuDatHang.getBtnThanhToanPhieu().addActionListener(e -> {
            PhieuDatHang p = tabPhieuDatHang.pdhDangChon();
            if (p == null) return;
            if (p.getTrangThai() == TrangThaiPhieuDatHang.DA_THANH_TOAN) {
                JOptionPane.showMessageDialog(this, "Phiếu đặt hàng này đã được thanh toán!",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (p.getTrangThai() == TrangThaiPhieuDatHang.DA_HUY) {
                JOptionPane.showMessageDialog(this, "Phiếu đặt hàng này đã bị hủy!",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Chuyen sang Tab 0 truoc de ChangeListener chay xong, sau do moi gan phieu dat hang
            tabs.setSelectedIndex(0);
            chonPhieuDatDeThanhToan(p);
        });

        tabs.addChangeListener(e -> {
            int idx = tabs.getSelectedIndex();
            if (idx == 0 || idx == 1) {
                taiDanhSachSanPham();
                taiDanhSachKhachHang();
                taiDanhSachKhuyenMai();
                taiDanhSachPhieuDatCho();
            } else if (idx == 2) {
                tabPhieuDatHang.taiDuLieu();
            } else if (idx == 3) {
                tabHoaDon.taiDuLieu();
            }
        });

        add(tabs, BorderLayout.CENTER);

        // Tai du lieu khi mo man hinh
        taiDanhSachSanPham();
        taiDanhSachKhachHang();
        taiDanhSachKhuyenMai();
        taiDanhSachPhieuDatCho();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                taiDanhSachSanPham();
                taiDanhSachKhachHang();
                taiDanhSachKhuyenMai();
                taiDanhSachPhieuDatCho();
            }
        });
    }

    private static NhanVien layNhanVienMacDinh() {
        try {
            List<NhanVien> ds = new NhanVienDAO().getAll();
            if (ds != null && !ds.isEmpty()) return ds.get(0);
        } catch (Exception ignored) {
        }
        NhanVien nv = new NhanVien();
        nv.setMaNhanVien("NV01");
        nv.setHoTenNhanVien("Nhân viên bán hàng");
        return nv;
    }

    /**
     * Lay ten thuong hieu / nha cung cap tu doi tuong SanPham.
     */
    private String layTenNCC(SanPham sp) {
        if (sp == null || sp.getMaNCC() == null) return "";
        String thuongHieu = sp.getMaNCC().getTenThuongHieu();
        if (thuongHieu != null && !thuongHieu.trim().isEmpty()) {
            return thuongHieu;
        }
        return sp.getMaNCC().getTenNCC() != null
                ? sp.getMaNCC().getTenNCC()
                : sp.getMaNCC().getMaNCC();
    }

    /**
     * Lay noi san xuat tu doi tuong SanPham.
     */
    private String layNoiSanXuat(SanPham sp) {
        if (sp == null || sp.getNoiSanXuat() == null) return "";
        return sp.getNoiSanXuat().trim();
    }

    // ==================== TAB 1: KHOI TAO GIAO DIEN BAN HANG ====================

    private JPanel taoTabBanHang() {

        JPanel panelChinh = new JPanel(new GridLayout(1, 2, 12, 0));
        panelChinh.setOpaque(false);
        panelChinh.setBorder(new EmptyBorder(10, 0, 0, 0));

        // ==================== BEN TRAI: SAN PHAM ====================

        JPanel panelTrai = new JPanel(new BorderLayout(0, 10));
        panelTrai.setBackground(Color.WHITE);
        panelTrai.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                new EmptyBorder(12, 12, 12, 12)
        ));

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

        // Bang san pham (Bo sung cot Nha cung cap va Noi san xuat)
        modelSanPham = new DefaultTableModel(
                new Object[]{
                    "Mã SP",
                    "Tên sản phẩm",
                    "Nhà cung cấp",
                    "Nơi sản xuất",
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

        // Nut them vao gio hang kem o chinh sua so luong sat ben phai nhu cu
        spnSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spnSoLuong.setFont(new Font("Arial", Font.PLAIN, 13));
        spnSoLuong.setPreferredSize(new Dimension(70, 34));

        JButton btnThemVaoGH = taoNut(
                "Thêm vào giỏ hàng ➔",
                new Color(14, 98, 195),
                Color.WHITE
        );
        btnThemVaoGH.setPreferredSize(new Dimension(170, 34));

        JPanel panelNutThem = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );
        panelNutThem.setOpaque(false);
        panelNutThem.add(new JLabel("Số lượng:"));
        panelNutThem.add(spnSoLuong);
        panelNutThem.add(btnThemVaoGH);

        panelTrai.add(panelNutThem, BorderLayout.SOUTH);

        // ==================== BEN PHAI: GIO HANG ====================

        JPanel panelPhai = new JPanel(new BorderLayout(0, 10));
        panelPhai.setBackground(Color.WHITE);
        panelPhai.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                new EmptyBorder(12, 12, 12, 12)
        ));

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

        // Bang gio hang (Bo sung cot Nha cung cap va Noi san xuat)
        modelGioHang = new DefaultTableModel(
                new Object[]{
                    "Mã SP",
                    "Tên sản phẩm",
                    "Nhà cung cấp",
                    "Nơi sản xuất",
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
                new GridLayout(5, 2, 8, 8)
        );

        panelThanhToan.setOpaque(false);
        panelThanhToan.setBorder(
                new EmptyBorder(10, 0, 10, 0)
        );

        // Lay danh sach khach hang
        cboKhachHang = new JComboBox<>();

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
        cboKhuyenMai.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof KhuyenMai) {
                    KhuyenMai km = (KhuyenMai) value;
                    setText(km.getMaKhuyenMai() + " - " + km.getTenKhuyenMai()
                            + " - Giảm " + BanHangBUS.moTaMucGiam(km));
                } else {
                    setText("Không áp dụng");
                }
                return this;
            }
        });
        cboKhuyenMai.addActionListener(e -> capNhatTongTien());

        // Phieu dat hang cho xu ly
        cboPhieuDat = new JComboBox<>();
        cboPhieuDat.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof PhieuDatHang) {
                    PhieuDatHang p = (PhieuDatHang) value;
                    String tenKH = p.getMaKhachHang() != null ? p.getMaKhachHang().getHoTenKhachHang() : "";
                    setText(p.getMaPhieuDatHang() + " - " + tenKH);
                } else {
                    setText("(Bán trực tiếp)");
                }
                return this;
            }
        });
        cboPhieuDat.addActionListener(e -> napGioHangTuPhieuDat());

        // Phuong thuc thanh toan
        cboThanhToan = new JComboBox<>(PhuongThucThanhToan.values());
        cboThanhToan.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof PhuongThucThanhToan) {
                    setText(((PhuongThucThanhToan) value).getMoTa());
                }
                return this;
            }
        });

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

        panelThanhToan.add(new JLabel("Phiếu đặt hàng:"));
        panelThanhToan.add(cboPhieuDat);

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

        JButton btnXoaHetGH = taoNut(
                "Xóa hết",
                Color.WHITE,
                new Color(14, 98, 195)
        );
        btnXoaHetGH.setBorder(BorderFactory.createLineBorder(new Color(14, 98, 195)));
        btnXoaHetGH.setPreferredSize(new Dimension(95, 32));

        JButton btnThanhToan = taoNut(
                "Thanh toán & Lập hóa đơn",
                new Color(16, 185, 129),
                Color.WHITE
        );

        JPanel panelNutChucNang = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );
        panelNutChucNang.setOpaque(false);

        panelNutChucNang.add(btnXoaHetGH);
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

        panelChinh.add(panelTrai);
        panelChinh.add(panelPhai);

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

        tblSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    themVaoGioHang();
                }
            }
        });

        btnXoaKhoiGH.addActionListener(
                e -> xoaKhoiGioHang()
        );

        btnXoaHetGH.addActionListener(e -> {
            gioHang.clear();
            if (cboPhieuDat.getItemCount() > 0) {
                dangNapPhieuDat = true;
                cboPhieuDat.setSelectedIndex(0);
                dangNapPhieuDat = false;
            }
            capNhatBangGioHang();
        });

        btnThanhToan.addActionListener(
                e -> xuLyThanhToan()
        );

        return panelChinh;
    }

    // ==================== TAB 2: KHOI TAO GIAO DIEN LAP PHIEU DAT HANG ====================

    private JPanel taoTabLapPhieuDat() {
        JPanel panelChinh = new JPanel(new GridLayout(1, 2, 12, 0));
        panelChinh.setOpaque(false);
        panelChinh.setBorder(new EmptyBorder(10, 0, 0, 0));

        // --- Ben trai: San pham ---
        JPanel panelTrai = new JPanel(new BorderLayout(0, 10));
        panelTrai.setBackground(Color.WHITE);
        panelTrai.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JPanel panelTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelTimKiem.setOpaque(false);
        txtTimKiemDat = new JTextField(18);
        txtTimKiemDat.setPreferredSize(new Dimension(200, 32));
        JButton btnTimDat = taoNut("Tìm kiếm", new Color(14, 98, 195), Color.WHITE);
        panelTimKiem.add(new JLabel("Sản phẩm:"));
        panelTimKiem.add(txtTimKiemDat);
        panelTimKiem.add(btnTimDat);
        panelTrai.add(panelTimKiem, BorderLayout.NORTH);

        // Bang san pham dat hang (Bo sung cot Nha cung cap va Noi san xuat)
        modelSanPhamDat = new DefaultTableModel(
                new Object[]{"Mã SP", "Tên sản phẩm", "Nhà cung cấp", "Nơi sản xuất", "Giá bán", "Tồn kho"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblSanPhamDat = new JTable(modelSanPhamDat);
        tblSanPhamDat.setRowHeight(26);
        tblSanPhamDat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelTrai.add(new JScrollPane(tblSanPhamDat), BorderLayout.CENTER);

        spnSoLuongDat = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spnSoLuongDat.setFont(new Font("Arial", Font.PLAIN, 13));
        spnSoLuongDat.setPreferredSize(new Dimension(70, 34));

        JButton btnThemVaoGHDat = taoNut("Thêm vào giỏ hàng ➔", new Color(14, 98, 195), Color.WHITE);
        btnThemVaoGHDat.setPreferredSize(new Dimension(170, 34));

        JPanel panelNutThem = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelNutThem.setOpaque(false);
        panelNutThem.add(new JLabel("Số lượng:"));
        panelNutThem.add(spnSoLuongDat);
        panelNutThem.add(btnThemVaoGHDat);
        panelTrai.add(panelNutThem, BorderLayout.SOUTH);

        // --- Ben phai: Gio dat hang (KHONG co cboKhuyenMai) ---
        JPanel panelPhai = new JPanel(new BorderLayout(0, 10));
        panelPhai.setBackground(Color.WHITE);
        panelPhai.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel lblTieuDe = new JLabel("Giỏ đặt hàng / Lập phiếu đặt hàng");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 16));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        panelPhai.add(lblTieuDe, BorderLayout.NORTH);

        // Bang gio dat hang (Bo sung cot Nha cung cap va Noi san xuat)
        modelGioHangDat = new DefaultTableModel(
                new Object[]{"Mã SP", "Tên sản phẩm", "Nhà cung cấp", "Nơi sản xuất", "Đơn giá", "Số lượng", "Thành tiền"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblGioHangDat = new JTable(modelGioHangDat);
        tblGioHangDat.setRowHeight(26);
        tblGioHangDat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelPhai.add(new JScrollPane(tblGioHangDat), BorderLayout.CENTER);

        JPanel panelThongTinDat = new JPanel(new GridLayout(3, 2, 8, 8));
        panelThongTinDat.setOpaque(false);
        panelThongTinDat.setBorder(new EmptyBorder(10, 0, 10, 0));

        cboKhachHangDat = new JComboBox<>();
        cboKhachHangDat.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof KhachHang) {
                    KhachHang kh = (KhachHang) value;
                    setText(kh.getMaKhachHang() + " - " + kh.getHoTenKhachHang() + " (" + kh.getSoDienThoai() + ")");
                }
                return this;
            }
        });

        txtGhiChuDat = new JTextField();
        lblTongTienDat = new JLabel("0 VNĐ");
        lblTongTienDat.setFont(new Font("Arial", Font.BOLD, 16));
        lblTongTienDat.setForeground(new Color(220, 38, 38));

        panelThongTinDat.add(new JLabel("Chọn khách hàng *:"));
        panelThongTinDat.add(cboKhachHangDat);
        panelThongTinDat.add(new JLabel("Ghi chú:"));
        panelThongTinDat.add(txtGhiChuDat);
        panelThongTinDat.add(new JLabel("Tổng tiền tạm tính:"));
        panelThongTinDat.add(lblTongTienDat);

        JButton btnXoaHetDat = taoNut("Xóa hết", Color.WHITE, new Color(14, 98, 195));
        btnXoaHetDat.setBorder(BorderFactory.createLineBorder(new Color(14, 98, 195)));
        btnXoaHetDat.setPreferredSize(new Dimension(95, 32));

        JButton btnXoaDongDat = taoNut("Xóa sản phẩm chọn", new Color(220, 38, 38), Color.WHITE);
        JButton btnLapPhieuDat = taoNut("Lập phiếu đặt hàng", new Color(14, 98, 195), Color.WHITE);

        JPanel panelNutDat = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelNutDat.setOpaque(false);
        panelNutDat.add(btnXoaHetDat);
        panelNutDat.add(btnXoaDongDat);
        panelNutDat.add(btnLapPhieuDat);

        JPanel panelDuoiPhai = new JPanel(new BorderLayout());
        panelDuoiPhai.setOpaque(false);
        panelDuoiPhai.add(panelThongTinDat, BorderLayout.CENTER);
        panelDuoiPhai.add(panelNutDat, BorderLayout.SOUTH);

        panelPhai.add(panelDuoiPhai, BorderLayout.SOUTH);

        panelChinh.add(panelTrai);
        panelChinh.add(panelPhai);

        // Su kien Tab 2
        btnTimDat.addActionListener(e -> timKiemSanPhamDat());
        txtTimKiemDat.addActionListener(e -> timKiemSanPhamDat());
        btnThemVaoGHDat.addActionListener(e -> themVaoGioHangDat());
        tblSanPhamDat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) themVaoGioHangDat();
            }
        });
        btnXoaDongDat.addActionListener(e -> xoaKhoiGioHangDat());
        btnXoaHetDat.addActionListener(e -> {
            gioHangDat.clear();
            capNhatBangGioHangDat();
        });
        btnLapPhieuDat.addActionListener(e -> xuLyLapPhieuDatHang());

        return panelChinh;
    }

    // ==================== TAI DANH SACH KHACH HANG / KHUYEN MAI / PHIEU DAT ====================

    private void taiDanhSachKhachHang() {
        KhachHang khDangChon = cboKhachHang != null ? (KhachHang) cboKhachHang.getSelectedItem() : null;
        if (cboKhachHang != null) cboKhachHang.removeAllItems();
        if (cboKhachHangDat != null) cboKhachHangDat.removeAllItems();

        List<KhachHang> danhSachKH =
                banHangBUS.getDanhSachKhachHang();

        if (danhSachKH == null) {
            return;
        }

        for (KhachHang kh : danhSachKH) {

            if (kh != null) {
                if (cboKhachHang != null) {
                    cboKhachHang.addItem(kh);
                    if (khDangChon != null && kh.getMaKhachHang().equals(khDangChon.getMaKhachHang())) {
                        cboKhachHang.setSelectedItem(kh);
                    }
                }
                if (cboKhachHangDat != null) cboKhachHangDat.addItem(kh);
            }
        }
    }

    private void taiDanhSachKhuyenMai() {
        if (cboKhuyenMai == null) return;
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
            boolean dungThoiGian = !now.isAfter(km.getThoiGianKetThuc());
            if (dungThoiGian && km.getGiaTriGiam() > 0) {
                cboKhuyenMai.addItem(km);
            }
        }
    }

    private void taiDanhSachPhieuDatCho() {
        if (cboPhieuDat == null) return;
        dangNapPhieuDat = true;
        cboPhieuDat.removeAllItems();
        cboPhieuDat.addItem("(Bán trực tiếp)");
        for (PhieuDatHang p : phieuDatHangBUS.getDanhSachChoXuLy()) {
            cboPhieuDat.addItem(p);
        }
        dangNapPhieuDat = false;
    }

    public void chonPhieuDatDeThanhToan(PhieuDatHang p) {
        if (p == null || cboPhieuDat == null) return;

        dangNapPhieuDat = true;
        int foundIndex = -1;
        for (int i = 0; i < cboPhieuDat.getItemCount(); i++) {
            Object item = cboPhieuDat.getItemAt(i);
            if (item instanceof PhieuDatHang
                    && ((PhieuDatHang) item).getMaPhieuDatHang().equals(p.getMaPhieuDatHang())) {
                foundIndex = i;
                break;
            }
        }

        if (foundIndex < 0) {
            cboPhieuDat.addItem(p);
            foundIndex = cboPhieuDat.getItemCount() - 1;
        }

        cboPhieuDat.setSelectedIndex(foundIndex);
        dangNapPhieuDat = false;

        napGioHangTuPhieuDat();
    }

    private void napGioHangTuPhieuDat() {
        if (dangNapPhieuDat || cboPhieuDat == null) return;
        gioHang.clear();

        Object chon = cboPhieuDat.getSelectedItem();
        if (chon instanceof PhieuDatHang) {
            PhieuDatHang pdh = (PhieuDatHang) chon;
            List<ChiTietPhieuDatHang> dsCt = phieuDatHangBUS.getChiTietByPhieu(pdh);
            for (ChiTietPhieuDatHang c : dsCt) {
                if (c.getMaSanPham() != null) {
                    gioHang.add(new ChiTietHoaDon(
                            new HoaDon(),
                            c.getMaSanPham(),
                            c.getSoLuong(),
                            c.getDonGia(),
                            0.10f
                    ));
                }
            }
            if (pdh.getMaKhachHang() != null) {
                String maKH = pdh.getMaKhachHang().getMaKhachHang();
                for (int i = 0; i < cboKhachHang.getItemCount(); i++) {
                    KhachHang kh = cboKhachHang.getItemAt(i);
                    if (kh != null && kh.getMaKhachHang().equals(maKH)) {
                        cboKhachHang.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
        capNhatBangGioHang();
    }

    private double tinhTienGiam() {
        KhuyenMai km = cboKhuyenMai == null
                ? null : (KhuyenMai) cboKhuyenMai.getSelectedItem();
        if (km == null) return 0;

        // giaTriGiam trong bang KhuyenMai duoc tinh qua BanHangBUS (ho tro ca 0.10, 10% va tien mat).
        return BanHangBUS.tinhTienGiam(km, tinhTongTien());
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
        danhSachSanPhamDatHienThi = new ArrayList<>(
                danhSachSanPham
        );

        hienThiDanhSachSanPham(
                danhSachSanPhamHienThi
        );
        hienThiDanhSachSanPhamDat(
                danhSachSanPhamDatHienThi
        );
    }

    // ==================== HIEN THI SAN PHAM ====================

    private void hienThiDanhSachSanPham(
            List<SanPham> danhSach
    ) {
        if (modelSanPham == null) return;
        modelSanPham.setRowCount(0);

        for (SanPham sp : danhSach) {

            if (sp == null) {
                continue;
            }

            modelSanPham.addRow(new Object[]{
                sp.getMaSanPham(),
                sp.getTenSanPham(),
                layTenNCC(sp),
                layNoiSanXuat(sp),
                String.format("%,.0f", sp.getGiaBan()),
                sp.getSoLuongTon()
            });
        }
    }

    private void hienThiDanhSachSanPhamDat(List<SanPham> danhSach) {
        if (modelSanPhamDat == null) return;
        modelSanPhamDat.setRowCount(0);
        for (SanPham sp : danhSach) {
            if (sp == null) continue;
            modelSanPhamDat.addRow(new Object[]{
                sp.getMaSanPham(),
                sp.getTenSanPham(),
                layTenNCC(sp),
                layNoiSanXuat(sp),
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

            String tenNCC = layTenNCC(sp).toLowerCase();
            String noiSX = layNoiSanXuat(sp).toLowerCase();

            if (maSP.contains(tuKhoa)
                    || tenSP.contains(tuKhoa)
                    || tenNCC.contains(tuKhoa)
                    || noiSX.contains(tuKhoa)) {

                danhSachSanPhamHienThi.add(sp);
            }
        }

        hienThiDanhSachSanPham(
                danhSachSanPhamHienThi
        );
    }

    private void timKiemSanPhamDat() {
        String tuKhoa = txtTimKiemDat.getText().trim().toLowerCase();
        danhSachSanPhamDatHienThi = new ArrayList<>();
        for (SanPham sp : danhSachSanPham) {
            if (sp == null) continue;
            String maSP = sp.getMaSanPham() == null ? "" : sp.getMaSanPham().toLowerCase();
            String tenSP = sp.getTenSanPham() == null ? "" : sp.getTenSanPham().toLowerCase();
            String tenNCC = layTenNCC(sp).toLowerCase();
            String noiSX = layNoiSanXuat(sp).toLowerCase();
            if (maSP.contains(tuKhoa) || tenSP.contains(tuKhoa) || tenNCC.contains(tuKhoa) || noiSX.contains(tuKhoa)) {
                danhSachSanPhamDatHienThi.add(sp);
            }
        }
        hienThiDanhSachSanPhamDat(danhSachSanPhamDatHienThi);
    }

    // ==================== THEM SAN PHAM VAO GIO HANG (TAB 1) ====================

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

        int soLuongThem = (Integer) spnSoLuong.getValue();

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
                if (ct.getSoLuong() + soLuongThem > sp.getSoLuongTon()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Số lượng vượt quá tồn kho (còn " + sp.getSoLuongTon() + ")",
                            "Không đủ hàng",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                ct.setSoLuong(ct.getSoLuong() + soLuongThem);

                daCo = true;
                break;
            }
        }

        // Neu chua co san pham thi them moi
        if (!daCo) {

            if (soLuongThem > sp.getSoLuongTon()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Số lượng vượt quá tồn kho (còn " + sp.getSoLuongTon() + ")",
                        "Không đủ hàng",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            HoaDon hoaDonTam = new HoaDon();

            ChiTietHoaDon chiTietMoi = new ChiTietHoaDon(
                    hoaDonTam,
                    sp,
                    soLuongThem,
                    sp.getGiaBan(),
                    0.10f
            );

            gioHang.add(chiTietMoi);
        }

        spnSoLuong.setValue(1);

        capNhatBangGioHang();
    }

    // ==================== XOA SAN PHAM KHOI GIO HANG (TAB 1) ====================

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

    // ==================== CAP NHAT BANG GIO HANG (TAB 1) ====================

    private void capNhatBangGioHang() {

        modelGioHang.setRowCount(0);

        for (ChiTietHoaDon ct : gioHang) {

            if (ct == null || ct.getMaSanPham() == null) {
                continue;
            }

            double thanhTien = BanHangBUS.tinhThanhTien(ct);

            modelGioHang.addRow(new Object[]{
                ct.getMaSanPham().getMaSanPham(),
                ct.getMaSanPham().getTenSanPham(),
                layTenNCC(ct.getMaSanPham()),
                layNoiSanXuat(ct.getMaSanPham()),
                String.format("%,.0f", ct.getDonGia()),
                ct.getSoLuong(),
                String.format("%,.0f", thanhTien)
            });
        }

        capNhatTongTien();
    }

    // ==================== TINH TONG TIEN (TAB 1) ====================

    private double tinhTongTien() {

        double tongTien = 0;

        for (ChiTietHoaDon ct : gioHang) {

            if (ct == null) {
                continue;
            }

            tongTien += BanHangBUS.tinhThanhTien(ct);
        }

        return tongTien;
    }

    // ==================== DIALOG THU TIEN MAT NHU CODE CU ====================

    private String dinhDangTienDong(double soTien) {
        return String.format("%,.0f", soTien).replace(',', '.') + "đ";
    }

    /**
     * Hoi so tien khach dua (tien mat) giong GioHangPanel cu.
     * @return so tien khach dua, hoac -1 neu bam Cancel.
     */
    private double nhapTienKhachDua(double canThu) {
        long canThuLamTron = Math.round(canThu);
        while (true) {
            String in = JOptionPane.showInputDialog(
                    this,
                    "Cần thu: " + dinhDangTienDong(canThuLamTron) + "\nTiền khách đưa (đồng):",
                    "Thu tiền mặt",
                    JOptionPane.QUESTION_MESSAGE
            );
            if (in == null) {
                return -1;
            }
            try {
                String clean = in.trim().replace(".", "").replace(",", "").replace("đ", "").replace(" ", "");
                double v = Double.parseDouble(clean);
                if (v >= canThuLamTron) {
                    return v;
                }
                JOptionPane.showMessageDialog(
                        this,
                        "Số tiền khách đưa chưa đủ (còn thiếu " + dinhDangTienDong(canThuLamTron - v) + ")",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE
                );
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng nhập số tiền hợp lệ",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    // ==================== THANH TOAN VA LAP HOA DON (TAB 1) ====================

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

        double canThu = Math.max(0, tinhTongTien() - tinhTienGiam());
        String ghiChu = "Bán hàng trực tiếp tại quầy";

        // Nghiep vu: Neu thanh toan TIEN_MAT thi hien dialog Thu tien mat & tinh tien thoi nhu code cu
        if (phuongThuc == PhuongThucThanhToan.TIEN_MAT) {
            double nhan = nhapTienKhachDua(canThu);
            if (nhan < 0) {
                return; // Nguoi dung bam Cancel
            }
            ghiChu = "Khách đưa " + dinhDangTienDong(nhan)
                    + ", tiền thối " + dinhDangTienDong(nhan - Math.round(canThu));
        } else {
            int xacNhan = JOptionPane.showConfirmDialog(
                    this,
                    "Cần thu: " + dinhDangTienDong(canThu)
                    + "\nPhương thức: " + phuongThuc.getMoTa()
                    + "\nXác nhận thanh toán?",
                    "Xác nhận thanh toán",
                    JOptionPane.YES_NO_OPTION
            );
            if (xacNhan != JOptionPane.YES_OPTION) {
                return;
            }
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

        PhieuDatHang pdhChon = (cboPhieuDat != null && cboPhieuDat.getSelectedItem() instanceof PhieuDatHang)
                ? (PhieuDatHang) cboPhieuDat.getSelectedItem()
                : null;

        // Luu khuyen mai da chon vao hoa don de in va tra cuu sau nay.
        HoaDon hd = new HoaDon(
                maHDMoi,
                pdhChon,
                kh,
                nhanVienDangNhap,
                (KhuyenMai) cboKhuyenMai.getSelectedItem(),
                phuongThuc,
                LocalDateTime.now(),
                ghiChu,
                TrangThaiHoaDon.DA_THANH_TOAN
        );

        // Goi BUS xu ly thanh toan
        boolean thanhCong = banHangBUS.thanhToan(hd, gioHang);

        if (thanhCong) {

            // Neu thanh toan tu Phieu dat hang thi cap nhat trang thai Phieu dat sang DA_THANH_TOAN
            if (pdhChon != null) {
                phieuDatHangBUS.thanhToan(pdhChon);
            }

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

            // Tai lai danh sach san pham va phieu dat hang
            taiDanhSachSanPham();
            taiDanhSachPhieuDatCho();
            tabPhieuDatHang.taiDuLieu();
            tabHoaDon.taiDuLieu();

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

    // ==================== XU LY GIO DAT HANG (TAB 2) ====================

    private void themVaoGioHangDat() {
        int row = tblSanPhamDat.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần đặt.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int modelRow = tblSanPhamDat.convertRowIndexToModel(row);
        if (modelRow < 0 || modelRow >= danhSachSanPhamDatHienThi.size()) return;

        SanPham sp = danhSachSanPhamDatHienThi.get(modelRow);
        if (sp == null) return;

        int soLuongThem = (Integer) spnSoLuongDat.getValue();
        if (sp.getSoLuongTon() <= 0) {
            JOptionPane.showMessageDialog(this, "Sản phẩm đã hết hàng trong kho!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean daCo = false;
        for (ChiTietPhieuDatHang ct : gioHangDat) {
            if (ct.getMaSanPham() != null && ct.getMaSanPham().getMaSanPham().equals(sp.getMaSanPham())) {
                if (ct.getSoLuong() + soLuongThem > sp.getSoLuongTon()) {
                    JOptionPane.showMessageDialog(this,
                            "Số lượng vượt quá tồn kho (còn " + sp.getSoLuongTon() + ")",
                            "Không đủ hàng", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                ct.setSoLuong(ct.getSoLuong() + soLuongThem);
                daCo = true;
                break;
            }
        }

        if (!daCo) {
            if (soLuongThem > sp.getSoLuongTon()) {
                JOptionPane.showMessageDialog(this,
                        "Số lượng vượt quá tồn kho (còn " + sp.getSoLuongTon() + ")",
                        "Không đủ hàng",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            gioHangDat.add(new ChiTietPhieuDatHang(new PhieuDatHang(), sp, soLuongThem, sp.getGiaBan()));
        }

        spnSoLuongDat.setValue(1);
        capNhatBangGioHangDat();
    }

    private void xoaKhoiGioHangDat() {
        int row = tblGioHangDat.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm trong giỏ đặt hàng để xóa.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int modelRow = tblGioHangDat.convertRowIndexToModel(row);
        if (modelRow >= 0 && modelRow < gioHangDat.size()) {
            gioHangDat.remove(modelRow);
            capNhatBangGioHangDat();
        }
    }

    private void capNhatBangGioHangDat() {
        modelGioHangDat.setRowCount(0);
        for (ChiTietPhieuDatHang ct : gioHangDat) {
            if (ct == null || ct.getMaSanPham() == null) continue;
            double thanhTien = Math.round(ct.getSoLuong() * ct.getDonGia());
            modelGioHangDat.addRow(new Object[]{
                ct.getMaSanPham().getMaSanPham(),
                ct.getMaSanPham().getTenSanPham(),
                layTenNCC(ct.getMaSanPham()),
                layNoiSanXuat(ct.getMaSanPham()),
                String.format("%,.0f", ct.getDonGia()),
                ct.getSoLuong(),
                String.format("%,.0f", thanhTien)
            });
        }
        double tong = phieuDatHangBUS.tinhTongTien(gioHangDat);
        lblTongTienDat.setText(String.format("%,.0f VNĐ", tong));
    }

    private void xuLyLapPhieuDatHang() {
        if (gioHangDat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ đặt hàng đang trống!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        KhachHang kh = (KhachHang) cboKhachHangDat.getSelectedItem();
        if (kh == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maPDHMoi = phieuDatHangBUS.taoMaMoi();
        PhieuDatHang pdh = new PhieuDatHang(
                maPDHMoi,
                kh,
                nhanVienDangNhap,
                LocalDateTime.now(),
                txtGhiChuDat.getText().trim(),
                TrangThaiPhieuDatHang.CHO_XU_LY
        );

        if (phieuDatHangBUS.insertWithDetails(pdh, gioHangDat)) {
            JOptionPane.showMessageDialog(this,
                    "Đã lập phiếu đặt hàng " + maPDHMoi + " (Chờ xử lý)!",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
            gioHangDat.clear();
            txtGhiChuDat.setText("");
            capNhatBangGioHangDat();
            taiDanhSachPhieuDatCho();
            tabPhieuDatHang.taiDuLieu();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Không lưu được phiếu đặt hàng!\n" + phieuDatHangBUS.getLastError(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
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