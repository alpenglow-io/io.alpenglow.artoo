package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Lazy<T> implements Cursor<T> {
  private final TrySupplier1<T> element;
  private final AtomicBoolean fetched;

  public Lazy(TrySupplier1<T> element) { this(element, new AtomicBoolean(false)); }
  private Lazy(TrySupplier1<T> element, AtomicBoolean fetched) {
    this.element = element;
    this.fetched = fetched;
  }

  @Override
  public <R> R tick(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    try {
      return fetch.invoke(0, element.invoke());
    } finally {
      fetched.set(true);
    }
  }

  @Override
  public boolean hasNext() {
    return !fetched.get();
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return null;
  }
}
