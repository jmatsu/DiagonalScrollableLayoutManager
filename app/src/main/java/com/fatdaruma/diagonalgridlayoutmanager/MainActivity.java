package com.fatdaruma.diagonalgridlayoutmanager;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        DiagonalScrollableGridLayoutManager layoutManager = new DiagonalScrollableGridLayoutManager(this, 10, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setScale(5f);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new Adapter(this));
//        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if (recyclerView.getHeight() > 0) {
//                    recyclerView.getLayoutParams().height *= 2;
//                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    recyclerView.requestLayout();
//
//                }
//            }
//        });
    }

    private static class Adapter extends RecyclerView.Adapter {
        private final Context context;

        private Adapter(Context context) {
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(new TextView(context));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).populate(String.valueOf(position));
            ((ViewHolder) holder).randomSize();
        }

        @Override
        public int getItemCount() {
            return 100;
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }

        void populate(String item) {
            textView.setText(item);
        }

        void randomSize() {
        }
    }
}
