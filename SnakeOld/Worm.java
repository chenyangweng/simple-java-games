// package experimentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.ListIterator;
import java.awt.*;
import javax.swing.*;


/**
 * @author weng
 * last modified time : 21/03/08
 *
 */
public class Worm extends JFrame implements Runnable{

	//field
	GridButton[][] jbArr ;     // make the buttons in this JFrame a matrix
	LinkedList<BodyPartPosition>  wormBody;   // keep the information of the position of all body part of this worm 
	boolean     stopNow=true;
	boolean     breakWallEnable=false;
	int frequence;           // the move frequence of this worn. (times/s) 
	int deltaRow=0;
	int deltaCol=1;
	int score=0;
	JTextField jtf_score=new JTextField("0");     //for score display
	JTextField jtf_frequence=new JTextField("");   //for frequence display
	ChangeDirection changeDirection=new ChangeDirection();  //KeyListener
	Thread t;   
	
	//constructor
	public Worm(int rowNum, int collumnNum , int f){
		super("Worm,worm...");
		frequence=f;
		jtf_frequence.setText(""+f);
		
		Container c = this.getContentPane();
		JPanel    jp = new JPanel();
		JPanel    jpNorth=new JPanel();
		JButton   jb1 = new JButton("SART");
		JButton   jb2 = new JButton("STOP");
		JButton   jb3 = new JButton("FASTER");
		JButton   jb4 = new JButton("SLOWER");
		JMenuBar  jmb = new JMenuBar();
		JMenu     jm1 = new JMenu("File");
		JMenuItem jmi1= new JMenuItem("New game");
		JMenuItem jmi2= new JMenuItem("Wall break enable("+!breakWallEnable+")");
		
		jmi1.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			    resetWorm();
			}
			
		});
		jmi2.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent ae) {
				// TODO Auto-generated method stub
				breakWallEnable=!breakWallEnable;
				((JMenuItem)ae.getSource()).setText("Wall break enable("+!breakWallEnable+")");
			}
			
		});
		
		jm1.add(jmi1);
		jm1.add(jmi2);
		jmb.add(jm1);
		
		jb1.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
		        stopNow=false;
		        t.interrupt();
			}
		});
		jb2.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				stopNow=true;
				t.interrupt();
			}
		});
        jb3.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				if (frequence==0){
					stopNow=false;
					t.interrupt();
				}		
				frequence++;
				jtf_frequence.setText(""+frequence);
			}
		});
        jb4.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				if (frequence==0){   
					stopNow=true;
					t.interrupt();
					return;
				}
				frequence--;
				jtf_frequence.setText(""+frequence);
			}
		});
		
        jb1.addKeyListener(changeDirection);
        jb3.addKeyListener(changeDirection);
		jb4.addKeyListener(changeDirection);
		jp.setLayout(new GridLayout(rowNum,collumnNum));
		
		jbArr = new GridButton[rowNum][collumnNum];
		for (int i=0;i<rowNum;i++){
			for (int j=0;j<collumnNum;j++){
				jbArr[i][j] = new GridButton();
				jbArr[i][j].setBackground(Color.white);
				jp.add(jbArr[i][j]);
			}
		}
		wormBody = new LinkedList(); 
		for (int i=0;i<4;i++){
			wormBody.add(new BodyPartPosition(rowNum/2,collumnNum/7-i));
		}

		this.randomBean(); //set a random bean
		
		jpNorth.setLayout(new GridLayout(1,8));
		jpNorth.add(jb1);
		jpNorth.add(jb2);
		jpNorth.add(new Label("f(Hz):"));
		jtf_frequence.setEditable(false);
		jtf_frequence.setFont(new Font("score",Font.BOLD,18));
		jtf_frequence.setBackground(Color.WHITE);
		jtf_frequence.setHorizontalAlignment(JTextField.RIGHT);
		jpNorth.add(jtf_frequence);
		jpNorth.add(new Label("Score:"));
		jtf_score.setEditable(false);
		jtf_score.setFont(new Font("score",Font.BOLD,18));
		jtf_score.setBackground(Color.WHITE);
		jtf_score.setHorizontalAlignment(JTextField.RIGHT);
		jpNorth.add(jtf_score);
		jpNorth.add(jb3);
		jpNorth.add(jb4);
		c.add(jp);
		c.add(jpNorth,BorderLayout.NORTH);
        this.setJMenuBar(jmb);
	    this.setLocation(300,0);
		this.setSize(700,720);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		t=new Thread(this);
        t.start();
		
	}//Worm(int rowNum, int collumnNum,int f)
	
	//method
	public void run() {
		// TODO worm runs!!
		while (true){
		 try{
			 while (stopNow==true){
				Thread.sleep(4000);
			 }
			
				Thread.sleep(1000/frequence);
				//show time : add a new head and remove the tail , so the worm runs!! 
				int ty=(wormBody.getFirst()).y+deltaRow;
				int tx=(wormBody.getFirst()).x+deltaCol;	
				if (breakWallEnable){
					ty=(ty+jbArr.length)%jbArr.length;
					tx=(tx+jbArr[0].length)%jbArr[0].length;// new head's (potential) position
				}
				//eat Beans now!
				if (jbArr[ty][tx].isBlack==false){                 //ate nothing
                     //add a new head:
					wormBody.addFirst(new BodyPartPosition(ty,tx)); 
					 //remove the tail:
					BodyPartPosition bpp=wormBody.removeLast();
					jbArr[bpp.y][bpp.x].setBackground(Color.WHITE); 
					jbArr[bpp.y][bpp.x].isBlack=false;
					jbArr[bpp.y][bpp.x].isPartOfBody=false;
				}else if(jbArr[ty][tx].isPartOfBody==false){ //and jbArr[ty][tx].isBlack==true , worm eats bean!
                     //add a new head:
					wormBody.addFirst(new BodyPartPosition(ty,tx)); 
				     //DO NOT remove the tail ,because this worm has just eaten a bean:
					score++;
					jtf_score.setText(""+score);
					randomBean();
				}else{   //the worm ate itself
					throw new  Exception();
				}
				
			
	   	 }catch(InterruptedException ie){
	   		continue;
	   	 }catch(Exception e){   // the worm breaks the wall or eats itself 
	   		stopNow=true;
	   		continue;
	   	 }
		}//while
	}//run()
	
	public void randomBean(){
		//TODO generate a bean at a random positon
		boolean isbeanSetSucceed=false;
		while (!isbeanSetSucceed){
			
			int row=(int)(Math.random()*(jbArr.length-1)+1);
			int col=(int)(Math.random()*(jbArr[0].length-1)+1);                                                
			if (jbArr[row][col].isBlack){
				continue;
			}else{
				jbArr[row][col].isBlack=true;
				jbArr[row][col].isPartOfBody=false; //is this one necessary?
				jbArr[row][col].setBackground(Color.BLACK);
				isbeanSetSucceed=true;
			}
		}
	}
	
	public void resetWorm(){
		//TODO reset worm to a new game
		//ListIterator li=wormBody.listIterator();
		stopNow=true;
		t.interrupt();
		deltaRow=0;
		deltaCol=1;
		for(BodyPartPosition bpp:wormBody){
			bpp.useWhenReset();
		}
		wormBody.clear();
		score=0;
		jtf_score.setText(""+score);
		for (int i=0;i<4;i++){
			wormBody.add(new BodyPartPosition(jbArr.length/2,jbArr[0].length/7-i));
		}
		
	}

	//inner class
	 class ChangeDirection extends KeyAdapter{
		
		public void keyPressed(KeyEvent e) {
         //TODO change the running direction of the worm!!	
			/*
			 * description of deltaRow and deltaCol;
			 *   
			 *          deltaRow  deltaCol
			 *     up:      -1         0
			 *   down:      +1         0
			 *   left:       0        -1
			 *  right:       0        +1
			 *  
			 * */
			switch (e.getKeyCode()){
			case KeyEvent.VK_UP:  { if(deltaRow==1&&deltaCol==0){
				return;
			}
				deltaRow=-1;  deltaCol=0; break;}
			case KeyEvent.VK_DOWN:  { if(deltaRow==-1&&deltaCol==0){
				return;
			}deltaRow=+1;  deltaCol=0; break;}
			case KeyEvent.VK_LEFT:  { if(deltaRow==0&&deltaCol==1){
				return;
			}deltaRow=0;  deltaCol=-1; break;}
			case KeyEvent.VK_RIGHT: { if(deltaRow==0&&deltaCol==-1){
				return;
			}deltaRow=0;  deltaCol=1; break;}
			}
			
		}
		 
	}//changeDirection
	
	
	 class BodyPartPosition{
		int y;        //the y coordinate
		int x;        //the x coordinate
		public BodyPartPosition(int y, int x) {
			super();
			// TODO let this worm make a step ahead 
			//      this class is created for the element--wormBody 
			this.y = y;
			this.x = x;
			jbArr[y][x].setBackground(Color.BLACK);
			jbArr[y][x].isBlack=true;
			jbArr[y][x].isPartOfBody=true;
		}
		
		public void useWhenReset(){
			jbArr[y][x].setBackground(Color.WHITE);
			jbArr[y][x].isBlack=false;
			jbArr[y][x].isPartOfBody=false;
		}
		
	}
	
	 class GridButton extends JButton{
		boolean isBlack;
		boolean isPartOfBody;
	}
	//main
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        new Worm(30,30,10);
        
	}

	
}
