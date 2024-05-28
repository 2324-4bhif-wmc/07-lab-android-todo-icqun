package at.htl.backend.control;

import at.htl.backend.entity.Todo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.List;

@ApplicationScoped
public class InitBean {
    @Inject
    TodoRepository todoRepository;

    public void onStartup(@Observes StartupEvent startupEvent) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            List<Todo> todos = mapper.readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream("todos.json"), new TypeReference<List<Todo>>(){});

            for (Todo todo : todos) {
                todoRepository.addFinished(todo);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
