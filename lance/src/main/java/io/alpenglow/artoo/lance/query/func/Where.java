package io.alpenglow.artoo.lance.query.func;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.func.TryPredicate2;
import io.alpenglow.artoo.lance.scope.Expectation;

import java.util.Objects;

public final class Where<T> implements TryFunction1<T, T>, Expectation {
  private final TryPredicate2<? super Integer, ? super T> where;
  private int index;

  public Where(final TryPredicate2<? super Integer, ? super T> where) {
    this.where = expect(where, Objects::nonNull);
    this.index = 0;
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    return where.tryTest(index++, element) ? element : null;
  }
}
