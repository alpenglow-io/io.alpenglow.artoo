package oak.query.many;

import oak.func.$2.Pred;
import oak.query.$2.many.Many;

public interface Joining<T1, T2> {
  Many<T1, T2> on(final Pred<? super T1, ? super T2> on);
}
