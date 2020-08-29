package io.artoo.lance.task.plan;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.task.Taskable;
import io.artoo.lance.task.Task;

public interface Projectable<T> extends Taskable<T> {
  default <R> Task<R> select(Func.Uni<? super T, ? extends R> select) {
    return () -> new Select<>(this, select);
  }

  default <R, P extends Taskable<R>> Task<R> selectTask(Func.Uni<? super T, ? extends P> select) {
    return () -> new SelectTask<>(this, select);
  }
}

final class Select<T, R> implements Suppl.Uni<R> {
  private final Taskable<T> taskable;
  private final Func.Uni<? super T, ? extends R> select;

  Select(final Taskable<T> taskable, final Func.Uni<? super T, ? extends R> select) {
    this.taskable = taskable;
    this.select = select;
  }

  @Override
  public R tryGet() throws Throwable {
    return select.tryApply(taskable.task().tryGet());
  }
}

final class SelectTask<T, R, P extends Taskable<R>> implements Suppl.Uni<R> {
  private final Taskable<T> taskable;
  private final Func.Uni<? super T, ? extends P> select;

  SelectTask(final Taskable<T> taskable, final Func.Uni<? super T, ? extends P> select) {
    this.taskable = taskable;
    this.select = select;
  }

  @Override
  public R tryGet() throws Throwable {
    return select.tryApply(taskable.task().tryGet()).task().tryGet();
  }
}
