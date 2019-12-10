package me.shagor.epathagarcom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class InternetConnection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_connection);

        Toast.makeText(getApplicationContext(),"Please connection to Internet",Toast.LENGTH_LONG).show();

       }
}
