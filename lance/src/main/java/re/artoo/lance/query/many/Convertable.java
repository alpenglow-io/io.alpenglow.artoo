package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.value.Array;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryIntFunction;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.One;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static re.artoo.lance.experimental.value.Nullability.nonNullable;

public interface Convertable<ELEMENT> extends Queryable<ELEMENT> {
  default <KEY, VALUE, MAP extends Map<KEY, VALUE>> One<MAP> asMap(MAP map, TryFunction1<? super ELEMENT, ? extends KEY> key, TryFunction1<? super ELEMENT, ? extends VALUE> value) {
    return () -> cursor().collect(map, (index, element, mp) -> mp.put(key.invoke(element), value.invoke(element)));
  }

  default <KEY, VALUE> One<Map<KEY, VALUE>> asMap(TryFunction1<? super ELEMENT, ? extends KEY> key, TryFunction1<? super ELEMENT, ? extends VALUE> value) {
    return asMap(new ConcurrentHashMap<>(), key, value);
  }

  default <LIST extends List<ELEMENT>> One<LIST> asList(LIST list) {
    return () -> cursor().collect(list, (index, element, ls) -> ls.add(index, element));
  }

  default One<List<ELEMENT>> asList() {
    return asList(new CopyOnWriteArrayList<>());
  }

  default One<ELEMENT[]> asArray(TryIntFunction<ELEMENT[]> array) {
    return () -> cursor()
      .fold(Array.<ELEMENT>none(), Array::push)
      .map(folded -> folded.copyTo(array));
  }
}
