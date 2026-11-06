package GUI;

import DAO.CaLamViecDAO;
import ENTITY.CaLamViec;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CaLamViecGUI extends JPanel {
    private final CaLamViecDAO caLamViecDAO = new CaLamViecDAO();
    private final DefaultTableModel modelCa;
    private final JTable tblCa;
    private final JTextField txtTimKiem;
    private final JLabel lblCount;
    private List<CaLamViec> danhSachCa = new ArrayList<>();

    public CaLamViecGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        JLabel lblTieuDe = new JLabel("Quản lý ca làm việc");
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
        JButton btnThem = taoNut("+ Thêm ca mới", new Color(14, 98, 195), Color.WHITE);
        buttons.add(btnSua);
        buttons.add(btnXoa);
        buttons.add(btnThem);
        bar.add(left, BorderLayout.WEST);
        bar.add(buttons, BorderLayout.EAST);
        top.add(bar, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        modelCa = new DefaultTableModel(new Object[]{
                "Mã ca", "Tên ca", "Giờ bắt đầu", "Giờ kết thúc", "Thời lượng (phút)"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblCa = new JTable(modelCa);
        tblCa.setRowHeight(28);
        tblCa.setFont(new Font("Arial", Font.PLAIN, 13));
        tblCa.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblCa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tblCa), BorderLayout.CENTER);

        lblCount = new JLabel("Đang tải dữ liệu...");
        lblCount.setForeground(new Color(100, 116, 139));
        add(lblCount, BorderLayout.SOUTH);

        btnThem.addActionListener(e -> moForm(false));
        btnSua.addActionListener(e -> moForm(true));
        btnXoa.addActionListener(e -> xoaCa());
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { locDuLieu(); }
            public void removeUpdate(DocumentEvent e) { locDuLieu(); }
            public void changedUpdate(DocumentEvent e) { locDuLieu(); }
        });

        taiDuLieu();
    }

    private void taiDuLieu() {
        try {
            danhSachCa = caLamViecDAO.getAll();
            hienThiDanhSach(danhSachCa);
            if (danhSachCa.isEmpty()) {
                lblCount.setText("Hiển thị 0 bản ghi – kiểm tra dữ liệu bảng CaLamViec trong SQL Server");
            }
        } catch (Exception e) {
            lblCount.setText("Không tải được dữ liệu ca làm việc");
        }
    }

    private void hienThiDanhSach(List<CaLamViec> ds) {
        modelCa.setRowCount(0);
        for (CaLamViec c : ds) {
            modelCa.addRow(new Object[]{
                    c.getMaCa(),
                    c.getTenCa(),
                    caLamViecDAO.gioBatDauText(c),
                    caLamViecDAO.gioKetThucText(c),
                    caLamViecDAO.thoiLuongPhut(c)
            });
        }
        lblCount.setText("Hiển thị " + ds.size() + " bản ghi");
    }

    private void locDuLieu() {
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
        List<CaLamViec> ketQua = new ArrayList<>();
        for (CaLamViec c : danhSachCa) {
            String thongTin = (c.getMaCa() + " " + c.getTenCa()).toLowerCase();
            if (thongTin.contains(tuKhoa)) ketQua.add(c);
        }
        hienThiDanhSach(ketQua);
    }

    private CaLamViec caDangChon() {
        int row = tblCa.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một ca làm việc.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        String ma = String.valueOf(modelCa.getValueAt(tblCa.convertRowIndexToModel(row), 0));
        for (CaLamViec c : danhSachCa) if (c.getMaCa().equals(ma)) return c;
        return null;
    }

    private void xoaCa() {
        CaLamViec c = caDangChon();
        if (c == null) return;
        int chon = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa ca " + c.getTenCa() + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (chon == JOptionPane.YES_OPTION) {
            if (caLamViecDAO.delete(c)) {
                JOptionPane.showMessageDialog(this, "Xóa ca thành công.");
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void moForm(boolean sua) {
        final CaLamViec cCu = sua ? caDangChon() : null;
        if (sua && cCu == null) return;

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), sua ? "Cập nhật ca làm" : "Thêm ca làm mới", Dialog.ModalityType.APPLICATION_MODAL);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(18, 22, 10, 22));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.anchor = GridBagConstraints.WEST;

        JTextField txtMa = new JTextField(sua ? cCu.getMaCa() : caLamViecDAO.taoMaMoi(), 22);
        txtMa.setEditable(false);
        JTextField txtTen = new JTextField(sua ? cCu.getTenCa() : "", 22);
        JTextField txtBatDau = new JTextField(sua && cCu.getGioBatDau() != null ? cCu.getGioBatDau().toLocalTime().toString() : "08:00", 22);
        JTextField txtKetThuc = new JTextField(sua && cCu.getGioKetThuc() != null ? cCu.getGioKetThuc().toLocalTime().toString() : "12:00", 22);

        themDong(form, g, 0, "Mã ca", txtMa);
        themDong(form, g, 1, "Tên ca *", txtTen);
        themDong(form, g, 2, "Giờ bắt đầu (HH:mm) *", txtBatDau);
        themDong(form, g, 3, "Giờ kết thúc (HH:mm) *", txtKetThuc);

        JLabel lblLoi = new JLabel(" ");
        lblLoi.setForeground(Color.RED);
        JButton btnLuu = taoNut("Lưu", new Color(14, 98, 195), Color.WHITE);
        JButton btnHuy = taoNut("Hủy", Color.WHITE, new Color(14, 98, 195));
        btnHuy.addActionListener(e -> dialog.dispose());

        btnLuu.addActionListener(e -> {
            try {
                String ten = txtTen.getText().trim();
                if (ten.isEmpty() || txtBatDau.getText().trim().isEmpty() || txtKetThuc.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("Vui lòng điền đầy đủ các thông tin.");
                }
                LocalTime bd = LocalTime.parse(txtBatDau.getText().trim());
                LocalTime kt = LocalTime.parse(txtKetThuc.getText().trim());

                CaLamViec c = new CaLamViec();
                c.setMaCa(txtMa.getText().trim());
                c.setTenCa(ten);
                caLamViecDAO.datGio(c, bd, kt);

                if (!caLamViecDAO.hopLe(c)) {
                    throw new IllegalArgumentException("Giờ kết thúc phải sau giờ bắt đầu.");
                }

                boolean thanhCong = sua ? caLamViecDAO.update(c) : caLamViecDAO.insert(c);
                if (thanhCong) {
                    JOptionPane.showMessageDialog(dialog, sua ? "Cập nhật thành công." : "Thêm mới thành công.");
                    dialog.dispose();
                    taiDuLieu();
                } else {
                    lblLoi.setText("Lưu thất bại.");
                }
            } catch (DateTimeParseException ex) {
                lblLoi.setText("Định dạng giờ phải là HH:mm (ví dụ: 08:30).");
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
