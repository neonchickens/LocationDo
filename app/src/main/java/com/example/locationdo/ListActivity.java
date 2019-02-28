package com.example.locationdo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private static final String TAG = "ListActivity";
    ArrayList<Task> toDoList;
    TaskAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddClick();
            }
        });

        toDoList = new ArrayList<Task>();
        listAdapter = new TaskAdapter(this, toDoList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(listAdapter);
    }

    // when the add button is clicked
    public void onAddClick(){

        /*
        An alert dialog can only hold one editText by default, so we create a layout
        view to hold the two fields we need.
         */
        LinearLayout layout = new LinearLayout(ListActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText titleEditText = new EditText(ListActivity.this);
        titleEditText.setHint("Task");
        layout.addView(titleEditText);

        final EditText descEditText = new EditText(ListActivity.this);
        descEditText.setHint("Details");
        layout.addView(descEditText);

        // create alert dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add a new task")
                .setView(layout)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = String.valueOf(titleEditText.getText());
                        Log.d(TAG, "Task to add: " + title);

                        String desc = String.valueOf(descEditText.getText());

                        // create task object to save
                        Task newTask = new Task(title, desc);
                        toDoList.add(newTask);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public void listUpdate(){

    }

    public class TaskAdapter extends ArrayAdapter<Task> {
        public TaskAdapter(Context context, ArrayList<Task> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Task task = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            }

            // Lookup view for data population
            TextView tvTitle = (TextView) convertView.findViewById(R.id.taskTitle);
            TextView tvDesc = (TextView) convertView.findViewById(R.id.taskDesc);

            // Populate the data into the template view using the data object
            tvTitle.setText(task.strTitle);
            tvDesc.setText(task.strDesc);

            // Return the completed view to render on screen
            return convertView;
        }
    }


    public class Task{

        private int id;
        private String strTitle;
        private String strDesc;
        private boolean boolStatus;
        private String strLat;
        private String strLong;

        Task(String task, String desc, String lat, String longi){
            boolStatus = false;
            strTitle = task;
            strDesc = desc;
            strLat = lat;
            strLong = longi;
        }

        Task(String task, String desc){
            boolStatus = false;
            strTitle = task;
            strDesc = desc;
        }

    }

}
