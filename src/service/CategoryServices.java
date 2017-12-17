package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import model.Category;
import model.Product;
import model.ProductImage;

@Path("/Category")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoryServices {

	ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * 
	 * @return {@link Category}}
	 * @throws JsonProcessingException
	 */
	@GET
	@Path("/")
	public Response getAllCategories() throws JsonProcessingException {
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<List<Category>> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		List<Category> categoryList = new ArrayList<>();
		
		
		try {
			
			DatabaseConnector connector = new DatabaseConnector();
			Connection connection = connector.getConnection();
			
			String query = "SELECT "
					+Database.CategoryMaster.CAT_ID
					+", "+Database.CategoryMaster.CAT_NAME
					+", "+Database.CategoryMaster.CAT_DESCRIPTION
					+", "+Database.CategoryMaster.CAT_IMAGE
					+", "+Database.CategoryMaster.IS_ENABLE
					+", "+Database.CategoryMaster.PARENT_ID
					+" FROM "
					+Database.CategoryMaster.TABLE_NAME
					+" WHERE "
					+Database.CategoryMaster.PARENT_ID+"= 0"
					+" AND "
					+Database.CategoryMaster.IS_ENABLE+"= 1";
			
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			
			resultSet.last();
			if(resultSet.getRow() == 0) {
				apiResponseStatus = ApiResponseStatus.CATEGORY_NOT_FOUND;
			} else {
				
				resultSet.first();
				do {
					Category category = new Category();
					category.setCat_id(resultSet.getInt(Database.CategoryMaster.CAT_ID));
					category.setCat_name(UtilsString.getStirng(resultSet.getString(Database.CategoryMaster.CAT_NAME)));
					category.setCat_description(UtilsString.getStirng(resultSet.getString(Database.CategoryMaster.CAT_DESCRIPTION)));
					category.setCat_image(UtilsString.getStirng(resultSet.getString(Database.CategoryMaster.CAT_IMAGE)));
					category.setIs_enable(resultSet.getInt(Database.CategoryMaster.IS_ENABLE));
					category.setParent_id(resultSet.getInt(Database.CategoryMaster.PARENT_ID));
					category.setSubCategory(getSubCategoryList(category.getCat_id()));
					// add category to list
					categoryList.add(category);
					//increment resultset
					resultSet.next();
				}while(!resultSet.isAfterLast());
				
				
				
				apiResponseStatus = ApiResponseStatus.CATEGORY_LIST;
			}
			
			
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			categoryList = new ArrayList<>();
			apiResponseStatus = ApiResponseStatus.DATABASE_CONNECTINO_ERROR;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			categoryList = new ArrayList<>();
			apiResponseStatus = ApiResponseStatus.MYSQL_EXCEPTION;
		} finally {
			
			response.setStatus(apiResponseStatus.getStatus_code());
			response.setMessage(apiResponseStatus.getStatus_message());
			response.setInfo(categoryList);
			responseJson = mapper.writeValueAsString(response);
			

		}
		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	private List<Category> getSubCategoryList(int cat_id){
		
		List<Category> subCategoryList = new ArrayList<>();
		
		try {
			
			DatabaseConnector connector = new DatabaseConnector();
			Connection connection = connector.getConnection();
			
			String query = "SELECT "
					+Database.CategoryMaster.CAT_ID
					+", "+Database.CategoryMaster.CAT_NAME
					+", "+Database.CategoryMaster.CAT_DESCRIPTION
					+", "+Database.CategoryMaster.CAT_IMAGE
					+", "+Database.CategoryMaster.IS_ENABLE
					+", "+Database.CategoryMaster.PARENT_ID
					+" FROM "
					+Database.CategoryMaster.TABLE_NAME
					+" WHERE "
					+Database.CategoryMaster.PARENT_ID+"= "+cat_id
					+" AND "
					+Database.CategoryMaster.IS_ENABLE+"= 1";
			
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			
			resultSet.last();
			if(resultSet.getRow() > 0) {
				resultSet.first();
				do {
					Category subcategory = new Category();
					subcategory.setCat_id(resultSet.getInt(Database.CategoryMaster.CAT_ID));
					subcategory.setCat_name(UtilsString.getStirng(resultSet.getString(Database.CategoryMaster.CAT_NAME)));
					subcategory.setCat_description(UtilsString.getStirng(resultSet.getString(Database.CategoryMaster.CAT_DESCRIPTION)));
					subcategory.setCat_image(UtilsString.getStirng(resultSet.getString(Database.CategoryMaster.CAT_IMAGE)));
					subcategory.setIs_enable(resultSet.getInt(Database.CategoryMaster.IS_ENABLE));
					subcategory.setParent_id(resultSet.getInt(Database.CategoryMaster.PARENT_ID));
					subcategory.setSubCategory(getSubCategoryList(subcategory.getCat_id()));
					// add category to list
					subCategoryList.add(subcategory);
					//increment resultset
					resultSet.next();
				}while(!resultSet.isAfterLast());
			}
			
			
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			subCategoryList = new ArrayList<>();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			subCategoryList = new ArrayList<>();
		} 
		
		return subCategoryList;
	}

	/** 
	 * @return {@link List<Product>}
	 * @throws JsonProcessingException
	 */
	@GET
	@Path("/getProducts/{user_id}/{category_id}")
	public Response getCategoryProduct(@PathParam("user_id") int user_id, @PathParam("category_id") int category_id) throws JsonProcessingException {
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<List<Product>> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		List<Product> productList = new ArrayList<>();
		Product product = new Product();
		
		
		try {
			
			DatabaseConnector connector = new DatabaseConnector();
			Connection connection = connector.getConnection();
			
			String query = "SELECT "
					+Database.ProductMaster.PRO_MST_ID
					+", "+Database.ProductMaster.PRO_NAME
					+", "+Database.ProductMaster.PRO_CODE
					+", "+Database.ProductMaster.PRO_DESCRIPTION
					+", "+Database.ProductMaster.PRO_PRICE
					+", "+Database.ProductMaster.IS_ENABLE
					+", "+Database.ProductMaster.CREATE_DATE
					+", "+Database.ProductMaster.CAT_ID
					+", "+Database.ProductMaster.BRAND_ID
					+", "+Database.ProductMaster.USER_ID
					+", "+Database.ProductMaster.GST_TYPE
					+", "+Database.ProductMaster.GST
					+", "+Database.ProductMaster.DISCOUNT_PRICE
					+", "+Database.ProductMaster.PRO_IMAGE
					+", "+Database.ProductMaster.PRO_IMAGE
					+", "+Database.ProductMaster.PRO_IMAGE
					+", "+Database.ProductMaster.PRO_IMAGE
					+", "+Database.ProductMaster.PRO_IMAGE
					+", "+Database.ProductMaster.PRO_IMAGE
					+" FROM "
					+Database.ProductMaster.TABLE_NAME
					+" WHERE "
					+Database.ProductMaster.CAT_ID+" = "+ category_id;
			
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			
			resultSet.last();
			if(resultSet.getRow() == 0) {
				apiResponseStatus = ApiResponseStatus.CATEGORY_PRODUCT_NOT_FOUND;
			} else {
				
				resultSet.first();
				do {
					product = new Product();
					product.setPro_mst_id(resultSet.getInt(Database.ProductMaster.PRO_MST_ID));
					product.setPro_name(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_NAME)));
					product.setPro_code(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_CODE)));
					product.setPro_description(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_DESCRIPTION)));
					product.setPro_price(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_PRICE)));
					product.setIs_enable(resultSet.getInt(Database.ProductMaster.IS_ENABLE));
					product.setCat_id(resultSet.getInt(Database.ProductMaster.CAT_ID));
					product.setBrand_id(resultSet.getInt(Database.ProductMaster.BRAND_ID));
					product.setUser_id(resultSet.getInt(Database.ProductMaster.USER_ID));
					product.setGst_type(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.GST_TYPE)));
					product.setGst(resultSet.getInt(Database.ProductMaster.GST));
					product.setDiscount_price(resultSet.getInt(Database.ProductMaster.DISCOUNT_PRICE));
					product.setPro_image(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_IMAGE)));
					product.setProductImage(getProductImageGallery(product.getPro_mst_id()));
					
					// set cat name
					String catNameQuery = "SELECT "
							+Database.CategoryMaster.CAT_NAME
							+" FROM "
							+Database.CategoryMaster.TABLE_NAME
							+" WHERE "
							+Database.CategoryMaster.CAT_ID+"=?";
					
					PreparedStatement catNameStatement = connection.prepareStatement(catNameQuery);
					catNameStatement.setInt(1, category_id);
					ResultSet catNameResultSet = catNameStatement.executeQuery();
					catNameResultSet.first();
					product.setCat_name(UtilsString.getStirng(catNameResultSet.getString(Database.CategoryMaster.CAT_NAME)));
					
					
					//set brand name
					
					String brandNameQuery = "SELECT "
							+Database.BrandMaster.BRAND_NAME
							+" FROM "
							+Database.BrandMaster.TABLE_NAME
							+" WHERE "
							+Database.BrandMaster.BRAND_ID+"=?";
					
					PreparedStatement brandNameStatement = connection.prepareStatement(brandNameQuery);
					brandNameStatement.setInt(1, product.getBrand_id());
					ResultSet brandNameResultSet = brandNameStatement.executeQuery();
					brandNameResultSet.first();
					product.setBrand_name(UtilsString.getStirng(brandNameResultSet.getString(Database.BrandMaster.BRAND_NAME)));
										
					
					// set wishlist id if added to wishlist
					
					String wishlistQuery = "SELECT * FROM "
							+Database.WISHLISTMASTER.TABLE_NAME
							+" WHERE "
							+Database.WISHLISTMASTER.USER_ID+"=?"
							+" AND "
							+Database.WISHLISTMASTER.PRODUCT_ID+"=?";
					PreparedStatement wishlistStatement = connection.prepareStatement(wishlistQuery);
					wishlistStatement.setInt(1, user_id);
					wishlistStatement.setInt(2, product.getPro_mst_id());
					ResultSet wishlistResultSet = wishlistStatement.executeQuery();
					wishlistResultSet.last();
					if(wishlistResultSet.getRow() > 0){
						product.setWishlist_id(wishlistResultSet.getInt(Database.WISHLISTMASTER.WISHLIST_ID));
					}
					
					// add product to list
					productList.add(product);
					
					wishlistResultSet.close();
					brandNameResultSet.close();
					catNameResultSet.close();
					
					wishlistStatement.close();
					brandNameStatement.close();
					catNameStatement.close();
					
					// move cursor to next item
					resultSet.next();
				}while(!resultSet.isAfterLast());
				
				apiResponseStatus = ApiResponseStatus.CATEGORY_PRODUCT_FOUND;
			}
			
			
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			productList = new ArrayList<>();
			apiResponseStatus = ApiResponseStatus.DATABASE_CONNECTINO_ERROR;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			productList = new ArrayList<>();
			apiResponseStatus = ApiResponseStatus.MYSQL_EXCEPTION;
		} finally {
			
			response.setStatus(apiResponseStatus.getStatus_code());
			response.setMessage(apiResponseStatus.getStatus_message());
			response.setInfo(productList);
			responseJson = mapper.writeValueAsString(response);
			

		}
		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	/** 
	 * @return {@link List<Product>}
	 * @throws JsonProcessingException
	 */
	@GET
	@Path("/getTopBestSellersProduct/{user_id}")
	public Response getTopBestSellersProduct(@PathParam("user_id") int user_id) throws JsonProcessingException {
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<List<Product>> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		List<Product> productList = new ArrayList<>();
		Product product = new Product();
		
		
		try {
			
			DatabaseConnector connector = new DatabaseConnector();
			Connection connection = connector.getConnection();
			
			String query = "SELECT "
					+Database.ProductMaster.PRO_MST_ID
					+", "+Database.ProductMaster.PRO_NAME
					+", "+Database.ProductMaster.PRO_CODE
					+", "+Database.ProductMaster.PRO_DESCRIPTION
					+", "+Database.ProductMaster.PRO_PRICE
					+", "+Database.ProductMaster.IS_ENABLE
					+", "+Database.ProductMaster.CREATE_DATE
					+", "+Database.ProductMaster.CAT_ID
					+", "+Database.ProductMaster.BRAND_ID
					+", "+Database.ProductMaster.USER_ID
					+", "+Database.ProductMaster.GST_TYPE
					+", "+Database.ProductMaster.GST
					+", "+Database.ProductMaster.DISCOUNT_PRICE
					+", "+Database.ProductMaster.PRO_IMAGE
					+", "+Database.ProductMaster.PRO_IMAGE
					+", "+Database.ProductMaster.PRO_IMAGE
					+", "+Database.ProductMaster.PRO_IMAGE
					+", "+Database.ProductMaster.PRO_IMAGE
					+", "+Database.ProductMaster.PRO_IMAGE
					+" FROM "
					+Database.ProductMaster.TABLE_NAME
					+" WHERE "
					+Database.ProductMaster.PRO_MST_ID+" in ( "
					+ "SELECT "
					+Database.BESTSELLERS.PRODUCT_ID
					+" FROM "
					+Database.BESTSELLERS.TABLE_NAME
					+") LIMIT 5";
			
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			
			resultSet.last();
			if(resultSet.getRow() == 0) {
				apiResponseStatus = ApiResponseStatus.CATEGORY_PRODUCT_NOT_FOUND;
			} else {
				
				resultSet.first();
				do {
					product = new Product();
					product.setPro_mst_id(resultSet.getInt(Database.ProductMaster.PRO_MST_ID));
					product.setPro_name(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_NAME)));
					product.setPro_code(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_CODE)));
					product.setPro_description(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_DESCRIPTION)));
					product.setPro_price(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_PRICE)));
					product.setIs_enable(resultSet.getInt(Database.ProductMaster.IS_ENABLE));
					product.setCat_id(resultSet.getInt(Database.ProductMaster.CAT_ID));
					product.setBrand_id(resultSet.getInt(Database.ProductMaster.BRAND_ID));
					product.setUser_id(resultSet.getInt(Database.ProductMaster.USER_ID));
					product.setGst_type(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.GST_TYPE)));
					product.setGst(resultSet.getInt(Database.ProductMaster.GST));
					product.setDiscount_price(resultSet.getInt(Database.ProductMaster.DISCOUNT_PRICE));
					product.setPro_image(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_IMAGE)));
					product.setProductImage(getProductImageGallery(product.getPro_mst_id()));
					
					// set cat name
					/*String catNameQuery = "SELECT "
							+Database.CategoryMaster.CAT_NAME
							+" FROM "
							+Database.CategoryMaster.TABLE_NAME
							+" WHERE "
							+Database.CategoryMaster.CAT_ID+"=?";
					
					PreparedStatement catNameStatement = connection.prepareStatement(catNameQuery);
					catNameStatement.setInt(1, category_id);
					ResultSet catNameResultSet = catNameStatement.executeQuery();
					catNameResultSet.first();
					product.setCat_name(UtilsString.getStirng(catNameResultSet.getString(Database.CategoryMaster.CAT_NAME)));*/
					
					
					//set brand name
					
					String brandNameQuery = "SELECT "
							+Database.BrandMaster.BRAND_NAME
							+" FROM "
							+Database.BrandMaster.TABLE_NAME
							+" WHERE "
							+Database.BrandMaster.BRAND_ID+"=?";
					
					PreparedStatement brandNameStatement = connection.prepareStatement(brandNameQuery);
					brandNameStatement.setInt(1, product.getBrand_id());
					ResultSet brandNameResultSet = brandNameStatement.executeQuery();
					brandNameResultSet.first();
					product.setBrand_name(UtilsString.getStirng(brandNameResultSet.getString(Database.BrandMaster.BRAND_NAME)));
										
					
					// set wishlist id if added to wishlist
					
					String wishlistQuery = "SELECT * FROM "
							+Database.WISHLISTMASTER.TABLE_NAME
							+" WHERE "
							+Database.WISHLISTMASTER.USER_ID+"=?"
							+" AND "
							+Database.WISHLISTMASTER.PRODUCT_ID+"=?";
					PreparedStatement wishlistStatement = connection.prepareStatement(wishlistQuery);
					wishlistStatement.setInt(1, user_id);
					wishlistStatement.setInt(2, product.getPro_mst_id());
					ResultSet wishlistResultSet = wishlistStatement.executeQuery();
					wishlistResultSet.last();
					if(wishlistResultSet.getRow() > 0){
						product.setWishlist_id(wishlistResultSet.getInt(Database.WISHLISTMASTER.WISHLIST_ID));
					}
					
					// add product to list
					productList.add(product);
					
					wishlistResultSet.close();
					brandNameResultSet.close();
