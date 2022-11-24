package lance.query.eventual.internal;

import lance.func.Func;

public interface Next<T> {
  void await();
  <P> Next<P> select(final Func.MaybeFunction<? super T, ? extends P> select);
}
