package oak.quill.single;

import oak.collect.cursor.Cursor;
import oak.func.sup.Supplier1;
import oak.quill.Structable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface StructableSingle<T> extends Structable<T>, Supplier1<T> {
  @NotNull
  @Override
  default Iterator<T> iterator() {
    return Cursor.ofNullable(this.get());
  }
}
