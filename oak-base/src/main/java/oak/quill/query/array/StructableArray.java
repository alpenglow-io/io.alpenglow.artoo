package oak.quill.query.array;

import oak.collect.cursor.Cursor;
import oak.func.sup.Supplier1;
import oak.quill.Structable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface StructableArray<T> extends Structable<T>, Supplier1<T[]> {
  @NotNull
  @Override
  default Iterator<T> iterator() {
    return Cursor.of(this.get());
  }
}
