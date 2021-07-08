package io.artoo.lance.scope;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.One;

public abstract class Scope<T, $this extends Scope<T, $this>> implements One<T>, Suppl.Uni<T> {
  protected int value;

  @Override
  public final Cursor<T> cursor() {
    return Cursor.maybe(get());
  }

  public final <R, $new extends Scope<R, $new>> $new let(final Func.Uni<? super T, ? extends $new> let) {
    return let.apply(get());
  }

  @SuppressWarnings("unchecked")
  public $this apply(final Cons.Uni<Scope<T, $this>> apply) {
    return ($this) new Scope<T, $this>() {

      {
        apply.apply(this);
      }


      @Override
      public T tryGet() throws Throwable {
        return Scope.this.tryGet();
      }
    };
  }

  public static void main(String[] args) {
    class Test extends Scope<Integer, Test> {
      @Override
      public Integer tryGet() {
        return 12;
      }
    }
  }
}
