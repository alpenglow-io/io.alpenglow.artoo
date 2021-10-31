package io.artoo.lance.query.one.oftwo;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.func.Select;
import io.artoo.lance.tuple.Pair;

import java.util.concurrent.atomic.AtomicReference;

public interface Projectable<A, B> extends Queryable.OfTwo<A, B> {
  default <R> One<R> select(Func.MaybeTriFunction<? super Integer, ? super A, ? super B, ? extends R> select) {
    return () -> cursor().map(as(Select.indexed((index, record) -> select.apply(index, record.first(), record.second()))));
  }

  default <R> One<R> select(Func.MaybeBiFunction<? super A, ? super B, ? extends R> select) {
    return select((index, first, second) -> select.tryApply(first, second));
  }

  default <R, O extends One<R>> One<R> selection(Func.MaybeTriFunction<? super Integer, ? super A, ? super B, ? extends O> select) {
    return () -> cursor().map(as(Select.indexed((i, pair) -> select.tryApply(i, pair.first(), pair.second())))).flatMap(Queryable::cursor);
  }

  default <R, O extends One<R>> One<R> selection(Func.MaybeBiFunction<? super A, ? super B, ? extends O> select) {
    return selection((i, first, second) -> select.tryApply(first, second));
  }

  default <P extends Pair<A, B>> One.OfTwo<A, B> to(Func.MaybeBiFunction<? super A, ? super B, ? extends P> func) {
    return () -> cursor().map(pair -> func.tryApply(pair.first(), pair.second()));
  }

  default <O extends One.OfTwo<A, B>> One.OfTwo<A, B> too(Func.MaybeBiFunction<? super A, ? super B, ? extends O> func) {
    return () -> cursor().flatMap(pair -> func.tryApply(pair.first(), pair.second()).cursor());
  }

  private <R> Func.MaybeFunction<Pair<A, B>, R> as(final AtomicReference<Select<Pair<A, B>, R>> selected) {
    return element -> {
      final var applied = selected.get().tryApply(element);
      selected.set(applied.func());
      return applied.returning();
    };
  }
}
