package bean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

// MainUI��ʾ��������棬GobangPanel�ฺ�������߲��ֵ�������ʾ���������߼����ܴ���
public class GobangPanel extends JPanel {
    private static final long serialVersionUID = 667503661521167626L;
    private JTextArea area; // �����ұߵ��ı��������������ʾ��ʾ��Ϣʱ��
    private static final int OFFSET = 40;// ����ƫ��
    private static final int CELL_WIDTH = 40;// �����
    public static final int BOARD_SIZE = 15;// ���̸���
    public static final int BT = 17; //���̸���+2��ȷ�������ϵ�1-15����Ͱ�1-15�����ʣ�Ҳ��֤�˱߽�������ж�
    public static final int CENTER = 8; //���ĵ�BOARD_SIZE / 2 + 1
    //��������Ϊ�������鼴boarddata�и�ֵ������
    public static final int BLACK = 1;//����
    public static final int WHITE = 2;//����
    public static final int BORDER = -1;//�߽�
    public static final int EMPTY = 0;//����������

    //����Ϊϵͳ��ѡ�������ģʽ�����ڵ�ǰ��Ŀ�����ð������£��������˻����ʽ�����ģʽ��ע�ͣ�����Ҳ�ɿ��ǻָ���չ
//    public static final int MANUAL = 0;// ˫��ģʽ
//    public static final int HALF = 1;// �˻�ģʽ
//    public static final int AUTO = 2;// ˫��ģʽ
    public static final int HUMAN = 3;//������
    public static final int COMPUTER = 4;//�������֣�����Ŀ��Ĭ�ϵ������ּ���һ������Ԫ���ʽ����ϵ�����ѡ����ʱʧЧ


    //history�洢�����ϵ������¼����ջ��ʽ�洢�������ӡ�����ʱ�ã�����Ϊstatic���ͣ�ȷ�����������ж����history����һ��
    // boardData�洢��ǰ��֣��ж�ĳλ���Ƿ����塢�������顢��������ʱ��  ���������������ʣ�
    private static Stack<Chess> history ;
    public static int[][] boardData; //��ǰ�����̾��棬EMPTY��ʾ���ӣ�BLACK��ʾ���ӣ�WHITE��ʾ���ӣ�BORDER��ʾ�߽�
    private int[] lastStep; //������һ�����ӵ㣬����Ϊ2�����飬��¼��һ�����ӵ�����꣬����ʱ��

    private static int currentPlayer = 0; // ��ǰ���ִ����ɫ��Ĭ��Ϊ����
    private static int computerSide = BLACK;// Ĭ�ϻ����ֺ�
    private static int humanSide = WHITE;  //�˳ְ�
    private static boolean isShowOrder = false; // ��ʾ����˳��Ĭ�ϲ���ʾ
    private static boolean isGameOver = true;   //��Ϸ�Ƿ����
    public static int initUser;// ����
    private static boolean isShowManual = false; //�Ƿ��ǻ����ף���������ʱ�ã����ڸ������׺���������Ļ����ӷ�ʽ��ͬ
    private boolean isAppendText = true;   //�����ұߵ��ı�����Ƿ�������ʾ

    private Chess AIGoChess;  //�������һ�����ӵ�
    private MustWinGo mustWinGo;  //��ʤ���������
    private int minx, maxx, miny, maxy; // ��ǰ������������ӵ���Сx�����x����Сy�����y��������С�������ӵ�ķ�Χ
    private int cx = CENTER, cy = CENTER;  // ���ĵ�

    //�չ��캯��
    public GobangPanel(){}
    //��area����ʾ��Ϣ�ı���Ϊ��������area�����캯������Ҫ��ʼ��������
    public GobangPanel(JTextArea area) throws Exception {
        boardData = new int[BT][BT];//��ʼ���������飬����Ϊ1-15�洢����״̬��0 16Ϊ�߽�
        for (int i = 0; i < BT; i++) {
            for (int j = 0; j < BT; j++) {
                boardData[i][j] = EMPTY;
                if (i == 0 || i == BT - 1 || j == 0 || j == BT - 1)
                    boardData[i][j] = BORDER;// �߽�
            }
        }
        history = new Stack<Chess>(); //��ʼ��������ʷ��¼
        lastStep = new int[2];   //��ʼ����һ�����ӵ�
        mustWinGo = new MustWinGo();  //��ʼ����ʤ���׶���
        this.area = area;
        addMouseMotionListener(mouseMotionListener); //����������¼�������
        addMouseListener(mouseListener);
        setPreferredSize(new Dimension(650, 700));  //����������ʾ��С
    }

