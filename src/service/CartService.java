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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Enums.ApiResponseStatus;
import Enums.CartStatusEnum;
import basemodel.BaseResponse;
import database.Database;

import database.DatabaseConnector;
import model.Address;
import model.Product;
import model.UserCart;
import model.UserCartProduct;

@Path("/cart")
@Produces(MediaType.APPLICATION_JSON)
public class CartService {
	
	private UserCart userCart;
	
	ObjectMapper mapper = new ObjectMapper();

	@Path("/addNewProduct")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response AddToCart(String jsonRequest) throws JsonParseException, JsonMappingException, IOException, SQLException {
		
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<UserCart> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		userCart = null;
		
		Connection connection = null;
		try {
			
			userCart = mapper.readValue(jsonRequest, UserCart.class);
			
			int usercartid = 0;
			if (userCart.getUser_id() != 0) {
//				cartList = productCartDao.getUSerCartByUserId(Integer.parseInt(userloginId));
				usercartid = getUserCartID(String.valueOf(userCart.getUser_id()), true, userCart.getToken());
			} else {
//				cartList = productCartDao.getUSerCartByIpAddress(ipAddress);
				usercartid = getUserCartID(userCart.getToken(), false, null);
			}
			
			
			if(usercartid == 0) {
				// create new cart and insert product
				String salt= generateSalt();
				String token = generateToken(userCart.getToken(), salt);	

				userCart.setToken(token);
				userCart.setSalt(salt);
				
				apiResponseStatus = insertItemIntoCart(userCart, connection, apiResponseStatus);
				
				
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
					String getProductQuery = "SELECT "
							+Database.UserCartProductTable.PRODUCT_QTY
							+" FROM "
							+Database.UserCartProductTable.TABLE_NAME
							+" WHERE "
							+Database.UserCartProductTable.CART_ID+"=?"
							+" AND "
							+Database.UserCartProductTable.PRODUCT_ID+"=?";
					PreparedStatement getProductStatement = connection.prepareStatement(getProductQuery);
					ResultSet resultSet = getProductStatement.executeQuery();
					
					resultSet.last();
					
					int rowCount = resultSet.getRow();
					
					if(rowCount == 0) {
						// execute insert
						apiResponseStatus = insertItemIntoCart(userCart, connection, apiResponseStatus);
					} else {
						apiResponseStatus = updateCartItem(userCart, connection, apiResponseStatus);
					}
				}
				
			}
			

			connection.commit();
			
			
			
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
				connection.close();
			}
		}

		
		
		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	
	private ApiResponseStatus insertItemIntoCart(UserCart userCart, Connection connection, ApiResponseStatus apiResponseStatus) throws ClassNotFoundException, SQLException {
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
				+", "+Database.UserCartTable.PAYMENT_TYPE_ID
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
			UserCartProduct userCartProduct = userCart.getUserCartProduct().get(0);
			ResultSet resultSet = statement.getResultSet();
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
					+", "+Database.UserCartTable.GST_TYPE
					+", "+Database.UserCartTable.GST
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
			cartProductStatement.setDouble(3, userCartProduct.getGst());
			
			int cartProductAffectedRow = cartProductStatement.executeUpdate();
			if(cartProductAffectedRow == 0) {
				apiResponseStatus = ApiResponseStatus.CART_PRODUCT_ADD_FAIL;
				connection.rollback();
			} else {
				apiResponseStatus = ApiResponseStatus.CART_PRODUCT_ADD_SUCCESS;
				userCart.setCart_id(lastInsertedID);
				connection.commit();
				
			}
		}
		
		return apiResponseStatus;
	}
	
	
	private ApiResponseStatus updateCartItem(UserCart userCart, Connection connection, ApiResponseStatus apiResponseStatus) throws ClassNotFoundException, SQLException {
		// generate salt and than create new token
		String salt, token;
		if(userCart.getToken() == null || userCart.getToken().isEmpty()) {
			salt= generateSalt();
			token = generateToken(userCart.getToken(), salt);	
		} else {
			salt = userCart.getSalt();
			token = userCart.getToken();
		}
		
		UserCartProduct cartProduct = userCart.getUserCartProduct().get(0);
		//now create new cart for user or guest user;
		String insertQuery = "SELECT "
				+Database.UserCartProductTable.PRODUCT_QTY
				+" FROM "
				+Database.UserCartProductTable.TABLE_NAME
				+" WHERE "
				+Database.UserCartProductTable.CART_ID+"=?"
				+" AND "
				+Database.UserCartProductTable.PRODUCT_ID+"=?";
		
		DatabaseConnector connector = new DatabaseConnector();
		connection = connector.getConnection();
		connection.setAutoCommit(false);
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
					+" WHERE"
					+Database.UserCartProductTable.CART_ID+"=?"
					+" AND "
					+Database.UserCartProductTable.PRODUCT_ID+"=?";
			
			PreparedStatement cartProductStatement = connection.prepareStatement(productUpdateQuery);
			int cartProductAffectedRow = cartProductStatement.executeUpdate();
			if(cartProductAffectedRow == 0) {
				apiResponseStatus = ApiResponseStatus.CART_PRODUCT_ADD_FAIL;
				connection.rollback();
			} else {
				userCart.setToken(token);
				userCart.setSalt(salt);
				apiResponseStatus = ApiResponseStatus.CART_PRODUCT_ADD_SUCCESS;
				connection.commit();
				
			}
		}
	
		
		return apiResponseStatus;
	}
	
	private int getUserCartID(String param, boolean isUserid, String token){
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
		
		Connection connection= null;
		DatabaseConnector connector = new DatabaseConnector();
		try {
			connection = connector.getConnection();
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
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		System.out.println(dateFormat.format(cal)); //20161116120843
		return salt_prefix+"_"+dateFormat.format(cal);
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
}
