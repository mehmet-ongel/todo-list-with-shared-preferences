package com.techmania.todolistwithsharedpreferences;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    EditText item;
    Button add;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;

    SharedPreferences sharedPreferences;
    ArrayList<String> itemList = new ArrayList<>();
    Set<String> mySet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        item = findViewById(R.id.editText);
        add = findViewById(R.id.button);
        listView = findViewById(R.id.list);

        readData();

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1
                ,android.R.id.text1,itemList);

        listView.setAdapter(arrayAdapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemName = item.getText().toString();
                itemList.add(itemName);
                item.setText("");
                writeData(itemList);
                arrayAdapter.notifyDataSetChanged();

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Delete");
                alert.setMessage("Do you want to delete this item from the list?");
                alert.setCancelable(false);
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        itemList.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                        writeData(itemList);

                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();

            }
        });

    }

    public void writeData(ArrayList<String> items){

        sharedPreferences = this.getSharedPreferences("listinfo.dat", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        mySet = new HashSet<>(items);
        editor.putStringSet("todo",mySet);
        editor.apply();

    }

    public void readData(){
        sharedPreferences = this.getSharedPreferences("listinfo.dat", Context.MODE_PRIVATE);
        mySet = sharedPreferences.getStringSet("todo",null);
        if (mySet != null){
            itemList.addAll(mySet);
        }

    }
}