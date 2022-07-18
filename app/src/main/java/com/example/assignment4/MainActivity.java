package com.example.assignment4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase rootNode, Node;
    DatabaseReference subNode;
    //int maxNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button questionButton = (Button) findViewById(R.id.questionButton);
        questionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance("https://assignment4-b159c-default-rtdb.firebaseio.com/");
                subNode = rootNode.getReference().child("question");
                subNode.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            long allNum = dataSnapshot.getChildrenCount();
                            int maxNum = (int)allNum;
                            int minNum = 1;
                            int randomNum = new Random().nextInt(maxNum - minNum + 1) + minNum;

                            int count = 0;
                            Iterable<DataSnapshot> ds = dataSnapshot.getChildren();
                            Iterator<DataSnapshot> ids = ds.iterator();
                            String newPost = "";

                            while(ids.hasNext() && count < randomNum) {
                                newPost = (String) ids.next().getValue();
                                count ++; // used as positioning.
                            }
                            ((TextView)findViewById(R.id.questionText)).setText(newPost);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        Button insertButton = (Button) findViewById(R.id.insertButton);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInsertQuestion();
            }
        });
        Button clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView)findViewById(R.id.questionText)).setText("QUESTION RESET");
            }
        });
    }

    private void openInsertQuestion() {
        Intent intent = new Intent(this, insertActivity.class);
        startActivity(intent);
    }
}