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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.asocity.bounceup680028.Constants;
import com.asocity.bounceup680028.MainGame;

public class GameScreen implements Screen {

    // -------------------------------------------------------------------------
    // Inner data classes
    // -------------------------------------------------------------------------

    private static class PlatformData {
        float x, y;
        boolean broken;
        boolean crumbling;
        float   crumbleTimer;
        boolean gone;
        Rectangle rect = new Rectangle();
        void updateRect() { rect.set(x, y, Constants.PLATFORM_WIDTH, Constants.PLATFORM_HEIGHT); }
    }

    private static class StarData {
        float x, y;
        boolean collected;
        Rectangle rect = new Rectangle();
        void updateRect() { rect.set(x, y, Constants.STAR_SIZE, Constants.STAR_SIZE); }
    }

    private static class HazardData {
        float x, y;
        boolean flying;
        float   velX;
        boolean dead;
        Rectangle rect = new Rectangle();
        void updateRect() { rect.set(x, y, Constants.HAZARD_WIDTH, Constants.HAZARD_HEIGHT); }
    }

    private static class SpringData {
        float x, y;
        boolean consumed;
        Rectangle rect = new Rectangle();
        void updateRect() { rect.set(x, y, 32f, 32f); }
    }

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    final MainGame game;
    final int      worldId;

    private OrthographicCamera gameCamera;
    private StretchViewport    gameViewport;
    private OrthographicCamera hudCamera;
    private StretchViewport    hudViewport;
    private Stage              hudStage;

    private InputMultiplexer inputMultiplexer;

    // Player
    private float playerX, playerY;
    private float playerVelX, playerVelY;
    private Rectangle playerRect = new Rectangle();

    // World objects
    private final Array<PlatformData> platforms = new Array<>();
    private final Array<StarData>     stars     = new Array<>();
    private final Array<HazardData>   hazards   = new Array<>();
    private final Array<SpringData>   springs   = new Array<>();
    private float highestPlatformY;

    // Camera / height
    private float cameraY;
    private float maxPlayerY;
    private int   heightMetres;
    private int   starsCollected;

    // Power-ups
    private boolean shieldActive;
    private float   shieldTimer;
    private boolean magnetActive;
    private float   magnetTimer;
    private boolean doubleScore;

    // Game state
    private boolean gameOver;

    // HUD labels
    private Label heightLabel;
    private Label starsLabel;

    // Texture shortcuts (all from AssetManager)
    private Texture bgTex;
    private Texture platformNormalTex;
    private Texture platformBrokenTex;
    private Texture playerStandTex;
    private Texture playerJumpTex;
    private Texture hazardStaticTex;
    private Texture hazardFlyTex;
    private Texture springTex;
    private Texture starTex;

