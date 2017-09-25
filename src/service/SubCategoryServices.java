package service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Enums.ApiResponseStatus;
import basemodel.BaseResponse;
import database.Database;
import database.DatabaseConnector;
import model.Category;
import model.Person;

@Path("/SubCategory")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SubCategoryServices {

	ObjectMapper mapper = new ObjectMapper();
	
	@POST
	@Path("/list")
	public Response getAllCategories(String JsonRequest) throws JsonProcessingException {
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
					category.setCat_name(resultSet.getString(Database.CategoryMaster.CAT_NAME));
					category.setCat_description(resultSet.getString(Database.CategoryMaster.CAT_DESCRIPTION));
					category.setCat_image(resultSet.getString(Database.CategoryMaster.CAT_IMAGE));
					category.setIs_enable(resultSet.getInt(Database.CategoryMaster.IS_ENABLE));
					category.setParent_id(resultSet.getInt(Database.CategoryMaster.PARENT_ID));
					
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
}
