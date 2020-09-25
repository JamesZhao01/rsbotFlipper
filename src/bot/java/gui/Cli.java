package gui;


import controller.command.CliCommandHandler;
import common.Globals;
import logger.ScriptLogger;
import org.osbot.rs07.script.Script;
import parser.CommandParser;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Cli extends JFrame {
    private static ScriptLogger scriptLogger;
    private final JTextArea textArea = new JTextArea(10, 40);
    private final JTextField textField = new JTextField("");
    private final JScrollPane scroll = new JScrollPane(textArea);
    private CliCommandHandler commandHandler;
    private Script script;

    public Cli(CliCommandHandler commandHandler) {
        script = Globals.getScript();
        scriptLogger = new ScriptLogger("Cli");
        this.commandHandler = commandHandler;
        setTitle("CLI");
        setSize(750, 250);
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        getContentPane().add(BorderLayout.CENTER, scroll);
        getContentPane().add(BorderLayout.SOUTH, textField);
        textField.addActionListener((ActionEvent e) -> {
            String text = textField.getText();
            if (text.equals(""))
                return;
            textArea.append(String.format("%s%n", text));
            textField.setText("");
            try {
                this.commandHandler.handleCommand(CommandParser.parseCliCommand(text));
            } catch (CommandParser.ParserException ex) {
                scriptLogger.w(ex.getMessage());
            }
        });
        textArea.setFocusable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                script.stop(false);
            }
        });
        scriptLogger.i("Finished Building GUI");
    }

}
