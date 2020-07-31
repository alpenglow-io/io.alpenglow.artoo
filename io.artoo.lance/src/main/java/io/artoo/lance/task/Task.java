package io.artoo.lance.task;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;

import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public interface Task<T> extends Future<T> {
  static <T> RecursiveTask<T> returns(final Suppl.Uni<T> supplier) {
    return new ReturningTask<>(supplier);
  }
}

final class ReturningTask<T> extends RecursiveTask<T> implements Task<T> {
  private final Suppl.Uni<T> supplier;

  ReturningTask(final Suppl.Uni<T> supplier) {
    this.supplier = supplier;
  }

  @Override
  protected T compute() {
    return supplier.get();
  }
}
