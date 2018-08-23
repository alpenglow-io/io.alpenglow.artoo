package oak.type;

import oak.collect.cursor.Cursor;
import oak.func.sup.Supplier1;

import java.util.Iterator;

public interface IntValue<T> extends Iterable<T>, Supplier1<T> {
  @Override
  default Iterator<T> iterator() {
    return Cursor.maybe(this);
  }
}
