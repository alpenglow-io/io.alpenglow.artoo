package lance.literator.cursor;

import lance.literator.Cursor;
import lance.literator.Literator;
import lance.literator.cursor.routine.Routine;

public interface Transformable<T> extends Literator<T> {
  default <R, C extends Cursor<R>> C to(Routine<T, C> routine) {
    return this.as(routine);
  }
  <R> R as(Routine<T, R> routine);
}

