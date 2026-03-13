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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.asocity.bounceup680028.Constants;
import com.asocity.bounceup680028.MainGame;

public class SettingsScreen implements Screen {

    private final MainGame game;
    private OrthographicCamera camera;
    private StretchViewport viewport;
    private Stage stage;

    public SettingsScreen(MainGame game) {
        this.game = game;

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    playSfx("sounds/sfx/sfx_button_back.ogg");
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                return false;
            }
        }));

        // Load saved preferences
        Preferences prefs = Gdx.app.getPreferences(Constants.PREF_FILE);
        game.musicEnabled = prefs.getBoolean(Constants.PREF_MUSIC, true);
        game.sfxEnabled   = prefs.getBoolean(Constants.PREF_SFX,   true);

        buildUI();
    }

    private TextButton.TextButtonStyle makeButtonStyle(String up, String down) {
        TextButton.TextButtonStyle s = new TextButton.TextButtonStyle();
        s.font      = game.fontBody;
        s.up        = new TextureRegionDrawable(game.manager.get(up,   Texture.class));
        s.down      = new TextureRegionDrawable(game.manager.get(down, Texture.class));
        s.fontColor = Color.WHITE;
        return s;
    }

    private void buildUI() {
        // Background
        Image bg = new Image(game.manager.get("backgrounds/bg_main.png", Texture.class));
        bg.setSize(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage.addActor(bg);

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // Title
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, Color.WHITE);
        root.add(new Label("SETTINGS", titleStyle)).padTop(80f).padBottom(60f).row();

        // Music toggle
        Label.LabelStyle bodyStyle = new Label.LabelStyle(game.fontBody, Color.WHITE);
        root.add(new Label("MUSIC", bodyStyle)).left().padBottom(16f).row();

        final ImageButton musicBtn = new ImageButton(
            new TextureRegionDrawable(game.manager.get("sprites/icon_music_on.png",  Texture.class)),
            new TextureRegionDrawable(game.manager.get("sprites/icon_music_on.png",  Texture.class)),
            new TextureRegionDrawable(game.manager.get("sprites/icon_music_off.png", Texture.class))
        );
        musicBtn.setChecked(!game.musicEnabled);
        musicBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.musicEnabled = !musicBtn.isChecked();
                Preferences prefs = Gdx.app.getPreferences(Constants.PREF_FILE);
                prefs.putBoolean(Constants.PREF_MUSIC, game.musicEnabled);
                prefs.flush();
                if (game.currentMusic != null) {
                    if (game.musicEnabled) game.currentMusic.play();
                    else                   game.currentMusic.pause();
                }
                playSfx("sounds/sfx/sfx_toggle.ogg");
            }
        });
        root.add(musicBtn).size(Constants.BTN_ROUND_SIZE, Constants.BTN_ROUND_SIZE).padBottom(36f).row();

        // SFX toggle
        root.add(new Label("SOUND FX", bodyStyle)).left().padBottom(16f).row();

        final ImageButton sfxBtn = new ImageButton(
            new TextureRegionDrawable(game.manager.get("sprites/icon_sfx_on.png",  Texture.class)),
            new TextureRegionDrawable(game.manager.get("sprites/icon_sfx_on.png",  Texture.class)),
            new TextureRegionDrawable(game.manager.get("sprites/icon_sfx_off.png", Texture.class))
        );
        sfxBtn.setChecked(!game.sfxEnabled);
        sfxBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.sfxEnabled = !sfxBtn.isChecked();
                Preferences prefs = Gdx.app.getPreferences(Constants.PREF_FILE);
                prefs.putBoolean(Constants.PREF_SFX, game.sfxEnabled);
                prefs.flush();
                playSfx("sounds/sfx/sfx_toggle.ogg");
            }
        });
        root.add(sfxBtn).size(Constants.BTN_ROUND_SIZE, Constants.BTN_ROUND_SIZE).padBottom(60f).row();

        // Main Menu button
        TextButton menuBtn = new TextButton("MAIN MENU",
            makeButtonStyle("sprites/button_grey.png", "sprites/button_grey_pressed.png"));
        menuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playSfx("sounds/sfx/sfx_button_back.ogg");
                game.setScreen(new MainMenuScreen(game));
            }
        });
        root.add(menuBtn).size(Constants.BTN_WIDTH_SECONDARY, Constants.BTN_HEIGHT_SECONDARY).row();
    }

    private void playSfx(String path) {
        if (game.sfxEnabled)
            game.manager.get(path, com.badlogic.gdx.audio.Sound.class).play(1.0f);
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
