package re.artoo.lance.tuple;

import re.artoo.lance.func.TryConsumer4;
import re.artoo.lance.func.TryFunction4;
import re.artoo.lance.func.TryPredicate4;
import re.artoo.lance.tuple.record.OfFour;

import static re.artoo.lance.tuple.Type.componentOf;

public interface Quadruple<A, B, C, D> extends Tuple {
  default A first() {
    return componentOf(this, 0);
  }
  default B second() {
    return componentOf(this, 1);
  }
  default C third() {
    return componentOf(this, 2);
  }
  default D forth() { return componentOf(this, 3); }
  default <T> T select(final TryFunction4<? super A, ? super B, ? super C, ? super D, ? extends T> select) {
    return select.apply(first(), second(), third(), forth());
  }
  default Quadruple<D, A, B, C> shift() { return select((a, b, c, d) -> Tuple.of(d, a, b, c)); }
  @SuppressWarnings("unchecked")
  default Quadruple<A, B, C, D> where(TryPredicate4<? super A, ? super B, ? super C, ? super D> predicate) {
    return predicate.test(first(), second(), third(), forth()) ? this : (Quadruple<A, B, C, D>) OfFour.Empty.Default;
  }

  default Quadruple<A, B, C, D> peek(TryConsumer4<? super A, ? super B, ? super C, ? super D> cons) {
    cons.accept(first(), second(), third(), forth());
    return this;
  }
}
