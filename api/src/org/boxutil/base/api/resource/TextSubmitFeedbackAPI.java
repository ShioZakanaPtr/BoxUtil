package org.boxutil.base.api.resource;

/**
 * @see org.boxutil.units.standard.entity.TextFieldEntity#setSubmitFeedback(TextSubmitFeedbackAPI)
 * @see org.boxutil.units.standard.misc.TextFieldObject#setSubmitFeedback(TextSubmitFeedbackAPI)
 */
public interface TextSubmitFeedbackAPI {
    /**
     * @param linefeed when linefeed occurred, whether it is linefeed symbol.
     * @param styleBits layout = <code>0b [invert] [italic] [underline] [strikeout]</code>; flag valid when 1 bit.
     */
    void processText(byte fontData, int textDataIndex, char character, boolean valid, boolean linefeed, int indexOfCurrString, float cursorX, float cursorY, float dataPadding, byte styleBits, byte colorRed, byte colorGreen, byte colorBlue, byte colorAlpha);

    /**
     * When not-any space that draw current character.
     */
    void processBreak(byte fontData, int textDataIndex, boolean beforeNextSubmit, char character, int indexOfCurrString, float cursorX, float cursorY);
}
