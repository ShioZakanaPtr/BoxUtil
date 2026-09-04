package org.boxutil.backends.core;

import org.boxutil.define.*;
import org.lwjgl.opengl.*;

import java.util.*;

public final class BUtil_ResourceStorage {
    private final static BUtil_ResourceStorage INST = new BUtil_ResourceStorage();

    private final BUtil_SyncResource sync = new BUtil_SyncResource();
    private final BUtil_CombatLayeredResource combatLayered = new BUtil_CombatLayeredResource();
    private final BUtil_CampaignLayeredResource campaignLayered = new BUtil_CampaignLayeredResource();
    private final BUtil_SharedResource sharedResource = new BUtil_SharedResource();

    public static BUtil_SyncResource syncResource() {
        return INST.sync;
    }

    public static BUtil_CombatLayeredResource combatLayered() {
        return INST.combatLayered;
    }

    public static BUtil_CampaignLayeredResource campaignLayered() {
        return INST.campaignLayered;
    }

    public static BUtil_SharedResource sharedResource() {
        return INST.sharedResource;
    }

    private BUtil_ResourceStorage() {}
}
