package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;

public class Game extends JFrame implements KeyListener
{
    public static int width = 800;
    public static int height = 800;
    public static boolean up = false;
    public static boolean down = false;
    public static boolean right = false;
    public static boolean left = false;
    public static long tick_in_nano = 1000000;
    public static long time1 = System.nanoTime();
    public static long time2;
    public static int tick_counter = 10;
    public static int temp_tick_counter = tick_counter;
    public static int spawn_counter = 500;
    public static int temp_spawn_counter = spawn_counter;

    public static int p_width = 50;
    public static int p_height = 50;
    public static double x = width/2;
    public static double y = height/2;
    public static double speed = 0.2;
    public static Color color = Color.BLUE;

    public static int bullet_damage  = 101;
    public static double bullet_speed = 1.0;
    public static long bullet_life = 1000000000L;
    public static int bullet_width = 20;
    public static int bullet_height = 20;
    public static Color bullet_color = Color.GREEN;


    public static Canvas canvas = new Canvas();
    public static JLabel shoots_firedl = new JLabel("Shoots fired: ");
    public static JLabel shoots_hitl = new JLabel("Shoots hit: ");
    public static JLabel enemy_destroyedl = new JLabel("Enemies destroyed: ");
    public static JLabel total_enemiesl = new JLabel("Total enemies: ");
    public static JFrame statistics = new JFrame();


    public BufferStrategy bufferStrategy;
    public Graphics g;
    public static boolean continue_game = true;

    public static int shoots_fired = 0;
    public static int shoots_hit = 0;
    public static int enemy_destroyed = 0;
    public static int total_enemies = 0;

    @Override
    public void keyTyped(KeyEvent e)
    {
        if (e.getKeyChar() == 'p')
        {
            System.out.println("test");
            if(continue_game == true)
            {
                continue_game = false;
            }
            else
            {
                continue_game = true;
            }
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if(continue_game)
        {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            {
                right = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT)
            {
                left = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP)
            {
                up = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN)
            {
                down = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if(continue_game)
        {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            {
                right = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT)
            {
                left = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP)
            {
                up = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN)
            {
                down = false;
            }
        }
    }

    public Game()
    {
        super();
        canvas.setSize(width, height);
        canvas.setVisible(true);
        canvas.setFocusable(false);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        statistics.setLayout(new FlowLayout());
        statistics.setSize(200, 200);
        statistics.add(shoots_firedl);
        statistics.add(shoots_hitl);
        statistics.add(total_enemiesl);
        statistics.add(enemy_destroyedl);

        add(canvas);
        addKeyListener(this);
        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        statistics.setResizable(false);
        statistics.setVisible(true);
        statistics.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        canvas.createBufferStrategy(4);
        canvas.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {

            }
            @Override
            public void mousePressed(MouseEvent e)
            {
                if(continue_game)
                {
                    newPlayerBullet(e);
                    shoots_fired++;
                }
            }
        });

       loop();
    }

    public static void newPlayerBullet(MouseEvent e)
    {
        Bullet.newPlayerBullet(e);
    }

    public static void generateNPC()
    {
        NPC.generateNPC(Color.RED, 0.3, 100, 100, 200000000000L, 50000000000L, (int) x, (int) y, 80, 80);
        total_enemies++;
    }

    public void loop()
    {
        while(true)
        {
            while (continue_game)
            {
                time2 = System.nanoTime();
                if (time2 - time1 > tick_in_nano)
                {
                    temp_tick_counter--;
                    time1 = time2;
                    temp_spawn_counter--;
                    if (temp_spawn_counter < 0)
                    {
                        temp_spawn_counter = spawn_counter;
                        generateNPC();
                    }
                    if (up && !down && !right && !left && ((int) y > -1))
                    {
                        y = y - speed;
                    }
                    if (!up && down && !right && !left && (y < Game.height - p_height * 0.75))
                    {
                        y = y + speed;
                    }
                    if (!up && !down && right && !left && ((int) x < Game.width - p_width * 0.25))
                    {
                        x = x + speed;
                    }
                    if (!up && !down && !right && left && ((int) x > -1))
                    {
                        x = x - speed;
                    }

                    if (up && !down && right && !left && ((int) y > -1) && ((int) x < Game.width - p_width * 0.25))
                    {
                        y = y - speed;
                        x = x + speed;
                    }
                    if (!up && down && right && !left && (y < Game.height - p_height * 0.75) && ((int) x < Game.width - p_width * 0.25))
                    {
                        y = y + speed;
                        x = x + speed;
                    }
                    if (up && !down && !right && left && ((int) y > -1) && ((int) x > -1))
                    {
                        y = y - speed;
                        x = x - speed;
                    }
                    if (!up && down && !right && left && (y < Game.height - p_height * 0.75) && ((int) x > -1))
                    {
                        y = y + speed;
                        x = x - speed;
                    }

                    NPC.updateAllNPC();
                    Bullet.updateAllBullet();

                    if (temp_tick_counter < 0)
                    {
                        temp_tick_counter = tick_counter;
                        bufferStrategy = canvas.getBufferStrategy();
                        g = bufferStrategy.getDrawGraphics();
                        g.clearRect(0, 0, width, height);
                        g.setColor(color);
                        g.fillOval((int) x - p_width / 2, (int) y - p_height / 2, p_width, p_height);
                        NPC.draw_all(g);
                        Bullet.draw_all(g);
                        bufferStrategy.show();
                        g.dispose();

                        shoots_firedl.setText("Shoots fired: " + shoots_fired);
                        shoots_hitl.setText("Shoots hit: " + shoots_hit);
                        enemy_destroyedl.setText("Enemies destroyed: " + enemy_destroyed);
                        total_enemiesl.setText("Total enemies: " + total_enemies);

                    }

                }

            }
        }
    }
}
