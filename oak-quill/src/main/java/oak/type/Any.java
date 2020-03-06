package oak.type;

import oak.cursor.Cursor;
import oak.func.Suppl;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Any<T> extends Iterable<T>, Suppl<T> {

  @NotNull
  @Override
  default Iterator<T> iterator() {
    return Cursor.of(this.get());
  }
}
