package rest;

import java.util.List;
import java.util.Optional;

import io.quarkiverse.renarde.htmx.HxController;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

import org.jboss.resteasy.reactive.RestForm;

import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.CheckedTemplate;
import model.Todo;
import org.jboss.resteasy.reactive.RestPath;

/**
 * This defines a REST controller, each method will be available under the "Classname/method" URI by convention
 */
@Path("/")
public class Todos extends HxController {
    
    /**
     * This defines templates available in src/main/resources/templates/Classname/method.html by convention
     */
    @CheckedTemplate
    public static class Templates {
        /**
         * This specifies that the Todos/index.html template does not take any parameter
         */
        public static native TemplateInstance index();

        public static native TemplateInstance htmx();
        /**
         * This specifies that the Todos/todos.html template takes a todos parameter of type List&lt;Todo&gt;
         */
        public static native TemplateInstance todos(List<Todo> todos);

        public static native TemplateInstance todos$list(List<Todo> todos);
    }

    // This overrides the convention and makes this method available at "/renarde"
    @Path("/")
    public TemplateInstance index() {
        // renders the Todos/index.html template
        return Templates.index();
    }

    public TemplateInstance htmx() {
        // renders the Todos/index.html template
        return Templates.htmx();
    }

    public TemplateInstance todos() {
        if(isHxRequest()) {
            return Templates.todos$list(Todo.listAll());
        }
        // renders the Todos/todos.html template with our list of Todo entities
        return Templates.todos(Todo.listAll());
    }

    @PUT
    public void done(@RestPath @NotBlank String id) {
        final Optional<Todo> todo = Todo.findById(id);
        if(todo.isEmpty()) {
            notFound();
            return;
        }

        todo.get().completed = !todo.get().completed;

        todos();
    }
    
    // Creates a POST action at Todos/add taking a form element named task
    @POST
    public void add(@RestForm @NotBlank String task) {
        // If validation fails, redirect to the todos page (with errors propagated)
        if(validationFailed()) {
            // redirect to the todos page by just calling the method: it does not return!
            todos();
        }
        // save the new Todo
        Todo todo = new Todo();
        todo.task = task;
        todo.persist();
        // redirect to the todos page
        todos();
    }
}