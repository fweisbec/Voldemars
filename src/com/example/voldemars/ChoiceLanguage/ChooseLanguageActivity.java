package com.example.voldemars.ChoiceLanguage;

import com.example.voldemars.R;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.app.Activity;

public class ChooseLanguageActivity extends Activity {	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Button latvian, russian;
    	ChoiceLanguageButtonListener listener;
    	
    	super.onCreate(savedInstanceState);	

    	setContentView(R.layout.choose_language);
    	latvian = (Button) findViewById(R.id.latvian_button);
    	russian = (Button) findViewById(R.id.russian_button);

    	listener = new ChoiceLanguageButtonListener(this, latvian, russian); 
    	latvian.setOnClickListener(listener);
    	russian.setOnClickListener(listener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }

}