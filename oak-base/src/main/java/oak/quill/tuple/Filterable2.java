package oak.quill.tuple;

import oak.func.pre.Predicate2;

public interface Filterable2<V1, V2> extends Tuple {
  Tuple2<V1, V2> where(Predicate2<? super V1, ? super V2> filter);
}
