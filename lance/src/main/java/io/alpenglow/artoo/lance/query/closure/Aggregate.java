package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryFunction2;
import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.func.TryPredicate1;
import io.alpenglow.artoo.lance.query.Closure;

@SuppressWarnings("unchecked")
public final class Aggregate<ELEMENT, AGGREGATED, SELECTED> implements Closure<ELEMENT, AGGREGATED> {
  private AGGREGATED aggregated;
  private final TryPredicate1<? super ELEMENT> where;
  private final TryFunction1<? super ELEMENT, ? extends SELECTED> select;
  private final TryFunction2<? super AGGREGATED, ? super SELECTED, ? extends AGGREGATED> aggregate;

  public Aggregate(final AGGREGATED aggregated, final TryPredicate1<? super ELEMENT> where, final TryFunction1<? super ELEMENT, ? extends SELECTED> select, final TryFunction2<? super AGGREGATED, ? super SELECTED, ? extends AGGREGATED> aggregate) {
    this.aggregated = aggregated;
    this.where = where;
    this.select = select;
    this.aggregate = aggregate;
  }

  @SuppressWarnings("unchecked")
  @Override
  public AGGREGATED invoke(final ELEMENT element) throws Throwable {
    return !where.invoke(element)
      ? aggregated
      : (aggregated = aggregated != null ? aggregate.invoke(aggregated, select.invoke(element)) : (AGGREGATED) select.invoke(element));
  }
}
