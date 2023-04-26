package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.OperationException;

import java.util.Iterator;

public interface Fetch<ELEMENT> extends Iterator<ELEMENT> {
  boolean hasElement() throws Throwable;

  <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable;
  default <NEXT> NEXT thrownAt(TryIntFunction<? extends NEXT> then) throws Throwable {
    return null;
  }

  @Override
  default boolean hasNext() {
    try {
      return hasElement();
    } catch (Throwable throwable) {
      return OperationException.byThrowing("Can't check for next element, since exception occurred", throwable);
    }
  }

  @Override
  default ELEMENT next() {
    try {
      return hasNext() ? element((index, element) -> element) : OperationException.byThrowingCantFetchNextElement("fetch", "fetchable");
    } catch (Throwable throwable) {
      return OperationException.byThrowing("Can't fetch next element, since exception occurred", throwable);
    }
  }
}

