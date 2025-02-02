package at.htl.todo.util.immer;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.htl.todo.util.mapper.Mapper;

/** Immer simplifies handling immutable data structures.
 * @author Christian Aberger (http://www.aberger.at)
 * @see <a>https://immerjs.github.io/immer/</a>
 * @see <a>https://redux.js.org/understanding/thinking-in-redux/motivation</a>
 *
 * @param <T> The type of the baseState
 */

public class Immer<T> {
    public static final String TAG = Immer.class.getSimpleName();
    final Mapper<T> mapper;
    final Handler handler;

    public Immer(Class<? extends T> type) {
        mapper = new Mapper<T>(type);
        handler = new Handler(Looper.getMainLooper());
    }
    /** Create a deep clone of the existing model, apply a recipe to it and finally pass the new state to the consumer.
     * To reduce the load on the main thread we clone the current state in a separate thread.
     * To avoid multithreading issues we call back the recipe and resultConsumer running on the one and only Main thread of the app.
     * @param currentState the previous readonly single source or truth
     * @param recipe the callback function that modifies parts of the cloned state
     * @param resultConsumer the callback function that uses the cloned & modified model
     */
    public void produce(final T currentState, Consumer<T> recipe, Consumer<T> resultConsumer) {
        Consumer<T> runOnMainThread = t -> handler.post(() -> {
            recipe.accept(t);
            resultConsumer.accept(t);
        });
        CompletableFuture
                .supplyAsync(() -> mapper.clone(currentState))
                .thenAccept(runOnMainThread);
    }
}