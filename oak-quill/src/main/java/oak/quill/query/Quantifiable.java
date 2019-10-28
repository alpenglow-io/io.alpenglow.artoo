package oak.quill.query;

import oak.collect.cursor.Cursor;
import oak.func.pre.Predicate1;
import oak.quill.Structable;
import oak.quill.single.Single;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static java.util.Objects.nonNull;
import static oak.type.Nullability.nonNullable;

public interface Quantifiable<T> extends Structable<T> {
  default <C> Queryable<T> allOfType(final Class<C> type) {
    return new AllOfType<>(this, type);
  }

  default Single<Boolean> all(final Predicate1<T> filter) {
    return new All<>(this, nonNullable(filter, "filter"));
  }
}

final class AllOfType<T, C> implements Queryable<T> {
  private final Structable<T> structable;
  private final Class<C> type;

  @Contract(pure = true)
  AllOfType(final Structable<T> structable, final Class<C> type) {
    this.structable = structable;
    this.type = type;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    var all = true;
    final var cursor = structable.iterator();
    while (cursor.hasNext() && all) {
      all = type.isInstance(cursor.next());
    }
    return all ? structable.iterator() : Cursor.none();
  }
}

final class All<T> implements Single<Boolean> {
  private final Structable<T> structable;
  private final Predicate1<T> filter;

  @Contract(pure = true)
  All(final Structable<T> structable, final Predicate1<T> filter) {
    this.structable = structable;
    this.filter = filter;
  }

  @Override
  public final Boolean get() {
    var all = true;
    for (final var iterator = structable.iterator(); iterator.hasNext() && all;) {
      final var next = iterator.next();
      all = nonNull(next) && filter.test(next);
    }
    return all;
  }
}
