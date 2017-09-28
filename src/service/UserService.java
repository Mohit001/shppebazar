package service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Enums.ApiResponseStatus;
import Utils.UtilsString;
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
				
				statement.close();
				connection.close();

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
	
	@GET
	@Path("/forgotPassowrd/{email}")
//	@Consumes(MediaType.APPLICATION_JSON)
	public Response forgotPassword(@PathParam("email") String email) throws JsonProcessingException {
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<Person> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		Person user = new Person();
		
		try {
			if(email.isEmpty()) {
				apiResponseStatus = ApiResponseStatus.INVALID_REQUEST;
			}else {
				DatabaseConnector connector = new DatabaseConnector();
				Connection connection = connector.getConnection();
				String query = "Select "
						+Database.Login.EMAIL
						+" from login where "
						+Database.Login.EMAIL+"=?";
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setString(1, email);
				
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
				
				statement.close();
				connection.close();
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
	 * @throws SQLException 
	 */
	
	@GET
	@Path("/userProfile/{id}")
	public Response userProfile(@PathParam("id") int id) throws JsonProcessingException, SQLException {
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<Person> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		Person user = new Person();
		Connection connection = null;
		try {
			if(id == 0) {
				apiResponseStatus = ApiResponseStatus.INVALID_REQUEST;
			} else {
				DatabaseConnector connector = new DatabaseConnector();
				connection = connector.getConnection();
				connection.setAutoCommit(false);
				
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
						+" LEFT JOIN "
						+Database.Profile.TABLE_NAME
						+" ON "
						+Database.Login.TABLE_NAME+"."+Database.Login.USER_ID+" = "+Database.Profile.TABLE_NAME+"."+Database.Profile.USER_ID
						+" WHERE "
						+Database.Login.TABLE_NAME+"."+Database.Login.USER_ID+" = ?";
				
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setInt(1, id);
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
				
				connection.commit();
				statement.close();
				connection.close();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			connection.rollback();
			e.printStackTrace();
			apiResponseStatus = ApiResponseStatus.DATABASE_CONNECTINO_ERROR;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			connection.rollback();
			e.printStackTrace();
			apiResponseStatus = ApiResponseStatus.MYSQL_EXCEPTION;
		}finally {
			
			response.setStatus(apiResponseStatus.getStatus_code());
			response.setMessage(apiResponseStatus.getStatus_message());
			response.setInfo(user);
			responseJson = mapper.writeValueAsString(response);
		
			if(connection != null && !connection.isClosed()){
				connection.close();
			}
			
		}
		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	/**
	 * 
	 * @param requestJson user object json 
	 * </br>
	 * <i>{
		  "user_id": 0,
		  "reffrence_id": 0,
		  "email": "test6@gmail.com",
		  "name": "",
		  "password": "",
		  "is_enable": 0,
		  "profile": {
		    "profie_id": 0,
		    "user_id": 0,
		    "account_type": 0,
		    "compnay_name": "test",
		    "fname": "Pinank",
		    "lname": "Soni",
		    "state": "Gujrat",
		    "country": "India",
		    "city": "Ankleshwar",
		    "street_address": "navidivi",
		    "alternet_mobile": "0264652535",
		    "mobile": "8000059755"
		  	}
		}</i>
	 * @return {@link Person}
	 * @throws JsonProcessingException
	 * @throws SQLException
	 */

	@POST
	@Path("/registerUser")
	public Response createUser(String requestJson) throws JsonProcessingException, SQLException {
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<Person> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		Person user = new Person();
		Connection connection = null;
		try {
			if(requestJson.isEmpty()) {
				apiResponseStatus = ApiResponseStatus.INVALID_REQUEST;
			} else {
				user = mapper.readValue(requestJson, Person.class);
				
				// init database connection
				DatabaseConnector connector = new DatabaseConnector();
				connection = connector.getConnection();
				connection.setAutoCommit(false);
				
				// check user is already available with same email or not.
				String query = "SELECT "
						+Database.Login.EMAIL
						+" FROM "
						+Database.Login.TABLE_NAME
						+" WHERE "
						+Database.Login.EMAIL+"=?";
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setString(1, user.getEmail());
				ResultSet resultSet = statement.executeQuery();
				
				resultSet.last();
				if(resultSet.getRow() >= 1) {
					apiResponseStatus = ApiResponseStatus.REGISTRATION_FAIL;
				} else {
										
					// no user is register with same email address so continue with registration
					query = "INSERT INTO "
							+Database.Login.TABLE_NAME
							+"("
							+Database.Login.EMAIL
							+"," +Database.Login.PASSWORD
							+"," +Database.Login.IS_ENABLE
							+"," +Database.Login.ROLE
							+"," +Database.Login.REFFERENCE_ID
							+")"
							+" VALUES "
							+"(?,?,?,?,?)";
					
					statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					statement.setString(1, user.getEmail());
					statement.setString(2, user.getPassword());
					statement.setInt(3, 1); // set default enable = 1
					statement.setInt(4, 3); // set default user role = 3
					statement.setInt(5, 1); // set default reference id = 1(admin)
					
					int afftectedRow = statement.executeUpdate();
					resultSet = statement.getGeneratedKeys();
					
					if(afftectedRow == 0) {
						apiResponseStatus = ApiResponseStatus.MYSQL_EXCEPTION;
					} else {
						resultSet.first();
						int lastInsertedID = resultSet.getInt(1); // last inserted user_id will always on index = 1
						resultSet.close();
						
						Profile profile = user.getProfile();
						
						// insert data into profile table
						query = "INSERT INTO "
								+Database.Profile.TABLE_NAME
								+"("
								+Database.Profile.USER_ID
								+"," +Database.Profile.ACCOUNT_TYPE
								+"," +Database.Profile.COMPANY_NAME
								+"," +Database.Profile.FIRST_NAME
								+"," +Database.Profile.LAST_NAME
								+"," +Database.Profile.STATE
								+"," +Database.Profile.COUNTRY
								+"," +Database.Profile.CITY
								+"," +Database.Profile.STREET_ADDRESS
								+"," +Database.Profile.ALTERNET_MOBILE
								+"," +Database.Profile.MOBILE
								+")"
								+" VALUES "
								+"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
						
						statement = connection.prepareStatement(query);
						statement.setInt(1, lastInsertedID);
						statement.setInt(2, 1); // static value to match database
						statement.setString(3, UtilsString.getStirng(profile.getCompnay_name()));
						statement.setString(4, UtilsString.getStirng(profile.getFname()));
						statement.setString(5, UtilsString.getStirng(profile.getLname()));
						statement.setString(6, UtilsString.getStirng(profile.getState()));
						statement.setString(7, UtilsString.getStirng(profile.getCountry()));
						statement.setString(8, UtilsString.getStirng(profile.getCity()));
						statement.setString(9, UtilsString.getStirng(profile.getStreet_address()));
						statement.setString(10, UtilsString.getStirng(profile.getAlternet_mobile()));
						statement.setString(11, UtilsString.getStirng(profile.getMobile()));
						
						afftectedRow = statement.executeUpdate();
						
						if(afftectedRow == 0) {
							//rollback transaction
						} else {
							user.setUser_id(lastInsertedID);
							profile.setUser_id(lastInsertedID);
							user.setProfile(profile);
						}
						
						
						apiResponseStatus = ApiResponseStatus.REGISTRATION_SUCCESS;
						
					}
					
					System.out.println(statement.toString());
					
				}
				
				connection.commit();
				statement.close();
				resultSet.close();
				connection.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			apiResponseStatus = ApiResponseStatus.REQUEST_PARSING_ERROR;
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			connection.rollback();
			e.printStackTrace();
			apiResponseStatus = ApiResponseStatus.DATABASE_CONNECTINO_ERROR;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			connection.rollback();
			e.printStackTrace();
			apiResponseStatus = ApiResponseStatus.MYSQL_EXCEPTION;
		}finally {
			
			response.setStatus(apiResponseStatus.getStatus_code());
			response.setMessage(apiResponseStatus.getStatus_message());
			response.setInfo(user);
			responseJson = mapper.writeValueAsString(response);
			
			if(connection != null && !connection.isClosed()) {
				connection.close();
			}
			
		}
	
		return Response.status(Status.OK).entity(responseJson).build();
	}
}
