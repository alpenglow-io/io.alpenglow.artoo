package re.artoo.lance.query.cursor;

import re.artoo.lance.query.FetchException;

import java.util.Iterator;

public interface Probe<ELEMENT> extends Iterator<ELEMENT> {
  default boolean canFetch() throws Throwable {
    return hasNext();
  }

  default ELEMENT fetch() throws Throwable {
    return null;
  }

  default Next<ELEMENT> nextElement() {
    return Next.nothing();
  }

  @Override
  default ELEMENT next() {
    var next = nextElement();
    while (next instanceof Nothing && hasNext()) next = nextElement();
    return switch (next) {
      case Success<ELEMENT> success -> success.element();
      case Failure<ELEMENT> failure -> throw failure.exception();
      case Nothing ignored -> FetchException.byThrowing("Can't fetch next element from iterator (no more elements?)");
    };
  }

  @Override
  default boolean hasNext() {
    try {
      return canFetch();
    } catch (Throwable throwable) {
      throw FetchException.with(throwable);
    }
  }

  default Probe<ELEMENT> rewind() {
    return this;
  }
}

