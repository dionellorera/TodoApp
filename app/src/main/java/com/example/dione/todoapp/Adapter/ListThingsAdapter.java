package com.example.dione.todoapp.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dione.todoapp.Model.Thing;
import com.example.dione.todoapp.R;

import java.util.ArrayList;

/**
 * Created by dione on 05/08/2016.
 */
public class ListThingsAdapter extends RecyclerView.Adapter<ListThingsAdapter.ViewHolder> {
    private final ArrayList<Thing> thingArrayList;

    public ListThingsAdapter(ArrayList<Thing> thingArrayList){
        this.thingArrayList = thingArrayList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_things_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvThingTodo.setText(thingArrayList.get(position).getToDoDescription());
        holder.tvListDate.setText(String.format("%d%s%d%s%d, %d%s%d",
                    thingArrayList.get(position).getYear(),
                    "-",
                    thingArrayList.get(position).getMonth() + 1,
                    "-",
                    thingArrayList.get(position).getDate(),
                    thingArrayList.get(position).getHour(),
                    ":",
                    thingArrayList.get(position).getMinute()));
        holder.cardViewRow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return thingArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvThingTodo;
        private final TextView tvListDate;
        private final CardView cardViewRow;
        public ViewHolder(View itemView) {
            super(itemView);
            tvThingTodo = (TextView) itemView.findViewById(R.id.tv_list_thing_to_do);
            tvListDate = (TextView) itemView.findViewById(R.id.tv_list_date);
            cardViewRow = (CardView) itemView.findViewById(R.id.cardViewRow);
        }
    }
}
