package com.example.dione.todoapp;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dione.todoapp.Adapter.ListThingsAdapter;
import com.example.dione.todoapp.Entities.User;
import com.example.dione.todoapp.Model.Thing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dione on 05/08/2016.
 */
public class ThingsListActivity extends AppCompatActivity implements View.OnClickListener {
    long idOfSelectedRecord = 0;
    private boolean add = false;
    private int counter = 1;
    RecyclerView thingsRecyclerView;
    ArrayList<Thing> thingArrayList;
    ListThingsAdapter adapter;
    private Paint p = new Paint();
    private View view;
    AlertDialog.Builder alertDialog;
    int edit_position;
    EditText etDescription;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_things_list);
        setupToolBar();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        createData();
        initializeRecyclerView();
        initDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (counter==2){
            super.onBackPressed();
        }else{
            counter++;
            Toast.makeText(getApplicationContext(), R.string.toast_back_to_exit, Toast.LENGTH_SHORT).show();
        }
    }

    //user created methods
    private void setupToolBar(){
        User user = User.findById(User.class, 1);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.showOverflowMenu();
        myToolbar.setTitle(String.format("%s - %s, %s", getString(R.string.app_title), user.getLastName(), user.getFirstName()));
        setSupportActionBar(myToolbar);
    }

    private void createData(){
        thingArrayList = new ArrayList<>();
        List<com.example.dione.todoapp.Entities.Thing> thingListz = com.example.dione.todoapp.Entities.Thing.listAll(com.example.dione.todoapp.Entities.Thing.class);
        for (int i=0;i<thingListz.size();i++){
            thingArrayList.add(new Thing(thingListz.get(i).getId(), thingListz.get(i).getDescription()));
        }
    }

    private void initializeRecyclerView(){
        thingsRecyclerView = (RecyclerView)findViewById(R.id.listRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        thingsRecyclerView.setLayoutManager(layoutManager);
        adapter = new ListThingsAdapter(thingArrayList);
        thingsRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        initSwipe();
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    com.example.dione.todoapp.Entities.Thing thing = com.example.dione.todoapp.Entities.Thing.findById(com.example.dione.todoapp.Entities.Thing.class, thingArrayList.get(position).getId());
                    thing.delete();
                    thingArrayList.remove(position);
                    adapter.notifyItemRemoved(position);
                } else {
                    removeView();
                    edit_position = position;
                    alertDialog.setTitle("Edit Description");
                    idOfSelectedRecord = thingArrayList.get(position).getId();
                    etDescription.setText(thingArrayList.get(position).getToDoDescription());
                    alertDialog.show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_redo_black_24dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_delete_black_24dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(thingsRecyclerView);
    }

    private void removeView(){
        if(view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    private void initDialog(){
        alertDialog = new AlertDialog.Builder(ThingsListActivity.this);
        view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(add){
                    if (etDescription.getText().toString().isEmpty()){
                        etDescription.setError(getString(R.string.field_required));
                    }else{
                        etDescription.setError(null);
                        add =false;
                        com.example.dione.todoapp.Entities.Thing thing = new com.example.dione.todoapp.Entities.Thing(etDescription.getText().toString());
                        thing.save();

                        thingArrayList.add(new Thing(thing.getId(), etDescription.getText().toString()));
                        dialog.dismiss();
                    }
                } else {
                    if (etDescription.getText().toString().isEmpty()){
                        etDescription.setError(getString(R.string.field_required));
                    }else{
                        thingArrayList.set(edit_position,new Thing(idOfSelectedRecord, etDescription.getText().toString()));
                        com.example.dione.todoapp.Entities.Thing thing = com.example.dione.todoapp.Entities.Thing.findById(com.example.dione.todoapp.Entities.Thing.class, idOfSelectedRecord);
                        thing.setDescription(etDescription.getText().toString());
                        thing.save();
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                }
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                adapter.notifyDataSetChanged();
            }
        });
        etDescription = (EditText)view.findViewById(R.id.etDescription);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                removeView();
                add = true;
                alertDialog.setTitle("Add Description");
                etDescription.setText("");
                alertDialog.show();
                break;
        }
    }
}
