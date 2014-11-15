package net.freelogue.recommender;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.String;

public class ApiManager implements Runnable {
	// class variables
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	private int apiPort;
	private ServerSocket apiSocket;
	private Socket apiConnection = null;
	private DataOutputStream apiOutput;
	private BufferedReader apiInput;
	private String apiRequest;
	private RequestAndResponseManager responseManager = new RequestAndResponseManager();

	static int getDefaultPort() {
		int defaultPort;
		if (System.getenv("RECOM_DEFAULT_PORT") != null) {
			defaultPort = Integer.parseInt(System.getenv("RECOM_DEFAULT_PORT"));
		} else {
			defaultPort = 2014;
		}
		return defaultPort;
	}

	/**
	 * Creates ApiManager with default server socket bound to the port specified in ENV[RECOM_DEFAULT_PORT] or default 2014"
	 */
	public ApiManager() {
		this(getDefaultPort());
	}
	
	/**
	 * Creates ApiManager with server socket bound to the specified port.
	 * @param port : specify port for the server socket 
	 */
	public ApiManager(int port) {
		this.apiPort = port;
	}

	/**
	 * 
	 */
	void listenAndProcessApiRequests() {
		try {
			this.apiSocket = new ServerSocket(this.apiPort);
			// this.apiSocket
			// .setSoTimeout(AppConstants.API_SOCKET_TIMEOUT_INTERVAL *
			// 1000);
			LOGGER.log(
					Level.INFO,
					"Opening server socket: "
							+ this.apiSocket.getLocalSocketAddress());
		} catch (IOException ioe) {
			LOGGER.log(Level.WARNING, ioe.getMessage(), ioe);
			// ioe.printStackTrace();
		}

		while (true) {
			try {
				this.apiConnection = this.apiSocket.accept();
				LOGGER.log(Level.INFO, "Just connected to "
						+ this.apiConnection.getRemoteSocketAddress());
				this.apiInput = new BufferedReader(new InputStreamReader(
						this.apiConnection.getInputStream()));
				this.apiOutput = new DataOutputStream(
						this.apiConnection.getOutputStream());
				this.processRequest();
			} catch (SocketTimeoutException stoe) {
				LOGGER.log(Level.WARNING, stoe.getMessage(), stoe);
			} catch (IOException ioe) {
				LOGGER.log(Level.WARNING, ioe.getMessage(), ioe);
				// ioe.printStackTrace();
			}
		}
	}

	void processRequest() {
		while (true) {
			try {
				this.apiRequest = this.apiInput.readLine();
				// end request processing is api request is null => clinet has
				// disconnected
				if (this.apiRequest == null) {
					break;
				}
				sendResponse(createResponseForRequest(this.apiRequest));
			} catch (IOException ioe) {
				LOGGER.log(Level.WARNING, ioe.getMessage(), ioe);
				// ioe.printStackTrace();
				// LOGGER.log(Level.INFO, "End of process request");
				break;
			}
		}

	}

	void sendResponse(String msg) {
		try {
			LOGGER.log(Level.FINE, msg);
			this.apiOutput.writeBytes(msg);
			this.apiOutput.writeBytes("\n");
			this.apiOutput.flush();
		} catch (IOException ioe) {
			LOGGER.log(Level.WARNING, ioe.getMessage(), ioe);
			// ioe.printStackTrace();
		}
	}

	String createResponseForRequest(String request) {
		LOGGER.log(Level.FINE, request);
		return responseManager.getResponseForRequest(request);
	}

	/**
	 * The run method for Runnable. Calls listenAndProcessApiRequests();
	 */
	public void run() {
		// LOGGER.log(Level.INFO,"Thread prority: " +
		// Thread.currentThread().getPriority());
		this.listenAndProcessApiRequests();
	}
}
