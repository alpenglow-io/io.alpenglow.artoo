package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.FetchException;

import java.util.Iterator;

public interface Probe<ELEMENT> extends Iterator<ELEMENT> {
  boolean isTickable() throws Throwable;
  ELEMENT tick() throws Throwable;

  @Override
  default ELEMENT next() {
    try {
      return tick();
    } catch (Throwable throwable) {
      throw FetchException.of(throwable);
    }
  }

  @Override
  default boolean hasNext() {
    try {
      return isTickable();
    } catch (Throwable throwable) {
      throw FetchException.of(throwable);
    }
  }

  default Probe<ELEMENT> rewind() { return this; }
}

