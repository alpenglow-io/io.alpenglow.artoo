package oak.query.one;

import oak.cursor.Cursor;
import oak.func.Cons;
import oak.func.Exec;
import oak.query.Queryable;

public interface Hookable<T> extends Queryable<T> {
  default One<T> sleek(final Exec exec) {
    return () -> {
      final var value = this.iterator().next();
      exec.execute();
      return Cursor.of(value);
    };
  }

  default One<T> peek(final Cons<? super T> peek) {
    return () -> {
      final var value = this.iterator().next();
      peek.accept(value);
      return Cursor.of(value);
    };
  }
}
