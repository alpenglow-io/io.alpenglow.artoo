package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public record Flat<ELEMENT>(Probe<Probe<ELEMENT>> probe, Reference<Probe<ELEMENT>> reference, Reference<ELEMENT> flatReference) implements Cursor<ELEMENT> {
  public Flat(Probe<Probe<ELEMENT>> probe) {
    this(probe, Reference.iterative(), Reference.iterative());
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (flatReference.isNotFetched()) return true;
    var source = reference.element();
    if (source != null && !source.canFetch() && !probe.canFetch()) return false;

    if ((source == null && probe.canFetch()) || (source != null && !source.canFetch() && probe.canFetch())) {
      source = probe.fetch();
      reference.element(source);
    }

    if (source != null && source.canFetch()) {
      flatReference.element(source.fetch());
    }

    return true;
  }
  @Override
  public ELEMENT fetch() throws Throwable {
    return canFetch() ? flatReference.element() : FetchException.byThrowing("Can't fetch next element from cursor (no more flattable elements?)");
  }

}
