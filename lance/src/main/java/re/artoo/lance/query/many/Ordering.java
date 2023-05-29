package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.Many;
import re.artoo.lance.value.Array;

import static re.artoo.lance.query.many.Ordering.Arrange.ascending;

public sealed interface Ordering<T> extends Many<T> {
  default <R> Ordering<T> by(TryFunction1<? super T, ? extends R> field) {
    return by(field, ascending);
  }

  default <R> Ordering<T> by(TryFunction1<? super T, ? extends R> field, Arrange arrange) {
    return null;
  }

  enum Arrange {ascending, descending}
}

final class Order<T> implements Ordering<T> {
  private final Queryable<T> queryable;

  Order(final Queryable<T> queryable) {
    this.queryable = queryable;
  }

  @Override
  public Cursor<T> cursor() {
    return queryable.cursor()
      .fold(Array.<T>none(), Array::push)
      .map(Array::sort)
      .flatMap(Array::cursor);
  }

  @Override
  public <R> Ordering<T> by(final TryFunction1<? super T, ? extends R> field, final Arrange arrange) {
    return null;
  }
}

