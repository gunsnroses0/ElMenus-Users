package Model;

import java.security.NoSuchAlgorithmException;

//import com.arangodb.ArangoCollection;
//import com.arangodb.ArangoCursor;
//import com.arangodb.ArangoDBException;
//import com.arangodb.entity.BaseDocument;
//import com.arangodb.entity.DocumentCreateEntity;
//import com.arangodb.util.MapBuilder;
//import lib.ArangoClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

//import sun.tools.jstat.Jstat;

//import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;
import java.util.Properties;

public class User {

	public static String getById(int id) {
		String callStatement = "{? = call Get_Profile_By_Id( ? ) }";
		JSONObject json = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject inputObject = new JSONObject();
		inputObject.put("type", Types.INTEGER);
		inputObject.put("value", id);
		jsonArray.add(inputObject);
		json.put("call_statement", callStatement);
		json.put("out_type", Types.OTHER);
		json.put("input_array", jsonArray);

		return json.toString();
	}

	public static String Create(String name, String birthdate, String bio,
			String phone_number, String address) throws NoSuchAlgorithmException {
		String callStatement = "{ call Add_Profile( ?, ?, ?, ?, ?) }";
		JSONObject json = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject inputName = new JSONObject();
		JSONObject inputBirthdate = new JSONObject();
		JSONObject inputBio = new JSONObject();
		JSONObject inputPhoneNumber = new JSONObject();
		JSONObject inputAddress = new JSONObject();
		inputName.put("type", Types.VARCHAR);
		inputName.put("value", name);
		inputBirthdate.put("type", Types.VARCHAR);
		inputBirthdate.put("value", birthdate);
		inputBio.put("type", Types.VARCHAR);
		inputBio.put("value", bio);
		inputPhoneNumber.put("type", Types.VARCHAR);
		inputPhoneNumber.put("value", phone_number);
		inputAddress.put("type", Types.VARCHAR);
		inputAddress.put("value", address);
		jsonArray.add(inputName);
		jsonArray.add(inputBirthdate);
		jsonArray.add(inputBio);
		jsonArray.add(inputPhoneNumber);
		jsonArray.add(inputAddress);
		json.put("out_type", 0);
		json.put("call_statement", callStatement);
		json.put("input_array", jsonArray);
		return json.toString();
	}

	public static String Update(int id, String name, String birthdate, String bio,
    String phone_number, String address) throws NoSuchAlgorithmException {
		String callStatement = "{ ? = call Update_Profile_By_Id( ?,?,?,?,?,?) }";
		JSONObject json = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject inputId = new JSONObject();
		JSONObject inputName = new JSONObject();
		JSONObject inputBirthdate = new JSONObject();
		JSONObject inputBio = new JSONObject();
		JSONObject inputPhoneNumber = new JSONObject();
		JSONObject inputAddress = new JSONObject();
		inputId.put("type", Types.INTEGER);
		inputId.put("value", id);
		inputName.put("type", Types.VARCHAR);
		inputName.put("value", name);
		inputBirthdate.put("type", Types.VARCHAR);
		inputBirthdate.put("value", birthdate);
		inputBio.put("type", Types.VARCHAR);
		inputBio.put("value", bio);
		inputPhoneNumber.put("type", Types.VARCHAR);
		inputPhoneNumber.put("value", phone_number);
		inputAddress.put("type", Types.VARCHAR);
		inputAddress.put("value", address);
		jsonArray.add(inputId);
		jsonArray.add(inputName);
		jsonArray.add(inputBirthdate);
		jsonArray.add(inputBio);
		jsonArray.add(inputPhoneNumber);
		jsonArray.add(inputAddress);
		json.put("out_type", Types.INTEGER);
		json.put("call_statement", callStatement);
		json.put("input_array", jsonArray);
		return json.toString();
	}

	public static String DeleteById(int id) {
		String callStatement = "{? = call Delete_Profile( ? ) }";
		JSONObject json = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject inputID = new JSONObject();
		inputID.put("type", Types.INTEGER);
		inputID.put("value", id);
		jsonArray.add(inputID);
		json.put("out_type", Types.INTEGER);
		json.put("call_statement", callStatement);
		json.put("input_array", jsonArray);
		System.out.println(json.toString());
		return json.toString();
	}
}