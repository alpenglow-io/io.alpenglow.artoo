package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Pointer;
import re.artoo.lance.query.cursor.Probe;

public record Map<ELEMENT, RETURN>(Probe<ELEMENT> probe, Pointer pointer, TryIntFunction1<? super ELEMENT, ? extends RETURN> operation) implements Cursor<RETURN> {
  public Map(Probe<ELEMENT> probe, TryIntFunction1<? super ELEMENT, ? extends RETURN> operation) {
    this(probe, Pointer.neverMove(), operation);
  }
  @Override
  public RETURN fetch() throws Throwable {
    return operation.invoke(pointer.next().index(), probe.fetch());
  }

  @Override
  public <AGAIN> Cursor<AGAIN> map(TryIntFunction1<? super RETURN, ? extends AGAIN> mapAgain) {
    return new Map<>(coalesce(), mapAgain);
  }

  @Override
  public boolean canFetch() throws Throwable {
    return probe.canFetch();
  }
}
