package com.baomidou.plugin.idea.mybatisx.codegenerator.utils;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Arrays;

enum Chess {
    RED,
    BLACK,
    NONE
}

public class ChessUtil {
    private static ChessUtil single;
    private final int BASE_X = 100;
    private final int BASE_Y = 100;

    private final int MAX_X = 350 + BASE_X;
    private final int MAX_Y = 350 + BASE_Y;
    Chess[][] chess = new Chess[4][4];
    private Chess currentPlayer;

    public Chess getCurrentPlayer() {
        return currentPlayer;
    }

    public static ChessUtil singleInstance() {
        if (single == null) {
            single = new ChessUtil();
        }
        return single;
    }

    public void initChess() {
        currentPlayer = Chess.BLACK;
        for (int i = 0; i < chess.length; i++) {
            for (int j = 0; j < 4; j++) {
                chess[i][j] = Chess.NONE;
            }
        }
        System.out.println(Arrays.deepToString(chess));

    }

    private void clearChess(Graphics g) {
        final Color color = Color.getColor("FFFFFF");
        for (int i = 0; i < chess.length; i++) {
            for (int j = 0; j < 4; j++) {
                if (chess[i][j] == Chess.BLACK) {
                    drawBLACKCircle(g, i, j, color);
                } else if (chess[i][j] == Chess.RED) {
                    drawRedRect(g, i, j, color);
                }
            }
        }
    }

    public void updateChess(Graphics g) {
        drawLine(g);
        drawChess(g);
    }

    private void drawLine(Graphics g) {
        drawLine(g, 0, 0, 0, 300);
        drawLine(g, 100, 0, 100, 300);
        drawLine(g, 200, 0, 200, 300);
        drawLine(g, 300, 0, 300, 300);

        drawLine(g, 0, 0, 300, 0);
        drawLine(g, 0, 100, 300, 100);
        drawLine(g, 0, 200, 300, 200);
        drawLine(g, 0, 300, 300, 300);
    }

    private void drawChess(Graphics g) {
        for (int i = 0; i < chess.length; i++) {
            for (int j = 0; j < 4; j++) {
                if (chess[i][j] == Chess.BLACK) {
                    drawBLACKCircle(g, i, j, Color.BLACK);
                } else if (chess[i][j] == Chess.RED) {
                    drawRedRect(g, i, j, Color.RED);
                }
            }
        }
    }

    private void drawBLACKCircle(Graphics g, int i, int j, Color color) {
        Graphics2D g2 = (Graphics2D) g;
        final int x = 100 * i + BASE_X;
        final int y = 100 * j + BASE_Y;
        Shape circle = new Ellipse2D.Double(x - 25, y - 25, 50, 50);
        g2.setColor(color);
        g2.draw(circle);
    }

    private void drawRedRect(Graphics g, int i, int j, Color color) {
        final int x = 100 * i + BASE_X;
        final int y = 100 * j + BASE_Y;
        Shape rect = new Rectangle(x - 25, y - 25, 50, 50);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.draw(rect);
    }

    private void drawLine(Graphics g, int x1, int y1, int x2, int y2) {
        Shape line = new Line2D.Double(x1 + BASE_X, y1 + BASE_Y, x2 + BASE_X, y2 + BASE_Y);
        Graphics2D g2 = (Graphics2D) g;
        g2.draw(line);
    }


    public void putChess(int x, int y) {

        if (x < BASE_X - 50 || y < BASE_Y - 50) {
            return;
        }
        if (x > MAX_X || y > MAX_Y) {
            return;
        }
        final int xIndex = (x + 50 - BASE_X) / 100;
        final int yIndex = (y + 50 - BASE_Y) / 100;
        if (chess[xIndex][yIndex] != Chess.NONE) {
            return;
        }
        //turn another player to put chess
        chess[xIndex][yIndex] = currentPlayer;
        currentPlayer = currentPlayer == Chess.BLACK ? Chess.RED : Chess.BLACK;
    }

    public void startGame(Graphics g) {
        initChess();
    }
}
