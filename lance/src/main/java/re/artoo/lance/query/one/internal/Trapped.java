package re.artoo.lance.query.one.internal;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.One;
import re.artoo.lance.query.one.Exceptionable;

import static re.artoo.lance.func.TryPredicate1.not;

public record Trapped<T>(String message, TryFunction1<? super String, ? extends Throwable> operation,
                         Cursor<T> cursor) implements Exceptionable.Trapping<T> {
  @Override
  public One<T> when(TryPredicate1<? super T> condition) {
    return () -> cursor.filter(not(condition)).or(message, operation);
  }
}
