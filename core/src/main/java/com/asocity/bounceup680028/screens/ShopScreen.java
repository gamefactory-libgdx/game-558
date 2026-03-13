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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.asocity.bounceup680028.Constants;
import com.asocity.bounceup680028.MainGame;

public class ShopScreen implements Screen {

    private final MainGame game;
    private OrthographicCamera camera;
    private StretchViewport    viewport;
    private Stage              stage;

    // Live state refreshed on each purchase
    private int totalStars;
    private Label currencyLabel;

    public ShopScreen(MainGame game) {
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

        Preferences prefs = Gdx.app.getPreferences(Constants.PREF_FILE);
        totalStars = prefs.getInteger(Constants.PREF_STARS, 0);

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

        Label.LabelStyle titleStyle  = new Label.LabelStyle(game.fontTitle, Color.WHITE);
        Label.LabelStyle bodyStyle   = new Label.LabelStyle(game.fontBody,  Color.WHITE);
        Label.LabelStyle sectionStyle= new Label.LabelStyle(game.fontBody,  Color.valueOf("FFD700"));
        Label.LabelStyle smallStyle  = new Label.LabelStyle(game.fontSmall, Color.LIGHT_GRAY);

        root.add(new Label("SHOP", titleStyle)).padTop(50f).padBottom(6f).row();

        // Currency display
        currencyLabel = new Label("STARS: " + totalStars, sectionStyle);
        root.add(currencyLabel).padBottom(20f).row();

        // --- POWER-UPS ---
        root.add(new Label("POWER-UPS", sectionStyle)).padBottom(10f).row();
        addPowerUpRow(root, "Shield",     "5s invincibility",  Constants.PRICE_SHIELD,
                Constants.PREF_SHIELD_OWNED, bodyStyle, smallStyle);
        addPowerUpRow(root, "Magnet",     "Auto-collect 10s",  Constants.PRICE_MAGNET,
                Constants.PREF_MAGNET_OWNED, bodyStyle, smallStyle);
        addPowerUpRow(root, "2x Score",   "Double stars",      Constants.PRICE_DOUBLE_SCORE,
                Constants.PREF_DOUBLE_OWNED, bodyStyle, smallStyle);

        // --- CHARACTER SKINS ---
        root.add(new Label("CHARACTER SKINS", sectionStyle)).padTop(20f).padBottom(10f).row();
        addSkinRow(root, "Default Skin", "FREE", 0, bodyStyle, smallStyle);
        addSkinRow(root, "Alternate Skin", Constants.PRICE_SKIN_2 + " stars", 1, bodyStyle, smallStyle);

        root.add(new Label("", bodyStyle)).expandY().row();

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

    private void addPowerUpRow(Table root, String name, String desc, int price,
                                String prefKey, Label.LabelStyle bodyStyle, Label.LabelStyle smallStyle) {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREF_FILE);
        boolean owned = prefs.getBoolean(prefKey, false);

        Table row = new Table();
        Table info = new Table();
        info.add(new Label(name, bodyStyle)).left().row();
        info.add(new Label(desc, smallStyle)).left().row();
        row.add(info).expandX().left().padLeft(20f);

        if (owned) {
            Label ownedLabel = new Label("READY", new Label.LabelStyle(game.fontSmall, Color.valueOf("00FF88")));
            row.add(ownedLabel).padRight(20f);
        } else {
            TextButton buyBtn = new TextButton(price + " *",
                    makeStyle("sprites/button_blue.png", "sprites/button_blue_pressed.png"));
            final String pk   = prefKey;
            final int    cost = price;
            buyBtn.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent e, Actor a) {
                    tryBuyPowerUp(pk, cost);
                }
            });
            row.add(buyBtn).size(100f, 50f).padRight(20f);
        }
        root.add(row).fillX().padBottom(10f).row();
    }

    private void addSkinRow(Table root, String name, String priceLabel, int skinIdx,
                             Label.LabelStyle bodyStyle, Label.LabelStyle smallStyle) {
        Preferences prefs   = Gdx.app.getPreferences(Constants.PREF_FILE);
        int currentSkin     = prefs.getInteger(Constants.PREF_SKIN, 0);
        boolean isEquipped  = currentSkin == skinIdx;
        boolean isOwned     = skinIdx == 0 || totalStars >= 0;  // 0 = always owned; 1 = purchasable

        // Skin 1 is owned if stars were spent (check via a separate pref)
        boolean skin1Owned = prefs.getBoolean("skin1Owned", false);
        if (skinIdx == 1) isOwned = skin1Owned;

        String prefix = (skinIdx == 1) ? "sprites/jumper/player2_stand.png" : "sprites/jumper/player_stand.png";
        Image skinPreview = new Image(game.manager.get(prefix, Texture.class));

        Table row = new Table();
        row.add(skinPreview).size(48f, 48f).padLeft(20f).padRight(16f);

        Table info = new Table();
        info.add(new Label(name, bodyStyle)).left().row();
        info.add(new Label(priceLabel, smallStyle)).left().row();
        row.add(info).expandX().left();

        if (isEquipped) {
            row.add(new Label("EQUIPPED", new Label.LabelStyle(game.fontSmall, Color.valueOf("00FF88"))))
                    .padRight(20f);
        } else if (skinIdx == 0 || isOwned) {
            TextButton equipBtn = new TextButton("EQUIP",
                    makeStyle("sprites/button_green.png", "sprites/button_green_pressed.png"));
            final int idx = skinIdx;
            equipBtn.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent e, Actor a) {
                    playSfx("sounds/sfx/sfx_button_click.ogg");
                    Preferences p = Gdx.app.getPreferences(Constants.PREF_FILE);
                    p.putInteger(Constants.PREF_SKIN, idx);
                    p.flush();
                    // Rebuild UI to show updated equipped state
                    stage.clear();
                    buildUI();
                }
            });
            row.add(equipBtn).size(100f, 50f).padRight(20f);
        } else {
            // Purchase skin
            TextButton buyBtn = new TextButton(Constants.PRICE_SKIN_2 + " *",
                    makeStyle("sprites/button_blue.png", "sprites/button_blue_pressed.png"));
            buyBtn.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent e, Actor a) {
                    tryBuySkin(skinIdx);
                }
            });
            row.add(buyBtn).size(100f, 50f).padRight(20f);
        }
        root.add(row).fillX().padBottom(10f).row();
    }

    private void tryBuyPowerUp(String prefKey, int cost) {
        if (totalStars < cost) {
            playSfx("sounds/sfx/sfx_hit.ogg");
            return;
        }
        totalStars -= cost;
        Preferences prefs = Gdx.app.getPreferences(Constants.PREF_FILE);
        prefs.putInteger(Constants.PREF_STARS, totalStars);
        prefs.putBoolean(prefKey, true);
        prefs.flush();
        playSfx("sounds/sfx/sfx_power_up.ogg");
        // Rebuild UI to reflect purchased state
        stage.clear();
        buildUI();
    }

    private void tryBuySkin(int skinIdx) {
        int cost = (skinIdx == 1) ? Constants.PRICE_SKIN_2 : Constants.PRICE_SKIN_3;
        if (totalStars < cost) {
            playSfx("sounds/sfx/sfx_hit.ogg");
            return;
        }
        totalStars -= cost;
        Preferences prefs = Gdx.app.getPreferences(Constants.PREF_FILE);
        prefs.putInteger(Constants.PREF_STARS, totalStars);
        prefs.putBoolean("skin" + skinIdx + "Owned", true);
        prefs.flush();
        playSfx("sounds/sfx/sfx_power_up.ogg");
        stage.clear();
        buildUI();
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
