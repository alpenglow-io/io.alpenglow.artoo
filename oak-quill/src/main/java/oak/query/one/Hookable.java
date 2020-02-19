package oak.query.one;

import oak.cursor.Cursor;
import oak.func.Con;
import oak.func.Exe;
import oak.query.Queryable;

public interface Hookable<T> extends Queryable<T> {
  default One<T> sleek(final Exe exe) {
    return () -> {
      final var value = this.iterator().next();
      exe.execute();
      return Cursor.ofNullable(value);
    };
  }

  default One<T> peek(final Con<? super T> peek) {
    return () -> {
      final var value = this.iterator().next();
      peek.accept(value);
      return Cursor.ofNullable(value);
    };
  }
}
