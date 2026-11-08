package GUI;

import BUS.KhachHangBUS; // Hoặc thay bằng KhachHangDAO nếu bạn gọi trực tiếp DAO
import ENTITY.KhachHang;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangGUI extends JPanel {
    private final KhachHangBUS khachHangBUS = new KhachHangBUS();
    private final DefaultTableModel modelKhachHang;
    private final JTable tblKhachHang;
    private final JTextField txtTimKiem;
    private final JLabel lblCount;
    private List<KhachHang> danhSachKhachHang = new ArrayList<>();

    public KhachHangGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        JLabel lblTieuDe = new JLabel("Quản lý khách hàng");
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

        // Khớp cấu trúc các cột với thực thể KhachHang mới
        modelKhachHang = new DefaultTableModel(new Object[]{
                "Mã KH", "Họ tên khách hàng", "Giới tính", "Số điện thoại", "Điểm tích lũy", "Loại KH"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblKhachHang = new JTable(modelKhachHang);
        tblKhachHang.setRowHeight(28);
        tblKhachHang.setFont(new Font("Arial", Font.PLAIN, 13));
        tblKhachHang.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblKhachHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tblKhachHang), BorderLayout.CENTER);

        lblCount = new JLabel("Đang tải dữ liệu...");
        lblCount.setForeground(new Color(100, 116, 139));
        add(lblCount, BorderLayout.SOUTH);

        btnThem.addActionListener(e -> moForm(false));
        btnSua.addActionListener(e -> moForm(true));
        btnXoa.addActionListener(e -> xoaKhachHang());
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { locDuLieu(); }
            public void removeUpdate(DocumentEvent e) { locDuLieu(); }
            public void changedUpdate(DocumentEvent e) { locDuLieu(); }
        });

        taiDuLieu();
    }

    private void taiDuLieu() {
        try {
            danhSachKhachHang = khachHangBUS.getAll();
            hienThiDanhSach(danhSachKhachHang);
            if (danhSachKhachHang.isEmpty()) {
                lblCount.setText("Hiển thị 0 bản ghi – kiểm tra dữ liệu bảng KhachHang trong SQL Server");
            }
        } catch (Exception e) {
            lblCount.setText("Không tải được dữ liệu khách hàng");
            JOptionPane.showMessageDialog(this,
                    "Không thể lấy dữ liệu khách hàng từ SQL Server.\n" + e.getMessage(),
                    "Lỗi tải dữ liệu", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hienThiDanhSach(List<KhachHang> ds) {
        modelKhachHang.setRowCount(0);
        for (KhachHang kh : ds) {
            modelKhachHang.addRow(new Object[]{
                    kh.getMaKhachHang(),
                    kh.getHoTenKhachHang(),
                    kh.isGioiTinh() ? "Nam" : "Nữ",
                    kh.getSoDienThoai(),
                    kh.getDiemTichLuy(),
                    kh.getLoaiKH()
            });
        }
        lblCount.setText("Hiển thị " + ds.size() + " bản ghi");
    }

    private void locDuLieu() {
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
        List<KhachHang> ketQua = new ArrayList<>();
        for (KhachHang kh : danhSachKhachHang) {
            String thongTin = (kh.getMaKhachHang() + " " + kh.getHoTenKhachHang() + " " 
                    + kh.getSoDienThoai() + " " + kh.getLoaiKH()).toLowerCase();
            if (thongTin.contains(tuKhoa)) ketQua.add(kh);
        }
        hienThiDanhSach(ketQua);
    }

    private KhachHang khachHangDangChon() {
        int row = tblKhachHang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        String ma = String.valueOf(modelKhachHang.getValueAt(tblKhachHang.convertRowIndexToModel(row), 0));
        for (KhachHang kh : danhSachKhachHang) if (kh.getMaKhachHang().equals(ma)) return kh;
        return null;
    }

    private void xoaKhachHang() {
        KhachHang kh = khachHangDangChon();
        if (kh == null) return;
        int chon = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa khách hàng " + kh.getHoTenKhachHang() + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (chon == JOptionPane.YES_OPTION) {
            if (khachHangBUS.delete(kh)) {
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công.");
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Xóa không thành công. Khách hàng có thể đang có hóa đơn liên quan.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void moForm(boolean sua) {
        final KhachHang khCu = sua ? khachHangDangChon() : null;
        if (sua && khCu == null) return;

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                sua ? "Cập nhật khách hàng" : "Thêm khách hàng mới", Dialog.ModalityType.APPLICATION_MODAL);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(18, 22, 10, 22));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.anchor = GridBagConstraints.WEST;

        JTextField txtMa = new JTextField(sua ? khCu.getMaKhachHang() : khachHangBUS.taoMaMoi(), 22);
        txtMa.setEditable(false);
        JTextField txtHoTen = new JTextField(sua ? khCu.getHoTenKhachHang() : "", 22);
        JComboBox<String> cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cboGioiTinh.setSelectedItem(sua && !khCu.isGioiTinh() ? "Nữ" : "Nam");
        
        JTextField txtSdt = new JTextField(sua ? khCu.getSoDienThoai() : "", 22);
        JTextField txtDiem = new JTextField(sua ? String.valueOf(khCu.getDiemTichLuy()) : "0", 22);
        if (sua) txtDiem.setEditable(false); // Thường điểm tích lũy sẽ tự động cộng qua hóa đơn
        
        JComboBox<String> cboLoaiKH = new JComboBox<>(new String[]{"Khách mới", "Khách VIP"});
        if (sua && khCu.getLoaiKH() != null) cboLoaiKH.setSelectedItem(khCu.getLoaiKH());

        themDong(form, g, 0, "Mã khách hàng", txtMa);
        themDong(form, g, 1, "Họ tên KH *", txtHoTen);
        themDong(form, g, 2, "Giới tính *", cboGioiTinh);
        themDong(form, g, 3, "Số điện thoại *", txtSdt);
        themDong(form, g, 4, "Điểm tích lũy", txtDiem);
        themDong(form, g, 5, "Loại khách hàng *", cboLoaiKH);

        JLabel lblLoi = new JLabel(" ");
        lblLoi.setForeground(Color.RED);
        JButton btnLuu = taoNut("Lưu", new Color(14, 98, 195), Color.WHITE);
        JButton btnHuy = taoNut("Hủy", Color.WHITE, new Color(14, 98, 195));
        btnHuy.addActionListener(e -> dialog.dispose());

        btnLuu.addActionListener(e -> {
            try {
                String hoTen = txtHoTen.getText().trim();
                String sdt = txtSdt.getText().trim();
                if (hoTen.isEmpty() || sdt.isEmpty()) {
                    throw new IllegalArgumentException("Vui lòng điền đầy đủ họ tên và số điện thoại.");
                }
                if (!hoTen.matches("[\\p{Lu}][\\p{L}]*([ \u0027-][\\p{Lu}][\\p{L}]*)*")) {
                    throw new IllegalArgumentException("Họ tên chỉ được chứa chữ và phải viết hoa chữ cái đầu mỗi từ.");
                }
                if (!sdt.matches("0[0-9]{9}")) {
                    throw new IllegalArgumentException("Số điện thoại phải gồm đúng 10 chữ số và bắt đầu bằng số 0.");
                }
                int diem = Integer.parseInt(txtDiem.getText().trim());

                KhachHang kh = new KhachHang(
                        txtMa.getText().trim(),
                        hoTen,
                        "Nam".equals(cboGioiTinh.getSelectedItem()),
                        sdt,
                        diem,
                        String.valueOf(cboLoaiKH.getSelectedItem())
                );

                boolean thanhCong = sua ? khachHangBUS.update(kh) : khachHangBUS.insert(kh);
                if (thanhCong) {
                    JOptionPane.showMessageDialog(dialog, sua ? "Cập nhật thành công." : "Thêm mới thành công.");
                    dialog.dispose();
                    taiDuLieu();
                } else {
                    lblLoi.setText("Lưu thất bại. Kiểm tra lại dữ liệu hoặc kết nối CSDL.");
                }
            } catch (NumberFormatException ex) {
                lblLoi.setText("Điểm tích lũy phải là số nguyên hợp lệ.");
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
        dialog.setMinimumSize(new Dimension(500, 380));
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void themDong(JPanel p, GridBagConstraints g, int row, String label, JComponent c) {
        g.gridy = row; g.gridx = 0; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setPreferredSize(new Dimension(150, 28));
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
