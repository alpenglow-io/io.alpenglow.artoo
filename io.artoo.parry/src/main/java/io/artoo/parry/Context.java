package io.artoo.parry;

import io.artoo.parry.async.AsyncResult;
import io.artoo.parry.async.Future;
import io.artoo.parry.async.Handler;
import io.artoo.parry.async.Promise;

public interface Context {

  /**
   * Is the current thread a worker thread?
   * <p>
   * NOTE! This is not always the same as calling {@link Context#isWorkerContext}. If you are running blocking code
   * from an event loop context, then this will return true but {@link Context#isWorkerContext} will return false.
   *
   * @return true if current thread is a worker thread, false otherwise
   */
  static boolean isOnWorkerThread() {
    var t = Thread.currentThread();
    return t instanceof VertxThread && ((VertxThread) t).isWorker();
  }

  /**
   * Is the current thread an event thread?
   * <p>
   * NOTE! This is not always the same as calling {@link Context#isEventLoopContext}. If you are running blocking code
   * from an event loop context, then this will return false but {@link Context#isEventLoopContext} will return true.
   *
   * @return true if current thread is an event thread, false otherwise
   */
  static boolean isOnEventLoopThread() {
    Thread t = Thread.currentThread();
    return t instanceof VertxThread && !((VertxThread) t).isWorker();
  }

  /**
   * Is the current thread a Vert.x thread? That's either a worker thread or an event loop thread
   *
   * @return true if current thread is a Vert.x thread, false otherwise
   */
  static boolean isOnVertxThread() {
    return Thread.currentThread() instanceof VertxThread;
  }

  /**
   * Run the specified action asynchronously on the same context, some time after the current execution has completed.
   *
   * @param action  the action to run
   */
  void runOnContext(Handler<Void> action);

  /**
   * Safely execute some blocking code.
   * <p>
   * Executes the blocking code in the handler {@code blockingCodeHandler} using a thread from the worker pool.
   * <p>
   * When the code is complete the handler {@code resultHandler} will be called with the result on the original context
   * (e.g. on the original event loop of the caller).
   * <p>
   * A {@code Future} instance is passed into {@code blockingCodeHandler}. When the blocking code successfully completes,
   * the handler should call the {@link Promise#complete} or {@link Promise#complete(Object)} method, or the {@link Promise#fail}
   * method if it failed.
   * <p>
   * The blocking code should block for a reasonable amount of time (i.e no more than a few seconds). Long blocking operations
   * or polling operations (i.e a thread that spin in a loop polling events in a blocking fashion) are precluded.
   * <p>
   * When the blocking operation lasts more than the 10 seconds, a message will be printed on the console by the
   * blocked thread checker.
   * <p>
   * Long blocking operations should use a dedicated thread managed by the application, which can interact with
   * verticles using the event-bus or {@link Context#runOnContext(Handler)}
   *
   * @param blockingCodeHandler  handler representing the blocking code to run
   * @param resultHandler  handler that will be called when the blocking code is complete
   * @param ordered  if true then if executeBlocking is called several times on the same context, the executions
   *                 for that context will be executed serially, not in parallel. if false then they will be no ordering
   *                 guarantees
   * @param <T> the type of the result
   */
  <T> void executeBlocking(Handler<Promise<T>> blockingCodeHandler, boolean ordered, Handler<AsyncResult<T>> resultHandler);

  /**
   * Invoke {@link #executeBlocking(Handler, boolean, Handler)} with order = true.
   * @param blockingCodeHandler  handler representing the blocking code to run
   * @param resultHandler  handler that will be called when the blocking code is complete
   * @param <T> the type of the result
   */
  <T> void executeBlocking(Handler<Promise<T>> blockingCodeHandler, Handler<AsyncResult<T>> resultHandler);

  /**
   * Same as {@link #executeBlocking(Handler, boolean, Handler)} but with an {@code handler} called when the operation completes
   */
  <T> Future<T> executeBlocking(Handler<Promise<T>> blockingCodeHandler, boolean ordered);

  /**
   * Same as {@link #executeBlocking(Handler, Handler)} but with an {@code handler} called when the operation completes
   */
  <T> Future<T> executeBlocking(Handler<Promise<T>> blockingCodeHandler);

  boolean isEventLoopContext();

  boolean isWorkerContext();

  /**
   * Get some data from the context.
   *
   * @param key  the key of the data
   * @param <T>  the type of the data
   * @return the data
   */
  <T> T get(String key);

  /**
   * Put some data in the context.
   * <p>
   * This can be used to share data between different handlers that share a context
   *
   * @param key  the key of the data
   * @param value  the data
   */
  void put(String key, Object value);

  /**
   * Remove some data from the context.
   *
   * @param key  the key to remove
   * @return true if removed successfully, false otherwise
   */
  boolean remove(String key);

  /**
   * Get some local data from the context.
   *
   * @param key  the key of the data
   * @param <T>  the type of the data
   * @return the data
   */
  <T> T getLocal(String key);

  /**
   * Put some local data in the context.
   * <p>
   * This can be used to share data between different handlers that share a context
   *
   * @param key  the key of the data
   * @param value  the data
   */
  void putLocal(String key, Object value);

  /**
   * Remove some local data from the context.
   *
   * @param key  the key to remove
   * @return true if removed successfully, false otherwise
   */
  boolean removeLocal(String key);

  /**
   * Set an exception handler called when the context runs an action throwing an uncaught throwable.<p/>
   *
   * When this handler is called, {@link Vertx#currentContext()} will return this context.
   *
   * @param handler the exception handler
   * @return a reference to this, so the API can be used fluently
   */
  Context exceptionHandler(Handler<Throwable> handler);

  /**
   * @return the current exception handler of this context
   */
  Handler<Throwable> exceptionHandler();

}
