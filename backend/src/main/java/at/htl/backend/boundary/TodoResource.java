package at.htl.backend.boundary;

import at.htl.backend.control.TodoRepository;
import at.htl.backend.entity.Todo;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;
import java.util.MissingResourceException;

@Path("/todos")
public class TodoResource {

    @Inject
    TodoRepository todoRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Todo> getAll() {
        return todoRepository.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNew(@Context UriInfo uriInfo, Todo todo) {
        todoRepository.add(todo);

        URI uri = uriInfo
                .getAbsolutePathBuilder()
                .path(String.format("%d",todo.getId()))
                .build();

        return Response
                .created(uri)
                .build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response update(@PathParam("id") int id, Todo todo) {
        todoRepository.update(id, todo);

        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        todoRepository.remove(id);
        return Response.noContent().build();
    }
}
