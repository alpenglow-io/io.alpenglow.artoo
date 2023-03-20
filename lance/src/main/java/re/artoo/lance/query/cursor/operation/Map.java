package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Next;
import re.artoo.lance.query.cursor.Probe;
import re.artoo.lance.query.cursor.operation.atom.Atom;

public record Map<ELEMENT, RETURN>(Probe<ELEMENT> probe, Atom<ELEMENT> current, TryIntFunction1<? super ELEMENT, ? extends RETURN> operation) implements Cursor<RETURN> {
  public Map(Probe<ELEMENT> probe, TryIntFunction1<? super ELEMENT, ? extends RETURN> operation) {
    this(probe, Atom.reference(), operation);
  }

  @Override
  public Next<RETURN> nextElement() {
    try {
      return hasNext() ? Next.success(operation.invoke(current.indexThenInc(), current.elementThenFetched())) : Next.failure(FetchException.byThrowing("Can't fetch next element from cursor (no more mappable elements?)"));
    } catch (Throwable throwable) {
      return Next.failure(throwable);
    }
  }

  @Override
  public boolean hasNext() {
    return current.isNotFetched() || probe.hasNext();
  }
}