//					catNameResultSet.close();
					
					wishlistStatement.close();
					brandNameStatement.close();
//					catNameStatement.close();
					
					// move cursor to next item
					resultSet.next();
				}while(!resultSet.isAfterLast());
				
				apiResponseStatus = ApiResponseStatus.CATEGORY_PRODUCT_FOUND;
			}
			
			
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			productList = new ArrayList<>();
			apiResponseStatus = ApiResponseStatus.DATABASE_CONNECTINO_ERROR;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			productList = new ArrayList<>();
			apiResponseStatus = ApiResponseStatus.MYSQL_EXCEPTION;
		} finally {
			
			response.setStatus(apiResponseStatus.getStatus_code());
			response.setMessage(apiResponseStatus.getStatus_message());
			response.setInfo(productList);
			responseJson = mapper.writeValueAsString(response);
			

		}
		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	
	/** 
	 * @return {@link List<Product>}
	 * @throws JsonProcessingException
	 */
	@GET
	@Path("/getAllBestSellersProduct/{user_id}")
	public Response getAllBestSellersProduct(@PathParam("user_id") int user_id) throws JsonProcessingException {
		String responseJson = "";
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<List<Product>> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		List<Product> productList = new ArrayList<>();
		Product product = new Product();
		
		
		try {
			
			DatabaseConnector connector = new DatabaseConnector();
			Connection connection = connector.getConnection();
			
			String query = "SELECT "
					+Database.ProductMaster.PRO_MST_ID
					+", "+Database.ProductMaster.PRO_NAME
					+", "+Database.ProductMaster.PRO_CODE
					+", "+Database.ProductMaster.PRO_DESCRIPTION
					+", "+Database.ProductMaster.PRO_PRICE
					+", "+Database.ProductMaster.IS_ENABLE
					+", "+Database.ProductMaster.CREATE_DATE
					+", "+Database.ProductMaster.CAT_ID
					+", "+Database.ProductMaster.BRAND_ID
					+", "+Database.ProductMaster.USER_ID
					+", "+Database.ProductMaster.GST_TYPE
					+", "+Database.ProductMaster.GST
					+", "+Database.ProductMaster.DISCOUNT_PRICE
					+", "+Database.ProductMaster.PRO_IMAGE
					+", "+Database.ProductMaster.PRO_IMAGE
					+", "+Database.ProductMaster.PRO_IMAGE
					+", "+Database.ProductMaster.PRO_IMAGE
					+", "+Database.ProductMaster.PRO_IMAGE
					+", "+Database.ProductMaster.PRO_IMAGE
					+" FROM "
					+Database.ProductMaster.TABLE_NAME
					+" WHERE "
					+Database.ProductMaster.PRO_MST_ID+" in ( "
					+ "SELECT "
					+Database.BESTSELLERS.PRODUCT_ID
					+" FROM "
					+Database.BESTSELLERS.TABLE_NAME
					+")";
			
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			
			resultSet.last();
			if(resultSet.getRow() == 0) {
				apiResponseStatus = ApiResponseStatus.CATEGORY_PRODUCT_NOT_FOUND;
			} else {
				
				resultSet.first();
				do {
					product = new Product();
					product.setPro_mst_id(resultSet.getInt(Database.ProductMaster.PRO_MST_ID));
					product.setPro_name(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_NAME)));
					product.setPro_code(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_CODE)));
					product.setPro_description(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_DESCRIPTION)));
					product.setPro_price(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_PRICE)));
					product.setIs_enable(resultSet.getInt(Database.ProductMaster.IS_ENABLE));
					product.setCat_id(resultSet.getInt(Database.ProductMaster.CAT_ID));
					product.setBrand_id(resultSet.getInt(Database.ProductMaster.BRAND_ID));
					product.setUser_id(resultSet.getInt(Database.ProductMaster.USER_ID));
					product.setGst_type(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.GST_TYPE)));
					product.setGst(resultSet.getInt(Database.ProductMaster.GST));
					product.setDiscount_price(resultSet.getInt(Database.ProductMaster.DISCOUNT_PRICE));
					product.setPro_image(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_IMAGE)));
					product.setProductImage(getProductImageGallery(product.getPro_mst_id()));
					
					// set cat name
					/*String catNameQuery = "SELECT "
							+Database.CategoryMaster.CAT_NAME
							+" FROM "
							+Database.CategoryMaster.TABLE_NAME
							+" WHERE "
							+Database.CategoryMaster.CAT_ID+"=?";
					
					PreparedStatement catNameStatement = connection.prepareStatement(catNameQuery);
					catNameStatement.setInt(1, category_id);
					ResultSet catNameResultSet = catNameStatement.executeQuery();
					catNameResultSet.first();
					product.setCat_name(UtilsString.getStirng(catNameResultSet.getString(Database.CategoryMaster.CAT_NAME)));*/
					
					
					//set brand name
					
					String brandNameQuery = "SELECT "
							+Database.BrandMaster.BRAND_NAME
							+" FROM "
							+Database.BrandMaster.TABLE_NAME
							+" WHERE "
							+Database.BrandMaster.BRAND_ID+"=?";
					
					PreparedStatement brandNameStatement = connection.prepareStatement(brandNameQuery);
					brandNameStatement.setInt(1, product.getBrand_id());
					ResultSet brandNameResultSet = brandNameStatement.executeQuery();
					brandNameResultSet.first();
					product.setBrand_name(UtilsString.getStirng(brandNameResultSet.getString(Database.BrandMaster.BRAND_NAME)));
										
					
					// set wishlist id if added to wishlist
					
					String wishlistQuery = "SELECT * FROM "
							+Database.WISHLISTMASTER.TABLE_NAME
							+" WHERE "
							+Database.WISHLISTMASTER.USER_ID+"=?"
							+" AND "
							+Database.WISHLISTMASTER.PRODUCT_ID+"=?";
					PreparedStatement wishlistStatement = connection.prepareStatement(wishlistQuery);
					wishlistStatement.setInt(1, user_id);
					wishlistStatement.setInt(2, product.getPro_mst_id());
					ResultSet wishlistResultSet = wishlistStatement.executeQuery();
					wishlistResultSet.last();
					if(wishlistResultSet.getRow() > 0){
						product.setWishlist_id(wishlistResultSet.getInt(Database.WISHLISTMASTER.WISHLIST_ID));
					}
					
					// add product to list
					productList.add(product);
					
					wishlistResultSet.close();
					brandNameResultSet.close();
