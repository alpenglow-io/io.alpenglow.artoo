package io.artoo.parry;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class BlockedThreadChecker {

  /**
   * A checked task.
   */
  public interface Task {
    long startTime();
    long maxExecTime();
    TimeUnit maxExecTimeUnit();
  }

  private static final Logger log = Logger.getLogger(BlockedThreadChecker.class.getCanonicalName());

  private final Map<Thread, Task> threads = new WeakHashMap<>();
  private final Timer timer; // Need to use our own timer - can't use event loop for this

  BlockedThreadChecker(long interval, TimeUnit intervalUnit, long warningExceptionTime, TimeUnit warningExceptionTimeUnit) {
    timer = new Timer("vertx-blocked-thread-checker", true);
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        synchronized (BlockedThreadChecker.this) {
          var now = System.nanoTime();
          for (var entry : threads.entrySet()) {
            var execStart = entry.getValue().startTime();
            var dur = now - execStart;
            final var timeLimit = entry.getValue().maxExecTime();
            var maxExecTimeUnit = entry.getValue().maxExecTimeUnit();
            var val = maxExecTimeUnit.convert(dur, TimeUnit.NANOSECONDS);
            if (execStart != 0 && val >= timeLimit) {
              final var message = "Thread " + entry.getKey() + " has been blocked for " + (dur / 1_000_000) + " ms, time limit is " + TimeUnit.MILLISECONDS.convert(timeLimit, maxExecTimeUnit) + " ms";
              if (warningExceptionTimeUnit.convert(dur, TimeUnit.NANOSECONDS) <= warningExceptionTime) {
                log.warn(message);
              } else {
                var stackTrace = new ParryException("Thread blocked");
                stackTrace.setStackTrace(entry.getKey().getStackTrace());
                log.warn(message, stackTrace);
              }
            }
          }
        }
      }
    }, intervalUnit.toMillis(interval), intervalUnit.toMillis(interval));
  }

  synchronized void registerThread(Thread thread, Task checked) {
    threads.put(thread, checked);
  }

  public void close() {
    timer.cancel();
  }
}
