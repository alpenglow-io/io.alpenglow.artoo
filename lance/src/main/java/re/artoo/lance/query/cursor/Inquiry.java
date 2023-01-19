package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.FetchException;

import java.util.Iterator;

public interface Inquiry<T> extends Iterator<T> {
  default T traverse() throws Throwable {
    return traverse((index, element) -> element);
  }
  <R> R traverse(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable;

  default Inquiry<T> reversal() {
    return this;
  }

  @Override
  default T next() {
    try {
      return traverse();
    } catch (Throwable throwable) {
      throw FetchException.of(throwable);
    }
  }
}

