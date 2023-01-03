package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.One;
import re.artoo.lance.query.closure.Extreme;

public interface Extremable<T> extends Queryable<T> {
  default <N extends Number> One<N> max(final TryFunction1<? super T, ? extends N> select) {
    return () -> cursor().map(Extreme.<T, N>max(select)).keepNull();
  }

  default One<T> max() {
    return () -> cursor().map(Extreme.max()).keepNull();
  }

  default <N extends Number> One<N> min(final TryFunction1<? super T, ? extends N> select) {
    return () -> cursor().map(Extreme.<T, N>min(select)).keepNull();
  }

  default One<T> min() {
    return () -> cursor().map(Extreme.min()).keepNull();
  }
}

