package GUI;

import DAO.CaLamViecDAO;
import DAO.NhanVienDAO;
import DAO.PhanCaDAO;
import ENTITY.CaLamViec;
import ENTITY.NhanVien;
import ENTITY.PhanCa;
import ENUM.TrangThaiPhanCa;

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

public class PhanCaGUI extends JPanel {
    private final PhanCaDAO phanCaDAO = new PhanCaDAO();
    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private final CaLamViecDAO caLamViecDAO = new CaLamViecDAO();
    
    private final DefaultTableModel modelPhanCa;
    private final JTable tblPhanCa;
    private final JTextField txtTimKiem;
    private final JLabel lblCount;
    private List<PhanCa> danhSachPhanCa = new ArrayList<>();

    public PhanCaGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        JLabel lblTieuDe = new JLabel("Quản lý phân ca làm việc");
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
        JButton btnThem = taoNut("+ Phân ca mới", new Color(14, 98, 195), Color.WHITE);
        buttons.add(btnSua);
        buttons.add(btnXoa);
        buttons.add(btnThem);
        bar.add(left, BorderLayout.WEST);
        bar.add(buttons, BorderLayout.EAST);
        top.add(bar, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        modelPhanCa = new DefaultTableModel(new Object[]{
                "Mã phân ca", "Mã nhân viên", "Họ tên nhân viên", "Ca làm", "Ngày làm", "Trạng thái"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblPhanCa = new JTable(modelPhanCa);
        tblPhanCa.setRowHeight(28);
        tblPhanCa.setFont(new Font("Arial", Font.PLAIN, 13));
        tblPhanCa.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblPhanCa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tblPhanCa), BorderLayout.CENTER);

        lblCount = new JLabel("Đang tải dữ liệu...");
        lblCount.setForeground(new Color(100, 116, 139));
        add(lblCount, BorderLayout.SOUTH);

        btnThem.addActionListener(e -> moForm(false));
        btnSua.addActionListener(e -> moForm(true));
        btnXoa.addActionListener(e -> xoaPhanCa());
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { locDuLieu(); }
            public void removeUpdate(DocumentEvent e) { locDuLieu(); }
            public void changedUpdate(DocumentEvent e) { locDuLieu(); }
        });

        taiDuLieu();
    }

    private void taiDuLieu() {
        try {
            danhSachPhanCa = phanCaDAO.getAll();
            hienThiDanhSach(danhSachPhanCa);
            if (danhSachPhanCa.isEmpty()) {
                lblCount.setText("Hiển thị 0 bản ghi – kiểm tra dữ liệu bảng PhanCa trong SQL Server");
            }
        } catch (Exception e) {
            lblCount.setText("Không tải được dữ liệu phân ca");
        }
    }

    private void hienThiDanhSach(List<PhanCa> ds) {
        modelPhanCa.setRowCount(0);
        for (PhanCa pc : ds) {
            String maNV = (pc.getMaNhanVien() != null) ? pc.getMaNhanVien().getMaNhanVien() : "";
            String tenNV = (pc.getMaNhanVien() != null) ? pc.getMaNhanVien().getHoTenNhanVien() : "";
            String tenCa = (pc.getMaCa() != null) ? pc.getMaCa().getTenCa() : "";
            modelPhanCa.addRow(new Object[]{
                    pc.getMaPhanCa(),
                    maNV,
                    tenNV,
                    tenCa,
                    pc.getNgayLam(),
                    pc.getTrangThai() != null ? pc.getTrangThai().getMoTa() : ""
            });
        }
        lblCount.setText("Hiển thị " + ds.size() + " bản ghi");
    }

    private void locDuLieu() {
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
        List<PhanCa> ketQua = new ArrayList<>();
        for (PhanCa pc : danhSachPhanCa) {
            String tenNV = (pc.getMaNhanVien() != null) ? pc.getMaNhanVien().getHoTenNhanVien() : "";
            String thongTin = (pc.getMaPhanCa() + " " + tenNV).toLowerCase();
            if (thongTin.contains(tuKhoa)) ketQua.add(pc);
        }
        hienThiDanhSach(ketQua);
    }

    private PhanCa phanCaDangChon() {
        int row = tblPhanCa.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phân ca.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        String ma = String.valueOf(modelPhanCa.getValueAt(tblPhanCa.convertRowIndexToModel(row), 0));
        for (PhanCa pc : danhSachPhanCa) if (pc.getMaPhanCa().equals(ma)) return pc;
        return null;
    }

    private void xoaPhanCa() {
        PhanCa pc = phanCaDangChon();
        if (pc == null) return;
        int chon = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa phân ca này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (chon == JOptionPane.YES_OPTION) {
            if (phanCaDAO.delete(pc)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công.");
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void moForm(boolean sua) {
        final PhanCa pcCu = sua ? phanCaDangChon() : null;
        if (sua && pcCu == null) return;

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), sua ? "Cập nhật phân ca" : "Thêm phân ca mới", Dialog.ModalityType.APPLICATION_MODAL);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(18, 22, 10, 22));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.anchor = GridBagConstraints.WEST;

        JTextField txtMa = new JTextField(sua ? pcCu.getMaPhanCa() : phanCaDAO.taoMaMoi(), 22);
        txtMa.setEditable(false);

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
        if (sua && pcCu.getMaNhanVien() != null) {
            for (NhanVien nv : listNV) {
                if (nv.getMaNhanVien().equals(pcCu.getMaNhanVien().getMaNhanVien())) {
                    cboNhanVien.setSelectedItem(nv);
                    break;
                }
            }
        }

        List<CaLamViec> listCa = caLamViecDAO.getAll();
        JComboBox<CaLamViec> cboCa = new JComboBox<>(listCa.toArray(new CaLamViec[0]));
        cboCa.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof CaLamViec) {
                    CaLamViec c = (CaLamViec) value;
                    setText(c.getMaCa() + " - " + c.getTenCa());
                }
                return this;
            }
        });
        if (sua && pcCu.getMaCa() != null) {
            for (CaLamViec c : listCa) {
                if (c.getMaCa().equals(pcCu.getMaCa().getMaCa())) {
                    cboCa.setSelectedItem(c);
                    break;
                }
            }
        }

        JTextField txtNgayLam = new JTextField(sua && pcCu.getNgayLam() != null ? pcCu.getNgayLam().toString() : LocalDate.now().toString(), 22);
        JComboBox<TrangThaiPhanCa> cboTrangThai = new JComboBox<>(TrangThaiPhanCa.values());
        if (sua && pcCu.getTrangThai() != null) cboTrangThai.setSelectedItem(pcCu.getTrangThai());

        themDong(form, g, 0, "Mã phân ca", txtMa);
        themDong(form, g, 1, "Nhân viên *", cboNhanVien);
        themDong(form, g, 2, "Ca làm việc *", cboCa);
        themDong(form, g, 3, "Ngày làm (yyyy-MM-dd) *", txtNgayLam);
        themDong(form, g, 4, "Trạng thái", cboTrangThai);

        JLabel lblLoi = new JLabel(" ");
        lblLoi.setForeground(Color.RED);
        JButton btnLuu = taoNut("Lưu", new Color(14, 98, 195), Color.WHITE);
        JButton btnHuy = taoNut("Hủy", Color.WHITE, new Color(14, 98, 195));
        btnHuy.addActionListener(e -> dialog.dispose());

        btnLuu.addActionListener(e -> {
            try {
                if (txtNgayLam.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("Vui lòng nhập ngày làm.");
                }
                LocalDate ngayLam = LocalDate.parse(txtNgayLam.getText().trim());
                NhanVien nv = (NhanVien) cboNhanVien.getSelectedItem();
                CaLamViec ca = (CaLamViec) cboCa.getSelectedItem();

                PhanCa pc = new PhanCa(
                        txtMa.getText().trim(),
                        nv,
                        ca,
                        ngayLam,
                        (TrangThaiPhanCa) cboTrangThai.getSelectedItem()
                );

                boolean thanhCong = sua ? phanCaDAO.update(pc) : phanCaDAO.insert(pc);
                if (thanhCong) {
                    JOptionPane.showMessageDialog(dialog, sua ? "Cập nhật thành công." : "Thêm mới thành công.");
                    dialog.dispose();
                    taiDuLieu();
                } else {
                    lblLoi.setText("Lưu thất bại.");
                }
            } catch (DateTimeParseException ex) {
                lblLoi.setText("Ngày làm phải đúng định dạng yyyy-MM-dd.");
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
