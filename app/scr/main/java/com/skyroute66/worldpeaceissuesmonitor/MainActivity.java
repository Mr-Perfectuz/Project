//
// MainActivity.java
//
// This is the code for the main screen
//
// This file is part of the course "Build a Firebase Android Application"
//
// Written by Harrison Kong @ coursera.org
//

package com.skyroute66.worldpeaceissuesmonitor;

//import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // properties

    private AlertDialog alertDialog = null;   // holds the reference to any open dialog box

    private ArrayList<Issue> issuesList = new ArrayList<Issue>();   // holds the issues

    // this adaptor is responsible to render the objects in our ArrayList to the screen
    private IssueListAdaptor issueListAdaptor = new IssueListAdaptor(this, issuesList);

    // helper method to open the add new issue dialog box
    public void openAddIssueDialog() {

        Intent intent = new Intent(this, AddIssueActivity.class);
        startActivity(intent);
    }

    // helper method to open the confirm delete dialog box
    private void openConfirmDeleteDialog(final int i) {

        alertDialog = new AlertDialog.Builder(this)
        .setTitle("Confirm Delete")
        .setMessage("Are you sure you want to delete this entry?\n\n" +
                issuesList.get(i).getDescription())
        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int button) {

                // use the DatabaseHelper object to delete it
                DatabaseHelper.deleteIssue(issuesList.get(i).myGetKey());
            }
        } )
        .setNegativeButton("Cancel", null)
        .setIcon(R.drawable.red_cross_circle)
        .setCancelable(false)
        .show();
    }

    // helper method to open the confirm status toggle dialog box
    public void openStatusToggleDialog(final int i) {

        String oldStatus = issuesList.get(i).getResolved();
        final String newStatus = (oldStatus.equals("no") ? "yes" : "no");

        alertDialog = new AlertDialog.Builder(this)
        .setTitle("Confirm Status Update")
        .setMessage("Update the resolved status this entry:\n\n" +
                issuesList.get(i).getDescription() + "\n\nfrom '" +
                oldStatus + "' to '" + newStatus + "'?")
        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int button) {

                // use the DatabaseHelper object to update the resolved status
                // Task 7: change the first parameter to the ID in the ArrayList item #i
                DatabaseHelper.updateIssue(issuesList.get(i).myGetKey(), newStatus);
            }
        } )
        .setNegativeButton("Cancel", null)
        .setIcon(newStatus == "yes" ? R.drawable.green_check_mark : R.drawable.red_hourglass)
        .setCancelable(false)
        .show();
    }

    // helper method to open the database error dialog box
    public void alertFirebaseFailure(DatabaseError error) {

        alertDialog = new AlertDialog.Builder(this)
                .setTitle("An error occurred while connecting to Firebase!")
                .setMessage(error.toString())
                .setPositiveButton("Dismiss", null)
                .setIcon(android.R.drawable.presence_busy)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        try {
            // this will send any local changes we make when we are offline to Firebase when
            // we are online again
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        catch (Exception e) {
            // ignore this because it will throw an exception when the device is rotated
            // and this method is called a second time
        }

        ListView myListView = (ListView) findViewById(R.id.issuesListView);

        myListView.setAdapter(issueListAdaptor);

        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                openConfirmDeleteDialog(i);
                return true;
            }
        });

        // let's read the issues from Google Firebase and (re)build the issues list!!

        // Task 4: Hardcode one entry for now (delete this block when done)
        String severity = "major";
        String resolved = "no";
        String description = "Complete Task 4!";

        // make a new Issue object and add it to the ArrayList
        issuesList.add(new Issue(severity, resolved, description));

        // Task 4: uncomment this block:
//        DatabaseReference dbRef // Task 4: get a reference from path /issues
//
//        dbRef.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                // this event is fired every time anything under /issues changes
//                // so we reconstruct the ArrayList and tell the adapter to update the screen
//                DatabaseHelper.buildIssuesList(snapshot, issuesList);
//                issueListAdaptor.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                alertFirebaseFailure(error);
//                error.toException();
//            }
//        });

        FloatingActionButton addButton = (FloatingActionButton)findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddIssueDialog();
            }
        });

    }

    @Override
    public void onPause() {

        super.onPause();
        if(alertDialog != null) {
            try {   // there is a chance the property is not null even though the dialog box has
                    // been dismissed, such as using the back button

                alertDialog.dismiss();  // dismiss prior to rotation to avoid memory leak
            } finally {
                alertDialog = null;
            }

        }
    }
}