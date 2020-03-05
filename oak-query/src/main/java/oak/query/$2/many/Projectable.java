package oak.query.$2.many;

public interface Projectable<V1, V2> extends oak.query.$2.Queryable<V1, V2> {
  /*
  default <R> oak.query.Many select(final Tuple2AsAny<? super V1, ? super V2, ? extends R> map) {
    Nullability.nonNullable(map, "map");
    return () -> {
      final var array = new ArrayList<R>();
      for (final var tuple : this)
        array.add(tuple.as(map::apply));
      return array.iterator();
    };
  }

  default <T1, T2, T extends Projectable<V1, V2> & Filterable<V1, V2> & Peekable2<V1, V2>> Many<T1, T2> select(final Tuple2AsTuple<? super V1, ? super V2, ? extends T> map) {
    Nullability.nonNullable(map, "map");
    return () -> {
      final var array = new ArrayList<Union<T1, T2>>();
      for (final var tuple : this)
        tuple
          .as(map::apply)
          .select(as(Union::of))
          .eventually(array::add);
      return array.iterator();
    };
  }

  default <T1, T2, M extends Many<T1, T2>> M select(final Tuple2AsMany<? super V1, ? super V2, ? extends M> flatMap) {
    Nullability.nonNullable(flatMap, "flatMap");
    return () -> {
      final var array = new ArrayList<Union<T1, T2>>();
      for (final var tuple : this)
        tuple
          .as(flatMap::apply)
          .select(as(t -> t.select(as(Union::of))))
          .select(as(One::asIs))
          .eventually(t -> t.);
      return array.iterator();
    };
  }

  default Many<V1, V2> peek2(final Cons<? super V1, ? super V2> peek) {
    return new Peek<>(this, Nullability.nonNullable(peek, "peek"));
  }
}

final class Select2AsAny<V1, V2, R> implements oak.query.Many {
  private final oak.query.$2.Queryable<V1, V2> queryable;
  private final Func<? super V1, ? super V2, ? extends R> map;

  @Contract(pure = true)
  Select2AsAny(final oak.query.$2.Queryable<V1, V2> queryable, final Func<? super V1, ? super V2, ? extends R> map) {
    this.queryable = queryable;
    this.map = map;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var result = new ArrayList<R>();
    for (final var tuple2 : queryable) {
      tuple2
        .select(map)
        .forEach(result::add);
    }
    return result.iterator();
  }
}

final class SelectAs<V1, V2, T1, T2, T extends Projectable<V1, V2> & Filterable<V1, V2> & Peekable2<V1, V2>> implements Many<T1, T2> {
  private final oak.query.$2.Queryable<V1, V2> queryable;
  private final Func<? super V1, ? super V2, ? extends T> map;

  @Contract(pure = true)
  SelectAs(final oak.query.$2.Queryable<V1, V2> queryable, final Func<? super V1, ? super V2, ? extends T> map) {
    this.queryable = queryable;
    this.map = map;
  }

  @NotNull
  @Override
  @Contract(pure = true)
  public final Iterator<Nominal2<T1, T2>> iterator() {
    final var result = new ArrayList<Nominal2<T1, T2>>();
    for (final var tuple : queryable) {
      map
        .apply(tuple.value1, tuple.value2)
        .select(as(Nominal2::new))
        .eventually(result::add);
    }
    return result.iterator();
  }
}

final class Selection<V1, V2, T1, T2, T extends Projectable<V1, V2> & Filterable<V1, V2> & Peekable2<V1, V2>, S extends oak.query.Queryable> implements Many<T1, T2> {
  private final oak.query.Queryable queryable;
  private final Func<? super V1, ? super V2, ? extends S> flatMap;

  @Contract(pure = true)
  Selection(oak.query.Queryable queryable, Func<? super V1, ? super V2, ? extends S> flatMap) {
    this.queryable = queryable;
    this.flatMap = flatMap;
  }

  @NotNull
  @Override
  public final Iterator<Tuple2<T1, T2>> iterator() {
    final var result = new ArrayList<Tuple2<T1, T2>>();
    for (final var value : queryable) {
      value
        .select(flatMap)
        .peek(struct -> struct.forEach(result::add));
    }
    return result.iterator();
  }
}

final class Peek<V1, V2> implements Many<V1, V2> {
  private final oak.query.Queryable queryable;
  private final Cons<? super V1, ? super V2> peek;

  @Contract(pure = true)
  Peek(oak.query.Queryable queryable, Cons<? super V1, ? super V2> peek) {
    this.queryable = queryable;
    this.peek = peek;
  }

  @NotNull
  @Override
  public Iterator<Tuple2<V1, V2>> iterator() {
    for (final var value : queryable)
      value.peek(peek);
    return queryable.iterator();
  }*/
}
