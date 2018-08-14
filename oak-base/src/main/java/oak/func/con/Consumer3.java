package oak.func.con;

import oak.func.Functional;

public interface Consumer3<T1, T2, T3> extends Functional.Con {
  void accept(T1 t1, T2 t2, T3 t3);
}
