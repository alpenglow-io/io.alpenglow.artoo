package io.alpenglow.artoo.lance.scope;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.func.TryPredicate1;

public interface Expectation {
  default <VALUE> VALUE expect(VALUE value, TryPredicate1<? super VALUE> expectation) {
    if (!(expectation.test(value))) {
      throw new IllegalArgumentException("Can't accept value %s, it doesn't satisfy expectation".formatted(value));
    }
    return value;
  }
}
