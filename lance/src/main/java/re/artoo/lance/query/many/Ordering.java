package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.Many;

import java.util.Arrays;

import static re.artoo.lance.query.many.Ordering.Arrange.asc;

public sealed interface Ordering<T> extends Many<T> {
  enum Arrange {asc, desc}

  default <R> Ordering<T> by(TryFunction1<? super T, ? extends R> field) {
    return by(field, asc);
  }

  default <R> Ordering<T> by(TryFunction1<? super T, ? extends R> field, Arrange arrange) {
    return null;
  }
}

final class Order<T> implements Ordering<T> {
  private final Queryable<T> queryable;

  Order(final Queryable<T> queryable) {this.queryable = queryable;}

  @Override
  public Cursor<T> cursor() {
    return queryable.cursor();
  }

  @Override
  public <R> Ordering<T> by(final TryFunction1<? super T, ? extends R> field, final Arrange arrange) {
    return null;
  }
}