//					catNameResultSet.close();
					
					wishlistStatement.close();
					brandNameStatement.close();
//					catNameStatement.close();
					
					// move cursor to next item
					resultSet.next();
				}while(!resultSet.isAfterLast());
				
				apiResponseStatus = ApiResponseStatus.CATEGORY_PRODUCT_FOUND;
			}
			
			
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			productList = new ArrayList<>();
			apiResponseStatus = ApiResponseStatus.DATABASE_CONNECTINO_ERROR;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			productList = new ArrayList<>();
			apiResponseStatus = ApiResponseStatus.MYSQL_EXCEPTION;
		} finally {
			
			response.setStatus(apiResponseStatus.getStatus_code());
			response.setMessage(apiResponseStatus.getStatus_message());
			response.setInfo(productList);
			responseJson = mapper.writeValueAsString(response);
			

		}
		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	
	@GET
	@Path("/product/details/{user_id}/{product_id}")
	public Response getProductDetails(@PathParam("user_id") int user_id, @PathParam("product_id") int product_id) throws JsonProcessingException {
		String responseJson = "";
		 
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<Product> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		Product product = null;
		
		
		try {
			
			DatabaseConnector connector = new DatabaseConnector();
			Connection connection = connector.getConnection();
			
			String query = "SELECT * From "
					+Database.ProductMaster.TABLE_NAME
					+" WHERE "
					+Database.ProductMaster.PRO_MST_ID+" = "+ product_id;
			
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			
			resultSet.last();
			if(resultSet.getRow() == 0) {
				apiResponseStatus = ApiResponseStatus.PRODUCT_NOT_FOUND;
			} else {
				
				resultSet.first();
				// add category to list

				product = new Product();
				product.setPro_mst_id(resultSet.getInt(Database.ProductMaster.PRO_MST_ID));
				product.setPro_name(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_NAME)));
				product.setPro_code(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_CODE)));
				product.setPro_description(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_DESCRIPTION)));
				product.setPro_price(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_PRICE)));
				product.setIs_enable(resultSet.getInt(Database.ProductMaster.IS_ENABLE));
				product.setCat_id(resultSet.getInt(Database.ProductMaster.CAT_ID));
				product.setBrand_id(resultSet.getInt(Database.ProductMaster.BRAND_ID));
				product.setUser_id(resultSet.getInt(Database.ProductMaster.USER_ID));
				product.setGst_type(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.GST_TYPE)));
				product.setGst(resultSet.getInt(Database.ProductMaster.GST));
				product.setDiscount_price(resultSet.getInt(Database.ProductMaster.DISCOUNT_PRICE));
				product.setPro_image(UtilsString.getStirng(resultSet.getString(Database.ProductMaster.PRO_IMAGE)));
				product.setProductImage(getProductImageGallery(product.getPro_mst_id()));
				
				// set cat name
				String catNameQuery = "SELECT "
						+Database.CategoryMaster.CAT_NAME
						+" FROM "
						+Database.CategoryMaster.TABLE_NAME
						+" WHERE "
						+Database.CategoryMaster.CAT_ID+"=?";
				
				PreparedStatement catNameStatement = connection.prepareStatement(catNameQuery);
				catNameStatement.setInt(1, product.getCat_id());
				ResultSet catNameResultSet = catNameStatement.executeQuery();
				catNameResultSet.first();
				product.setCat_name(UtilsString.getStirng(catNameResultSet.getString(Database.CategoryMaster.CAT_NAME)));
				
				
				//set brand name
				
				String brandNameQuery = "SELECT "
						+Database.BrandMaster.BRAND_NAME
						+" FROM "
						+Database.BrandMaster.TABLE_NAME
						+" WHERE "
						+Database.BrandMaster.BRAND_ID+"=?";
				
				PreparedStatement brandNameStatement = connection.prepareStatement(brandNameQuery);
				brandNameStatement.setInt(1, product.getBrand_id());
				ResultSet brandNameResultSet = brandNameStatement.executeQuery();
				brandNameResultSet.first();
				product.setBrand_name(UtilsString.getStirng(brandNameResultSet.getString(Database.BrandMaster.BRAND_NAME)));
									
				
				// set wishlist id if added to wishlist
				
				String wishlistQuery = "SELECT * FROM "
						+Database.WISHLISTMASTER.TABLE_NAME
						+" WHERE "
						+Database.WISHLISTMASTER.USER_ID+"=?"
						+" AND "
						+Database.WISHLISTMASTER.PRODUCT_ID+"=?";
				PreparedStatement wishlistStatement = connection.prepareStatement(wishlistQuery);
				wishlistStatement.setInt(1, user_id);
				wishlistStatement.setInt(2, product.getPro_mst_id());
				ResultSet wishlistResultSet = wishlistStatement.executeQuery();
				wishlistResultSet.last();
				if(wishlistResultSet.getRow() > 0){
					product.setWishlist_id(wishlistResultSet.getInt(Database.WISHLISTMASTER.WISHLIST_ID));
				}
				
				
				wishlistResultSet.close();
				brandNameResultSet.close();
				catNameResultSet.close();
				
				wishlistStatement.close();
				brandNameStatement.close();
				catNameStatement.close();
				
				apiResponseStatus = ApiResponseStatus.PRODUCT_FOUND;
			}
			
			
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			product = new Product();
			apiResponseStatus = ApiResponseStatus.DATABASE_CONNECTINO_ERROR;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			product = new Product();
			apiResponseStatus = ApiResponseStatus.MYSQL_EXCEPTION;
		} finally {
			
			response.setStatus(apiResponseStatus.getStatus_code());
			response.setMessage(apiResponseStatus.getStatus_message());
			response.setInfo(product);
			responseJson = mapper.writeValueAsString(response);
			

		}
		
		return Response.status(Status.OK).entity(responseJson).build();
	}
	
	
