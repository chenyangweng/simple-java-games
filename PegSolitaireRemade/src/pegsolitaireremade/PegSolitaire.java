package pegsolitaireremade;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author weng
 * @version 2008.08
 */
public class PegSolitaire extends JFrame {

//field
    int counter = 0, //plus 1 per step
            chessmanSum = 32, //minus 1 per step
            delta = 5;
    Container con = this.getContentPane();
    Chessman[][] chessmanArray = new Chessman[7][];
    JTextField conterDisplay = new JTextField("0"),
            chessmanSumDisplay = new JTextField("32");
    int chessmanWidth, chessmanHeight;
    Recorder recorder = new Recorder();
    static Register register = new Register();
    static JScrollPane tabJpRecords = new JScrollPane();

//constructor
    public PegSolitaire() throws HeadlessException {
        super("Peg Solitaire    by Weng  08.08");
        this.setLayout(null);
        this.setSize(750, 520);
        con.setLayout(null);
        chessmanWidth = 70;
        chessmanHeight = 70;
        Chessman.actionListener = new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                // TODO Auto-generated method stub
                Chessman pressedChess = (Chessman) (ae.getSource());
                if (!pressedChess.isDisappeared()) {  //a chessman has been pressed
                    if (register.isRegistered()) {
                        chessmanArray[register.i][getArrayIndexY(register.i, register.j)].stay();
                    }
                    register.register(pressedChess);
                    pressedChess.prepare();
                } else {                              //there is not any chessman in the box you click
                    if (register.isRegistered()) {
                        //rule:
                        if ((register.i == pressedChess.i && Math.abs(register.j - pressedChess.j) == 2)
                                || register.j == pressedChess.j && Math.abs(register.i - pressedChess.i) == 2) {
                            int mi = register.i;
                            int mj = (int) ((register.j + pressedChess.j) / 2.0);
                            if (register.j == pressedChess.j) {
                                mi = (int) ((register.i + pressedChess.i) / 2.0);
                                mj = register.j;
                            }
                            recorder.record(register.i, register.j, mi, mj, pressedChess.i, pressedChess.j);
                            chessmanArray[register.i][getArrayIndexY(register.i, register.j)].disappear();
                            chessmanArray[mi][getArrayIndexY(mi, mj)].disappear();
                            chessmanArray[pressedChess.i][getArrayIndexY(pressedChess.i, pressedChess.j)].stay();
                            register.clear();
                            conterDisplay.setText(++counter + "");
                            chessmanSumDisplay.setText(--chessmanSum + "");
                        } else {
                            chessmanArray[register.i][getArrayIndexY(register.i, register.j)].stay();
                            register.clear();
                        }
                    }//if
                }//else
            }//actionPerformed()

        };

        loadChessman();

        //other components:	
        addOtherComponents();
        //other initialization

        new Rank(this);
        con.setBackground(Color.WHITE);
        conterDisplay.setEditable(false);
        chessmanSumDisplay.setEditable(false);
        conterDisplay.setBackground(Color.white);
        chessmanSumDisplay.setBackground(Color.white);
        this.setResizable(false);
        this.setLocation(100, 0);
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent arg0) {
                // TODO Auto-generated method stub

                FileWriter fw = null;
                PrintWriter pw = null;
                try {
                    fw = new FileWriter("save.txt");
                    pw = new PrintWriter(fw);

                    for (Rank rank : Rank.results) {
                        pw.println(rank.name + " " + rank.chessman + " " + rank.steps);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    pw.flush();
                    pw.close();
                    System.exit(0);
                }

            }

        });
        //read file:

        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader("save.txt");
            br = new BufferedReader(fr);
            System.out.println(br);
            Rank.results.clear();
            String temp = br.readLine();
            String[] s = null;
            while (temp != null) {
                s = temp.split(" ");
                if (s.length == 3) {
                    Rank.results.add(new Rank(s[0], Integer.parseInt(s[1]), Integer.parseInt(s[2])));
                }
                temp = br.readLine();
                
            }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }//construcor

    //method
    void addOtherComponents() {
//    	other components:
        JButton seeResult = new JButton(new ImageIcon("image/seeresult.gif")),
                backwards = new JButton(new ImageIcon("image/backwards.gif")),
                newGame = new JButton(new ImageIcon("image/newgame.gif")),
                exit = new JButton(new ImageIcon("image/exit.gif"));
        JLabel background = new JLabel(new ImageIcon("image/background.gif")),
                buttonAreaBackground = new JLabel(new ImageIcon("image/buttonAreaBackground.gif")),
                counter = new JLabel("Counter: "),
                chessmanSum = new JLabel("Chessman: ");
        JTabbedPane tabs = new JTabbedPane();
        JPanel tabJpDisplay = new JPanel();

        tabs.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent arg0) {
                // TODO Auto-generated method stub
                JTabbedPane jtp = (JTabbedPane) (arg0.getSource());
                if (Rank.results.size() == 0 || jtp.getSelectedIndex() != 1) {
                    return;
                }

                String[] colHeads = {"Name", "Chessman", "Step Counter"};
                Object[][] data = new Object[Rank.results.size()][3];
                JTable jt;

                int i = 0;
                for (Rank rank : Rank.results) {
                    data[i][0] = rank.name;
                    data[i][1] = rank.chessman;
                    data[i][2] = rank.steps;
                    i++;

                }
                jt = new JTable(data, colHeads);
                tabJpRecords = new JScrollPane(jt);
                tabJpRecords.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                jtp.setComponentAt(1, tabJpRecords);
                jtp.validate();
            }
        });

        con.add(background);
        background.setBounds(0, 0, 495, 495);

        tabs.addTab("Display", tabJpDisplay);
        tabs.addTab("Rank", tabJpRecords);
        con.add(tabs);
        tabs.setBounds(495, 0, 250, 520);
        //display
        tabJpDisplay.setLayout(null);
        conterDisplay.setHorizontalAlignment(JTextField.RIGHT);
        conterDisplay.setFont(new Font("name", Font.BOLD, 12));
        chessmanSumDisplay.setHorizontalAlignment(JTextField.RIGHT);
        chessmanSumDisplay.setFont(new Font("name", Font.BOLD, 12));
        tabJpDisplay.add(counter);
        tabJpDisplay.add(chessmanSum);
        tabJpDisplay.add(conterDisplay);
        tabJpDisplay.add(chessmanSumDisplay);
        counter.setBounds(20, 10, 100, 20);
        conterDisplay.setBounds(100, 10, 100, 20);
        chessmanSum.setBounds(20, 40, 100, 20);
        chessmanSumDisplay.setBounds(100, 40, 100, 20);

        //Buttons
        seeResult.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                int i = 1, j = 1;
                if (chessmanArray[3][3].isDisappeared() == false) {
                    i = 3;
                    j = 3;
                }
                Rank.rank(PegSolitaire.this.chessmanSum, PegSolitaire.this.counter, i, j);
            }

        });
        backwards.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                recorder.backwards();
            }

        });
        newGame.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                reset();
            }

        });
        exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                FileWriter fw = null;
                PrintWriter pw = null;
                try {
                    fw = new FileWriter("save.txt");
                    pw = new PrintWriter(fw);

                    for (Rank rank : Rank.results) {
                        pw.println(rank.name + " " + rank.chessman + " " + rank.steps);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    pw.flush();
                    pw.close();
                    System.exit(0);
                }
            }

        });
        tabJpDisplay.add(seeResult);
        tabJpDisplay.add(backwards);
        tabJpDisplay.add(newGame);
        tabJpDisplay.add(exit);
        tabJpDisplay.add(buttonAreaBackground);
        backwards.setBounds(20, 130, 137, 40);
        seeResult.setBounds(90, 210, 137, 40);
        newGame.setBounds(20, 290, 137, 40);
        exit.setBounds(90, 370, 137, 40);
        buttonAreaBackground.setBounds(0, 75, 245, 390);

    }

    int getArrayIndexY(int x, int y) {
        if (x < 2 || x > 4) {
            y = y - 2;
        }
        return y;
    }

    void reset() {
        counter = 0;
        chessmanSum = 32;
        register.clear();
        for (Chessman[] x : chessmanArray) {
            for (Chessman y : x) {
                y.stay();
            }
        }
        chessmanArray[3][3].disappear();
        conterDisplay.setText("0");
        chessmanSumDisplay.setText("32");
    }

    void loadChessman() {
        for (int i = 0; i < 2; i++) {
            chessmanArray[i] = new Chessman[3];
            for (int j = 2; j < 5; j++) {
                chessmanArray[i][j - 2] = new Chessman(i, j);
                con.add(chessmanArray[i][j - 2]);
                chessmanArray[i][j - 2].setLocation(j * chessmanWidth + delta, i * chessmanHeight);

            }
        }
        for (int i = 2; i < 5; i++) {
            chessmanArray[i] = new Chessman[7];
            for (int j = 0; j < 7; j++) {
                chessmanArray[i][j] = new Chessman(i, j);
                con.add(chessmanArray[i][j]);
                chessmanArray[i][j].setLocation(j * chessmanWidth + delta, i * chessmanHeight);
            }
        }
        for (int i = 5; i < 7; i++) {
            chessmanArray[i] = new Chessman[3];
            for (int j = 2; j < 5; j++) {
                chessmanArray[i][j - 2] = new Chessman(i, j);
                con.add(chessmanArray[i][j - 2]);
                chessmanArray[i][j - 2].setLocation(j * chessmanWidth + delta, i * chessmanHeight);

            }
        }
        chessmanArray[3][3].disappear();

    }