    // �Ƿ���ʾ����˳�򣬸����û��ڽ����ϵĹ�ѡ��ѡ���������isShowOrder��Ӧȡ������ˢ�½�����ʾ
    public void toggleOrder() {
        isShowOrder = !isShowOrder;
        repaint();
    }

    // ����
    public void undo() {
        if (!history.isEmpty()) {  //���ж��Ƿ���������ʷ��¼
            if(history.size()==1&&initUser==COMPUTER) //��ֻ��Ĭ�ϵĵ���������Ԫ����ֱ�ӷ���
            {
                return;
            }
            Chess p1 = history.pop();  //��ջ�е������µ����Ӽ�¼
            boardData[p1.x][p1.y] = EMPTY;   //���ø�λ��Ϊ��
            if (!history.isEmpty()) {
                Chess chess = history.peek();
                lastStep[0] = chess.x;  //������һ�����Ӽ�¼lastStep
                lastStep[1] = chess.y;
            }
        } else {  //��historyΪ�գ�������һ�����Ӽ�¼lastStepΪ�߽�
            lastStep[0] = BORDER;
            lastStep[1] = BORDER;
        }
        togglePlayer();  //ת����ǰ���ӽ�ɫ
        repaint();  //ˢ�½���
        mustWinGo.goback();   //������Ŀ�ǰ���ʤ�����ߣ�������ʤ���׶���ҲҪ������Ӧ�ķ��ش���
    }


    // ���漰�������ʼ��
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        g2d.setStroke(new BasicStroke(2));
        g2d.setFont(new Font("April", Font.BOLD, 12));

        // ������ͼ
        ImageIcon icon = new ImageIcon("src/main/resources/image/background.jpg");
        g2d.drawImage(icon.getImage(), 0, 0, getSize().width, getSize().height, this);

        // ������
        drawBoard(g2d);
        // ����Ԫ����
        drawStar(g2d, CENTER, CENTER);
        drawStar(g2d, (BOARD_SIZE + 1) / 4, (BOARD_SIZE + 1) / 4);
        drawStar(g2d, (BOARD_SIZE + 1) / 4, (BOARD_SIZE + 1) * 3 / 4);
        drawStar(g2d, (BOARD_SIZE + 1) * 3 / 4, (BOARD_SIZE + 1) / 4);
        drawStar(g2d, (BOARD_SIZE + 1) * 3 / 4, (BOARD_SIZE + 1) * 3 / 4);

