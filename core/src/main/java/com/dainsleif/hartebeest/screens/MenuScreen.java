package com.dainsleif.hartebeest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dainsleif.hartebeest.helpers.*;
import com.dainsleif.hartebeest.world.StartAreaMap;

public class MenuScreen implements Screen {
    private SpriteBatch batch;
    private Animation<TextureRegion> animation;
    private VariableTimeAnimation<TextureRegion> logoAnimation; // Change the type here
    private float stateTime;
    private CursorStyle cursorStyle;

    private BitmapFont font;

    private Rectangle startButtonBounds;
    private Rectangle optionsButtonBounds;
    private Rectangle exitButtonBounds;

    private Texture startButtonTexture;
    private Texture optionsButtonTexture;
    private Texture exitButtonTexture;
    private Texture startButtonClickedTexture;
    private Texture optionsButtonClickedTexture;
    private Texture exitButtonClickedTexture;

    private TextureRegion startButton;
    private TextureRegion optionsButton;
    private TextureRegion exitButton;
    private TextureRegion startButtonClicked;
    private TextureRegion optionsButtonClicked;
    private TextureRegion exitButtonClicked;

    private boolean isStartButtonClicked = false;
    private boolean isOptionsButtonClicked = false;
    private boolean isExitButtonClicked = false;


    private boolean isTouched = false;

    Music backgroundMusic;



