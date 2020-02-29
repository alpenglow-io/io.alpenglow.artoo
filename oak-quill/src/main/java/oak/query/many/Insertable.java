package oak.query.many;

import oak.func.$2.IntCons;
import oak.query.Many;
import oak.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static java.util.Arrays.copyOf;
import static java.util.Collections.addAll;
import static oak.func.$2.IntCons.nothing;
import static oak.type.Nullability.nonNullable;

public interface Insertable<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> insert(final T... values) {
    return new Insert<>(this, nothing(), nonNullable(values, "values"));
  }
}

// TODO: where and select are missing
final class Insert<T> implements Many<T> {
  private final Queryable<T> queryable;
  private final IntCons<? super T> peek;
  private final T[] values;

  @SafeVarargs
  Insert(final Queryable<T> queryable, final IntCons<? super T> peek, final T... values) {
    this.queryable = queryable;
    this.peek = peek;
    this.values = values;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var array = new ArrayList<T>();
    var index = 0;
    for (var cursor = queryable.iterator(); cursor.hasNext(); index++) {
      var it = cursor.next();
      peek.acceptInt(index, it);
      array.add(it);
    }
    addAll(array, copyOf(values, values.length));
    return array.iterator();
  }
}

