package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public record Filter<ELEMENT>(Probe<ELEMENT> probe, Atom<ELEMENT> atom, TryIntPredicate1<? super ELEMENT> condition) implements Cursor<ELEMENT> {
  public Filter(Probe<ELEMENT> probe, TryIntPredicate1<? super ELEMENT> condition) {
    this(probe, Atom.reference(), condition);
  }
  @Override
  public ELEMENT fetch() throws Throwable {
    return canFetch() ? atom.elementThenFetched() : FetchException.byThrowing("Can't fetch next element on filter cursor (no more condition-met elements?)");
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (atom.isNotFetched()) return true;
    if (!probe.canFetch()) return false;

    ELEMENT element = probe.fetch();
    if (condition.invoke(atom.indexThenInc(), element)) atom.element(element);

    return true;
  }
}
