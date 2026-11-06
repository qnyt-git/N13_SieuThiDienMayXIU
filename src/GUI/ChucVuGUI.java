package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ChucVuGUI extends JPanel {
    private DefaultTableModel modelChucVu;
    private JTable tblChucVu;

    public ChucVuGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel lblTieuDe = new JLabel("Quản lý danh mục Chức vụ nhân viên");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        top.add(lblTieuDe, BorderLayout.WEST);

        JButton btnThem = taoNut("+ Thêm chức vụ", new Color(14, 98, 195), Color.WHITE);
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtn.setOpaque(false);
        pnlBtn.add(btnThem);
        top.add(pnlBtn, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        modelChucVu = new DefaultTableModel(new Object[]{"Mã chức vụ", "Tên chức vụ / Vị trí", "Mô tả quyền hạn"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblChucVu = new JTable(modelChucVu);
        tblChucVu.setRowHeight(28);
        
        // Thêm dữ liệu mẫu sẵn có từ hệ thống
        modelChucVu.addRow(new Object[]{"CV01", "Nhân viên quản lý", "Toàn quyền quản trị hệ thống siêu thị"});
        modelChucVu.addRow(new Object[]{"CV02", "Nhân viên bán hàng", "Thực hiện lập hóa đơn, bán hàng trực tiếp"});
        modelChucVu.addRow(new Object[]{"CV03", "Nhân viên kế toán", "Quản lý tài chính, hóa đơn và công nợ"});
        modelChucVu.addRow(new Object[]{"CV04", "Nhân viên kho", "Lập phiếu nhập kho, kiểm kê thiết bị"});
        modelChucVu.addRow(new Object[]{"CV05", "Nhân viên CSKH", "Tiếp nhận bảo hành và chăm sóc khách hàng"});

        add(new JScrollPane(tblChucVu), BorderLayout.CENTER);
        
        btnThem.addActionListener(e -> {
            String tenCV = JOptionPane.showInputDialog(this, "Nhập tên chức vụ mới:");
            if (tenCV != null && !tenCV.trim().isEmpty()) {
                modelChucVu.addRow(new Object[]{"CV0" + (tblChucVu.getRowCount() + 1), tenCV, "Chức vụ tùy chỉnh mới"});
                JOptionPane.showMessageDialog(this, "Thêm chức vụ thành công!");
            }
        });
    }

    private JButton taoNut(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(fg);
        b.setFocusPainted(false);
        return b;
    }
}
