package re.artoo.lance.experimental.value;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.One;

public abstract class Scope<T, $this extends Scope<T, $this>> implements One<T>, TrySupplier1<T> {
  protected int value;

  public static void main(String[] args) {
    class Test extends Scope<Integer, Test> {
      @Override
      public Integer invoke() {
        return 12;
      }
    }
  }

  @Override
  public final Cursor<T> cursor() {
    return Cursor.maybe(get());
  }

  public final <R, $new extends Scope<R, $new>> $new let(final TryFunction1<? super T, ? extends $new> let) {
    return let.apply(get());
  }

  @SuppressWarnings("unchecked")
  public $this apply(final TryConsumer1<Scope<T, $this>> apply) {
    return ($this) new Scope<T, $this>() {

      {
        apply.accept(this);
      }


      @Override
      public T invoke() throws Throwable {
        return Scope.this.invoke();
      }
    };
  }
}