//inner class
    class Recorder {

        int originalI,
                originalJ,
                middleI,
                middleJ,
                newI,
                newJ;
        boolean backwardsable;

        void record(int originalI, int originalJ, int middleI, int middleJ, int newI, int newJ) {
            this.originalI = originalI;
            this.originalJ = originalJ;
            this.middleI = middleI;
            this.middleJ = middleJ;
            this.newI = newI;
            this.newJ = newJ;
            backwardsable = true;
        }

        void backwards() {
            if (backwardsable == false) {
                return;
            }
            int oJ = getArrayIndexY(originalI, originalJ),
                    mJ = getArrayIndexY(middleI, middleJ),
                    nJ = getArrayIndexY(newI, newJ);
            chessmanArray[originalI][oJ].stay();
            chessmanArray[middleI][mJ].stay();
            chessmanArray[newI][nJ].disappear();
            conterDisplay.setText(--counter + "");
            chessmanSumDisplay.setText(++chessmanSum + "");
            backwardsable = false;
        }

    }

//main
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new PegSolitaire();
    }

}

class Register {

    int i = -1;
    int j = -1;
    private Chessman chessman;

    public void register(Chessman chessman) {
        this.chessman = chessman;
        i = this.chessman.i;
        j = this.chessman.j;
    }

