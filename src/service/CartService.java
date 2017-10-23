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
import java.util.Date;
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

import org.apache.jasper.tagplugins.jstl.core.ForEach;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Enums.ApiResponseStatus;
import Enums.CartStatusEnum;
import Utils.UtilsString;
import basemodel.BaseResponse;
import database.Database;

import database.DatabaseConnector;
import model.Address;
import model.InvoiceMaster;
import model.PaymentCCTypes;
import model.PaymentMethod;
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
				apiResponseStatus = createAndInsertItemIntoCart(connection, userCart, product, apiResponseStatus);
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
							apiResponseStatus = insertItemIntoCart(connection, userCart, product, apiResponseStatus);
						} else {
							userCart.setCart_id(usercartid);
							apiResponseStatus = updateCartItem(connection, userCart, product, i, apiResponseStatus);
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
	
	
	@Path("/updateCart")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCart(String jsonRequest) throws JsonParseException, JsonMappingException, IOException, SQLException {
		
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
			
			String cartUpdateQuery = "UPDATE "
					+Database.UserCartTable.TABLE_NAME
					+" SET "
					+Database.UserCartTable.USER_ID+"=?"
					+", "+Database.UserCartTable.SHIPPING_CHARGE+"=?"
					+" WHERE "
					+Database.UserCartProductTable.CART_ID+"=?";
			
			PreparedStatement cartUpdateStatement = connection.prepareStatement(cartUpdateQuery);
			cartUpdateStatement.setInt(1, userCart.getUser_id());
			cartUpdateStatement.setString(2, userCart.getShipping_charge());
			cartUpdateStatement.setInt(3, userCart.getCart_id());
			
			
			int cartUpdateAffectedRow = cartUpdateStatement.executeUpdate();
			
			List<UserCartProduct> list = userCart.getUserCartProduct();
			
			if(list != null && list.size() > 0) {
			
				for(int i=0; i<list.size(); i++) {
					UserCartProduct product = list.get(i);
					
					double subtotal = (Double.parseDouble(product.getProduct_price()) * product.getProduct_qty())
							+ (product.getShipping_charge() * product.getProduct_qty());
					
					String productUpdateQuery = "UPDATE "
							+Database.UserCartProductTable.TABLE_NAME
							+" SET "
							+Database.UserCartProductTable.PRODUCT_QTY+"=?"
							+", "+Database.UserCartProductTable.SUBTOTAL+"=?"
							+" WHERE "
							+Database.UserCartProductTable.CART_ID+"=?"
							+" AND "
							+Database.UserCartProductTable.PRODUCT_ID+"=?";
					
					PreparedStatement cartProductStatement = connection.prepareStatement(productUpdateQuery);
					cartProductStatement.setInt(1, product.getProduct_qty());
					cartProductStatement.setString(2, String.valueOf(subtotal));
					cartProductStatement.setInt(3, userCart.getCart_id());
					cartProductStatement.setInt(4, product.getProduct_id());
					
					int cartProductAffectedRow = cartProductStatement.executeUpdate();
					if(cartProductAffectedRow == 0) {
						apiResponseStatus = ApiResponseStatus.CART_PRODUCT_UPDATE_FAIL;
						connection.rollback();
						break;
					} else {
						userCart.getUserCartProduct().set(i, product);
					}
					
				}
				
			}
			
			
			apiResponseStatus = ApiResponseStatus.CART_UPDATE_SUCCESS;
			
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
			apiResponseStatus = ApiResponseStatus.CART_LIST_SUCCESS;
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
				userCart.setCart_status(UtilsString.getStirng(resultSet.getString(Database.UserCartTable.CART_STATUS)));
				userCart.setToken(UtilsString.getStirng(resultSet.getString(Database.UserCartTable.CART_TOKEN)));
				userCart.setShipping_address_id(resultSet.getInt(Database.UserCartTable.CART_SHIPPING_ID));
				userCart.setBilling_address_id(resultSet.getInt(Database.UserCartTable.CART_BILLING_ID));
				userCart.setPayment_type_id(resultSet.getInt(Database.UserCartTable.CART_PAYMENT_TYPE_ID));
				userCart.setSalt(UtilsString.getStirng(resultSet.getString(Database.UserCartTable.SALT)));
				userCart.setShipping_charge(UtilsString.getStirng(resultSet.getString(Database.UserCartTable.SHIPPING_CHARGE)));
				userCart.setCreate_date("");
				userCart.setIp_address("");
				userCart.setShippingAddress(new Address());
				userCart.setBillingAddress(new Address());
				userCart.setPayment_type_code("COD");
				List<UserCartProduct> list = getUserCartProducts(connection, userCart.getCart_id());
				userCart.setUserCartProduct(list);
				userCart.setCartCount(list.size());
			}
		}
		return userCart;
	}
	
	private List<UserCartProduct> getUserCartProducts(Connection connection, int cart_id) throws SQLException{
		
		List<UserCartProduct> list = new ArrayList<>();
		
		String getUserCartProductQuery = "SELECT * FROM "
				+Database.UserCartProductTable.TABLE_NAME
				+" WHERE "
				+Database.UserCartProductTable.CART_ID+"=?";
		
		PreparedStatement cartProductStatement = connection.prepareStatement(getUserCartProductQuery);
		cartProductStatement.setInt(1, cart_id);
		ResultSet userCartProductResultSet = cartProductStatement.executeQuery();
		userCartProductResultSet.last();
		if(userCartProductResultSet.getRow() != 0) {
			userCartProductResultSet.first();
			do {
				UserCartProduct product = new UserCartProduct();
				product.setCart_id(userCartProductResultSet.getInt(Database.UserCartProductTable.CART_ID));
				product.setProduct_id(userCartProductResultSet.getInt(Database.UserCartProductTable.PRODUCT_ID));
				product.setProduct_name(userCartProductResultSet.getString(Database.UserCartProductTable.PRODUCT_NAME));
				product.setProduct_qty(userCartProductResultSet.getInt(Database.UserCartProductTable.PRODUCT_QTY));
				product.setProduct_price(userCartProductResultSet.getString(Database.UserCartProductTable.PRODUCT_PRICE));
				product.setProduct_code(userCartProductResultSet.getString(Database.UserCartProductTable.PRODUCT_CODE));
				product.setShipping_charge(userCartProductResultSet.getInt(Database.UserCartProductTable.SHIPPING_CHARGE));
				product.setStatus(userCartProductResultSet.getString(Database.UserCartProductTable.STATUS));
				product.setGst_type(userCartProductResultSet.getString(Database.UserCartProductTable.GST_TYPE));
				product.setGst(userCartProductResultSet.getDouble(Database.UserCartProductTable.GST));
				product.setSubtotal(userCartProductResultSet.getString(Database.UserCartProductTable.SUBTOTAL));
				product.setDescription(userCartProductResultSet.getString(Database.UserCartProductTable.DESCRIPTION));
				product.setCat_id(userCartProductResultSet.getInt(Database.UserCartProductTable.CART_ID));
				product.setCategory_name(userCartProductResultSet.getString(Database.UserCartProductTable.CATEGORY_NAME));
				product.setBrand_id(userCartProductResultSet.getInt(Database.UserCartProductTable.BRAND_ID));
				product.setBrand_name(userCartProductResultSet.getString(Database.UserCartProductTable.BRAND_NAME));
				product.setImage_name(UtilsString.getStirng(userCartProductResultSet.getString(Database.UserCartProductTable.IMAGE_NAME)));
				product.setDiscount_price(userCartProductResultSet.getString(Database.UserCartProductTable.DISCOUNT_PRICE));
				list.add(product);
				userCartProductResultSet.next();
			}while(!userCartProductResultSet.isAfterLast());
		}
		
		return list;
		
	}
	private ApiResponseStatus createAndInsertItemIntoCart(Connection connection, UserCart userCart,UserCartProduct userCartProduct, ApiResponseStatus apiResponseStatus) throws ClassNotFoundException, SQLException {
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
			userCart.setCart_id(lastInsertedID);
			
			String productInsertQuery = "INSERT INTO "
					+Database.UserCartProductTable.TABLE_NAME
					+"("
					+Database.UserCartProductTable.CART_ID
					+", "+Database.UserCartProductTable.PRODUCT_ID
					+", "+Database.UserCartProductTable.PRODUCT_NAME
					+", "+Database.UserCartProductTable.DESCRIPTION
					+", "+Database.UserCartProductTable.PRODUCT_QTY
					+", "+Database.UserCartProductTable.PRODUCT_PRICE
					+", "+Database.UserCartProductTable.PRODUCT_CODE
					+", "+Database.UserCartProductTable.SHIPPING_CHARGE
					+", "+Database.UserCartProductTable.STATUS
					+", "+Database.UserCartProductTable.GST_TYPE
					+", "+Database.UserCartProductTable.GST
					+", "+Database.UserCartProductTable.SUBTOTAL
					+", "+Database.UserCartProductTable.CATEGORY_ID
					+", "+Database.UserCartProductTable.CATEGORY_NAME
					+", "+Database.UserCartProductTable.BRAND_ID
					+", "+Database.UserCartProductTable.BRAND_NAME
					+", "+Database.UserCartProductTable.DISCOUNT_PRICE
					+", "+Database.UserCartProductTable.IMAGE_NAME
					+")"
					+" VALUES "
					+"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			double subtotal = (Double.parseDouble(userCartProduct.getProduct_price()) * userCartProduct.getProduct_qty())
					+ (userCartProduct.getShipping_charge() * userCartProduct.getProduct_qty());
			
			
			PreparedStatement cartProductStatement = connection.prepareStatement(productInsertQuery);
			cartProductStatement.setInt(1, userCart.getCart_id());
			cartProductStatement.setInt(2, userCartProduct.getProduct_id());
			cartProductStatement.setString(3, userCartProduct.getProduct_name());
			cartProductStatement.setString(4, userCartProduct.getDescription());
			cartProductStatement.setInt(5, userCartProduct.getProduct_qty());
			cartProductStatement.setString(6, userCartProduct.getProduct_price());
			cartProductStatement.setString(7, userCartProduct.getProduct_code());
			cartProductStatement.setInt(8, userCartProduct.getShipping_charge());
			cartProductStatement.setString(9, CartStatusEnum.OPEN.getStatus());
			cartProductStatement.setString(10, userCartProduct.getGst_type());
			cartProductStatement.setDouble(11, userCartProduct.getGst());
			cartProductStatement.setString(12, String.valueOf(subtotal));
			cartProductStatement.setInt(13, userCartProduct.getCat_id());
			cartProductStatement.setString(14, userCartProduct.getCategory_name());
			cartProductStatement.setInt(15, userCartProduct.getBrand_id());
			cartProductStatement.setString(16, userCartProduct.getBrand_name());
			cartProductStatement.setString(17, userCartProduct.getDiscount_price());
			cartProductStatement.setString(18, userCartProduct.getImage_name());
			
			
			
			int cartProductAffectedRow = cartProductStatement.executeUpdate();
			if(cartProductAffectedRow == 0) {
				apiResponseStatus = ApiResponseStatus.CART_PRODUCT_ADD_FAIL;
				connection.rollback();
			} else {
				
				userCart.setCart_id(lastInsertedID);
				userCart.setCartCount(getCartItemCount(connection, lastInsertedID));
				
				apiResponseStatus = ApiResponseStatus.CART_PRODUCT_ADD_SUCCESS;
				
			}
		}
		
		return apiResponseStatus;
	}
	
	
	private ApiResponseStatus insertItemIntoCart(Connection connection, UserCart userCart,UserCartProduct userCartProduct, ApiResponseStatus apiResponseStatus) throws ClassNotFoundException, SQLException {
		// generate salt and than create new token
		
			
		String productInsertQuery = "INSERT INTO "
				+Database.UserCartProductTable.TABLE_NAME
				+"("
				+Database.UserCartProductTable.CART_ID
				+", "+Database.UserCartProductTable.PRODUCT_ID
				+", "+Database.UserCartProductTable.PRODUCT_NAME
				+", "+Database.UserCartProductTable.DESCRIPTION
				+", "+Database.UserCartProductTable.PRODUCT_QTY
				+", "+Database.UserCartProductTable.PRODUCT_PRICE
				+", "+Database.UserCartProductTable.PRODUCT_CODE
				+", "+Database.UserCartProductTable.SHIPPING_CHARGE
				+", "+Database.UserCartProductTable.STATUS
				+", "+Database.UserCartProductTable.GST_TYPE
				+", "+Database.UserCartProductTable.GST
				+", "+Database.UserCartProductTable.SUBTOTAL
				+", "+Database.UserCartProductTable.CATEGORY_ID
				+", "+Database.UserCartProductTable.CATEGORY_NAME
				+", "+Database.UserCartProductTable.BRAND_ID
				+", "+Database.UserCartProductTable.BRAND_NAME
				+", "+Database.UserCartProductTable.DISCOUNT_PRICE
				+", "+Database.UserCartProductTable.IMAGE_NAME
				+")"
				+" VALUES "
				+"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		double subtotal = (Double.parseDouble(userCartProduct.getProduct_price()) * userCartProduct.getProduct_qty())
				+ (userCartProduct.getShipping_charge() * userCartProduct.getProduct_qty());
		
		
		PreparedStatement cartProductStatement = connection.prepareStatement(productInsertQuery);
		cartProductStatement.setInt(1, userCart.getCart_id());
		cartProductStatement.setInt(2, userCartProduct.getProduct_id());
		cartProductStatement.setString(3, userCartProduct.getProduct_name());
		cartProductStatement.setString(4, userCartProduct.getDescription());
		cartProductStatement.setInt(5, userCartProduct.getProduct_qty());
		cartProductStatement.setString(6, userCartProduct.getProduct_price());
		cartProductStatement.setString(7, userCartProduct.getProduct_code());
		cartProductStatement.setInt(8, userCartProduct.getShipping_charge());
		cartProductStatement.setString(9, CartStatusEnum.OPEN.getStatus());
		cartProductStatement.setString(10, userCartProduct.getGst_type());
		cartProductStatement.setDouble(11, userCartProduct.getGst());
		cartProductStatement.setString(12, String.valueOf(subtotal));
		cartProductStatement.setInt(13, userCartProduct.getCat_id());
		cartProductStatement.setString(14, userCartProduct.getCategory_name());
		cartProductStatement.setInt(15, userCartProduct.getBrand_id());
		cartProductStatement.setString(16, userCartProduct.getBrand_name());
		cartProductStatement.setString(17, userCartProduct.getDiscount_price());
		cartProductStatement.setString(18, userCartProduct.getImage_name());
		

			int cartProductAffectedRow = cartProductStatement.executeUpdate();
			if(cartProductAffectedRow == 0) {
				apiResponseStatus = ApiResponseStatus.CART_PRODUCT_ADD_FAIL;
				connection.rollback();
			} else {
				apiResponseStatus = ApiResponseStatus.CART_PRODUCT_ADD_SUCCESS;
				userCart.setCartCount(getCartItemCount(connection, userCart.getCart_id()));
				
			}
		
		return apiResponseStatus;
	}
	
	
	private ApiResponseStatus updateCartItem(Connection connection, UserCart userCart, UserCartProduct cartProduct, int index, ApiResponseStatus apiResponseStatus) throws ClassNotFoundException, SQLException {
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
				+Database.UserCartProductTable.PRODUCT_PRICE
				+", "+Database.UserCartProductTable.PRODUCT_QTY
				+", "+Database.UserCartProductTable.SHIPPING_CHARGE
				+" FROM "
				+Database.UserCartProductTable.TABLE_NAME
				+" WHERE "
				+Database.UserCartProductTable.CART_ID+"=?"
				+" AND "
				+Database.UserCartProductTable.PRODUCT_ID+"=?";
		

		PreparedStatement statement = connection.prepareStatement(insertQuery);
		statement.setInt(1, userCart.getCart_id());
		statement.setInt(2, cartProduct.getProduct_id());
		ResultSet resultSet = statement.executeQuery();
		resultSet.first();
		
		double productPrice = Double.parseDouble(resultSet.getString(Database.UserCartProductTable.PRODUCT_PRICE));
		int shippingCharge = resultSet.getInt(Database.UserCartProductTable.SHIPPING_CHARGE);
		int oldQTY = resultSet.getInt(Database.UserCartProductTable.PRODUCT_QTY);
		int newQTY = oldQTY + cartProduct.getProduct_qty();
		if(newQTY > 10) {
			apiResponseStatus = ApiResponseStatus.CART_QUENTITY_EXCEED;
		} else {
			
			double subtotal = (productPrice * newQTY)+ (shippingCharge * newQTY);
			
			String productUpdateQuery = "UPDATE "
					+Database.UserCartProductTable.TABLE_NAME
					+" SET "
					+Database.UserCartProductTable.PRODUCT_QTY+"=?"
					+", "+Database.UserCartProductTable.SUBTOTAL+"=?"
					+" WHERE "
					+Database.UserCartProductTable.CART_ID+"=?"
					+" AND "
					+Database.UserCartProductTable.PRODUCT_ID+"=?";
			
			PreparedStatement cartProductStatement = connection.prepareStatement(productUpdateQuery);
			cartProductStatement.setInt(1, newQTY);
			cartProductStatement.setString(2, String.valueOf(subtotal));
			cartProductStatement.setInt(3, userCart.getCart_id());
			cartProductStatement.setInt(4, cartProduct.getProduct_id());
			
			int cartProductAffectedRow = cartProductStatement.executeUpdate();
			if(cartProductAffectedRow == 0) {
				apiResponseStatus = ApiResponseStatus.CART_PRODUCT_UPDATE_FAIL;
				connection.rollback();
			} else {
				userCart.setToken(token);
				userCart.setSalt(salt);
				userCart.getUserCartProduct().get(0).setProduct_qty(newQTY);
				apiResponseStatus = ApiResponseStatus.CART_PRODUCT_UPDATE_SUCCESS;
				userCart.setCartCount(getCartItemCount(connection, userCart.getCart_id()));
				cartProduct.setSubtotal(String.valueOf(subtotal));
				userCart.getUserCartProduct().set(index, cartProduct);
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
		BaseResponse<List<Address>> response = new BaseResponse<>();
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
			List<Address> list = new ArrayList<>();
			response.setInfo(list);
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
	@Path("/getPaymentMethod")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPaymentType() throws SQLException, JsonProcessingException {
		
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<List<PaymentMethod>> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		List<PaymentMethod> paymentMethodList = new ArrayList<>();
		
		
		Connection connection = null;
		try {
			DatabaseConnector connector = new DatabaseConnector();
			connection = connector.getConnection();
			connection.setAutoCommit(false);
			
			PaymentMethod paymentMethod = new PaymentMethod();
			paymentMethod.setId(1);
			paymentMethod.setTitle("Cash On Delivery");
			paymentMethod.setCode("COD");
			paymentMethod.setSelected(true);
			ArrayList<PaymentCCTypes> ccTypeList = new ArrayList<>();
			paymentMethod.setCc_types(ccTypeList);
			
			paymentMethodList.add(paymentMethod);
			
			apiResponseStatus = ApiResponseStatus.CART_PAYMENT_TYPE_LIST_SUCCESS;
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
			response.setInfo(paymentMethodList);
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
		BaseResponse<List<PaymentMethod>> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		
		Connection connection = null;
		try {
			DatabaseConnector connector = new DatabaseConnector();
			connection = connector.getConnection();
			connection.setAutoCommit(false);
			
			String deleteQuery = "UPDATE "
					+Database.UserCartTable.TABLE_NAME
					+" SET "
					+Database.UserCartTable.CART_PAYMENT_TYPE_ID+"=?"
					+", "+Database.UserCartTable.CART_PAYMENT_TYPE_CODE+"=?"
					+" WHERE "
					+Database.UserCartTable.CART_ID+"=?";
			
			PreparedStatement statement = connection.prepareStatement(deleteQuery);
			statement.setInt(1, payment_type_id);
			if(payment_type_id == 1) {
				statement.setString(2, "COD");
			} else {
				statement.setString(2, "ONLINE");
			}
			statement.setInt(3, cart_id);
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
			List<PaymentMethod> list = new ArrayList<>();
			response.setInfo(list);
			responseJson = mapper.writeValueAsString(response);
			
			if(connection != null && !connection.isClosed()) {
				connection.commit();
				connection.close();
			}
		}
		
		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	private int getCartItemCount(Connection connection, int cart_id) throws SQLException {
		String productQuery = "SELECT COUNT(*) AS count FROM "
				+Database.UserCartProductTable.TABLE_NAME
				+" WHERE "
				+Database.UserCartProductTable.CART_ID+"=?";
		PreparedStatement productStatement = connection.prepareStatement(productQuery);
		productStatement.setInt(1, cart_id);
		ResultSet productResultset = productStatement.executeQuery();
		productResultset.last();
		int productCount = productResultset.getInt("count");
		
		return productCount;
	}
	
	@GET
	@Path("/orderReview/{cart_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response orderReview(@PathParam("cart_id") int cart_id) throws SQLException, JsonProcessingException {
	
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<UserCart> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		userCart = new UserCart();
		Connection connection = null;
		try {
			DatabaseConnector connector = new DatabaseConnector();
			connection = connector.getConnection();
			connection.setAutoCommit(false);
			
			String cartSelectQuery = "SELECT * FROM "
					+Database.UserCartTable.TABLE_NAME
					+" WHERE "
					+Database.UserCartTable.CART_ID+"=?";
			
			PreparedStatement statement = connection.prepareStatement(cartSelectQuery);
			statement.setInt(1, cart_id);
			ResultSet resultSet = statement.executeQuery();
			resultSet.last();
			if(resultSet.getRow() == 1) {
				// set details to userCart
				
				userCart.setCart_id(resultSet.getInt(Database.UserCartTable.CART_ID));
				userCart.setUser_id(resultSet.getInt(Database.UserCartTable.USER_ID));
				userCart.setShipping_address_id(resultSet.getInt(Database.UserCartTable.CART_SHIPPING_ID));
				userCart.setShippingAddress(getSelectedAddress(connection, userCart.getShipping_address_id()));
				userCart.setBilling_address_id(resultSet.getInt(Database.UserCartTable.CART_BILLING_ID));
				userCart.setBillingAddress(getSelectedAddress(connection, userCart.getBilling_address_id()));
				userCart.setPayment_type_id(resultSet.getInt(Database.UserCartTable.CART_PAYMENT_TYPE_ID));
				userCart.setPayment_type_code(UtilsString.getStirng(resultSet.getString(Database.UserCartTable.CART_PAYMENT_TYPE_CODE)));
				userCart.setUserCartProduct(getUserCartProducts(connection, userCart.getCart_id()));
				userCart.setCart_status(UtilsString.getStirng(resultSet.getString(Database.UserCartTable.CART_STATUS)));
				userCart.setToken(resultSet.getString(Database.UserCartTable.CART_TOKEN));
				userCart.setSalt(resultSet.getString(Database.UserCartTable.SALT));
				userCart.setCartCount(userCart.getUserCartProduct().size());
				apiResponseStatus = ApiResponseStatus.CART_GET_DETAILS_SUCCESS;
			} else {
				apiResponseStatus = ApiResponseStatus.CART_GET_DETAILS_FAIL;
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
	
	private Address getSelectedAddress(Connection connection, int address_id) throws SQLException {
		Address address = new Address();
		
		String getAddressQuery = "SELECT * FROM "
				+Database.UserAddress.TABLE_NAME
				+" WHERE "
				+Database.UserAddress.ADDRESS_ID+"=?";
		
		PreparedStatement statement = connection.prepareStatement(getAddressQuery);
		statement.setInt(1, address_id);
		ResultSet resultSet = statement.executeQuery();
		resultSet.last();
		if(resultSet.getRow() == 1) {
			address.setAddress_id(resultSet.getInt(Database.UserAddress.ADDRESS_ID));
			address.setAddress1(resultSet.getString(Database.UserAddress.ADDRESS1));
			address.setAddress2(resultSet.getString(Database.UserAddress.ADDRESS2));
			address.setState(resultSet.getString(Database.UserAddress.STATE));
			address.setCity(resultSet.getString(Database.UserAddress.CITY));
			address.setPostcode(resultSet.getString(Database.UserAddress.POSTCODE));
			address.setAddition_detail(resultSet.getString(Database.UserAddress.ADDITIONAL_DETIALS));
			address.setUser_id(resultSet.getString(Database.UserAddress.USER_ID));
			address.setFull_name(resultSet.getString(Database.UserAddress.FULL_NAME));
			address.setEmail(resultSet.getString(Database.UserAddress.EMAIL));
			address.setContact_number(resultSet.getString(Database.UserAddress.CONTACT_NUMBER));	
			
			return address;
		} else {
			return null;
		}
		
	}
	
	
	@POST
	@Path("/placeOrder")
	@Produces(MediaType.APPLICATION_JSON)
	public Response placeOrder(String jsonRequest) throws SQLException, JsonProcessingException {
	
		
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<String> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		InvoiceMaster invoiceMaster = new InvoiceMaster();
		Connection connection = null;
		try {
			DatabaseConnector connector = new DatabaseConnector();
			connection = connector.getConnection();
			connection.setAutoCommit(false);
			
			userCart = mapper.readValue(jsonRequest, UserCart.class);
			
			String insertInvoiceMasterQuery = 
					"INSERT INTO "
					+Database.InvoiceMaster.TABLE_NAME
					+"("
					+Database.InvoiceMaster.USER_ID
					+", "+Database.InvoiceMaster.ORDER_TYPE
					+", "+Database.InvoiceMaster.USER_TYPE
					+", "+Database.InvoiceMaster.SHIPPING_ADDRESS1
					+", "+Database.InvoiceMaster.SHIPPING_ADDRESS2
					+", "+Database.InvoiceMaster.SHIPPING_STATE
					+", "+Database.InvoiceMaster.SHIPPING_CITY
					+", "+Database.InvoiceMaster.SHIPPING_POSTCODE
					+", "+Database.InvoiceMaster.SHIPPING_ADDITIONAL_DETAILS
					+", "+Database.InvoiceMaster.BILLING_ADDRESS1
					+", "+Database.InvoiceMaster.BILLING_ADDRESS2
					+", "+Database.InvoiceMaster.BILLING_STATE
					+", "+Database.InvoiceMaster.BILLING_CITY
					+", "+Database.InvoiceMaster.BILLING_POSTCODE
					+", "+Database.InvoiceMaster.BILLING_ADDITIONAL_DETAILS
					+", "+Database.InvoiceMaster.SHIPPING_FULL_NAME
					+", "+Database.InvoiceMaster.SHIPPING_EMAIL
					+", "+Database.InvoiceMaster.BILLING_FULL_NAME
					+", "+Database.InvoiceMaster.BILLING_EMAIL
					+", "+Database.InvoiceMaster.TOTAL_AMOUNT
					+", "+Database.InvoiceMaster.GRAND_TOTAL
					+", "+Database.InvoiceMaster.SHIPPING_CHARGE
					+", "+Database.InvoiceMaster.ORDER_STATUS
					+", "+Database.InvoiceMaster.CART_ID
					+", "+Database.InvoiceMaster.TOKEN
					+", "+Database.InvoiceMaster.SALT
					+", "+Database.InvoiceMaster.CREATE_DATE
					+")"
					+" VALUES"
					+"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement preparedStatement = connection.prepareStatement(insertInvoiceMasterQuery, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, userCart.getUser_id());
			preparedStatement.setString(2, userCart.getPayment_type_code());
			preparedStatement.setString(3, "RegisterUser");
			
			Address shippingAddress = userCart.getShippingAddress();
			preparedStatement.setString(4, shippingAddress.getAddress1());
			preparedStatement.setString(5, shippingAddress.getAddress2());
			preparedStatement.setString(6, shippingAddress.getState());
			preparedStatement.setString(7, shippingAddress.getCity());
			preparedStatement.setString(8, shippingAddress.getPostcode());
			preparedStatement.setString(9, UtilsString.getStirng(shippingAddress.getAddition_detail()));
			
			Address billingAddress = userCart.getBillingAddress();
			preparedStatement.setString(10, billingAddress.getAddress1());
			preparedStatement.setString(11, billingAddress.getAddress2());
			preparedStatement.setString(12, billingAddress.getState());
			preparedStatement.setString(13, billingAddress.getCity());
			preparedStatement.setString(14, billingAddress.getPostcode());
			preparedStatement.setString(15, UtilsString.getStirng(billingAddress.getAddition_detail()));
			preparedStatement.setString(16, shippingAddress.getFull_name());
			preparedStatement.setString(17, shippingAddress.getEmail());
			preparedStatement.setString(18, billingAddress.getFull_name());
			preparedStatement.setString(19, billingAddress.getEmail());
			
			preparedStatement.setString(20, String.valueOf(getTotalWithoutShippingCharge(userCart.getUserCartProduct())));
			preparedStatement.setString(21, String.valueOf(getGrandTotal(userCart.getUserCartProduct())));
			preparedStatement.setString(22, String.valueOf(getTotalShippingCharge(userCart.getUserCartProduct())));
			preparedStatement.setString(23, CartStatusEnum.PENDING.getStatus());
			preparedStatement.setInt(24, userCart.getCart_id());
			preparedStatement.setString(25, userCart.getToken());
			preparedStatement.setString(26, userCart.getSalt());
			
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String createDate = simpleDateFormat.format(date);
			
			preparedStatement.setString(27, createDate);
			
			int invoiceMasterAffectedRows = preparedStatement.executeUpdate();
			if(invoiceMasterAffectedRows == 0) {
				apiResponseStatus = ApiResponseStatus.ORDER_PLACE_FAIL;
			} else {
				
				// insert products into invoice details table
				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				resultSet.first();
				int invoiceid = resultSet.getInt(1);
				invoiceMaster.setInvoice_id(invoiceid);
				
				List<UserCartProduct> list = userCart.getUserCartProduct();
				UserCartProduct userCartProduct = null;
				for(int i=0; i<list.size(); i++) {
					userCartProduct = list.get(i);
					
					String insertInvoiceDetailsQuery = 
							"INSERT INTO "
							+Database.InvoiceDetails.TABLE_NAME
							+"("
							+Database.InvoiceDetails.INVOICE_ID
							+", "+Database.InvoiceDetails.PRODUCT_ID
							+", "+Database.InvoiceDetails.PRODUCT_NAME
							+", "+Database.InvoiceDetails.PRODUCT_CODE
							+", "+Database.InvoiceDetails.PRODUCT_DESCRIPTION
							+", "+Database.InvoiceDetails.PRODUCT_PRICE
							+", "+Database.InvoiceDetails.PRODUCT_CAT_ID
							+", "+Database.InvoiceDetails.PRODUCT_BRAND_ID
							+", "+Database.InvoiceDetails.PRODUCT_GST_TYPE
							+", "+Database.InvoiceDetails.PRODUCT_GST
							+", "+Database.InvoiceDetails.PRODUCT_DISCOUNT_PRICE
							+", "+Database.InvoiceDetails.PRODUCT_IMAGE_NAME
							+", "+Database.InvoiceDetails.PRODUCT_CATEGORY_NAME
							+", "+Database.InvoiceDetails.PRODUCT_BRAND_NAME
							+", "+Database.InvoiceDetails.PRODUCT_QTY
							+")"
							+" VALUES "
							+"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
					
					PreparedStatement invoiceDetailsPreparedStatement = connection.prepareStatement(insertInvoiceDetailsQuery);
					invoiceDetailsPreparedStatement.setInt(1, invoiceid);
					invoiceDetailsPreparedStatement.setInt(2, userCartProduct.getProduct_id());
					invoiceDetailsPreparedStatement.setString(3, userCartProduct.getProduct_name());
					invoiceDetailsPreparedStatement.setString(4, userCartProduct.getProduct_code());
					invoiceDetailsPreparedStatement.setString(5, userCartProduct.getDescription());
					invoiceDetailsPreparedStatement.setString(6, userCartProduct.getProduct_price());
					invoiceDetailsPreparedStatement.setString(7, String.valueOf(userCartProduct.getCat_id()));
					invoiceDetailsPreparedStatement.setString(8, String.valueOf(userCartProduct.getBrand_id()));
					invoiceDetailsPreparedStatement.setString(9, userCartProduct.getGst_type());
					invoiceDetailsPreparedStatement.setString(10, String.valueOf(userCartProduct.getGst()));
					invoiceDetailsPreparedStatement.setString(11, userCartProduct.getDiscount_price());
					invoiceDetailsPreparedStatement.setString(12, userCartProduct.getImage_name());
					invoiceDetailsPreparedStatement.setString(13, userCartProduct.getCategory_name());
					invoiceDetailsPreparedStatement.setString(14, userCartProduct.getBrand_name());
					invoiceDetailsPreparedStatement.setInt(15, userCartProduct.getProduct_qty());
					
					int invoiceDetailsAffectedRows = invoiceDetailsPreparedStatement.executeUpdate();
					if(invoiceDetailsAffectedRows == 0) {
						apiResponseStatus = ApiResponseStatus.ORDER_PLACE_FAIL;
						break;
					} 
				}
				
				
				if(apiResponseStatus != ApiResponseStatus.ORDER_PLACE_FAIL)
				{
					// now update cart status to placed
					
					String updateCartStatus = "UPDATE "
							+Database.UserCartTable.TABLE_NAME
							+" SET "
							+Database.UserCartTable.CART_STATUS+"=?"
							+" WHERE "
							+Database.UserCartTable.CART_ID+"=?";
					
					PreparedStatement updateCartStatusPreparedStatement = connection.prepareStatement(updateCartStatus);
					updateCartStatusPreparedStatement.setString(1, CartStatusEnum.PLACED.getStatus());
					updateCartStatusPreparedStatement.setInt(2, userCart.getCart_id());
					
					int updatedRows = updateCartStatusPreparedStatement.executeUpdate();
					if(updatedRows == 0) {
						apiResponseStatus = ApiResponseStatus.ORDER_PLACE_FAIL;
					} else {
						apiResponseStatus = ApiResponseStatus.ORDER_PLACE_SUCCESS;
					}
					
				}
				
			}
			
			
			
		}catch (ClassNotFoundException e) {
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
			response.setInfo("");
			responseJson = mapper.writeValueAsString(response);
			
			if(connection != null && !connection.isClosed()) {
				connection.commit();
				connection.close();
			}
		}

		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	private double getTotalWithoutShippingCharge(List<UserCartProduct> list) {
		double total=0;
		for(int i=0; i<list.size(); i++) {
			UserCartProduct cartProduct = list.get(i);
			if(Double.parseDouble(cartProduct.getDiscount_price()) == 0) {
				total += (Double.parseDouble(cartProduct.getProduct_price()) 
							* cartProduct.getProduct_qty());
			} else {
				total += (Double.parseDouble(cartProduct.getDiscount_price())
							* cartProduct.getProduct_qty());
			}
		}
		
		return total;
	}
	
	private double getTotalShippingCharge(List<UserCartProduct> list) {
		double total=0;
		for(int i=0; i<list.size(); i++) {
			UserCartProduct cartProduct = list.get(i);
			total += (cartProduct.getShipping_charge() * cartProduct.getProduct_qty());
			
		}
		
		return total;
	}

	private double getGrandTotal(List<UserCartProduct> list) {
		double grandtotal=0;
		for(int i=0; i<list.size(); i++) {
			UserCartProduct cartProduct = list.get(i);
			double productTotal=0;
			if(Double.parseDouble(cartProduct.getDiscount_price()) == 0) {
				productTotal = (Double.parseDouble(cartProduct.getProduct_price()) * cartProduct.getProduct_qty());
			} else {
				productTotal = (Double.parseDouble(cartProduct.getDiscount_price()) * cartProduct.getProduct_qty());
			}
			
			double shippingtotal = (cartProduct.getShipping_charge() * cartProduct.getProduct_qty());
			
			grandtotal = grandtotal + productTotal + shippingtotal;
		}
		
		return grandtotal;
	}

	
	
}
