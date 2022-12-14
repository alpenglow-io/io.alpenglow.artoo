package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryPredicate2;
import io.alpenglow.artoo.lance.query.Closure;
import io.alpenglow.artoo.lance.query.Unit;
import io.alpenglow.artoo.lance.scope.Expectation;

import java.util.Objects;

public final class Where<T> implements Closure<Unit<T>, Unit<T>>, Expectation {
  private final TryPredicate2<? super Integer, ? super T> where;
  private int index;

  public Where(final TryPredicate2<? super Integer, ? super T> where) {
    this.where = expect(where, Objects::nonNull);
    this.index = 0;
  }

  @Override
  public Unit<T> tryApply(final Unit<T> element) throws Throwable {
    return where.invoke(index++, element.invoke()) ? element : Unit.nothing();
  }
}
