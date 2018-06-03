package liza.weatherappdlc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ListView listView = (ListView)findViewById(R.id.listView);
        CustomAdapter adapter = new CustomAdapter(this,MainActivity.weatherResponse.getTemperatureList());
        listView.setAdapter(adapter);
    }
}
