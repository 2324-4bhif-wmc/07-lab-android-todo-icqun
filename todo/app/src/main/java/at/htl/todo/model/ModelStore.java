package at.htl.todo.model;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.htl.todo.util.store.Store;

@Singleton
public class ModelStore extends Store<Model> {
    public static final String TAG = ModelStore.class.getSimpleName();
    @Inject
    protected ModelStore() {
        super(Model.class, new Model());
    }

    public void setTodos(Todo[] todos) {
        apply(model -> model.todos = todos);
    }
}
