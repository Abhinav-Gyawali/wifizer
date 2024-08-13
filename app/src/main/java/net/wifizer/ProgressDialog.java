// ProgressDialog.java
package net.wifizer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressDialog extends Dialog {

	private ProgressBar progressBar;
	private TextView dataField;
	private TextView progressText;
	private Activity activity;
	private onDialogCancelListener cancelListener;

	public ProgressDialog(Context context, Activity activity) {
		super(context);
		this.activity = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_progress_connection);

		// Initialize views
		progressBar = findViewById(R.id.loading_spinner);
		dataField = findViewById(R.id.data_field);
		progressText = findViewById(R.id.progress_text);
		ImageView closeIcon = findViewById(R.id.close_icon);

		// Close icon click listener
		closeIcon.setOnClickListener(v -> dismiss());

		// Handle dialog dismiss
		setOnDismissListener(dialog -> {
			if (cancelListener != null) {
				cancelListener.onDialogCancel(); // Notify listener
			}
		});
	}

	public void updateProgress(int current, int total) {
		activity.runOnUiThread(() -> progressText.setText(current + "/" + total));
	}

	public void setDataFieldText(String text) {
		activity.runOnUiThread(() -> dataField.setText(text));
	}

	public void dismissDialog() {
		if (isShowing()) {
			dismiss();
		}
	}

	// Set the cancel listener
	public void setOnDialogCancelListener(onDialogCancelListener listener) {
		this.cancelListener = listener;
	}


}