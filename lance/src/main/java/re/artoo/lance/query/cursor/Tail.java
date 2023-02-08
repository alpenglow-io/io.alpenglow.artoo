package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.FetchException;

import java.util.Iterator;

public interface Tail<T> extends BackIterator<T> {
  default T scrollback() throws Throwable {
    return scrollback((index, element) -> element);
  }
  <R> R scrollback(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable;

  @Override
  default T prior() {
    try {
      return scrollback();
    } catch (Throwable throwable) {
      throw FetchException.of(throwable);
    }
  }

  Tail<T> tail();
}

