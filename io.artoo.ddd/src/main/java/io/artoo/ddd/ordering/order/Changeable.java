package io.artoo.ddd.ordering.order;

import io.artoo.ddd.domain.Domain;
import io.artoo.ddd.ordering.Order;

public sealed interface Changeable extends Domain.Aggregate permits Order {
}
