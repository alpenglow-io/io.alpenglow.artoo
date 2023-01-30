package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.FetchException;

import java.util.Iterator;

public interface Probe<T> extends Iterator<T> {
  default T tick() throws Throwable {
    return tick((index, element) -> element);
  }
  <R> R tick(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable;

  default Probe<T> reverse() {
    return this;
  }

  @Override
  default T next() {
    try {
      return tick();
    } catch (Throwable throwable) {
      throw FetchException.of(throwable);
    }
  }
}

