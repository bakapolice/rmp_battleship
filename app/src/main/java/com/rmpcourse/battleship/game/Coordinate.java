package com.rmpcourse.battleship.game;


// Класс координаты
public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // получить координату х
    public int getX() {
        return x;
    }

    // установить координату х
    public void setX(int x) {
        this.x = x;
    }

    // получить координату y
    public int getY() {
        return y;
    }

    // получить координату y
    public void setY(int y) {
        this.y = y;
    }

    // установить координату
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Coordinate)) {
            return false;
        }

        Coordinate c = (Coordinate) object;

        if (this.x == c.getX() && this.y == c.getY()) {
            return true;
        } else {
            return false;
        }
    }
}