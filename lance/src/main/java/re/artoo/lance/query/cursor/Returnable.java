package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

public sealed interface Returnable<ELEMENT> extends Probe<ELEMENT> permits Cursor {
  default <RETURN> Cursor<RETURN> yield(TryIntFunction1<? super ELEMENT, ? extends RETURN> returns) {
    return null;
  }
}

final class Yield<ELEMENT, RETURN> implements Cursor<RETURN> {
  private final Probe<? extends ELEMENT> probe;
  private final TryIntFunction1<? super ELEMENT, ? extends RETURN> returns;

  Yield(Probe<? extends ELEMENT> probe, TryIntFunction1<? super ELEMENT, ? extends RETURN> returns) {
    this.probe = probe;
    this.returns = returns;
  }

  @Override
  public <TO> TO as(Routine<RETURN, TO> routine) {
    return null;
  }

  @Override
  public <R> R tick(TryIntFunction1<? super RETURN, ? extends R> fetch) throws Throwable {
    return probe.tick((index, it) -> fetch.invoke(index, returns.invoke(index, it)));
  }

  @Override
  public boolean hasNext() {
    return false;
  }
}
