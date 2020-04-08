package trydent.query.many;

import trydent.func.$2.Pred;
import trydent.query.$2.Many;

public interface Joining<T1, T2> {
  Many<T1, T2> on(final Pred<? super T1, ? super T2> on);
}
