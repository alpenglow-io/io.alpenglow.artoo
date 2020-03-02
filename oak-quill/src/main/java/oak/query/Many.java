package oak.query;

import oak.func.Suppl;
import oak.query.many.Many;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

final class Repeat<T> implements Many<T> {
  private final Suppl<? extends T> supplier;
  private final int count;

  @Contract(pure = true)
  Repeat(final Suppl<? extends T> supplier, final int count) {
    this.supplier = supplier;
    this.count = count;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var array = new ArrayList<T>();
    for (var index = 0; index < count; index++) {
      array.add(supplier.get());
    }
    return array.iterator();
  }
}

