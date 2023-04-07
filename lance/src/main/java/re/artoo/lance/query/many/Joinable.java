package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;

@SuppressWarnings({"unused", "unchecked"})
public interface Joinable<FIRST> extends Queryable<FIRST> {
  /*
  default <SECOND, QUERYABLE extends Queryable<SECOND>> Join<FIRST, SECOND> outerJoin(QUERYABLE queryable) {
    return new OuterJoin<>(cursor(), queryable.cursor());
  }

  default <SECOND> Join<FIRST, SECOND> outerJoin(SECOND... elements) {
    return new OuterJoin<>(cursor(), Cursor.open(elements));
  }

  default <SECOND, QUERYABLE extends Queryable<SECOND>> Join<FIRST, SECOND> leftJoin(QUERYABLE queryable) {
    return new LeftJoin<>(cursor(), queryable.cursor());
  }

  default <SECOND, QUERYABLE extends Queryable<SECOND>> Join<FIRST, SECOND> rightJoin(QUERYABLE queryable) {
    return new RightJoin<>(cursor(), queryable.cursor());
  }

  interface Join<A, B> extends Many<Pair<A, B>> {
    Many<Pair<A, B>> on(TryPredicate2<? super A, ? super B> on);
  }

  final class LeftJoin<FIRST, SECOND> implements Join<FIRST, SECOND> {
    private final Cursor<FIRST> left;
    private final Cursor<SECOND> right;

    LeftJoin(Cursor<FIRST> left, Cursor<SECOND> right) {
      this.left = left;
      this.right = right;
    }

    private re.artoo.lance.query.cursor.Join<FIRST, SECOND> join() {
      return left.leftJoin(right);
    }

    @Override
    public Cursor<Pair<FIRST, SECOND>> cursor() {
      return null;
    }

    @Override
    public Many<Pair<FIRST, SECOND>> on(TryPredicate2<? super FIRST, ? super SECOND> on) {
      return null;
    }
  }

  final class RightJoin<FIRST, SECOND> implements Join<FIRST, SECOND> {
    private final Cursor<FIRST> left;
    private final Cursor<SECOND> right;

    RightJoin(Cursor<FIRST> left, Cursor<SECOND> right) {
      this.left = left;
      this.right = right;
    }

    private re.artoo.lance.query.cursor.Join<FIRST, SECOND> join() {
      return left.leftJoin(right);
    }
    @Override
    public Cursor<Pair<FIRST, SECOND>> cursor() {
      return join();
    }

    @Override
    public Many<Pair<FIRST, SECOND>> on(TryPredicate2<? super FIRST, ? super SECOND> condition) {
      return () -> join().on(condition);
    }
  }

  final class OuterJoin<FIRST, SECOND> implements Join<FIRST, SECOND> {
    private final Cursor<FIRST> left;
    private final Cursor<SECOND> right;

    public OuterJoin(Cursor<FIRST> left, Cursor<SECOND> right) {
      this.left = left;
      this.right = right;
    }

    private re.artoo.lance.query.cursor.Join<FIRST, SECOND> outerJoin() {
      return left.outerJoin(right);
    }

    @Override
    public Cursor<Pair<FIRST, SECOND>> cursor() {
      return outerJoin();
    }
    @Override
    public Many<Pair<FIRST, SECOND>> on(final TryPredicate2<? super FIRST, ? super SECOND> condition) {
      return () -> outerJoin().on(condition);
    }

  }*/
}