        // �����ֺ���ĸ
        drawNumAndLetter(g2d);
        if (!isShowManual) {   //�����Ǹ������ף�����������
            // ����ʾ�򣬼�����ƶ��ĺ�ɫԤѡ��
            drawCell(g2d, cx, cy);
            if (!isGameOver) {  // ��Ϸδ����
                for (Chess chess : history) {  // ����������
                    drawChess(g2d, chess.x, chess.y, chess.color);
                }

                if (isShowOrder) {  // ��˳��
                    drawOrder(g2d);
                }
                else {    // ����һ�����ӵ������ʾ
                    if (lastStep[0] > 0 && lastStep[1] > 0) {
                        g2d.setColor(Color.RED);
                        g2d.fillRect((lastStep[0] - 1) * CELL_WIDTH + OFFSET
                                        - CELL_WIDTH / 10, (lastStep[1] - 1) * CELL_WIDTH
                                        + OFFSET - CELL_WIDTH / 10, CELL_WIDTH / 5,
                                CELL_WIDTH / 5);

                    }
                }
            }
        }
        else {   //��������
            // ����������
            for (Chess chess : history) {
                drawChess(g2d, chess.x, chess.y, chess.color);
            }
            drawOrder(g2d);
        }
    }

    // ������
    private void drawBoard(Graphics g2d) {
        for (int x = 0; x < BOARD_SIZE; ++x) {   // ������
            g2d.drawLine(x * CELL_WIDTH + OFFSET, OFFSET, x * CELL_WIDTH
                    + OFFSET, (BOARD_SIZE - 1) * CELL_WIDTH + OFFSET);

        }
        for (int y = 0; y < BOARD_SIZE; ++y) {   // ������
            g2d.drawLine(OFFSET, y * CELL_WIDTH + OFFSET,
                    (BOARD_SIZE - 1) * CELL_WIDTH + OFFSET, y
                            * CELL_WIDTH + OFFSET);
        }
    }

    // ����Ԫ����
    private void drawStar(Graphics g2d, int cx, int cy) {
        g2d.fillOval((cx - 1) * CELL_WIDTH + OFFSET - 4, (cy - 1) * CELL_WIDTH
                + OFFSET - 4, 8, 8);
    }

    // �����ֺ���ĸ
    private void drawNumAndLetter(Graphics g2d) {
        FontMetrics fm = g2d.getFontMetrics();
        int stringWidth, stringAscent;
        stringAscent = fm.getAscent();
        for (int i = 1; i <= BOARD_SIZE; i++) {

            String num = String.valueOf(BOARD_SIZE - i + 1);
            stringWidth = fm.stringWidth(num);
            g2d.drawString(String.valueOf(BOARD_SIZE - i + 1), OFFSET / 4    // ������
                    - stringWidth / 2, OFFSET + (CELL_WIDTH * (i - 1))
                    + stringAscent / 2);

            String letter = String.valueOf((char) (64 + i));
            stringWidth = fm.stringWidth(letter);
            g2d.drawString(String.valueOf((char) (64 + i)), OFFSET    //����ĸ
                    + (CELL_WIDTH * (i - 1)) - stringWidth / 2, OFFSET * 3 / 4
                    + OFFSET + CELL_WIDTH * (BOARD_SIZE - 1)
                    + stringAscent / 2);
        }
    }

    // ������
    private static void drawChess(Graphics g2d, int cx, int cy, int color) {
        if (color == 0)
            return;
        int size = CELL_WIDTH * 5 / 6;
        g2d.setColor(color == BLACK ? Color.BLACK : Color.WHITE);
        g2d.fillOval((cx - 1) * CELL_WIDTH + OFFSET - size / 2, (cy - 1)
                * CELL_WIDTH - size / 2 + OFFSET, size, size);
    }

    // ��Ԥѡ��
    private void drawCell(Graphics g2d, int x, int y) {
        int length = CELL_WIDTH / 4;
        int xx = (x - 1) * CELL_WIDTH + OFFSET;
        int yy = (y - 1) * CELL_WIDTH + OFFSET;
        int x1, y1, x2, y2, x3, y3, x4, y4;
        x1 = x4 = xx - CELL_WIDTH / 2;
        x2 = x3 = xx + CELL_WIDTH / 2;
        y1 = y2 = yy - CELL_WIDTH / 2;
        y3 = y4 = yy + CELL_WIDTH / 2;
        g2d.setColor(Color.RED);
        g2d.drawLine(x1, y1, x1 + length, y1);
        g2d.drawLine(x1, y1, x1, y1 + length);
        g2d.drawLine(x2, y2, x2 - length, y2);
        g2d.drawLine(x2, y2, x2, y2 + length);
        g2d.drawLine(x3, y3, x3 - length, y3);
        g2d.drawLine(x3, y3, x3, y3 - length);
        g2d.drawLine(x4, y4, x4 + length, y4);
        g2d.drawLine(x4, y4, x4, y4 - length);
    }

    // ������˳��
    private void drawOrder(Graphics g2d) {
        if (history.size() > 0) {
            g2d.setColor(Color.RED);
            int i = 0;
            for (Chess chess : history) {
                int x = chess.x;
                int y = chess.y;
                String text = String.valueOf(i + 1);
                // ����
                FontMetrics fm = g2d.getFontMetrics();
                int stringWidth = fm.stringWidth(text);
                int stringAscent = fm.getAscent();
                g2d.drawString(text, (x - 1) * CELL_WIDTH + OFFSET - stringWidth / 2,
                        (y - 1) * CELL_WIDTH + OFFSET + stringAscent / 2);
                i++;
            }
        }
    }

    // �������ף�����manualPathΪ���ֵ�����·��
    public void drawManual(String manualPath) {
        // ��һ������ȡ���ײ�����
        File manual = new File("manual/" + manualPath); //������ӦFile����
        int[][] records = new int[230][3]; //���������ڵ����Ӽ�¼��ÿ����¼�� ����˳�� x y ��ʽ�洢
        int lineNum = 0,x,y; //lineNum���ڶ�λ�ļ��е���һ�У��ļ���ȡ������lineNum����ʾ���ײ���
        try {
            BufferedReader br = new BufferedReader(new FileReader(manual));
            String line;
            String[] content;

            while ((line = br.readLine()) != null) {  //��ȡ�ļ����ļ���ÿ���� ����˳�� xy ����ʽ�洢���硰1 H8��
                content = line.split(" "); // ��ÿ�������Կո���룬��ȡ����˳������������
                int order = Integer.parseInt(content[0]); //����˳��
                if (order % 2 ==0) {  // ��������˳���жϺڰ���
                    records[lineNum][0] = 2;
                } else {
                    records[lineNum][0] = 1;
                }
                x = content[1].charAt(0) - 64;  //����ȡ����x����ת������ĸת��Ϊ����
                y = 16 - Integer.parseInt(content[1].substring(1));  //��ȡy����
                records[lineNum][1] = x;
                records[lineNum][2] = y;
                lineNum++;
            }
            br.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        //�ڶ����������������
        reset(); //�������
        repaint();

        //��������������������ݣ�ˢ�½���
        for (int j = 0; j < lineNum; j++) {  // ��������������������boradData�����ݣ���Ϊ���̻����ӵĸ�����boardData������
            x = records[j][1];
            y = records[j][2];
            boardData[x][y] = records[j][0];
        }
        setHistory(records,lineNum); // ����������������history����
        isShowManual = true;   // ������ʾ���ױ�ǣ���������ˢ�½���ʱ����Ŀ��ѡ�������׶�������������
        isShowOrder = true;    // ��ʾ����˳��
        MainUI.clearText();    // ��ս����Ҳ��ı������ʾ����

        repaint();
    }


    // ��ʼ��Ϸ������Ϊ���֣���ĿĬ������Ϊ����
    public void startGame(int initUser) {
        this.initUser = initUser;   //��ʼ������
        this.reset();   //���ø���������
        area.setText("");  //����Ҳ��ı�����ʾ����
        isGameOver = false;  //���ñ���
        if (!(MainUI.orderBtn.isSelected()))  //���ݽ������Ƿ�ѡ��ʾ����˳�����ָ�
            isShowOrder = false;
        isShowManual = false;  //���÷Ǹ�������
        isAppendText = true;   //�����ұ��ı������ʾ

        mustWinGo.startGame();  //��ʤ���׶�����Ҳ������س�ʼ��

        if (initUser == 4) {  // ����Ϊ���ԣ�����ĿĬ�ϵ�������
            currentPlayer = Chess.BLACK; // Ĭ�Ϻ�������
            Chess chess = mustWinGo.AIGo(); //�ӱ�ʤ�����л�ȡ���ӵ�����λ��
            putChess(chess.x, chess.y);  //���ص�����Ԫ���꣬Ĭ�ϵ�һ����������
            minx = maxx = miny = maxy = chess.x;  //����������Χ����ǰ�����Ŀ�ʼ
            MainUI.appendText("���壺 ��" + (char) (64 + 8) + (16 - 8) + "��\n");  //�Ҳ��ı�����ʾ����
            System.out.println("----�������----");
        } else {
            currentPlayer = Chess.WHITE;
        }
        repaint();

    }

    // ����ƶ��¼��������������������ƶ�λ�ã���ʾԤѡ��
    private MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
        public void mouseMoved(MouseEvent e) {
            int tx = Math.round((e.getX() - OFFSET) * 1.0f / CELL_WIDTH) + 1;
            int ty = Math.round((e.getY() - OFFSET) * 1.0f / CELL_WIDTH) + 1;
            if (tx != cx || ty != cy) {
                if (tx >= 1 && tx <= BOARD_SIZE && ty >= 1
                        && ty <= BOARD_SIZE) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    repaint();
                } else
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                cx = tx;
                cy = ty;
            }
        }
    };

    // ������¼�����������
    private MouseListener mouseListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (isGameOver) {
                JOptionPane.showMessageDialog(GobangPanel.this, "�뿪ʼ����Ϸ��");
                return;
            }
            // ��ȡ��������
            int x = Math.round((e.getX() - OFFSET) * 1.0f / CELL_WIDTH) + 1;
            int y = Math.round((e.getY() - OFFSET) * 1.0f / CELL_WIDTH) + 1;

            if (cx >= 1 && cx <= BOARD_SIZE && cy >= 1 && cy <= BOARD_SIZE) {
                if (putChess(x, y)) {  //����
                    if (isAppendText) {
                        MainUI.appendText("���壺 ��" + (char) (64 + x) + (16 - y) + "��\n");
                    }
                    mustWinGo.PlayerGo(x, 16 - y);  //��ʤ�����������һ������
                                                        //����16-y�Ĳ���������java������ԭ�������Ͻǣ���������ԭ�������½�
                    System.out.println("----�������----");

                    AIGoChess = mustWinGo.AIGo(); //���ݰ��ӵ�����λ�ã��ӱ�ʤ�����л�ȡ���ӵ���һ���߷�
                    if (AIGoChess != null){
                        putChess(AIGoChess.x, 16 - AIGoChess.y);
                    }
                    else { //���ݷ��ؽ���жϰ������������������δ��¼��λ�ã������£�������ĿĿǰ�Ǹ��������߷��������ģ�����ʾ��ʾ��Ϣ�����û����£��������봫ͳ�����㷨���ٸĽ���
                        System.out.println("�������´����ˣ������");
                        MainUI.appendText("�������´����ˣ������");
                    }

                    if (isAppendText) {
                        MainUI.appendText("���壺 ��" + (char) (64 + AIGoChess.x) + (AIGoChess.y) + "��\n");
                    }
                    System.out.println("----�������----");
                }
            }
        }
        //}
    };

    // ���Ӻ���
    private boolean putChess(int x, int y) {
        if (boardData[x][y] == EMPTY) {  //�����ж�����λ���Ƿ�Ϊ��
            // ����������Χ����
            minx = Math.min(minx, x); //����������Χ����
            maxx = Math.max(maxx, x);
            miny = Math.min(miny, y);
            maxy = Math.max(maxy, y);
            boardData[x][y] = currentPlayer;  //����Ϊ��ǰ���������
            history.push(new Chess(x, y, currentPlayer));  //ѹ����ʷ��¼ջ
            togglePlayer();  //ת�������ɫ
            System.out.println("��" + (char) (64 + x) + (16 - y) + "��");

            lastStep[0] = x;// ������һ�����ӵ�
            lastStep[1] = y;
            repaint();
            int winSide = isGameOver(initUser);// �ж��վ�
            if (winSide > 0) {  //>0��ʾ���վ�
                if (winSide == humanSide) {
                    MainUI.appendText("�׷�Ӯ�ˣ�\n");
                    JOptionPane.showMessageDialog(GobangPanel.this, "�׷�Ӯ�ˣ�");
                } else if (winSide == computerSide) {
                    MainUI.appendText("�ڷ�Ӯ�ˣ�\n");
                    JOptionPane.showMessageDialog(GobangPanel.this, "�ڷ�Ӯ�ˣ�");
                } else {
                    MainUI.appendText("˫��ƽ�֣�\n");
                    JOptionPane.showMessageDialog(GobangPanel.this, "˫��ƽ��");
                }

                return false;
            }

            return true;
        }
        return false;
    }

    // ת���ڰ���
    void togglePlayer() {
        currentPlayer = 3 - currentPlayer;
    }

    //��������״̬
    public void reset() {
        for (int i = 1; i < BT - 1; i++)
            for (int j = 1; j < BT - 1; j++) {
                boardData[i][j] = EMPTY;  // ����boardData
            }
        history.clear();  // ������Ӽ�¼ջ
    }

    //�ж�����Ƿ����  return 0��������  1������Ӯ  2������Ӯ  3��ƽ��
    public int isGameOver(int initUser) {
        if (!history.isEmpty()) {
            int color;  //�жϵ�ǰ���ӷ�

            if (initUser == 4) {   //��������
                color = (history.size() % 2 == 1) ? Chess.BLACK : Chess.WHITE;
            } else {
                color = (history.size() % 2 == 1) ? Chess.WHITE : Chess.BLACK;
            }
            Chess lastStep = history.peek();  //��ȡ��һ�����Ӽ�¼
            int x = lastStep.getX();
            int y = lastStep.getY();

            // �ж��ĸ������Ƿ��γ����������ϣ����򷵻ص�ǰӮ��
            if (check(x, y, 1, 0, color) + check(x, y, -1, 0, color) >= 4 ||
                    check(x, y, 0, 1, color) + check(x, y, 0, -1, color) >= 4 ||
                    check(x, y, 1, 1, color) + check(x, y, -1, -1, color) >= 4 ||
                    check(x, y, 1, -1, color) + check(x, y, -1, 1, color) >= 4) {
                return color;
            }

        }

        // ������
        for (int i = 0; i < BOARD_SIZE; ++i) {
            for (int j = 0; j < BOARD_SIZE; ++j)
                if (boardData[i][j] == EMPTY) {
                    return 0;
                }
        }
        return 3;  //ƽ��
    }

    // ������ͬ��ɫ���ӵ�������xyΪ��ǰ���꣬dx��dy��ʾ��Ӧ������dx1dy0�����ң�dx0dy1���ϣ�dx1dy1���ϵ�
    private int check(int x, int y, int dx, int dy, int color) {
        int sum = 0;
        for (int i = 0; i < 4; ++i) {
            x += dx;
            y += dy;
            if (x < 1 || x > BOARD_SIZE || y < 1 || y > BOARD_SIZE) {
                break;
            }
            if (boardData[x][y] == color) {
                sum++;
            } else {
                break;
            }
        }
        return sum;  //������������
    }

    // �����Ӽ�¼�Զ�ά�������ʽ���������ӵ�����˳�򷵻أ���������ʱ��
    public int[][] getHistory() {
        int length = history.size();
        int[][] array = new int[length][2];
        for (int i = 0; i < length; i++)
            for (int j = 0; j < 2; j++) { //�����Ӽ�¼����������浽��ά������
                Chess p = history.get(i);
                array[i][0] = p.getX();
                array[i][1] = p.getY();
            }
        return array;
    }


    //���浱ǰ��ֵ������ļ���
    public void writeManual () throws IOException {
        int[][] temp = getHistory();  //��ȡ������ʷ��¼
        SimpleDateFormat ma = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");  //�����ļ����Ա���ʱ��Ϊ��
        Date time = new Date();
        String path =  "manual/" + ma.format(time) + ".txt";
        File file = new File(path);  // ���������ļ�

        if(!file.exists()){
            //��û��manualĿ¼�����ȵõ��ļ����ϼ�Ŀ¼���������ϼ�Ŀ¼���ٴ����ļ�
            file.getParentFile().mkdir();
            try {
                //�����ļ�
                file.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                for (int i = 0; i < temp.length; i++) {    //�� ����˳�� x y ��ʽ���� ����˳��Ϊ������ʾ���壬��֮����
                    bw.write((i+1) + " " + (char)(64 + temp[i][0]) + (16 - temp[i][1]) + "\n");
                }
                bw.flush();
                bw.close();
                JOptionPane.showMessageDialog(this,"����ɹ���");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // ���ݴ����array�������������¸�ֵ���Ӽ�¼historyջ����������ʱ��
    public void setHistory(int[][] array,int length) {
        Chess p;
        for (int i = 0; i < length; i++) {
            int x = array[i][1],y = array[i][2];
            p = new Chess(x,y,boardData[x][y]);
            history.push(p);
        }
    }

}
