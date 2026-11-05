package io.github.shiozakanaptr.acext.util.function.doubletype;

/**
 * Bootstrap declaration of the double family's consumer.
 *
 * <p>Exists only so that the {@code float -> double} Wrap templates compile as
 * part of {@code template/src}; the real interface is generated into
 * {@code build/src} from {@code TypeNameConsumer}. Neither copied nor
 * generated, so it does not collide with the generated type.
 */
public interface DoubleConsumer extends java.util.function.DoubleConsumer {
    void accept(double t);
}
