// package experimentation;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Gobang {

	/**
	 * @author Weng
	 */
	/**
	 * field
	 * cm: chessman    int   cm%2=0:"black"    cm%2=1:"white"
	 * bts: buttons     List  16*16 buttons
	 * f                Frame
	 */
	
	int cm=0;  
	List bts=new ArrayList(16);
	Frame f=new Frame("Gobang");
	/**
	 * constructor
	 */
	
	public Gobang() {
		super();
		// TODO initialize the window
		
		f.setLayout(new GridLayout(16,16));
		MenuBar mb=new MenuBar();
		Menu m=new Menu("File");
		MenuItem mi1=new MenuItem ("new game");
		MenuItem mi2=new MenuItem ("exit");
		m.add(mi1);
		m.addSeparator();
		m.add(mi2);
		mb.add(m);
		f.setMenuBar(mb);
		f.addWindowListener(new WindowClosing());
		mi1.addActionListener(new NewGame());
		mi2.addActionListener(new Exit());
		
		// TODO initialize buttons
		ListIterator it=bts.listIterator();
		for (int i=0;i<16;i++){
			List bts_temp=new ArrayList(16);
			ListIterator it_temp=bts_temp.listIterator();
			for (int j=0;j<16;j++){	
				NewButton nb=new NewButton(i,j);
				nb.addActionListener(new BTAcion());
				it_temp.add(nb);
				f.add(nb);
			}
			it.add(bts_temp);     
		}
        //	  ((NewButton)((List)bts.get(0)).get(0)).c
		
		
		
		
		f.setSize(600,600);
		f.setVisible(true);
		
		
	}
	
	/**
	 * method
	 * cmNum :  ChessManNUMber
	 */
	
	public int cmNum(int row,int collumn,int y,int x,char color){
		if (row+y<0||row+y>15||collumn+x<0||collumn+x>15){
			return 1;
		}
		else if(((NewButton)((List)bts.get(row+y)).get(collumn+x)).c!=color){
			return 1;
		}
		else return 1+cmNum(row+y,collumn+x,y,x,color);
	}
	
    /**
     * Inner class
     */
	private class WindowClosing implements WindowListener{

		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void windowClosing(WindowEvent arg0) {
			// TODO Auto-generated method stub
			System.exit(0);
			
		}

		public void windowClosed(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void windowDeactivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class NewGame implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			for (int i=0;i<16;i++){
				for (int j=0;j<16;j++){	
					if ( ((NewButton)((List)bts.get(i)).get(j)).c!=0){
						((NewButton)((List)bts.get(i)).get(j)).setBackground(Color.lightGray);
						((NewButton)((List)bts.get(i)).get(j)).c=0;
					}
				}
				     
			}
			
		}
		
	}
	
    private class Exit implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			System.exit(0);
		}
		
	}
    
    private class NewButton extends Button {
    	
        int row;  
        int col;  
        char c=0;  // 'b':black    'w':white
		public NewButton(int row, int col) throws HeadlessException {
			super();
			// TODO Auto-generated constructor stub
			this.row = row;
			this.col = col;
			this.setBackground(Color.lightGray);
		} 
    }
    
    private class BTAcion implements ActionListener{

		public void actionPerformed(ActionEvent ae) {
			// TODO 1.to change button color
			//      2.to check whether there is a 5-more-sequence of 
			//        same-color chessmen  
			if (((NewButton)ae.getSource()).c==0){
			char tc=0;  //temp_color
			int t_r=((NewButton)ae.getSource()).row;  //temp_row
			int t_c=((NewButton)ae.getSource()).col;  //temp_collumn
			
			switch (cm%2){
			case 0 :{((NewButton)ae.getSource()).setBackground(Color.black);
			          tc=((NewButton)ae.getSource()).c='b';break;}
			case 1 :{((NewButton)ae.getSource()).setBackground(Color.white);
	                  tc=((NewButton)ae.getSource()).c='w';break;}
			}
			cm++;
			
			/**
			 * directions:
			 *              n(north)        ne(northeast)
			 *  (-1,-1)  (-1, 0)   (-1,+1)
			 *  ( 0,-1)  (row,col) ( 0,+1)  e(east)
			 *  (+1,-1)  (+1, 0)   (+1,+1)
			 *                              se(sortheast)
			 */
			
			int n=cmNum(t_r,t_c,-1,0,tc)+cmNum(t_r,t_c,+1,0,tc)-1;
			int ne=cmNum(t_r,t_c,-1,+1,tc)+cmNum(t_r,t_c,+1,-1,tc)-1;
			int e=cmNum(t_r,t_c,0,+1,tc)+cmNum(t_r,t_c,0,-1,tc)-1;
			int se=cmNum(t_r,t_c,+1,+1,tc)+cmNum(t_r,t_c,-1,-1,tc)-1;
			
			if (n>=5||ne>=5||e>=5||se>=5){
				new ResultWindow(tc);
			}
			
			}
		}
    	
    }
    
    private class ResultWindow {

    	Dialog d=new Dialog (f,"result");
		public ResultWindow(char tc) {
			super();
			// TODO Auto-generated constructor 
			
			Button b=new Button("ok");
			Label l = new Label("Results",Label.CENTER);
			if (tc=='b'){
			l.setText("black win");
			}
			else {l.setText("white win");}
			
			l.setSize(100,50);
			
			b.setSize(100,50);
			b.addActionListener(new Hide());
			
			d.setLayout(null);
			d.add(b);
			d.add(l);
			b.setLocation(100,120);
			l.setLocation(100,20);
			d.setLocation(200,200);
			d.setSize(300,200);
			d.setVisible(true);
			
			}
			class Hide implements ActionListener{

				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					d.setVisible(false);
				}
		}
    	
    }
	/**
	 * main()
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        new Gobang();
	}

}
