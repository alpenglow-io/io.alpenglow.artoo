package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntConsumer1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.FetchException;

import java.util.Iterator;

public interface Fetch<ELEMENT> extends Iterator<ELEMENT> {
  boolean hasElement() throws Throwable;

  <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable;
  <NEXT> NEXT thrownAt(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable;

  @Override
  default boolean hasNext() {
    try {
      return hasElement();
    } catch (Throwable throwable) {
      return FetchException.byThrowing("Can't check for next element, since exception occurred", throwable);
    }
  }

  @Override
  default ELEMENT next() {
    try {
      return hasNext() ? element((index, element) -> element) : FetchException.byThrowingCantFetchNextElement("fetch", "fetchable");
    } catch (Throwable throwable) {
      throw throwable instanceof RuntimeException it ? it : FetchException.of("Can't fetch next element, since exception occurred: %s".formatted(throwable.getMessage()), throwable);
    }
  }
}

