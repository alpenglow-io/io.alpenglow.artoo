package io.artoo.lance.thread;

import io.artoo.lance.func.Suppl;
import io.artoo.lance.thread.task.Projectable;

import java.util.concurrent.CountDownLatch;

public interface Task<T> extends Projectable<T> {
  static <T> Task<T> async(final Suppl.Uni<T> task) {
    return () -> {
      final var latch = new CountDownLatch(1);
      new Thread(() -> {
        task.get();
        latch.countDown();
      }).start();

      return null;
    };
  }
}
