package net.freelogue.recommender;

import java.net.*;
import java.io.*;
import java.lang.String; 

public class ApiManager implements Runnable {
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

	public ApiManager() {
		this(ApiManager.getDefaultPort());
	}

	public ApiManager(int port) {
		this.apiPort = port;
	}

	public void listenAndProcessApiRequests() {
		try {
			this.apiSocket = new ServerSocket(this.apiPort);
			// this.apiSocket
			// .setSoTimeout(AppConstants.API_SOCKET_TIMEOUT_INTERVAL *
			// 1000);
			CommonUtil.printTextWithTimeStamp("Opening server socket: "
					+ this.apiSocket.getLocalSocketAddress());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		while (true) {
			try {
				this.apiConnection = this.apiSocket.accept();
				System.out.println("Just connected to "
						+ this.apiConnection.getRemoteSocketAddress());
				this.apiInput = new BufferedReader(new InputStreamReader(
						this.apiConnection.getInputStream()));
				this.apiOutput = new DataOutputStream(
						this.apiConnection.getOutputStream());
				this.processRequest();
			} catch (SocketTimeoutException stoe) {
				System.out.println("Socket timed out!");
				// break;
			} catch (IOException ioe) {
				ioe.printStackTrace();
				// break;
			}
		}
	}

	void processRequest() {
		while (true) {
			try {
				this.apiRequest = this.apiInput.readLine();
				// end request processing is api request is null => clinet has disconnected 
				if (this.apiRequest == null) {
					break;
				}
				this.sendResponse(this.createResponseForRequest(this.apiRequest));
			} catch (IOException ioe) {
				ioe.printStackTrace();
				CommonUtil.printTextWithTimeStamp("End of process request");
				break;
			}
		}

	}
	
	void sendResponse(String msg) {
		try {
			System.out.println(msg);
			this.apiOutput.writeBytes(msg);
			this.apiOutput.writeBytes("\n");
			this.apiOutput.flush();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	String createResponseForRequest (String request){
		return responseManager.getResponseForRequest(request);
	}
	
	// the run method for Runnable
	public void run() {
		this.listenAndProcessApiRequests();
	}
}
