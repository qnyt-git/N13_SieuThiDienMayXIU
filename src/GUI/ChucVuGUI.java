package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ChucVuGUI extends JPanel {

    private JTextField txt0;
    private JTextField txt1;
    private JTextField txt2;
    private JComboBox<String> cbo3;
    private JTable tblDuLieu;

    public ChucVuGUI() {
        setLayout(new BorderLayout(12, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel lblTieuDe = new JLabel("Quản lý chức vụ");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 21));
        lblTieuDe.setForeground(new Color(20, 55, 95));
        add(lblTieuDe, BorderLayout.NORTH);

        JPanel panelChinh = new JPanel(new BorderLayout(10, 10));
        panelChinh.setOpaque(false);

        JPanel panelThongTin = new JPanel(new BorderLayout(8, 8));
        panelThongTin.setBackground(Color.WHITE);
        panelThongTin.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                new EmptyBorder(14, 14, 14, 14)));

        JLabel lblThongTin = new JLabel("Thông tin và bộ lọc");
        lblThongTin.setFont(new Font("Arial", Font.BOLD, 14));
        lblThongTin.setForeground(new Color(35, 65, 95));

        JPanel form = new JPanel(new GridLayout(0, 4, 10, 9));
        form.setOpaque(false);

        JLabel lbl0 = new JLabel("Mã chức vụ");
        txt0 = new JTextField();
        form.add(lbl0);
        form.add(txt0);
        JLabel lbl1 = new JLabel("Tên chức vụ");
        txt1 = new JTextField();
        form.add(lbl1);
        form.add(txt1);
        JLabel lbl2 = new JLabel("Mô tả");
        txt2 = new JTextField();
        form.add(lbl2);
        form.add(txt2);
        JLabel lbl3 = new JLabel("Trạng thái");
        cbo3 = new JComboBox<String>();
        form.add(lbl3);
        form.add(cbo3);

        panelThongTin.add(lblThongTin, BorderLayout.NORTH);
        panelThongTin.add(form, BorderLayout.CENTER);

        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelNut.setOpaque(false);
        String[] tenNut = {"Thêm", "Cập nhật", "Xóa", "Làm mới", "Tìm kiếm"};
        for (String ten : tenNut) {
            JButton btn = new JButton(ten);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Arial", Font.PLAIN, 12));
            btn.setBackground(new Color(12, 68, 136));
            btn.setForeground(Color.WHITE);
            btn.setBorder(new EmptyBorder(8, 15, 8, 15));
            panelNut.add(btn);
        }

        JPanel panelTren = new JPanel(new BorderLayout(8, 12));
        panelTren.setOpaque(false);
        panelTren.add(panelThongTin, BorderLayout.CENTER);
        panelTren.add(panelNut, BorderLayout.SOUTH);

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Mã chức vụ", "Tên chức vụ", "Mô tả", "Trạng thái"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblDuLieu = new JTable(model);
        tblDuLieu.setRowHeight(27);
        tblDuLieu.setFont(new Font("Arial", Font.PLAIN, 12));
        tblDuLieu.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblDuLieu.getTableHeader().setBackground(new Color(225, 235, 245));
        tblDuLieu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tblDuLieu);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));

        panelChinh.add(panelTren, BorderLayout.NORTH);
        panelChinh.add(scroll, BorderLayout.CENTER);
        add(panelChinh, BorderLayout.CENTER);
    }

    public JTable getTblDuLieu() {
        return tblDuLieu;
    }
}
