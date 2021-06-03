package io.artoo.parry;

import io.artoo.parry.async.AsyncResult;
import io.artoo.parry.async.Future;
import io.artoo.parry.async.Handler;
import io.artoo.parry.async.Promise;
import io.artoo.parry.async.PromiseImpl;
import io.artoo.parry.async.PromiseInternal;

import java.util.List;

abstract class AbstractContext implements ContextInternal {

  @Override
  public abstract boolean isEventLoopContext();

  @Override
  public final boolean isRunningOnContext() {
    return Vertx.currentContext() == this && inThread();
  }

  abstract boolean inThread();

  @Override
  public boolean isWorkerContext() {
    return !isEventLoopContext();
  }

  @Override
  public void emit(Handler<Void> task) {
    emit(null, task);
  }

  @Override
  public final void execute(Handler<Void> task) {
    execute(null, task);
  }

  @Override
  public final void dispatch(Handler<Void> handler) {
    dispatch(null, handler);
  }

  public final ContextInternal beginDispatch() {
    ContextInternal prev;
    var th = (VertxThread) Thread.currentThread();
    prev = th.beginEmission(this);
    th.setContextClassLoader(classLoader());
    return prev;
  }

  public final void endDispatch(ContextInternal previous) {
    VertxThread th = (VertxThread) Thread.currentThread();
    th.setContextClassLoader(previous != null ? previous.classLoader() : null);
    th.endEmission(previous);
  }

  @Override
  public long setPeriodic(long delay, Handler<Long> handler) {
    VertxImpl owner = (VertxImpl) owner();
    return owner.scheduleTimeout(this, handler, delay, true);
  }

  @Override
  public long setTimer(long delay, Handler<Long> handler) {
    VertxImpl owner = (VertxImpl) owner();
    return owner.scheduleTimeout(this, handler, delay, false);
  }

  @Override
  public final <T> void dispatch(T event, Handler<T> handler) {
    ContextInternal prev = beginDispatch();
    try {
      handler.handle(event);
    } catch (Throwable t) {
      reportException(t);
    } finally {
      endDispatch(prev);
    }
  }

  public final void dispatch(Runnable handler) {
    ContextInternal prev = beginDispatch();
    try {
      handler.run();
    } catch (Throwable t) {
      reportException(t);
    } finally {
      endDispatch(prev);
    }
  }

  @Override
  public final <T> void executeBlockingInternal(Handler<Promise<T>> action, Handler<AsyncResult<T>> resultHandler) {
    var fut = executeBlockingInternal(action);
    setResultHandler(this, fut, resultHandler);
  }

  @Override
  public <T> void executeBlockingInternal(Handler<Promise<T>> action, boolean ordered, Handler<AsyncResult<T>> resultHandler) {
    Future<T> fut = executeBlockingInternal(action, ordered);
    setResultHandler(this, fut, resultHandler);
  }

  @Override
  public <T> void executeBlocking(Handler<Promise<T>> blockingCodeHandler, boolean ordered, Handler<AsyncResult<T>> resultHandler) {
    Future<T> fut = executeBlocking(blockingCodeHandler, ordered);
    setResultHandler(this, fut, resultHandler);
  }

  @Override
  public <T> void executeBlocking(Handler<Promise<T>> blockingCodeHandler, TaskQueue queue, Handler<AsyncResult<T>> resultHandler) {
    Future<T> fut = executeBlocking(blockingCodeHandler, queue);
    setResultHandler(this, fut, resultHandler);
  }

  @Override
  public final <T> void executeBlocking(Handler<Promise<T>> blockingCodeHandler, Handler<AsyncResult<T>> resultHandler) {
    executeBlocking(blockingCodeHandler, true, resultHandler);
  }

  public <T> Future<T> executeBlocking(Handler<Promise<T>> blockingCodeHandler) {
    return executeBlocking(blockingCodeHandler, true);
  }

  @Override
  public <T> PromiseInternal<T> promise() {
    return new PromiseImpl<>(this);
  }

  @Override
  public <T> PromiseInternal<T> promise(Handler<AsyncResult<T>> handler) {
    if (handler instanceof PromiseInternal) {
      return (PromiseInternal<T>) handler;
    } else {
      PromiseInternal<T> promise = promise();
      promise.future().onComplete(handler);
      return promise;
    }
  }

  @Override
  public <T> Future<T> succeededFuture() {
    return new SucceededFuture<>(this, null);
  }

  @Override
  public <T> Future<T> succeededFuture(T result) {
    return new SucceededFuture<>(this, result);
  }

  @Override
  public <T> Future<T> failedFuture(Throwable failure) {
    return new FailedFuture<>(this, failure);
  }

  @Override
  public <T> Future<T> failedFuture(String message) {
    return new FailedFuture<>(this, message);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T get(String key) {
    return (T) contextData().get(key);
  }

  @Override
  public final void put(String key, Object value) {
    contextData().put(key, value);
  }

  @Override
  public final boolean remove(String key) {
    return contextData().remove(key) != null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T getLocal(String key) {
    return (T) localContextData().get(key);
  }

  @Override
  public final void putLocal(String key, Object value) {
    localContextData().put(key, value);
  }

  @Override
  public final boolean removeLocal(String key) {
    return localContextData().remove(key) != null;
  }

  private static <T> void setResultHandler(ContextInternal ctx, Future<T> fut, Handler<AsyncResult<T>> resultHandler) {
    if (resultHandler != null) {
      fut.onComplete(resultHandler);
    } else {
      fut.onFailure(ctx::reportException);
    }
  }
}
