package br.com.vagner.apirote.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.vagner.apirote.exception.ServiceException;
import br.com.vagner.apirote.model.Map;
import br.com.vagner.apirote.service.MapService;
import br.com.vagner.apirote.util.HttpResponse;


@Path("mapa/")
@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
@Component(value="mapResource")
public class MapResource {
	
	private static final Logger LOGGER = Logger.getLogger(MapResource.class);
	
	@Autowired
	private MapService service;
	
	@POST
	@Path("/create/")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Response create(@QueryParam("mapa") Map mapa) {

		LOGGER.info("Criando novo mapa.");

		try {
			return Response.ok(service.incluir(mapa)).build();
		} catch (ServiceException e) {
			return Response.status(Status.BAD_REQUEST).entity(new HttpResponse(e.getMessage())).build();

		} catch (Exception e) {
			return Response.serverError().entity(new HttpResponse(e.getMessage())).build();
		}

	}
	
	@GET
	@Path("/rota/")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Response calcularRota(@QueryParam("name") String name, 
								 @QueryParam("origem")String origem,
								 @QueryParam("destino")String destino,
								 @QueryParam("origem")Double distancia,
								 @QueryParam("valorLitro")Double valorLitro){
		
		try {
			return Response.ok(service.calculateShortestRoute(name, origem, destino, distancia, valorLitro)).build();
		} catch (ServiceException e) {
			return Response.status(Status.BAD_REQUEST).entity(new HttpResponse(e.getMessage())).build();

		} catch (Exception e) {
			return Response.serverError().entity(new HttpResponse(e.getMessage())).build();
		}
	}
	

}
