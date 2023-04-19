import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


class Bird extends GameObject {
    private ProxyImage proxyImage; 
    private Tube[] tube; 

    
    public Bird(int x, int y){
        super(x, y);
        if(proxyImage == null) {
            proxyImage = new ProxyImage("bird.png"); 
        }
        this.image = proxyImage.loadImage().getImage();
        this.width = image.getWidth(null); 
        this.height = image.getHeight(null);
        this.x -= width; 
        this.y -= height; 
        tube = new Tube[1]; 
        tube[0] = new Tube(900, Window.HEIGHT - 60); 
        this.dy = 2; 
    }

    
    public void tick() {
        if(dy < 5) { 
            dy += 2; 
        }
        this.y += dy; 
        tube[0].tick(); 
        checkWindowBorder(); 
    }

    public void jump() {
        if(dy > 0) { 
            dy = 0; 
        }
        dy -= 15; 
    }
    
    
    private void checkWindowBorder() { 
        if(this.x > Window.WIDTH) { 
            this.x = Window.WIDTH; 
        }
        if(this.x < 0) { 
            this.x = 0; 
        }
        if(this.y > Window.HEIGHT - 50) { 
            this.y = Window.HEIGHT - 50; 
        }
        if(this.y < 0) { 
            this.y = 0; 
        }
    }

    
    public void render(Graphics2D g, ImageObserver obs) { 
        g.drawImage(image, x, y, obs); 
        tube[0].render(g, obs);
    }
    
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}


class TubeColumn {

    private int base = Window.HEIGHT - 60;

    private List<Tube> tubes;
    private Random random;
    private int points = 0; 
    private int speed = 5; 
    private int changeSpeed = speed; 

    public TubeColumn() { 
        tubes = new ArrayList<>();
        random = new Random();
        initTubes();
    }


    private void initTubes() {

        int last = base;
        int randWay = random.nextInt(10);

         
        for (int i = 0; i < 20; i++) {

            Tube tempTube = new Tube(900, last); 
            tempTube.setDx(speed); 
            last = tempTube.getY() - tempTube.getHeight(); 
            if (i < randWay || i > randWay + 4) {  
                tubes.add(tempTube); 
            }

        }

    }

    
    public void tick() { 

        for (int i = 0; i < tubes.size(); i++) {  
            tubes.get(i).tick(); 

            if (tubes.get(i).getX() < 0) { 
                tubes.remove(tubes.get(i)); 
            }
        }
        if (tubes.isEmpty()) { 
            this.points += 1; 
            if (changeSpeed == points) {
                this.speed += 1; 
                changeSpeed += 5;
            }
            initTubes(); 
        }

    }

    
    public void render(Graphics2D g, ImageObserver obs) {
        for (int i = 0; i < tubes.size(); i++) { 
            tubes.get(i).render(g, obs);  
        }

    }


    public List<Tube> getTubes() {
        return tubes;
    }

    public void setTubes(List<Tube> tubes) {
        this.tubes = tubes;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}

interface IStrategy {
    
    public void controller(Bird bird, KeyEvent kevent);
    public void controllerReleased(Bird bird, KeyEvent kevent);
}


class Controller implements IStrategy {

    public void controller(Bird bird, KeyEvent kevent) {
    }

    public void controllerReleased(Bird bird, KeyEvent kevent) {
        if(kevent.getKeyCode() == KeyEvent.VK_SPACE) { 
            bird.jump();
        }
    }
    
}

interface IImage {
    public ImageIcon loadImage();
}

class ProxyImage implements IImage {

    private final String src;
    private RealImage realImage;
    
    public ProxyImage(String src) {
        this.src = src;
    }
    
    public ImageIcon loadImage() {
        if(realImage == null) { 
            this.realImage = new RealImage(src); 
        }
        
        return this.realImage.loadImage(); 
    }
    
}

class RealImage implements IImage {

    private final String src;
    private ImageIcon imageIcon;
    
