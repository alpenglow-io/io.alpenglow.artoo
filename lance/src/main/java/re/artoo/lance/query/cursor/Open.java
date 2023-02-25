package re.artoo.lance.query.cursor;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public record Open<ELEMENT>(ELEMENT[] elements, AtomicInteger index, AtomicReference<ELEMENT> reference, AtomicBoolean fetched) implements Cursor<ELEMENT> {
  @SafeVarargs
  public Open(ELEMENT... elements) {
    this(elements, new AtomicInteger(0), new AtomicReference<>(), new AtomicBoolean(true));
  }

  @Override
  public ELEMENT fetch() throws Throwable {
    try {
      return canFetch() ? reference.get() : FetchException.byThrowing("Can't fetch next element in open cursor (no more elements?)");
    } finally {
      fetched.set(true);
    }
  }

  @Override
  public Probe<ELEMENT> rewind() {
    return new Open<>(elements);
  }

  @Override
  public boolean canFetch() {
    if (!fetched.get()) return true;
    if (index.get() >= elements.length) return false;

    int index = this.index.getAndIncrement();
    reference.set(elements[index]);
    fetched.set(false);

    return true;
  }
}
