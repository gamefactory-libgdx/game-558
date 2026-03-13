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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.asocity.bounceup680028.Constants;
import com.asocity.bounceup680028.MainGame;

public class CreditsScreen implements Screen {

    private final MainGame game;
    private OrthographicCamera camera;
    private StretchViewport    viewport;
    private Stage              stage;

    public CreditsScreen(MainGame game) {
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

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label.LabelStyle titleStyle   = new Label.LabelStyle(game.fontTitle, Color.WHITE);
        Label.LabelStyle sectionStyle = new Label.LabelStyle(game.fontBody,  Color.valueOf("FFD700"));
        Label.LabelStyle bodyStyle    = new Label.LabelStyle(game.fontSmall, Color.WHITE);

        root.add(new Label("CREDITS", titleStyle)).padTop(50f).padBottom(20f).row();

        // Scrollable credits content
        Table content = new Table();
        content.defaults().left().padBottom(6f);

        addSection(content, "GAME",        sectionStyle, bodyStyle, "Bounce Up v1.0");
        addSection(content, "FRAMEWORK",   sectionStyle, bodyStyle, "libGDX 1.14.0", "libgdx.com");
        addSection(content, "SPRITES",     sectionStyle, bodyStyle,
                "Kenney (kenney.nl)", "Jumper Pack — CC0 1.0 Universal");
        addSection(content, "FONTS",       sectionStyle, bodyStyle,
                "Orbitron — Vernon Adams (SIL Open Font)",
                "Roboto — Google (Apache 2.0)");
        addSection(content, "SOUNDS",      sectionStyle, bodyStyle,
                "OpenGameArt.org contributors",
                "Various CC0 licensed SFX and music");
        addSection(content, "BUILT WITH",  sectionStyle, bodyStyle,
                "Gradle", "Android SDK");

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFadeScrollBars(false);
        root.add(scroll).expand().fillX().padLeft(30f).padRight(30f).padBottom(20f).row();

        // Main Menu button
        TextButton menuBtn = new TextButton("MAIN MENU",
                makeStyle("sprites/button_grey.png", "sprites/button_grey_pressed.png"));
        menuBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playSfx("sounds/sfx/sfx_button_back.ogg");
                game.setScreen(new MainMenuScreen(game));
            }
        });
        root.add(menuBtn).size(Constants.BTN_WIDTH_SECONDARY, Constants.BTN_HEIGHT_SECONDARY)
                .padBottom(40f).row();
    }

    private void addSection(Table table, String heading, Label.LabelStyle sectionStyle,
                             Label.LabelStyle bodyStyle, String... lines) {
        table.add(new Label(heading, sectionStyle)).left().padTop(14f).row();
        for (String line : lines) {
            table.add(new Label(line, bodyStyle)).left().padLeft(16f).row();
        }
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
