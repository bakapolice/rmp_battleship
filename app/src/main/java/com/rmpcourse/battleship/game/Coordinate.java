package com.rmpcourse.battleship.game;

import android.os.Parcel;
import android.os.Parcelable;

public class Coordinate
{
    private int x;
    private int y;

    public Coordinate(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void set(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof Coordinate))
        {
            return false;
        }
        
        Coordinate c = (Coordinate) object;

        if (this.x == c.getX() && this.y == c.getY())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}