import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

class Myframe extends JFrame {
public static Object obj = new Object();
public final static JTextField[][] filed = new JTextField[9][9];

public Myframe() {
	for (int a = 0; a < 9; a++) {
	 for (int b = 0; b < 9; b++) {
		filed[a][b] = new JTextField();
		filed[a][b].setText("");
	 }
	}
	JPanel jpan = new JPanel();
	jpan.setLayout(new GridLayout(9, 9));
	for (int a = 8; a > -1; a--) {
	 for (int b = 0; b < 9; b++) {
		jpan.add(filed[b][a]);
	 }
	}
	add(jpan, BorderLayout.CENTER);
	JPanel jpb = new JPanel();
	JButton button1 = new JButton("calc");
	JButton button2 = new JButton("close");
	jpb.add(button1);
	jpb.add(button2);
	button1.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent event) {
	synchronized (obj) {
	 for (int a = 0; a < 9; a++) {
		for (int b3 = 0; b3 < 9; b3++) {
		 int pp = 0;
		 if (!(filed[a][b3].getText().trim().equals(""))) {
			pp = Integer.parseInt(filed[a][b3].getText()
					.trim());
			Calculate.b[a][b3] = pp;
		 }
		}
	}
	}
	synchronized (obj) {
		new Thread(new Calculate()).start();
	}
	}
	});
	button2.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			System.exit(0);
		}
	});
	add(jpb, BorderLayout.SOUTH);
}
}

public class Sudoku {
public static void main(String[] args) {
	Myframe myf = new Myframe();
	myf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	myf.setTitle("sudoku");
	myf.setSize(500, 500);
	myf.setVisible(true);
}
}

class Calculate implements Runnable {
	public static boolean[][] boo = new boolean[9][9];
	public static int upRow = 0;
	public static int upColumn = 0;
	public static int[][] b = new int[9][9];

	public static void flyBack(boolean[][] judge, int row, int column) {
		int s = column * 9 + row;
		s--;
		int quotient = s / 9;
		int remainder = s % 9;
		if (judge[remainder][quotient]) {
			flyBack(judge, remainder, quotient);
		} else {
			upRow = remainder;
			upColumn = quotient;
		}
	}

  public static void arrayAdd(ArrayList<Integer> array, TreeSet<Integer> tree) {
	for (int z = 1; z < 10; z++) {
		boolean flag3 = true;
		Iterator<Integer> it = tree.iterator();
		while (it.hasNext()) {
			int b = it.next().intValue();
			if (z == b) {
				flag3 = false;
				break;
			}
		}
		if (flag3) {
			array.add(new Integer(z));
		}
		flag3 = true;
	}
	}

  public static ArrayList<Integer> assume(int row, int column) {
	ArrayList<Integer> array = new ArrayList<Integer>();
	TreeSet<Integer> tree = new TreeSet<Integer>();
	for (int a = 0; a < 9; a++) { 
		if (a != column && b[row][a] != 0) {
			tree.add(new Integer(b[row][a]));
		}
	}
	for (int c = 0; c < 9; c++) {
		if (c != row && b[c][column] != 0) {
			tree.add(new Integer(b[c][column]));
		}
	}
	for (int a = (row / 3) * 3; a < (row / 3 + 1) * 3; a++)
	{
	  for (int c = (column / 3) * 3; c < (column / 3 + 1) * 3; c++) {
		if ((!(a == row && c == column)) && b[a][c] != 0) {
			tree.add(new Integer(b[a][c]));
		}
	}
	}
	arrayAdd(array, tree);
	return array;
	}

  public void run() {
	int row = 0,column = 0; 
	boolean flag = true;
	for (int a = 0; a < 9; a++) {
	 for (int c = 0; c < 9; c++) {
		if (b[a][c] != 0) {
			boo[a][c] = true;
		} else {
			boo[a][c] = false;
		}
	}
	}
	ArrayList<Integer>[][] utilization = new ArrayList[9][9];
	while (column < 9) {
		if (flag == true) {
			row = 0;
		}
	while (row < 9) {
	if (b[row][column] == 0) {
	if (flag) {
		ArrayList<Integer> list = assume(row, column);//
		utilization[row][column] = list;
	}
	if (utilization[row][column].isEmpty()) {
		flyBack(boo, row, column); 
		row = upRow;
		column = upColumn;
		b[row][column] = 0;
		column--;
		flag = false;
		break;
	} else {
		b[row][column] = utilization[row][column].get(0);
		utilization[row][column].remove(0);
		flag = true;
		judge();
	}
	} else {
		flag = true;
	}
	row++;
	}
	column++;
  }
 }
  public void judge()
  {
	boolean r = true;
	for (int a1 = 0; a1 < 9; a1++) {
	  for (int b1 = 0; b1 < 9; b1++) {
		if (r == false) {
			break;
		}
		if (b[a1][b1] == 0) {
			r = false;
		}
	}
	}
	if (r) { 
	  for (int a1 = 0; a1 < 9; a1++) {
		for (int b1 = 0; b1 < 9; b1++) {
			Myframe.filed[a1][b1].setText(b[a1][b1]
					+ "");
		}
	}
	}
  }
}