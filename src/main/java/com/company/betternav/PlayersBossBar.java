package com.company.betternav;

import com.company.betternav.events.NavBossBar;
import org.bukkit.boss.BossBar;

import java.util.UUID;

public class PlayersBossBar {

    private UUID uuid;
    private NavBossBar navbossbar;

    public PlayersBossBar(UUID uuid, NavBossBar navbossbar){
        this.uuid = uuid;
        this.navbossbar = navbossbar;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public NavBossBar getnavbossbar() {
        return navbossbar;
    }

    public void setnavbosbar(NavBossBar navbossbar) {
        this.navbossbar = navbossbar;
    }
}
