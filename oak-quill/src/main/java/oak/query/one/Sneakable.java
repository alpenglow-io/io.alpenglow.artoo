package oak.query.one;

import oak.func.$2.IntCons;
import oak.query.Queryable;

public interface Sneakable<T> extends Queryable<T> {
  default IntCons<? super T> sneak() {
    return IntCons.nothing();
  }
}
