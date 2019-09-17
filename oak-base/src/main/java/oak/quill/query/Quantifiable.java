package oak.quill.query;

import oak.collect.cursor.Cursor;
import oak.quill.Structable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Quantifiable<T> extends Structable<T> {
  default <C> Queryable<T> allOfType(final Class<C> type) {
    return new AllOfType<>(this, type);
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
