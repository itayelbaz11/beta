package com.example.beta;

public class Vector {
    public int direction;
    public int steps;

    public Vector(int direction,int steps){
        this.direction=direction;
        this.steps=steps;
    }

    public int getDirection() {
        return direction;
    }

    public int getSteps() {
        return steps;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}
