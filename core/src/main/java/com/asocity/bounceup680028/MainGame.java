package com.asocity.bounceup680028;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.asocity.bounceup680028.screens.MainMenuScreen;

public class MainGame extends Game {

    public SpriteBatch batch;
    public AssetManager manager;

    // Fonts
    public BitmapFont fontTitle;   // Orbitron — titles, scores
    public BitmapFont fontBody;    // Roboto  — labels, buttons, HUD
    public BitmapFont fontSmall;   // Roboto small — secondary labels
    public BitmapFont fontHud;     // Roboto HUD — in-game height/score

    // Audio state
    public boolean musicEnabled = true;
    public boolean sfxEnabled   = true;
    public Music   currentMusic = null;

    @Override
    public void create() {
        batch   = new SpriteBatch();
        manager = new AssetManager();

        generateFonts();
        loadAssets();
        manager.finishLoading();

        setScreen(new MainMenuScreen(this));
    }

    // -------------------------------------------------------------------------
    // Font generation
    // -------------------------------------------------------------------------

    private void generateFonts() {
        FreeTypeFontGenerator titleGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Orbitron-Regular.ttf"));
        FreeTypeFontGenerator bodyGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Roboto-Regular.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();

        p.size = Constants.FONT_SIZE_TITLE;
        fontTitle = titleGen.generateFont(p);

        p.size = Constants.FONT_SIZE_BODY;
        fontBody = bodyGen.generateFont(p);

        p.size = Constants.FONT_SIZE_SMALL;
        fontSmall = bodyGen.generateFont(p);

        p.size = Constants.FONT_SIZE_HUD;
        fontHud = bodyGen.generateFont(p);

        titleGen.dispose();
        bodyGen.dispose();
    }

    // -------------------------------------------------------------------------
    // Asset loading
    // -------------------------------------------------------------------------

    private void loadAssets() {
        // Backgrounds
        manager.load("backgrounds/bg_main.png",  com.badlogic.gdx.graphics.Texture.class);
        manager.load("backgrounds/bg_sky.png",   com.badlogic.gdx.graphics.Texture.class);
        manager.load("backgrounds/bg_cave.png",  com.badlogic.gdx.graphics.Texture.class);
        manager.load("backgrounds/bg_space.png", com.badlogic.gdx.graphics.Texture.class);

        // Buttons
        manager.load("sprites/button_blue.png",         com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/button_blue_pressed.png", com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/button_green.png",        com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/button_green_pressed.png",com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/button_grey.png",         com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/button_grey_pressed.png", com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/button_red.png",          com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/button_red_pressed.png",  com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/button_yellow.png",       com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/button_yellow_pressed.png",com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/button_round_blue.png",   com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/button_round_blue_pressed.png", com.badlogic.gdx.graphics.Texture.class);

        // Icons
        manager.load("sprites/icon_music_on.png",    com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/icon_music_off.png",   com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/icon_sfx_on.png",      com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/icon_sfx_off.png",     com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/icon_settings.png",    com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/icon_leaderboard.png", com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/icon_shop.png",        com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/icon_pause.png",       com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/icon_play.png",        com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/icon_home.png",        com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/icon_close.png",       com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/icon_star.png",        com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/icon_trophy.png",      com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/icon_locked.png",      com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/icon_unlocked.png",    com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/icon_medal.png",       com.badlogic.gdx.graphics.Texture.class);

        // Star collectible
        manager.load("sprites/star.png", com.badlogic.gdx.graphics.Texture.class);

        // Jumper sprites — player skins
        manager.load("sprites/jumper/player_stand.png",  com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/jumper/player_jump.png",   com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/jumper/player_walk1.png",  com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/jumper/player_walk2.png",  com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/jumper/player_hurt.png",   com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/jumper/player2_stand.png", com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/jumper/player2_jump.png",  com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/jumper/player2_walk1.png", com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/jumper/player2_walk2.png", com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/jumper/player2_hurt.png",  com.badlogic.gdx.graphics.Texture.class);

        // Platforms
        manager.load("sprites/jumper/platform_grass.png",        com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/jumper/platform_grass_broken.png", com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/jumper/platform_sand.png",         com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/jumper/platform_snow.png",         com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/jumper/platform_stone.png",        com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/jumper/platform_wood.png",         com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/jumper/platform_wood_broken.png",  com.badlogic.gdx.graphics.Texture.class);

        // Hazards
        manager.load("sprites/jumper/hazard_spikeball.png", com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/jumper/enemy_fly_stand.png",  com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/jumper/enemy_fly_jump.png",   com.badlogic.gdx.graphics.Texture.class);
        manager.load("sprites/jumper/item_spring.png",      com.badlogic.gdx.graphics.Texture.class);

        // Music
        manager.load("sounds/music/music_menu.ogg",      Music.class);
        manager.load("sounds/music/music_gameplay.ogg",  Music.class);
        manager.load("sounds/music/music_game_over.ogg", Music.class);

        // SFX
        manager.load("sounds/sfx/sfx_button_click.ogg",   Sound.class);
        manager.load("sounds/sfx/sfx_button_back.ogg",    Sound.class);
        manager.load("sounds/sfx/sfx_toggle.ogg",         Sound.class);
        manager.load("sounds/sfx/sfx_coin.ogg",           Sound.class);
        manager.load("sounds/sfx/sfx_jump.ogg",           Sound.class);
        manager.load("sounds/sfx/sfx_hit.ogg",            Sound.class);
        manager.load("sounds/sfx/sfx_game_over.ogg",      Sound.class);
        manager.load("sounds/sfx/sfx_level_complete.ogg", Sound.class);
        manager.load("sounds/sfx/sfx_power_up.ogg",       Sound.class);
    }

    // -------------------------------------------------------------------------
    // Music helpers
    // -------------------------------------------------------------------------

    /** Play a looping track. Skips restart if the same track is already playing. */
    public void playMusic(String path) {
        Music requested = manager.get(path, Music.class);
        if (requested == currentMusic && currentMusic.isPlaying()) return;
        if (currentMusic != null) currentMusic.stop();
        currentMusic = requested;
        currentMusic.setLooping(true);
        currentMusic.setVolume(0.7f);
        if (musicEnabled) currentMusic.play();
    }

    /** Play a track once (no loop) — for game over jingle. */
    public void playMusicOnce(String path) {
        if (currentMusic != null) currentMusic.stop();
        currentMusic = manager.get(path, Music.class);
        currentMusic.setLooping(false);
        currentMusic.setVolume(0.7f);
        if (musicEnabled) currentMusic.play();
    }

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        manager.dispose();
        fontTitle.dispose();
        fontBody.dispose();
        fontSmall.dispose();
        fontHud.dispose();
    }
}
