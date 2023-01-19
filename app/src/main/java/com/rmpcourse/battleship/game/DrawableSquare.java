package com.rmpcourse.battleship.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;

import com.rmpcourse.battleship.R;

// Класс отрисовывемого квдрата, который можно нажать
@SuppressLint("ViewConstructor")
public
class DrawableSquare extends AppCompatImageButton {
    private Coordinate coordinate;
    boolean clicked = false;

    // Конструктор
    DrawableSquare(Context context, Coordinate coordinate) {
        super(context);
        this.coordinate = coordinate;
        setColor(R.color.colorBoard);
        setPadding(0, 0, 0, 0);
        setScaleType(ScaleType.FIT_CENTER);
    }

    // Получить координаты квадрта
    public Coordinate getCoordinate() {
        return coordinate;
    }

    // Проверить, было ли нажатие на квадрат
    public boolean isClicked() {
        return clicked;
    }

    // Указать, что было нажатие
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    // Установить цвет квадрта
    public void setColor(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(4, ContextCompat.getColor(getContext(), R.color.colorStroke));
        drawable.setColor(ContextCompat.getColor(getContext(), color));
        setBackgroundDrawable(drawable);
    }

    // Установить изображение на квадрат
    public void setImage(int image) {
        setImageResource(image);
    }
}
