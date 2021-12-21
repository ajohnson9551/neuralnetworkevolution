package lunarlander;

import java.awt.*;

public class LunarLanderCanvas extends Canvas {

    private double[] pos = new double[2];
    private double cos = 0;
    private double sin = -1;

    private double fuel = 0;
    private double[] vel = new double[2];

    private int width;
    private int height;

    private int scale = 5;
    private double shipX;
    private int shipY = 300;
    private int shipYOffset = 5;

    private int marginGap = 50;
    private double craterOffset = 0;
    private int moonSurfaceHeight = 50;

    private int score = 0;

    boolean drawEngine = false;
    boolean drawScore = false;

    public LunarLanderCanvas(int width, int height, double[] initialPos, double[] vel) {
        setBackground(Color.BLACK);
        setSize(width, height);
        this.width = width;
        this.height = height;
        this.pos[0] = initialPos[0];
        this.pos[1] = initialPos[1];
        this.shipX = (int) pos[0];
        this.shipY = (height - (int) pos[1]) - moonSurfaceHeight - shipYOffset;
        this.vel = vel;
    }

    public void updateInfo(double[] newPos, double cos, double sin, double fuel) {
        this.cos = cos;
        this.sin = sin;
        this.fuel = fuel;
//        double deltaX = newPos[0] - this.pos[0];
//        if (deltaX > 0) {
//            if (shipX + deltaX < width - marginGap) {
//                shipX += deltaX;
//            } else {
//                deltaX -= (width - marginGap) - shipX;
//                craterOffset -= deltaX;
//            }
//        } else {
//            if (shipX + deltaX > marginGap) {
//                shipX += deltaX;
//            } else {
//                deltaX -= marginGap - shipX;
//                craterOffset -= deltaX;
//            }
//        }
//        this.pos[0] = newPos[0];
        this.pos[1] = newPos[1];
        shipX = goodMod((int) newPos[0], width);
        shipY = (height - (int) newPos[1]) - moonSurfaceHeight - shipYOffset;
    }

    public void setScore(int score) {
        this.score = score;
        drawScore = true;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(0, height-moonSurfaceHeight, width, moonSurfaceHeight);
        g.setColor(Color.DARK_GRAY);
        g.fillOval(goodMod((int) (20 + craterOffset), width), height-moonSurfaceHeight, 100, 20);
        g.fillOval(goodMod((int) (250 + craterOffset), width), height-moonSurfaceHeight+25, 50, 10);
        g.fillOval(goodMod((int) (400 + craterOffset), width), height-moonSurfaceHeight+10, 80, 16);
        g.fillOval(goodMod((int) (650 + craterOffset), width), height-moonSurfaceHeight+15, 75, 15);
        g.setColor(Color.GREEN);
        int[][] ship = getShipPolyPoints();
        g.drawPolygon(ship[0], ship[1], 8);
        if (drawEngine) {
            g.setColor(Color.RED);
            int[][] engine = getEnginePolyPoints();
            g.drawPolygon(engine[0], engine[1], 3);
            g.setColor(Color.YELLOW);
            int[][] engine2 = getEnginePolyPoints2();
            g.drawPolygon(engine2[0], engine2[1], 3);
        }
        g.setColor(Color.WHITE);
        g.drawString("ALTITUDE: " + Math.round(10*pos[1])/10.0, width - 120, 50);
        g.drawString("X VEL: " + Math.round(10*vel[0])/10.0, width - 120, 70);
        g.drawString("Y VEL: " + Math.round(10*vel[1])/10.0, width - 120, 90);
        g.drawString("FUEL: " + Math.round(10*fuel)/10.0, width - 120, 110);
        if (drawScore) {
            g.drawString("Score: " + score, width/2, height/2);
        }
    }

    public int goodMod(int x, int mod) {
        int y = x % mod;
        return y >= 0 ? y : y + mod;
    }

    public int[][] getEnginePolyPoints2() {
        double[][] poly = new double[2][3];
        poly[0][0] = 2;
        poly[1][0] = 2;
        poly[0][1] = 4;
        poly[1][1] = 2;
        poly[0][2] = 3;
        poly[1][2] = 0;
        for (int j = 0; j < 3; j++) {
            poly[0][j] -= 3;
            poly[1][j] -= 1;
            poly[1][j] *= -1;
        }
        rotate(poly);
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 2; i++) {
                poly[i][j] *= scale;
            }
            poly[0][j] += shipX;
            poly[1][j] += shipY;
        }
        return round(poly);
    }

    public int[][] round(double[][] poly) {
        int[][] roundPoly = new int[2][poly[0].length];
        for (int j = 0; j < poly[0].length; j++) {
            for (int i = 0; i < 2; i++) {
                roundPoly[i][j] = (int) poly[i][j];
            }
        }
        return roundPoly;
    }

    public void rotate(double[][] poly) {
        for (int j = 0; j < poly[0].length; j++) {
            double a = poly[0][j];
            double b = poly[1][j];
            poly[0][j] = (a * cos) + (-b * sin);
            poly[1][j] = (a * sin) + (b * cos);
        }
    }

    public int[][] getEnginePolyPoints() {
        double[][] poly = new double[2][3];
        poly[0][0] = 2;
        poly[1][0] = 2;
        poly[0][1] = 4;
        poly[1][1] = 2;
        poly[0][2] = 3;
        poly[1][2] = 1;
        for (int j = 0; j < 3; j++) {
            poly[0][j] -= 3;
            poly[1][j] -= 1;
            poly[1][j] *= -1;
        }
        rotate(poly);
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 2; i++) {
                poly[i][j] *= scale;
            }
            poly[0][j] += shipX;
            poly[1][j] += shipY;
        }
        return round(poly);
    }

    public int[][] getShipPolyPoints() {
        double[][] poly = new double[2][8];
        poly[0][0] = 0;
        poly[1][0] = 0;
        poly[0][1] = 2;
        poly[1][1] = 4;
        poly[0][2] = 4;
        poly[1][2] = 4;
        poly[0][3] = 6;
        poly[1][3] = 0;
        poly[0][4] = 5;
        poly[1][4] = 0;
        poly[0][5] = 4;
        poly[1][5] = 2;
        poly[0][6] = 2;
        poly[1][6] = 2;
        poly[0][7] = 1;
        poly[1][7] = 0;
        for (int j = 0; j < 8; j++) {
            poly[0][j] -= 3;
            poly[1][j] -= 1;
            poly[1][j] *= -1;
        }
        rotate(poly);
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 2; i++) {
                poly[i][j] *= scale;
            }
            poly[0][j] += shipX;
            poly[1][j] += shipY;
        }
        return round(poly);
    }
}
