package re.artoo.lance.tuple;

import re.artoo.lance.func.TryConsumer2;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.tuple.record.OfTwo;

import static re.artoo.lance.tuple.Type.componentOf;


public interface Pair<FIRST, SECOND> extends Tuple {
  default FIRST first() {
    return componentOf(this, 0);
  }
  default SECOND second() {
    return componentOf(this, 1);
  }

  default <T> T let(final TryFunction2<? super FIRST, ? super SECOND, ? extends T> select) {
    return select.apply(first(), second());
  }

  default Pair<FIRST, SECOND> both(FIRST first, SECOND second) {
    return Tuple.of(first, second);
  }

  default Pair<FIRST, SECOND> letFirst(FIRST first) {
    return Tuple.of(first, second());
  }

  default Pair<FIRST, SECOND> letSecond(SECOND second) {
    return Tuple.of(first(), second);
  }

  default Pair<SECOND, FIRST> shift() { return let((first, second) -> Tuple.of(second, first)); }
  @SuppressWarnings("unchecked")
  default Pair<FIRST, SECOND> takeIf(TryPredicate2<? super FIRST, ? super SECOND> predicate) {
    return predicate.test(first(), second()) ? this : (Pair<FIRST, SECOND>) OfTwo.Empty.Default;
  }

  default Pair<FIRST, SECOND> also(TryConsumer2<? super FIRST, ? super SECOND> cons) {
    cons.accept(first(), second());
    return this;
  }
}
