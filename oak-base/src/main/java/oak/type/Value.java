package oak.type;

import oak.collect.cursor.Cursor;
import oak.func.With;
import oak.func.fun.Function1;
import oak.func.sup.Supplier1;

import java.util.Iterator;

import static java.util.Objects.isNull;

public interface Value<T> extends Iterable<T>, Supplier1<T> {
  @Override
  default Iterator<T> iterator() {
    return as(Cursor::once, Cursor::none);
  }

  default <R> R as(final Function1<T, R> as) {
    return With.on(get()).then(as);
  }
  default <R> R as(final Function1<T, R> as, final Supplier1<R> otherwise) {
    return With.on(get()).then(it -> isNull(it) ? otherwise.get() : as.apply(it));
  }
}
