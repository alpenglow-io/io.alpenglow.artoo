package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("UnnecessaryBreak")
public record Distinct<ELEMENT>(Fetch<ELEMENT> fetch, Set<ELEMENT> elements, TryIntPredicate1<? super ELEMENT> condition) implements Cursor<ELEMENT> {
  public Distinct(Fetch<ELEMENT> fetch, TryIntPredicate1<? super ELEMENT> condition) {
    this(fetch, new HashSet<>(), condition);
  }

  @Override
  public boolean hasElement() throws Throwable {
    return fetch.hasElement();
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    final Value<NEXT> value = new Value<>();
    again:
    if (hasElement() && fetch.element((index, element) -> condition.invoke(index, element) && elements.add(element) && value.set(then.invoke(index, element)))) {
      return value.element;
    } else if (hasElement()) {
      break again;
    }

    return FetchException.byThrowingCantFetchNextElement("distinct", "distinctable");
  }

}
