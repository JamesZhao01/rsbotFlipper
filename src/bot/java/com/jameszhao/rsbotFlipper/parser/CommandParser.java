package com.jameszhao.rsbotFlipper.parser;

import com.jameszhao.rsbotFlipper.common.CliCommand;

import java.util.Arrays;

public class CommandParser {
    private CommandParser() {
    }

    public static CliCommand parseCliCommand(String text) throws ParserException {
        if (text == null) {
            throw new ParserException("Text was null.");
        }
        text = text.trim();
        String[] tokens = text.split(" ");
        if (tokens.length == 0) {
            throw new ParserException("Too few arguments.");
        }
        try {
            return new CliCommand(CliCommand.CliCommandType.valueOf(tokens[0].toUpperCase()),
                    Arrays.asList(Arrays.copyOfRange(tokens, 1, tokens.length)));
        } catch (IllegalArgumentException e) {
            throw new ParserException(String.format("Command not recognized: %s", tokens[0].toUpperCase()));
        }
    }

    public static class ParserException extends Exception {
        ParserException(String exceptionText) {
            super(exceptionText);
        }
    }
}
