package edu.purdue.cleopold;

//@author Corinne Leopold

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements SubmitCallbackListener,
		StartOverCallbackListener {

	/**
	 * The ClientFragment used by the activity.
	 */
	private ClientFragment clientFragment;

	/**
	 * The ServerFragment used by the activity.
	 */
	private ServerFragment serverFragment;

	/**
	 * UI component of the ActionBar used for navigation.
	 */
	private Button left;
	private Button right;
	private TextView title;
	private String name;
	private int type;
	private String to;
	private String from;

	/**
	 * Called once the activity is created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_layout);

		this.clientFragment = ClientFragment.newInstance(this);
		this.serverFragment = ServerFragment.newInstance();

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.fl_main, this.clientFragment);
		ft.commit();
	}

	/**
	 * Creates the ActionBar: - Inflates the layout - Extracts the components
	 */
	@SuppressLint("InflateParams")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater()
				.inflate(R.layout.action_bar, null);

		// Set up the ActionBar
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(actionBarLayout);

		// Extract the UI component.
		this.title = (TextView) actionBarLayout.findViewById(R.id.tv_title);
		this.left = (Button) actionBarLayout.findViewById(R.id.bu_left);
		this.right = (Button) actionBarLayout.findViewById(R.id.bu_right);
		this.right.setVisibility(View.INVISIBLE);

		return true;
	}

	/**
	 * Callback function called when the user click on the right button of the
	 * ActionBar.
	 * 
	 * @param v
	 */
	public void onRightClick(View v) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();

		this.title.setText(this.getResources().getString(R.string.client));
		this.left.setVisibility(View.VISIBLE);
		this.right.setVisibility(View.INVISIBLE);
		ft.replace(R.id.fl_main, this.clientFragment);
		ft.commit();
	}

	/**
	 * Callback function called when the user click on the left button of the
	 * ActionBar.
	 * 
	 * @param v
	 */
	public void onLeftClick(View v) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();

		this.title.setText(this.getResources().getString(R.string.server));
		this.left.setVisibility(View.INVISIBLE);
		this.right.setVisibility(View.VISIBLE);
		ft.replace(R.id.fl_main, this.serverFragment);
		ft.commit();

	}

	/**
	 * Callback function called when the user click on the submit button.
	 */
	@Override
	public void onSubmit() {
		// TODO: Get client info via client fragment
		this.name = this.clientFragment.getName();
		this.type = this.clientFragment.getType();
		this.to = this.clientFragment.spinnerToInfo();
		this.from = this.clientFragment.spinnerFromInfo();
		
		// Server info
		String host = this.serverFragment.getHost(getResources().getString(
				R.string.default_host));
		int port = 0;
		try {
			port = this.serverFragment.getPort(Integer.parseInt(getResources()
				.getString(R.string.default_port))); 
		}
		catch (NumberFormatException e) {
		}
		
		// TODO: Need to get command from client fragment
				String[] locations = {"CL50: Class of 1950 Lecture Hall", "EE: Electrical Engineering", "LWSN: Lawson Computer Science Building",
						"PMU: Purdue Memorial Union", "PUSH: Purdue University Student Health Center"};
				if (to.equals(locations[0]))
					to = "CL50";
				else if (to.equals(locations[1]))
					to = "EE";
				else if (to.equals(locations[2]))
					to = "LWSN";
				else if (to.equals(locations[3]))
					to = "PMU";
				else if (to.equals(locations[4]))
					to = "PUSH";
				
				if (from.equals(locations[0]))
					from = "CL50";
				else if (from.equals(locations[1]))
					from = "EE";
				else if (from.equals(locations[2]))
					from = "LWSN";
				else if (from.equals(locations[3]))
					from = "PMU";
				else if (from.equals(locations[4]))
					from = "PUSH";
					
				String command = name + "," + from + "," + to + "," + type;		
		
		// TODO: sanity check the results of the previous two dialogs
		if (name.matches("") || name.contains(",") || type == -1 || to.equals(from) || 
				(to.equals("*") && type != 2) || host.matches("") || host.contains(" ") || port < 1 || port > 65535) {
			newClientDialog(name, type, to, from, host, port);
		}
		else {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		this.title.setText(getResources().getString(R.string.match));
		this.left.setVisibility(View.INVISIBLE);
		this.right.setVisibility(View.INVISIBLE);

		// TODO: You may want additional parameters here if you tailor
		// the match fragment
		MatchFragment frag = MatchFragment.newInstance(this, host, port,
				command);

		ft.replace(R.id.fl_main, frag);
		ft.commit();
	}
		this.clientFragment.clearAll();
	}
	public void newClientDialog(String name, int type, String to, String from, String host, int port) {
		AlertDialog.Builder message = new AlertDialog.Builder(this); 
		if (name.matches("") || name.contains(","))
			message.setMessage("Invalid name");
		else if (to.equals(from))
			message.setMessage("The To location cannot be the same as the From location");
		else if (host.matches("") || host.contains(" "))
			message.setMessage("Invalid host");
		else if (port < 1 || port > 65535)
			message.setMessage("Invalid port");
		else
			message.setMessage("Invalid selection");
		message.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.fl_main, clientFragment);
				ft.commit();
	         }
		});
		AlertDialog alertDialog = message.create();
		alertDialog.show();
	}
	
	public void newHostDialog(String host, int port) {
		AlertDialog.Builder message = new AlertDialog.Builder(this);
		if (host == null || host.contains(" "))
			message.setMessage("Invalid host");
		else if (port < 1 || port > 65535)
			message.setMessage("Invalid port");
		else
			message.setMessage("Invalid selection");
		message.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.fl_main, clientFragment);
				ft.commit();
	         }
		});
		AlertDialog alertDialog = message.create();
		alertDialog.show();
	}

	/**
	 * Callback function call from MatchFragment when the user want to create a
	 * new request.
	 */
	@Override
	public void onStartOver() {
		onRightClick(null);
	}

}
