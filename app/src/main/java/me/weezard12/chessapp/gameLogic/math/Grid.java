package me.weezard12.chessapp.gameLogic.math;

public abstract class Grid<T> {
    protected T[][] grid;

    public Grid(int rows, int columns, int rowDistance, int columnDistance){
    }

    public abstract void updateGrid(int rowDistance, int columnDistance);
    public T[][] getGrid(){return grid;}

}
