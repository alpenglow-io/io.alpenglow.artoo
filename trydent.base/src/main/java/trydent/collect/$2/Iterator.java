package trydent.collect.$2;

import trydent.cursor.$2.Cursor;
import trydent.union.$2.Union;
import org.jetbrains.annotations.NotNull;

public interface Iterator<V1, V2> extends java.util.Iterator<Union<V1, V2>> {
  @NotNull
  static <T1, T2> Iterator<T1, T2> from(final java.util.Iterator<Union<T1, T2>> iterator) {
    return Cursor.from(iterator);
  }
}
