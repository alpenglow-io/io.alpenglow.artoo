package re.artoo.lance.query.cursor.mapper;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;

public record Map<ELEMENT, RETURN>(Probe<ELEMENT> probe, TryIntFunction1<? super ELEMENT, ? extends RETURN> map) implements Cursor<RETURN> {
  @Override
  public RETURN tick() throws Throwable {
    return map.invoke(0, probe.tick());
  }

  @Override
  public <AGAIN> Cursor<AGAIN> map(TryIntFunction1<? super RETURN, ? extends AGAIN> mapAgain) {
    return new Map<>(coalesce(), mapAgain);
  }

  @Override
  public boolean isTickable() throws Throwable {
    return probe.isTickable();
  }
}
