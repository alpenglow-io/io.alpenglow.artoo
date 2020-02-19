package oak.query.many;

import oak.query.Queryable;
import oak.query.UnwrapException;
import oak.type.Numeric;

import static java.lang.String.format;
import static java.util.Objects.isNull;

public interface Unwrappable<T> extends Queryable<T> {
  private T unwrap() {
    final var cursor = this.iterator();
    final var value = cursor.next();
    if (cursor.hasNext()) throw new UnwrapException("Can't unwrap: more than one value is present.");
    if (isNull(value)) throw new UnwrapException("Can't unwrap: since the value is absent.");
    return value;
  }

  private Number asNumber(final T value, final String type) {
    return Numeric.asNumber(value, format("Can't unwrap: the value is not %s.", type), UnwrapException::new);
  }


  default T asIs() {
    return unwrap();
  }
}

