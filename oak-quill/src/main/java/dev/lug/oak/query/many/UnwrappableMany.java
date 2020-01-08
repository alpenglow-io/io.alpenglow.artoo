package dev.lug.oak.query.many;

import dev.lug.oak.collect.Many;
import dev.lug.oak.query.Unwrappable;
import dev.lug.oak.type.Numeric;

import static java.lang.String.format;
import static java.util.Objects.isNull;

public interface UnwrappableMany<T> extends Unwrappable<T, Many<T>> {
  private T unwrap() {
    final var cursor = this.iterator();
    final var value = cursor.next();
    if (cursor.hasNext()) throw new UnwrapException("Can't unwrap more than one value is present.");
    if (isNull(value)) throw new UnwrapException("Can't unwrap since the value is absent.");
    return value;
  }

  private Number asNumber(final T value, final String type) {
    return Numeric.asNumber(value, format("Can't unwrap the value is not %s.", type), UnwrapException::new);
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
    return unwrap();
  }
}

final class UnwrapException extends RuntimeException {
  public UnwrapException() {
    super();
  }

  public UnwrapException(String message) {
    super(message);
  }

  public UnwrapException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnwrapException(Throwable cause) {
    super(cause);
  }

  protected UnwrapException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
