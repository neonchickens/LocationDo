package com.example.locationdo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private static final String TAG = "ListActivity";
    ArrayList<Task> toDoList;
    TaskAdapter listAdapter;
    private static final String DB_URL = "jdbc:jtds:sqlserver://3.87.197.166:1433/LocationDo;user=LocationDo;password=CitSsd!";

    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        id = intent.getIntExtra("id", -1);

        if(id == -1){
            // TODO - send back to login
        }

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

        // TODO - populate array list from remote server
        loadTasks();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    public void loadTasks(){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection con = DriverManager.getConnection(DB_URL);

            Statement statement = con.createStatement();

            ResultSet resultat = statement.executeQuery(
                        "SELECT * " +
                            "FROM TASK " +
                            "JOIN USERTASK ON (TASK.ID = USERTASK.TASK_ID)" +
                                "WHERE USERTASK.ACCOUNT_ID =" + id);

            while (resultat.next()) {
                Task newTask = new Task(resultat.getInt("id"), (resultat.getByte("status")!= 0),
                        resultat.getString("name"), resultat.getString("description"),
                        resultat.getString("latitude"), resultat.getString("longitude"));
                Log.v(resultat.getString("name") + ": ", String.valueOf(resultat.getByte("status") != 0));
                listAdapter.add(newTask);
                Toast.makeText(this,"Loaded tasks from remote.", Toast.LENGTH_LONG);
            }
            resultat.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // when the add button is pressed
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

                        // TODO - try to get and set lat&long
                        float latitude = 0, longitutde = 0;

                        // create task object to save
                        Task newTask = new Task(title, desc);
                        toDoList.add(newTask);

                        Intent intent = new Intent(getApplicationContext(), com.example.locationdo.MapsSelector.class);
                        intent.putExtra("title", title);
                        intent.putExtra("desc", desc);
                        intent.putExtra("id", id);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    // open edit/delete dialog when edit button is pressed
    public void onEditClick(final int position, final Task task){
        /*
        An alert dialog can only hold one editText by default, so we create a layout
        view to hold the two fields we need.
         */
        LinearLayout layout = new LinearLayout(ListActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText titleEditText = new EditText(ListActivity.this);
        titleEditText.setText(task.strTitle);
        layout.addView(titleEditText);

        final EditText descEditText = new EditText(ListActivity.this);
        descEditText.setText(task.strDesc);
        layout.addView(descEditText);

        // could add an update location button here

        // create alert dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit task")
                .setView(layout)

                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = String.valueOf(titleEditText.getText());
                        String desc = String.valueOf(descEditText.getText());
                        Log.d(TAG, "Task to edit: " + title);

                        // create task object to save
                        Task newTask = new Task(title, desc);
                        toDoList.set(position, newTask);
                        // TODO - update remote server task

                        try {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            Class.forName("net.sourceforge.jtds.jdbc.Driver");
                            Connection con = DriverManager.getConnection(DB_URL);

                            Statement statement = con.createStatement();
                            int result = statement.executeUpdate("UPDATE TASK SET name = '" + title + "', description = '" + desc + "' WHERE ID = '" + task.taskID + "'");

                            statement.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)

                //delete button
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toDoList.remove(position);
                        listAdapter.notifyDataSetChanged();
                        // TODO - delete remote server task

                        try {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            Class.forName("net.sourceforge.jtds.jdbc.Driver");
                            Connection con = DriverManager.getConnection(DB_URL);

                            Statement statement = con.createStatement();
                            int result = statement.executeUpdate("DELETE FROM USERTASK WHERE TASK_ID = '" + task.taskID + "'");

                            statement = con.createStatement();
                            result = statement.executeUpdate("DELETE FROM TASK WHERE ID = '" + task.taskID + "'");

                            statement.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .create();
        dialog.show();

    }

    // TODO - go to map activity
    public void onLocationClick(final int position, Task task){

        Intent intent = new Intent(getApplicationContext(), com.example.locationdo.MapsActivity.class);
        intent.putExtra("latitude", task.strLat);
        intent.putExtra("longitude", task.strLong);
        intent.putExtra("title", task.strTitle);
        startActivity(intent);
    }

    // custom data adapter to connect listview to arraylist & init each list item
    public class TaskAdapter extends ArrayAdapter<Task>  {
        public TaskAdapter(Context context, ArrayList<Task> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            final Task task = getItem(position);
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

            // get buttons
            CheckBox chkBox = (CheckBox)convertView.findViewById(R.id.checkBox);
            Button btnEdit = (Button)convertView.findViewById(R.id.btnEdit);
            ImageButton btnLoc = (ImageButton)convertView.findViewById(R.id.btnLocation);

            chkBox.setChecked(task.boolStatus);

            // set button listeners
            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    toDoList.get(position).setCompletion(isChecked);

                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        Class.forName("net.sourceforge.jtds.jdbc.Driver");
                        Connection con = DriverManager.getConnection(DB_URL);

                        Statement statement = con.createStatement();
                        int result = statement.executeUpdate("UPDATE TASK SET STATUS = '" + (isChecked ? 1: 0) + "' WHERE ID = '" + task.taskID + "'");

                        statement.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    onEditClick(position, task);
                }
            });

            btnLoc.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    onLocationClick(position, task);
                }
            });

            // Return the completed view to render on screen
            return convertView;
        }
    }

    // custom task object
    public class Task{

        private int taskID;
        private String strTitle;
        private String strDesc;
        private boolean boolStatus;
        private String strLat;
        private String strLong;

        Task(int inID, boolean status, String task, String desc, String lat, String longi){
            taskID = inID;
            boolStatus = status;
            strTitle = task;
            strDesc = desc;
            strLat = lat;
            strLong = longi;
        }

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

        public void setCompletion(boolean status){
            boolStatus = status;
        }
    }
}