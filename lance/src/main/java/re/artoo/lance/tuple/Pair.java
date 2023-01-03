package re.artoo.lance.tuple;

import re.artoo.lance.func.TryConsumer2;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.tuple.record.OfTwo;

import static re.artoo.lance.tuple.Type.componentOf;


public interface Pair<A, B> extends Tuple {
  default A first() {
    return componentOf(this, 0);
  }
  default B second() {
    return componentOf(this, 1);
  }

  default <T> T select(final TryFunction2<? super A, ? super B, ? extends T> select) {
    return select.apply(first(), second());
  }
  default Pair<B, A> shift() { return select((a, b) -> Tuple.of(b, a)); }
  @SuppressWarnings("unchecked")
  default Pair<A, B> where(TryPredicate2<? super A, ? super B> predicate) {
    return predicate.test(first(), second()) ? this : (Pair<A, B>) OfTwo.Empty.Default;
  }

  default Pair<A, B> peek(TryConsumer2<? super A, ? super B> cons) {
    cons.accept(first(), second());
    return this;
  }
}
