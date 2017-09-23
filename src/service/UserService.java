package service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;

import Enums.ApiResponseStatus;
import basemodel.BaseResponse;
import database.Database;
import database.DatabaseConnector;
import model.Person;

@Path("/user")
public class UserService {

	private ObjectMapper mapper = new ObjectMapper();
	
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
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
				String quary = "Select "
						+Database.Login.USER_ID
						+","+Database.Login.EMAIL
						+","+Database.Login.PASSWORD
						+","+Database.Login.IS_ENABLE
						
						+" from login where "
						+ Database.Login.EMAIL+"=? and "
						+ Database.Login.PASSWORD+"=?";
				PreparedStatement preparedStatement = connection.prepareStatement(quary);
				preparedStatement.setString(1, user.getEmail());
				preparedStatement.setString(2, user.getPassword());
				
				System.out.println(preparedStatement.toString());
				
				ResultSet resultSet = preparedStatement.executeQuery();
				resultSet.last();
				int rowCount = resultSet.getRow();
				System.out.println("Resultset: "+resultSet.toString());
				System.out.println("row count: "+rowCount);
				
				if(rowCount == 0) {

					System.out.println(resultSet.getInt(0));
					System.out.println(resultSet.getString(1));
					apiResponseStatus = ApiResponseStatus.LOGIN_FAIL;
				} else if(rowCount > 1){
					apiResponseStatus = ApiResponseStatus.MULTIPLE_USER_FOUND;
				} else {
					resultSet.first();
					System.out.println(resultSet.toString());
					user.setId(resultSet.getInt(Database.Login.USER_ID));
					user.setEmail(resultSet.getString(Database.Login.EMAIL));
					user.setPassword(resultSet.getString(Database.Login.PASSWORD));
					user.setIs_enable(resultSet.getInt(Database.Login.IS_ENABLE));
					user.setReffrence_id(resultSet.getInt(Database.Login.REFFERENCE_ID));
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
		
		
		return Response.status(200).entity(responseJson).build();
	}
}
