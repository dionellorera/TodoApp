package com.example.dione.todoapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
    private ArrayList<Thing> thingArrayList;

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
    }

    @Override
    public int getItemCount() {
        return thingArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvThingTodo;
        public ViewHolder(View itemView) {
            super(itemView);
            tvThingTodo = (TextView) itemView.findViewById(R.id.tv_list_thing_to_do);
        }
    }
}
