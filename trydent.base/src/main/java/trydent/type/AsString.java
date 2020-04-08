package trydent.type;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@FunctionalInterface
public interface AsString {
  String eval();

  default boolean is(final AsString asString) {
    return nonNull(asString) && this.eval().equals(asString.eval());
  }

  default boolean isNot(final AsString asString) {
    return isNull(asString) || !this.eval().equals(asString.eval());
  }

  default boolean is(final String string) {
    return nonNull(string) && this.eval().equals(string);
  }

  default boolean isNot(final String string) {
    return isNull(string) || !this.eval().equals(string);
  }
}
