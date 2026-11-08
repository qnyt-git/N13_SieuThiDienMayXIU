package GUI;

import DAO.NhaCungCapDAO;
import DAO.SanPhamDAO;
import ENTITY.NhaCungCap;
import ENTITY.SanPham;
import ENUM.TrangThaiSanPham;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SanPhamGUI extends JPanel {
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private final NhaCungCapDAO nccDAO = new NhaCungCapDAO();
    
    private final DefaultTableModel modelSanPham;
    private final JTable tblSanPham;
    private final JTextField txtTimKiem;
    private final JLabel lblCount;
    private List<SanPham> danhSachSanPham = new ArrayList<>();

    public SanPhamGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        JLabel lblTieuDe = new JLabel("Quản lý sản phẩm thiết bị");
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
        JButton btnThem = taoNut("+ Thêm sản phẩm", new Color(14, 98, 195), Color.WHITE);
        buttons.add(btnSua);
        buttons.add(btnXoa);
        buttons.add(btnThem);
        bar.add(left, BorderLayout.WEST);
        bar.add(buttons, BorderLayout.EAST);
        top.add(bar, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        modelSanPham = new DefaultTableModel(new Object[]{
                "Mã SP", "Tên sản phẩm", "Nhà cung cấp", "Nơi sản xuất", "Giá bán (VNĐ)", "Tồn kho", "Bảo hành", "Trạng thái"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblSanPham = new JTable(modelSanPham);
        tblSanPham.setRowHeight(28);
        tblSanPham.setFont(new Font("Arial", Font.PLAIN, 13));
        tblSanPham.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblSanPham.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tblSanPham), BorderLayout.CENTER);

        lblCount = new JLabel("Đang tải dữ liệu...");
        lblCount.setForeground(new Color(100, 116, 139));
        add(lblCount, BorderLayout.SOUTH);

        btnThem.addActionListener(e -> moForm(false));
        btnSua.addActionListener(e -> moForm(true));
        btnXoa.addActionListener(e -> xoaSanPham());
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { locDuLieu(); }
            public void removeUpdate(DocumentEvent e) { locDuLieu(); }
            public void changedUpdate(DocumentEvent e) { locDuLieu(); }
        });

        taiDuLieu();
    }

    private void taiDuLieu() {
        try {
            danhSachSanPham = sanPhamDAO.getAll();
            hienThiDanhSach(danhSachSanPham);
            if (danhSachSanPham.isEmpty()) {
                lblCount.setText("Hiển thị 0 bản ghi – kiểm tra dữ liệu bảng SanPham trong SQL Server");
            }
        } catch (Exception e) {
            lblCount.setText("Không tải được dữ liệu sản phẩm");
        }
    }

    private void hienThiDanhSach(List<SanPham> ds) {
        modelSanPham.setRowCount(0);
        for (SanPham sp : ds) {
            String tenNCC = (sp.getMaNCC() != null) ? sp.getMaNCC().getTenNCC() : "";
            modelSanPham.addRow(new Object[]{
                    sp.getMaSanPham(),
                    sp.getTenSanPham(),
                    tenNCC,
                    sp.getNoiSanXuat(),
                    String.format("%,.0f", sp.getGiaBan()),
                    sp.getSoLuongTon(),
                    sp.getThoiHanBaoHanh(),
                    sp.getTrangThai() != null ? sp.getTrangThai().getMoTa() : ""
            });
        }
        lblCount.setText("Hiển thị " + ds.size() + " bản ghi");
    }

    private void locDuLieu() {
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
        List<SanPham> ketQua = new ArrayList<>();
        for (SanPham sp : danhSachSanPham) {
            String tenNCC = (sp.getMaNCC() != null) ? sp.getMaNCC().getTenNCC() : "";
            String noiSX = sp.getNoiSanXuat() != null ? sp.getNoiSanXuat() : "";
            String thongTin = (sp.getMaSanPham() + " " + sp.getTenSanPham() + " " + tenNCC + " " + noiSX).toLowerCase();
            if (thongTin.contains(tuKhoa)) ketQua.add(sp);
        }
        hienThiDanhSach(ketQua);
    }

    private SanPham sanPhamDangChon() {
        int row = tblSanPham.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        String ma = String.valueOf(modelSanPham.getValueAt(tblSanPham.convertRowIndexToModel(row), 0));
        for (SanPham sp : danhSachSanPham) if (sp.getMaSanPham().equals(ma)) return sp;
        return null;
    }

    private void xoaSanPham() {
        SanPham sp = sanPhamDangChon();
        if (sp == null) return;
        int chon = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sản phẩm " + sp.getTenSanPham() + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (chon == JOptionPane.YES_OPTION) {
            if (sanPhamDAO.delete(sp)) {
                JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công.");
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void moForm(boolean sua) {
        final SanPham spCu = sua ? sanPhamDangChon() : null;
        if (sua && spCu == null) return;

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), sua ? "Cập nhật sản phẩm" : "Thêm sản phẩm mới", Dialog.ModalityType.APPLICATION_MODAL);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(18, 22, 10, 22));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.anchor = GridBagConstraints.WEST;

        JTextField txtMa = new JTextField(sua ? spCu.getMaSanPham() : sanPhamDAO.taoMaMoi(), 22);
        txtMa.setEditable(false);
        JTextField txtTen = new JTextField(sua ? spCu.getTenSanPham() : "", 22);

        List<NhaCungCap> listNCC = nccDAO.getAll();
        JComboBox<NhaCungCap> cboNCC = new JComboBox<>(listNCC.toArray(new NhaCungCap[0]));
        cboNCC.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof NhaCungCap) {
                    NhaCungCap ncc = (NhaCungCap) value;
                    setText(ncc.getMaNCC() + " - " + ncc.getTenNCC());
                }
                return this;
            }
        });
        if (sua && spCu.getMaNCC() != null) {
            for (NhaCungCap ncc : listNCC) {
                if (ncc.getMaNCC().equals(spCu.getMaNCC().getMaNCC())) {
                    cboNCC.setSelectedItem(ncc);
                    break;
                }
            }
        }

        JTextField txtNoiSX = new JTextField(sua ? spCu.getNoiSanXuat() : "", 22);
        JTextField txtGia = new JTextField(sua ? String.valueOf(spCu.getGiaBan()) : "", 22);
        JTextField txtTon = new JTextField(sua ? String.valueOf(spCu.getSoLuongTon()) : "0", 22);
        JTextField txtBaoHanh = new JTextField(sua ? spCu.getThoiHanBaoHanh() : "12 tháng", 22);
        JTextField txtMoTa = new JTextField(sua ? spCu.getMoTaSanPham() : "", 22);
        JComboBox<TrangThaiSanPham> cboTrangThai = new JComboBox<>(TrangThaiSanPham.values());
        if (sua && spCu.getTrangThai() != null) cboTrangThai.setSelectedItem(spCu.getTrangThai());

        themDong(form, g, 0, "Mã sản phẩm", txtMa);
        themDong(form, g, 1, "Tên sản phẩm *", txtTen);
        themDong(form, g, 2, "Nhà cung cấp *", cboNCC);
        themDong(form, g, 3, "Nơi sản xuất", txtNoiSX);
        themDong(form, g, 4, "Giá bán (VNĐ) *", txtGia);
        themDong(form, g, 5, "Số lượng tồn *", txtTon);
        themDong(form, g, 6, "Thời hạn bảo hành *", txtBaoHanh);
        themDong(form, g, 7, "Mô tả sản phẩm", txtMoTa);
        themDong(form, g, 8, "Trạng thái", cboTrangThai);

        JLabel lblLoi = new JLabel(" ");
        lblLoi.setForeground(Color.RED);
        JButton btnLuu = taoNut("Lưu", new Color(14, 98, 195), Color.WHITE);
        JButton btnHuy = taoNut("Hủy", Color.WHITE, new Color(14, 98, 195));
        btnHuy.addActionListener(e -> dialog.dispose());

        btnLuu.addActionListener(e -> {
            try {
                String ten = txtTen.getText().trim();
                if (ten.isEmpty() || txtGia.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("Vui lòng nhập tên và giá bán sản phẩm.");
                }
                double gia = Double.parseDouble(txtGia.getText().trim());
                int ton = Integer.parseInt(txtTon.getText().trim());
                NhaCungCap ncc = (NhaCungCap) cboNCC.getSelectedItem();

                SanPham sp = new SanPham(
                        txtMa.getText().trim(),
                        ten,
                        ncc,
                        gia,
                        ton,
                        txtBaoHanh.getText().trim(),
                        txtNoiSX.getText().trim(),
                        txtMoTa.getText().trim(),
                        (TrangThaiSanPham) cboTrangThai.getSelectedItem()
                );

                boolean thanhCong = sua ? sanPhamDAO.update(sp) : sanPhamDAO.insert(sp);
                if (thanhCong) {
                    JOptionPane.showMessageDialog(dialog, sua ? "Cập nhật thành công." : "Thêm mới thành công.");
                    dialog.dispose();
                    taiDuLieu();
                } else {
                    lblLoi.setText("Lưu thất bại.");
                }
            } catch (NumberFormatException ex) {
                lblLoi.setText("Giá bán và tồn kho phải là số hợp lệ.");
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