package lance.scope;

import lance.func.Cons;
import lance.func.Func;
import lance.func.Pred;
import lance.func.Suppl.MaybeSupplier;
import lance.query.One;

import java.util.Optional;

@SuppressWarnings({"unchecked", "SwitchStatementWithTooFewBranches"})
public final class Maybe<T> {
  private static final Maybe<Object> empty = new Maybe<>();

  private final Throwable left;
  final T right;

  private Maybe() { this(null, null); }
  private Maybe(Throwable left) { this(left, null); }
  private Maybe(T right) { this(null, right); }
  private Maybe(final Throwable left, final T right) {
    this.left = left;
    this.right = right;
  }

  public <R> Maybe<R> map(Func.MaybeFunction<? super T, ? extends R> func) {
    return switch (right) {
      case null -> empty();
      default -> new Maybe<>(func.apply(right));
    };
  }

  public <R> Maybe<R> flatMap(Func.MaybeFunction<? super T, ? extends Maybe<R>> func) {
    return switch (right) {
      case null -> empty();
      default -> func.apply(right);
    };
  }

  public Maybe<T> filter(Pred.MaybePredicate<? super T> pred) {
    return switch (right) {
      case null -> this;
      default -> pred.verify(right).or(() -> false).otherwise("Can't be thrown", IllegalStateException::new)
        ? this
        : empty();
    };
  }

  public Maybe<T> peek(Cons.MaybeConsumer<? super T> cons) {
    if (right != null) cons.accept(right);
    return this;
  }

  public Maybe<T> exceptionally(Cons.MaybeConsumer<? super Throwable> cons) {
    return switch (left) {
      case null -> this;
      default -> {
        cons.accept(left);
        yield this;
      }
    };
  }

  public T nullable() {
    return switch (right) {
      case null -> throw new ScopeException(left);
      default -> right;
    };
  }

  public Optional<T> asOptional() { return Optional.ofNullable(right); }
  public One<T> asOne() { return right != null ? One.lone(right) : One.gone("Can't retrieve anything", message -> new ScopeException(message, left)); }

  public T otherwise(String message, Func.MaybeBiFunction<? super String, ? super Throwable, ? extends RuntimeException> func) {
    return switch (left) {
      case null -> right;
      default -> throw func.apply(message, left);
    };
  }

  public T otherwise(MaybeSupplier<T> value) {
    return switch (right) {
      case null -> value.get();
      default -> right;
    };
  }

  public Maybe<T> or(MaybeSupplier<? extends T> other) {
    return switch (right) {
      case null -> new Maybe<>(other.get());
      default -> this;
    };
  }

  public Maybe<T> or(String message, Func.MaybeFunction<? super String, ? extends RuntimeException> func) {
    return switch (right) {
      case null -> new Maybe<>(func.apply(message));
      default -> this;
    };
  }

  public static <T> Maybe<T> empty() { return (Maybe<T>) empty;}

  public static <T> Maybe<T> value(T value) {
    return switch (value) {
      case null -> empty();
      default -> new Maybe<>(value);
    };
  }

  public static <T> Maybe<T> error(Throwable throwable) {
    return switch (throwable) {
      case null -> empty();
      default -> new Maybe<>(throwable);
    };
  }
}
