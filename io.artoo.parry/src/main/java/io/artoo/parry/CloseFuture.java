package io.artoo.parry;

import io.artoo.parry.async.Future;
import io.artoo.parry.async.Promise;
import org.slf4j.Logger;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class CloseFuture implements Closeable {

  private final Logger log;
  private final Promise<Void> promise;
  private boolean closed;
  private Map<Closeable, CloseFuture> hooks;

  public CloseFuture() {
    this(null);
  }

  public CloseFuture(Logger log) {
    this.promise = Promise.promise();
    this.log = log;
  }

  /**
   * Add a close hook, notified when the {@link #close(Promise)} )} method is called.
   *
   * @param hook the hook to add
   */
  public synchronized void add(Closeable hook) {
    if (closed) {
      throw new IllegalStateException();
    }
    if (hook instanceof CloseFuture) {
      // Close future might be closed independantly, so we optimize and remove the hooks when
      // the close future completes
      CloseFuture fut = (CloseFuture) hook;
      fut.future().onComplete(ar -> remove(fut));
    }
    if (hooks == null) {
      hooks = new WeakHashMap<>();
    }
    hooks.put(hook, this);
  }

  /**
   * Remove an existing hook.
   *
   * @param hook the hook to remove
   */
  public synchronized void remove(Closeable hook) {
    if (hooks != null) {
      hooks.remove(hook);
    }
  }

  /**
   * @return whether the future is closed.
   */
  public synchronized boolean isClosed() {
    return closed;
  }

  /**
   * @return the future completed after completion of all close hooks.
   */
  public Future<Void> future() {
    return promise.future();
  }

  /**
   * Run all close hooks, after completion of all hooks, the future is closed.
   *
   * @return the future completed after completion of all close hooks
   */
  public Future<Void> close() {
    boolean close;
    List<Closeable> toClose;
    synchronized (this) {
      close = !closed;
      toClose = hooks != null ? new ArrayList<>(hooks.keySet()) : null;
      closed = true;
      hooks = null;
    }
    if (close) {
      // We want an immutable version of the list holding strong references to avoid racing against finalization
      if (toClose != null && !toClose.isEmpty()) {
        int num = toClose.size();
        AtomicInteger count = new AtomicInteger();
        for (Closeable hook : toClose) {
          Promise<Void> closePromise = Promise.promise();
          closePromise.future().onComplete(ar -> {
            if (count.incrementAndGet() == num) {
              promise.complete();
            }
          });
          try {
            hook.close(closePromise);
          } catch (Throwable t) {
            if (log != null) {
              log.warn("Failed to run close hook", t);
            }
            closePromise.tryFail(t);
          }
        }
      } else {
        promise.complete();
      }
    }
    return promise.future();
  }

  /**
   * Run the close hooks.
   *
   * @param completionHandler called when all hooks have been executed
   */
  public void close(Promise<Void> completionHandler) {
    close().onComplete(completionHandler);
  }
}
