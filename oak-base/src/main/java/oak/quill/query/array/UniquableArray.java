package oak.quill.query.array;

import oak.quill.query.Uniquable;
import oak.quill.single.Nullable;
import oak.quill.single.Single;
import org.jetbrains.annotations.Contract;

public interface UniquableArray<T> extends Uniquable<T>, StructableArray<T> {
  @Override
  default Nullable<T> elementAt(final int index) {
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
  default Single<T> single() {
    return new Just<>(this);
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
  @org.jetbrains.annotations.Nullable
  public final T get() {
    return array.get().length > index ? array.get()[index] : null;
  }
}

final class Just<T> implements Single<T> {
  private final StructableArray<T> array;

  @Contract(pure = true)
  Just(final StructableArray<T> array) {this.array = array;}

  @Override
  @org.jetbrains.annotations.Nullable
  public final T get() {
    return switch (array.get().length) {
      case 0 -> throw new IllegalStateException("Query can't be satisfied, Queryable has no elements.");
      case 1 -> throw new IllegalStateException("Query can't be satisfied, Queryable has more than one elements.");
      default -> array.get()[0];
    };
  }
}
