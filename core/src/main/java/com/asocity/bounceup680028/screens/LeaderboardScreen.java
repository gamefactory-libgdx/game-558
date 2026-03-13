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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.asocity.bounceup680028.Constants;
import com.asocity.bounceup680028.MainGame;

public class LeaderboardScreen implements Screen {

    private final MainGame game;
    private OrthographicCamera camera;
    private StretchViewport    viewport;
    private Stage              stage;

    // -------------------------------------------------------------------------
    // Static helper — saves score into the top-10 leaderboard in SharedPrefs
    // -------------------------------------------------------------------------

    public static void addScore(int newScore) {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREF_FILE);
        Array<Integer> scores = loadScores(prefs);
        scores.add(newScore);
        scores.sort((a, b) -> b - a); // descending
        // Keep top 10
        while (scores.size > Constants.LEADERBOARD_SIZE) {
            scores.removeIndex(scores.size - 1);
        }
        saveScores(prefs, scores);
    }

    private static Array<Integer> loadScores(Preferences prefs) {
        Array<Integer> result = new Array<>();
        String raw = prefs.getString(Constants.PREF_LEADERBOARD, "");
        if (!raw.isEmpty()) {
            for (String part : raw.split(",")) {
                try { result.add(Integer.parseInt(part.trim())); }
                catch (NumberFormatException ignored) {}
            }
        }
        return result;
    }

    private static void saveScores(Preferences prefs, Array<Integer> scores) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < scores.size; i++) {
            if (i > 0) sb.append(',');
            sb.append(scores.get(i));
        }
        prefs.putString(Constants.PREF_LEADERBOARD, sb.toString());
        prefs.flush();
    }

    // -------------------------------------------------------------------------

    public LeaderboardScreen(MainGame game) {
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
        // Background
        Image bg = new Image(game.manager.get("backgrounds/bg_main.png", Texture.class));
        bg.setSize(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage.addActor(bg);

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, Color.WHITE);
        Label.LabelStyle rankStyle  = new Label.LabelStyle(game.fontBody,  Color.valueOf("FFD700"));
        Label.LabelStyle bodyStyle  = new Label.LabelStyle(game.fontBody,  Color.WHITE);
        Label.LabelStyle emptyStyle = new Label.LabelStyle(game.fontSmall, Color.LIGHT_GRAY);

        root.add(new Label("TOP 10 SCORES", titleStyle)).padTop(60f).padBottom(30f).row();

        // Load scores
        Preferences prefs = Gdx.app.getPreferences(Constants.PREF_FILE);
        Array<Integer> scores = loadScores(prefs);

        if (scores.size == 0) {
            root.add(new Label("No scores yet — go play!", emptyStyle)).padBottom(20f).row();
        } else {
            for (int i = 0; i < scores.size; i++) {
                Table row = new Table();
                row.add(new Label((i + 1) + ".", rankStyle)).width(50f).left();
                row.add(new Label(scores.get(i) + " m",  bodyStyle)).expandX().left();
                root.add(row).fillX().padLeft(60f).padRight(60f).padBottom(10f).row();
            }
        }

        root.add(new Label("", bodyStyle)).expandY().row(); // spacer

        // Main Menu button
        TextButton menuBtn = new TextButton("MAIN MENU",
            makeStyle("sprites/button_grey.png", "sprites/button_grey_pressed.png"));
        menuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playSfx("sounds/sfx/sfx_button_back.ogg");
                game.setScreen(new MainMenuScreen(game));
            }
        });
        root.add(menuBtn).size(Constants.BTN_WIDTH_SECONDARY, Constants.BTN_HEIGHT_SECONDARY)
            .padBottom(40f).row();
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
