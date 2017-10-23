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
import Enums.CartStatusEnum;
import Utils.UtilsString;
import basemodel.BaseResponse;
import database.Database;
import database.DatabaseConnector;
import model.BasicCMS;
import model.Environment;
import model.PaymentInfo;
import model.Person;
import model.Profile;
import model.UserCartProduct;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserService {

	private ObjectMapper mapper = new ObjectMapper();
	
	
	@GET
	@Path("/environment/{unique_pararm}")
	public Response environment(@PathParam("unique_pararm") String unique_pararm) throws JsonProcessingException, SQLException {
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<Environment> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		Environment environment = new Environment();
		
		Connection connection = null;
		try {
			environment.setLoginCompalsory(true);	
			environment.setCurrency_sign("₹");
			environment.setCurrency_multiplier(1.0);
			environment.setCurrency_id(1);
			environment.setBasicCMSPage(new ArrayList<BasicCMS>());
			environment.setImagePrefix("");
			environment.setThumbPrefix("");
			
			PaymentInfo paymentInfo = new PaymentInfo();
			/*paymentInfo.setIs_live_mode(0);
			paymentInfo.setKey("");
			paymentInfo.setSalt("");
			paymentInfo.setStatus("");
			paymentInfo.setTitle("");*/
			environment.setPaymentInfo(paymentInfo);
			
//			System.out.println("Length:"+unique_pararm.length()+" : "+unique_pararm);
			
			List<UserCartProduct> cartProductList = new ArrayList<>();
			
			if(unique_pararm.length() == 0) {
				environment.setUser_id(0);
				environment.setToken("");
				
				apiResponseStatus = ApiResponseStatus.ENVIRONMENT_FAIL;
			} else {
				/*String tokenQuery = "SELECT "
						+Database.UserCartTable.CART_ID
						+", "+Database.UserCartTable.CART_TOKEN
						+" FROM  "
						+Database.UserCartTable.TABLE_NAME
						+" WHERE "
						+Database.UserCartTable.CART_STATUS+" LIKE ?"
						+" AND ";
						if(unique_pararm.length() == 128) {
							tokenQuery = tokenQuery+Database.UserCartTable.CART_TOKEN+"=?";	
						} else {
							tokenQuery = tokenQuery+Database.UserCartTable.USER_ID+"=?";
						}*/
						
				
				String tokenQuery = "SELECT "
						+Database.UserCartTable.CART_ID
						+", "+Database.UserCartTable.USER_ID
						+", "+Database.UserCartTable.CART_TOKEN
						+" FROM  "
						+Database.UserCartTable.TABLE_NAME
						+" WHERE "
						+Database.UserCartTable.CART_STATUS+" LIKE ?"
						+" AND "
						+Database.UserCartTable.UNIQUE_ID+"=?";	
						
				
				DatabaseConnector connector = new DatabaseConnector();
				connection = connector.getConnection();
				connection.setAutoCommit(false);
				PreparedStatement statement = connection.prepareStatement(tokenQuery);
				statement.setString(1, CartStatusEnum.OPEN.getStatus());
				statement.setString(2, unique_pararm);
				ResultSet resultSet = statement.executeQuery();
				
				resultSet.last();
				
				
				
				if(resultSet.getRow() != 0) {
					String productQuery = "SELECT * FROM "
							+Database.UserCartProductTable.TABLE_NAME
							+" WHERE "
							+Database.UserCartProductTable.CART_ID+"=?";
					PreparedStatement productStatement = connection.prepareStatement(productQuery);
					productStatement.setInt(1, resultSet.getInt(Database.UserCartTable.CART_ID));
					ResultSet productResultset = productStatement.executeQuery();
					productResultset.last();
					/*int productCount = productResultset.getInt("count");
					environment.setCartCount(productCount);*/
					
					if(productResultset.getRow() != 0) {
						productResultset.first();
						do {
							UserCartProduct product = new UserCartProduct();
							product.setCart_id(productResultset.getInt(Database.UserCartProductTable.CART_ID));
							product.setProduct_id(productResultset.getInt(Database.UserCartProductTable.PRODUCT_ID));
							product.setProduct_name(UtilsString.getStirng(productResultset.getString(Database.UserCartProductTable.PRODUCT_NAME)));
							product.setProduct_qty(productResultset.getInt(Database.UserCartProductTable.PRODUCT_QTY));
							product.setProduct_price(String.valueOf(productResultset.getDouble(Database.UserCartProductTable.PRODUCT_PRICE)));
							product.setProduct_code(UtilsString.getStirng(productResultset.getString(Database.UserCartProductTable.PRODUCT_CODE)));
							product.setShipping_charge(productResultset.getInt(Database.UserCartProductTable.SHIPPING_CHARGE));
							product.setStatus(UtilsString.getStirng(productResultset.getString(Database.UserCartProductTable.STATUS)));
							product.setGst_type(UtilsString.getStirng(productResultset.getString(Database.UserCartProductTable.GST_TYPE)));
							product.setGst(productResultset.getInt(Database.UserCartProductTable.GST));
							product.setSubtotal(UtilsString.getStirng(productResultset.getString(Database.UserCartProductTable.SUBTOTAL)));
							product.setDescription(UtilsString.getStirng(productResultset.getString(Database.UserCartProductTable.DESCRIPTION)));
							product.setCat_id(productResultset.getInt(Database.UserCartProductTable.CATEGORY_ID));
							product.setBrand_id(productResultset.getInt(Database.UserCartProductTable.BRAND_ID));
							product.setDiscount_price(UtilsString.getStirng(productResultset.getString(Database.UserCartProductTable.DISCOUNT_PRICE)));
							product.setImage_name(UtilsString.getStirng(productResultset.getString(Database.UserCartProductTable.IMAGE_NAME)));
							product.setCategory_name(UtilsString.getStirng(productResultset.getString(Database.UserCartProductTable.CATEGORY_NAME)));
							product.setBrand_name(UtilsString.getStirng(productResultset.getString(Database.UserCartProductTable.BRAND_NAME)));
							
							cartProductList.add(product);
							productResultset.next();
						}while(!productResultset.isAfterLast());	
					}
					
					environment.setUser_id(resultSet.getInt(Database.UserCartTable.USER_ID));
					environment.setCart_id(resultSet.getInt(Database.UserCartTable.CART_ID));
					environment.setToken(resultSet.getString(Database.UserCartTable.CART_TOKEN));
					
					
				} else {
					environment.setUser_id(Integer.parseInt(unique_pararm));
					environment.setToken("");
				}
				
				apiResponseStatus = ApiResponseStatus.ENVIRONMENT_SUCCESS;
				
			}
			
			environment.setCartProductList(cartProductList);
			environment.setCartCount(cartProductList.size());
			
			
			
			
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			environment = new Environment();
			apiResponseStatus = ApiResponseStatus.DATABASE_CONNECTINO_ERROR;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			environment = new Environment();
			apiResponseStatus = ApiResponseStatus.MYSQL_EXCEPTION;
		} finally {
			
			response.setStatus(apiResponseStatus.getStatus_code());
			response.setMessage(apiResponseStatus.getStatus_message());
			response.setInfo(environment);
			responseJson = mapper.writeValueAsString(response);
			
			if(connection != null && !connection.isClosed()) {
				
				connection.commit();
				connection.close();
			}

		}
		
		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	
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
						+ ", "+Database.Login.TABLE_NAME + "."+Database.Login.PASSWORD
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
					user.setPassword(resultSet.getString(Database.Login.PASSWORD));
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
							connection.rollback();
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
	@Path("/updateUser")
	public Response updateUser(String requestJson) throws JsonProcessingException, SQLException {
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
						+Database.Login.USER_ID+"=?";
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setInt(1, user.getUser_id());
				ResultSet resultSet = statement.executeQuery();
				
				resultSet.last();
				if(resultSet.getRow() == 0) {
					apiResponseStatus = ApiResponseStatus.UPDATE_PROFILE_FAIL;
				} else {
										
					// no user is register with same email address so continue with registration
					query = "UPDATE "
							+Database.Login.TABLE_NAME
							+" SET "
							+Database.Login.PASSWORD+"=?"
							+" WHERE "
							+Database.Login.USER_ID+"=?";
					
					statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					statement.setString(1, user.getPassword());
					statement.setInt(2, user.getUser_id());
					int afftectedRow = statement.executeUpdate();
						
					Profile profile = user.getProfile();
					
					// insert data into profile table
					query = "UPDATE "
							+Database.Profile.TABLE_NAME
							+" SET "
							+Database.Profile.FIRST_NAME+"=?"
							+"," +Database.Profile.LAST_NAME+"=?"
							+"," +Database.Profile.MOBILE+"=?"
//							+"," +Database.Profile.COMPANY_NAME+"=?"
//							+"," +Database.Profile.STATE+"=?"
//							+"," +Database.Profile.COUNTRY+"=?"
//							+"," +Database.Profile.CITY+"=?"
//							+"," +Database.Profile.STREET_ADDRESS+"=?"
//							+"," +Database.Profile.ALTERNET_MOBILE+"=?"
							+" WHERE "
							+Database.Profile.USER_ID+"=?";
					
					statement = connection.prepareStatement(query);
					statement.setString(1, UtilsString.getStirng(profile.getFname()));
					statement.setString(2, UtilsString.getStirng(profile.getLname()));
					statement.setString(3, UtilsString.getStirng(profile.getMobile()));
//					statement.setString(4, UtilsString.getStirng(profile.getCompnay_name()));
//					statement.setString(5, UtilsString.getStirng(profile.getState()));
//					statement.setString(6, UtilsString.getStirng(profile.getCountry()));
//					statement.setString(7, UtilsString.getStirng(profile.getCity()));
//					statement.setString(8, UtilsString.getStirng(profile.getStreet_address()));
//					statement.setString(9, UtilsString.getStirng(profile.getAlternet_mobile()));
					
					statement.setInt(4, user.getUser_id());
//					statement.setInt(10, user.getUser_id());
					
					afftectedRow = statement.executeUpdate();
					
					if(afftectedRow == 0) {
						//rollback transaction
						connection.rollback();
						apiResponseStatus = ApiResponseStatus.UPDATE_PROFILE_FAIL;
					} else {
						apiResponseStatus = ApiResponseStatus.UPDATE_PROFILE_SUCCESS;	
					}
					
//					System.out.println(statement.toString());
					
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
