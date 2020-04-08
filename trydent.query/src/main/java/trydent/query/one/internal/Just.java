package trydent.query.one.internal;

import trydent.cursor.Cursor;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class Just<T> implements Projectable<T>, Filterable<T>, Either<T> {
  private final T value;

  public Just(T value) {this.value = value;}

  @NotNull
  @Override
  public Iterator<T> iterator() {
    return Cursor.of(value);
  }
}
