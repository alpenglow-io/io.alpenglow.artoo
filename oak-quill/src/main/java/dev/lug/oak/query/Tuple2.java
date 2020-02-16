package dev.lug.oak.query;

import dev.lug.oak.query.tuple2.Filterable2;
import dev.lug.oak.query.tuple2.Peekable2;
import dev.lug.oak.query.tuple2.Projectable2;

public interface Tuple2<V1, V2> extends Projectable2<V1, V2>, Filterable2<V1, V2>, Peekable2<V1, V2> {
}
