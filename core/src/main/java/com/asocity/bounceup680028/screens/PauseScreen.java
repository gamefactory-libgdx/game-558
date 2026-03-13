package com.asocity.bounceup680028.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
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

public class PauseScreen implements Screen {

    private final MainGame    game;
    private final GameScreen  gameScreen;
    private final int         worldId;

    private OrthographicCamera camera;
    private StretchViewport    viewport;
    private Stage              stage;

    public PauseScreen(MainGame game, GameScreen gameScreen, int worldId) {
        this.game       = game;
        this.gameScreen = gameScreen;
        this.worldId    = worldId;

        camera   = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage    = new Stage(viewport, game.batch);

        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    resumeGame();
                    return true;
                }
                return false;
            }
        }));

        // Pause background music while paused
        if (game.currentMusic != null && game.currentMusic.isPlaying()) {
            game.currentMusic.pause();
        }

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
        // World background for context
        String bgKey;
        switch (worldId) {
            case Constants.WORLD_CAVE:  bgKey = "backgrounds/bg_cave.png";  break;
            case Constants.WORLD_SPACE: bgKey = "backgrounds/bg_space.png"; break;
            default:                    bgKey = "backgrounds/bg_sky.png";   break;
        }
        Image bg = new Image(game.manager.get(bgKey, Texture.class));
        bg.setSize(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage.addActor(bg);

        // Dark overlay
        Image overlay = new Image(game.manager.get("backgrounds/bg_main.png", Texture.class));
        overlay.setSize(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        overlay.setColor(0f, 0f, 0f, 0.75f);
        stage.addActor(overlay);

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, Color.WHITE);
        root.add(new Label("PAUSED", titleStyle)).padBottom(60f).row();

        // Resume
        TextButton resumeBtn = new TextButton("RESUME",
                makeStyle("sprites/button_green.png", "sprites/button_green_pressed.png"));
        resumeBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playSfx("sounds/sfx/sfx_button_click.ogg");
                resumeGame();
            }
        });
        root.add(resumeBtn).size(Constants.BTN_WIDTH_MAIN, Constants.BTN_HEIGHT_MAIN).padBottom(20f).row();

        // Restart
        TextButton restartBtn = new TextButton("RESTART",
                makeStyle("sprites/button_yellow.png", "sprites/button_yellow_pressed.png"));
        restartBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playSfx("sounds/sfx/sfx_button_click.ogg");
                gameScreen.dispose();
                game.setScreen(new GameScreen(game, worldId));
            }
        });
        root.add(restartBtn).size(Constants.BTN_WIDTH_MAIN, Constants.BTN_HEIGHT_MAIN).padBottom(20f).row();

        // Main Menu
        TextButton menuBtn = new TextButton("MAIN MENU",
                makeStyle("sprites/button_grey.png", "sprites/button_grey_pressed.png"));
        menuBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playSfx("sounds/sfx/sfx_button_back.ogg");
                gameScreen.dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        root.add(menuBtn).size(Constants.BTN_WIDTH_MAIN, Constants.BTN_HEIGHT_MAIN).row();
    }

    private void resumeGame() {
        game.setScreen(gameScreen);  // gameScreen.show() re-registers input and resumes music
    }

    private void playSfx(String path) {
        if (game.sfxEnabled) game.manager.get(path, Sound.class).play(1.0f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void show()   {}
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() { stage.dispose(); }
}
