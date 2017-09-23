package service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;

import Enums.ApiResponseStatus;
import basemodel.BaseResponse;
import database.Database;
import database.DatabaseConnector;
import model.Person;
import model.Profile;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserService {

	private ObjectMapper mapper = new ObjectMapper();
	
	
	/**
	 * User login webservice 
	 * @param requestJson : you need to pass json object of user class
	 * </br>
	 * <i>{
        "id": 0,
        "name": "",
        "email": "pinank1510@gmail.com",
        "password": "pinank"
		}</i>
	 * @return {@link BaseResponse}
	 * @throws JsonProcessingException
	 * sample json
	 * 
	 */
	
	@POST
	@Path("/login")
//	@Consumes(MediaType.APPLICATION_JSON)
	public Response userlogin(String requestJson) throws JsonProcessingException {
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<Person> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		Person user = new Person();
		try {
			if(requestJson.isEmpty()) {
				apiResponseStatus = ApiResponseStatus.INVALID_REQUEST;
			} else {
				user = mapper.readValue(requestJson, Person.class);
				DatabaseConnector connector = new DatabaseConnector();
				Connection connection = connector.getConnection();
				String query = "SELECT "
						+ Database.Login.TABLE_NAME + "." + Database.Login.USER_ID
						+ ", "+Database.Login.TABLE_NAME + "."+Database.Login.EMAIL
						+ ", "+Database.Profile.TABLE_NAME + "."+Database.Profile.COMPANY_NAME
						+ ", "+Database.Profile.TABLE_NAME+"."+Database.Profile.FIRST_NAME
						+ ", "+Database.Profile.TABLE_NAME+"."+Database.Profile.LAST_NAME
						+", "+Database.Profile.TABLE_NAME+"."+Database.Profile.STATE
						+", "+Database.Profile.TABLE_NAME+"."+Database.Profile.COUNTRY
						+", "+Database.Profile.TABLE_NAME+"."+Database.Profile.CITY
						+", "+Database.Profile.TABLE_NAME+"."+Database.Profile.STREET_ADDRESS
						+", "+Database.Profile.TABLE_NAME+"."+Database.Profile.ALTERNET_MOBILE
						+", "+Database.Profile.TABLE_NAME+"."+Database.Profile.MOBILE
						+" FROM "
						+Database.Login.TABLE_NAME
						+" INNER JOIN "
						+Database.Profile.TABLE_NAME
						+" ON "
						+Database.Login.TABLE_NAME+"."+Database.Login.USER_ID+" = "+Database.Profile.TABLE_NAME+"."+Database.Profile.USER_ID
						+" WHERE "
						+Database.Login.TABLE_NAME+"."+Database.Login.EMAIL+" = ?"
						+" AND "
						+Database.Login.TABLE_NAME+"."+Database.Login.PASSWORD+"= ?";
				
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setString(1, user.getEmail());
				statement.setString(2, user.getPassword());
				ResultSet resultSet = statement.executeQuery();
				
				resultSet.last();
				
				if(resultSet.getRow() == 0) {

					System.out.println(resultSet.getInt(0));
					System.out.println(resultSet.getString(1));
					apiResponseStatus = ApiResponseStatus.LOGIN_FAIL;
				} else if(resultSet.getRow() > 1){
					apiResponseStatus = ApiResponseStatus.MULTIPLE_USER_FOUND;
				} else {
					resultSet.first();
					Profile profile = new Profile();
					profile.setCompnay_name(resultSet.getString(Database.Profile.COMPANY_NAME));
					profile.setFname(resultSet.getString(Database.Profile.FIRST_NAME));
					profile.setLname(resultSet.getString(Database.Profile.LAST_NAME));
					profile.setState(resultSet.getString(Database.Profile.STATE));
					profile.setCountry(resultSet.getString(Database.Profile.COUNTRY));
					profile.setState(resultSet.getString(Database.Profile.STATE));
					profile.setCity(resultSet.getString(Database.Profile.CITY));
					profile.setStreet_address(resultSet.getString(Database.Profile.STREET_ADDRESS));
					profile.setAlternet_mobile(resultSet.getString(Database.Profile.ALTERNET_MOBILE));
					profile.setMobile(resultSet.getString(Database.Profile.MOBILE));
					

					user.setUser_id(resultSet.getInt(Database.Login.USER_ID));
					user.setEmail(resultSet.getString(Database.Login.EMAIL));
					user.setProfile(profile);
					
					response.setInfo(user);
					
					// udpate api status to success
					apiResponseStatus = ApiResponseStatus.LOGIN_SUCCESS;
				}
				

			}
			
		} catch (JsonProcessingException e) {
			// TODO: handle exception
			e.printStackTrace();
			user = new Person();
			apiResponseStatus = ApiResponseStatus.REQUEST_PARSING_ERROR;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			user = new Person();
			apiResponseStatus = ApiResponseStatus.DATABASE_CONNECTINO_ERROR;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			user = new Person();
			apiResponseStatus = ApiResponseStatus.MYSQL_EXCEPTION;
		} finally {
			
			response.setStatus(apiResponseStatus.getStatus_code());
			response.setMessage(apiResponseStatus.getStatus_message());
			response.setInfo(user);
			responseJson = mapper.writeValueAsString(response);
		}
		
		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	/**
	 * 
	 * @param requestJson : user object json 
	 * </br>
	 * <i>{
        "id": 0,
        "name": "",
        "email": "pinank1510@gmail.com",
        "password": ""
		}</i>
	 * @return {@link BaseResponse}
	 * @throws JsonProcessingException
	 * 
	 */
	
	@POST
	@Path("/forgotPassowrd")
//	@Consumes(MediaType.APPLICATION_JSON)
	public Response forgotPassword(String requestJson) throws JsonProcessingException {
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<Person> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		Person user = new Person();
		
		try {
			if(requestJson.isEmpty()) {
				apiResponseStatus = ApiResponseStatus.INVALID_REQUEST;
			}else {
				user = mapper.readValue(requestJson, Person.class);
				DatabaseConnector connector = new DatabaseConnector();
				Connection connection = connector.getConnection();
				String query = "Select "
						+Database.Login.EMAIL
						+" from login where "
						+Database.Login.EMAIL+"=?";
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setString(1, user.getEmail());
				
				ResultSet resultSet = statement.executeQuery();
				resultSet.last();
				int rowCount = resultSet.getRow();
				if(rowCount == 0) {
					apiResponseStatus = ApiResponseStatus.FORGOT_PASSWORD_FAIL;
				} else if(rowCount > 1) {
					apiResponseStatus = ApiResponseStatus.MULTIPLE_USER_FOUND;
				} else {
					// send email code
					
					/*
					 * email code here
					 */
					
					apiResponseStatus = ApiResponseStatus.FORGOT_PASSWORD_SUCCESS;
				}
			}
		}catch(Exception exception) {
			exception.printStackTrace();
		}finally {
			response.setStatus(apiResponseStatus.getStatus_code());
			response.setMessage(apiResponseStatus.getStatus_message());
			response.setInfo(user);
			responseJson = mapper.writeValueAsString(response);
		}
		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	/**
	 * 
	 * @param requestJson : user object json 
	 * </br>
	 * <i>{
        "id": 21,
        "name": "",
        "email": "",
        "password": ""
		}</i>
	 * @return
	 * @throws JsonProcessingException
	 */
	
	@POST
	@Path("/userProfile")
	public Response userProfile(String requestJson) throws JsonProcessingException {
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<Person> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		Person user = new Person();
		
		try {
			if(requestJson.isEmpty()) {
				apiResponseStatus = ApiResponseStatus.INVALID_REQUEST;
			} else {
				user = mapper.readValue(requestJson, Person.class);
				DatabaseConnector connector = new DatabaseConnector();
				Connection connection = connector.getConnection();
				String query = "SELECT "
						+ Database.Login.TABLE_NAME + "." + Database.Login.USER_ID
						+ ", "+Database.Login.TABLE_NAME + "."+Database.Login.EMAIL
						+ ", "+Database.Profile.TABLE_NAME + "."+Database.Profile.COMPANY_NAME
						+ ", "+Database.Profile.TABLE_NAME+"."+Database.Profile.FIRST_NAME
						+ ", "+Database.Profile.TABLE_NAME+"."+Database.Profile.LAST_NAME
						+", "+Database.Profile.TABLE_NAME+"."+Database.Profile.STATE
						+", "+Database.Profile.TABLE_NAME+"."+Database.Profile.COUNTRY
						+", "+Database.Profile.TABLE_NAME+"."+Database.Profile.CITY
						+", "+Database.Profile.TABLE_NAME+"."+Database.Profile.STREET_ADDRESS
						+", "+Database.Profile.TABLE_NAME+"."+Database.Profile.ALTERNET_MOBILE
						+", "+Database.Profile.TABLE_NAME+"."+Database.Profile.MOBILE
						+" FROM "
						+Database.Login.TABLE_NAME
						+" INNER JOIN "
						+Database.Profile.TABLE_NAME
						+" ON "
						+Database.Login.TABLE_NAME+"."+Database.Login.USER_ID+" = "+Database.Profile.TABLE_NAME+"."+Database.Profile.USER_ID
						+" WHERE "
						+Database.Login.TABLE_NAME+"."+Database.Login.USER_ID+" = ?";
				
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setInt(1, user.getUser_id());
				ResultSet resultSet = statement.executeQuery();
				
				resultSet.last();
				
				if(resultSet.getRow() == 0) {
					apiResponseStatus = ApiResponseStatus.INVALID_USER_ID;
				} else if(resultSet.getRow() > 1) {
					apiResponseStatus = ApiResponseStatus.MULTIPLE_USER_FOUND;
				} else {
					Profile profile = new Profile();
					profile.setCompnay_name(resultSet.getString(Database.Profile.COMPANY_NAME));
					profile.setFname(resultSet.getString(Database.Profile.FIRST_NAME));
					profile.setLname(resultSet.getString(Database.Profile.LAST_NAME));
					profile.setState(resultSet.getString(Database.Profile.STATE));
					profile.setCountry(resultSet.getString(Database.Profile.COUNTRY));
					profile.setState(resultSet.getString(Database.Profile.STATE));
					profile.setCity(resultSet.getString(Database.Profile.CITY));
					profile.setStreet_address(resultSet.getString(Database.Profile.STREET_ADDRESS));
					profile.setAlternet_mobile(resultSet.getString(Database.Profile.ALTERNET_MOBILE));
					profile.setMobile(resultSet.getString(Database.Profile.MOBILE));
					

					user.setUser_id(resultSet.getInt(Database.Login.USER_ID));
					user.setEmail(resultSet.getString(Database.Login.EMAIL));
					user.setProfile(profile);
					
					response.setInfo(user);
					
					//set api response to success
					apiResponseStatus = ApiResponseStatus.PROFILE_FATCH_SUCCESS;
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			apiResponseStatus = ApiResponseStatus.REQUEST_PARSING_ERROR;
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			apiResponseStatus = ApiResponseStatus.DATABASE_CONNECTINO_ERROR;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			apiResponseStatus = ApiResponseStatus.MYSQL_EXCEPTION;
		}finally {
			
			response.setStatus(apiResponseStatus.getStatus_code());
			response.setMessage(apiResponseStatus.getStatus_message());
			response.setInfo(user);
			responseJson = mapper.writeValueAsString(response);
			
		}
		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	

}
