package GUI;

import BUS.ChucVuBUS;
import ENTITY.ChucVu;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ChucVuGUI extends JPanel {
    private final ChucVuBUS bus=new ChucVuBUS();
    private final DefaultTableModel model;
    public ChucVuGUI(){
        setLayout(new BorderLayout(0,12));setBackground(new Color(245,247,250));setBorder(new EmptyBorder(18,22,18,22));
        JPanel top=new JPanel(new BorderLayout());top.setOpaque(false);JLabel title=new JLabel("Quản lý danh mục Chức vụ nhân viên");title.setFont(new Font("Arial",Font.BOLD,20));title.setForeground(new Color(20,40,70));top.add(title,BorderLayout.WEST);
        JButton add=new JButton("+ Thêm chức vụ");add.setBackground(new Color(14,98,195));add.setForeground(Color.WHITE);JPanel right=new JPanel(new FlowLayout(FlowLayout.RIGHT));right.setOpaque(false);right.add(add);top.add(right,BorderLayout.EAST);add(top,BorderLayout.NORTH);
        model=new DefaultTableModel(new Object[]{"Mã chức vụ","Tên chức vụ","Vị trí làm việc","Mô tả công việc","Trạng thái"},0){public boolean isCellEditable(int r,int c){return false;}};JTable table=new JTable(model);table.setRowHeight(28);add(new JScrollPane(table),BorderLayout.CENTER);
        add.addActionListener(e->themChucVu());taiDuLieu();
    }
    private void taiDuLieu(){model.setRowCount(0);List<ChucVu> ds=bus.getAll();for(ChucVu cv:ds)model.addRow(new Object[]{cv.getMaChucVu(),cv.getTenChucVu(),cv.getViTriLamViec(),cv.getMoTaCongViec(),cv.isTrangThai()?"Đang hoạt động":"Tạm ngưng"});}
    private void themChucVu(){
        JTextField ten=new JTextField(),viTri=new JTextField(),moTa=new JTextField();JPanel p=new JPanel(new GridLayout(0,2,8,8));p.add(new JLabel("Tên chức vụ *"));p.add(ten);p.add(new JLabel("Vị trí làm việc *"));p.add(viTri);p.add(new JLabel("Mô tả công việc"));p.add(moTa);
        if(JOptionPane.showConfirmDialog(this,p,"Thêm chức vụ mới",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;
        if(ten.getText().trim().isEmpty()||viTri.getText().trim().isEmpty()){JOptionPane.showMessageDialog(this,"Vui lòng nhập tên chức vụ và vị trí làm việc.");return;}
        ChucVu cv=new ChucVu(bus.taoMaMoi(),ten.getText().trim(),viTri.getText().trim(),moTa.getText().trim(),true);
        if(bus.them(cv)){JOptionPane.showMessageDialog(this,"Thêm chức vụ thành công.");taiDuLieu();}else JOptionPane.showMessageDialog(this,"Thêm thất bại. Tên chức vụ có thể đã tồn tại.","Lỗi",JOptionPane.ERROR_MESSAGE);
    }
}
