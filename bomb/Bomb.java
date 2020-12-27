// package experimentation;

//import java.awt.BorderLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author weng
 * last modified date : 21/03/08
 *
 */
public class Bomb {

	/**
	 * field
	 */
	
	int bombNum=0;
	int rightMarkCounter=0;
	int markCount=0;
	NewButton[][] nba;
	int rowNumber;        //for "set a new bomb to another place" only!
	int collumnNumber;    //for "set a new bomb to another place" only!
	int wantedBombNumber;  //for text counter only!
	
	JFrame jf=new JFrame("BOMB");
	Container c=jf.getContentPane();
	
	boolean firstTimeRun=true;//for "jf" only
	boolean neverClicked=true;//for Button only
	
	JDialog jd=new JDialog(jf);   //jd and jdiaBt are created for the result dialog
	JButton jdiaBt=new JButton();
	
	JPanel jpForCounter=new JPanel();//for text counter only 
	JButton jbeast=new JButton();
	JButton jbwest=new JButton();
	JTextField jtfForCounter=new  JTextField();//for text counter only 
	
	
	JMenuBar jmb=new JMenuBar();
	JMenu jm=new JMenu("Game");
	JMenuItem jmi1=new JMenuItem("bomb*10");
	JMenuItem jmi2=new JMenuItem("bomb*30");
	JMenuItem jmi3=new JMenuItem("bomb*50");
	JMenuItem jmi4=new JMenuItem("Quit");
	
	
	/**
	 * consturctor
	 */
	public Bomb(){
		
		jf.addWindowListener(new WindowAdapter(){
			 public void windowClosing(WindowEvent e)  {
				System.exit(0);
			}
		});
		jmi1.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Bomb.this.initialize(10,10,10);
			}	
		});
		jmi2.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				initialize(30,15,15);
			}	
		});
		jmi3.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				initialize(50,18,18);
			}	
		});
		jmi4.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(0);
			}	
		});
		jpForCounter.setLayout(new BorderLayout());
		jtfForCounter.setSize(100,20);
		jtfForCounter.setFont(new Font("counter",Font.PLAIN,18));
		jtfForCounter.setHorizontalAlignment(JTextField.RIGHT);
		jbwest.setBackground(Color.LIGHT_GRAY);
		jbeast.setBackground(Color.LIGHT_GRAY);
		jpForCounter.add(jtfForCounter,BorderLayout.CENTER);  //add the counter
		jpForCounter.add(jbwest,BorderLayout.WEST);
		jpForCounter.add(jbeast,BorderLayout.EAST);
		
		jm.add(jmi1);
		jm.add(jmi2);
		jm.add(jmi3);
		jm.addSeparator();
		jm.add(jmi4);
		jmb.add(jm);
		
		jdiaBt.setFont(new Font("result", Font.PLAIN, 25));
		jd.add(jdiaBt);
		jd.setSize(400,200);
		jd.setLocation(300,200);
		jd.setResizable(false);
		
		jf.setJMenuBar(jmb);
		jf.setSize(750,700);
		jf.setLocation(100,5);
		jf.setResizable(false);
		jf.setVisible(true);
		
		
	}
	/**
	 * method
	 */
	public void initialize(int wantedBombNum,int rowNum,int collumnNum){
		bombNum=0;
		rightMarkCounter=0;
		markCount=0;
		wantedBombNumber=wantedBombNum; //for text counter only
		nba=new NewButton[rowNum][collumnNum];
		
		rowNumber=rowNum;
		collumnNumber=collumnNum;
		
		jtfForCounter.setText(String.valueOf(wantedBombNum));
		
		if (firstTimeRun==false){
			c.removeAll();
			
		}
		JPanel jp=new JPanel();//must put jp here!
		jp.setLayout(new GridLayout(rowNum,collumnNum));
		


		c.add(jp);
		c.add(jpForCounter,BorderLayout.NORTH);
		  for (int i=0;i<rowNum;i++){
			  for (int j=0;j<collumnNum;j++){
			    	nba[i][j]=new NewButton(i,j);
			    	if (wantedBombNum>10){
			    	nba[i][j].setFont(new Font("button",Font.BOLD,10));
			    	}
				    jp.add(nba[i][j]);
				
			  }
		  }
		
		while (bombNum<wantedBombNum){
		  int i=(int)Math.floor(rowNum*Math.random());
		  int j=(int)Math.floor(collumnNum*Math.random());
		  if (nba[i][j].isBomb==false){
			  nba[i][j].isBomb=true;
			  bombNum++;
			  for (int k=i-1;k<=i+1;k++){
					if (k==-1||k==rowNum){
						continue;
					}
				  for (int l=j-1;l<=j+1;l++){
					  if (l==-1||l==collumnNum/*||nba[k][l].isBomb==true*/){
							continue;
						}			  
					  nba[k][l].pileNum++;
					}
				}//for
		  }//if
		}//while
		
		
		jf.setVisible(true);
		firstTimeRun=false; //will not be the first time to run this programme from this time on
		neverClicked=true;
	}//initialize
	
	public void blankCross(int r,int c){
		nba[r][c].open();
		// up  (r-1,c)
		if (r-1!=-1&&nba[r-1][c].isOpened==false&&nba[r-1][c].isMarked==false){
			if (nba[r-1][c].pileNum>0){
				nba[r-1][c].open();
			}else {
				blankCross(r-1,c);
			}
			
		}
        //right (r,c+1)
		if (c+1!=nba[0].length&&nba[r][c+1].isOpened==false&&nba[r][c+1].isMarked==false){
			if (nba[r][c+1].pileNum>0){
				nba[r][c+1].open();
			}else {
				blankCross(r,c+1);
			}
			
		}
		//down (r+1,c)
		if (r+1!=nba.length&&nba[r+1][c].isOpened==false&&nba[r+1][c].isMarked==false){
			if (nba[r+1][c].pileNum>0){
				nba[r+1][c].open();
			}else {
				blankCross(r+1,c);
			}
			
		}
		//left (r,c-1)
		if (c-1!=-1&&nba[r][c-1].isOpened==false&&nba[r][c-1].isMarked==false){
			if (nba[r][c-1].pileNum>0){
				nba[r][c-1].open();
			}else {
				blankCross(r,c-1);
			}
			
		}
	}//blankCross
	
	
	/**
	 * inner class
	 */
	class NewButton extends  JButton{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int pileNum=0;
		int row,col;
		boolean isOpened=false;
		boolean isBomb=false;
		boolean isMarked=false;
		public NewButton(int row, int col) {
			super();
			// TODO 自动生成构造函数存根
			this.row = row;
			this.col = col;
			this.addMouseListener(new NewButtonAction());
		}
		public void open(){
			if (isOpened==true||isMarked==true){
				return;
			}
			if (isBomb==true){
				setBackground(Color.red);
				setText("BOMB");
			}else if (pileNum!=0){
				setBackground(Color.white);
				setText(String.valueOf(pileNum));
			}else {
				setBackground(Color.white);
			}
			isOpened=true;
			
		}//open()
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			if (isMarked==true){  //if there is not any flag , paint a flag on this button 
				int w=this.getWidth();
				int h=this.getHeight();
				int[] x={w/4,w/4,3*w/8,3*w/8,3*w/4,w/4};
				int[] y={h/6,5*h/6,5*h/6,2*h/3,2*h/3,h/6};
				g.setColor(Color.RED);
				g.fillPolygon(x,y,x.length);
			}
			
		}
		public void setFlag() {
			// TODO Auto-generated method stub
			this.repaint();
		}
		public void removeFlag() {
			// TODO Auto-generated method stub
			this.repaint();
		}
		
		
		
		
	}// NewButton
	
	class NewButtonAction implements MouseListener{

		public void mousePressed(MouseEvent e) {
			// TODO 自动生成方法存根
			int mouseButton=e.getButton();
			NewButton nb=(NewButton)e.getSource();
			
			if (nb.isOpened==true){
				return;
			}
			if (mouseButton==MouseEvent.BUTTON3){//right button
				if (nb.isMarked==false){		//indicates that this button has not any flag yet			
					nb.isMarked=true;
					nb.setFlag();
					markCount++;
					jtfForCounter.setText(String.valueOf(wantedBombNumber-markCount));
					if (nb.isBomb==true){
						rightMarkCounter++;
					}				
				}else {                         //indicates that this button has a flag already	
					nb.isMarked=false;
					nb.removeFlag();
					markCount--;
					jtfForCounter.setText(String.valueOf(wantedBombNumber-markCount));
					if (nb.isBomb==true){
						rightMarkCounter--;
					}					
				}
			
				
				if (markCount==rightMarkCounter&&markCount==bombNum){
					jdiaBt.setText("you win");
					
					jd.setVisible(true);
					
				}
			
				
				
			}else{                            //left button 
				
				if (nb.isMarked==true){
					return;
				}
				if (neverClicked==true){
					/**/
					if (nb.isBomb==true){ //the first-clicked button is a bomb
						while (nba[nb.row][nb.col].isBomb==true){ //this "while" would make sure that the first button you clicked is not a bomb
							//1.clear this bomb
							nba[nb.row][nb.col].isBomb=false;
							for (int k=nb.row-1;k<=nb.row+1;k++){
								if (k==-1||k==rowNumber){   
									continue;
								}
							  for (int l=nb.col-1;l<=nb.col+1;l++){
								  if (l==-1||l==collumnNumber/*||nba[k][l].isBomb==true*/){
										continue;
									}			  
								  nba[k][l].pileNum--;
								}
							}//for
							
							//2.set a new bomb to another place
							  int i=(int)Math.floor(rowNumber*Math.random());  
							  int j=(int)Math.floor(collumnNumber*Math.random());
							  if (nba[i][j].isBomb==false){
								  nba[i][j].isBomb=true;
								
								  for (int k=i-1;k<=i+1;k++){
										if (k==-1||k==rowNumber){
											continue;
										}
									  for (int l=j-1;l<=j+1;l++){
										  if (l==-1||l==collumnNumber/*||nba[k][l].isBomb==true*/){
												continue;
											}			  
										  nba[k][l].pileNum++;
										}
									}//for
							  }//if
							
						}//while
					}
					
					neverClicked=false;
				}
				
				if (nb.isBomb==true){
		
				    for (int i=0;i<nba.length;i++){    //open all
					    for (int j=0;j<nba[0].length;j++){
						    nba[i][j].open();
					    }
				    }
				    jdiaBt.setText("BOMB!! HA HA HA ");
					
					jd.setVisible(true);
				
			    }else if (nb.pileNum>0){
				     nb.open();
			    }else {
				     blankCross(nb.row,nb.col);
				
			     }
			
		    	
		     }

		
		}//void mouseClicked(MouseEvent e)
		
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	/**
	 * main()
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根
        Bomb bb=new Bomb();
        bb.initialize(10,10,10);
	}

}
