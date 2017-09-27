package service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
import model.Address;


@Path("/address")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AddressService {

	private ObjectMapper mapper = new ObjectMapper();
	
	@GET
	@Path("/get/{id}")
	public Response getUserAddress(@PathParam("id") int id) throws SQLException, JsonProcessingException {
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<List<Address>> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		List<Address> addressList = new ArrayList<>();
		
		Connection connection = null;
		
		try {
			if(id == 0) {
				apiResponseStatus = ApiResponseStatus.ADDRESS_INVALID_USER_ID;
			}  else {
				addressList.clear();
				addressList.addAll(getUserAddressList(id));
				if(addressList.size() == 0) {
					apiResponseStatus = ApiResponseStatus.ADDRESS_ADD_FAIL;
				} else {
					apiResponseStatus = apiResponseStatus.ADDRESS_SUCCESS;
				}
			}
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			connection.rollback();
			e.printStackTrace();
			apiResponseStatus = ApiResponseStatus.DATABASE_CONNECTINO_ERROR;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			connection.rollback();
			e.printStackTrace();
			apiResponseStatus = ApiResponseStatus.MYSQL_EXCEPTION;
		} finally {

			response.setStatus(apiResponseStatus.getStatus_code());
			response.setMessage(apiResponseStatus.getStatus_message());
			response.setInfo(addressList);
			responseJson = mapper.writeValueAsString(response);
			
			if(connection != null && !connection.isClosed()) {
				connection.close();
			}
		}

		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	
	@GET
	@Path("/remove/{id}")
	public Response removeUserAddress(@PathParam("id") int id) throws SQLException, JsonProcessingException {
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<List<Address>> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		List<Address> addressList = new ArrayList<>();
		
		Connection connection = null;
		
		try {
			if(id == 0) {
				apiResponseStatus = ApiResponseStatus.ADDRESS_INVALID_ADDRESS_ID;
			}  else {
				
				DatabaseConnector connector = new DatabaseConnector();
				connection = connector.getConnection();
				connection.setAutoCommit(false);
				String query  = "DELETE FROM "
						+Database.UserAddress.TABLE_NAME
						+" WHERE "
						+Database.UserAddress.ADDRESS_ID+"=?";
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setInt(1, id);
				int affectedRow = statement.executeUpdate();
				
				if(affectedRow == 0 || affectedRow > 1) {
					connection.rollback();
					apiResponseStatus = ApiResponseStatus.ADDRESS_DELETE_FAIL;
				} else {
					
					connection.commit();
					addressList.clear();
					addressList.addAll(getUserAddressList(id));
					
						response.setInfo(addressList);
						
						statement.close();
						connection.close();
						
						apiResponseStatus = ApiResponseStatus.ADDRESS_DELETE_SUCCESS;
					
				}
			}
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			connection.rollback();
			e.printStackTrace();
			apiResponseStatus = ApiResponseStatus.DATABASE_CONNECTINO_ERROR;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			connection.rollback();
			e.printStackTrace();
			apiResponseStatus = ApiResponseStatus.MYSQL_EXCEPTION;
		} finally {

			response.setStatus(apiResponseStatus.getStatus_code());
			response.setMessage(apiResponseStatus.getStatus_message());
			response.setInfo(addressList);
			responseJson = mapper.writeValueAsString(response);
			
			if(connection != null && !connection.isClosed()) {
				connection.close();
			}
		}

		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	
	@POST
	@Path("/add")
	public Response addNewAddress(String requestJson) throws SQLException, JsonProcessingException {
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<List<Address>> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		List<Address> addressList = new ArrayList<>();
		
		Connection connection = null;
		
		try {
				Address address = mapper.readValue(requestJson, Address.class);
				DatabaseConnector connector = new DatabaseConnector();
				connection = connector.getConnection();
				connection.setAutoCommit(false);
				
				String query = "INSERT INTO "
						+Database.UserAddress.TABLE_NAME
						+" ("
						+Database.UserAddress.ADDRESS1
						+", "+Database.UserAddress.ADDRESS2
						+", "+Database.UserAddress.STATE
						+", "+Database.UserAddress.CITY
						+", "+Database.UserAddress.POSTCODE
						+", "+Database.UserAddress.ADDITIONAL_DETIALS
						+", "+Database.UserAddress.USER_ID
						+", "+Database.UserAddress.IS_ENABLE
						+", "+Database.UserAddress.FULL_NAME
						+", "+Database.UserAddress.EMAIL
						+", "+Database.UserAddress.DEFAULT_VALUE
						+")"
						+ " VALUES "
						+"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, address.getAddress1());
				statement.setString(2, address.getAddress2());
				statement.setString(3, address.getState());
				statement.setString(4, address.getCity());
				statement.setString(5, address.getPostcode());
				statement.setString(6, address.getAddition_detail());
				statement.setString(7, address.getUser_id());
				statement.setInt(8, 1); // set is_enable value 1 to default enable
				statement.setString(9, address.getFull_name());
				statement.setString(10, address.getEmail());
				statement.setInt(11, 0); // set default value 0
				
				
				int affectedRaw = statement.executeUpdate();
				if(affectedRaw == 0) {
					apiResponseStatus = ApiResponseStatus.ADDRESS_ADD_FAIL;
					connection.rollback();
				} else {
					ResultSet resultSet = statement.getGeneratedKeys();
					resultSet.first();
					int insertedNewID = resultSet.getInt(1);
					connection.commit();
					addressList.clear();
					addressList.addAll(getUserAddressList(Integer.parseInt(address.getUser_id())));
					response.setInfo(addressList);
					resultSet.close();
					
					apiResponseStatus = ApiResponseStatus.ADDRESS_ADD_SUCCESS; 
				}
				connection.commit();
				statement.close();
				connection.close();
			
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			connection.rollback();
			e.printStackTrace();
			apiResponseStatus = ApiResponseStatus.DATABASE_CONNECTINO_ERROR;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			connection.rollback();
			e.printStackTrace();
			apiResponseStatus = ApiResponseStatus.MYSQL_EXCEPTION;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			response.setStatus(apiResponseStatus.getStatus_code());
			response.setMessage(apiResponseStatus.getStatus_message());
			response.setInfo(addressList);
			responseJson = mapper.writeValueAsString(response);
			
			if(connection != null && !connection.isClosed()) {
				connection.close();
			}
		}

		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	
	
	@POST
	@Path("/update")
	public Response updateUserAddress(String requestJson) throws SQLException, JsonProcessingException {
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<List<Address>> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		List<Address> addressList = new ArrayList<>();
		
		Connection connection = null;
		
		try {
				Address address = mapper.readValue(requestJson, Address.class);
				DatabaseConnector connector = new DatabaseConnector();
				connection = connector.getConnection();
				connection.setAutoCommit(false);
				
				String query = "UPDATE "
						+Database.UserAddress.TABLE_NAME
						+" SET "
						+Database.UserAddress.ADDRESS1 +"=?"
						+", "+Database.UserAddress.ADDRESS2+"=?"
						+", "+Database.UserAddress.STATE+"=?"
						+", "+Database.UserAddress.CITY+"=?"
						+", "+Database.UserAddress.POSTCODE+"=?"
						+", "+Database.UserAddress.ADDITIONAL_DETIALS+"=?"
						+", "+Database.UserAddress.FULL_NAME+"=?"
						+", "+Database.UserAddress.EMAIL+"=?"
						+", "+Database.UserAddress.DEFAULT_VALUE+"=?"
						+""
						+" WHERE "
						+Database.UserAddress.ADDRESS_ID+"=?";
				
				PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, address.getAddress1());
				statement.setString(2, address.getAddress2());
				statement.setString(3, address.getState());
				statement.setString(4, address.getCity());
				statement.setString(5, address.getPostcode());
				statement.setString(6, address.getAddition_detail());
				statement.setString(7, address.getFull_name());
				statement.setString(8, address.getEmail());
				statement.setInt(9, address.getDefault_value());
				statement.setInt(10, address.getAddress_id());
				
				int affectedRaw = statement.executeUpdate();
				if(affectedRaw == 0) {
					apiResponseStatus = ApiResponseStatus.ADDRESS_UPDATE_FAIL;
					connection.rollback();
				} else {
//					ResultSet resultSet = statement.getGeneratedKeys();
//					resultSet.first();
//					int insertedNewID = resultSet.getInt(1);
					connection.commit();
					addressList.clear();
//					addressList.addAll(getUserAddressList(Integer.parseInt(address.getUser_id())));
					response.setInfo(addressList);
//					resultSet.close();
					
					apiResponseStatus = ApiResponseStatus.ADDRESS_UPDATE_SUCCESS; 
				}
				connection.commit();
				statement.close();
				connection.close();
			
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			connection.rollback();
			e.printStackTrace();
			apiResponseStatus = ApiResponseStatus.DATABASE_CONNECTINO_ERROR;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			connection.rollback();
			e.printStackTrace();
			apiResponseStatus = ApiResponseStatus.MYSQL_EXCEPTION;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			response.setStatus(apiResponseStatus.getStatus_code());
			response.setMessage(apiResponseStatus.getStatus_message());
			response.setInfo(addressList);
			responseJson = mapper.writeValueAsString(response);
			
			if(connection != null && !connection.isClosed()) {
				connection.close();
			}
		}

		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	
	private List<Address> getUserAddressList(int id) throws ClassNotFoundException, SQLException{
		List<Address> list = new ArrayList<>();
		DatabaseConnector connector = new DatabaseConnector();
		Connection connection = connector.getConnection();
		connection.setAutoCommit(false);
		
		String query = "SELECT "
				+Database.UserAddress.ADDRESS_ID
				+", "+Database.UserAddress.ADDRESS1
				+", "+Database.UserAddress.ADDRESS2
				+", "+Database.UserAddress.STATE
				+", "+Database.UserAddress.CITY
				+", "+Database.UserAddress.POSTCODE
				+", "+Database.UserAddress.ADDITIONAL_DETIALS
				+", "+Database.UserAddress.USER_ID
				+", "+Database.UserAddress.FULL_NAME
				+", "+Database.UserAddress.EMAIL
				+", "+Database.UserAddress.DEFAULT_VALUE
				+" FROM "
				+Database.UserAddress.TABLE_NAME
				+" WHERE "
				+Database.UserAddress.USER_ID+"=?"
				+" AND "
				+Database.UserAddress.IS_ENABLE+"=?";
		
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, id);
		statement.setInt(2, 1);
		
		ResultSet resultSet = statement.executeQuery();
		connection.commit();
		resultSet.last();
		if(resultSet.getRow() == 0) {
			list.clear();
		} else {
			
			resultSet.first();
			do {
				Address address = new Address();
				address.setAddress_id(resultSet.getInt(Database.UserAddress.ADDRESS_ID));
				address.setAddress1(UtilsString.getStirng(resultSet.getString(Database.UserAddress.ADDRESS1)));
				address.setAddress2(UtilsString.getStirng(resultSet.getString(Database.UserAddress.ADDRESS2)));
				address.setState(UtilsString.getStirng(resultSet.getString(Database.UserAddress.STATE)));
				address.setCity(UtilsString.getStirng(resultSet.getString(Database.UserAddress.CITY)));
				address.setPostcode(UtilsString.getStirng(resultSet.getString(Database.UserAddress.POSTCODE)));
				address.setAddition_detail(UtilsString.getStirng(resultSet.getString(Database.UserAddress.ADDITIONAL_DETIALS)));
				address.setUser_id(UtilsString.getStirng(resultSet.getString(Database.UserAddress.USER_ID)));
				address.setFull_name(UtilsString.getStirng(resultSet.getString(Database.UserAddress.FULL_NAME)));
				address.setEmail(UtilsString.getStirng(resultSet.getString(Database.UserAddress.EMAIL)));
				address.setDefault_value(resultSet.getInt(Database.UserAddress.DEFAULT_VALUE));
				
				list.add(address);
				
				resultSet.next();
			}while(!resultSet.isAfterLast());
			
		}
		
		
		resultSet.close();
		statement.close();
		connection.close();
		return list;
	}
}
