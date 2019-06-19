package oak.type;

import oak.collect.cursor.Cursor;
import oak.func.sup.Supplier1;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@FunctionalInterface
public interface Value<T> extends Iterable<T>, Supplier1<T> {
  @NotNull
  @Override
  default Iterator<T> iterator() {
    return Cursor.maybe(this.get());
  }
}
