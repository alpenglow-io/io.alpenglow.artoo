package re.artoo.lance.query.one;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Many;
import re.artoo.lance.query.One;

public interface Projectable<ELEMENT> extends Queryable<ELEMENT> {
  default <TARGET> One<TARGET> select(final TryFunction1<? super ELEMENT, ? extends TARGET> then) {
    return () -> cursor().map(then);
  }

  default <TARGET> Many<TARGET> selection(final TryFunction1<? super ELEMENT, ? extends Queryable<TARGET>> operation) {
    return () -> cursor().map(operation).flatMap(Queryable::cursor);
  }

  default <TARGET> One<TARGET> select(final TrySupplier1<? extends TARGET> supplier) {
    return () -> cursor().map(__ -> supplier.invoke());
  }
}
