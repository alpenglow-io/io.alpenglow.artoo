package oak.quill.tuple;

import oak.func.con.Consumer3;
import oak.func.fun.Function3;
import oak.quill.single.Nullable;

public interface Projection3<V1, V2, V3> extends Tuple {
  <R> Nullable<R> select(Function3<? super V1, ? super V2, ? super V3, ? extends R> map);
  Tuple3<V1, V2, V3> peek(Consumer3<? super V1, ? super V2, ? super V3> peek);
  <T extends Tuple> T selection(Function3<? super V1, ? super V2, ? super V3, ? extends T> flatMap);
}
