package GUI;

import DAO.NhanVienDAO;
import DAO.TaiKhoanNhanVienDAO;
import ENTITY.NhanVien;
import ENTITY.TaiKhoanNhanVien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanNhanVienGUI extends JPanel {
    private final TaiKhoanNhanVienDAO taiKhoanDAO = new TaiKhoanNhanVienDAO();
    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();
    
    private final DefaultTableModel modelTaiKhoan;
    private final JTable tblTaiKhoan;
    private final JTextField txtTimKiem;
    private final JLabel lblCount;
    private List<TaiKhoanNhanVien> danhSachTaiKhoan = new ArrayList<>();

    public TaiKhoanNhanVienGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        JLabel lblTieuDe = new JLabel("Quản lý tài khoản nhân viên");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        top.add(lblTieuDe, BorderLayout.NORTH);

        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        txtTimKiem = new JTextField(24);
        txtTimKiem.setPreferredSize(new Dimension(240, 32));
        left.add(new JLabel("Tìm kiếm:"));
        left.add(txtTimKiem);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        JButton btnSua = taoNut("Sửa", Color.WHITE, new Color(14, 98, 195));
        JButton btnXoa = taoNut("Xóa", new Color(220, 38, 38), Color.WHITE);
        JButton btnThem = taoNut("+ Thêm tài khoản", new Color(14, 98, 195), Color.WHITE);
        buttons.add(btnSua);
        buttons.add(btnXoa);
        buttons.add(btnThem);
        bar.add(left, BorderLayout.WEST);
        bar.add(buttons, BorderLayout.EAST);
        top.add(bar, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        modelTaiKhoan = new DefaultTableModel(new Object[]{
                "Mã tài khoản", "Tên đăng nhập", "Mã nhân viên", "Họ tên nhân viên", "Loại quyền", "Trạng thái"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblTaiKhoan = new JTable(modelTaiKhoan);
        tblTaiKhoan.setRowHeight(28);
        tblTaiKhoan.setFont(new Font("Arial", Font.PLAIN, 13));
        tblTaiKhoan.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblTaiKhoan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tblTaiKhoan), BorderLayout.CENTER);

        lblCount = new JLabel("Đang tải dữ liệu...");
        lblCount.setForeground(new Color(100, 116, 139));
        add(lblCount, BorderLayout.SOUTH);

        btnThem.addActionListener(e -> moForm(false));
        btnSua.addActionListener(e -> moForm(true));
        btnXoa.addActionListener(e -> xoaTaiKhoan());
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { locDuLieu(); }
            public void removeUpdate(DocumentEvent e) { locDuLieu(); }
            public void changedUpdate(DocumentEvent e) { locDuLieu(); }
        });

        taiDuLieu();
    }

    private void taiDuLieu() {
        try {
            danhSachTaiKhoan = taiKhoanDAO.getAll();
            hienThiDanhSach(danhSachTaiKhoan);
            if (danhSachTaiKhoan.isEmpty()) {
                lblCount.setText("Hiển thị 0 bản ghi – kiểm tra dữ liệu bảng TaiKhoanNhanVien trong SQL Server");
            }
        } catch (Exception e) {
            lblCount.setText("Không tải được dữ liệu tài khoản");
        }
    }

    private void hienThiDanhSach(List<TaiKhoanNhanVien> ds) {
        modelTaiKhoan.setRowCount(0);
        for (TaiKhoanNhanVien tk : ds) {
            String maNV = (tk.getMaNhanVien() != null) ? tk.getMaNhanVien().getMaNhanVien() : "";
            String tenNV = (tk.getMaNhanVien() != null) ? tk.getMaNhanVien().getHoTenNhanVien() : "";
            modelTaiKhoan.addRow(new Object[]{
                    tk.getMaTaiKhoan(),
                    tk.getTenDangNhap(),
                    maNV,
                    tenNV,
                    tk.getLoaiQuyen(),
                    tk.isTrangThai() ? "Hoạt động" : "Vô hiệu hóa"
            });
        }
        lblCount.setText("Hiển thị " + ds.size() + " bản ghi");
    }

    private void locDuLieu() {
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
        List<TaiKhoanNhanVien> ketQua = new ArrayList<>();
        for (TaiKhoanNhanVien tk : danhSachTaiKhoan) {
            String tenNV = (tk.getMaNhanVien() != null) ? tk.getMaNhanVien().getHoTenNhanVien() : "";
            String thongTin = (tk.getMaTaiKhoan() + " " + tk.getTenDangNhap() + " " + tenNV).toLowerCase();
            if (thongTin.contains(tuKhoa)) ketQua.add(tk);
        }
        hienThiDanhSach(ketQua);
    }

    private TaiKhoanNhanVien taiKhoanDangChon() {
        int row = tblTaiKhoan.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tài khoản.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        String ma = String.valueOf(modelTaiKhoan.getValueAt(tblTaiKhoan.convertRowIndexToModel(row), 0));
        for (TaiKhoanNhanVien tk : danhSachTaiKhoan) if (tk.getMaTaiKhoan().equals(ma)) return tk;
        return null;
    }

    private void xoaTaiKhoan() {
        TaiKhoanNhanVien tk = taiKhoanDangChon();
        if (tk == null) return;
        int chon = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tài khoản " + tk.getTenDangNhap() + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (chon == JOptionPane.YES_OPTION) {
            if (taiKhoanDAO.delete(tk)) {
                JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công.");
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void moForm(boolean sua) {
        final TaiKhoanNhanVien tkCu = sua ? taiKhoanDangChon() : null;
        if (sua && tkCu == null) return;

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), sua ? "Cập nhật tài khoản" : "Thêm tài khoản mới", Dialog.ModalityType.APPLICATION_MODAL);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(18, 22, 10, 22));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.anchor = GridBagConstraints.WEST;

        JTextField txtMa = new JTextField(sua ? tkCu.getMaTaiKhoan() : taiKhoanDAO.taoMaMoi(), 22);
        txtMa.setEditable(false);
        JTextField txtTenDangNhap = new JTextField(sua ? tkCu.getTenDangNhap() : "", 22);
        JPasswordField txtMatKhau = new JPasswordField(sua ? tkCu.getMatKhau() : "", 22);

        List<NhanVien> listNV = nhanVienDAO.getAll();
        JComboBox<NhanVien> cboNhanVien = new JComboBox<>(listNV.toArray(new NhanVien[0]));
        cboNhanVien.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof NhanVien) {
                    NhanVien nv = (NhanVien) value;
                    setText(nv.getMaNhanVien() + " - " + nv.getHoTenNhanVien());
                }
                return this;
            }
        });
        if (sua && tkCu.getMaNhanVien() != null) {
            for (NhanVien nv : listNV) {
                if (nv.getMaNhanVien().equals(tkCu.getMaNhanVien().getMaNhanVien())) {
                    cboNhanVien.setSelectedItem(nv);
                    break;
                }
            }
        }

        JComboBox<String> cboQuyen = new JComboBox<>(new String[]{"QUAN_LY", "BAN_HANG", "KE_TOAN", "KHO", "CSKH"});
        cboQuyen.setSelectedItem(sua ? tkCu.getLoaiQuyen() : "BAN_HANG");

        JComboBox<String> cboTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Vô hiệu hóa"});
        cboTrangThai.setSelectedItem(sua && tkCu.isTrangThai() ? "Hoạt động" : "Vô hiệu hóa");

        themDong(form, g, 0, "Mã tài khoản", txtMa);
        themDong(form, g, 1, "Tên đăng nhập *", txtTenDangNhap);
        themDong(form, g, 2, "Mật khẩu *", txtMatKhau);
        themDong(form, g, 3, "Nhân viên sở hữu *", cboNhanVien);
        themDong(form, g, 4, "Loại quyền *", cboQuyen);
        themDong(form, g, 5, "Trạng thái", cboTrangThai);

        JLabel lblLoi = new JLabel(" ");
        lblLoi.setForeground(Color.RED);
        JButton btnLuu = taoNut("Lưu", new Color(14, 98, 195), Color.WHITE);
        JButton btnHuy = taoNut("Hủy", Color.WHITE, new Color(14, 98, 195));
        btnHuy.addActionListener(e -> dialog.dispose());

        btnLuu.addActionListener(e -> {
            try {
                String tenDN = txtTenDangNhap.getText().trim();
                String matKhau = new String(txtMatKhau.getPassword()).trim();
                NhanVien nv = (NhanVien) cboNhanVien.getSelectedItem();

                if (tenDN.isEmpty() || matKhau.isEmpty() || nv == null) {
                    throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin bắt buộc.");
                }

                TaiKhoanNhanVien tk = new TaiKhoanNhanVien(
                        txtMa.getText().trim(),
                        tenDN,
                        nv,
                        matKhau,
                        (String) cboQuyen.getSelectedItem(),
                        "Hoạt động".equals(cboTrangThai.getSelectedItem())
                );

                boolean thanhCong = sua ? taiKhoanDAO.update(tk) : taiKhoanDAO.insert(tk);
                if (thanhCong) {
                    JOptionPane.showMessageDialog(dialog, sua ? "Cập nhật thành công." : "Thêm mới thành công.");
                    dialog.dispose();
                    taiDuLieu();
                } else {
                    lblLoi.setText("Lưu thất bại. Tên đăng nhập có thể đã tồn tại.");
                }
            } catch (IllegalArgumentException ex) {
                lblLoi.setText(ex.getMessage());
            }
        });

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(0, 22, 16, 22));
        JPanel nut = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nut.setBackground(Color.WHITE);
        nut.add(btnHuy);
        nut.add(btnLuu);
        bottom.add(lblLoi, BorderLayout.NORTH);
        bottom.add(nut, BorderLayout.SOUTH);
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(bottom, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void themDong(JPanel p, GridBagConstraints g, int row, String label, JComponent c) {
        g.gridy = row; g.gridx = 0; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setPreferredSize(new Dimension(170, 28));
        p.add(lbl, g);
        g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        c.setFont(new Font("Arial", Font.PLAIN, 13));
        c.setPreferredSize(new Dimension(280, 32));
        p.add(c, g);
    }

    private JButton taoNut(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(fg);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setFocusPainted(false);
        return b;
    }
}
