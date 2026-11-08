package GUI;

import DAO.NhaCungCapDAO;
import ENTITY.NhaCungCap;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NhaCungCapGUI extends JPanel {
    private final NhaCungCapDAO nccDAO = new NhaCungCapDAO();
    private final DefaultTableModel modelNCC;
    private final JTable tblNCC;
    private final JTextField txtTimKiem;
    private final JLabel lblCount;
    private List<NhaCungCap> danhSachNCC = new ArrayList<>();

    public NhaCungCapGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        JLabel lblTieuDe = new JLabel("Quản lý nhà cung cấp");
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

        modelNCC = new DefaultTableModel(new Object[]{
                "Mã NCC", "Tên công ty", "Thương hiệu", "Số điện thoại", "Email", "Địa chỉ", "Trạng thái"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblNCC = new JTable(modelNCC);
        tblNCC.setRowHeight(28);
        tblNCC.setFont(new Font("Arial", Font.PLAIN, 13));
        tblNCC.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblNCC.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tblNCC), BorderLayout.CENTER);

        lblCount = new JLabel("Đang tải dữ liệu...");
        lblCount.setForeground(new Color(100, 116, 139));
        add(lblCount, BorderLayout.SOUTH);

        btnThem.addActionListener(e -> moForm(false));
        btnSua.addActionListener(e -> moForm(true));
        btnXoa.addActionListener(e -> xoaNCC());
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { locDuLieu(); }
            public void removeUpdate(DocumentEvent e) { locDuLieu(); }
            public void changedUpdate(DocumentEvent e) { locDuLieu(); }
        });

        taiDuLieu();
    }

    private void taiDuLieu() {
        try {
            danhSachNCC = nccDAO.getAll();
            hienThiDanhSach(danhSachNCC);
            if (danhSachNCC.isEmpty()) {
                lblCount.setText("Hiển thị 0 bản ghi – kiểm tra dữ liệu bảng NhaCungCap trong SQL Server");
            }
        } catch (Exception e) {
            lblCount.setText("Không tải được dữ liệu nhà cung cấp");
        }
    }

    private void hienThiDanhSach(List<NhaCungCap> ds) {
        modelNCC.setRowCount(0);
        for (NhaCungCap ncc : ds) {
            modelNCC.addRow(new Object[]{
                    ncc.getMaNCC(),
                    ncc.getTenNCC(),
                    ncc.getTenThuongHieu(),
                    ncc.getSoDienThoai(),
                    ncc.getEmail(),
                    ncc.getDiaChi(),
                    nccDAO.trangThaiText(ncc)
            });
        }
        lblCount.setText("Hiển thị " + ds.size() + " bản ghi");
    }

    private void locDuLieu() {
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
        List<NhaCungCap> ketQua = new ArrayList<>();
        for (NhaCungCap ncc : danhSachNCC) {
            String thongTin = (ncc.getMaNCC() + " " + ncc.getTenNCC() + " " + ncc.getTenThuongHieu() + " " + ncc.getSoDienThoai()).toLowerCase();
            if (thongTin.contains(tuKhoa)) ketQua.add(ncc);
        }
        hienThiDanhSach(ketQua);
    }

    private NhaCungCap nccDangChon() {
        int row = tblNCC.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhà cung cấp.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        String ma = String.valueOf(modelNCC.getValueAt(tblNCC.convertRowIndexToModel(row), 0));
        for (NhaCungCap ncc : danhSachNCC) if (ncc.getMaNCC().equals(ma)) return ncc;
        return null;
    }

    private void xoaNCC() {
        NhaCungCap ncc = nccDangChon();
        if (ncc == null) return;
        int chon = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhà cung cấp " + ncc.getTenNCC() + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (chon == JOptionPane.YES_OPTION) {
            if (nccDAO.delete(ncc)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công.");
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void moForm(boolean sua) {
        final NhaCungCap nccCu = sua ? nccDangChon() : null;
        if (sua && nccCu == null) return;

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), sua ? "Cập nhật nhà cung cấp" : "Thêm nhà cung cấp mới", Dialog.ModalityType.APPLICATION_MODAL);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(18, 22, 10, 22));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.anchor = GridBagConstraints.WEST;

        JTextField txtMa = new JTextField(sua ? nccCu.getMaNCC() : nccDAO.taoMaMoi(), 22);
        txtMa.setEditable(false);
        JTextField txtTen = new JTextField(sua ? nccCu.getTenNCC() : "", 22);
        JTextField txtThuongHieu = new JTextField(sua ? nccCu.getTenThuongHieu() : "", 22);
        JTextField txtSdt = new JTextField(sua ? nccCu.getSoDienThoai() : "", 22);
        JTextField txtEmail = new JTextField(sua ? nccCu.getEmail() : "", 22);
        JTextField txtDiaChi = new JTextField(sua ? nccCu.getDiaChi() : "", 22);
        JComboBox<String> cboTrangThai = new JComboBox<>(new String[]{"Hợp tác", "Tạm ngừng"});
        cboTrangThai.setSelectedItem(sua && nccCu.isTrangThai() ? "Hợp tác" : "Tạm ngừng");

        themDong(form, g, 0, "Mã NCC", txtMa);
        themDong(form, g, 1, "Tên công ty *", txtTen);
        themDong(form, g, 2, "Thương hiệu *", txtThuongHieu);
        themDong(form, g, 3, "Số điện thoại *", txtSdt);
        themDong(form, g, 4, "Email *", txtEmail);
        themDong(form, g, 5, "Địa chỉ *", txtDiaChi);
        themDong(form, g, 6, "Trạng thái", cboTrangThai);

        JLabel lblLoi = new JLabel(" ");
        lblLoi.setForeground(Color.RED);
        JButton btnLuu = taoNut("Lưu", new Color(14, 98, 195), Color.WHITE);
        JButton btnHuy = taoNut("Hủy", Color.WHITE, new Color(14, 98, 195));
        btnHuy.addActionListener(e -> dialog.dispose());

        btnLuu.addActionListener(e -> {
            try {
                String ten = txtTen.getText().trim();
                String thuongHieu = txtThuongHieu.getText().trim();
                String sdt = txtSdt.getText().trim();
                String email = txtEmail.getText().trim();
                String diaChi = txtDiaChi.getText().trim();

                if (ten.isEmpty() || thuongHieu.isEmpty() || sdt.isEmpty() || email.isEmpty() || diaChi.isEmpty()) {
                    throw new IllegalArgumentException("Vui lòng điền đầy đủ tất cả các trường bắt buộc.");
                }
                if (!sdt.matches("0[0-9]{9}")) {
                    throw new IllegalArgumentException("Số điện thoại phải gồm đúng 10 chữ số và bắt đầu bằng số 0.");
                }
                if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                    throw new IllegalArgumentException("Email không đúng định dạng.");
                }

                NhaCungCap ncc = new NhaCungCap(
                        txtMa.getText().trim(),
                        ten,
                        thuongHieu,
                        diaChi,
                        sdt,
                        email,
                        "Hợp tác".equals(cboTrangThai.getSelectedItem())
                );

                boolean thanhCong = sua ? nccDAO.update(ncc) : nccDAO.insert(ncc);
                if (thanhCong) {
                    JOptionPane.showMessageDialog(dialog, sua ? "Cập nhật thành công." : "Thêm mới thành công.");
                    dialog.dispose();
                    taiDuLieu();
                } else {
                    lblLoi.setText("Lưu thất bại. Kiểm tra lại dữ liệu.");
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
