package edu.purdue.cleopold;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

/**
 * This fragment is the "page" where the user inputs information about the
 * request, he/she wishes to send.
 *
 * @author Corinne Leopold
 */
public class ClientFragment extends Fragment implements OnClickListener {
	
	private Spinner spinnerTo;
	private Spinner spinnerFrom;
	private EditText clientName;
	private RadioButton rb1;
	private RadioButton rb2;
	private RadioButton rb3;
	
	/**
	 * Activity which have to receive callbacks.
	 */
	private SubmitCallbackListener activity;

	/**
	 * Creates a ProfileFragment
	 * 
	 * @param activity
	 *            activity to notify once the user click on the submit Button.
	 * 
	 *            ** DO NOT CREATE A CONSTRUCTOR FOR MatchFragment **
	 * 
	 * @return the fragment initialized.
	 */
	// ** DO NOT CREATE A CONSTRUCTOR FOR ProfileFragment **
	public static ClientFragment newInstance(SubmitCallbackListener activity) {
		ClientFragment f = new ClientFragment();

		f.activity = activity;
		return f;
	}

	/**
	 * Called when the fragment will be displayed.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		View view = inflater.inflate(R.layout.client_fragment_layout,
				container, false);

		/**
		 * Register this fragment to be the OnClickListener for the submit
		 * Button.
		 */
		view.findViewById(R.id.bu_submit).setOnClickListener(this);

		// TODO: import your Views from the layout here. See example in
		// ServerFragment.
		this.clientName = (EditText) view.findViewById(R.id.name);
		view.findViewById(R.id.message1);
		view.findViewById(R.id.message2);
		view.findViewById(R.id.textView3);
		view.findViewById(R.id.textView4);
		this.rb1 = (RadioButton) view.findViewById(R.id.radioButton1);
		this.rb2 = (RadioButton) view.findViewById(R.id.radioButton2);
		this.rb3 = (RadioButton) view.findViewById(R.id.radioButton4);
		view.findViewById(R.id.edit_text);
		this.spinnerTo = (Spinner) view.findViewById(R.id.spinnerTo);
		this.spinnerFrom = (Spinner) view.findViewById(R.id.spinnerFrom);		
		return view; 
	}		

	/**
	 * Callback function for the OnClickListener interface.
	 */
	@Override
	public void onClick(View v) {
		this.activity.onSubmit();
	}
	
	public String getName() {
		return this.clientName.getText().toString();
	}
	
	public int getType() {
		int result = -1;
		if (rb1.isChecked()) {
			result = 0; 
		}
		else if (rb2.isChecked()) {
			result = 1; 
		}
		else if (rb3.isChecked()) {
			result = 2; 
		}
		return result;
	}
	
	public String spinnerToInfo() {
		return String.valueOf(spinnerTo.getSelectedItem());		
	}
	
	public String spinnerFromInfo() {
		return String.valueOf(spinnerFrom.getSelectedItem());
	}
	
	public void clearAll() {
		this.clientName.setText("");
		this.rb1.setChecked(false);
		this.rb2.setChecked(false);
		this.rb3.setChecked(false);
		this.spinnerTo.setSelection(0);
		this.spinnerFrom.setSelection(0);
	}
}