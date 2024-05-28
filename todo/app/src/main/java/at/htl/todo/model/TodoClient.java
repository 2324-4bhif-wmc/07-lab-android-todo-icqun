package at.htl.todo.model;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;

@Path("/todos")
@Consumes(MediaType.APPLICATION_JSON)
public interface TodoClient {
    @GET
    Todo[] all();

    @POST
    void create(Todo todo);

    @PUT
    @Path("/{id}")
    void update(@PathParam("id") int id, Todo todo);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") int id);
}
