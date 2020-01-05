package dev.lug.oak.quill.query;

import dev.lug.oak.quill.Structable;

import static dev.lug.oak.type.Numeric.asNumber;
import static java.util.Objects.isNull;

public interface Unwrappable<T> extends Structable<T> {
  default int asInt() { return asNumber(this.iterator().next(), "Can't unwrap to int.", UnwrapException::new).intValue(); }
  default long asLong() { return asNumber(this.iterator().next(), "Can't unwrap to long.", UnwrapException::new).longValue(); }
  default short asShort() { return asNumber(this.iterator().next(), "Can't unwrap to short.", UnwrapException::new).shortValue(); }
  default byte asByte() { return asNumber(this.iterator().next(), "Can't unwrap to byte.", UnwrapException::new).byteValue(); }
  default float asFloat() { return asNumber(this.iterator().next(), "Can't unwrap to int.", UnwrapException::new).floatValue(); }
  default double asDouble() { return asNumber(this.iterator().next(), "Can't unwrap to double.", UnwrapException::new).doubleValue(); }
  default char asChar() {
    try {
      final var value = this.iterator().next();
      if (isNull(value)) throw new UnwrapException("Can't unwrap the value is absent.");
      return (char) value;
    } catch (ClassCastException e) {
      throw new UnwrapException("Can't unwrap to char.");
    }
  }
  default String asString() {
    try {
      final var value = this.iterator().next();
      if (isNull(value)) throw new UnwrapException("Can't unwrap the value is absent.");
      return (String) value;
    } catch (ClassCastException e) {
      throw new UnwrapException("Can't unwrap to String.");
    }
  }
  default T asIs() {
    try {
      return this.iterator().next();
    } catch (Throwable throwable) {
      return null;
    }
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
