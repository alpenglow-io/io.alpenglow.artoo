package io.artoo.parry;

import io.artoo.parry.async.Future;
import io.artoo.parry.async.Handler;
import io.artoo.parry.async.Promise;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

abstract class ContextImpl extends AbstractContext {

  /**
   * Execute the {@code task} disabling the thread-local association for the duration
   * of the execution. {@link Vertx#currentContext()} will return {@code null},
   *
   * @param task the task to execute
   * @throws IllegalStateException if the current thread is not a Vertx thread
   */
  static void executeIsolated(Handler<Void> task) {
    Thread currentThread = Thread.currentThread();
    if (currentThread instanceof VertxThread) {
      VertxThread vertxThread = (VertxThread) currentThread;
      ContextInternal prev = vertxThread.beginEmission(null);
      try {
        task.handle(null);
      } finally {
        vertxThread.endEmission(prev);
      }
    } else {
      task.handle(null);
    }
  }

  private static final Logger log = LoggerFactory.getLogger(ContextImpl.class);

  private static final String DISABLE_TIMINGS_PROP_NAME = "vertx.disableContextTimings";
  static final boolean DISABLE_TIMINGS = Boolean.getBoolean(DISABLE_TIMINGS_PROP_NAME);

  protected final VertxInternal owner;
  protected final VertxTracer<?, ?> tracer;
  protected final JsonObject config;
  private final Deployment deployment;
  private final CloseFuture closeFuture;
  private final ClassLoader tccl;
  private final EventLoop eventLoop;
  private ConcurrentMap<Object, Object> data;
  private ConcurrentMap<Object, Object> localData;
  private volatile Handler<Throwable> exceptionHandler;
  final TaskQueue internalOrderedTasks;
  final WorkerPool internalBlockingPool;
  final WorkerPool workerPool;
  final TaskQueue orderedTasks;

  ContextImpl(VertxInternal vertx,
              VertxTracer<?, ?> tracer,
              EventLoop eventLoop,
              WorkerPool internalBlockingPool,
              WorkerPool workerPool,
              Deployment deployment,
              CloseFuture closeFuture,
              ClassLoader tccl) {
    if (VertxThread.DISABLE_TCCL && tccl != ClassLoader.getSystemClassLoader()) {
      log.warn("You have disabled TCCL checks but you have a custom TCCL to set.");
    }
    this.tracer = tracer;
    this.deployment = deployment;
    this.config = deployment != null ? deployment.config() : new JsonObject();
    this.eventLoop = eventLoop;
    this.tccl = tccl;
    this.owner = vertx;
    this.workerPool = workerPool;
    this.closeFuture = closeFuture;
    this.internalBlockingPool = internalBlockingPool;
    this.orderedTasks = new TaskQueue();
    this.internalOrderedTasks = new TaskQueue();
  }

  public Deployment getDeployment() {
    return deployment;
  }

  @Override
  public CloseFuture closeFuture() {
    return closeFuture;
  }

  @Override
  public boolean isDeployment() {
    return deployment != null;
  }

  @Override
  public String deploymentID() {
    return deployment != null ? deployment.deploymentID() : null;
  }

  @Override
  public JsonObject config() {
    return config;
  }

  public EventLoop nettyEventLoop() {
    return eventLoop;
  }

  public VertxInternal owner() {
    return owner;
  }

  @Override
  public <T> Future<T> executeBlockingInternal(Handler<Promise<T>> action) {
    return executeBlocking(this, action, internalBlockingPool, internalOrderedTasks);
  }

  @Override
  public <T> Future<T> executeBlockingInternal(Handler<Promise<T>> action, boolean ordered) {
    return executeBlocking(this, action, internalBlockingPool, ordered ? internalOrderedTasks : null);
  }

  @Override
  public <T> Future<T> executeBlocking(Handler<Promise<T>> blockingCodeHandler, boolean ordered) {
    return executeBlocking(this, blockingCodeHandler, workerPool, ordered ? orderedTasks : null);
  }

  @Override
  public <T> Future<T> executeBlocking(Handler<Promise<T>> blockingCodeHandler, TaskQueue queue) {
    return executeBlocking(this, blockingCodeHandler, workerPool, queue);
  }

  static <T> Future<T> executeBlocking(ContextInternal context, Handler<Promise<T>> blockingCodeHandler,
                                       WorkerPool workerPool, TaskQueue queue) {
    Promise<T> promise = context.promise();
    Future<T> fut = promise.future();
    try {
      Runnable command = () -> {
        Object execMetric = null;
        context.dispatch(promise, f -> {
          try {
            blockingCodeHandler.handle(promise);
          } catch (Throwable e) {
            promise.tryFail(e);
          }
        });
      };
      Executor exec = workerPool.executor();
      if (queue != null) {
        queue.execute(command, exec);
      } else {
        exec.execute(command);
      }
    } catch (RejectedExecutionException e) {
      // Pool is already shut down
      throw e;
    }
    return fut;
  }

  @Override
  public ClassLoader classLoader() {
    return tccl;
  }

  @Override
  public WorkerPool workerPool() {
    return workerPool;
  }

  @Override
  public synchronized ConcurrentMap<Object, Object> contextData() {
    if (data == null) {
      data = new ConcurrentHashMap<>();
    }
    return data;
  }

  @Override
  public synchronized ConcurrentMap<Object, Object> localContextData() {
    if (localData == null) {
      localData = new ConcurrentHashMap<>();
    }
    return localData;
  }

  public void reportException(Throwable t) {
    Handler<Throwable> handler = exceptionHandler;
    if (handler == null) {
      handler = owner.exceptionHandler();
    }
    if (handler != null) {
      handler.handle(t);
    } else {
      //log.error("Unhandled exception", t);
    }
  }

  @Override
  public Context exceptionHandler(Handler<Throwable> handler) {
    exceptionHandler = handler;
    return this;
  }

  @Override
  public Handler<Throwable> exceptionHandler() {
    return exceptionHandler;
  }

  @Override
  public final void runOnContext(Handler<Void> action) {
    runOnContext(this, action);
  }

  abstract void runOnContext(AbstractContext ctx, Handler<Void> action);

  @Override
  public void execute(Runnable task) {
    execute(this, task);
  }

  abstract <T> void execute(AbstractContext ctx, Runnable task);

  @Override
  public final <T> void execute(T argument, Handler<T> task) {
    execute(this, argument, task);
  }

  abstract <T> void execute(AbstractContext ctx, T argument, Handler<T> task);

  @Override
  public <T> void emit(T argument, Handler<T> task) {
    emit(this, argument, task);
  }

  abstract <T> void emit(AbstractContext ctx, T argument, Handler<T> task);
}
