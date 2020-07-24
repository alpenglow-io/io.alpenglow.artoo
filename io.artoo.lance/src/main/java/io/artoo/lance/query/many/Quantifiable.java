package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.operation.All;
import io.artoo.lance.query.operation.Any;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Quantifiable<T> extends Queryable<T> {
  default <R> One<Boolean> all(final Class<R> type) {
    return all(type::isInstance);
  }

  default One<Boolean> all(final Pred.Uni<? super T> where) {
    final var w = nonNullable(where, "where");
    return One.done(() -> cursor().map(new All<>(w)));
  }

  default One<Boolean> any() { return this.any(t -> true); }

  default One<Boolean> any(final Pred.Uni<? super T> where) {
    return One.done(() -> cursor().map(new Any<>(where))).or(false);
  }

  default One<Boolean> contains(final T element) {
    return One.done(() -> cursor().map(new Contains<>(element)));
  }

  default One<Boolean> notContains(final T element) {
    return One.done(() -> cursor().map(new Contains<>(element))).or(true);
  }
}

final class Contains<T> implements Func.Uni<T, Boolean> {
  private final T element;

  Contains(final T element) {
    assert element != null;
    this.element = element;
  }

  @Override
  public Boolean tryApply(final T origin) {
    return element.equals(origin) ? true : null;
  }
}


