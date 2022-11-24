package lance.query.eventual.internal;

import lance.func.Func;

public interface Next<T> {
  void await();
  <P> Next<P> select(final Func.TryFunction<? super T, ? extends P> select);
}
