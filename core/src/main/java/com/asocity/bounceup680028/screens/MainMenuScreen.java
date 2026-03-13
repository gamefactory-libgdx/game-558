package com.asocity.bounceup680028.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

public class MainMenuScreen implements Screen {

    private final MainGame game;
    private OrthographicCamera camera;
    private StretchViewport viewport;
    private Stage stage;

    public MainMenuScreen(MainGame game) {
        this.game = game;

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    Gdx.app.exit();
                    return true;
                }
                return false;
            }
        }));

        game.playMusic("sounds/music/music_menu.ogg");

        buildUI();
    }

    private TextButton.TextButtonStyle makeStyle(String up, String down) {
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

        // Root table
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // Title
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, Color.WHITE);
        Label title = new Label("BOUNCE UP", titleStyle);
        root.add(title).padTop(80f).padBottom(48f).row();

        // PLAY button
        TextButton playBtn = new TextButton("PLAY",
            makeStyle("sprites/button_yellow.png", "sprites/button_yellow_pressed.png"));
        playBtn.setSize(Constants.BTN_WIDTH_MAIN, Constants.BTN_HEIGHT_MAIN);
        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playSfx("sounds/sfx/sfx_button_click.ogg");
                game.setScreen(new WorldSelectScreen(game));
            }
        });
        root.add(playBtn).size(Constants.BTN_WIDTH_MAIN, Constants.BTN_HEIGHT_MAIN).padBottom(20f).row();

        // SHOP button
        TextButton shopBtn = new TextButton("SHOP",
            makeStyle("sprites/button_blue.png", "sprites/button_blue_pressed.png"));
        shopBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playSfx("sounds/sfx/sfx_button_click.ogg");
                game.setScreen(new ShopScreen(game));
            }
        });
        root.add(shopBtn).size(Constants.BTN_WIDTH_MAIN, Constants.BTN_HEIGHT_MAIN).padBottom(20f).row();

        // LEADERBOARD button
        TextButton lbBtn = new TextButton("LEADERBOARD",
            makeStyle("sprites/button_green.png", "sprites/button_green_pressed.png"));
        lbBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playSfx("sounds/sfx/sfx_button_click.ogg");
                game.setScreen(new LeaderboardScreen(game));
            }
        });
        root.add(lbBtn).size(Constants.BTN_WIDTH_MAIN, Constants.BTN_HEIGHT_MAIN).padBottom(20f).row();

        // SETTINGS button
        TextButton settingsBtn = new TextButton("SETTINGS",
            makeStyle("sprites/button_grey.png", "sprites/button_grey_pressed.png"));
        settingsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playSfx("sounds/sfx/sfx_button_click.ogg");
                game.setScreen(new SettingsScreen(game));
            }
        });
        root.add(settingsBtn).size(Constants.BTN_WIDTH_MAIN, Constants.BTN_HEIGHT_MAIN).padBottom(20f).row();

        // CREDITS button
        TextButton creditsBtn = new TextButton("CREDITS",
            makeStyle("sprites/button_grey.png", "sprites/button_grey_pressed.png"));
        creditsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playSfx("sounds/sfx/sfx_button_click.ogg");
                game.setScreen(new CreditsScreen(game));
            }
        });
        root.add(creditsBtn).size(Constants.BTN_WIDTH_SECONDARY, Constants.BTN_HEIGHT_SECONDARY).row();
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
