package org.itver.com.messenger.service;


import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/injectdemo")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class InjectDemoResource {
 
  //EJemplos no importante para el curso
  @GET
  @Path("annotations")
  public String getParamsUsingAnnotations(@MatrixParam("param") String matrixParam,
		                                  @HeaderParam("customHeaderValue") String header,
		                                  @CookieParam("name") String cookie) {
	  return "Matrix param: " + matrixParam + " Header Param: " + header + " Cookie " + cookie;
  }

  @GET
  @Path("context")
  public String getParamUsingContext(@Context UriInfo uriInfo, @Context HttpHeaders header) {
	  String path = uriInfo.getAbsolutePath().toString();
	  String cookies = header.getCookies().toString();
	  return "Parth: " + path + " Cookies: " + cookies;
  }
  
  
}
