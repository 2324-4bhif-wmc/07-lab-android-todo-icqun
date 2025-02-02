package at.htl.todo.util.store;

import android.util.Log;

import java.util.concurrent.CompletionException;
import java.util.function.Consumer;

import at.htl.todo.util.immer.Immer;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

/** Base class for implementations using a single source of truth approach.
 * @see <a href="https://redux.js.org/understanding/thinking-in-redux/three-principles">single source of truth</a>
 * @author Christian Aberger (http://www.aberger.at)
 * @param <T> the class of the ReadOnly Single Source of Truth.
 */
public class Store<T> {
    static final String TAG = Store.class.getSimpleName();
    public final BehaviorSubject<T> pipe;
    protected final Immer<T> immer;

    protected Store(Class<? extends T> type, T initialState) {
        try {
            pipe = BehaviorSubject.createDefault(initialState);
            immer = new Immer<T>(type);
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    /** clone the current Model, apply the recipe to it and submit it to the pipe as the next Model.
     * @param recipe
     * The function that receives a clone of the current model and applies its changes to it.
     */
    public void apply(Consumer<T> recipe) {
        Consumer<T> onNext = nextState -> pipe.onNext(nextState);
        immer.produce(pipe.getValue(), recipe, onNext);
    }
}