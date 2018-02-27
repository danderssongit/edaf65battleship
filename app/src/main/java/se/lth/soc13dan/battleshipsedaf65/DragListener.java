package se.lth.soc13dan.battleshipsedaf65;

import android.support.v7.widget.GridLayout;
import android.view.DragEvent;
import android.view.View;

public class DragListener implements View.OnDragListener {
    private GridLayout mGrid;

    public DragListener(GridLayout mGrid) {
        this.mGrid = mGrid;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        final View view = (View) event.getLocalState();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_LOCATION:
                // do nothing if hovering above own position
                if (view == v) return true;
                // get the new list index
                final int index = calculateNewIndex(event.getX(), event.getY());
                // remove the view from the old position
                mGrid.removeView(view);
                // and push to the new
                mGrid.addView(view, index);
                break;
            case DragEvent.ACTION_DROP:
                view.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                if (!event.getResult()) {
                    view.setVisibility(View.VISIBLE);
                }
                break;
        }
        return true;
    }

    private int calculateNewIndex(float x, float y) {
        // calculate which column to move to
        final float cellWidth = mGrid.getWidth() / mGrid.getColumnCount();
        final int column = (int)(x / cellWidth);

        // calculate which row to move to
        final float cellHeight = mGrid.getHeight() / mGrid.getRowCount();
        final int row = (int)Math.floor(y / cellHeight);

        // the items in the GridLayout is organized as a wrapping list
        // and not as an actual grid, so this is how to get the new index
        int index = row * mGrid.getColumnCount() + column;
        if (index >= mGrid.getChildCount()) {
            index = mGrid.getChildCount() - 1;
        }
        return index;
    }
}