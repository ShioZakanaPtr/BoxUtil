package org.boxutil.base.api.container;

/**
 * Marker interface used by containers to indicate that they support Jumbo
 * capacities and sizes.
 *
 * <p>This interface declares no new methods. It marks implementing containers
 * as capable of representing capacities, remaining space, and element counts
 * that may exceed {@link Integer#MAX_VALUE}. The long-valued operations are
 * inherited from {@link Container}, including
 * {@link Container#resize64(long)}, {@link Container#reserve64(long)},
 * {@link Container#capacity64()}, {@link Container#remaining64()},
 * {@link Container#size64()}, and {@link Container#clear64(long)}.
 *
 * <p>Implementations of this interface are expected to override the relevant
 * {@code *64} methods in {@link Container} to provide meaningful long-valued
 * semantics. The default implementations in {@link Container} either throw
 * {@link UnsupportedOperationException} or return {@code -1L} to indicate that
 * long-sized capacities are not supported, infinite, or unknown.
 *
 * @apiNote This interface is intended to be used as a type-level marker. It
 *          does not add any methods to the contract inherited from
 *          {@link Container}.
 *
 * @see Container
 * @see Container#resize64(long)
 * @see Container#reserve64(long)
 * @see Container#capacity64()
 * @see Container#remaining64()
 * @see Container#size64()
 * @see Container#clear64(long)
 */
public interface JumboContainer extends Container {
}
