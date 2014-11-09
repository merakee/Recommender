package net.freelogue.recommender;

import org.json.JSONObject;
import org.json.JSONException;

import java.util.Arrays;

//import java.io.*;

public class RequestAndResponseManager {
	enum ApiProcessingError {
		InvalidRequest, CommandMissing, IncompleteRequest, InvalidParameter, ParameterMissing, RequestParseError, ResponseParseError, GenericError
	}

	String getResponseForRequest(String request) {
		return parseRequest(request);
	}

	String parseRequest(String request) {
		try {
			JSONObject json = new JSONObject(request);
			String command = json.getString("command");
			String params = json.getString("params");
			return createResponse(command, params);
		} catch (JSONException je) {
			return createJsonString(false,
					getErrorString(ApiProcessingError.RequestParseError));
		}
	}

	String createResponse(String command, String params) {
		switch (command) {
		case "getRecommendationForUser":
			return getRecommendationForUserResponse(params);
		default:
			return createJsonString(false,
					getErrorString(ApiProcessingError.InvalidRequest));
		}

	}

	String getRecommendationForUserResponse(String params) {
		try {
			int userId = Integer.parseInt(params);
			if (userId <= 0) {
				return createJsonString(false,
						getErrorString(ApiProcessingError.InvalidParameter));
			}
			String response = convert2DArrayToString(RecommendationManager
					.getRecommendationForUser(userId));
			return createJsonString(true, response);
		} catch (NumberFormatException nfe) {
			return createJsonString(false,
					getErrorString(ApiProcessingError.InvalidParameter));
		}
	}

	String convert2DArrayToString(double[][] array2D){
		String arrayString = "[";
		for(double[] array:  array2D){
			arrayString += "[";
			for(double val: array){
				arrayString += val + ",";
			}
			arrayString = CommonUtil.stringTrimLast(arrayString);
			arrayString += "],";
			System.out.println(arrayString);
		}
		arrayString = CommonUtil.stringTrimLast(arrayString);
		return arrayString + "]";
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
}
