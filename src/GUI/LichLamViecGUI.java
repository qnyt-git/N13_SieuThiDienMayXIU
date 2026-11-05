package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LichLamViecGUI extends JPanel {
    private JButton btnTuanTruoc, btnTuanNay, btnTuanSau;
    private JLabel lblTuanHienTai;
    private JTable tblLichLamViec;

    public LichLamViecGUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel panelTren = new JPanel(new BorderLayout(0, 12));
        panelTren.setOpaque(false);

        JLabel lblTieuDe = new JLabel("Lịch làm việc");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(new Color(20, 40, 70));
        panelTren.add(lblTieuDe, BorderLayout.NORTH);

        JPanel panelDieuHuong = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelDieuHuong.setOpaque(false);

        btnTuanTruoc = new JButton("‹ Tuần trước");
        btnTuanNay = new JButton("Tuần này");
        btnTuanNay.setBackground(new Color(14, 98, 195)); btnTuanNay.setForeground(Color.WHITE);
        btnTuanSau = new JButton("Tuần sau ›");
        lblTuanHienTai = new JLabel("   Tuần hiện tại");
        lblTuanHienTai.setFont(new Font("Arial", Font.BOLD, 14));

        panelDieuHuong.add(btnTuanTruoc);
        panelDieuHuong.add(btnTuanNay);
        panelDieuHuong.add(btnTuanSau);
        panelDieuHuong.add(lblTuanHienTai);

        panelTren.add(panelDieuHuong, BorderLayout.CENTER);
        add(panelTren, BorderLayout.NORTH);

        String[] cot = {"Ca làm việc", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
        tblLichLamViec = new JTable(new DefaultTableModel(cot, 0));
        tblLichLamViec.setRowHeight(100);

        add(new JScrollPane(tblLichLamViec), BorderLayout.CENTER);
    }
}