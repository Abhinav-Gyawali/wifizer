package net.wifizer;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
	private LinearLayout availableWifiContainer;
	private LinearLayout savedWifiContainer;
	private WifiManager wifiManager;
	private static final int REQUEST_LOCATION_PERMISSION = 1;
	private WifiScanReceiver wifiScanReceiver;
	private Set<String> configuredSsids;
	public static final String PREFS_NAME = "WifizerPrefs";
	public static final String KEY_USE_OWN_PASSES = "useOwnPasses";
	private ActivityResultLauncher<Intent> filePickerLauncher;
	FileReaderHelper fileReaderHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
					REQUEST_LOCATION_PERMISSION);
		} else {
			initializeWifiScan();
		}
		
		fileReaderHelper = new FileReaderHelper(this);
		
		filePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
			if (result.getResultCode() == RESULT_OK && result.getData() != null) {
				Uri uri = result.getData().getData();
				if (uri != null) {
					// Add data from file
					fileReaderHelper.addDataFromFile(getContentResolver(), uri);
					
					// Fetch and process the cached data
					String[] cachedData = fileReaderHelper.fetchData();
					for (String line : cachedData) {
						Log.d("FileContent", line);
					}
				}
			}
		});
		
		Notifier notify = new Notifier(this);
		notify.showNotification("Welcome", "You are heartly welcomed", "");

		availableWifiContainer = findViewById(R.id.availableWifiContainer);
		savedWifiContainer = findViewById(R.id.savedWifiContainer);
		ImageButton scanWifi = findViewById(R.id.scanWifi);
		CheckBox checkBox = findViewById(R.id.usePasswordCheckbox);

		SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		boolean useOwnPasses = preferences.getBoolean(KEY_USE_OWN_PASSES, false);
		checkBox.setChecked(useOwnPasses);

		// Set an OnCheckedChangeListener
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
					intent.setType("*/*");
					filePickerLauncher.launch(intent);
				}
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(KEY_USE_OWN_PASSES, isChecked);
				editor.commit();
			}
		});

		scanWifi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scanWifi.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate));
				startWifiScan();
			}
		});
	}

	private void initializeWifiScan() {
		wifiScanReceiver = new WifiScanReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		registerReceiver(wifiScanReceiver, filter);
	}

	private void startWifiScan() {
		if (wifiManager != null) {
			wifiManager.startScan();
		}
	}

	private void displayAvailableNetworks(List<ScanResult> results) {
		availableWifiContainer.removeAllViews(); // Clear existing views
		savedWifiContainer.removeAllViews(); // Clear existing views

		// Get list of configured networks
		List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
		configuredSsids = new HashSet<>();
		for (WifiConfiguration config : configuredNetworks) {
			configuredSsids.add(config.SSID.replace("\"", "")); // Remove quotes from SSID
		}

		for (ScanResult result : results) {
			LinearLayout wifiItem = (LinearLayout) getLayoutInflater().inflate(R.layout.wifi_item, null);

			ImageView wifiIcon = wifiItem.findViewById(R.id.wifiIcon);
			TextView wifiName = wifiItem.findViewById(R.id.wifiName);
			TextView wifiDescription = wifiItem.findViewById(R.id.wifiDescription);

			String ssid = result.SSID;
			int signalLevel = result.level;
			if (signalLevel > -50) {
				wifiIcon.setImageResource(R.drawable.ic_wifi_best);
			} else if (signalLevel > -60) {
				// Good signal
				wifiIcon.setImageResource(R.drawable.ic_wifi_good);
			} else if (signalLevel > -70) {
				// Fair signal
				wifiIcon.setImageResource(R.drawable.ic_wifi_fair);
			} else {
				// Poor signal
				wifiIcon.setImageResource(R.drawable.ic_wifi_poor);
			}
			// Use appropriate drawable
			wifiName.setText(ssid);

			String description = configuredSsids.contains(ssid) ? "Saved network"
					: "Signal strength: " + result.level + "dBm";
			wifiDescription.setText(description);

			wifiItem.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Create an intent to start WifiDetailActivity
					Intent intent = new Intent(MainActivity.this, ConnectWifiActivity.class);
					intent.putExtra("SSID", ssid);
					intent.putExtra("HIDDEN", ssid.isEmpty());
					startActivity(intent);
				}
			});

			if (configuredSsids.contains(ssid)) {
				savedWifiContainer.addView(wifiItem);
			} else {
				availableWifiContainer.addView(wifiItem);
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_LOCATION_PERMISSION) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				initializeWifiScan();
			} else {
				Toast.makeText(this, "Location permission is required to scan WiFi networks.", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	private class WifiScanReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			List<ScanResult> results = wifiManager.getScanResults();
			displayAvailableNetworks(results);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (wifiScanReceiver != null) {
			unregisterReceiver(wifiScanReceiver);
		}
	}
}