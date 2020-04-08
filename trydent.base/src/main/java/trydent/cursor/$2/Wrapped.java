package trydent.cursor.$2;

import trydent.func.$2.Func;
import trydent.union.$2.Union;
import org.jetbrains.annotations.Contract;

final class Wrapped<T1, T2> implements Cursor<T1, T2> {
  private final java.util.Iterator<Union<T1, T2>> iterator;

  @Contract(pure = true)
  Wrapped(final java.util.Iterator<Union<T1, T2>> iterator) {this.iterator = iterator;}

  @Override
  public final boolean hasNext() {
    return iterator.hasNext();
  }

  @Override
  public final <T> T as(Func<T1, T2, T> as) {
    return iterator.next().as(as);
  }
}
