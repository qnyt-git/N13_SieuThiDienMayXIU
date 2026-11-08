package GUI;

import DAO.KhuyenMaiDAO;
import ENTITY.KhuyenMai;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import com.toedter.calendar.JDateChooser;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiGUI extends JPanel {
    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();
    private final DefaultTableModel modelKhuyenMai;
    private final JTable tblKhuyenMai;
    private final JTextField txtTimKiem;
    private final JLabel lblCount;
    private List<KhuyenMai> danhSachKhuyenMai = new ArrayList<>();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public KhuyenMaiGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        JLabel lblTieuDe = new JLabel("Quản lý chương trình khuyến mãi");
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
        JButton btnThem = taoNut("+ Thêm khuyến mãi", new Color(14, 98, 195), Color.WHITE);
        buttons.add(btnSua);
        buttons.add(btnXoa);
        buttons.add(btnThem);
        bar.add(left, BorderLayout.WEST);
        bar.add(buttons, BorderLayout.EAST);
        top.add(bar, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        modelKhuyenMai = new DefaultTableModel(new Object[]{
                "Mã KM", "Tên khuyến mãi", "Thời gian bắt đầu", "Thời gian kết thúc", "Giá trị giảm", "Trạng thái"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblKhuyenMai = new JTable(modelKhuyenMai);
        tblKhuyenMai.setRowHeight(28);
        tblKhuyenMai.setFont(new Font("Arial", Font.PLAIN, 13));
        tblKhuyenMai.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblKhuyenMai.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tblKhuyenMai), BorderLayout.CENTER);

        lblCount = new JLabel("Đang tải dữ liệu...");
        lblCount.setForeground(new Color(100, 116, 139));
        add(lblCount, BorderLayout.SOUTH);

        btnThem.addActionListener(e -> moForm(false));
        btnSua.addActionListener(e -> moForm(true));
        btnXoa.addActionListener(e -> xoaKhuyenMai());
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { locDuLieu(); }
            public void removeUpdate(DocumentEvent e) { locDuLieu(); }
            public void changedUpdate(DocumentEvent e) { locDuLieu(); }
        });

        taiDuLieu();
    }

    private void taiDuLieu() {
        try {
            danhSachKhuyenMai = khuyenMaiDAO.getAll();
            hienThiDanhSach(danhSachKhuyenMai);
            if (danhSachKhuyenMai.isEmpty()) {
                lblCount.setText("Hiển thị 0 bản ghi – kiểm tra dữ liệu bảng KhuyenMai trong SQL Server");
            }
        } catch (Exception e) {
            lblCount.setText("Không tải được dữ liệu khuyến mãi");
        }
    }

    private void hienThiDanhSach(List<KhuyenMai> ds) {
        modelKhuyenMai.setRowCount(0);
        for (KhuyenMai km : ds) {
            modelKhuyenMai.addRow(new Object[]{
                    km.getMaKhuyenMai(),
                    km.getTenKhuyenMai(),
                    km.getThoiGianBatDau() != null ? km.getThoiGianBatDau().format(FORMATTER) : "",
                    km.getThoiGianKetThuc() != null ? km.getThoiGianKetThuc().format(FORMATTER) : "",
                    km.getGiaTriGiam(),
                    khuyenMaiDAO.trangThaiText(km)
            });
        }
        lblCount.setText("Hiển thị " + ds.size() + " bản ghi");
    }

    private void locDuLieu() {
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
        List<KhuyenMai> ketQua = new ArrayList<>();
        for (KhuyenMai km : danhSachKhuyenMai) {
            String thongTin = (km.getMaKhuyenMai() + " " + km.getTenKhuyenMai()).toLowerCase();
            if (thongTin.contains(tuKhoa)) ketQua.add(km);
        }
        hienThiDanhSach(ketQua);
    }

    private KhuyenMai khuyenMaiDangChon() {
        int row = tblKhuyenMai.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khuyến mãi.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        String ma = String.valueOf(modelKhuyenMai.getValueAt(tblKhuyenMai.convertRowIndexToModel(row), 0));
        for (KhuyenMai km : danhSachKhuyenMai) if (km.getMaKhuyenMai().equals(ma)) return km;
        return null;
    }

    private void xoaKhuyenMai() {
        KhuyenMai km = khuyenMaiDangChon();
        if (km == null) return;
        int chon = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khuyến mãi " + km.getTenKhuyenMai() + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (chon == JOptionPane.YES_OPTION) {
            if (khuyenMaiDAO.delete(km)) {
                JOptionPane.showMessageDialog(this, "Xóa khuyến mãi thành công.");
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void moForm(boolean sua) {
        final KhuyenMai kmCu = sua ? khuyenMaiDangChon() : null;
        if (sua && kmCu == null) return;

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), sua ? "Cập nhật khuyến mãi" : "Thêm khuyến mãi mới", Dialog.ModalityType.APPLICATION_MODAL);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(18, 22, 10, 22));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.anchor = GridBagConstraints.WEST;

        JTextField txtMa = new JTextField(sua ? kmCu.getMaKhuyenMai() : khuyenMaiDAO.taoMaMoi(), 22);
        txtMa.setEditable(false);
        JTextField txtTen = new JTextField(sua ? kmCu.getTenKhuyenMai() : "", 22);
        JDateChooser txtBatDau = new JDateChooser();
        txtBatDau.setDateFormatString("dd/MM/yyyy");
        txtBatDau.setDate(sua && kmCu.getThoiGianBatDau() != null ? java.sql.Date.valueOf(kmCu.getThoiGianBatDau().toLocalDate()) : java.sql.Date.valueOf(LocalDate.now()));
        JDateChooser txtKetThuc = new JDateChooser();
        txtKetThuc.setDateFormatString("dd/MM/yyyy");
        txtKetThuc.setDate(sua && kmCu.getThoiGianKetThuc() != null ? java.sql.Date.valueOf(kmCu.getThoiGianKetThuc().toLocalDate()) : java.sql.Date.valueOf(LocalDate.now().plusDays(7)));
        JTextField txtGiaTri = new JTextField(sua ? String.valueOf(kmCu.getGiaTriGiam()) : "", 22);
        JComboBox<String> cboTrangThai = new JComboBox<>(new String[]{"Đang diễn ra", "Đã kết thúc"});
        cboTrangThai.setSelectedItem(sua && kmCu.isTrangThai() ? "Đang diễn ra" : "Đã kết thúc");

        themDong(form, g, 0, "Mã khuyến mãi", txtMa);
        themDong(form, g, 1, "Tên khuyến mãi *", txtTen);
        themDong(form, g, 2, "Ngày bắt đầu *", txtBatDau);
        themDong(form, g, 3, "Ngày kết thúc *", txtKetThuc);
        themDong(form, g, 4, "Giá trị giảm *", txtGiaTri);
        themDong(form, g, 5, "Trạng thái", cboTrangThai);

        JLabel lblLoi = new JLabel(" ");
        lblLoi.setForeground(Color.RED);
        JButton btnLuu = taoNut("Lưu", new Color(14, 98, 195), Color.WHITE);
        JButton btnHuy = taoNut("Hủy", Color.WHITE, new Color(14, 98, 195));
        btnHuy.addActionListener(e -> dialog.dispose());

        btnLuu.addActionListener(e -> {
            try {
                String ten = txtTen.getText().trim();
                if (ten.isEmpty() || txtBatDau.getDate() == null || txtKetThuc.getDate() == null || txtGiaTri.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("Vui lòng điền đầy đủ các thông tin bắt buộc.");
                }
                LocalDate bd = new java.sql.Date(txtBatDau.getDate().getTime()).toLocalDate();
                LocalDate kt = new java.sql.Date(txtKetThuc.getDate().getTime()).toLocalDate();
                if (kt.isBefore(bd)) throw new IllegalArgumentException("Ngày kết thúc phải sau hoặc bằng ngày bắt đầu.");
                double giaTri = Double.parseDouble(txtGiaTri.getText().trim());

                KhuyenMai km = new KhuyenMai();
                km.setMaKhuyenMai(txtMa.getText().trim());
                km.setTenKhuyenMai(ten);
                km.setGiaTriGiam(giaTri);
                km.setTrangThai("Đang diễn ra".equals(cboTrangThai.getSelectedItem()));
                khuyenMaiDAO.datThoiGian(km, bd, kt);

                boolean thanhCong = sua ? khuyenMaiDAO.update(km) : khuyenMaiDAO.insert(km);
                if (thanhCong) {
                    JOptionPane.showMessageDialog(dialog, sua ? "Cập nhật thành công." : "Thêm mới thành công.");
                    dialog.dispose();
                    taiDuLieu();
                } else {
                    lblLoi.setText("Lưu thất bại.");
                }
            } catch (NumberFormatException ex) {
                lblLoi.setText("Giá trị giảm phải là số hợp lệ.");
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
