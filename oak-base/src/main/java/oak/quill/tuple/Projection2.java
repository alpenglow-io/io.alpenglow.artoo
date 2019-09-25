package oak.quill.tuple;

import oak.func.con.Consumer2;
import oak.func.fun.Function2;
import oak.quill.single.Nullable;

public interface Projection2<V1, V2> extends Tuple {
  <R> Nullable<R> select(Function2<? super V1, ? super V2, ? extends R> map);
  Tuple2<V1, V2> peek(Consumer2<? super V1, ? super V2> peek);
  <T extends Tuple> Nullable<T> selection(Function2<? super V1, ? super V2, ? extends T> flatMap);
}
