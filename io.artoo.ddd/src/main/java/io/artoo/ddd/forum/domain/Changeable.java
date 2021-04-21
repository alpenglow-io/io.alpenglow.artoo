package io.artoo.ddd.forum.domain;

import io.artoo.ddd.core.Domain;
import io.artoo.ddd.forum.Order;

public sealed interface Changeable extends Domain.Aggregate permits Order {
}
