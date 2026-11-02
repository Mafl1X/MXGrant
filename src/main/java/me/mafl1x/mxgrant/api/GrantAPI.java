package me.mafl1x.mxgrant.api;

import me.mafl1x.mxgrant.MXGrant;

import java.util.UUID;

public class GrantAPI implements IGrantAPI {

    @Override
    public int getGroupGiveCount(UUID uuid, String group) {
        return MXGrant.getInstance().getDatabase().getGroupGiveCount(uuid, group);
    }

    @Override
    public void addPlayerGroup(UUID uuid, String group) {
        MXGrant.getInstance().getDatabase().addPlayerGroup(uuid, group);
    }
}
