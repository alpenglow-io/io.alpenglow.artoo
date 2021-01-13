package io.artoo.vortxen.thread;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalRunnable;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.UnstableApi;

public final class Fast extends Thread implements Action {
  // This will be set to true if we have a chance to wrap the Runnable.
  private final boolean cleanupFastThreadLocals;

  private InternalThreadLocalMap threadLocalMap;

  public Fast() {
    cleanupFastThreadLocals = false;
  }

  public Fast(Runnable target) {
    super(FastThreadLocalRunnable.wrap(target));
    cleanupFastThreadLocals = true;
  }

  public Fast(ThreadGroup group, Runnable target) {
    super(group, FastThreadLocalRunnable.wrap(target));
    cleanupFastThreadLocals = true;
  }

  public Fast(String name) {
    super(name);
    cleanupFastThreadLocals = false;
  }

  public Fast(ThreadGroup group, String name) {
    super(group, name);
    cleanupFastThreadLocals = false;
  }

  public Fast(Runnable target, String name) {
    super(FastThreadLocalRunnable.wrap(target), name);
    cleanupFastThreadLocals = true;
  }

  public Fast(ThreadGroup group, Runnable target, String name) {
    super(group, FastThreadLocalRunnable.wrap(target), name);
    cleanupFastThreadLocals = true;
  }

  public Fast(ThreadGroup group, Runnable target, String name, long stackSize) {
    super(group, FastThreadLocalRunnable.wrap(target), name, stackSize);
    cleanupFastThreadLocals = true;
  }

  /**
   * Returns the internal data structure that keeps the thread-local variables bound to this thread.
   * Note that this method is for internal use only, and thus is subject to change at any time.
   */
  public final InternalThreadLocalMap threadLocalMap() {
    return threadLocalMap;
  }

  /**
   * Sets the internal data structure that keeps the thread-local variables bound to this thread.
   * Note that this method is for internal use only, and thus is subject to change at any time.
   */
  public final void setThreadLocalMap(InternalThreadLocalMap threadLocalMap) {
    this.threadLocalMap = threadLocalMap;
  }

  /**
   * Returns {@code true} if {@link FastThreadLocal#removeAll()} will be called once {@link #run()} completes.
   */
  @UnstableApi
  public boolean willCleanupFastThreadLocals() {
    return cleanupFastThreadLocals;
  }

  /**
   * Returns {@code true} if {@link FastThreadLocal#removeAll()} will be called once {@link Thread#run()} completes.
   */
  @UnstableApi
  public static boolean willCleanupFastThreadLocals(Thread thread) {
    return thread instanceof io.netty.util.concurrent.FastThreadLocalThread &&
      ((io.netty.util.concurrent.FastThreadLocalThread) thread).willCleanupFastThreadLocals();
  }
}
