package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Pointer;
import re.artoo.lance.query.cursor.Probe;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public record Map<ELEMENT, RETURN>(Probe<ELEMENT> probe, AtomicInteger index, AtomicReference<RETURN> reference, AtomicBoolean fetched, TryIntFunction1<? super ELEMENT, ? extends RETURN> operation) implements Cursor<RETURN> {
  public Map(Probe<ELEMENT> probe, TryIntFunction1<? super ELEMENT, ? extends RETURN> operation) {
    this(probe, new AtomicInteger(0), new AtomicReference<>(), new AtomicBoolean(false), operation);
  }
  @Override
  public RETURN fetch() throws Throwable {
    return canFetch() ? reference.getAndSet(null) : FetchException.byThrowing("Can't fetch next element on map cursor (no more mappable elements?)");
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (!fetched.get()) return true;
    if (!probe.canFetch()) return false;

    reference.set(operation.invoke(index.getAndIncrement(), probe.fetch()));
    fetched.set(false);

    return true;
  }
}
