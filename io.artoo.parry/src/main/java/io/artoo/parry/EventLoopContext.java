package io.artoo.parry;

public class EventLoopContext extends ContextImpl {

  EventLoopContext(VertxInternal vertx,
                   VertxTracer<?, ?> tracer,
                   EventLoop eventLoop,
                   WorkerPool internalBlockingPool,
                   WorkerPool workerPool,
                   Deployment deployment,
                   CloseFuture closeFuture,
                   ClassLoader tccl) {
    super(vertx, tracer, eventLoop, internalBlockingPool, workerPool, deployment, closeFuture, tccl);
  }

  @Override
  void runOnContext(AbstractContext ctx, Handler<Void> action) {
    try {
      nettyEventLoop().execute(() -> ctx.dispatch(action));
    } catch (RejectedExecutionException ignore) {
      // Pool is already shut down
    }
  }

  @Override
  <T> void emit(AbstractContext ctx, T argument, Handler<T> task) {
    EventLoop eventLoop = nettyEventLoop();
    if (eventLoop.inEventLoop()) {
      ContextInternal prev = ctx.beginDispatch();
      try {
        task.handle(argument);
      } catch (Throwable t) {
        reportException(t);
      } finally {
        ctx.endDispatch(prev);
      }
    } else {
      eventLoop.execute(() -> emit(ctx, argument, task));
    }
  }

  /**
   * <ul>
   *   <li>When the current thread is event-loop thread of this context the implementation will execute the {@code task} directly</li>
   *   <li>Otherwise the task will be scheduled on the event-loop thread for execution</li>
   * </ul>
   */
  @Override
  <T> void execute(AbstractContext ctx, T argument, Handler<T> task) {
    EventLoop eventLoop = nettyEventLoop();
    if (eventLoop.inEventLoop()) {
      task.handle(argument);
    } else {
      eventLoop.execute(() -> task.handle(argument));
    }
  }

  @Override
  <T> void execute(AbstractContext ctx, Runnable task) {
    EventLoop eventLoop = nettyEventLoop();
    if (eventLoop.inEventLoop()) {
      task.run();
    } else {
      eventLoop.execute(task);
    }
  }

  @Override
  public boolean isEventLoopContext() {
    return true;
  }

  @Override
  boolean inThread() {
    return nettyEventLoop().inEventLoop();
  }

}
