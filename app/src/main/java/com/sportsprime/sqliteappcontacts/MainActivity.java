package com.sportsprime.sqliteappcontacts;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
 ArrayList<User> users = new ArrayList<>();
    final String LOG_TAG = "myLogs";
    Button btnAdd,btnRead,btnClear;
    EditText etName,etEmail;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = findViewById(R.id.btnAdd);
        btnRead = findViewById(R.id.btnRead);
        btnClear = findViewById(R.id.btnClear);

        btnAdd.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {
        ContentValues cv = new ContentValues();
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (v.getId()){
            case(R.id.btnAdd):
                Log.d(LOG_TAG,"----- Insert in mytable: ---");
              cv.put("name",name);
              cv.put("email",email);
              long rowId = db.insert("mytable",null,cv);
                Log.d(LOG_TAG,"----- row inserted, ID = " + rowId);
                Toast.makeText(MainActivity.this, "Insert object BD", Toast.LENGTH_LONG).show();
                Log.d(LOG_TAG,"toast");

                break;
            case (R.id.btnRead):
                Log.d(LOG_TAG,"----- Rows in mytable: ---");
                Cursor c = db.query("mytable",null,null,null,null,null,null);
                if (c.moveToFirst()){
                    int idColIndex = c.getColumnIndex("id");
                    int nameColIndex = c.getColumnIndex("name");
                    int emailColIndex = c.getColumnIndex("email");

                    do {
                        int idDB = c.getInt(idColIndex);
                       String nameDB = c.getString(nameColIndex);
                        String emailDB =c.getString(emailColIndex);
                        User user = new User(idDB, nameDB, emailDB);
                        users.add(user);
                        Log.d(LOG_TAG,"ID = " + idDB+
                                ", name = " + nameDB+
                                ", email = " + emailDB);




                    }while (c.moveToNext());

                    RecyclerView recyclerView = findViewById(R.id.recycler);

                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(new UserAdapter(getApplicationContext(), users));
                }else
                    Log.d(LOG_TAG,"0 rows");
                c.close();
                break;
            case(R.id.btnClear):
                Log.d(LOG_TAG,"---- Clear mytable: ---");
                int clearCount = db.delete("mytable",null,null);
                Log.d(LOG_TAG,"deleted rows count = " + clearCount);
                Toast.makeText(MainActivity.this, "DB deleted", Toast.LENGTH_LONG).show();
                break;
        }
        dbHelper.close();
    }
}