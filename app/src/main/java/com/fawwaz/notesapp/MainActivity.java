package com.fawwaz.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> titles, descriptions;
    static ArrayAdapter<String> arrayAdapter;
    static SQLiteDatabase myNotesDatabase;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.addNote){

            Intent intent = new Intent(getApplicationContext(), NotesEditorActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listview);
        titles = new ArrayList<String>();
        descriptions = new ArrayList<String>();

        myNotesDatabase = this.openOrCreateDatabase("notesDatabase", MODE_PRIVATE, null);
        myNotesDatabase.execSQL("CREATE TABLE IF NOT EXISTS notes (id INT PRIMARY KEY, title VARCHAR, description VARCHAR)");

        Cursor cursor = myNotesDatabase.rawQuery("SELECT * FROM notes", null);
        int titleIndex, descIndex;

        if(cursor == null) {
            titles.add("Example note");
            descriptions.add("This is the beginning");
        }
        else{

            titleIndex = cursor.getColumnIndex("title");
            descIndex = cursor.getColumnIndex("description");


            cursor.moveToFirst();
            while (!cursor.isAfterLast()){

                Log.i("mama", Integer.toString(titleIndex) + cursor.getString(titleIndex));

                titles.add(cursor.getString(titleIndex));
                descriptions.add(cursor.getString(descIndex));

                cursor.moveToNext();
            }

            cursor.close();
        }
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), NotesEditorActivity.class);
                intent.putExtra("noteId", position);
                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                titles.remove(position);
                                descriptions.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }


    @Override
    protected void onStop() {
        updateDatabase();
        super.onStop();
    }

    static public void updateDatabase(){
        myNotesDatabase.execSQL("DELETE FROM notes");

        for(int i = 0; i < titles.size(); i++){

            myNotesDatabase.execSQL("INSERT INTO notes (id, title, description) VALUES (?,?,?)", new String[]{Integer.toString(i), titles.get(i), descriptions.get(i)});

        }
        myNotesDatabase.close();
    }
}