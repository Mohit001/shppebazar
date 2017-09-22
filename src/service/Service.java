package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Person;

@Path("/")
public class Service {
	
	ObjectMapper mapper = new ObjectMapper();

	@GET
	@Path("/verify")
	@Produces(MediaType.TEXT_PLAIN)
	public Response verifyRestApi() {
		String message = "Rest api verfied successfully";
		return Response.status(200).entity(message).build();
	}
	
	@GET
	@Path("/getJsonApi")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJsonApi() {
		String responseString = "";
		List<Person> list = new ArrayList<>();
		Person person = new Person();
		person.setId(1);
		person.setName("test");
		list.add(person);
		try {
			 responseString = mapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return Response.status(200).entity(responseString).build();
	}
	
	@POST
	@Path("/readJsonRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response readJsonRequest(String jsonRequest) {
		String responseString = "";
		try {
//			System.out.println(jsonRequest);
			Person person = mapper.readValue(jsonRequest, Person.class);
//			System.out.println(person.toString());
			person.setName("this is updated name");
			responseString = mapper.writeValueAsString(person);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.status(200).entity(responseString).build();
	}
}
