package re.artoo.lance.query.cursor.mapper;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;
import re.artoo.lance.scope.Late;
import re.artoo.lance.scope.Let;

import java.util.Iterator;

public record Or<ELEMENT, PROBE extends Probe<ELEMENT>>(Probe<ELEMENT> probe, Let<PROBE> otherwise) implements Cursor<ELEMENT> {

  @Override
  public ELEMENT tick() throws Throwable {
    return isTickable() ? probe.tick() : null;
  }

  @Override
  public boolean isTickable() throws Throwable {
    boolean tickable = probe.isTickable();
    if (tickable) otherwise.flush();
    return tickable;
  }
}
