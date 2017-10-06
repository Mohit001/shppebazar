package service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Enums.ApiResponseStatus;
import Enums.CartStatusEnum;
import basemodel.BaseResponse;
import database.Database;

import database.DatabaseConnector;
import model.UserCart;
import model.UserCartProduct;

@Path("/cart")
@Produces(MediaType.APPLICATION_JSON)
public class CartService {
	
	private UserCart userCart;
	
	ObjectMapper mapper = new ObjectMapper();

	@Path("/addUpdateProduct")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addUpdateProductToCart(String jsonRequest) throws JsonParseException, JsonMappingException, IOException, SQLException {
		
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<UserCart> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		userCart = null;
		
		Connection connection = null;
		try {
			DatabaseConnector connector = new DatabaseConnector();
			connection = connector.getConnection();
			connection.setAutoCommit(false);
			
			userCart = mapper.readValue(jsonRequest, UserCart.class);
			
			int usercartid = 0;
			if (userCart.getUser_id() != 0) {
//				cartList = productCartDao.getUSerCartByUserId(Integer.parseInt(userloginId));
				usercartid = getUserCartID(connection,String.valueOf(userCart.getUser_id()), true, userCart.getToken());
			} else {
//				cartList = productCartDao.getUSerCartByIpAddress(ipAddress);
				usercartid = getUserCartID(connection, userCart.getToken(), false, null);
			}
			
			
			if(usercartid == 0) {
				// create new cart and insert product
				String salt= generateSalt();
				String token = generateToken(userCart.getToken(), salt);	

				userCart.setToken(token);
				userCart.setSalt(salt);
				
				for(int i=0; i< userCart.getUserCartProduct().size(); i++) {
					UserCartProduct product = userCart.getUserCartProduct().get(i);
				apiResponseStatus = insertItemIntoCart(userCart, product, connection, apiResponseStatus);
				}
				
				
			} else if(usercartid == -1) {
				// multiple cart found for same userid or ip 
				apiResponseStatus = ApiResponseStatus.CART_MULTIPLE_INSTANCE;
			} else {
				// cart is already generate now add or update product
				List<UserCartProduct> list = userCart.getUserCartProduct();
				if(list.size() == 0) {
					apiResponseStatus = ApiResponseStatus.CART_PRODUCT_MISSING;
				} else {
					UserCartProduct cartProduct = list.get(0);
					String getProductQuery = "SELECT * "
							+" FROM "
							+Database.UserCartProductTable.TABLE_NAME
							+" WHERE "
							+Database.UserCartProductTable.CART_ID+"=?"
							+" AND "
							+Database.UserCartProductTable.PRODUCT_ID+"=?";
					
					
					PreparedStatement getProductStatement = connection.prepareStatement(getProductQuery);
					getProductStatement.setInt(1, usercartid);
					getProductStatement.setInt(2, cartProduct.getProduct_id());
					ResultSet resultSet = getProductStatement.executeQuery();
					
					resultSet.last();
					
					int rowCount = resultSet.getRow();
					for(int i=0; i< list.size(); i++) {
						UserCartProduct product = list.get(i);
						if(rowCount == 0) {
							// execute insert
							apiResponseStatus = insertItemIntoCart(userCart, product, connection, apiResponseStatus);
						} else {
							userCart.setCart_id(usercartid);
							apiResponseStatus = updateCartItem(userCart, product, connection, apiResponseStatus);
						}
						
					}
					
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
			response.setInfo(userCart);
			responseJson = mapper.writeValueAsString(response);
			
			if(connection != null && !connection.isClosed()) {
				connection.commit();
				connection.close();
			}
		}

		
		
		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/removeCartProduct/{cart_id}/{product_id}")
	public Response removeProductFromCart(@PathParam("cart_id") int cart_id, @PathParam("product_id") int product_id) throws SQLException, JsonProcessingException {
		
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<UserCart> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		userCart = null;
		
		Connection connection = null;
		try {
			DatabaseConnector connector = new DatabaseConnector();
			connection = connector.getConnection();
			connection.setAutoCommit(false);
			
			String deleteQuery = "DELETE FROM "
					+Database.UserCartProductTable.TABLE_NAME
					+" WHERE "
					+Database.UserCartProductTable.CART_ID+"=?"
					+" AND "
					+Database.UserCartProductTable.PRODUCT_ID+"=?";
			
			PreparedStatement statement = connection.prepareStatement(deleteQuery);
			statement.setInt(1, cart_id);
			statement.setInt(2, product_id);
			int affectedRow = statement.executeUpdate();
			
			if(affectedRow == 0) {
				apiResponseStatus = ApiResponseStatus.CART_PRODUCT_REMOVE_FAIL;
			} else {
				userCart = getUserCart(connection, cart_id);
				apiResponseStatus = ApiResponseStatus.CART_PRODUCT_REMOVE_SUCCESS;
				
			}
			
		}catch (ClassNotFoundException e) {
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
			response.setInfo(userCart);
			responseJson = mapper.writeValueAsString(response);
			
			if(connection != null && !connection.isClosed()) {
				connection.commit();
				connection.close();
			}
		}

		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCart/{cart_id}")
	public Response getCartList(@PathParam("cart_id") int cart_id) throws SQLException, JsonProcessingException {
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<UserCart> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		userCart = null;
		
		Connection connection = null;
		try {
			DatabaseConnector connector = new DatabaseConnector();
			connection = connector.getConnection();
			connection.setAutoCommit(false);
			
			userCart = getUserCart(connection, cart_id);
			
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
			response.setInfo(userCart);
			responseJson = mapper.writeValueAsString(response);
			
			if(connection != null && !connection.isClosed()) {
				connection.commit();
				connection.close();
			}
		}

		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	private UserCart getUserCart(Connection connection, int cart_id) throws SQLException {
		UserCart userCart = new UserCart();
		if (cart_id != 0) {
			String getUsercartQuery = "SELECT * FROM "
					+Database.UserCartTable.TABLE_NAME
					+" WHERE "
					+Database.UserCartTable.CART_ID+"=? "
					+" AND "
					+Database.UserCartTable.CART_STATUS+"=?";
			PreparedStatement statement = connection.prepareStatement(getUsercartQuery);
			statement.setInt(1, cart_id);
			statement.setString(2, CartStatusEnum.OPEN.getStatus());
			ResultSet resultSet = statement.executeQuery();
			resultSet.last();
			if(resultSet.getRow() != 0) {
				userCart.setCart_id(resultSet.getInt(Database.UserCartTable.CART_ID));
				userCart.setUser_id(resultSet.getInt(Database.UserCartTable.USER_ID));
				userCart.setCart_status(resultSet.getString(Database.UserCartTable.CART_STATUS));
				userCart.setToken(resultSet.getString(Database.UserCartTable.CART_TOKEN));
				userCart.setShipping_address_id(resultSet.getInt(Database.UserCartTable.CART_SHIPPING_ID));
				userCart.setBilling_address_id(resultSet.getInt(Database.UserCartTable.CART_BILLING_ID));
				userCart.setPayment_type_id(resultSet.getInt(Database.UserCartTable.CART_PAYMENT_TYPE_ID));
				userCart.setSalt(resultSet.getString(Database.UserCartTable.SALT));
				
				List<UserCartProduct> list = new ArrayList<>();
				
				String getUserCartProductQuery = "SELECT * FROM "
						+Database.UserCartProductTable.TABLE_NAME
						+" WHERE "
						+Database.UserCartProductTable.CART_ID+"=?";
				
				PreparedStatement cartProductStatement = connection.prepareStatement(getUserCartProductQuery);
				cartProductStatement.setInt(1, cart_id);
				ResultSet userCartProductResultSet = cartProductStatement.executeQuery();
				resultSet.last();
				if(userCartProductResultSet.getRow() != 0) {
					userCartProductResultSet.first();
					do {
						UserCartProduct product = new UserCartProduct();
						product.setCart_id(userCartProductResultSet.getInt(Database.UserCartProductTable.CART_ID));
						product.setProduct_id(userCartProductResultSet.getInt(Database.UserCartProductTable.PRODUCT_ID));
						product.setProduct_name(userCartProductResultSet.getString(Database.UserCartProductTable.PRODUCT_NAME));
						product.setProduct_qty(userCartProductResultSet.getInt(Database.UserCartProductTable.PRODUCT_QTY));
						product.setProduct_price(userCartProductResultSet.getDouble(Database.UserCartProductTable.PRODUCT_PRICE));
						product.setProduct_code(userCartProductResultSet.getString(Database.UserCartProductTable.PRODUCT_CODE));
						product.setShipping_charge(userCartProductResultSet.getInt(Database.UserCartProductTable.SHIPPING_CHARGE));
						product.setStatus(userCartProductResultSet.getString(Database.UserCartProductTable.STATUS));
						product.setGst_type(userCartProductResultSet.getString(Database.UserCartProductTable.GST_TYPE));
						product.setGst(userCartProductResultSet.getDouble(Database.UserCartProductTable.GST));
						list.add(product);
						userCartProductResultSet.next();
					}while(!userCartProductResultSet.isAfterLast());
				}
				userCart.setUserCartProduct(list);
			}
		}
		return userCart;
	}
	private ApiResponseStatus insertItemIntoCart(UserCart userCart,UserCartProduct userCartProduct, Connection connection, ApiResponseStatus apiResponseStatus) throws ClassNotFoundException, SQLException {
		// generate salt and than create new token
		String salt, token;
		if(userCart.getToken() == null || userCart.getToken().isEmpty()) {
			salt= generateSalt();
			token = generateToken(userCart.getToken(), salt);	
		} else {
			token = userCart.getToken();
			salt = userCart.getSalt();
		}	
		
		//now create new cart for user or guest user;
		String insertQuery = "INSERT INTO "
				+Database.UserCartTable.TABLE_NAME
				+"("
				+Database.UserCartTable.USER_ID
				+", "+Database.UserCartTable.CART_STATUS
				+", "+Database.UserCartTable.CART_TOKEN
				+", "+Database.UserCartTable.CART_SHIPPING_ID
				+", "+Database.UserCartTable.CART_BILLING_ID
				+", "+Database.UserCartTable.CART_PAYMENT_TYPE_ID
				+", "+Database.UserCartTable.SALT
				+")"
				+" VALUES "
				+"(?, ?, ?, ?, ?, ?, ?)";
		
		DatabaseConnector connector = new DatabaseConnector();
		connection = connector.getConnection();
		connection.setAutoCommit(false);
		PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, userCart.getUser_id());
		statement.setString(2, CartStatusEnum.OPEN.getStatus());
		statement.setString(3, token);
		statement.setInt(4, userCart.getShipping_address_id());
		statement.setInt(5, userCart.getBilling_address_id());
		statement.setInt(6, userCart.getPayment_type_id());
		statement.setString(7, salt);
		
		int affectedRows = statement.executeUpdate();

		if(affectedRows == 0) {
			connection.rollback();
			apiResponseStatus = ApiResponseStatus.CART_PRODUCT_ADD_FAIL;
		} else {
			
			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.first();
			int lastInsertedID = resultSet.getInt(1);
			
			String productInsertQuery = "INSERT INTO "
					+Database.UserCartProductTable.TABLE_NAME
					+"("
					+Database.UserCartProductTable.CART_ID
					+", "+Database.UserCartProductTable.PRODUCT_ID
					+", "+Database.UserCartProductTable.PRODUCT_NAME
					+", "+Database.UserCartProductTable.PRODUCT_QTY
					+", "+Database.UserCartProductTable.PRODUCT_PRICE
					+", "+Database.UserCartProductTable.PRODUCT_CODE
					+", "+Database.UserCartProductTable.SHIPPING_CHARGE
					+", "+Database.UserCartProductTable.STATUS
					+", "+Database.UserCartProductTable.GST_TYPE
					+", "+Database.UserCartProductTable.GST
					+")"
					+" VALUES "
					+"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement cartProductStatement = connection.prepareStatement(productInsertQuery);
			cartProductStatement.setInt(1, lastInsertedID);
			cartProductStatement.setInt(2, userCartProduct.getProduct_id());
			cartProductStatement.setString(3, userCartProduct.getProduct_name());
			cartProductStatement.setInt(4, userCartProduct.getProduct_qty());
			cartProductStatement.setDouble(5, userCartProduct.getProduct_price());
			cartProductStatement.setString(6, userCartProduct.getProduct_code());
			cartProductStatement.setInt(7, userCartProduct.getShipping_charge());
			cartProductStatement.setString(8, CartStatusEnum.OPEN.getStatus());
			cartProductStatement.setString(9, userCartProduct.getGst_type());
			cartProductStatement.setDouble(10, userCartProduct.getGst());
			
			int cartProductAffectedRow = cartProductStatement.executeUpdate();
			if(cartProductAffectedRow == 0) {
				apiResponseStatus = ApiResponseStatus.CART_PRODUCT_ADD_FAIL;
				connection.rollback();
			} else {
				apiResponseStatus = ApiResponseStatus.CART_PRODUCT_ADD_SUCCESS;
				userCart.setCart_id(lastInsertedID);
				
				
			}
		}
		
		return apiResponseStatus;
	}
	
	
	private ApiResponseStatus updateCartItem(UserCart userCart, UserCartProduct cartProduct, Connection connection, ApiResponseStatus apiResponseStatus) throws ClassNotFoundException, SQLException {
		// generate salt and than create new token
		String salt, token;
		if(userCart.getToken() == null || userCart.getToken().isEmpty()) {
			salt= generateSalt();
			token = generateToken(userCart.getToken(), salt);	
		} else {
			salt = userCart.getSalt();
			token = userCart.getToken();
		}
		
		//now create new cart for user or guest user;
		String insertQuery = "SELECT "
				+Database.UserCartProductTable.PRODUCT_QTY
				+" FROM "
				+Database.UserCartProductTable.TABLE_NAME
				+" WHERE "
				+Database.UserCartProductTable.CART_ID+"=?"
				+" AND "
				+Database.UserCartProductTable.PRODUCT_ID+"=?";
		
//		DatabaseConnector connector = new DatabaseConnector();
//		connection = connector.getConnection();
//		connection.setAutoCommit(false);
		PreparedStatement statement = connection.prepareStatement(insertQuery);
		statement.setInt(1, userCart.getCart_id());
		statement.setInt(2, cartProduct.getProduct_id());
		ResultSet resultSet = statement.executeQuery();
		resultSet.first();
		
		int oldQTY = resultSet.getInt(Database.UserCartProductTable.PRODUCT_QTY);
		int newQTY = oldQTY + cartProduct.getProduct_qty();
		if(newQTY > 10) {
			apiResponseStatus = ApiResponseStatus.CART_QUENTITY_EXCEED;
		} else {
			
			String productUpdateQuery = "UPDATE "
					+Database.UserCartProductTable.TABLE_NAME
					+" SET "
					+Database.UserCartProductTable.PRODUCT_QTY+"=?"
					+" WHERE "
					+Database.UserCartProductTable.CART_ID+"=?"
					+" AND "
					+Database.UserCartProductTable.PRODUCT_ID+"=?";
			
			PreparedStatement cartProductStatement = connection.prepareStatement(productUpdateQuery);
			cartProductStatement.setInt(1, newQTY);
			cartProductStatement.setInt(2, userCart.getCart_id());
			cartProductStatement.setInt(3, cartProduct.getProduct_id());
			int cartProductAffectedRow = cartProductStatement.executeUpdate();
			if(cartProductAffectedRow == 0) {
				apiResponseStatus = ApiResponseStatus.CART_PRODUCT_UPDATE_FAIL;
				connection.rollback();
			} else {
				userCart.setToken(token);
				userCart.setSalt(salt);
				userCart.getUserCartProduct().get(0).setProduct_qty(newQTY);
				apiResponseStatus = ApiResponseStatus.CART_PRODUCT_UPDATE_SUCCESS;
				
				
			}
		}
	
		
		return apiResponseStatus;
	}
	
	private int getUserCartID(Connection connection, String param, boolean isUserid, String token){
		int userCartID = 0;
		
		StringBuilder query = new StringBuilder();
		String partQuery1 = "SELECT "
				+Database.UserCartTable.CART_ID
				+" FROM "
				+Database.UserCartTable.TABLE_NAME
				+" WHERE "
				+Database.UserCartTable.CART_STATUS+"=?"
				+" AND ";
		
		query.append(partQuery1);
				
		String partQuery2 = "";
		if(isUserid) {
			// param is userid
			if(token==null || token.isEmpty())
				partQuery2 = Database.UserCartTable.USER_ID+"=?";
			else
				partQuery2 = Database.UserCartTable.USER_ID+"=?"
							+" AND "
							+Database.UserCartTable.CART_TOKEN+"=?";
		} else {
			//param is ipAddress
			partQuery2 = Database.UserCartTable.CART_TOKEN+"=?";
		}
		
		query.append(partQuery2);
		
		try {
			PreparedStatement statement = connection.prepareStatement(query.toString());
			statement.setString(1, "open");
			statement.setString(2, param);
			if(token != null && !token.isEmpty()) {
				statement.setString(3, token);
			}
			
			ResultSet resultSet = statement.executeQuery();
			resultSet.last();
			if(resultSet.getRow() == 0) {
				userCartID = 0;
			} else if(resultSet.getRow() == 1){
				userCartID = resultSet.getInt(Database.UserCartTable.CART_ID);
			} else {
				userCartID = -1;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userCartID;
	}
	
	private String generateSalt() {
		String salt_prefix = "@klavya!nfotec#";
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime())); //20161116120843
		return salt_prefix+"_"+dateFormat.format(cal.getTime());
	}
	private String generateToken(String uniqueid, String salt) {
		
		String finalToken = "";
		
		try {
	         MessageDigest md = MessageDigest.getInstance("SHA-512");
	         md.update(salt.getBytes("UTF-8"));
	         byte[] bytes = md.digest(uniqueid.getBytes("UTF-8"));
	         StringBuilder sb = new StringBuilder();
	         for(int i=0; i< bytes.length ;i++){
	            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	         }
	         finalToken = sb.toString();
	        } 
	       catch (NoSuchAlgorithmException e){
	        e.printStackTrace();
	       } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return finalToken;
	}
	
	@GET
	@Path("/setShippingAddress/{cart_id}/{address_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setShippingAddressTocart(@PathParam("cart_id") int cart_id, @PathParam("address_id") int address_id) throws SQLException, JsonProcessingException {
	
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<UserCart> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		userCart = null;
		
		Connection connection = null;
		try {
			DatabaseConnector connector = new DatabaseConnector();
			connection = connector.getConnection();
			connection.setAutoCommit(false);
			
			String deleteQuery = "UPDATE "
					+Database.UserCartTable.TABLE_NAME
					+" SET "
					+Database.UserCartTable.CART_SHIPPING_ID+"=?"
					+" WHERE "
					+Database.UserCartTable.CART_ID+"=?";
			
			PreparedStatement statement = connection.prepareStatement(deleteQuery);
			statement.setInt(1, address_id);
			statement.setInt(2, cart_id);
			int affectedRow = statement.executeUpdate();
			
			apiResponseStatus = ApiResponseStatus.CART_SHIPPING_ADDRESS_UPDATE_SUCCESS;
			
			/*if(affectedRow == 0) {
				apiResponseStatus = ApiResponseStatus.CART_SHIPPING_ADDRESS_UPDATE_FAIL;
			} else {
				apiResponseStatus = ApiResponseStatus.CART_SHIPPING_ADDRESS_UPDATE_SUCCESS;	
			}*/
			
		}catch (ClassNotFoundException e) {
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
			response.setInfo(userCart);
			responseJson = mapper.writeValueAsString(response);
			
			if(connection != null && !connection.isClosed()) {
				connection.commit();
				connection.close();
			}
		}

		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	
	
	@GET
	@Path("/setBillingAddress/{cart_id}/{address_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setBillingAddressTocart(@PathParam("cart_id") int cart_id, @PathParam("address_id") int address_id) throws SQLException, JsonProcessingException {
	
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<UserCart> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		userCart = null;
		
		Connection connection = null;
		try {
			DatabaseConnector connector = new DatabaseConnector();
			connection = connector.getConnection();
			connection.setAutoCommit(false);
			
			String deleteQuery = "UPDATE "
					+Database.UserCartTable.TABLE_NAME
					+" SET "
					+Database.UserCartTable.CART_BILLING_ID+"=?"
					+" WHERE "
					+Database.UserCartTable.CART_ID+"=?";
			
			PreparedStatement statement = connection.prepareStatement(deleteQuery);
			statement.setInt(1, address_id);
			statement.setInt(2, cart_id);
			int affectedRow = statement.executeUpdate();
			
			apiResponseStatus = ApiResponseStatus.CART_BILLING_ADDRESS_UPDATE_SUCCESS;
			
			/*if(affectedRow == 0) {
				apiResponseStatus = ApiResponseStatus.CART_BILLING_ADDRESS_UPDATE_FAIL;
			} else {
				apiResponseStatus = ApiResponseStatus.CART_BILLING_ADDRESS_UPDATE_SUCCESS;	
			}*/
			
		}catch (ClassNotFoundException e) {
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
			response.setInfo(userCart);
			responseJson = mapper.writeValueAsString(response);
			
			if(connection != null && !connection.isClosed()) {
				connection.commit();
				connection.close();
			}
		}

		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	@GET
	@Path("/setPaymentType/{cart_id}/{payment_type_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setPaymentTypeTocart(@PathParam("cart_id") int cart_id, @PathParam("payment_type_id") int payment_type_id) throws SQLException, JsonProcessingException {
	
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<UserCart> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		userCart = null;
		
		Connection connection = null;
		try {
			DatabaseConnector connector = new DatabaseConnector();
			connection = connector.getConnection();
			connection.setAutoCommit(false);
			
			String deleteQuery = "UPDATE "
					+Database.UserCartTable.TABLE_NAME
					+" SET "
					+Database.UserCartTable.CART_PAYMENT_TYPE_ID+"=?"
					+" WHERE "
					+Database.UserCartTable.CART_ID+"=?";
			
			PreparedStatement statement = connection.prepareStatement(deleteQuery);
			statement.setInt(1, payment_type_id);
			statement.setInt(2, cart_id);
			int affectedRow = statement.executeUpdate();
			
			if(affectedRow == 0) {
				apiResponseStatus = ApiResponseStatus.CART_PAYMENT_TYPE_UPDATE_FAIL;
			} else {
				apiResponseStatus = ApiResponseStatus.CART_PAYMENT_TYPE_UPDATE_SUCCESS;
				
			}
			
		}catch (ClassNotFoundException e) {
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
			response.setInfo(userCart);
			responseJson = mapper.writeValueAsString(response);
			
			if(connection != null && !connection.isClosed()) {
				connection.commit();
				connection.close();
			}
		}

		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	
}
