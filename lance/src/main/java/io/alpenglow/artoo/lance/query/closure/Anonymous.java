package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.query.Closure;

/**
 * Simple trick to let defining an anonymous closure
 *
 * @param <S> source parameter
 * @param <T> target result
 */
non-sealed public interface Anonymous<S, T> extends Closure<S, T> {
}
