package io.artoo.lance.task;

import io.artoo.lance.func.Suppl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public interface Task<T> {
  Future<T> submit(final Suppl.Uni<T> suppl);

  static <T> Task<T> safe() {
    return new SafeTask<>(Executors.newSingleThreadExecutor());
  }
}

final class SafeTask<T> implements Task<T> {
  private final ExecutorService service;

  SafeTask(final ExecutorService service) {
    assert service != null;
    this.service = service;
  }

  @Override
  public final Future<T> submit(final Suppl.Uni<T> suppl) {
    try {
      return service.submit(() -> {
        try {
          return suppl.tryGet();
        } catch (Throwable throwable) {
          throw new TaskException(throwable);
        }
      });
    } finally {
      service.shutdown();
    }
  }
}
