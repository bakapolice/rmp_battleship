package com.rmpcourse.battleship.game;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private boolean alive;
    private Coordinate coordinate;

    private ShipDirection direction;
    private ShipType type;

    public Ship(Coordinate coordinate, ShipDirection direction, ShipType type) {
        alive = true;
        this.coordinate = coordinate;
        this.direction = direction;
        this.type = type;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
        fixInvalidCoordinate();
    }

    public List<Coordinate> getListCoordinates() {
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(coordinate);

        int x = coordinate.getX();
        int y = coordinate.getY();

        for (int i = 1; i < type.getLength(); i++) {
            if (direction == ShipDirection.HORIZONTAL) {
                x += 1;
            } else if (direction == ShipDirection.VERTICAL) {
                y += 1;
            }

            coordinates.add(new Coordinate(x, y));
        }

        return coordinates;
    }

    public Coordinate getCenter() {
        int center = ((type.getLength() - 1) / 2);
        return getListCoordinates().get(center);
    }

    public ShipDirection getDirection() {
        return direction;
    }

    public void setDirection(ShipDirection direction) {
        if (this.direction != direction) {
            int center = ((type.getLength() - 1) / 2);
            this.direction = direction;

            if (direction == ShipDirection.HORIZONTAL) {
                setCoordinate(new Coordinate(coordinate.getX() - center, coordinate.getY() + center));
            } else if (direction == ShipDirection.VERTICAL) {
                setCoordinate(new Coordinate(coordinate.getX() + center, coordinate.getY() - center));
            }
        }
    }

    private void fixInvalidCoordinate() {
        int x = coordinate.getX();
        int y = coordinate.getY();

        if (x < 0) {
            coordinate.setX(0);
        } else if (direction == ShipDirection.HORIZONTAL && x + type.getLength() - 1 >= BoardSize.COLUMNS) {
            coordinate.setX(BoardSize.COLUMNS - type.getLength());
        } else if (direction == ShipDirection.VERTICAL && x >= BoardSize.COLUMNS) {
            coordinate.setX(BoardSize.COLUMNS - 1);
        }

        if (y < 0) {
            coordinate.setY(0);
        } else if (direction == ShipDirection.VERTICAL && y + type.getLength() - 1 >= BoardSize.ROWS) {
            coordinate.setY(BoardSize.ROWS - type.getLength());
        } else if (direction == ShipDirection.HORIZONTAL && y >= BoardSize.ROWS) {
            coordinate.setY(BoardSize.ROWS - 1);
        }
    }

    public void rotate() {
        if (direction == ShipDirection.HORIZONTAL) {
            setDirection(ShipDirection.VERTICAL);
        } else {
            setDirection(ShipDirection.HORIZONTAL);
        }
    }

    public ShipType getType() {
        return type;
    }

    public int getLength() {
        return type.getLength();
    }
}
