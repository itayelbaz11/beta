package com.example.beta;

import java.util.ArrayList;

public class Spot {
    public double f;
    public double h;
    public double g;
    public int i;
    public int j;
    public boolean isNeighbord;
    public Spot previous;
    public ArrayList<Spot> neighbors;
    public boolean wall;

    public Spot(int i,int j,boolean wall){
        this.i=i;
        this.j=j;
        this.wall=wall;
        this.f=0;
        this.h=0;
        this.g=0;
        this.isNeighbord=false;
        neighbors=new ArrayList<Spot>();
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    /**
     * adding all of the object's neigbor to its own neighbors list
     * @param grid
     */
    public void addNeighbors(Spot[][] grid) {
        int i = this.i;
        int j = this.j;
        if (!isNeighbord) {

            if (i < grid.length - 1) {
                this.neighbors.add(grid[i + 1][j]);
                if (j > 0) {
                    this.neighbors.add(grid[i + 1][j - 1]);
                }
                if (j < grid[0].length - 1) {
                    this.neighbors.add(grid[i + 1][j + 1]);
                }
            }

            if (i > 0) {
                this.neighbors.add(grid[i - 1][j]);
                if (j > 0) {
                    this.neighbors.add(grid[i - 1][j - 1]);
                }
                if (j < grid[0].length - 1) {
                    this.neighbors.add(grid[i - 1][j + 1]);
                }
            }

            if (j < grid[0].length - 1) {
                this.neighbors.add(grid[i][j + 1]);
            }

            if (j > 0) {
                this.neighbors.add(grid[i][j - 1]);
            }


        }
        isNeighbord=true;
    }

}
