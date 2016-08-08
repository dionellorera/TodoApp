package com.example.dione.todoapp;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
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
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dione.todoapp.Adapter.ListThingsAdapter;
import com.example.dione.todoapp.Entities.User;
import com.example.dione.todoapp.Model.Thing;
import com.facebook.FacebookSdk;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dione on 05/08/2016.
 */
public class ThingsListActivity extends AppCompatActivity implements View.OnClickListener {
    private com.example.dione.todoapp.AlarmManager alarmManager;
    private long idOfSelectedRecord = 0;
    private boolean add = false;
    private int counter = 1;
    private RecyclerView thingsRecyclerView;
    private ArrayList<Thing> thingArrayList;
    private ListThingsAdapter adapter;
    private DatePicker pickerDate;
    private TimePicker pickerTime;
    private Paint p = new Paint();
    private View view;
    private View dialogDatePickerView;
    private View dialogTimePickerView;
    private AlertDialog.Builder alertDialog;
    private AlertDialog.Builder timePickerDialog;
    private AlertDialog.Builder datePickerDialog;
    private int edit_position;
    private int pendingIntentId = 0;
    private EditText etDescription;
    private int hour;
    private int minute;
    private int year;
    private int month;
    private int date;

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
        getDevHashKey();
        FacebookSdk.sdkInitialize(getApplicationContext());
        alarmManager = new com.example.dione.todoapp.AlarmManager();
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
            case R.id.action_invite_facebook_friends:
                showFacebookInvite();
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
            thingArrayList.add(new Thing(thingListz.get(i).getId(),
                        thingListz.get(i).getDescription(),
                        thingListz.get(i).getYear(),
                        thingListz.get(i).getMonth(),
                        thingListz.get(i).getDate(),
                        thingListz.get(i).getHour(),
                        thingListz.get(i).getMinute(),
                        thingListz.get(i).getPendingIntentId()));
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

                    year = thingArrayList.get(position).getYear();
                    month = thingArrayList.get(position).getMonth();
                    date = thingArrayList.get(position).getDate();

                    hour = thingArrayList.get(position).getHour();
                    minute = thingArrayList.get(position).getMinute();

                    pendingIntentId = thingArrayList.get(position).getPendingIntentId();
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

    private void removeDatePickerParentView(){
        if(dialogDatePickerView.getParent()!=null) {
            ((ViewGroup) dialogDatePickerView.getParent()).removeView(dialogDatePickerView);
        }
    }

    private void removeTimePickerParentView(){
        if(dialogTimePickerView.getParent()!=null) {
            ((ViewGroup) dialogTimePickerView.getParent()).removeView(dialogTimePickerView);
        }
    }

    private void initDialog(){
        alertDialog = new AlertDialog.Builder(ThingsListActivity.this);
        timePickerDialog = new AlertDialog.Builder(ThingsListActivity.this);
        datePickerDialog = new AlertDialog.Builder(ThingsListActivity.this);
        view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
        dialogDatePickerView = getLayoutInflater().inflate(R.layout.dialog_datepicker,null);
        dialogTimePickerView = getLayoutInflater().inflate(R.layout.dialog_timepicker,null);
        timePickerDialog.setView(dialogTimePickerView);
        datePickerDialog.setView(dialogDatePickerView);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(add){
                    if (etDescription.getText().toString().isEmpty()){
                        etDescription.setError(getString(R.string.field_required));
                    }else{
                        dialog.dismiss();
                        removeDatePickerParentView();
                        datePickerDialog.show();
                    }
                } else {
                    if (etDescription.getText().toString().isEmpty()){
                        etDescription.setError(getString(R.string.field_required));
                    }else{
                        dialog.dismiss();
                        removeDatePickerParentView();
                        datePickerDialog.show();
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
        pickerDate = (DatePicker) dialogDatePickerView.findViewById(R.id.pickerDate);
        pickerTime = (TimePicker) dialogTimePickerView.findViewById(R.id.pickerTime);



        datePickerDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                year = pickerDate.getYear();
                month = pickerDate.getMonth();
                date = pickerDate.getDayOfMonth();
                removeTimePickerParentView();
                timePickerDialog.show();
            }
        });

        timePickerDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hour = pickerTime.getCurrentHour();
                minute = pickerTime.getCurrentMinute();

                if(add){
                    etDescription.setError(null);
                    add =false;
                    com.example.dione.todoapp.Entities.Thing thing = new com.example.dione.todoapp.Entities.Thing(etDescription.getText().toString(), year, month, date, hour, minute, pendingIntentId);
                    thing.save();
                    thingArrayList.add(new Thing(thing.getId(), etDescription.getText().toString(), year, month, date, hour, minute, pendingIntentId));
                    getUniqueId();
                    setAlarm();
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    if (etDescription.getText().toString().isEmpty()){
                        etDescription.setError(getString(R.string.field_required));
                    }else{
                        thingArrayList.set(edit_position,new Thing(idOfSelectedRecord, etDescription.getText().toString(), year, month, date, hour, minute, pendingIntentId));
                        com.example.dione.todoapp.Entities.Thing thing = com.example.dione.todoapp.Entities.Thing.findById(com.example.dione.todoapp.Entities.Thing.class, idOfSelectedRecord);
                        thing.setDescription(etDescription.getText().toString());
                        thing.setYear(year);
                        thing.setMonth(month);
                        thing.setDate(date);
                        thing.setHour(hour);
                        thing.setMinute(minute);
                        thing.save();
                        setAlarm();
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                }
                dialog.dismiss();
            }
        });
    }

    private void setAlarm(){
        alarmManager.setAlarm(getApplicationContext(), year, month, date, hour, minute, pendingIntentId, etDescription.getText().toString());
        Log.d("alarm_set_this_date", String.valueOf(year+"-"+month+"-"+date+"//"+hour+"-"+minute+",,"+pendingIntentId));
    }

    private void getUniqueId(){
        Random myRandom = new Random();
        pendingIntentId = myRandom.nextInt();
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

    private void getDevHashKey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.dione.todoapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {

        }
    }
    private void showFacebookInvite(){
        String appLinkUrl, previewImageUrl;
        appLinkUrl = "https://fb.me/602589753248003";
        previewImageUrl = "http://www.asendia.com/fileadmin/asendia/global/know-how/header/14-34_Direct_Mail_little_things_h.jpg";
        if (AppInviteDialog.canShow()){
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                    .build();
            AppInviteDialog.show(this, content);
        }
    }
}
