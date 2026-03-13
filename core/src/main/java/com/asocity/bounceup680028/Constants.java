package com.asocity.bounceup680028;

public class Constants {

    // World dimensions
    public static final float WORLD_WIDTH  = 480f;
    public static final float WORLD_HEIGHT = 854f;

    // Player
    public static final float PLAYER_WIDTH        = 56f;
    public static final float PLAYER_HEIGHT       = 56f;
    public static final float PLAYER_START_X      = WORLD_WIDTH / 2f - PLAYER_WIDTH / 2f;
    public static final float PLAYER_START_Y      = 200f;
    public static final float PLAYER_MOVE_SPEED   = 280f;  // horizontal tilt speed
    public static final float BOUNCE_VELOCITY     = 700f;  // velocity on platform bounce
    public static final float GRAVITY             = -1200f;
    public static final float MAX_FALL_SPEED      = -900f;

    // Platforms
    public static final float PLATFORM_WIDTH      = 80f;
    public static final float PLATFORM_HEIGHT     = 20f;
    public static final float PLATFORM_MIN_GAP    = 100f;  // min vertical gap between platforms
    public static final float PLATFORM_MAX_GAP    = 160f;  // max vertical gap between platforms
    public static final int   PLATFORM_POOL_SIZE  = 20;
    public static final float BROKEN_PLATFORM_CHANCE = 0.15f; // 15% chance

    // Stars / collectibles
    public static final float STAR_SIZE           = 32f;
    public static final float STAR_SPAWN_CHANCE   = 0.3f;  // 30% chance per platform
    public static final int   STAR_SCORE_VALUE    = 10;    // score per star

    // Hazards
    public static final float HAZARD_WIDTH        = 40f;
    public static final float HAZARD_HEIGHT       = 40f;

    // Scroll / camera
    public static final float CAMERA_LEAD         = WORLD_HEIGHT * 0.55f; // player Y threshold before camera scrolls up

    // Height tracking (metres)
    public static final float PIXELS_PER_METRE    = 4f;    // 4 world-units = 1 metre

    // World unlock thresholds (metres)
    public static final int   CAVE_UNLOCK_HEIGHT  = 300;
    public static final int   SPACE_UNLOCK_HEIGHT = 500;

    // Shop — power-up prices (stars)
    public static final int   PRICE_SHIELD        = 20;
    public static final int   PRICE_MAGNET        = 30;
    public static final int   PRICE_DOUBLE_SCORE  = 50;
    public static final int   PRICE_SKIN_2        = 100;
    public static final int   PRICE_SKIN_3        = 150;

    // Power-up durations (seconds)
    public static final float SHIELD_DURATION     = 5f;
    public static final float MAGNET_DURATION     = 10f;
    public static final float DOUBLE_SCORE_DURATION = 60f; // lasts full run

    // Magnet pull radius
    public static final float MAGNET_RADIUS       = 120f;

    // UI sizes
    public static final float BTN_WIDTH_MAIN      = 240f;
    public static final float BTN_HEIGHT_MAIN     = 70f;
    public static final float BTN_WIDTH_SECONDARY = 200f;
    public static final float BTN_HEIGHT_SECONDARY = 60f;
    public static final float BTN_ROUND_SIZE      = 60f;
    public static final float ICON_SIZE           = 48f;
    public static final float PAUSE_BTN_SIZE      = 56f;

    // Font sizes
    public static final int   FONT_SIZE_TITLE     = 48;
    public static final int   FONT_SIZE_BODY      = 28;
    public static final int   FONT_SIZE_SMALL     = 20;
    public static final int   FONT_SIZE_HUD       = 24;
    public static final int   FONT_SIZE_SCORE     = 36;

    // SharedPreferences keys
    public static final String PREF_FILE          = "GamePrefs";
    public static final String PREF_MUSIC         = "musicEnabled";
    public static final String PREF_SFX           = "sfxEnabled";
    public static final String PREF_BEST_HEIGHT   = "bestHeight";
    public static final String PREF_STARS         = "totalStars";
    public static final String PREF_SKIN          = "selectedSkin";
    public static final String PREF_SHIELD_OWNED  = "shieldOwned";
    public static final String PREF_MAGNET_OWNED  = "magnetOwned";
    public static final String PREF_DOUBLE_OWNED  = "doubleScoreOwned";
    public static final String PREF_CAVE_UNLOCKED = "caveUnlocked";
    public static final String PREF_SPACE_UNLOCKED= "spaceUnlocked";
    public static final String PREF_LEADERBOARD   = "leaderboard";

    // Leaderboard
    public static final int   LEADERBOARD_SIZE    = 10;

    // World IDs
    public static final int   WORLD_SKY           = 0;
    public static final int   WORLD_CAVE          = 1;
    public static final int   WORLD_SPACE         = 2;
}