private List<ProductImage> getProductImageGallery(int product_id){
		
		List<ProductImage> imageList = new ArrayList<>();
		
		try {
			
			DatabaseConnector connector = new DatabaseConnector();
			Connection connection = connector.getConnection();
			
			String query = "SELECT "
					+Database.ProductImage.IMAGE_ID
					+", "+Database.ProductImage.IMAGE_NAME
					+", "+Database.ProductImage.PRODUCT_ID
					+", "+Database.ProductImage.IS_ENABLE
					+", "+Database.ProductImage.IMAGE_PATH
					+" FROM "
					+Database.ProductImage.TABLE_NAME
					+" WHERE "
					+Database.ProductImage.PRODUCT_ID+"= "+product_id
					+" AND "
					+Database.ProductImage.IS_ENABLE+"= 1";
			
//			System.out.println(query);
			
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			
			resultSet.last();
			if(resultSet.getRow() > 0) {
				resultSet.first();
				do {
					ProductImage productImage = new ProductImage();
					productImage.setImge_id(resultSet.getInt(Database.ProductImage.IMAGE_ID));
					productImage.setImage_name(UtilsString.getStirng(resultSet.getString(Database.ProductImage.IMAGE_NAME)));
					productImage.setProduct_id(resultSet.getInt(Database.ProductImage.PRODUCT_ID));
					productImage.setIs_enable(resultSet.getInt(Database.ProductImage.IS_ENABLE));
					productImage.setImage_path(UtilsString.getStirng(resultSet.getString(Database.ProductImage.IMAGE_PATH)));
					// add image to list
					imageList.add(productImage);
					//increment resultset
					resultSet.next();
				}while(!resultSet.isAfterLast());
			}
			
			
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			imageList = new ArrayList<>();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			imageList = new ArrayList<>();
		} 
		
		return imageList;
	}
}
