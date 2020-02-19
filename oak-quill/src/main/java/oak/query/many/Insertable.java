package oak.query.many;

import oak.query.Many;
import oak.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public interface Insertable<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> insert(final T... values) {
    return new Insert<>(this, values);
  }

  default <Q extends Queryable<T>> Many<T> insert(final Q queryable) {
    return new InsertQueryable<>(this, queryable);
  }
}

final class Insert<T> implements Many<T> {
  private final Queryable<T> queryable;
  private final T[] values;

  Insert(final Queryable<T> queryable, final T[] values) {
    this.queryable = queryable;
    this.values = values;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var array = new ArrayList<T>();
    for (final var element : queryable) array.add(element);
    Collections.addAll(array, values);
    return array.iterator();
  }
}

final class InsertQueryable<T> implements Many<T> {
  private final Queryable<T> queryable;
  private final Queryable<T> q;

  InsertQueryable(final Queryable<T> queryable, final Queryable<T> q) {
    this.queryable = queryable;
    this.q = q;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var array = new ArrayList<T>();
    for (final var element : queryable) array.add(element);
    for (final var element : q) array.add(element);
    return array.iterator();
  }
}
