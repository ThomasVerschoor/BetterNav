package com.company.betternav.navigation;

public class PlayersActionBar {

    String UUID;
    boolean enabled;

    public PlayersActionBar(String UUID,boolean enabled){
        this.UUID = UUID;
        this.enabled = enabled;
    }

    public String getUUID() {
        return UUID;
    }

    public boolean isEnabled() {
        return enabled;
    }

    // turn on or off the action bar
    public void toggle(){
        enabled = !enabled;
    }
}
