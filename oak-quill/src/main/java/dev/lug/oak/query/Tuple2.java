package dev.lug.oak.query;

import dev.lug.oak.collect.cursor.Cursor2;
import dev.lug.oak.func.as.As2;
import dev.lug.oak.query.tuple2.Projectable2;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@SuppressWarnings("UnusedReturnValue")
public interface Tuple2<V1, V2> extends Projectable2<V1, V2> {
}

final class Pair<V1, V2> implements Tuple2<V1, V2> {
  private final V1 value1;
  private final V2 value2;

  Pair(V1 value1, V2 value2) {
    this.value1 = value1;
    this.value2 = value2;
  }

  @NotNull
  @Override
  public final Iterator<As2<V1, V2>> iterator() {
    return Cursor2.once(value1, value2);
  }
}

final class None2<V1, V2> implements Tuple2<V1, V2> {
  @NotNull
  @Override
  public Iterator<As2<V1, V2>> iterator() {
    return Cursor2.none();
  }
}
