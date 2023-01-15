package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Lazy<T> implements Cursor<T> {
  private final TrySupplier1<T> element;
  private final AtomicBoolean exausted;

  public Lazy(TrySupplier1<T> element) { this(element, new AtomicBoolean(false)); }
  private Lazy(TrySupplier1<T> element, AtomicBoolean exausted) {
    this.element = element;
    this.exausted = exausted;
  }

  @Override
  public <R> R fetch(TryIntFunction1<? super T, ? extends R> detach) throws Throwable {
    return detach.invoke(index, elements[index++]);
  }

  @Override
  public boolean hasNext() {
    return index < elements.length;
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onArray().apply(Arrays.copyOf(elements, elements.length));
  }
}
