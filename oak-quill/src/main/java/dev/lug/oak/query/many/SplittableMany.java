package dev.lug.oak.query.many;

import dev.lug.oak.collect.Many;
import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.query.Splittable;
import dev.lug.oak.query.Structable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface SplittableMany<T> extends Splittable<T, Many<T>> {
  @Override
  default Queryable<Many<T>[]> splitAt(int... indexes) {
    return new SplitAt<>(this, indexes);
  }
}

@SuppressWarnings("FieldCanBeLocal")
final class SplitAt<T> implements Queryable<Many<T>[]> {
  private final Structable<T> structable;
  private final int[] indexes;

  SplitAt(final Structable<T> structable, final int[] indexes) {
    this.structable = structable;
    this.indexes = indexes;
  }

  @NotNull
  @Override
  // TODO: need a test, can't be implemented at mentula canis
  public final Iterator<Many<T>[]> iterator() {
    return Cursor.none();
  }
}
