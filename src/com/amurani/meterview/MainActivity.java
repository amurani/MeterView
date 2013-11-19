package com.amurani.meterview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button thread = (Button) this.findViewById(R.id.thread);
		thread.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, TimerActivity.class));
			}
		});
		
		Button progress = (Button) this.findViewById(R.id.progress);
		progress.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, ProgressActivity.class));
			}
		});
		
		Button battery = (Button) this.findViewById(R.id.battery);
		battery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, BatteryActivity.class));
			}
		});
	}

}