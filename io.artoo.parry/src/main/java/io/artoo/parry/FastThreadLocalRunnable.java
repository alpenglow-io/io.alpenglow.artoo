package io.artoo.parry;

final class FastThreadLocalRunnable implements Runnable {
  private final Runnable runnable;

  private FastThreadLocalRunnable(Runnable runnable) {
    this.runnable = (Runnable)ObjectUtil.checkNotNull(runnable, "runnable");
  }

  public void run() {
    try {
      this.runnable.run();
    } finally {
      FastThreadLocal.removeAll();
    }

  }

  static Runnable wrap(Runnable runnable) {
    return (Runnable)(runnable instanceof FastThreadLocalRunnable ? runnable : new FastThreadLocalRunnable(runnable));
  }
}