    @Override
    public void show() {
        // Initialize the SpriteBatch
        batch = new SpriteBatch();

        // Load and play background music
        backgroundMusic = MusicSingleton.getInstance().getBackgroundMusic();
        backgroundMusic.play();

        // Load the cursor style
        cursorStyle = new CursorStyle();
        cursorStyle.changeCursorToHand();


        try {
            // Load sprite sheet and animation frames
            SpriteSheetLoaderJson spriteSheetLoader = new SpriteSheetLoaderJson("Screen/MenuScreen/MenuBG.png", "Screen/MenuScreen/MenuBG.json");
            TextureRegion[] frames = spriteSheetLoader.getFrames();
            System.out.println("Frames loaded: " + frames.length);

            //logo sprite
            SpriteSheetLoaderJson logoSpriteSheetLoader = new SpriteSheetLoaderJson("Screen/MenuScreen/Logo_Myrnhelm.png", "Screen/MenuScreen/Logo_Myrnhelm.json");


            AnimationLoader animationLoader = new AnimationLoader(frames, .1f);
            animation = animationLoader.getAnimation();

            // For logo animation with variable durations
            SpriteSheetLoaderJson.FrameData logoFrameData = logoSpriteSheetLoader.getFramesWithDurations(true);
            logoAnimation = new VariableTimeAnimation<>(logoFrameData.regions, logoFrameData.durations);


            stateTime = 0f; // Start from the beginning of the animation
        } catch (Exception e) {
            System.err.println("Error loading background animation: " + e.getMessage());
            e.printStackTrace();
        }


        // Load button textures
        startButtonTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/Start_btn.png"));
        optionsButtonTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/Settings_btn.png"));
        exitButtonTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/Exit_btn.png"));
        startButtonClickedTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/Start_btn_clicked.png"));
        optionsButtonClickedTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/Settings_btn_clicked.png"));
        exitButtonClickedTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/Exit_btn_clicked.png"));

        // Create TextureRegions for buttons
        startButton = new TextureRegion(startButtonTexture);
        optionsButton = new TextureRegion(optionsButtonTexture);
        exitButton = new TextureRegion(exitButtonTexture);
        startButtonClicked = new TextureRegion(startButtonClickedTexture);
        optionsButtonClicked = new TextureRegion(optionsButtonClickedTexture);
        exitButtonClicked = new TextureRegion(exitButtonClickedTexture);

        float scaleFactor = 1.5f; // Adjust as needed

        // Start Button Bounds
        float startButtonScaledWidth = startButton.getRegionWidth() * scaleFactor;
        float startButtonScaledHeight = startButton.getRegionHeight() * scaleFactor;

        // Define button bounds
        startButtonBounds = new Rectangle(
            (Gdx.graphics.getWidth() - startButtonScaledWidth) / 2,
            (Gdx.graphics.getHeight() / 2) - 100,
            startButtonScaledWidth,
            startButtonScaledHeight
        );
        float optionsButtonScaledWidth = optionsButton.getRegionWidth() * scaleFactor;
        float optionsButtonScaledHeight = optionsButton.getRegionHeight() * scaleFactor;

        optionsButtonBounds = new Rectangle(
            (Gdx.graphics.getWidth() - optionsButtonScaledWidth) / 2,
            (Gdx.graphics.getHeight() / 2) - 200,
            optionsButtonScaledWidth,
            optionsButtonScaledHeight
        );
        float exitButtonScaledWidth = exitButton.getRegionWidth() * scaleFactor;
        float exitButtonScaledHeight = exitButton.getRegionHeight() * scaleFactor;

        exitButtonBounds = new Rectangle(
            (Gdx.graphics.getWidth() - exitButtonScaledWidth) / 2,
            (Gdx.graphics.getHeight() / 2) - 300,
            exitButtonScaledWidth,
            exitButtonScaledHeight
        );
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update animation state time
        stateTime += delta;
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

        // Draw everything
        batch.begin();

        batch.draw(currentFrame, 0, 0, Gdx.graphics.getWidth()+ 50, Gdx.graphics.getHeight());

        TextureRegion logoFrame = logoAnimation.getKeyFrame(stateTime, true);
        float logoX = (Gdx.graphics.getWidth() - logoFrame.getRegionWidth()) / 2;
        float logoY = Gdx.graphics.getHeight() - logoFrame.getRegionHeight() - 50;
        batch.draw(logoFrame, logoX, logoY);

        TextureRegion startTexture = isStartButtonClicked ? startButtonClicked : startButton;
        TextureRegion optionsTexture = isOptionsButtonClicked ? optionsButtonClicked : optionsButton;
        TextureRegion exitTexture = isExitButtonClicked ? exitButtonClicked : exitButton;

        batch.draw(startTexture, startButtonBounds.x, startButtonBounds.y, startButtonBounds.width, startButtonBounds.height);
        batch.draw(optionsTexture, optionsButtonBounds.x, optionsButtonBounds.y, optionsButtonBounds.width, optionsButtonBounds.height);
        batch.draw(exitTexture, exitButtonBounds.x, exitButtonBounds.y, exitButtonBounds.width, exitButtonBounds.height);

        batch.end();

        // Handle input
        if (Gdx.input.isTouched()) {
            if (!isTouched) {
                isTouched = true; // Prevent multitouch issues
                Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
                if (startButtonBounds.contains(touchPos)) {
                    // Handle Start button click
                    isStartButtonClicked = true;
                    ((com.badlogic.gdx.Game) Gdx.app.getApplicationListener()).setScreen(new StartAreaMap());
                    backgroundMusic.stop();
                } else if (optionsButtonBounds.contains(touchPos)) {
                    // Handle Options button click
                    isOptionsButtonClicked = true;
                } else if (exitButtonBounds.contains(touchPos)) {
                    // Handle Exit button click
                    isExitButtonClicked = true;
                    System.out.println("Exit Button clicked!");
                }
            }
        } else {
            if (isStartButtonClicked) {
                isStartButtonClicked = false;
                System.out.println("Start Button clicked!");
            }
            if (isOptionsButtonClicked) {
                isOptionsButtonClicked = false;
                // Switch to options screen
                ((com.badlogic.gdx.Game) Gdx.app.getApplicationListener()).setScreen(new OptionsScreen(backgroundMusic));
            }
            if (isExitButtonClicked) {
                isExitButtonClicked = false;
                System.out.println("Exit Button clicked!");
            }
            isTouched = false;
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }


    @Override
    public void dispose() {
        batch.dispose();
        backgroundMusic.dispose();
        startButtonTexture.dispose();
        optionsButtonTexture.dispose();
        exitButtonTexture.dispose();
    }
}
