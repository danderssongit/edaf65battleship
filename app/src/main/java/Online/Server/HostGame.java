package Online.Server;

import android.content.ClipData;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import se.lth.soc13dan.battleshipsedaf65.R;
import se.lth.soc13dan.battleshipsedaf65.Square;

public class HostGame extends AppCompatActivity {
    private static final int NBR_ITEMS = 100;
    private GridLayout mGrid;
    private Random rand;
    private Square selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGrid = (GridLayout) findViewById(R.id.grid_layout);
        mGrid.setOnDragListener(new DragListener());

        Button shootButton = (Button) this.findViewById(R.id.angry_btn);
        shootButton.setOnClickListener(new ButtonListener());
        final LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 1; i <= NBR_ITEMS; i++) {
            final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
            final TextView text = itemView.findViewById(R.id.text);
            itemView.setTag(new Square(i));
            itemView.setOnLongClickListener(new LongPressListener());
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final TextView text = v.findViewById(R.id.text);
                    selected = (Square)v.getTag();
                    text.setText(String.valueOf("X"));

                }
            });
            text.setText(String.valueOf(i));
            mGrid.addView(itemView);
        }


    }

    class LongPressListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            final ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    class DragListener implements View.OnDragListener {
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

    class ButtonListener implements android.view.View.OnClickListener {
        public void onClick(View v) {
            View parent = (View)v.getParent();
            if (parent != null) {
                Button p1_button = (Button)findViewById(R.id.angry_btn);
                p1_button.setText(selected.getCoord());
            }
        }
    }
}

