package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.internal.Select;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public interface Projectable<T> extends Queryable<T> {
  default <R> Many<R> select(Func.Bi<? super Integer, ? super T, ? extends R> select) {
    return () -> cursor().map(as(Select.indexed(select)));
  }

  default <R> Many<R> select(Func.Uni<? super T, ? extends R> select) {
    return select((index, value) -> select.tryApply(value));
  }

  default <R, Q extends Queryable<R>> Many<R> selection(Func.Bi<? super Integer, ? super T, ? extends Q> select) {
    return () -> cursor().map(as(Select.indexed(select))).flatMap(Queryable::cursor);
  }

  default <R, Q extends Queryable<R>> Many<R> selection(Func.Uni<? super T, ? extends Q> select) {
    return selection((i, it) -> select.tryApply(it));
  }

  @NotNull
  private <R> Func.Uni<T, R> as(final AtomicReference<Select<T, R>> selected) {
    return element -> {
      final var applied = selected.get().tryApply(element);
      selected.set(applied.func());
      return applied.returning();
    };
  }
}