    // Skin selection
    private int skinIndex;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public GameScreen(MainGame game, int worldId) {
        this.game    = game;
        this.worldId = worldId;

        gameCamera   = new OrthographicCamera();
        gameViewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, gameCamera);
        hudCamera    = new OrthographicCamera();
        hudViewport  = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, hudCamera);
        hudStage     = new Stage(hudViewport, game.batch);

        setupInput();

        // Activate power-ups from prefs (one-time use per run)
        Preferences prefs = Gdx.app.getPreferences(Constants.PREF_FILE);
        if (prefs.getBoolean(Constants.PREF_SHIELD_OWNED, false)) {
            shieldActive = true;
            shieldTimer  = Constants.SHIELD_DURATION;
            prefs.putBoolean(Constants.PREF_SHIELD_OWNED, false);
        }
        if (prefs.getBoolean(Constants.PREF_MAGNET_OWNED, false)) {
            magnetActive = true;
            magnetTimer  = Constants.MAGNET_DURATION;
            prefs.putBoolean(Constants.PREF_MAGNET_OWNED, false);
        }
        if (prefs.getBoolean(Constants.PREF_DOUBLE_OWNED, false)) {
            doubleScore = true;
            prefs.putBoolean(Constants.PREF_DOUBLE_OWNED, false);
        }
        prefs.flush();

        skinIndex = prefs.getInteger(Constants.PREF_SKIN, 0);

        resolveTextures();
        initWorld();
        buildHUD();

        game.playMusic("sounds/music/music_gameplay.ogg");
    }

    private void setupInput() {
        inputMultiplexer = new InputMultiplexer(hudStage, new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    navigateToPause();
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void resolveTextures() {
        switch (worldId) {
            case Constants.WORLD_CAVE:
                bgTex            = game.manager.get("backgrounds/bg_cave.png",  Texture.class);
                platformNormalTex = game.manager.get("sprites/jumper/platform_stone.png", Texture.class);
                platformBrokenTex = game.manager.get("sprites/jumper/platform_wood.png",  Texture.class);
                break;
            case Constants.WORLD_SPACE:
                bgTex            = game.manager.get("backgrounds/bg_space.png", Texture.class);
                platformNormalTex = game.manager.get("sprites/jumper/platform_snow.png", Texture.class);
                platformBrokenTex = game.manager.get("sprites/jumper/platform_sand.png", Texture.class);
                break;
            default: // WORLD_SKY
                bgTex            = game.manager.get("backgrounds/bg_sky.png",  Texture.class);
                platformNormalTex = game.manager.get("sprites/jumper/platform_grass.png",        Texture.class);
                platformBrokenTex = game.manager.get("sprites/jumper/platform_grass_broken.png", Texture.class);
                break;
        }

        String prefix = (skinIndex == 1) ? "sprites/jumper/player2_" : "sprites/jumper/player_";
        playerStandTex  = game.manager.get(prefix + "stand.png", Texture.class);
        playerJumpTex   = game.manager.get(prefix + "jump.png",  Texture.class);
        hazardStaticTex = game.manager.get("sprites/jumper/hazard_spikeball.png", Texture.class);
        hazardFlyTex    = game.manager.get("sprites/jumper/enemy_fly_stand.png",  Texture.class);
        springTex       = game.manager.get("sprites/jumper/item_spring.png",      Texture.class);
        starTex         = game.manager.get("sprites/star.png",                    Texture.class);
    }

    private void initWorld() {
        playerX   = Constants.PLAYER_START_X;
        playerY   = Constants.PLAYER_START_Y;
        playerVelX = 0f;
        playerVelY = Constants.BOUNCE_VELOCITY;
        cameraY   = 0f;
        maxPlayerY = playerY;

        // First solid platform directly under player
        PlatformData first = new PlatformData();
        first.x = playerX - (Constants.PLATFORM_WIDTH - Constants.PLAYER_WIDTH) / 2f;
        first.y = playerY - Constants.PLATFORM_HEIGHT;
        first.broken = false;
        first.updateRect();
        platforms.add(first);
        highestPlatformY = first.y;

        // Pre-generate two screens of platforms
        generatePlatformsUpTo(Constants.WORLD_HEIGHT * 2f);
    }

    private void buildHUD() {
        Table hud = new Table();
        hud.setFillParent(true);
        hud.top();

        Label.LabelStyle hudStyle = new Label.LabelStyle(game.fontHud, Color.WHITE);
        heightLabel = new Label("HEIGHT: 0m", hudStyle);
        starsLabel  = new Label("* 0",         hudStyle);

        // Pause button (round, top-right area)
        ImageButton pauseBtn = new ImageButton(
                new TextureRegionDrawable(game.manager.get("sprites/icon_pause.png", Texture.class)),
                new TextureRegionDrawable(game.manager.get("sprites/icon_pause.png", Texture.class)));
        pauseBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                navigateToPause();
            }
        });

        Table topRow = new Table();
        topRow.add(heightLabel).expandX().left().padLeft(16f);
        topRow.add(starsLabel).padRight(16f);
        topRow.add(pauseBtn).size(Constants.PAUSE_BTN_SIZE, Constants.PAUSE_BTN_SIZE).padRight(16f);
        hud.add(topRow).fillX().padTop(12f).row();

        hudStage.addActor(hud);
    }

    // -------------------------------------------------------------------------
    // Platform / object generation
    // -------------------------------------------------------------------------

    private void generatePlatformsUpTo(float targetY) {
        while (highestPlatformY < targetY) {
            float gap = MathUtils.random(Constants.PLATFORM_MIN_GAP, Constants.PLATFORM_MAX_GAP);
            highestPlatformY += gap;

            PlatformData p = new PlatformData();
            p.x = MathUtils.random(0f, Constants.WORLD_WIDTH - Constants.PLATFORM_WIDTH);
            p.y = highestPlatformY;
            p.broken = MathUtils.random() < Constants.BROKEN_PLATFORM_CHANCE;
            p.updateRect();
            platforms.add(p);

            // Star above platform
            if (MathUtils.random() < Constants.STAR_SPAWN_CHANCE) {
                StarData s = new StarData();
                s.x = p.x + (Constants.PLATFORM_WIDTH - Constants.STAR_SIZE) / 2f;
                s.y = p.y + Constants.PLATFORM_HEIGHT + 8f;
                s.updateRect();
                stars.add(s);
            }

            // Hazards — spawn after first 50m of height
            float heightSoFar = highestPlatformY / Constants.PIXELS_PER_METRE;
            if (heightSoFar > 50f && MathUtils.random() < 0.12f) {
                HazardData h = new HazardData();
                h.x = MathUtils.random(0f, Constants.WORLD_WIDTH - Constants.HAZARD_WIDTH);
                h.y = highestPlatformY + MathUtils.random(20f, 70f);
                h.flying = (worldId == Constants.WORLD_SPACE) ||
                           (worldId == Constants.WORLD_CAVE && MathUtils.random() < 0.4f) ||
                           (worldId == Constants.WORLD_SKY  && MathUtils.random() < 0.2f);
                if (h.flying) {
                    h.velX = MathUtils.randomSign() * MathUtils.random(80f, 180f);
                }
                h.updateRect();
                hazards.add(h);
            }

            // Spring items — rare
            if (MathUtils.random() < 0.05f) {
                SpringData sp = new SpringData();
                sp.x = p.x + (Constants.PLATFORM_WIDTH - 32f) / 2f;
                sp.y = p.y + Constants.PLATFORM_HEIGHT;
                sp.updateRect();
                springs.add(sp);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Update
    // -------------------------------------------------------------------------

    private void update(float delta) {
        // Horizontal input via accelerometer (tilt left/right)
        float accelX = Gdx.input.getAccelerometerX();
        playerVelX = MathUtils.clamp(-accelX * 28f, -Constants.PLAYER_MOVE_SPEED, Constants.PLAYER_MOVE_SPEED);

        // Keyboard fallback for development
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  playerVelX = -Constants.PLAYER_MOVE_SPEED;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) playerVelX =  Constants.PLAYER_MOVE_SPEED;

        // Apply gravity
        playerVelY += Constants.GRAVITY * delta;
        playerVelY = MathUtils.clamp(playerVelY, Constants.MAX_FALL_SPEED, 1500f);

        // Move player
        playerX += playerVelX * delta;
        playerY += playerVelY * delta;

        // Screen wrap
        if (playerX + Constants.PLAYER_WIDTH < 0f)     playerX = Constants.WORLD_WIDTH;
        else if (playerX > Constants.WORLD_WIDTH)       playerX = -Constants.PLAYER_WIDTH;

        playerRect.set(playerX, playerY, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);

        // Track max height
        if (playerY > maxPlayerY) {
            maxPlayerY = playerY;
            heightMetres = (int)(maxPlayerY / Constants.PIXELS_PER_METRE);
            heightLabel.setText("HEIGHT: " + heightMetres + "m");
        }

        // Camera: only scrolls up
        float desiredCamY = playerY - Constants.CAMERA_LEAD;
        if (desiredCamY > cameraY) cameraY = desiredCamY;
        cameraY = Math.max(cameraY, 0f);

        // Generate more platforms ahead of camera top
        generatePlatformsUpTo(cameraY + Constants.WORLD_HEIGHT + Constants.PLATFORM_MAX_GAP * 3f);

        // Platform collision (only when falling)
        if (playerVelY <= 0f) {
            checkPlatformCollisions();
        }

        // Update crumbling platforms; remove gone or below-camera ones
        for (int i = platforms.size - 1; i >= 0; i--) {
            PlatformData p = platforms.get(i);
            if (p.crumbling) {
                p.crumbleTimer += delta;
                if (p.crumbleTimer > 0.35f) p.gone = true;
            }
            if (p.gone || p.y + Constants.PLATFORM_HEIGHT < cameraY - 200f) {
                platforms.removeIndex(i);
            }
        }

        // Stars
        updateStars(delta);

        // Hazards
        updateHazards(delta);

        // Springs
        checkSprings();

        // Power-up timers
        if (shieldActive) { shieldTimer -= delta; if (shieldTimer <= 0f) shieldActive = false; }
        if (magnetActive) { magnetTimer -= delta; if (magnetTimer <= 0f) magnetActive = false; }

        // Game over: fell below camera bottom
        if (playerY + Constants.PLAYER_HEIGHT < cameraY) {
            triggerGameOver();
        }
    }

    private void checkPlatformCollisions() {
        float playerBottom = playerY;
        for (int i = 0; i < platforms.size; i++) {
            PlatformData p = platforms.get(i);
            if (p.gone || p.crumbling) continue;
            float platTop = p.y + Constants.PLATFORM_HEIGHT;
            // Player feet are between platform bottom and top — landed
            if (playerBottom >= p.y &&
                playerBottom <= platTop + 5f &&
                playerX + Constants.PLAYER_WIDTH > p.x &&
                playerX < p.x + Constants.PLATFORM_WIDTH) {

                playerY    = platTop;
                playerVelY = Constants.BOUNCE_VELOCITY;
                playSfx("sounds/sfx/sfx_jump.ogg");

                if (p.broken) {
                    p.crumbling    = true;
                    p.crumbleTimer = 0f;
                }
                break;
            }
        }
    }

    private void updateStars(float delta) {
        for (int i = stars.size - 1; i >= 0; i--) {
            StarData s = stars.get(i);
            if (s.collected) { stars.removeIndex(i); continue; }
            // Remove stars that fell well below camera
            if (s.y + Constants.STAR_SIZE < cameraY - 100f) { stars.removeIndex(i); continue; }

            // Magnet pull
            if (magnetActive) {
                float dx = (playerX + Constants.PLAYER_WIDTH / 2f) - (s.x + Constants.STAR_SIZE / 2f);
                float dy = (playerY + Constants.PLAYER_HEIGHT / 2f) - (s.y + Constants.STAR_SIZE / 2f);
                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                if (dist < Constants.MAGNET_RADIUS) {
                    float speed = 400f;
                    s.x += (dx / dist) * speed * delta;
                    s.y += (dy / dist) * speed * delta;
                    s.updateRect();
                }
            }

            s.updateRect();
            if (playerRect.overlaps(s.rect)) {
                s.collected = true;
                int earned = doubleScore ? 2 : 1;
                starsCollected += earned;
                starsLabel.setText("* " + starsCollected);
                playSfx("sounds/sfx/sfx_coin.ogg");
            }
        }
    }

    private void updateHazards(float delta) {
        for (int i = hazards.size - 1; i >= 0; i--) {
            HazardData h = hazards.get(i);
            if (h.dead) { hazards.removeIndex(i); continue; }
            if (h.y + Constants.HAZARD_HEIGHT < cameraY - 200f) { hazards.removeIndex(i); continue; }

            // Move flying hazards horizontally
            if (h.flying) {
                h.x += h.velX * delta;
                if (h.x < 0f || h.x + Constants.HAZARD_WIDTH > Constants.WORLD_WIDTH) {
                    h.velX = -h.velX;
                    h.x = MathUtils.clamp(h.x, 0f, Constants.WORLD_WIDTH - Constants.HAZARD_WIDTH);
                }
                h.updateRect();
            }

            if (!shieldActive && playerRect.overlaps(h.rect)) {
                triggerGameOver();
                return;
            }
        }
    }

    private void checkSprings() {
        for (int i = springs.size - 1; i >= 0; i--) {
            SpringData sp = springs.get(i);
            if (sp.consumed) { springs.removeIndex(i); continue; }
            if (sp.y < cameraY - 200f) { springs.removeIndex(i); continue; }
            sp.updateRect();
            if (playerRect.overlaps(sp.rect) && playerVelY <= 0f) {
                playerVelY = Constants.BOUNCE_VELOCITY * 1.8f;  // super-bounce
                sp.consumed = true;
                playSfx("sounds/sfx/sfx_jump.ogg");
            }
        }
    }

    private void triggerGameOver() {
        if (gameOver) return;
        gameOver = true;
        playSfx("sounds/sfx/sfx_hit.ogg");
        game.setScreen(new GameOverScreen(game, heightMetres, starsCollected));
    }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------

    void navigateToPause() {
        game.setScreen(new PauseScreen(game, this, worldId));
    }

    // -------------------------------------------------------------------------
    // Rendering
    // -------------------------------------------------------------------------

    @Override
    public void render(float delta) {
        if (!gameOver) update(delta);

        Gdx.gl.glClearColor(0.05f, 0.05f, 0.15f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Apply game viewport & update camera
        gameViewport.apply();
        gameCamera.position.set(Constants.WORLD_WIDTH / 2f, cameraY + Constants.WORLD_HEIGHT / 2f, 0f);
        gameCamera.update();
        game.batch.setProjectionMatrix(gameCamera.combined);

        game.batch.begin();
        drawWorld();
        game.batch.end();

        // HUD (fixed camera)
        hudViewport.apply();
        hudCamera.position.set(Constants.WORLD_WIDTH / 2f, Constants.WORLD_HEIGHT / 2f, 0f);
        hudCamera.update();
        hudStage.act(delta);
        hudStage.draw();
    }

    private void drawWorld() {
        // Background — fill visible world area
        game.batch.draw(bgTex, 0f, cameraY, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        // Platforms
        for (int i = 0; i < platforms.size; i++) {
            PlatformData p = platforms.get(i);
            if (p.gone) continue;
            if (p.crumbling) {
                float alpha = MathUtils.clamp(1f - p.crumbleTimer / 0.35f, 0f, 1f);
                game.batch.setColor(1f, 1f, 1f, alpha);
            }
            Texture tex = p.broken ? platformBrokenTex : platformNormalTex;
            game.batch.draw(tex, p.x, p.y, Constants.PLATFORM_WIDTH, Constants.PLATFORM_HEIGHT);
            if (p.crumbling) game.batch.setColor(Color.WHITE);
        }

        // Stars
        for (int i = 0; i < stars.size; i++) {
            StarData s = stars.get(i);
            if (!s.collected) {
                game.batch.draw(starTex, s.x, s.y, Constants.STAR_SIZE, Constants.STAR_SIZE);
            }
        }

        // Springs
        for (int i = 0; i < springs.size; i++) {
            SpringData sp = springs.get(i);
            if (!sp.consumed) {
                game.batch.draw(springTex, sp.x, sp.y, 32f, 32f);
            }
        }

        // Hazards
        for (int i = 0; i < hazards.size; i++) {
            HazardData h = hazards.get(i);
            if (!h.dead) {
                Texture tex = h.flying ? hazardFlyTex : hazardStaticTex;
                game.batch.draw(tex, h.x, h.y, Constants.HAZARD_WIDTH, Constants.HAZARD_HEIGHT);
            }
        }

        // Shield glow tint when active
        if (shieldActive) game.batch.setColor(0.5f, 0.8f, 1f, 1f);
        Texture pTex = playerVelY > 0f ? playerJumpTex : playerStandTex;
        game.batch.draw(pTex, playerX, playerY, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
        if (shieldActive) game.batch.setColor(Color.WHITE);
    }

    // -------------------------------------------------------------------------
    // Screen lifecycle
    // -------------------------------------------------------------------------

    @Override
    public void show() {
        // Re-register input processor when returning from PauseScreen
        Gdx.input.setInputProcessor(inputMultiplexer);
        game.playMusic("sounds/music/music_gameplay.ogg");
    }

    @Override
    public void resize(int w, int h) {
        gameViewport.update(w, h, true);
        hudViewport.update(w, h, true);
    }

    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        hudStage.dispose();
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private void playSfx(String path) {
        if (game.sfxEnabled) game.manager.get(path, Sound.class).play(1.0f);
    }
}
