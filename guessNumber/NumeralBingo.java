// package experimentation;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author Weng
 * @version 1.0
 * 
 * function:
 * 1.When you open this programme and push the button "开始", the screen will display: "请输入一个大于0小于100的整数"
 *   and this program will generate a random integer number ranging from 1 to 100 (1=<n<100). Both of these 
 *   tasks will be complished by inner class-ButtonListener
 * 2.When you push the "enter" key ,this program will receive the number you typed on the screen  and output 
 *   "比正确数字大了" or "比正确数字小了" or "恭喜你答对了，你共用了N次" to the screen ,or pop a dialog "恭喜你答对了，你
 *   共用了N次，创造了新的世界记录!请在屏幕上写上你的名字" . All the comparison and output tasks will be complished
 *   by inner class-EnterKeyListener
 */


public class NumeralBingo {
	
	public static void main(String[] args){
		// TODO 1.initialize this class
		new NumeralBingo();

	}//main

	//field:
	int level=100;
	int randomNumber;      //the right answer
	int guessCounter;    //the times you have guessed
	int bestScore;         //i.e the record of history , for inner class-EnterKeyListener only
	String receiveFromScr; //receive input from screen , for inner class-EnterKeyListener only
	boolean cls;           //cls is short for CLear Screen , which variable is used for inner 
	                       //class-EnterKeyListener and inner class ButtonListener only
	//boolean gameOver;      //if game is over,set gameOver to true
	boolean breakRecord;   //used for record-break only
	
	JFrame      jf   = new JFrame("NumeralBingo");
	JButton     jb   = new JButton("スタ一ト");
	JTextField  jtf  = new JTextField();
	JDialog     jd   = new JDialog(jf);   //for "帮助" only
	JTextArea   jta  = new JTextArea("请点击スタ一ト按钮\n");
	JScrollPane jsp  = new JScrollPane(jta);
	Container   c  ;
	