    void clear() {
        i = -1;
        j = -1;
        if (chessman != null) {
            chessman = null;
        }
    }

    boolean isRegistered() {
        return chessman == null ? false : true;
    }

}

class Chessman extends JButton {

    int i; //row 
    int j; //collumn	
    private boolean isDisappeared = false;
    static Icon imageStay = new ImageIcon("image/no1.gif");
    static Icon imagePrepare = new ImageIcon("image/no2.gif");
    static Icon imageMove = new ImageIcon("image/no3.gif");
    static ActionListener actionListener;
    static MouseListener mouseListener;
  

    public Chessman(int i, int j) {
        super();
        this.i = i;
        this.j = j;
        setBackground(Color.white);
        setSize(70, 70);
        mouseListener = new MouseAdapter() {
            public void mouseEntered(MouseEvent arg0) {
                // TODO Auto-generated method stub
                Icon originalIcon = ((Chessman) (arg0.getSource())).getIcon();
                if (originalIcon != null && originalIcon.equals(imageStay)) {
                    setIcon(Chessman.imageMove);
                }

            }

            public void mouseExited(MouseEvent arg0) {
                // TODO Auto-generated method stub
                Icon originalIcon = ((Chessman) (arg0.getSource())).getIcon();
                if (originalIcon != null && originalIcon.equals(imageMove)) {
                    setIcon(Chessman.imageStay);
                }

            }

        };
        this.addMouseListener(mouseListener);
        addActionListener(actionListener);
        stay();

    }

