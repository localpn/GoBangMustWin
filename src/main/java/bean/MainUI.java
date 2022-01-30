package bean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class MainUI extends JFrame {

    // ���캯��
    public MainUI() throws Exception {
        initComponents();  //�����ʼ��

        int initUser = -1; //��������
        if (humanBtn.isSelected())  //���ݽ���ѡ������ְ���Ӧ���ֿ��֣�Ŀǰ�ݶ����������
            initUser = GobangPanel.HUMAN;
        else if (computerBtn.isSelected())
            initUser = GobangPanel.COMPUTER;

        panel.startGame(initUser); //����GobangPanel�е�startGame��ʼ��Ϸ��Ĭ�Ͻ���һ��ʾ���Զ�����
    }

    // ��ʼ�����漰�����
    private void initComponents() throws Exception {
        grp_alg = new ButtonGroup();
        rightPane = new JPanel();
        panel1 = new JScrollPane();
        area = new JTextArea();
        panel2 = new JPanel();
        jLabel1 = new JLabel();
        computerBtn = new JRadioButton();
        humanBtn = new JRadioButton();
        btn = new JButton();
        undoBtn = new JButton();
        showGamesBtn = new JButton();
        saveGameBtn = new JButton();
        orderBtn = new JCheckBox();
        area = new JTextArea();
        panel = new GobangPanel(area);  //�������ʼ��

        // ������������
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("AI�������˻�����");
        setPreferredSize(new Dimension(900, 700));
        ImageIcon icon = new ImageIcon("src/main/resources/image/logo.png");
        setIconImage(icon.getImage());
        setResizable(false);
        setLocation(250,20);
        setVisible(true);

        //�����������
        GroupLayout panelLayout = new GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
                panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 669, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
                panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );

        panel1.setBorder(BorderFactory.createTitledBorder("�������"));
        area.setColumns(15);
        area.setRows(5);
        area.setBorder(null);
        area.setEnabled(false);
        panel1.setViewportView(area);
        panel2.setBorder(BorderFactory.createTitledBorder("����"));
        jLabel1.setText("ѡ�����֣�");
        grp_alg.add(computerBtn);
        computerBtn.setText("����");
        computerBtn.setSelected(true);

        grp_alg.add(humanBtn);
        humanBtn.setText("��");
        btn.setText("��ʼ��Ϸ");
        undoBtn.setText("����");
        saveGameBtn.setText("���浱ǰ���");
        showGamesBtn.setText("�鿴��ʷ���");
        orderBtn.setText("��ʾ����˳��");

        //����ť����¼�����
        btn.addActionListener(l);
        saveGameBtn.addActionListener(l);
        showGamesBtn.addActionListener(l);
        orderBtn.addActionListener(l);
        undoBtn.addActionListener(l);

        //����
        GroupLayout panel2Layout = new GroupLayout(panel2);
        panel2.setLayout(panel2Layout);
        panel2Layout.setHorizontalGroup(
                panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panel2Layout.createSequentialGroup()
                                .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(panel2Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(btn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(undoBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(panel2Layout.createSequentialGroup()
                                                                .addComponent(jLabel1)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(computerBtn)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(humanBtn)
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                        .addComponent(showGamesBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(panel2Layout.createSequentialGroup()
                                                .addGap(39, 39, 39)
                                                .addComponent(orderBtn)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(panel2Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(saveGameBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        panel2Layout.setVerticalGroup(
                panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(computerBtn)
                                        .addComponent(humanBtn))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                                .addComponent(undoBtn)
                                .addGap(12, 12, 12)
                                .addComponent(saveGameBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(showGamesBtn)
                                .addGap(18, 18, 18)
                                .addComponent(orderBtn)
                                .addGap(11, 11, 11))
        );

        GroupLayout rightPaneLayout = new GroupLayout(rightPane);
        rightPane.setLayout(rightPaneLayout);
        rightPaneLayout.setHorizontalGroup(
                rightPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, rightPaneLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(panel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addGroup(rightPaneLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        rightPaneLayout.setVerticalGroup(
                rightPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(rightPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panel1, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        //����
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rightPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(rightPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        pack();
    }

    // �����¼�����
    private ActionListener l = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();  //��ȡ�¼�Դ
            if (source == btn) {    //��ʼ��Ϸ
                int initUser = -1;
                if (humanBtn.isSelected())
                    initUser = GobangPanel.HUMAN;
                else if (computerBtn.isSelected())
                    initUser = GobangPanel.COMPUTER;

                panel.startGame(initUser);
            }
            else if (source == orderBtn) {   //��ʾ����˳��
                panel.toggleOrder();
            }
            else if (source == undoBtn) {    //����
                if (MustWinGo.undoFlag == 2){ //=2������ǣ��ڱ�ʤ�����а����м��ֱ仯�����������˺���ӻ�Ӧ�Եģ���ʱ�����������
                    panel.undo();
                    panel.undo();
                }
                else {  //=1������ǣ��������£�ֻ��ڵ�ǰһ��
                    panel.undo();
                }
            }
            else if (source == saveGameBtn) {   // ���浱ǰ���
                try {
                    panel.writeManual();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else if (source == showGamesBtn) {   //��ʾ��ʷ���
                try {
                    showGames();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }
    };

    // �ڽ����Ҳ��ı���������ı�����
    public static void appendText(String s) {
        area.append(s);
    }

    // ��ս����Ҳ��ı���
    public static void clearText() {
        area.setText("");
    }

    // ��ʾ��ʷ���׶Ի���
    public void showGames() throws SQLException, ClassNotFoundException {
        new GameDialog(this,true).setVisible(true);
    }

    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }


        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new MainUI().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private static JTextArea area; //�Ҳ��ı���
    private JButton btn; //��ʼ��Ϸ��ť
    private ButtonGroup grp_alg; //����ѡ��
    private JRadioButton computerBtn;
    private JRadioButton humanBtn;
    private JLabel jLabel1;
    public static JCheckBox orderBtn; //�Ƿ���ʾ����˳��ѡ��
    private JScrollPane panel1;
    private JPanel panel2;
    private JPanel rightPane;
    private JButton saveGameBtn;  //���浱ǰ��ְ�ť
    private JButton showGamesBtn; //��ʾ��ʷ���װ�ť
    private JButton undoBtn;  //���尴ť
    private GobangPanel panel; //GobangPanel����
}

