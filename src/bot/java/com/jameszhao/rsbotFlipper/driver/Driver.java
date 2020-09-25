package com.jameszhao.rsbotFlipper.driver;

import com.jameszhao.rsbotFlipper.controller.connections.AutomaticUpdater;
import com.jameszhao.rsbotFlipper.controller.operations.OperationController;
import com.jameszhao.rsbotFlipper.controller.operations.OperationRunner;
import com.jameszhao.rsbotFlipper.controller.command.CliCommandHandler;
import com.jameszhao.rsbotFlipper.controller.connections.ConnectionHandler;
import com.jameszhao.rsbotFlipper.controller.GrandExchangeAPI;
import com.jameszhao.rsbotFlipper.controller.GrandExchangeInteractionUtils;
import com.jameszhao.rsbotFlipper.common.Globals;
import com.jameszhao.rsbotFlipper.gui.Cli;
import com.jameszhao.rsbotFlipper.gui.mouse.MouseCursor;
import com.jameszhao.rsbotFlipper.gui.mouse.MouseTrail;
import com.jameszhao.rsbotFlipper.logger.ScriptLoggers;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.listener.LoginResponseCodeListener;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * This class represents the entry point to the Script.
 */
@ScriptManifest(name = "flippa", author = "Billy, Jamez", version = 1.246, info = "3", logo = "")
public class Driver extends Script {

    public LoginHandler loginHandler;
    private static Logger scriptLogger;

    private CliCommandHandler commandHandler;
    private ConnectionHandler connectionHandler;
    private OperationRunner operationRunner;
    private OperationController operationController;
    private AutomaticUpdater automaticUpdater;
    private MouseTrail trail;
    private MouseCursor cursor;

    private JFrame cli;

    private ExecutorService blockingThread = Executors.newFixedThreadPool(1);

    @Override
    public void onStart() {
        InputStream in = null;
        try {
            in = this.getDataResourceAsStream("./com.jameszhao.rsbotFlipper.logger.properties");
            LogManager.getLogManager().readConfiguration(new BufferedInputStream(in));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log(in);

        scriptLogger = ScriptLoggers.obtainLogger("Driver", this);

        scriptLogger.info(String.format("Script %s %f Starting", getName(), getVersion()));
//        operationController = new OperationController(-1, false);
//        Globals.setOperationController(operationController);

        Globals.setScript(this);
        Globals.setApi(new GrandExchangeAPI());

        commandHandler = new CliCommandHandler();
        scriptLogger.info("Command Handler Initialized");

        connectionHandler = new ConnectionHandler();
        Globals.setConnectionHandler(connectionHandler);
        scriptLogger.info("Connection Handler Initialized");
//        automaticUpdater = new AutomaticUpdater(connectionHandler);


        loginHandler = new LoginHandler();
        this.getBot().addLoginListener(loginHandler);


        initializeUI();

        this.getCamera().movePitch(67);
        scriptLogger.info("All Initialization Complete");
    }

    @Override
    public void onExit() {
        cli.dispose();
        scriptLogger.info("Cli com.jameszhao.rsbotFlipper.gui destroyed");
    }

    @Override
    public int onLoop() {
        return 3000; //The amount of time in milliseconds before the loop starts over
    }

    @Override
    public void onPaint(Graphics2D g) {
        trail.paint(g);
        cursor.paint(g);
    }

    private void initializeUI() {
        cli = new Cli(commandHandler);
        cli.setVisible(true);
        scriptLogger.info("Cli com.jameszhao.rsbotFlipper.gui displayed");
        trail = new MouseTrail(0, 255, 255, 2000, this);
        cursor = new MouseCursor(52, 4, Color.white, this);
        scriptLogger.info("Rendered cursor");
    }

    /**
     * Run command.
     *
     * @param r the r
     */
    public void runCommand(Runnable r) {
        blockingThread.submit(r);
    }

    /**
     * The type Login handler.
     */
    public class LoginHandler implements LoginResponseCodeListener{
        /**
         * Existing user on screen boolean.
         *
         * @return the boolean
         */
        public boolean existingUserOnScreen() {
            return getColorPicker().colorAt(445, 290).equals(Color.WHITE);
        }

        /**
         * Click existing user.
         */
        public void clickExistingUser() {
            if(existingUserOnScreen()) {
                getMouse().click(445, 290, false);
                new ConditionalSleep(10000) {
                    @Override
                    public boolean condition() throws InterruptedException {
                        return !existingUserOnScreen();
                    }
                }.sleep();
            }
        }

        /**
         * Login button on screen boolean.
         *
         * @return the boolean
         */
        public boolean loginButtonOnScreen() {
            return getColorPicker().colorAt(287, 321).equals(Color.WHITE);
        }

        /**
         * Click username.
         */
        public void clickUsername() {
            getMouse().click(316, 249, false);
            GrandExchangeInteractionUtils.halfSecSleep();
        }

        /**
         * Type username.
         */
        public void typeUsername() {
            getKeyboard().typeString("nowaythisistaken1@gmail.com");
        }

        /**
         * Click password.
         */
        public void clickPassword() {
            getMouse().click(348, 265, false);
            GrandExchangeInteractionUtils.halfSecSleep();
        }

        /**
         * Type password.
         */
        public void typePassword() {
            getKeyboard().typeString("Asdf1234Asdf1234");
            new ConditionalSleep(10000) {
                @Override
                public boolean condition() throws InterruptedException {
                    return !loginButtonOnScreen() && redButtonOnScreen();
                }
            }.sleep();
        }

        //378 85

        /**
         * Gets red button.
         *
         * @return the red button
         */
        public RS2Widget getRedButton() {
            return getWidgets().getWidgetContainingText("CLICK HERE TO PLAY");
        }

        /**
         * Red button on screen boolean.
         *
         * @return the boolean
         */
        public boolean redButtonOnScreen() {
           return getRedButton() !=  null;
        }

        /**
         * Click red button.
         */
        public void clickRedButton() {
            if(redButtonOnScreen()) {
                getRedButton().interact();
                new ConditionalSleep(10000) {
                    @Override
                    public boolean condition() throws InterruptedException {
                        return !redButtonOnScreen();
                    }
                }.sleep();
            }
        }

        /**
         * Login.
         */
        public void login() {
            GrandExchangeInteractionUtils.oneSecSleep();
            if(!getClient().isLoggedIn() && existingUserOnScreen()) {
                clickExistingUser();
            }
            if (!getClient().isLoggedIn() && loginButtonOnScreen()) {
                clickUsername();
                typeUsername();
                typePassword();
            }
            if (redButtonOnScreen()){
                clickRedButton();
            }

        }

        @Override
        public void onResponseCode(int i) throws InterruptedException {
            if (i==4) {
                handleBan();
            }
        }

        private void handleBan() {

        }
    }
}
