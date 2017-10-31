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
import model.InvoiceDetails;
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
					+Database.InvoiceMasterTable.INVOICE_ID
					+", "+Database.InvoiceMasterTable.ORDER_TYPE
					+", "+Database.InvoiceMasterTable.ORDER_STATUS
					+", "+Database.InvoiceMasterTable.GRAND_TOTAL
					+", "+Database.InvoiceMasterTable.CREATE_DATE
					+" FROM "
					+Database.InvoiceMasterTable.TABLE_NAME
					+" WHERE "
					+Database.InvoiceMasterTable.USER_ID+"=?"
					+" ORDER BY "+Database.InvoiceMasterTable.INVOICE_ID+" DESC";
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
					invoiceMaster.setInvoice_id(resultSet.getInt(Database.InvoiceMasterTable.INVOICE_ID));
					invoiceMaster.setOrder_type(resultSet.getString(Database.InvoiceMasterTable.ORDER_TYPE));
					invoiceMaster.setOrder_status(resultSet.getString(Database.InvoiceMasterTable.ORDER_STATUS));
					invoiceMaster.setGrand_total(resultSet.getString(Database.InvoiceMasterTable.GRAND_TOTAL));
					invoiceMaster.setCreate_date(resultSet.getString(Database.InvoiceMasterTable.CREATE_DATE));
					invoiceMaster.setUser_id(user_id);
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
	
	@GET
	@Path("/getOrderHistoryDetails/{invoice_id}")
	public Response getUserOrderHistoryDetails(@PathParam("invoice_id") int invoice_id) throws JsonProcessingException {
		String responseJson = "";
		 
		ApiResponseStatus apiResponseStatus = ApiResponseStatus.OUT_OF_SERVICE;
		BaseResponse<InvoiceMaster> response = new BaseResponse<>();
		response.setStatus(apiResponseStatus.getStatus_code());
		response.setMessage(apiResponseStatus.getStatus_message());
		
		InvoiceMaster invoiceMaster = new InvoiceMaster();
		
		try {
			
			DatabaseConnector connector = new DatabaseConnector();
			Connection connection = connector.getConnection();
			
			String invoiceListQuery = "SELECT * "
					+" FROM "
					+Database.InvoiceMasterTable.TABLE_NAME
					+" WHERE "
					+Database.InvoiceMasterTable.INVOICE_ID+"=?";
			PreparedStatement getinvoicelistStatement = connection.prepareStatement(invoiceListQuery);
			getinvoicelistStatement.setInt(1, invoice_id);
			ResultSet resultSet = getinvoicelistStatement.executeQuery();
			resultSet.last();
			if(resultSet.getRow() == 0) {
				apiResponseStatus = ApiResponseStatus.ORDER_HISTORY_DETAILS_FAIL;
			} else if(resultSet.getRow() > 1){
				apiResponseStatus = ApiResponseStatus.ORDER_HISTORY_DETAILS_FAIL;
			}else {
				resultSet.first();
				
				invoiceMaster = new InvoiceMaster();
				invoiceMaster.setInvoice_id(resultSet.getInt(Database.InvoiceMasterTable.INVOICE_ID));
				invoiceMaster.setUser_id(resultSet.getInt(Database.InvoiceMasterTable.USER_ID));
				invoiceMaster.setOrder_type(resultSet.getString(Database.InvoiceMasterTable.ORDER_TYPE));
				invoiceMaster.setUser_type(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.USER_TYPE)));
				invoiceMaster.setShiping_address1(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.SHIPPING_ADDRESS1)));
				invoiceMaster.setShiping_address2(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.SHIPPING_ADDRESS2)));
				invoiceMaster.setShiping_state(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.SHIPPING_STATE)));
				invoiceMaster.setShiping_city(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.SHIPPING_CITY)));
				invoiceMaster.setShiping_postcode(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.SHIPPING_POSTCODE)));
				invoiceMaster.setShiping_additiondetails(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.SHIPPING_ADDITIONAL_DETAILS)));
				invoiceMaster.setBilling_address1(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.BILLING_ADDRESS1)));
				invoiceMaster.setBilling_address2(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.BILLING_ADDRESS2)));
				invoiceMaster.setBilling_state(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.BILLING_STATE)));
				invoiceMaster.setBilling_city(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.BILLING_CITY)));
				invoiceMaster.setBilling_postcode(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.BILLING_POSTCODE)));
				invoiceMaster.setBilling_addtionaldeatails(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.BILLING_ADDITIONAL_DETAILS)));
				invoiceMaster.setCreate_date(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.CREATE_DATE)));
				invoiceMaster.setShiping_fullname(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.SHIPPING_FULL_NAME)));
				invoiceMaster.setShiping_email(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.SHIPPING_EMAIL)));
				invoiceMaster.setBilling_fullname(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.BILLING_FULL_NAME)));
				invoiceMaster.setBilling_email(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.BILLING_EMAIL)));
				invoiceMaster.setTotal_amount(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.TOTAL_AMOUNT)));
				invoiceMaster.setGrand_total(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.GRAND_TOTAL)));
				invoiceMaster.setShipping_charge(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.SHIPPING_CHARGE)));
				invoiceMaster.setOrder_no(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.ORDER_NO)));
				invoiceMaster.setIp_address(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.IP_ADDRESS)));
				invoiceMaster.setOrder_status(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.ORDER_STATUS)));
				invoiceMaster.setCart_id(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.CART_ID)));
				invoiceMaster.setShipping_contact_no(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.SHIPPING_ADDRESS_CONTACT_NO)));
				invoiceMaster.setBilling_contact_no(UtilsString.getStirng(resultSet.getString(Database.InvoiceMasterTable.BILLING_ADDRESS_CONTACT_NO))); 
				
				List<InvoiceDetails> invoiceDetailList = new ArrayList<>();
				
				String invoiceDetailsQuery = "SELECT * FROM "
						+Database.InvoiceDetailsTable.TABLE_NAME
						+" WHERE "
						+Database.InvoiceDetailsTable.INVOICE_ID+"=?";
				
				PreparedStatement invoiceDetailsStatement = connection.prepareStatement(invoiceDetailsQuery);
				invoiceDetailsStatement.setInt(1, invoiceMaster.getInvoice_id());
				ResultSet invoiceDetailsResultSet = invoiceDetailsStatement.executeQuery();
				
				invoiceDetailsResultSet.last();
				if(invoiceDetailsResultSet.getRow() == 0) {
					apiResponseStatus = ApiResponseStatus.ORDER_HISTORY_DETAILS_FAIL;
				} else {
					invoiceDetailsResultSet.first();
					do {
						InvoiceDetails details = new InvoiceDetails();
						details.setInvoice_id(invoiceDetailsResultSet.getInt(Database.InvoiceDetailsTable.INVOICE_ID));
						details.setProduct_id(invoiceDetailsResultSet.getInt(Database.InvoiceDetailsTable.PRODUCT_ID));
						details.setProduct_name(UtilsString.getStirng(invoiceDetailsResultSet.getString(Database.InvoiceDetailsTable.PRODUCT_NAME)));
						details.setProduct_code(UtilsString.getStirng(invoiceDetailsResultSet.getString(Database.InvoiceDetailsTable.PRODUCT_CODE)));
						details.setProduct_description(UtilsString.getStirng(invoiceDetailsResultSet.getString(Database.InvoiceDetailsTable.PRODUCT_DESCRIPTION)));
						details.setProduct_price(UtilsString.getStirng(invoiceDetailsResultSet.getString(Database.InvoiceDetailsTable.PRODUCT_PRICE)));
						details.setProduct_cat_id(UtilsString.getStirng(invoiceDetailsResultSet.getString(Database.InvoiceDetailsTable.PRODUCT_CAT_ID)));
						details.setProduct_brand_id(UtilsString.getStirng(invoiceDetailsResultSet.getString(Database.InvoiceDetailsTable.PRODUCT_BRAND_ID)));
						details.setProduct_gst_type(UtilsString.getStirng(invoiceDetailsResultSet.getString(Database.InvoiceDetailsTable.PRODUCT_GST_TYPE)));
						details.setProduct_gst(UtilsString.getStirng(invoiceDetailsResultSet.getString(Database.InvoiceDetailsTable.PRODUCT_GST)));
						details.setProduct_discount_price(UtilsString.getStirng(invoiceDetailsResultSet.getString(Database.InvoiceDetailsTable.PRODUCT_DISCOUNT_PRICE)));
						details.setProduct_image_name(UtilsString.getStirng(invoiceDetailsResultSet.getString(Database.InvoiceDetailsTable.PRODUCT_IMAGE_NAME)));
						details.setProduct_category_name(UtilsString.getStirng(invoiceDetailsResultSet.getString(Database.InvoiceDetailsTable.PRODUCT_CATEGORY_NAME)));
						details.setProduct_brand_name(UtilsString.getStirng(invoiceDetailsResultSet.getString(Database.InvoiceDetailsTable.PRODUCT_BRAND_NAME)));
						details.setProduct_qty(invoiceDetailsResultSet.getInt(Database.InvoiceDetailsTable.PRODUCT_QTY));
						details.setShipping_charge(invoiceDetailsResultSet.getString(Database.InvoiceDetailsTable.SHIPPING_CHARGE));
						
						invoiceDetailList.add(details);
						invoiceDetailsResultSet.next();
						
					}while(!invoiceDetailsResultSet.isAfterLast());
					
					invoiceMaster.setInvoiceProductList(invoiceDetailList);
					
					apiResponseStatus = ApiResponseStatus.ORDER_HISTORY_DETAILS_SUCCESS;
				}
				
			}
			
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			invoiceMaster = new InvoiceMaster();
			apiResponseStatus = ApiResponseStatus.DATABASE_CONNECTINO_ERROR;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			invoiceMaster = new InvoiceMaster();
			apiResponseStatus = ApiResponseStatus.MYSQL_EXCEPTION;
		} finally {
			
			response.setStatus(apiResponseStatus.getStatus_code());
			response.setMessage(apiResponseStatus.getStatus_message());
			response.setInfo(invoiceMaster);
			responseJson = mapper.writeValueAsString(response);
			
		}
		
		return Response.status(Status.OK).entity(responseJson).build();
	}
}
