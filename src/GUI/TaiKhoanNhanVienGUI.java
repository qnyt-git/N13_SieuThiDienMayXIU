package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TaiKhoanNhanVienGUI extends JPanel {

    private DefaultTableModel modelTaiKhoan;
    private JTable tblTaiKhoan;
    private JTextField txtTimKiem;
    private JLabel lblCount;

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
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 13));
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

        modelTaiKhoan = new DefaultTableModel(new Object[]{
                "Mã tài khoản", "Tên đăng nhập", "Mật khẩu", "Loại quyền", "Trạng thái", "Nhân viên"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblTaiKhoan = new JTable(modelTaiKhoan);
        tblTaiKhoan.setRowHeight(28);
        tblTaiKhoan.setFont(new Font("Arial", Font.PLAIN, 13));
        tblTaiKhoan.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblTaiKhoan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tblTaiKhoan), BorderLayout.CENTER);

        lblCount = new JLabel("Hiển thị 0 bản ghi");
        lblCount.setFont(new Font("Arial", Font.PLAIN, 12));
        lblCount.setForeground(new Color(100, 116, 139));
        add(lblCount, BorderLayout.SOUTH);

        btnThem.addActionListener(e -> openForm(false));
        btnSua.addActionListener(e -> {
            if (kiemTraChonDong()) openForm(true);
        });
        btnXoa.addActionListener(e -> xoaDongDangChon());

        tblTaiKhoan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tblTaiKhoan.getSelectedRow() >= 0) {
                    openForm(true);
                }
            }
        });
    }

    private boolean kiemTraChonDong() {
        if (tblTaiKhoan.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng trong bảng", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    private void xoaDongDangChon() {
        if (!kiemTraChonDong()) return;
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tài khoản đã chọn?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            // TODO: Gọi TaiKhoanNhanVienDAO xóa dưới CSDL và nạp lại bảng
        }
    }

    private void openForm(boolean edit) {
        JDialog d = new JDialog(SwingUtilities.getWindowAncestor(this),
                (edit ? "Cập nhật " : "Thêm mới ") + "quản lý tài khoản nhân viên",
                Dialog.ModalityType.APPLICATION_MODAL);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(18, 22, 10, 22));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.anchor = GridBagConstraints.WEST;

        JTextField txtId = new JTextField(24);
        txtId.setEnabled(false);
        JTextField txtUser = new JTextField(24);
        JPasswordField txtPass = new JPasswordField(24);
        JComboBox<String> cboQuyen = new JComboBox<>();
        JComboBox<String> cboTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Tạm khóa"});
        JComboBox<String> cboNhanVien = new JComboBox<>();

        // TODO: Nạp danh sách Loại quyền và Nhân viên từ DAO vào cboQuyen, cboNhanVien
        // TODO: Nếu edit == true, đổ dữ liệu dòng đang chọn vào form

        addRow(p, g, 0, "Mã tài khoản (tự động)", txtId);
        addRow(p, g, 1, "Tên đăng nhập *", txtUser);
        addRow(p, g, 2, "Mật khẩu *", txtPass);
        addRow(p, g, 3, "Loại quyền *", cboQuyen);
        addRow(p, g, 4, "Trạng thái *", cboTrangThai);
        addRow(p, g, 5, "Nhân viên *", cboNhanVien);

        JLabel err = new JLabel(" ");
        err.setForeground(new Color(220, 38, 38));
        JButton btnLuu = taoNut("Lưu", new Color(14, 98, 195), Color.WHITE);
        JButton btnHuy = taoNut("Hủy", Color.WHITE, new Color(14, 98, 195));
        btnHuy.addActionListener(e -> d.dispose());

        btnLuu.addActionListener(e -> {
            // TODO: Kiểm tra hợp lệ và gọi TaiKhoanNhanVienDAO lưu vào CSDL
            d.dispose();
        });

        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(Color.WHITE);
        south.setBorder(new EmptyBorder(0, 22, 16, 22));
        JPanel bt = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        bt.setBackground(Color.WHITE);
        bt.add(btnHuy);
        bt.add(btnLuu);
        south.add(err, BorderLayout.NORTH);
        south.add(bt, BorderLayout.SOUTH);

        d.getRootPane().setDefaultButton(btnLuu);
        d.add(p, BorderLayout.CENTER);
        d.add(south, BorderLayout.SOUTH);
        d.pack();
        d.setMinimumSize(new Dimension(480, 0));
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    private void addRow(JPanel p, GridBagConstraints g, int row, String label, JComponent c) {
        g.gridy = row; g.gridx = 0; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        JLabel l = new JLabel(label);
        l.setFont(new Font("Arial", Font.BOLD, 13));
        l.setPreferredSize(new Dimension(160, 28));
        p.add(l, g);
        g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        c.setFont(new Font("Arial", Font.PLAIN, 13));
        c.setPreferredSize(new Dimension(280, 32));
        p.add(c, g);
    }

    private JButton taoNut(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setFocusPainted(false);
        return b;
    }
}