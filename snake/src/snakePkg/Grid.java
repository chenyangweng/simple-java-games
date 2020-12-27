/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakePkg;


/**
 * Grid is customized JLabel which makes it easier to construct the body of the snake
 * @author wengc
 */
class Grid extends javax.swing.JLabel{
    int row; 
    int col;  
    boolean isFood;
    boolean isSnakeBody;
    static java.awt.Color darkColor = new java.awt.Color(112, 144, 128);
    static java.awt.Color bgColor = new java.awt.Color(220, 163, 163);
    Grid prevGrid;
    Grid nextGrid;
    Grid(int row, int col){
        this.row = row;
        this.col = col;
        this.isFood = false;
        this.isSnakeBody = false;
        this.setOpaque(true);
        becomeSnakeBody(false);
    }
    
    /**
     * Set tr=true if the label is part of the snake body
     * @param tf 
     */
    final void becomeSnakeBody(boolean tf){
        this.isFood = false;
        this.isSnakeBody = tf;
        if (tf == true){
            this.setBackground(Grid.darkColor);   // not body
        }else{
            this.setBackground(Grid.bgColor);  // Is body
        }
    }
    
    void becomeFood(){
        this.setBackground(Grid.darkColor);  // Set color only
        this.isFood = true;
        this.isSnakeBody = false;
    }
}