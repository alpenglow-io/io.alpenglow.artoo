package io.artoo.query.one;

import io.artoo.query.Queryable;
import io.artoo.query.UnwrapException;
import io.artoo.type.Numeric;

import static java.lang.String.format;
import static java.util.Objects.isNull;

public interface Unwrappable<T> extends Queryable<T> {
  private T unwrap() {
    final var cursor = this.iterator();
    final var value = cursor.next();
    if (isNull(value))
      throw new UnwrapException("Can't unwrap: value is absent.");
    return value;
  }

  private Number asNumber(final T value, final String type) {
    return Numeric.asNumber(value, format("Can't unwrap to %s.", type), UnwrapException::new);
  }

  default int asInt() { return asNumber(unwrap(), "int").intValue(); }

  default long asLong() { return asNumber(unwrap(), "long").longValue(); }

  default short asShort() { return asNumber(unwrap(), "short").shortValue(); }

  default byte asByte() { return asNumber(unwrap(), "byte").byteValue(); }

  default float asFloat() { return asNumber(unwrap(), "float").floatValue(); }

  default double asDouble() { return asNumber(unwrap(), "double").doubleValue(); }

  default char asChar() {
    try {
      return (char) unwrap();
    } catch (ClassCastException e) {
      throw new UnwrapException("Can't unwrap to char.");
    }
  }

  default String asString() {
    try {
      return (String) unwrap();
    } catch (ClassCastException e) {
      throw new UnwrapException("Can't unwrap to String.");
    }
  }

  default T asIs() {
    return iterator().next();
  }
}
