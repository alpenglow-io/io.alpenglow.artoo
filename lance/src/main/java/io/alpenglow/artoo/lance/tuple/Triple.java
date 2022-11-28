package io.alpenglow.artoo.lance.tuple;

import io.alpenglow.artoo.lance.func.*;
import io.alpenglow.artoo.lance.tuple.record.OfThree;
import io.alpenglow.artoo.lance.tuple.record.OfTwo;

import static io.alpenglow.artoo.lance.tuple.Type.*;

public interface Triple<A, B, C> extends Tuple {
  default A first() {
    return componentOf(this, 0);
  }
  default B second() {
    return componentOf(this, 1);
  }
  default C third() { return componentOf(this, 2); }

  default <T> T select(final TryFunction3<? super A, ? super B, ? super C, ? extends T> select) {
    return select.apply(first(), second(), third());
  }
  default Triple<C, A, B> shift() { return select((a, b, c) -> Tuple.of(c, a, b)); }
  @SuppressWarnings("unchecked")
  default Triple<A, B, C> where(TryPredicate3<? super A, ? super B, ? super C> predicate) {
    return predicate.test(first(), second(), third()) ? this : (Triple<A, B, C>) OfThree.Empty.Default;
  }

  default Triple<A, B, C> peek(TryConsumer3<? super A, ? super B, ? super C> cons) {
    cons.accept(first(), second(), third());
    return this;
  }
}
