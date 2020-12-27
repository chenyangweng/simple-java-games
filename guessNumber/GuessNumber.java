// package experimentation;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.jar.JarFile;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GuessNumber extends JFrame {

	/**
	 * @param args
	 */
	JPanel jp=new JPanel();
	JTextField tf=new JTextField();
	JTextArea ta=new JTextArea();
	JScrollPane cp=new JScrollPane(ta);
	JButton jb=new JButton("开局");
	
	JMenuBar mb=new JMenuBar();
	JMenu mselect=new JMenu("Select");
	JMenu mhelp=new JMenu("Help");
	
	JMenuItem irenew=new JMenuItem("Renewstart");
	JMenuItem irecord=new JMenuItem("LookRecord");
	JMenu igrade=new JMenu("Grade");
	
	JMenuItem elementary=new JMenuItem("elementary");
	JMenuItem middleGrade=new JMenuItem("MiddleGrade");
	JMenuItem highGrade=new JMenuItem("HighGrade");
	
	JMenuItem iexit=new JMenuItem("Exit");
	JMenuItem igame=new JMenuItem("Gameexplaim");
	
	int count;
	int number;
	String name;
	int oldNumber;
	boolean isEnd;
	int grade=100;
	
	/**
	 * GuessNumber
	 * @param title
	 */
	
	GuessNumber(String title){
		super(title);
		init();
	}
	
	/**
	 * init
	 */
	void init(){
		this.setJMenuBar(mb);
		mb.add(mselect);
		mb.add(mhelp);
		
		mselect.add(irenew);
		mselect.add(irecord);
		mselect.add(igrade);
		mselect.addSeparator();
		igrade.add(elementary);
		igrade.add(middleGrade);
		igrade.add(highGrade);
		mselect.add(iexit);
		mhelp.add(igame);
		cp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		ta.setBackground(jp.getBackground());
		
		Container c=getContentPane();
		jp.add(jb);
		c.add(tf,BorderLayout.NORTH);
		c.add(cp,BorderLayout.CENTER);
		c.add(jp,BorderLayout.SOUTH);
		tf.setEditable(false);
		tf.setText("请点击开局开始游戏");
		
		irenew.addActionListener(new MenuItemListener());
		elementary.addActionListener(new MenuItemListener());
		middleGrade.addActionListener(new MenuItemListener());
		highGrade.addActionListener(new MenuItemListener());
		iexit.addActionListener(new MenuItemListener());
		
		
		jb.addActionListener(new Buttonlistener());
		tf.addActionListener(new Filelistener()); 
		
		this.setSize(400,300);
		this.setVisible(true);
		creatRecord();
		readRecord();	
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});	
	}

	
	/**
	 * @return
	 */
	
	public void go(){
		jb.setEnabled(false);
		tf.setText("");
		tf.setEditable(true);
		number=getNumber();
		isEnd=false;
		count=0;
		tf.requestFocus();
		this.setResizable(true);
	}
	public int getNumber(){
		 number=(int)(Math.random()*grade)+1;
		 System.out.println(number);
		 return number;
	}
	/**
	 * @param name
	 * @param count
	 */
	public void saveRecord(String name,int count){
		File f=new File("record.txt");
		try{
			PrintWriter pw=new PrintWriter(new FileWriter(f));
			pw.println(name);
			pw.println(count);
			pw.close();
		}catch(IOException ex){
			ex.printStackTrace();
			System.exit(0);
		}
	}
	/**
	 * 
	 */
	public void readRecord(){
		File f=new File("record.txt");
		try{
			BufferedReader br=new BufferedReader(new FileReader(f));
			String s=br.readLine();
			oldNumber=Integer.parseInt(s);
			name=br.readLine();
			br.close();
		}catch(IOException ex){
			ex.printStackTrace();
			System.exit(0);
		}
	}
	public void creatRecord(){
		File f=new File(System.getProperty("record.text"));
		if(!f.exists()){
			try{
				f.createNewFile();
				PrintWriter pw=new PrintWriter(new FileWriter(f));
				pw.println("10");
				pw.println("Alice");
				pw.close();
			}catch(java.io.FileNotFoundException e){	
				e.printStackTrace();
				System.exit(0);
			}catch(IOException ex){
				ex.printStackTrace();
				System.exit(0);
		}
	}
	}
	/**
	 * @author hp
	 *
	 */
	class Buttonlistener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成方法存根
			go();
		}
		
	}
	
	/**
	 * @author hp
	 *
	 */
	class Filelistener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成方法存根
			int n=0;
			if(!isEnd)
			   {
			     String s=tf.getText();
			      try
			          {
				         n=Integer.parseInt(s);
				            count++;
			          }
			         catch(Exception ex)
			          {
				         ta.append("\n请输入数字,谢谢合作");
			           }
			if(n==number)
			   {
				ta.append("\n恭喜您猜对了，您所猜的次数是："+count);
				tf.setText("");
				return;
			   }
			if(n>number)
			   {
				ta.append("\n您第"+count+"次输入的数字是"+n+"您猜的数字偏大");
				tf.setText("");
				return;
			   }
			if(n<number)
			   {
				ta.append("\n您第"+count+"次输入的数字是"+n+"您猜的数字偏小");
				tf.setText("");
				return;
			   }
	      }else{
				ta.append("\n恭喜您猜对了，所花的次数为"+count);
				tf.setText("");
				tf.setEnabled(false);
				jb.setEnabled(true);
				   if(count<=oldNumber)
				   {					
					   ta.append("\n您破纪录了，请在文本框输入您的姓名");
					   tf.requestFocus();
					   tf.setEnabled(true);
					   jb.setEnabled(false);
					   isEnd = true;			
			       }
		       }
	     }
	}
	class MenuItemListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			JMenuItem jmi=(JMenuItem)e.getSource();
			if(jmi==irenew){
				go();
			}
			if(jmi==iexit){
				System.exit(0);
			}
			if(jmi==elementary){
				grade=100;
				go();
			}
			if(jmi==middleGrade){
				grade=1000;
				go();
			}
			if(jmi==highGrade){
				grade=10000;
				go();
			}
			
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根
        GuessNumber gn=new GuessNumber("猜数字");
	}

}
