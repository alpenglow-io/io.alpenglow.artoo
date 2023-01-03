package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.One;
import re.artoo.lance.query.closure.Sum;

public interface Summable<T> extends Queryable<T> {
  default <N extends Number> One<N> sum(final TryFunction1<? super T, ? extends N> select) {
    return () -> cursor().map(new Sum<T, N, N>(select)).keepNull();
  }

  default One<T> sum() {
    return () -> cursor().map(new Sum<T, Object, T>(it -> it)).keepNull();
  }
}