    void stay() {
        this.setIcon(Chessman.imageStay);
        isDisappeared = false;
    }

    void prepare() {
        this.setIcon(Chessman.imagePrepare);
        isDisappeared = false;
    }

    void disappear() {
        this.setIcon(null);
        isDisappeared = true;
    }

    boolean isDisappeared() {
        return isDisappeared;
    }
}

class Rank implements Comparable {

    String name;
    int chessman,
            steps;
    static JDialog jDialog;
    static JButton jButton = new JButton("OK");
    static JTextField jTextField = new JTextField();
    static JLabel jLabel = new JLabel();
    static String result;
    static List<Rank> results = new ArrayList<Rank>();

    static {
        jTextField.setVisible(false);
        jButton.setVisible(false);
        jButton.addActionListener(new ActionListener() {

            @SuppressWarnings("unchecked")
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                results.get(results.size() - 1).name = jTextField.getText();
                Collections.sort(results);
                if (results.size() == 11) {
                    results.remove(10);
                }
                jTextField.setVisible(false);
                jButton.setVisible(false);

            }

        });
        jLabel.setVerticalAlignment(JLabel.CENTER);
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        jLabel.setFont(new Font("jlabel", Font.BOLD, 25));

    }

    public Rank(JFrame j) {
        jDialog = new JDialog(j);
        jDialog.setTitle("Results");
        jDialog.setSize(200, 200);
        jDialog.setLocation(320, 200);
        jDialog.setLayout(new BorderLayout());
        jDialog.add(jLabel, BorderLayout.NORTH);
        jDialog.add(jButton, BorderLayout.SOUTH);
        jDialog.add(jTextField, BorderLayout.CENTER);
    }

    public Rank(int chessman, int steps) {
        super();
        this.chessman = chessman;
        this.steps = steps;
    }

    public Rank(String name, int chessman, int steps) {
        super();
        this.name = name;
        this.chessman = chessman;
        this.steps = steps;
    }

    static void rank(int chessmanSum, int counter, int i, int j) {

        if (chessmanSum >= 6) {
            result = "Normal!";
        } else {
            switch (chessmanSum) {
                case 5:
                    result = "Not Bad!";
                case 4:
                    result = "Good!";
                case 3:
                    result = "Clever!";
                case 2:
                    result = "Terrific!";
                case 1: {
                    result = "Master!";
                    if (i == 3 && j == 3) {
                        result = "Genius£¡";
                        if (counter <= 18) {
                            result = "You Are Super Genius£¡";
                        }
                    }
                }

            }
        }
        if (Rank.results.size() >= 0 && Rank.results.size() <= 10) {
            jButton.setVisible(true);
            jTextField.setVisible(true);
            jLabel.setText(result);
            jDialog.setVisible(true);
            results.add(new Rank(chessmanSum, counter));

        }

    }

    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        int c = ((Rank) o).chessman,
                s = ((Rank) o).steps;
        if (chessman < c) {
            return -1;
        } else if (chessman > c) {
            return 1;
        } else {
            return steps < s ? -1 : (steps == s ? 0 : 1);
        }
    }

    void popUp(String s) {

    }
}
