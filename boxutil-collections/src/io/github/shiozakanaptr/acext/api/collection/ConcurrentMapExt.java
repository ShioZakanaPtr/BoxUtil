package io.github.shiozakanaptr.acext.api.collection;

import java.util.concurrent.ConcurrentMap;

/**
 * A concurrent {@link MapExt} that combines the additional mapping operations
 * of {@link MapExt} with the thread-safety and atomicity contract of
 * {@link ConcurrentMap}.
 *
 * <p>As a matter of principle, this interface does not introduce or define any
 * additional methods or members of its own. Its sole purpose is to extend both
 * {@link MapExt} and {@link ConcurrentMap}, thereby providing concurrency
 * guarantees for the additional methods inherited from {@link MapExt}.
 *
 * <p>In addition to the operations inherited from {@link MapExt} and
 * {@link ConcurrentMap}, a {@code ConcurrentMapExt} exposes the extra map
 * operations in a form suitable for concurrent use. Unless a concrete
 * implementation documents otherwise, operations that update a mapping should
 * preserve the atomicity guarantees required by {@link ConcurrentMap}; the
 * exact atomicity and consistency guarantees of the additional operations are
 * defined by the concrete implementation.
 *
 * <p>The views and iterators exposed through the {@link java.util.Map} contract
 * are expected to follow the weakly consistent semantics of {@link ConcurrentMap}
 * unless an implementation documents stronger guarantees.
 *
 * <p><b>Null handling.</b> Typically, for any key or value that is {@code null},
 * it should be treated as {@code 0} if the key is of a specific primitive type
 * that the implementation defines. However, for any value that is {@code 0}, it
 * usually is not treated as {@code null} if the value is of a specific
 * primitive type that the implementation defines. Concrete implementations of
 * {@link ConcurrentMap} may impose additional restrictions on {@code null} keys
 * and values; consult their documentation.
 *
 * <p><b>Optional operations.</b> Implementations may not support every
 * mutating operation. An implementation that does not support a mutating
 * operation is documented as throwing {@link UnsupportedOperationException}
 * for that operation. Callers should consult the documentation of the concrete
 * implementation for the operations it supports and their concurrency
 * semantics.
 *
 * <p><b>Thread-safe.</b> This interface is intended for concurrent use.
 * Concrete implementations define their exact thread-safety, atomicity and
 * consistency guarantees.
 *
 * <p><b>Concurrent support for additional {@link MapExt} operations.</b>
 * The following additional operations inherited from {@link MapExt} are
 * supported with concurrent semantics by this interface:
 *
 * <ul>
 *   <li>{@link MapExt#update}: when multiple threads attempt updates including
 *   potentially calling function parameter multiple times.</li>
 *   <li>{@link MapExt#remapping}: when multiple threads attempt updates including
 *   potentially calling function parameter multiple times.</li>
 *   <li>{@link MapExt#swapAt}: atomic swap and updates</li>
 * </ul>
 *
 * <p>For any additional {@link MapExt} operations not listed above, no
 * concurrent semantics are guaranteed unless the concrete implementation
 * explicitly documents them. Callers should consult the documentation of the
 * concrete implementation to confirm the thread-safety, atomicity and
 * consistency guarantees of each additional method.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * @see MapExt
 * @see ConcurrentMap
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ConcurrentMapExt<K, V> extends MapExt<K, V>, ConcurrentMap<K, V> {
}
