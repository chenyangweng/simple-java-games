/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakePkg;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


/**
 * This class draws and move the snake
 * @author wengc
 */
class SnakeDrawer  implements Runnable{
    AppParameters appParameters;
    javax.swing.JPanel panel;
    SnakeMainWindow snakeMainWindow;
    
    // Snake bodies:
    private Grid[][] gridMatrix;
    private Grid head, tail;
    int[] dir;     // up: [-1, 0] down: [1, 0] left: [0, -1] right: [0, 1] 
    // Concurrent
    private boolean isAlive;

    public boolean isAlive() {
        return isAlive;
    }
    
    public void killSnake(){
        this.isAlive = false;
    }
    /**
     * constructor...
     * @param appParameters
     * @param panel 
     */
    public SnakeDrawer(AppParameters appParameters, javax.swing.JPanel panel, SnakeMainWindow snakeMainWindow) {
        this.appParameters = appParameters;
        this.panel = panel;
        this.snakeMainWindow = snakeMainWindow;   
        this.initializeGridMatrix();  
        this.initializeSnakeAndFood();
    }
    
    public void startNewGame(){
        this.isAlive = true;
        for (int i=0; i<this.appParameters.gridRows; i++){
            for (int j=0; j<this.appParameters.gridCols; j++){
               if (this.gridMatrix[i][j].isSnakeBody | this.gridMatrix[i][j].isFood){
                   this.gridMatrix[i][j].becomeSnakeBody(false);
               }
            }
        }
        this.initializeSnakeAndFood();
    }
    
    /**
     * Initialize the grid matrix, and draw the grids in the panel
     * @author wengc
     */
    private void initializeGridMatrix(){
        this.gridMatrix = new Grid[this.appParameters.gridRows][this.appParameters.gridCols];
        for (int i=0; i<this.appParameters.gridRows; i++){
            for (int j=0; j<this.appParameters.gridCols; j++){
               this.gridMatrix[i][j] = new Grid(i, j);  // The matrix
               panel.add(this.gridMatrix[i][j]);       // Add the grid to the panel
            }
        }
    }
    
    /**
     * Initialize the snake when the program starts
     * 
     */
    private void initializeSnakeAndFood(){
        this.dir = this.appParameters.initDir;
        this.head = this.gridMatrix[this.appParameters.initHeadRow][this.appParameters.initHeadCol];
        this.tail = this.gridMatrix[this.appParameters.initHeadRow-this.dir[0]]
                [this.appParameters.initHeadCol-this.dir[1]];
        // Relation
        this.head.becomeSnakeBody(true);
        this.head.nextGrid = this.tail;
        this.tail.becomeSnakeBody(true);
        this.tail.prevGrid = this.head;
        // Food:
        this.drawAFood();
    }
    
    /**
     * Move the snake one step further.
     * @return successful move or not?
     */
    private boolean moveOneStep(){
        // Compute next head
        int rowNew = this.head.row + this.dir[0];
        int colNew = this.head.col + this.dir[1];
        Grid newHead;
        // Check #1: Is it wall?
        try{
            newHead = this.gridMatrix[rowNew][colNew]; 
        }catch(java.lang.ArrayIndexOutOfBoundsException aiobe){
            return false;
        }
        // Check #2: Is it snake body?
        if (newHead.isSnakeBody){
            return false;
        }
        // Check #3: Is it Food? If yes, then make it new head, and draw a new food
        if (newHead.isFood){
            newHead.becomeSnakeBody(true);
            newHead.nextGrid = this.head;
            this.head.prevGrid = newHead;
            this.head = newHead;
            //Draw a new food
            this.drawAFood();
            return this.moveOneStep();
        }
        // If you arrive at this step...just move one step further...
        this.drawNewHead(newHead);
        this.removeOldTail();
        return true;
    }
    
    
    
    /**
     * Draw new head of snake. 
     * The head only needs to know its next part of body.
     * @param row
     * @param col
     * @param nextRow
     * @param nextCol 
     */
    private void drawNewHead(Grid newHead){
        // The current head will be the next part of the new head
        Grid oldHead = this.head;
        // Update relation 
        newHead.nextGrid = oldHead;
        oldHead.prevGrid = newHead;
        // Set new head
        this.head = newHead;
        this.head.becomeSnakeBody(true);
    }
    
    private void removeOldTail(){
        Grid oldTail = this.tail;
        this.tail = oldTail.prevGrid;
        this.tail.nextGrid = null;
        // Remove
        oldTail.becomeSnakeBody(false); 
    }
    
    /**
     * Draw a food at a random location
     */
    private void drawAFood(){
        int foodRow = (int) Math.floor(Math.random()*this.appParameters.gridRows);
        int foodCol = (int) Math.floor(Math.random()*this.appParameters.gridCols);
        Grid food = this.gridMatrix[foodRow][foodCol];
        if (food.isSnakeBody || food.isFood || 
                (foodRow==0&&foodCol==0) || 
                (foodRow==0&&foodCol==this.appParameters.gridCols-1) ||
                (foodRow==this.appParameters.gridRows-1&&foodCol==0) || 
                (foodRow==this.appParameters.gridRows-1&&foodCol==this.appParameters.gridCols-1)){
            this.drawAFood();  // If it is part of the snake or food, or in the corner, then just keep calling
        }else{
            food.becomeFood();
        }
            
    }
    
    /**
     * Check if the new direction is ok.
     * I realize that it is not sufficient to check if the new dir is ok or not by only looking at the dir vector itself.
     * @param newDir
     * @return 
     */
    public boolean isNewDirOk(int[] newDir){
        int rowNew = this.head.row + newDir[0];
        int colNew = this.head.col + newDir[1];
        Grid newHead;
        // Check #1: Is it wall?
        try{
            newHead = this.gridMatrix[rowNew][colNew]; 
        }catch(java.lang.ArrayIndexOutOfBoundsException aiobe){
            return true;  // It is ok if the new direction leads to the wall ;)
        }
        // Check #2: Is it snake body?
        return !newHead.isSnakeBody;
    }
    
    private int getSnakeLength(){
        int sum = 0;
        Grid currGrid = this.head;
        while(currGrid != null){
            sum+=1;
            currGrid = currGrid.nextGrid;
        }
        return sum;
    }

    @Override
    public void run() {
        boolean isMoving = true;
        try { 
            while (isMoving && !Thread.currentThread().isInterrupted() && this.isAlive==true){
                // As long as it is for timing purpose, using Thread.sleep is bloody fine.
                // https://stackoverflow.com/questions/3956512/java-performance-issue-with-thread-sleep
                Thread.sleep(this.appParameters.sleepMilliSecond);
                if (this.appParameters.isPause){
                    continue;
                }
                isMoving = this.moveOneStep();  
            }  
            this.isAlive = false;
            System.out.println(Thread.currentThread().getName() + ": Game Over...");
            //JOptionPane.showMessageDialog(this.panel.getParent(), "The snake length is " + this.getSnakeLength());
            String username = JOptionPane.showInputDialog(this.panel.getParent(),  "The snake length is "+this.getSnakeLength()+".\nWhat's your name?");
            if (username!=null && username.trim().length()>0){
                this.snakeMainWindow.addNewRowToJTable(username, this.getSnakeLength());
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(SnakeDrawer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}