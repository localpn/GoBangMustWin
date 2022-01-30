package bean;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.SQLException;

// ��ʷ�����б�Ի������ʾ��
public class GameDialog extends JDialog {

    public GameDialog(JFrame parent, boolean modal) throws SQLException, ClassNotFoundException {
        super(parent,modal);
        initComponents();
    }

    // �������������ʼ��
    private void initComponents() {
        // ������ʼ��
        jScrollPane = new JScrollPane();
        table = new JTable();
        showManualBtn = new JButton();
        backBtn = new JButton();
        panel = new GobangPanel();
        r = new DefaultTableCellRenderer();
        fileNameList = getFileNameList();  // ͨ��getFileNameList������ȡ������ʷ�����ļ���
        tableData = new Object[fileNameList.length][];
        for (int i = 0; i < fileNameList.length; i++) {  // ��ʼ�������ʾ���ݣ�����ʷ�����ļ���
            tableData[i] = new Object[]{fileNameList[i]};
        }

        // ���ñ�����ݾ�����ʾ
        r.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class,r);
        table.getTableHeader().setDefaultRenderer(r);

        // ���ڡ���ť��������
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("��ʷ�����б�");
        setLocation(400,200);
        showManualBtn.setText("��ʾ����");
        backBtn.setText("����");

        // ����ť����¼���������
        showManualBtn.addActionListener(l);
        backBtn.addActionListener(l);

        // ���ñ���ʽ
        table.setModel(new javax.swing.table.DefaultTableModel(
                tableData,
                new String [] {
                        "��ʷ����"
                }
        ) {
            Class[] types = new Class [] {
                    java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                    false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setResizable(false);
        }

        // ���ý����ϸ��������
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(95, 95, 95)
                                .addComponent(showManualBtn)
                                .addGap(94, 94, 94)
                                .addComponent(backBtn, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jScrollPane, GroupLayout.Alignment.TRAILING)
        );
        layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {backBtn, showManualBtn});
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(backBtn)
                                        .addComponent(showManualBtn))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    // �¼���������
    private ActionListener l = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            if (source == showManualBtn) {    // ��ʾ����
                int index = table.getSelectedRow();  // ��ȡ�û�ѡ�е�������ţ�index��0��ʼ
                if (index == -1){  // -1��ʾδѡ��
                    showMessage();  // ��ʾ��ѡ���¼����ʾ��Ϣ
                }
                else {
                    String manualPath = (String) table.getValueAt(index,0);  // ��ȡѡ�е������ļ���
                    panel.drawManual(manualPath);  // ����drawManual������������
                    setUnseen();  // ���öԻ�����ʧ
                }
            } else if (source == backBtn) {  // ���ذ�ť
                setUnseen();
            }
        }
    };

    // ��ȡ��ʷ�����ļ����б�
    private String[] getFileNameList() {
        String path = "manual";  // manualĿ¼���������ļ�
        File dir = new File(path);
        String[] result = null;

        if (dir.isDirectory()) {  // �ж�dir�Ƿ�ΪĿ¼����
            result = dir.list();  //File���е�list�����ɻ�ȡ��ǰĿ¼�µ������ļ���������String[]��ʽ����
        }
        return result;
    }

    // ��ʾ��ʾ��ѡ���¼����ʾ��Ϣ
    private void showMessage() {
        JOptionPane.showMessageDialog(this,"��ѡ��һ����¼��");
    }

    // ���öԻ�����ʧ
    public void setUnseen() {
        this.setVisible(false);
    }


    private JButton backBtn;  // ���ذ�ť
    private JScrollPane jScrollPane;  // �������
    private JButton showManualBtn;  // ��ʾ���װ�ť
    private JTable table;   // ��ʾ��ʷ���׵ı��
    private String[] fileNameList;  // ������ʷ�����ļ������б�
    private Object [][]tableData;   // ���ڼ��ص������ʾ�еı������
    private DefaultTableCellRenderer r;  // ���ñ�����ݸ�ʽ��ʾ
    private GobangPanel panel;  // ������
}
