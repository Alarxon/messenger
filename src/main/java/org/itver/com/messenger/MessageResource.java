package org.itver.com.messenger;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.itver.com.messenger.beans.MessageFIlterBean;
import org.itver.com.messenger.model.Message;
import org.itver.com.messenger.service.MessageService;

@Path("/messages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageResource {
	
	MessageService messageService = new MessageService();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> getJSONMessages(@BeanParam MessageFIlterBean filterBean) {
		System.out.println("JSON");
		if(filterBean.getYear() > 0) {
	    	return messageService.getAllMessagesForYear(filterBean.getYear());
	    }
	    //Checar bien
	    if(filterBean.getStart() >= 0 && filterBean.getSize() > 0) {
	    	return messageService.getAllMessagesPaginated(filterBean.getStart(), filterBean.getSize());
	    }
		return messageService.getAllMessages();
	}
	
	@GET
	@Produces(MediaType.TEXT_XML)
	public List<Message> getXMLMessages(@BeanParam MessageFIlterBean filterBean) {
		System.out.println("XML");
		if(filterBean.getYear() > 0) {
	    	return messageService.getAllMessagesForYear(filterBean.getYear());
	    }
	    //Checar bien
	    if(filterBean.getStart() >= 0 && filterBean.getSize() > 0) {
	    	return messageService.getAllMessagesPaginated(filterBean.getStart(), filterBean.getSize());
	    }
		return messageService.getAllMessages();
	}
	
	@POST
    public Response addMEssage(Message message, @Context UriInfo uriInfo) {
		Message newMessage = messageService.addMessage(message);
		String newId = String.valueOf(newMessage.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(uri)
				.entity(newMessage)
				.build();
		//return messageService.addMessage(message);
	}
	
	@PUT
	@Path("/{messageId}")
	public Message updateMessage(@PathParam("messageId") long id, Message message) {
		message.setId(id);
		return messageService.updateMessage(message);
	}
	
	@DELETE
	@Path("/{messageId}")
	public void deleteMessage(@PathParam("messageId") long id) {
		messageService.removeMessage(id);
	}
	
	@GET
	@Path("/{messageId}")
	public Message getMessage(@PathParam("messageId") long messageId, @Context UriInfo uriInfo) {
		Message message = messageService.getMessage(messageId);		
		message.addLink(getUriForSelf(uriInfo, message), "self");
		message.addLink(getUriForProfile(uriInfo, message), "profile");
		message.addLink(getUriForComments(uriInfo, message), "profile");

		
		return message;
	}
	private String getUriForComments(UriInfo uriInfo, Message message) {
		URI uri = uriInfo.getBaseUriBuilder()
			     .path(MessageResource.class)
		         .path(MessageResource.class, "getCommentResource")
		         .path(CommentResource.class)
		         .resolveTemplate("messageId",message.getId())
		         .build();
        return uri.toString(); 
	}
	private String getUriForProfile(UriInfo uriInfo, Message message) {
		URI uri = uriInfo.getBaseUriBuilder()
				         .path(ProfileResource.class)
				         .path(message.getAuthor())
				         .build();
		return uri.toString();
	}
	private String getUriForSelf(UriInfo uriInfo, Message message) {
		String uri = uriInfo.getBaseUriBuilder()
		       .path(MessageResource.class)
		       .path(Long.toString(message.getId()))
		       .build()
		       .toString();
		return uri;
	}
	@Path("/{messageId}/comments")
	public CommentResource getCommentResource() {
		return new CommentResource();
	}
}
