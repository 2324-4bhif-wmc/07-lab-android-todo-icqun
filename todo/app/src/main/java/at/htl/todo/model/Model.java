package at.htl.todo.model;

public class Model {
    public static class UIState {
        public int pageIndex = 0;
        public int selectedTodoIndex = -1;
    };
    public Todo[] todos = new Todo[0];
    public UIState uiState = new UIState();
}
