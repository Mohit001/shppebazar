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
import javax.ws.rs.DELETE;
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
import model.Product;
import model.ProductImage;
import model.Wishlist;


@Path("/wishlist")
@Produces(MediaType.APPLICATION_JSON)
public class WishlistService {

	private ObjectMapper mapper = new ObjectMapper();
	
	@GET
	@Path("/get/{user_id}")
	public Response getWishlist(@PathParam("user_id") String user_id) throws SQLException, JsonProcessingException {
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<List<Product>> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		List<Product> wishList = new ArrayList<>();
		
		Connection connection = null;
		
		try {
			if(user_id.isEmpty() || user_id.equalsIgnoreCase("0")) {
				apiResponseStatus = ApiResponseStatus.WISHLIST_GET_FAIL_EMPTY;
			}  else {
				DatabaseConnector connector = new DatabaseConnector();
				connection = connector.getConnection();
				connection.setAutoCommit(false);
				wishList.clear();
				
				String getWishlsitQuery = 
						"SELECT * FROM "
						+Database.WISHLISTMASTER.TABLE_NAME
						+" WHERE "
						+Database.WISHLISTMASTER.USER_ID+"=?";
				PreparedStatement wishlistStatement = connection.prepareStatement(getWishlsitQuery);
				wishlistStatement.setString(1, user_id);
				ResultSet resultSet = wishlistStatement.executeQuery();
				resultSet.last();
				if(resultSet.getRow() > 0) {
					resultSet.first();
					do {
						int wishlist_id = resultSet.getInt(Database.WISHLISTMASTER.WISHLIST_ID);
						int product_id = resultSet.getInt(Database.WISHLISTMASTER.PRODUCT_ID);
						
						String getProduct_information = 
								"SELECT * FROM "
								+Database.ProductMaster.TABLE_NAME
								+" WHERE "
								+Database.ProductMaster.PRO_MST_ID+"=?";
						PreparedStatement productStatement = connection.prepareStatement(getProduct_information);
						productStatement.setInt(1, product_id);
						ResultSet productResultSet = productStatement.executeQuery();
						
						productResultSet.last();
						Product product = null;
						if(productResultSet.getRow() == 1) {
							product = new Product();
							product.setPro_mst_id(productResultSet.getInt(Database.ProductMaster.PRO_MST_ID));
							product.setPro_name(UtilsString.getStirng(productResultSet.getString(Database.ProductMaster.PRO_NAME)));
							product.setPro_code(UtilsString.getStirng(productResultSet.getString(Database.ProductMaster.PRO_CODE)));
							product.setPro_description(UtilsString.getStirng(productResultSet.getString(Database.ProductMaster.PRO_DESCRIPTION)));
							product.setPro_price(UtilsString.getStirng(productResultSet.getString(Database.ProductMaster.PRO_PRICE)));
							product.setIs_enable(productResultSet.getInt(Database.ProductMaster.IS_ENABLE));
							product.setCat_id(productResultSet.getInt(Database.ProductMaster.CAT_ID));
							product.setBrand_id(productResultSet.getInt(Database.ProductMaster.BRAND_ID));
							product.setUser_id(productResultSet.getInt(Database.ProductMaster.USER_ID));
							product.setGst_type(UtilsString.getStirng(productResultSet.getString(Database.ProductMaster.GST_TYPE)));
							product.setGst(productResultSet.getInt(Database.ProductMaster.GST));
							product.setDiscount_price(productResultSet.getInt(Database.ProductMaster.DISCOUNT_PRICE));
							product.setPro_image(UtilsString.getStirng(productResultSet.getString(Database.ProductMaster.PRO_IMAGE)));

							
							product.setWishlist_id(resultSet.getInt(Database.WISHLISTMASTER.WISHLIST_ID));

							
						}
						if(product != null) {
							wishList.add(product);	
						}
						
						resultSet.next();
					}while(!resultSet.isAfterLast());
					
					
					
				}
				
				if(wishList.size() == 0) {
					apiResponseStatus = ApiResponseStatus.WISHLIST_GET_FAIL_EMPTY;
				} else {
					apiResponseStatus = ApiResponseStatus.WISHLIST_GET_SUCCESS;
				}
				
			}
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			if(connection != null) {
				connection.rollback();
			}
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
			response.setInfo(wishList);
			responseJson = mapper.writeValueAsString(response);
			
			if(connection != null && !connection.isClosed()) {
				connection.commit();
				connection.close();
			}
		}

		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	
	@GET
	@Path("/remove/{wishlist_id}")
	public Response removeFormWishlist(@PathParam("wishlist_id") String wishlist_id) throws SQLException, JsonProcessingException {
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<Integer> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		
		Connection connection = null;
		
		try {
			if(wishlist_id.isEmpty() || wishlist_id.equalsIgnoreCase("0")) {
				apiResponseStatus = ApiResponseStatus.WISHLIST_REMOVE_FAIL;
			}  else {
				
				DatabaseConnector connector = new DatabaseConnector();
				connection = connector.getConnection();
				connection.setAutoCommit(false);
				String query  = "DELETE FROM "
						+Database.WISHLISTMASTER.TABLE_NAME
						+" WHERE "
						+Database.WISHLISTMASTER.WISHLIST_ID+"=?";
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setString(1, wishlist_id);
				int affectedRow = statement.executeUpdate();
				
				if(affectedRow == 0 || affectedRow > 1) {
					connection.rollback();
					apiResponseStatus = ApiResponseStatus.WISHLIST_REMOVE_FAIL;
				} else {
					apiResponseStatus = ApiResponseStatus.WISHLIST_REMOVE_SUCCESS;
				}
			}
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			if(connection != null) {
				connection.rollback();				
			}

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
			if(apiResponseStatus == ApiResponseStatus.WISHLIST_REMOVE_FAIL)
				response.setInfo(Integer.parseInt(wishlist_id));
			else
				response.setInfo(0);
			responseJson = mapper.writeValueAsString(response);
			
			if(connection != null && !connection.isClosed()) {
				connection.commit();
				connection.close();
			}
		}

		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	
	@GET
	@Path("/add/{user_id}/{product_id}")
	public Response addToWishlist(@PathParam("user_id") String user_id, @PathParam("product_id") String product_id) throws SQLException, JsonProcessingException {
		String responseJson = "";
		int wishlist_id = 0;
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<Integer> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		
		Connection connection = null;
		
		try {
				
				DatabaseConnector connector = new DatabaseConnector();
				connection = connector.getConnection();
				connection.setAutoCommit(false);
				
				String query = "INSERT INTO "
						+Database.WISHLISTMASTER.TABLE_NAME
						+" ("
						+Database.WISHLISTMASTER.USER_ID
						+", "+Database.WISHLISTMASTER.PRODUCT_ID
						+")"
						+ " VALUES "
						+"(?, ?)";
				
				PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, Integer.parseInt(user_id));
				statement.setInt(2, Integer.parseInt(product_id));
				
				int affectedRaw = statement.executeUpdate();
				if(affectedRaw == 0) {
					apiResponseStatus = ApiResponseStatus.WISHLIST_ADD_FAIL;
					connection.rollback();
				} else {
					ResultSet resultSet = statement.getGeneratedKeys();
					resultSet.first();
					wishlist_id = resultSet.getInt(1);
					
					resultSet.close();
					
					apiResponseStatus = ApiResponseStatus.WISHLIST_ADD_SUCCESS; 
				}
				
				statement.close();
			
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
			response.setInfo(wishlist_id);
			responseJson = mapper.writeValueAsString(response);
			
			if(connection != null && !connection.isClosed()) {
				connection.commit();
				connection.close();
			}
		}

		return Response.status(Status.OK).entity(responseJson).build();
	}


}
