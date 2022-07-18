package com.example.assignment4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class insertActivity extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference subNode;
    public String question;
    public long maxId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        EditText questionInput = (EditText) findViewById(R.id.questionInput);
        String questionInputText = questionInput.getText().toString();

        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance("https://assignment4-b159c-default-rtdb.firebaseio.com/");
                subNode = rootNode.getReference().child("question");
                subNode.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            maxId = (dataSnapshot.getChildrenCount());
                            boolean valid = true;
                            String errorMessage = "";
                            if(questionInput.getText().toString().isEmpty()){
                                errorMessage += "question is empty";
                                valid = false;
                            }
                            if(questionInput.getText().toString().trim().length() >= 120){
                                errorMessage += "question is too long";
                                valid = false;
                            }
                            subNode.orderByChild("ID").equalTo(questionInput.getText().toString());
                            if(valid){
                                for(DataSnapshot data: dataSnapshot.getChildren()){
                                    if (data.child(questionInput.getText().toString()).exists()) {
                                        valid = false;
                                        Toast.makeText(insertActivity.this, "BAD CHILD", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //do something if not exists
                                    }
                                }
                                errorMessage += "question is already in bank";
                            }
                            if(valid){
                                subNode.child(String.valueOf(maxId+1)).setValue(questionInput.getText().toString());
                                ((TextView)findViewById(R.id.promptText)).setText(questionInput.getText().toString());
                                questionInput.setText("");
                                Toast.makeText(insertActivity.this, "Successfully added question", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(insertActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}