    public RealImage(String src) {
        this.src = src;
    }
    @Override
    public ImageIcon loadImage() {
        if(imageIcon == null) {
            this.imageIcon = new ImageIcon(getClass().getResource(src));
        }
        return imageIcon;
    }
    
}

abstract class GameObject {
    protected int x, y;
    protected int dx, dy;
    protected int width, height;
    protected Image image;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Image getImage() {
        return image;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setImage(Image image) {
        this.image = image;
    }
    
    
    public abstract void tick();
    public abstract void render(Graphics2D g, ImageObserver obs);
}


class Tube extends GameObject {

    private ProxyImage proxyImage;
    public Tube(int x, int y) {
        super(x, y);
        if (proxyImage == null) { 
            proxyImage = new ProxyImage("TubeBody.png"); 

        }
        this.image = proxyImage.loadImage().getImage(); 
        this.width = image.getWidth(null); 
        this.height = image.getHeight(null); 
    }

    @Override
    public void tick() {
        this.x -= dx;
    }

    @Override
    public void render(Graphics2D g, ImageObserver obs) {
        g.drawImage(image, x, y, obs);

    }

    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}

 
class Game extends JPanel implements ActionListener {

    private boolean isRunning = false; 
    private ProxyImage proxyImage; 
    private Image background; 
    private Bird bird; 
    private TubeColumn tubeColumn; 
    private int score; 
    private int highScore; 

    public Game() {

        proxyImage = new ProxyImage("background.jpg"); 
        background = proxyImage.loadImage().getImage(); 
        setFocusable(true); 
        setDoubleBuffered(false);     
        addKeyListener(new GameKeyAdapter());
        Timer timer = new Timer(15, this); 
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Toolkit.getDefaultToolkit().sync(); 
        if (isRunning) { 
            bird.tick(); 
            tubeColumn.tick(); 
            checkColision(); 
            score++; 
        }

        repaint(); 
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(background, 0, 0, null);
        if (isRunning) {
            this.bird.render(g2, this);
            this.tubeColumn.render(g2, this);
            g2.setColor(Color.black);
            g.setFont(new Font("MV Boli", 1, 30));
            g2.drawString("Current score: " + this.tubeColumn.getPoints(), 10, 50);
            
        } else {
            g2.setColor(Color.black);
             g.setFont(new Font("MV Boli", 1, 50));
            g2.drawString("Press Enter to Start Game", Window.WIDTH / 2 - 350, Window.HEIGHT / 2);
            g2.setColor(Color.black);
            g.setFont(new Font("MV Boli", 1, 15));
        }
        g2.setColor(Color.black);
        g.setFont(new Font("MV Boli", 1, 30));
        g2.drawString("High Score: " + highScore, Window.WIDTH - 230, 50);

        g.dispose();
    }

    private void restartGame() {
        if (!isRunning) {
            this.isRunning = true;
            this.bird = new Bird(Window.WIDTH / 2, Window.HEIGHT / 2); 
            this.tubeColumn = new TubeColumn(); 
        }
    }

    private void endGame() {
        this.isRunning = false;
        if (this.tubeColumn.getPoints() > highScore) { 
            this.highScore = this.tubeColumn.getPoints(); 
        }
        this.tubeColumn.setPoints(0); 

    }

    private void checkColision() {
        Rectangle rectBird = this.bird.getBounds(); 
        Rectangle rectTube; 

        for (int i = 0; i < this.tubeColumn.getTubes().size(); i++) { 
            Tube tempTube = this.tubeColumn.getTubes().get(i);
            rectTube = tempTube.getBounds(); 
            if (rectBird.intersects(rectTube)) { 
                endGame(); 
            }
        }
    }

   
    class GameKeyAdapter extends KeyAdapter {

        private final Controller controller;

        public GameKeyAdapter() {
            controller = new Controller();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                restartGame();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (isRunning) {
                controller.controllerReleased(bird, e);
            }
        }
    }
}

class Window {
    public static int WIDTH = 900; 
    public static int HEIGHT = 600;
    public Window(int width, int height, String title, Game game) {
        JFrame frame = new JFrame();
        frame.add(game);
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setMaximumSize(new Dimension(width, height)); 
        frame.setPreferredSize(new Dimension(width, height)); 
        frame.setMinimumSize(new Dimension(width, height));     
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        
    }

   
    public static void main(String[] args) {

        Game game = new Game();
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            Window window = new Window(WIDTH, HEIGHT, "Flappy Bird", game);
        });
    }
}