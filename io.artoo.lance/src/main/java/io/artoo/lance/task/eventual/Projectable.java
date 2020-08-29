package io.artoo.lance.task.eventual;

import io.artoo.lance.func.Func;
import io.artoo.lance.task.Eventual;
import io.artoo.lance.task.Operable;
import io.artoo.lance.task.Taskable;

import static io.artoo.lance.query.operation.Select.as;

public interface Projectable<T> {
  default <R> Eventual<R> select(final Func.Uni<? super T, ? extends R> select) {
    return new Map<>(as(select));
  }

  default <R, E extends Eventual<R>> Eventual<R> selectEventual(final Func.Uni<? super T, ? extends E> select) {
    return new Flat<>(new Map<>());
  }
}

final class Map<T, R> implements Operable<T, R> {
  private final Func.Uni<? super T, ? extends R> select;

  Map(final Func.Uni<? super T, ? extends R> select) {
    this.select = select;
  }

  @Override
  public R tryApply(final T t) throws Throwable {
    return select.tryApply(t);
  }
}

final class Flat<T, R> implements Operable<T, R> {
  private final Operable<T, R> origin;

  Flat(final Operable<T, R> origin) {this.origin = origin;}

  @Override
  public R tryApply(final T t) throws Throwable {
    return origin.tryApply(t);
  }
}

