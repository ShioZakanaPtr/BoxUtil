package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

/**
 * Type specialization interface.
 *
 * @see Pair
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface CharObjPair<R> extends Pair<Character, R>, CharObjMap.Entry<R> {
    char leftRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #leftRaw()
     */
    @Deprecated
    default Character left() {
        return this.leftRaw();
    }

    default char getKeyRaw() {
        return this.leftRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyRaw()
     */
    @Deprecated
    default Character getKey() {
        return this.leftRaw();
    }


    default char left(char value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #left(char)
     */
    @Deprecated
    default <T extends Character> Character left(T value) {
        return this.left(value != null ? value.charValue() : (char) 0);
    }

}
