package io.artoo.lance.query.one;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.internal.Select;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public interface Projectable<T> extends Queryable<T> {
  default <R> One<R> select(final Func.Uni<? super T, ? extends R> select) {
    return () -> cursor().map(as(Select.reference(select)));
  }

  default <R, Q extends Queryable<R>> Many<R> selection(final Func.Uni<? super T, ? extends Q> selection) {
    return () -> cursor().map(as(Select.reference((i, it) -> selection.tryApply(it)))).flatMap(Queryable::cursor);
  }

  default <R> One<R> select(final Suppl.Uni<? extends R> select) {
    return () -> cursor().map(as(Select.reference((i, it) -> select.tryGet())));
  }

  @NotNull
  private <R> Func.Uni<T, R> as(final AtomicReference<Select<T, R>> selected) {
    return element -> {
      final var applied = selected.get().tryApply(element);
      selected.set(applied.select());
      return applied.value();
    };
  }
}
