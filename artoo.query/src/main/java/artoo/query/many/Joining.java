package artoo.query.many;

import artoo.func.$2.Pred;
import artoo.query.Many;
import artoo.union.$2.Union;

public interface Joining<T1, T2> {
  Many<Bag<T1, T2>> on(final Pred<? super T1, ? super T2> on);

  interface Bag<T1, T2> extends Union<T1, T2> {
  }
}
