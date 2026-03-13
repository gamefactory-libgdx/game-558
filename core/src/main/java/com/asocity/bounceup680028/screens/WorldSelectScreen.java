package com.asocity.bounceup680028.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.asocity.bounceup680028.Constants;
import com.asocity.bounceup680028.MainGame;

public class WorldSelectScreen implements Screen {

    private final MainGame game;
    private OrthographicCamera camera;
    private StretchViewport    viewport;
    private Stage              stage;

    public WorldSelectScreen(MainGame game) {
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
        Image bg = new Image(game.manager.get("backgrounds/bg_main.png", Texture.class));
        bg.setSize(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage.addActor(bg);

        Preferences prefs = Gdx.app.getPreferences(Constants.PREF_FILE);
        int bestHeight   = prefs.getInteger(Constants.PREF_BEST_HEIGHT, 0);
        boolean caveUnlocked  = bestHeight >= Constants.CAVE_UNLOCK_HEIGHT;
        boolean spaceUnlocked = bestHeight >= Constants.SPACE_UNLOCK_HEIGHT;

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, Color.WHITE);
        Label.LabelStyle bodyStyle  = new Label.LabelStyle(game.fontBody,  Color.WHITE);
        Label.LabelStyle smallStyle = new Label.LabelStyle(game.fontSmall, Color.LIGHT_GRAY);
        Label.LabelStyle lockStyle  = new Label.LabelStyle(game.fontSmall, Color.valueOf("FF6347"));

        root.add(new Label("SELECT WORLD", titleStyle)).padTop(60f).padBottom(40f).row();

        addWorldCard(root, "SKY WORLD",   "backgrounds/bg_sky.png",   "Always unlocked",
                true,  Constants.WORLD_SKY,   bodyStyle, smallStyle, lockStyle);
        addWorldCard(root, "CAVE WORLD",  "backgrounds/bg_cave.png",  "Reach " + Constants.CAVE_UNLOCK_HEIGHT + "m to unlock",
                caveUnlocked,  Constants.WORLD_CAVE,  bodyStyle, smallStyle, lockStyle);
        addWorldCard(root, "SPACE WORLD", "backgrounds/bg_space.png", "Reach " + Constants.SPACE_UNLOCK_HEIGHT + "m to unlock",
                spaceUnlocked, Constants.WORLD_SPACE, bodyStyle, smallStyle, lockStyle);

        root.add(new Label("", bodyStyle)).expandY().row();

        TextButton backBtn = new TextButton("BACK",
                makeStyle("sprites/button_grey.png", "sprites/button_grey_pressed.png"));
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playSfx("sounds/sfx/sfx_button_back.ogg");
                game.setScreen(new MainMenuScreen(game));
            }
        });
        root.add(backBtn).size(Constants.BTN_WIDTH_SECONDARY, Constants.BTN_HEIGHT_SECONDARY).padBottom(40f).row();
    }

    private void addWorldCard(Table root, String name, String bgPath, String unlockText,
                               boolean unlocked, int worldId,
                               Label.LabelStyle bodyStyle, Label.LabelStyle smallStyle,
                               Label.LabelStyle lockStyle) {
        Table card = new Table();
        card.setBackground(new TextureRegionDrawable(game.manager.get("backgrounds/bg_main.png", Texture.class)));

        // Thumbnail
        Image thumb = new Image(game.manager.get(bgPath, Texture.class));
        card.add(thumb).size(100f, 70f).padLeft(16f).padRight(16f);

        // Labels
        Table info = new Table();
        info.add(new Label(name, bodyStyle)).left().row();
        if (!unlocked) {
            info.add(new Label(unlockText, lockStyle)).left().row();
        } else {
            info.add(new Label("UNLOCKED", smallStyle)).left().row();
        }
        card.add(info).expandX().left().padRight(16f);

        // Play or lock button
        if (unlocked) {
            TextButton playBtn = new TextButton("PLAY",
                    makeStyle("sprites/button_yellow.png", "sprites/button_yellow_pressed.png"));
            final int wid = worldId;
            playBtn.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent e, Actor a) {
                    playSfx("sounds/sfx/sfx_button_click.ogg");
                    game.setScreen(new GameScreen(game, wid));
                }
            });
            card.add(playBtn).size(100f, 50f).padRight(16f);
        } else {
            ImageButton lockBtn = new ImageButton(
                    new TextureRegionDrawable(game.manager.get("sprites/icon_locked.png", Texture.class)));
            lockBtn.setDisabled(true);
            card.add(lockBtn).size(50f, 50f).padRight(16f);
        }

        root.add(card).fillX().padLeft(20f).padRight(20f).padBottom(18f).row();
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
