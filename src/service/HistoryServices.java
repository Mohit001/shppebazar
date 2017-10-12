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
import basemodel.BaseResponse;
import database.Database;
import database.DatabaseConnector;
import model.InvoiceMaster;


@Path("/history")
@Produces(MediaType.APPLICATION_JSON)
public class HistoryServices {

	ObjectMapper mapper = new ObjectMapper();
	
	@GET
	@Path("/getOrderHistoryList/{user_id}")
	public Response getUserOrderHistoryList(@PathParam("user_id") int user_id) throws JsonProcessingException {
		String responseJson = "";
		 
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<List<InvoiceMaster>> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		
		List<InvoiceMaster> list = new ArrayList<>();
		
		try {
			
			DatabaseConnector connector = new DatabaseConnector();
			Connection connection = connector.getConnection();
			
			String invoiceListQuery = "SELECT "
					+Database.InvoiceMaster.INVOICE_ID
					+", "+Database.InvoiceMaster.ORDER_TYPE
					+", "+Database.InvoiceMaster.ORDER_STATUS
					+", "+Database.InvoiceMaster.GRAND_TOTAL
					+", "+Database.InvoiceMaster.CREATE_DATE
					+" FROM "
					+Database.InvoiceMaster.TABLE_NAME
					+" WHERE "
					+Database.InvoiceMaster.USER_ID+"=?";
			PreparedStatement getinvoicelistStatement = connection.prepareStatement(invoiceListQuery);
			getinvoicelistStatement.setInt(1, user_id);
			ResultSet resultSet = getinvoicelistStatement.executeQuery();
			resultSet.last();
			if(resultSet.getRow() == 0) {
				apiResponseStatus = ApiResponseStatus.ORDER_HISTORY_LIST_FAIL;
			} else {
				
				resultSet.first();
				InvoiceMaster invoiceMaster = null;
				do {
					invoiceMaster = new InvoiceMaster();
					invoiceMaster.setInvoice_id(resultSet.getInt(Database.InvoiceMaster.INVOICE_ID));
					invoiceMaster.setOrder_type(resultSet.getString(Database.InvoiceMaster.ORDER_TYPE));
					invoiceMaster.setOrder_status(resultSet.getString(Database.InvoiceMaster.ORDER_STATUS));
					invoiceMaster.setGrand_total(resultSet.getString(Database.InvoiceMaster.GRAND_TOTAL));
					invoiceMaster.setCreate_date(resultSet.getString(Database.InvoiceMaster.CREATE_DATE));
					list.add(invoiceMaster);
					resultSet.next();
					
				}while(!resultSet.isAfterLast());
				
				apiResponseStatus = ApiResponseStatus.ORDER_HISTORY_LIST_SUCCESS;
			}
			
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			list = new ArrayList<>();
			apiResponseStatus = ApiResponseStatus.DATABASE_CONNECTINO_ERROR;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			list = new ArrayList<>();
			apiResponseStatus = ApiResponseStatus.MYSQL_EXCEPTION;
		} finally {
			
			response.setStatus(apiResponseStatus.getStatus_code());
			response.setMessage(apiResponseStatus.getStatus_message());
			response.setInfo(list);
			responseJson = mapper.writeValueAsString(response);
			
		}
		
		return Response.status(Status.OK).entity(responseJson).build();
	}
}
