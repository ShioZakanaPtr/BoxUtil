package org.boxutil.backends.core;

import org.lwjgl.opengl.Drawable;

final class BUtil_StaticTrailThread extends BUtil_BoxUtilBackgroundThread._ThreadTemplate {
    BUtil_StaticTrailThread(Thread hostThread, Drawable sharedDrawable, Object args) {
        super(hostThread, sharedDrawable, args);
    }

    protected void runBody() {

    }

    protected void logicalInit() {

    }

    protected void logicalDestroy() {

    }
}
