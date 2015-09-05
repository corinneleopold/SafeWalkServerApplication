package edu.purdue.cleopold;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import edu.purdue.cleopold.R;
import edu.purdue.cleopold.R.id;
import edu.purdue.cleopold.R.layout;
import android.app.Fragment;
import android.util.Log;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * This fragment is the "page" where the application display the log from the
 * server and wait for a match.
 *
 * @author Corinne Leopold
 */
public class MatchFragment extends Fragment implements OnClickListener {

	private static final String DEBUG_TAG = "DEBUG";
	private static final String ERROR_TAG = "ERROR";

	/**
	 * Activity which have to receive callbacks.
	 */
	private StartOverCallbackListener activity;

	/**
	 * AsyncTask sending the request to the server.
	 */
	private Client client;

	/**
	 * Coordinate of the server.
	 */
	private String host;
	private int port;

	/**
	 * Command the user should send.
	 */
	private String command;

	// TODO: your own class fields here
	private String to;
	private String from;
	private Socket s;
	PrintWriter pw;
	BufferedReader br;
	TextView response1;
	TextView response2;
	TextView response3;
	TextView response4;
	TextView response5;
	String response = "";

	// Class methods
	/**
	 * Creates a MatchFragment
	 * 
	 * @param activity
	 *            activity to notify once the user click on the start over
	 *            Button.
	 * @param host
	 *            address or IP address of the server.
	 * @param port
	 *            port number.
	 * 
	 * @param command
	 *            command you have to send to the server.
	 * 
	 * @return the fragment initialized.
	 */
	// TODO: you can add more parameters, follow the way we did it.
	// ** DO NOT CREATE A CONSTRUCTOR FOR MatchFragment **
	public static MatchFragment newInstance(StartOverCallbackListener activity,
			String host, int port, String command) {
		MatchFragment f = new MatchFragment();

		f.activity = activity;
		f.host = host;
		f.port = port;
		f.command = command;

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

		View view = inflater.inflate(R.layout.match_fragment_layout, container,
				false);

		/**
		 * Register this fragment to be the OnClickListener for the startover
		 * button.
		 */
		view.findViewById(R.id.bu_start_over).setOnClickListener(this);

		// TODO: import your Views from the layout here. See example in
		// ServerFragment.
		response1 = (TextView) view.findViewById(R.id.message1);
		response2 = (TextView) view.findViewById(R.id.message2);
		response3 = (TextView) view.findViewById(R.id.message3);
		response4 = (TextView) view.findViewById(R.id.textView1);
		response5 = (TextView) view.findViewById(R.id.message5);
		/**
		 * Launch the AsyncTask
		 */
		this.client = new Client();
		this.client.execute("");

		return view;
	}

	/**
	 * Callback function for the OnClickListener interface.
	 */
	@Override
	public void onClick(View v) {
		/**
		 * Close the AsyncTask if still running.
		 */
		this.client.close();

		/**
		 * Notify the Activity.
		 */
		this.activity.onStartOver();
	}

	class Client extends AsyncTask<String, String, String> implements Closeable {

		/**
		 * NOTE: you can access MatchFragment field from this class:
		 * 
		 * Example: The statement in doInBackground will print the message in
		 * the Eclipse LogCat view.
		 */

		/**
		 * The system calls this to perform work in a worker thread and delivers
		 * it the parameters given to AsyncTask.execute()
		 */
		protected String doInBackground(String... params) {

			/**
			 * TODO: Your Client code here.
			 */
			try { 
				s = new Socket(host, port);
				br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				pw = new PrintWriter(s.getOutputStream(), true);	
				publishProgress("\nYou sent the request: " + command + "\n");
			    Log.d(DEBUG_TAG, String
						.format("The Server at the address %s uses the port %d",
							host, port));
				
				Log.d(DEBUG_TAG, String.format(
						"The Client will send the command: %s", command));
				
				pw.println(command);
				publishProgress("Waiting for a match...\n");
	                response = br.readLine();	
	                if (response.startsWith("RESPONSE: ")) {
	                	pw.println(":ACK");
	                	Log.d(DEBUG_TAG, String.format(
	    						"A match has been found"));
	                }
	                	
	                
	                publishProgress(response);
			}
			catch (Exception e) { 
				Log.e(ERROR_TAG, String
						.format("The server is not available."));
			}
			return response;
		}
		
		public void close() {
                    // TODO: Clean up the client
			try {
				s.close();
			}
			catch (Exception e) {
			}
		}

		/**
		 * The system calls this to perform work in the UI thread and delivers
		 * the result from doInBackground()
		 */

		// TODO: use the following method to update the UI.
		// ** DO NOT TRY TO CALL UI METHODS FROM doInBackground!!!!!!!!!! **

		/**
		 * Method executed just before the task.
		 */
		@Override
		protected void onPreExecute() {

		}

		/**
		 * Method executed once the task is completed.
		 */
		@Override
		protected void onPostExecute(String result) {
			try {
				s.close();
			} catch (Exception e) {}
            
		}

		/**
		 * Method executed when progressUpdate is called in the doInBackground
		 * function.
		 */
		@Override
		protected void onProgressUpdate(String... result) {
			response1.setVisibility(View.VISIBLE);
			response1.append("\n\n" + result[0]);
			
			if (result[0].contains("RESPONSE")) {
				response1.append("\n\n\nA pair has been found by the server.");
				ArrayList<String> requests = new ArrayList<String>();
				StringTokenizer st = new StringTokenizer(response, ",");
				while (st.hasMoreTokens()) {
					requests.add(st.nextToken());
				} 
				response3.setText("PARTNER:     " + requests.get(0).substring(10) + "\n\nFROM:     " + requests.get(1) +
						"\n\nTO:     " + requests.get(2)); //use substring(10) to get the info after the "RESPONSE: "
				response3.setVisibility(View.VISIBLE);
				response4.setVisibility(View.VISIBLE); //congrats message
			}
		}
	}
}