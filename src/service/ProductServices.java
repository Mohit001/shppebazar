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
import model.Product;
import model.ProductImage;

@Path("/product")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductServices {

	ObjectMapper mapper = new ObjectMapper();
	
	@GET
	@Path("/{id}")
	public Response getProductDetails(@PathParam("id") int id) throws JsonProcessingException {
		String responseJson = "";
		 
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<Product> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
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
					+" FROM "
					+Database.ProductMaster.TABLE_NAME
					+" WHERE "
					+Database.ProductMaster.PRO_MST_ID+" = "+ id;
			
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
				product.setProductImage(getProductImageGallery(id));
				
				// get category name
//				String catNameQuery = "SELECT "
				
				
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
