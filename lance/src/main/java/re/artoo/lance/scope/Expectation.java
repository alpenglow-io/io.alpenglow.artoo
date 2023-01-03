package re.artoo.lance.scope;

import re.artoo.lance.func.TryPredicate1;

public interface Expectation {
  default <VALUE> VALUE expect(VALUE value, TryPredicate1<? super VALUE> expectation) {
    if (!(expectation.test(value))) {
      throw new IllegalArgumentException("Can't accept value %s, it doesn't satisfy expectation".formatted(value));
    }
    return value;
  }

  default <T> T expect(T argument, boolean assertion, String message) {
    if (assertion) return argument;
    throw new IllegalArgumentException(message);
  }

  default <T> T expect(T argument, boolean assertion) {
    return expect(argument, assertion, "Can't assert");
  }
}
