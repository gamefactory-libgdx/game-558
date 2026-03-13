package com.asocity.bounceup680028.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.asocity.bounceup680028.Constants;
import com.asocity.bounceup680028.MainGame;

public class GameOverScreen implements Screen {

    private final MainGame game;
    private final int      finalHeight; // metres reached
    private final int      starsEarned;

    private OrthographicCamera camera;
    private StretchViewport    viewport;
    private Stage              stage;

    public GameOverScreen(MainGame game, int score, int extra) {
        this.game        = game;
        this.finalHeight = score;
        this.starsEarned = extra;

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                return false;
            }
        }));

        // Save leaderboard entry
        LeaderboardScreen.addScore(finalHeight);

        // Update best height
        Preferences prefs = Gdx.app.getPreferences(Constants.PREF_FILE);
        int prevBest = prefs.getInteger(Constants.PREF_BEST_HEIGHT, 0);
        if (finalHeight > prevBest) {
            prefs.putInteger(Constants.PREF_BEST_HEIGHT, finalHeight);
        }
        // Accumulate stars
        int totalStars = prefs.getInteger(Constants.PREF_STARS, 0) + starsEarned;
        prefs.putInteger(Constants.PREF_STARS, totalStars);
        prefs.flush();

        game.playMusicOnce("sounds/music/music_game_over.ogg");

        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_game_over.ogg", com.badlogic.gdx.audio.Sound.class).play(1.0f);

        buildUI(Math.max(prevBest, finalHeight));
    }

    private TextButton.TextButtonStyle makeStyle(String up, String down) {
        TextButton.TextButtonStyle s = new TextButton.TextButtonStyle();
        s.font      = game.fontBody;
        s.up        = new TextureRegionDrawable(game.manager.get(up,   Texture.class));
        s.down      = new TextureRegionDrawable(game.manager.get(down, Texture.class));
        s.fontColor = Color.WHITE;
        return s;
    }

    private void buildUI(int bestHeight) {
        // Background
        Image bg = new Image(game.manager.get("backgrounds/bg_main.png", Texture.class));
        bg.setSize(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage.addActor(bg);

        // Dark overlay
        com.badlogic.gdx.scenes.scene2d.ui.Image overlay =
            new com.badlogic.gdx.scenes.scene2d.ui.Image(
                new com.badlogic.gdx.graphics.g2d.TextureRegion(
                    game.manager.get("backgrounds/bg_main.png", Texture.class)));
        overlay.setSize(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        overlay.setColor(0f, 0f, 0f, 0.65f);
        stage.addActor(overlay);

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, Color.WHITE);
        Label.LabelStyle bodyStyle  = new Label.LabelStyle(game.fontBody,  Color.WHITE);
        Label.LabelStyle goldStyle  = new Label.LabelStyle(game.fontBody,  Color.valueOf("FFD700"));
        Label.LabelStyle smallStyle = new Label.LabelStyle(game.fontSmall, Color.LIGHT_GRAY);

        root.add(new Label("GAME OVER", titleStyle)).padTop(80f).padBottom(40f).row();
        root.add(new Label("FINAL HEIGHT: " + finalHeight + "m", bodyStyle)).padBottom(14f).row();
        root.add(new Label("STARS EARNED: " + starsEarned,       goldStyle)).padBottom(14f).row();
        root.add(new Label("BEST HEIGHT: "  + bestHeight  + "m", smallStyle)).padBottom(60f).row();

        // Buttons row
        Table btns = new Table();

        TextButton retryBtn = new TextButton("PLAY AGAIN",
            makeStyle("sprites/button_yellow.png", "sprites/button_yellow_pressed.png"));
        retryBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get("sounds/sfx/sfx_button_click.ogg",
                        com.badlogic.gdx.audio.Sound.class).play(1.0f);
                game.setScreen(new WorldSelectScreen(game));
            }
        });
        btns.add(retryBtn).size(Constants.BTN_WIDTH_SECONDARY, Constants.BTN_HEIGHT_SECONDARY).padRight(16f);

        TextButton menuBtn = new TextButton("MAIN MENU",
            makeStyle("sprites/button_grey.png", "sprites/button_grey_pressed.png"));
        menuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.sfxEnabled)
                    game.manager.get("sounds/sfx/sfx_button_back.ogg",
                        com.badlogic.gdx.audio.Sound.class).play(1.0f);
                game.setScreen(new MainMenuScreen(game));
            }
        });
        btns.add(menuBtn).size(Constants.BTN_WIDTH_SECONDARY, Constants.BTN_HEIGHT_SECONDARY);

        root.add(btns).row();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void show()   {}
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
