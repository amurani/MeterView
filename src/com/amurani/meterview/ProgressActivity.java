package com.amurani.meterview;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ProgressActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_layout);
		
		final MeterView meter = (MeterView) this.findViewById(R.id.meter);
		meter.runAsync(false);
		meter.setOnFinishListener(new OnFinishListener() {
			public void onFinish() {
				Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
			}
		});
		
		final Button run = (Button) this.findViewById(R.id.run);
		final Button download = (Button) this.findViewById(R.id.download);
		
		run.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				run.setEnabled(false);
				download.setEnabled(false);
				
				new Thread(new Runnable() {
					public void run() {
						final int stop = 100;
						for (int i = 1; i <= stop; i++) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) { }
							
							final int n = i;
							runOnUiThread(new Runnable() {
								public void run() {
									meter.setValue(n, stop);
								}
							});
						}
						runOnUiThread(new Runnable() {
							public void run() {
								run.setEnabled(true);
								download.setEnabled(true);
							}
						});
						
					}
				}).start();
				
			}
		});
		
		
		download.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new AsyncTask<Void, Integer, String>() {
					
					protected void onPreExecute() {
						run.setEnabled(false);
						download.setEnabled(false);
					}
					
					protected String doInBackground(Void...n) {
						try {
			                URL url = new URL("http://amurani.net/dm/files/Desiderata.pdf");
			                URLConnection urlcon = url.openConnection();
			                urlcon.connect();
			                int bytelen = urlcon.getContentLength();
			                BufferedInputStream input = new BufferedInputStream(url.openStream(), 8080);
			                FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Desiderata.pdf");
			                byte[] data = new byte[10 * 1024 * 1024];
			                int count, total = 0;
			                while((count = input.read(data)) != -1) {
			                    total += count;
			                    output.write(data, 0, count);
			                    publishProgress(total, bytelen);
			                }
			                output.flush();
			                output.close();
			                input.close();
			            } catch(IOException e) {
			                Log.i("HIR", "Error: " + e.getMessage());
			            } catch(Exception e) {
			            	Log.i("HIR", "Error: " + e.getMessage());
			            }
						return null;
					}
					
					protected void onProgressUpdate(Integer...values) {
						meter.setValue(values[0], values[1]);
					}
					
					protected void onPostExecute(String s) {
						run.setEnabled(true);
						download.setEnabled(true);
					}
					
				}.execute();
			}
		});
	}

}