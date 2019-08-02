package com.example.mysqlliteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText mFname,mLname,mId;
    Button mSave,mDel,mView;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFname=findViewById(R.id.edtFname);
        mLname=findViewById(R.id.edtLname);
        mId=findViewById(R.id.edtIdno);
        mSave=findViewById(R.id.btnSave);
        mDel=findViewById(R.id.btnDel);
         mView=findViewById(R.id.btnView);

        //Create the database
        db=openOrCreateDatabase("huduma",MODE_PRIVATE,null);

        //Create a table in your database
        db.execSQL("CREATE TABLE IF NOT EXISTS citizens(first_name VARCHAR,last_name VARCHAR,id_number INTEGER)");

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the data from the user
                String firstName= mFname.getText().toString();
                String lastName= mFname.getText().toString();
                String idNumber= mId.getText().toString().trim();

                //Check if thr user is attempting to submit empty fields
                if (firstName.isEmpty()){
                    messages("First Name Error","Please Enter the First Name ");
                }else if (lastName.isEmpty()){
                    messages("Last Name Error","Please Enter the Last Name");
                } else if(idNumber.isEmpty()){
                    messages("ID number error","Please Enter the ID");
                }
                else {
                    //Proceed to save the data into your db called huduma
                    //Insert data into the DB using an insert query

                    db.execSQL("INSERT INTO citizens VALUES ('"+firstName+"','"+lastName+"','"+idNumber+"') ");
                    messages("SUCCESS","User Saved Succesfully");

                    //Clear input fields for the next entry
                    mFname.setText("");
                    mLname.setText("");
                    mId.setText("");
                }
            }
        });
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Use a cursor to query and select data from your db table
                Cursor cursor=db.rawQuery("SELECT * FROM citizens", null);

                //Check if the cursor found any data in the db
                if (cursor.getCount() == 0){
                    messages("Empty Database","Sorry , No Data was found");
                }else {
                    //Proceed to display the selected data
                    //Use the String Buffer to append and display the records
                    StringBuffer buffer = new StringBuffer();

                    //Loop through the selected data that is on your cursor to display
                    while (cursor.moveToNext()){
                        buffer.append(cursor.getString(0)+"\t"); //Zero is a column for fname .//t ni tab halafu n ni new line
                        buffer.append(cursor.getString(1)+"\t"); //Zero is a column for lname
                        buffer.append(cursor.getString(2)+"\n"); //Zero is a column for idno
                    }
                    //Display your data using the string buffer on the message dialog
                    messages("DATABASE RECORDS",buffer.toString());
                }
            }
        });
        mDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the ID to use as a unique identifier to delete any row
                String id=mId.getText().toString().trim();
                //Check if the user deleting with an empty field

                if (id.isEmpty()) {
                    messages("ID error","Please enter the ID number");
                }
                else {
                    Cursor cursor= db.rawQuery("SELECT * FROM citizens WHERE id_number = '"+id+"'",null);
                    //Proceed to delete
                    if (cursor.moveToFirst()){
                        db.execSQL("DELETE FROM citizens WHERE id_number = '"+id+"',null");
                        messages("SUCCESS","User Deleted Successfully");
                        mId.setText("");
                    }
                }

            }
        });
    }
    //Message display function
    public  void messages(String title,String message){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.create().show();
    }
}
