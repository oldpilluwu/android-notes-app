package com.fawwaz.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NotesEditorActivity extends AppCompatActivity {

    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_editor);

        EditText editorView = (EditText) findViewById(R.id.editorView);
        EditText titleView = (EditText) findViewById(R.id.titleView);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);

        if(noteId != -1) {

            titleView.setText(MainActivity.titles.get(noteId));
            editorView.setText(MainActivity.descriptions.get(noteId));

        }else {

            MainActivity.titles.add("");
            MainActivity.descriptions.add("");
            noteId = MainActivity.titles.size() - 1;

        }

        editorView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.descriptions.set(noteId, String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        titleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.titles.set(noteId, String.valueOf(s));
                MainActivity.arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}