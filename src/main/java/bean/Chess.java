package bean;

// �����ࣺ����������Chess��ʽ�洢����Ҫ����x��y��color�������ԣ��������������ɫ
public class Chess{
	public static final int BLACK = 1; // ������ʾ������ɫ�ĳ�����1��ʾ���ӣ�2��ʾ���ӣ�0��ʾ�ô�Ϊ��
	public static final int WHITE = 2;
	public static final int EMPTY = 0;
	protected int x; //x���꣬������int�ʹ洢�����ڼ�¼�����滭���ӣ����̽�������ʾ����A-OӢ����ĸ��
					//x+64���õ���Ӧ��ĸ��ASC���룬��ת���ɶ�Ӧ��ĸ��ʾ
	protected int y; //��Ӧ�����ϵ�������
	protected int color; //������ɫ

	// ���캯��
	public Chess(int x, int y, int color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}

	// ֻ���������������Ĺ��캯��
	public Chess(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// ��ȡx����
	public int getX() {
		return x;
	}

	// ����x����
	public void setX(int x) {
		this.x = x;
	}

	// ��ȡy����
	public int getY() {
		return y;
	}

	// ����y����
	public void setY(int y) {
		this.y = y;
	}

	// ��ȡ������ɫ
	public int getColor() {
		return color;
	}

	// ����������ɫ
	public void setColor(int color) {
		this.color = color;
	}

	// ��գ�����������ɫΪ�գ����ô�����
	public void reset() {
		color = EMPTY;
	}

	// �жϸô��Ƿ�Ϊ�գ����򷵻�true����֮����false
	public boolean isEmpty() {
		return color == EMPTY ? true : false;
	}

	// ��ӡchess����
	public String toString() {
		return "Chess{" +
				"x=" + x +
				", y=" + y +
				", color=" + color +
				'}';
	}
}
