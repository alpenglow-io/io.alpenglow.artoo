package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;

import java.util.Iterator;

public interface Fetchable<ELEMENT> extends Iterator<ELEMENT> {
  boolean canFetch() throws Throwable;

  <NEXT> NEXT fetch(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable;

  @Override
  default boolean hasNext() {
    try {
      return canFetch();
    } catch (Throwable throwable) {
      throw throwable instanceof RuntimeException exception ? exception : Exception.of("Can't check fot the next element: \n\t%s".formatted(throwable.getMessage()), throwable);
    }
  }

  @Override
  default ELEMENT next() {
    try {
      return hasNext() ? fetch((index, element) -> element) : Exception.of("fetch", "fetchable");
    } catch (Throwable throwable) {
      throw throwable instanceof RuntimeException exception ? exception : Exception.of("Can't fetch next element, since exception occurred: %s".formatted(throwable.getMessage()), throwable);
    }
  }

  final class Exception extends RuntimeException {
    private Exception(final Throwable throwable) {
      super(throwable);
    }

    private Exception(final String s, final Throwable object) {
      super(s, object);
    }

    private Exception(String message) {
      super(message);
    }

    public static Exception with(Throwable throwable) {
      return new Exception(throwable);
    }

    public static Exception withMessage(String message) {
      return new Exception(message);
    }

    public static Exception of(String message, Throwable throwable) {
      return new Exception(message, throwable);
    }

    public static <RETURN> RETURN byThrowing(Throwable throwable) {
      throw new Exception(throwable);
    }

    public static <RETURN> RETURN byThrowing(String message, Throwable throwable) {
      throw new Exception(message, throwable);
    }

    public static <RETURN> RETURN byThrowing(String message) {
      throw new Exception(message);
    }

    public static <RETURN> RETURN of(String operation, String adjective) {
      throw new Exception("Can't fetch next element for %s origin (no more %s elements?)".formatted(operation, adjective));
    }
  }
}

