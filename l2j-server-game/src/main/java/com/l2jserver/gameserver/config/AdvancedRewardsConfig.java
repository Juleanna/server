package com.l2jserver.gameserver.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("file:config/custom/AdvancedRewards.properties")
public interface AdvancedRewardsConfig extends Config {
    
    @DefaultValue("true")
    boolean getEnableAdvancedRewards();
    
    @DefaultValue("true") 
    boolean getEnableDetailedLogging();
    
    @DefaultValue("l2j_rewards")
    String getDatabaseSchema();
    
    @DefaultValue("true")
    boolean getEnableAntiAFK();
    
    @DefaultValue("10")
    int getAFKTimeoutMinutes();
    
    @DefaultValue("true")
    boolean getEnableProgressiveRewards();
    
    @DefaultValue("5.0")
    double getMaxProgressiveMultiplier();
    
    @DefaultValue("true")
    boolean getEnableWebAPI();
    
    @DefaultValue("8080")
    int getWebAPIPort();
    
    @DefaultValue("secure_token_123")
    String getWebAPIAuthToken();
    
    @DefaultValue("5")
    int getConfigReloadIntervalMinutes();
    
    @DefaultValue("30") 
    int getCleanupIntervalMinutes();
    
    @DefaultValue("true")
    boolean getEnableCalendarEvents();
    
    @DefaultValue("10")
    int getMaxRewardGroupsPerPlayer();
    
    @DefaultValue("false")
    boolean getDebugMode();
}