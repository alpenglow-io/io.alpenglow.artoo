package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public record PresenceOnly<ELEMENT>(Probe<ELEMENT> probe, AtomicReference<ELEMENT> reference) implements Cursor<ELEMENT> {
  public PresenceOnly(Probe<ELEMENT> probe) {
    this(probe, new AtomicReference<>());
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (reference.get() != null) return true;

    while (probe.canFetch() && reference.get() == null) {
      ELEMENT element = probe.fetch();
      reference.set(element);
    }

    return reference.get() != null;
  }

  @Override
  public ELEMENT fetch() throws Throwable {
    return canFetch() ? reference.getAndSet(null) : FetchException.byThrowing("Can't fetch next element on coalesce operation");
  }
}
