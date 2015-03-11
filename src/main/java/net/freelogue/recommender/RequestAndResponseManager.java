package net.freelogue.recommender;

import org.json.JSONObject;
import org.json.JSONException;

import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.util.logging.Level;
//import java.util.Arrays;
import java.util.logging.Logger;

//import java.io.*;

public class RequestAndResponseManager {
	enum ApiProcessingError {
		InvalidRequest, CommandMissing, IncompleteRequest, InvalidParameter, ParameterMissing, RequestParseError, ResponseParseError, GenericError
	}

	// class variables
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	String getResponseForRequest(String request) {
		return parseRequest(request);
	}

	String parseRequest(String request) {
		try {
			// JSONObject json = new JSONObject(request);
			// String command = json.getString("command");
			// String params = json.getString("params");
			// return createResponse(command, params);
			ApiRequest apiRequest = getApiRequest(request);
			HashMap<String, String> paramsHash = parseParams(apiRequest.params);
			return processCommandAndCreateResponse(apiRequest.command,
					paramsHash);
		} catch (JSONException je) {
			LOGGER.log(Level.WARNING, je.getMessage(), je);
			return createJsonString(false,
					getErrorString(ApiProcessingError.RequestParseError));
		}
	}

	String processCommandAndCreateResponse(String command,
			HashMap<String, String> paramsHash) {
		switch (command) {
		case "getRecommendationForUser":
			return getRecommendationForUserResponse(paramsHash);
		default:
			return createJsonString(false,
					getErrorString(ApiProcessingError.InvalidRequest));
		}

	}
	
	/* count is the number of recommendations desired. */
	String getRecommendationForUserResponse(HashMap<String, String> paramsHash) {
		try {
			int userId = Integer.parseInt(paramsHash.get("user_id"));
			if (userId <= 0) {
				return createJsonString(false,
						getErrorString(ApiProcessingError.InvalidParameter));
			}
			int count = 0;
			if (paramsHash.get("count") != null) {
				count = Integer.parseInt(paramsHash.get("count"));
			}
			String response;
			if(count<=0){
				response = convertRecommendedItemListToString(RecommendationManager.getRecommendationForUser(userId));
			}
			else{
				response = convertRecommendedItemListToString(RecommendationManager.getRecommendationForUser(userId, count));
			}
			return createJsonString(true, response);
		} catch (NumberFormatException nfe) {
			LOGGER.log(Level.WARNING, nfe.getMessage(), nfe);
			return createJsonString(false,
					getErrorString(ApiProcessingError.InvalidParameter));
		}
	}

	String convertRecommendedItemListToString(List<RecommendedItem> recomList) {
		if (recomList.isEmpty()) {
			return "[]";
		}
		String arrayString = "[";
		for (RecommendedItem item : recomList) {
			arrayString += "[" + item.getItemID() + "," + item.getValue()
					+ "],";
		}
		arrayString = CommonUtil.stringTrimLast(arrayString);
		arrayString += "]";
		return arrayString;
	}

	String getErrorString(ApiProcessingError errorType) {
		switch (errorType) {
		case InvalidRequest:
			return "Invalid Request";
		case CommandMissing:
			return "Command Missing";
		case IncompleteRequest:
			return "Imcomplete Request";
		case ParameterMissing:
			return "Parameter Missing";
		case InvalidParameter:
			return "Invalid Parameter";
		case RequestParseError:
			return "Request Parse Error";
		case ResponseParseError:
			return "Response Parse Error";
		case GenericError:
			return "Generic Error";
		default:
			return "Something went wrong with request processing";
		}
	}

	/*
	 * JSON creation and parsing
	 */
	class ApiRequest {
		String command, params;

		public ApiRequest(String command, String params) {
			this.command = command;
			this.params = params;
		}
	}

	ApiRequest getApiRequest(String request) throws JSONException {
		JSONObject json = new JSONObject(request);
		String command = json.getString("command");
		String params = json.getString("params");
		return new ApiRequest(command, params);
	}

	HashMap<String, String> parseParams(String params) throws JSONException {
		JSONObject json = new JSONObject(params);
		HashMap<String, String> paramHash = new HashMap<String, String>();
		Iterator<?> keys = json.keys();
		String key;
		while (keys.hasNext()) {
			key = (String) keys.next();
			paramHash.put(key, json.getString(key));
		}
		return paramHash;
	}

	String createJsonString(boolean success, String response) {
		JSONObject json = new JSONObject();
		try {
			json.put("success", success);
			if (success) {
				json.put("response", response);
			} else {
				json.put("error", response);
			}
			return json.toString();
		} catch (JSONException je) {
			LOGGER.log(Level.WARNING, je.getMessage(), je);
			return "{\"success\": false, \"error\": \""
					+ getErrorString(ApiProcessingError.ResponseParseError)
					+ "\"}";
		}
		// StringWriter jsonString = new StringWriter();
		// try {
		// json.writeJSONString(jsonString);
		// return jsonString.toString();
		// } catch (IOException ioe) {
		// ioe.printStackTrace();
		// return
		// "{\"success\": false, \"error\": \"invalid response string\"}";
		// }
	}

}
