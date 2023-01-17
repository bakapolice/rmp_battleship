package com.rmpcourse.battleship.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

public class LeaderboardItemDivider extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable divider;


    /**
     * Конструктор загружает дефолтный разделитель
     */
    @SuppressLint({"ResourceType", "Recycle"})
    public LeaderboardItemDivider(Context context){
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        divider = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    /**
     * Конструктор загружает кастомный разделитель
     */
    public LeaderboardItemDivider(Context context, int resID){
        divider = ContextCompat.getDrawable(context, resID);
    }

    //Рисование разделителей элементов списка в RecyclerView
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        //Вычисление координат x для всех разделителей
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        //Для каждого элемента, кроме последнего, нарисовать линию
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i); //Получить i-тый элемент списка

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            //Вычисление координат y (игрек) текущего разделителя
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            //Рисование разделителя с вычисленными границами
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}
