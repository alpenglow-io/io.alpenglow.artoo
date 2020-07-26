package io.artoo.lance.task;

import io.artoo.lance.func.Suppl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public interface Task<T> {
  Future<T> commit(final Suppl.Uni<T> suppl);

  static <T> Task<T> single() {
    return new Single<>(Executors.newSingleThreadExecutor());
  }
}

final class Single<T> implements Task<T> {
  private final ExecutorService service;

  Single(final ExecutorService service) {
    assert service != null;
    this.service = service;
  }

  @Override
  public final Future<T> commit(final Suppl.Uni<T> suppl) {
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
