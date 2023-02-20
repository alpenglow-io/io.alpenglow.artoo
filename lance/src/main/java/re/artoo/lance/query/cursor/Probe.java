package re.artoo.lance.query.cursor;

import re.artoo.lance.query.FetchException;

import java.util.Iterator;

public interface Probe<ELEMENT> extends Iterator<ELEMENT> {
  boolean canFetch() throws Throwable;
  ELEMENT fetch() throws Throwable;

  @Override
  default ELEMENT next() {
    try {
      return fetch();
    } catch (Throwable throwable) {
      throw FetchException.of(throwable);
    }
  }

  @Override
  default boolean hasNext() {
    try {
      return canFetch();
    } catch (Throwable throwable) {
      throw FetchException.of(throwable);
    }
  }

  default Probe<ELEMENT> rewind() { return this; }
}

