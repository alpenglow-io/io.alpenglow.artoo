package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public record Filter<ELEMENT>(Probe<ELEMENT> probe, AtomicInteger index, AtomicReference<ELEMENT> reference, TryIntPredicate1<? super ELEMENT> condition) implements Cursor<ELEMENT> {
  public Filter(Probe<ELEMENT> probe, TryIntPredicate1<? super ELEMENT> condition) {
    this(probe, new AtomicInteger(0), new AtomicReference<>(), condition);
  }
  @Override
  public ELEMENT fetch() throws Throwable {
    return canFetch() ? reference.getAndSet(null) : FetchException.byThrowing("Can't fetch next element on filter cursor (no more condition-met elements?)");
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (reference.get() != null) return true;
    if (!probe.canFetch()) return false;

    ELEMENT fetched = probe.fetch();
    if (condition.invoke(index.getAndIncrement(), fetched)) reference.set(fetched);

    return true;
  }
}
