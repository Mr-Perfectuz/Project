//
// DatabaseHelper.java
//
// This object encapsulates all the Firebase functionality of our application
//
// This file is part of the course "Build a Firebase Android Application"
//
// Written by Harrison Kong @ coursera.org
//

package com.skyroute66.worldpeaceissuesmonitor;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;


public final class DatabaseHelper {

    public static final void buildIssuesList(@NonNull DataSnapshot snapshot, ArrayList<Issue> issuesList) {

        issuesList.clear();  // clear the old data

        for (DataSnapshot issue : snapshot.getChildren()) {

            // retrieve the key
            // Task 4: read from database
            // retrieve the severity
            // Task 4: read from database
            // retrieve the resolved status
            // Task 4: read from database
            // retrieve the description
            // Task 4: read from database

            // make a new Issue object and add it to the ArrayList
            // Task 4: add it to the list
        }

    }

    public static final void addNewIssue(Issue newIssue) {

        // use the reference to root node /issues
        // Task 5: build the reference and call it rootRef, the path is /issues"

        // to push a new child node and get the reference to it

        // set the children nodes of this new reference to our object's properties

    }

    public static final void deleteIssue(String keyToDelete) {

        // get a reference to the issue child node to be deleted
        // Task 6: build the reference and call it issueRef, the path is /issues/<keyToDelete>"

        // remove the child node and its children
    }

    public static final void updateIssue(String issueKey, String newStatus) {

        // get a reference to the child node to be updated and to the "resolved" child node
        // Task 7: build the reference and call it issueRef, the path is /issues/<issueKey>/resolved"

        // set the value of the node

    }
}
