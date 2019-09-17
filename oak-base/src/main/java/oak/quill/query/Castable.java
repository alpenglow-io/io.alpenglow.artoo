package oak.quill.query;

import oak.collect.Array;
import oak.collect.cursor.Cursor;
import oak.quill.Structable;
import oak.quill.query.spec.QueryableNumbers;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static java.util.Objects.isNull;

public interface Castable<T> extends Structable<T> {
  default QueryableNumbers<Double> asDoubles() {
    return new AsNumbers<>(this, Double.class);
  }
  default QueryableNumbers<Integer> asIntegers() {
    return new AsNumbers<>(this, Integer.class);
  }
  default QueryableNumbers<Long> asLongs() {
    return new AsNumbers<>(this, Long.class);
  }
}

final class AsNumbers<T, N extends Number> implements QueryableNumbers<N> {
  private final Structable<T> structable;
  private final Class<N> type;

  @Contract(pure = true)
  AsNumbers(final Structable<T> structable, final Class<N> type) {
    this.structable = structable;
    this.type = type;
  }

  @NotNull
  @Override
  public final Iterator<N> iterator() {
    var all = true;
    final var array = Array.<N>of();
    final var cursor = structable.iterator();
    while (cursor.hasNext() && all) {
      final var next = cursor.next();
      all = isNull(next) || type.isInstance(next);
      if (all) {
        array.add(type.cast(next));
      }
    }
    return all ? Cursor.of(array.get()) : Cursor.none();
  }

  @Override
  public final Class<N> get() {
    return type;
  }
}
