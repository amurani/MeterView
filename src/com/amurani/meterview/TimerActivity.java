package com.amurani.meterview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TimerActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thread_layout);
		
		final MeterView meter = (MeterView) this.findViewById(R.id.meter);
		
		final EditText upto = (EditText) this.findViewById(R.id.upto);
		final EditText interval = (EditText) this.findViewById(R.id.interval);
		
		upto.setText("50");
		interval.setText("100");
		
		final Button count = (Button) this.findViewById(R.id.count);
		final Button reset = (Button) this.findViewById(R.id.reset);
		
		reset.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (upto.getText().toString().length() > 0 && interval.getText().toString().length() > 0) {
					if (reset.getText().toString().matches("Set")) {
						reset.setText("Reset");
						count.setText("Count");
						meter.prepare(
								Integer.parseInt(upto.getText().toString()),
								Integer.parseInt(interval.getText().toString())
								);
						count.setEnabled(true);
						upto.setEnabled(false);
						interval.setEnabled(false);
					} else {
						reset.setText("Set");
						meter.stop();
						count.setEnabled(false);
						upto.setEnabled(true);
						interval.setEnabled(true);
					}
				} else {
					Toast.makeText(getApplicationContext(), "We all need to start somewhere and rest once in a while.", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		count.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (count.getText().toString().matches("Count")) {
					count.setText("Pause");
					meter.start();
				} else {
					count.setText("Count");
					meter.pause();
				}
			}
		});
		
		meter.setOnFinishListener(new OnFinishListener() {
			public void onFinish() {
				count.setText("Count");
				Toast.makeText(getApplicationContext(), "Done.", Toast.LENGTH_LONG).show();
			}
		});
	}
}
