package oak.type;

import oak.func.sup.Supplier1;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.collect.cursor.Cursor.maybe;

@FunctionalInterface
public interface As<T> extends Iterable<T>, Supplier1<T> {
  @NotNull
  @Override
  default Iterator<T> iterator() { return maybe(get()); }
}
