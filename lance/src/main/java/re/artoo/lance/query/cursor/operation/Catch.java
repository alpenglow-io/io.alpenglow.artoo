package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;
import re.artoo.lance.query.cursor.operation.atom.Atom;

public record Catch<ELEMENT>(Probe<ELEMENT> probe, Atom<ELEMENT> atom, TryConsumer1<? super Throwable> fallback) implements Cursor<ELEMENT> {
  public Catch(Probe<ELEMENT> probe, TryConsumer1<? super Throwable> fallback) {
    this(probe, Atom.reference(), fallback);
  }

  @Override
  public ELEMENT fetch() throws Throwable {
      return canFetch() ? atom.elementThenFetched() : FetchException.byThrowing("Can't fetch next element from catch cursor (no more catchable elements?)");
  }

  @Override
  public boolean canFetch() {
    try {
      if (atom.isNotFetched()) return true;
      if (!probe.canFetch()) return false;

      atom.element(probe.fetch());

      return atom.isNotFetched();
    } catch (Throwable throwable) {
      fallback.accept(throwable);
      atom.element(null);
      return atom.isNotFetched();
    }
  }
}
