package io.artoo.lance.task;

import io.artoo.lance.func.Suppl;
import io.artoo.lance.task.eventual.Awaitable;
import io.artoo.lance.task.plan.Projectable;

public interface Task<T> extends Projectable<T>, Awaitable<T> {
  static <T> Task<T> returning(final Taskable<T> taskable) {
    return new Returning<>(taskable);
  }
}

final class Returning<T> implements Task<T> {
  private final Taskable<T> taskable;

  Returning(final Taskable<T> taskable) {
    this.taskable = taskable;
  }

  @Override
  public Suppl.Uni<T> task() {
    return taskable.task();
  }
}
