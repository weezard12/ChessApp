package me.weezard12.chessapp.gameLogic.math;

import android.graphics.Point;

public class GridPoints extends Grid<Point>{

    public GridPoints(int rows, int columns, int rowDistance, int columnDistance) {
        super(rows, columns, rowDistance, columnDistance);
        grid = new Point[rows][columns];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                grid[y][x] = new Point(x * rowDistance,y * columnDistance);
            }
        }
    }

    @Override
    public void updateGrid(int rowDistance, int columnDistance) {
        // TODO add update grid logic.
    }

    public Point getClosestPoint(Point point) {
        Point closestPoint = grid[0][0];
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                double distance = calculateDistance(point, grid[i][j]);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPoint = grid[i][j];
                }
            }
        }
        return closestPoint;
    }
    public Point getClosestPointAsIdx(Point point) {
        Point closestPointIDX = grid[0][0];
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                double distance = calculateDistance(point, grid[i][j]);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPointIDX = new Point(i,j);
                }
            }
        }
        return closestPointIDX;
    }

    private double calculateDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

}
