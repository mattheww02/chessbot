package com.example.chessbot;

import chesspresso.position.Position;

public class ChessGame {
    public static void main(String[] args) {
        System.out.println("ClassPath: " + System.getProperty("java.class.path"));
        Position position = new Position();
        System.out.println("Initial Chess Position: " + position);
    }
}