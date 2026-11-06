package GUI;

import DAO.NhanVienDAO;
import ENTITY.NhanVien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class NhanVienGUI extends JPanel {
    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private final DefaultTableModel modelNhanVien;
    private final JTable tblNhanVien;
    private final JTextField txtTimKiem;
    private final JLabel lblCount;
    private List<NhanVien> danhSachNhanVien = new ArrayList<>();

    public NhanVienGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        JLabel lblTieuDe = new JLabel("Quản lý nhân viên");
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
        JButton btnThem = taoNut("+ Thêm mới", new Color(14, 98, 195), Color.WHITE);
        buttons.add(btnSua);
        buttons.add(btnXoa);
        buttons.add(btnThem);
        bar.add(left, BorderLayout.WEST);
        bar.add(buttons, BorderLayout.EAST);
        top.add(bar, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        modelNhanVien = new DefaultTableModel(new Object[]{
                "Mã NV", "Họ tên", "Giới tính", "Chức vụ", "Số điện thoại",
                "Ngày sinh", "Trình độ học vấn", "Hệ số lương", "Lương cơ bản", "Trạng thái"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblNhanVien = new JTable(modelNhanVien);
        tblNhanVien.setRowHeight(28);
        tblNhanVien.setFont(new Font("Arial", Font.PLAIN, 13));
        tblNhanVien.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblNhanVien.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tblNhanVien), BorderLayout.CENTER);

        lblCount = new JLabel("Đang tải dữ liệu...");
        lblCount.setForeground(new Color(100, 116, 139));
        add(lblCount, BorderLayout.SOUTH);

        btnThem.addActionListener(e -> moForm(false));
        btnSua.addActionListener(e -> moForm(true));
        btnXoa.addActionListener(e -> xoaNhanVien());
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { locDuLieu(); }
            public void removeUpdate(DocumentEvent e) { locDuLieu(); }
            public void changedUpdate(DocumentEvent e) { locDuLieu(); }
        });

        taiDuLieu();
    }

    private void taiDuLieu() {
        try {
            danhSachNhanVien = nhanVienDAO.getAll();
            hienThiDanhSach(danhSachNhanVien);
            if (danhSachNhanVien.isEmpty()) {
                lblCount.setText("Hiển thị 0 bản ghi – kiểm tra dữ liệu bảng NhanVien trong SQL Server");
            }
        } catch (Exception e) {
            lblCount.setText("Không tải được dữ liệu nhân viên");
            JOptionPane.showMessageDialog(this,
                    "Không thể lấy dữ liệu nhân viên từ SQL Server.\n" + e.getMessage(),
                    "Lỗi tải dữ liệu", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hienThiDanhSach(List<NhanVien> ds) {
        modelNhanVien.setRowCount(0);
        for (NhanVien nv : ds) {
            modelNhanVien.addRow(new Object[]{
                    nv.getMaNhanVien(), nv.getHoTenNhanVien(), nv.isGioiTinh() ? "Nam" : "Nữ",
                    nv.getChucVu(), nv.getSoDienThoai(), nv.getNgaySinh(), nv.getTrinhDoHocVan(),
                    nv.getHeSoLuong(), nv.getLuongCoBan(), nv.isTrangThai() ? "Đang làm" : "Nghỉ việc"
            });
        }
        lblCount.setText("Hiển thị " + ds.size() + " bản ghi");
    }

    private void locDuLieu() {
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
        List<NhanVien> ketQua = new ArrayList<>();
        for (NhanVien nv : danhSachNhanVien) {
            String thongTin = (nv.getMaNhanVien() + " " + nv.getHoTenNhanVien() + " "
                    + nv.getSoDienThoai() + " " + nv.getChucVu()).toLowerCase();
            if (thongTin.contains(tuKhoa)) ketQua.add(nv);
        }
        hienThiDanhSach(ketQua);
    }

    private NhanVien nhanVienDangChon() {
        int row = tblNhanVien.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        String ma = String.valueOf(modelNhanVien.getValueAt(tblNhanVien.convertRowIndexToModel(row), 0));
        for (NhanVien nv : danhSachNhanVien) if (nv.getMaNhanVien().equals(ma)) return nv;
        return null;
    }

    private void xoaNhanVien() {
        NhanVien nv = nhanVienDangChon();
        if (nv == null) return;
        int chon = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa nhân viên " + nv.getHoTenNhanVien() + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (chon == JOptionPane.YES_OPTION) {
            if (nhanVienDAO.delete(nv)) {
                JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công.");
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Xóa không thành công. Nhân viên có thể đang được tham chiếu ở bảng khác.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void moForm(boolean sua) {
        final NhanVien nvCu = sua ? nhanVienDangChon() : null;
        if (sua && nvCu == null) return;

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                sua ? "Cập nhật nhân viên" : "Thêm nhân viên mới", Dialog.ModalityType.APPLICATION_MODAL);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(18, 22, 10, 22));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 4, 5, 4);
        g.anchor = GridBagConstraints.WEST;

        JTextField txtMa = new JTextField(sua ? nvCu.getMaNhanVien() : nhanVienDAO.taoMaMoi(), 22);
        txtMa.setEditable(false);
        JTextField txtHoTen = new JTextField(sua ? nvCu.getHoTenNhanVien() : "", 22);
        JComboBox<String> cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cboGioiTinh.setSelectedItem(sua && !nvCu.isGioiTinh() ? "Nữ" : "Nam");
        JComboBox<String> cboChucVu = new JComboBox<>(new String[]{"QUAN_LY", "BAN_HANG", "THU_NGAN", "CSKH", "KHO", "KE_TOAN"});
        if (sua && nvCu.getChucVu() != null) cboChucVu.setSelectedItem(nvCu.getChucVu());
        JTextField txtSdt = new JTextField(sua ? nvCu.getSoDienThoai() : "", 22);
        JTextField txtNgaySinh = new JTextField(sua && nvCu.getNgaySinh() != null ? nvCu.getNgaySinh().toString() : "", 22);
        JTextField txtTrinhDo = new JTextField(sua ? nvCu.getTrinhDoHocVan() : "", 22);
        JTextField txtHeSo = new JTextField(sua ? String.valueOf(nvCu.getHeSoLuong()) : "", 22);
        JTextField txtLuong = new JTextField(sua ? String.valueOf(nvCu.getLuongCoBan()) : "", 22);
        JComboBox<String> cboTrangThai = new JComboBox<>(new String[]{"Đang làm", "Nghỉ việc"});
        cboTrangThai.setSelectedItem(sua && !nvCu.isTrangThai() ? "Nghỉ việc" : "Đang làm");

        themDong(form, g, 0, "Mã nhân viên", txtMa);
        themDong(form, g, 1, "Họ tên *", txtHoTen);
        themDong(form, g, 2, "Giới tính *", cboGioiTinh);
        themDong(form, g, 3, "Chức vụ *", cboChucVu);
        themDong(form, g, 4, "Số điện thoại *", txtSdt);
        themDong(form, g, 5, "Ngày sinh (yyyy-MM-dd) *", txtNgaySinh);
        themDong(form, g, 6, "Trình độ học vấn", txtTrinhDo);
        themDong(form, g, 7, "Hệ số lương *", txtHeSo);
        themDong(form, g, 8, "Lương cơ bản *", txtLuong);
        themDong(form, g, 9, "Trạng thái", cboTrangThai);

        JLabel lblLoi = new JLabel(" ");
        lblLoi.setForeground(Color.RED);
        JButton btnLuu = taoNut("Lưu", new Color(14, 98, 195), Color.WHITE);
        JButton btnHuy = taoNut("Hủy", Color.WHITE, new Color(14, 98, 195));
        btnHuy.addActionListener(e -> dialog.dispose());
        btnLuu.addActionListener(e -> {
            try {
                String hoTen = txtHoTen.getText().trim();
                String sdt = txtSdt.getText().trim();
                if (hoTen.isEmpty() || sdt.isEmpty() || txtNgaySinh.getText().trim().isEmpty()
                        || txtHeSo.getText().trim().isEmpty() || txtLuong.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("Vui lòng nhập đầy đủ các trường bắt buộc.");
                }
                if (!sdt.matches("0[0-9]{9}")) throw new IllegalArgumentException("Số điện thoại phải gồm 10 chữ số và bắt đầu bằng số 0.");
                LocalDate ngaySinh = LocalDate.parse(txtNgaySinh.getText().trim());
                if (ngaySinh.isAfter(LocalDate.now()) || ngaySinh.plusYears(18).isAfter(LocalDate.now()))
                    throw new IllegalArgumentException("Nhân viên phải đủ 18 tuổi và ngày sinh không được ở tương lai.");
                float heSo = Float.parseFloat(txtHeSo.getText().trim());
                int luong = Integer.parseInt(txtLuong.getText().trim());
                if (heSo <= 0 || luong <= 0) throw new IllegalArgumentException("Hệ số lương và lương cơ bản phải lớn hơn 0.");

                NhanVien nv = new NhanVien(txtMa.getText().trim(), hoTen,
                        "Nam".equals(cboGioiTinh.getSelectedItem()), sdt, ngaySinh,
                        txtTrinhDo.getText().trim(), String.valueOf(cboChucVu.getSelectedItem()),
                        heSo, luong, "Đang làm".equals(cboTrangThai.getSelectedItem()));
                boolean thanhCong = sua ? nhanVienDAO.update(nv) : nhanVienDAO.insert(nv);
                if (thanhCong) {
                    JOptionPane.showMessageDialog(dialog, sua ? "Cập nhật nhân viên thành công." : "Thêm nhân viên thành công.");
                    dialog.dispose();
                    taiDuLieu();
                } else {
                    lblLoi.setText("Không lưu được dữ liệu. Kiểm tra Console hoặc ràng buộc SQL.");
                }
            } catch (DateTimeParseException ex) {
                lblLoi.setText("Ngày sinh phải đúng định dạng yyyy-MM-dd.");
            } catch (NumberFormatException ex) {
                lblLoi.setText("Hệ số lương và lương cơ bản phải là số hợp lệ.");
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
        dialog.getRootPane().setDefaultButton(btnLuu);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(500, 540));
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void themDong(JPanel p, GridBagConstraints g, int row, String label, JComponent c) {
        g.gridy = row; g.gridx = 0; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setPreferredSize(new Dimension(175, 28));
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
