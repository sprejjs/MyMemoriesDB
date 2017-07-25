package com.spreys.mymemoriesdb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.GridView;

import com.spreys.mymemoriesdb.R;
import com.spreys.mymemoriesdb.db.MemoriesAdapter;
import com.spreys.mymemoriesdb.db.MemoryDbHelper;

public class MainActivity extends AppCompatActivity {
    private MemoryDbHelper dbHelper;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.gridView = (GridView) findViewById(R.id.activity_main_grid_view);
        this.dbHelper = new MemoryDbHelper(this);
        this.gridView.setAdapter(new MemoriesAdapter(this, this.dbHelper.readAllMemories(), false));
        this.gridView.setEmptyView(findViewById(R.id.activity_main_empty_view));
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((CursorAdapter)gridView.getAdapter()).swapCursor(this.dbHelper.readAllMemories());
    }

    public void addNewMemory(View view) {
        Intent intent = new Intent(this, NewMemoryActivity.class);
        startActivity(intent);
    }
}
