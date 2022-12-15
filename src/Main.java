import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static ArrayList<Player> Players = new ArrayList<>();
    public static ArrayList<Food> Meal = new ArrayList<>();
    public static ArrayList<Player> Eaten = new ArrayList<>();
    public static int PLAYER_SPEED = 40;
    public static int FRAME_WIDTH = 1920;
    public static int FRAME_HEIGHT = 1080;
    public static int BORDER_RIGHT = 1920;
    public static int BORDER_DOWN = 1080;
    public static int FOOD_NUMBER = 400;

    public static int Generation = 0;

    public static int iteration = 0;

    public static int ID = 1;

    public static void GameCycle() {
        for (Food food : Meal) {
            for (Player player : Players) {
                if (Math.sqrt(Math.abs(player.x - food.x) ^ 2 + Math.abs(player.y - food.y ) ^ 2) < player.radius + food.radius) { // FIXME
                    player.radius += food.value / (player.radius * 10);
                    food.isEaten = true;
                    break;
                }
            }
        }
        for (Player player_one : Players) {
            for (Player player_two : Players) {
                if (Math.sqrt(Math.abs(player_one.x - player_two.x) ^ 2 + Math.abs(player_one.y - player_two.y) ^ 2) < player_one.radius + player_two.radius) { // FIXME
                    if (player_one.radius > player_two.radius) {
                        player_one.eaten++;
                        player_one.scores += player_two.radius * 10;
                        Eaten.add(player_one);
                    } else if (player_one.radius < player_two.radius) {
                        player_two.eaten++;
                        player_two.scores += player_two.radius * 10;
                        Eaten.add(player_two);
                    }
                }
            }
        }
        for (Player player : Eaten) {
            Players.remove(player);
        }
        Eaten.clear();
        for (Player player : Players) {
            player.x += PLAYER_SPEED * player.lr / player.radius;
            player.y += PLAYER_SPEED * player.ud / player.radius;
        }
        RespawnEatenFood();
    }

    public static void SpawnFood() {
        for (int i = 0; i < FOOD_NUMBER; i++) {
            Meal.add(new Food());
        }
        for (Food food : Meal) {
            Random random = new Random();
            int x_coordinate = random.nextInt(BORDER_RIGHT);
            int y_coordinate = random.nextInt(BORDER_DOWN);
            food.x = x_coordinate;
            food.y = y_coordinate;
//            if(){
//              Респавн, если появился в игроке
//            }
        }
    }

    public static void RespawnEatenFood() {
        for (Food food : Meal) {
            if (food.isEaten) {
                Random random = new Random();
                int x_coordinate = random.nextInt(BORDER_RIGHT);
                int y_coordinate = random.nextInt(BORDER_DOWN);
                food.x = x_coordinate;
                food.y = y_coordinate;
                food.isEaten = false;
            }
        }
    }

    public static void SpawnPlayers(int number) {
        for (int i = 0; i <= number; i++) {
            CreatePlayer(new Player());
        }
    }

    public static void CreatePlayer(Player player) {
        Players.add(player);
        player.id = ID;
        ID++;
        Random random = new Random();
        int x_coordinate = random.nextInt(BORDER_RIGHT);
        int y_coordinate = random.nextInt(BORDER_DOWN);
        player.x = x_coordinate;
        player.y = y_coordinate;
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.createBufferStrategy(2);
        SpawnPlayers(20);
        SpawnFood();
        while (true) {
            long frameLength = 1000 / 60;
            long start = System.currentTimeMillis();
            BufferStrategy bs = frame.getBufferStrategy();
            Graphics2D g = (Graphics2D) bs.getDrawGraphics();
            g.clearRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
            GameCycle();
            draw(g);
            bs.show();
            g.dispose();
            long end = System.currentTimeMillis();
            long len = end - start;
            if (len < frameLength) {
                Thread.sleep(frameLength - len);
            }
        }
    }

    public static void draw(Graphics2D g) {
        for (Player player : Players) {
            g.setColor(Color.RED);
            g.fillOval(player.x, player.y, 2 * (int) player.radius, 2 * (int) player.radius);
        }
        for (Food food : Meal) {
            g.setColor(Color.GREEN);
            g.fillOval(food.x, food.y, 4, 4);
        }
    }
}
