package oak.quill.query.array;

import oak.collect.cursor.Cursor;
import oak.quill.single.Nullable;
import oak.quill.query.Uniquable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static java.util.Objects.requireNonNull;

public interface UniquableArray<T> extends Uniquable<T>, StructableArray<T> {
  @Override
  default Nullable<T> at(final int index) {
    return new At<>(this, index);
  }

  @Override
  default Nullable<T> first() {
    return new At<>(this, 0);
  }

  @Override
  default Nullable<T> last() {
    return new At<>(this, this.get().length - 1);
  }

  @Override
  default oak.quill.single.Single<T> single() {
    return new Single<>(this);
  }
}

final class At<T> implements Nullable<T> {
  private final StructableArray<T> array;
  private final int index;

  @Contract(pure = true)
  At(final StructableArray<T> array, final int index) {
    this.array = array;
    this.index = index;
  }

  @Override
  @NotNull
  public Iterator<T> iterator() {
    return array.get().length > index ? Cursor.of(array.get()[index]) : Cursor.none();
  }
}

final class Single<T> implements oak.quill.single.Single<T> {
  private final StructableArray<T> array;

  @Contract(pure = true)
  Single(final StructableArray<T> array) {this.array = array;}

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    return array.get().length == 1 ? Cursor.of(array.get()[0]) : Cursor.none();
  }
}
