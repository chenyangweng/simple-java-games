/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakePkg;

/**
 * Holds the parameters of the program
 * @author wengc
 */
class AppParameters{
    int gridRows, gridCols;
    int[] initDir;    // See SnakeDrawer for def. of direction
    int initHeadRow, initHeadCol;
    int sleepMilliSecond;
    boolean isPause;
    /**
     * Set default values
     */
    AppParameters() {
        this.gridRows = 37;
        this.gridCols = 37;
        // Init. direction
        this.initDir = new int[2];
        this.initDir[0] = -1;
        // sleepMilliSecond
        this.sleepMilliSecond = 26;
        // Init. snake head location
        this.initHeadRow = Math.round(this.gridRows/2)-1;
        this.initHeadCol = Math.round(this.gridCols/2)-1;
        // Pause?
        this.isPause = false;
    }
}
