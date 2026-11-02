package me.mafl1x.mxgrant.api;

import java.util.UUID;

public interface IGrantAPI {
    int getGroupGiveCount(UUID uuid, String group);
    void addPlayerGroup(UUID uuid, String group);
}
