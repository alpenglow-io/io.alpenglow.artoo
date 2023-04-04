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
  public boolean hasNext() {
    return fetch.hasNext();
  }

  @Override
  public <NEXT> NEXT next(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) {
    final Value<NEXT> value = new Value<>();
    again:
    if (hasNext() && fetch.next((index, element) -> condition.test(index, element) && elements.add(element) && value.set(then.apply(index, element)))) {
      return value.element;
    } else if (hasNext()) {
      break again;
    }

    return FetchException.byThrowingCantFetchNextElement("distinct", "distinctable");
  }

}
