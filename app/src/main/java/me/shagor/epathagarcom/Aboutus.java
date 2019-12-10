package me.shagor.epathagarcom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class Aboutus extends AppCompatActivity {

    TextView developer;
    WebView wb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        developer = (TextView) findViewById(R.id.developer);

        developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SourceCodeSoft.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"SourceCode Soft",Toast.LENGTH_LONG).show();
            }
        });

    }


    // Share Top Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        menu.findItem(R.id.action_dev).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                finish();
                return true;
        }

        return true;
    }
}