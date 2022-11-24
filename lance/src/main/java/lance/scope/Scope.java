package lance.scope;

import lance.func.Cons;
import lance.func.Func;
import lance.func.Suppl;
import lance.literator.Cursor;
import lance.query.One;

public abstract class Scope<T, $this extends Scope<T, $this>> implements One<T>, Suppl.MaybeSupplier<T> {
  protected int value;

  @Override
  public final Cursor<T> cursor() {
    return Cursor.maybe(get());
  }

  public final <R, $new extends Scope<R, $new>> $new let(final Func.MaybeFunction<? super T, ? extends $new> let) {
    return let.apply(get());
  }

  @SuppressWarnings("unchecked")
  public $this apply(final Cons.MaybeConsumer<Scope<T, $this>> apply) {
    return ($this) new Scope<T, $this>() {

      {
        apply.accept(this);
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