	JMenuBar    jmb  = new JMenuBar();
	JMenu       jm1  = new JMenu("选项");
	JMenu       jm2  = new JMenu("帮助");
	JMenuItem   jmi1 = new JMenuItem("查看记录");
	JMenu       jm1_1  = new JMenu("等级");
	JMenuItem   jmi2 = new JMenuItem("1级");
	JMenuItem   jmi3 = new JMenuItem("2级");
	JMenuItem   jmi4 = new JMenuItem("3级");
	JMenuItem   jmi5 = new JMenuItem("退出");
	JMenuItem   jmi6 = new JMenuItem("游戏说明");
	//constructor:
	public NumeralBingo(){
	  
		jtf.setFont(new Font("result",Font.PLAIN,18));
		jtf.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				jtf.setText(null);
			}
		});
		jtf.addKeyListener(new EnterKeyListener());
		
		jta.setEditable(false);
		
		cls=true;
		jb.addActionListener(new ButtonListener());
		jb.setFont(new Font("button",Font.PLAIN,18));
	
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		c=jf.getContentPane();
		c.add(jtf,BorderLayout.NORTH);
		c.add(jsp);
		
		c.add(jb,BorderLayout.SOUTH);
		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jmi1.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			try{
				BufferedReader brForDisplay=new BufferedReader(new FileReader("record.txt"));
				String times=brForDisplay.readLine();
				String name=brForDisplay.readLine();
				jta.append("目前记录是"+name+"保持的"+times+"次\n");
				brForDisplay.close();
			}catch (Exception exc){
				jta.setText("目前没有记录\n");
			}
			}
			
		});
		jmi2.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				level=100;
				jta.setText("请点击スタ一ト按钮\n");
			}
			
		});
		jmi3.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				level=1000;
				jta.setText("请点击スタ一ト按钮\n");
			}
			
		});
		jmi4.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				level=10000;
				jta.setText("请点击スタ一ト按钮\n");
			}
			
		});
		jmi5.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
			
		});
		jm1_1.add(jmi2);
		jm1_1.add(jmi3);
		jm1_1.add(jmi4);
		jm1.add(jmi1);
		jm1.add(jm1_1);
		jm1.addSeparator();
		jm1.add(jmi5);
        
		jmi6.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JTextArea jtaForJd =new JTextArea();
				jtaForJd.setText("\n   在开始游戏前，正确答案是0...\n   明白我的意思了吧@_@!!");
				jd.add(jtaForJd);
				jd.setSize(200,100);
				jd.setLocation(350,350);
				jd.setVisible(true);
				
			}
			
		});
		jm2.add(jmi6);
		
		jmb.add(jm1);
		jmb.add(jm2);
		jf.setJMenuBar(jmb);
		
		jf.setSize(400,300);
		jf.setLocation(300,300);
		jf.setVisible(true);
		
		
	}
	
	//method:
	
	//inner class:
	
	public class ButtonListener implements ActionListener{

		public void actionPerformed(ActionEvent ae){
			/* TODO 
			 * 1.generate a random number and give it to randomNumber
			 * 2.display : 请输入一个大于0小于100的整数
			 */
			
			//1
			  randomNumber=(int)(Math.random()*level+1);
			//2
			  jta.setText("请输入一个大于等于0小于等于"+level+"的整数\n");
			  cls=true;
			  //gameOver=false;
			  breakRecord=false;
			  guessCounter=0;
		}//actionPerformed()
	}//ButtonListener
	
	public class EnterKeyListener extends KeyAdapter{
	
		public void keyPressed(KeyEvent e) {
			/* TODO 
			 * 1.compare the number you put with the randomNumber , output
			 *   the result on the screen
			 * 2.if the player breaks the record , put the new record into the
			 *   text file-record.txt
			 */
		 if (e.getKeyCode()!=KeyEvent.VK_ENTER){
			 if (cls==true){
				 jtf.setText(null);
				 cls=false;
			 }
			 return;
		 }
		   receiveFromScr=jtf.getText();  
		   guessCounter++;
			//1  
		 if (breakRecord==false&&!receiveFromScr.equals(String.valueOf(randomNumber))){   //wrong guess
			 int integerizedReceiveFromScr=0;
			try{	   
			 integerizedReceiveFromScr=Integer.parseInt(receiveFromScr);
			}catch(Exception ex){
			 jta.append("输入错了，请重新输入\n");
			 guessCounter--;
			 return;
			}
			if (integerizedReceiveFromScr>randomNumber){
				jta.append("您猜的第"+guessCounter+"次的数字"+receiveFromScr+"比正确数字大了\n"); 	  
			}else{
				jta.append("您猜的第"+guessCounter+"次的数字"+receiveFromScr+"比正确数字小了\n"); 
			}
			cls=true; 
		 }else if (breakRecord==false){                    //  Bingo!!
			//2
			 
			 
		   try{
			    
			    BufferedReader brForRecordText=new BufferedReader(new FileReader("record.txt"));
			    bestScore=Integer.valueOf(brForRecordText.readLine()).intValue();
			    brForRecordText.close();
			}catch(FileNotFoundException fnfe){
				bestScore=100;   //except for idiot...	
			}catch(Exception exc){
				
			}finally{
			
			 try{
			  if (guessCounter<bestScore){  //break the record
				jta.append("恭喜你答对了，你共用了"+guessCounter+"次，创造了新的世界记录!\n请在屏幕上写上你的名字\n");
				//jd.setVisible(true);
				breakRecord=true;
				
			  }else {                       //not break the record
			    BufferedReader brForDisplay=new BufferedReader(new FileReader("record.txt"));
				String times=brForDisplay.readLine();
				String name=brForDisplay.readLine();
				jta.append("恭喜你答对了，你共用了"+guessCounter+"次!\n目前记录是"+name+"保持的"+times+"次\n");
				brForDisplay.close();
			  }
			 }catch(Exception exc){
				 
			 }//inner try-catch
			
		   }//finally
		
		 }else if(breakRecord==true){
			 
			try{
				PrintWriter bw=new PrintWriter(new FileWriter("record.txt"));
				bw.println(String.valueOf(--guessCounter));
				//when you type your name and press the key "enter",receiveFromScr will receive your name 
				bw.println(receiveFromScr);  //write name to record.txt
				bw.close();
				jta.append("恭喜你 ，"+receiveFromScr+"，您的记录保存\n请点击スタ一ト按钮\n");
			}catch(Exception exc){
				//Logically,NoFileFoundException will not happen here!
			}
		 }
		 
		 jtf.setText(null);
		 
		}//keyPressed()
	
	}//EnterKeyListener

}
