package GUI;

import DAO.CaLamViecDAO;
import DAO.PhanCaDAO;
import ENTITY.CaLamViec;
import ENTITY.PhanCa;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LichLamViecGUI extends JPanel {
    private LocalDate ngayDauTuan=LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    private final JTable table;
    private final DefaultTableModel model;
    private final JLabel lblTuan;
    private final PhanCaDAO phanCaDAO=new PhanCaDAO();
    private final CaLamViecDAO caDAO=new CaLamViecDAO();

    public LichLamViecGUI(){
        setLayout(new BorderLayout(0,12));setBackground(new Color(245,247,250));setBorder(new EmptyBorder(18,22,18,22));
        JPanel tren=new JPanel(new BorderLayout(0,12));tren.setOpaque(false);
        JLabel title=new JLabel("Lịch làm việc");title.setFont(new Font("Arial",Font.BOLD,20));title.setForeground(new Color(20,40,70));tren.add(title,BorderLayout.NORTH);
        JPanel dieuHuong=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));dieuHuong.setOpaque(false);
        JButton truoc=new JButton("‹ Tuần trước"),nay=new JButton("Tuần này"),sau=new JButton("Tuần sau ›"),capNhat=new JButton("Cập nhật lịch làm việc");
        lblTuan=new JLabel();lblTuan.setFont(new Font("Arial",Font.BOLD,14));
        dieuHuong.add(truoc);dieuHuong.add(nay);dieuHuong.add(sau);dieuHuong.add(lblTuan);dieuHuong.add(capNhat);tren.add(dieuHuong,BorderLayout.CENTER);add(tren,BorderLayout.NORTH);
        String[] cot={"Ca làm việc","Thứ 2","Thứ 3","Thứ 4","Thứ 5","Thứ 6","Thứ 7","Chủ nhật"};
        model=new DefaultTableModel(cot,0){public boolean isCellEditable(int r,int c){return false;}};table=new JTable(model);table.setRowHeight(85);table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);add(new JScrollPane(table),BorderLayout.CENTER);
        truoc.addActionListener(e->{ngayDauTuan=ngayDauTuan.minusWeeks(1);taiLich();});
        nay.addActionListener(e->{ngayDauTuan=LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));taiLich();});
        sau.addActionListener(e->{ngayDauTuan=ngayDauTuan.plusWeeks(1);taiLich();});
        capNhat.addActionListener(e->{JDialog d=new JDialog(SwingUtilities.getWindowAncestor(this),"Cập nhật lịch làm việc",Dialog.ModalityType.APPLICATION_MODAL);d.setLayout(new BorderLayout());d.add(new PhanCaGUI(),BorderLayout.CENTER);d.setSize(1050,650);d.setLocationRelativeTo(this);d.setVisible(true);taiLich();});
        taiLich();
    }
    private void taiLich(){
        DateTimeFormatter f=DateTimeFormatter.ofPattern("dd/MM");lblTuan.setText("Tuần: "+ngayDauTuan.format(f)+" - "+ngayDauTuan.plusDays(6).format(f));
        model.setRowCount(0);List<CaLamViec> cacCa=caDAO.getAll();List<PhanCa> phanCa=phanCaDAO.getAll();
        for(CaLamViec ca:cacCa){Object[] row=new Object[8];row[0]=ca.getTenCa();
            for(int i=0;i<7;i++){LocalDate ngay=ngayDauTuan.plusDays(i);StringBuilder noiDung=new StringBuilder();
                for(PhanCa pc:phanCa){if(pc.getNgayLam()!=null&&pc.getNgayLam().equals(ngay)&&pc.getMaCa()!=null&&pc.getMaCa().getMaCa().equals(ca.getMaCa())){if(noiDung.length()>0)noiDung.append("\n");noiDung.append(pc.getMaNhanVien()!=null?pc.getMaNhanVien().getHoTenNhanVien():"Nhân viên");if(pc.getTrangThai()!=null)noiDung.append(" - ").append(pc.getTrangThai().getMoTa());}}
                row[i+1]=noiDung.length()==0?"-": "<html>"+noiDung.toString().replace("\n","<br>")+"</html>";
            }model.addRow(row);
        }
    }
}
