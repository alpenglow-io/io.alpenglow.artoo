package oak.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Varchar {
  @NotNull
  @Contract(value = "_ -> new", pure = true)
  static Varchar string(final String value) {
    return new VarcharImpl(value);
  }

  default Varchar format(Object... objects) {
    return new Format(this, objects);
  }
}

final class VarcharImpl implements Varchar {
  private final String value;

  @Contract(pure = true)
  VarcharImpl(final String value) {
    this.value = value;
  }


}

final class Format implements Varchar {
  private final Varchar varchar;
  private final Object[] objects;

  @Contract(pure = true)
  Format(Varchar varchar, Object[] objects) {
    this.varchar = varchar;
    this.objects = objects;
  }

  @Override
  public final String toString() {
    return String.format(varchar.toString(), objects);
  }
}
