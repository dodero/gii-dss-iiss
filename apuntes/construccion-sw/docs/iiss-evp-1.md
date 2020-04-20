# EVENTOS ASÍNCRONOS

## Programación asíncrona

### <a id="listeners">Listeners</a>

#### Listener con un solo control

```java
public class DemoKeyEvents {
   public static void main(String[] args) {
      DemoKeyEventsFrame frame = new DemoKeyEventsFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setTitle("DemoKeyEvents");
      frame.pack();
      frame.setVisible(true);
   }
}

class DemoKeyEventsFrame extends JFrame implements KeyListener
{
   private JTextField enterField;

   public DemoKeyEventsFrame() {
      enterField = new JTextField(10);
      enterField.addKeyListener(this);

      JPanel panel = new JPanel();
      panel.add(new JLabel("Enter some text:"));
      panel.add(enterField);
      panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      this.setContentPane(panel);
   }

   public void keyPressed(KeyEvent ke) {
      System.out.println(ke.paramString());

      final KeyStroke BEL = KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK);
      KeyStroke k = KeyStroke.getKeyStroke(ke.getKeyCode(), ke.getModifiers());
      if (k == BEL)
      {
         System.out.println("Ctrl-A pressed!");
         Toolkit.getDefaultToolkit().beep();
      }
   }

   public void keyReleased(KeyEvent ke) {
      System.out.println(ke.paramString());
   }

   public void keyTyped(KeyEvent ke) {
      System.out.println(ke.paramString());
   }
}
```

#### Listener con varios controles

```java
public class DemoKeyEvents2 {
   public static void main(String[] args) {
      DemoKeyEvents2Frame frame = new DemoKeyEvents2Frame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setTitle("DemoKeyEvents2");
      frame.pack();
      frame.setVisible(true);
   }
}

class DemoKeyEvents2Frame extends JFrame implements KeyListener {
   private JTextField pro;
   private JTextField con;

   // constructor
   public DemoKeyEvents2Frame() {

      // construct and configure components
      pro = new JTextField(10);
      con = new JTextField(10);

      // add listeners
      pro.addKeyListener(this);
      con.addKeyListener(this);

      // arrange components
      // add components to panels

      JPanel banner = new JPanel();
      banner.add(new JLabel("WHAT'S YOUR OPINION ON ANCHOIVES?"));

      JPanel proPanel = new JPanel();
      proPanel.add(new JLabel("Pro:"));
      proPanel.add(pro);

      JPanel conPanel = new JPanel();
      conPanel.add(new JLabel("Con:"));
      conPanel.add(con);

      // put panels in a content pane panel
      JPanel contentPane = new JPanel();
      contentPane.setLayout(new GridLayout(3, 1));
      contentPane.add(banner);
      contentPane.add(proPanel);
      contentPane.add(conPanel);

      // make panel this JFrame's content pane
      this.setContentPane(contentPane);
   }

   public void keyPressed(KeyEvent ke) {}   // do nothing
   public void keyReleased(KeyEvent ke) {}  // do nothing
   public void keyTyped(KeyEvent ke) {
      Object source = ke.getSource();

      // check if event occurred on pro component
      if (source == pro)
         System.out.println("Pro : " + ke.paramString());

      // check if event occurred on con component
      else if (source == con)
         System.out.println("Con : " + ke.paramString());
   }
}
```

#### Listener con clases anónimas

```java
public class DemoKeyEvents3 {
   public static void main(String[] args) {
      DemoKeyEvents3Frame frame = new DemoKeyEvents3Frame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setTitle("DemoKeyEvents3");
      frame.pack();
      frame.setVisible(true);
   }
}

class DemoKeyEvents3Frame extends JFrame {
   private JTextField enterField;

   // constructor
   public DemoKeyEvents3Frame() {
      enterField = new JTextField(10);

      enterField.addKeyListener(new KeyListener() {
          public void keyPressed(KeyEvent ke) {
            System.out.println(ke.paramString());

            final KeyStroke BEL = KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK);
            KeyStroke k = KeyStroke.getKeyStroke(ke.getKeyCode(), ke.getModifiers());
            if (k == BEL) {
               System.out.println("Ctrl-A pressed!");
               Toolkit.getDefaultToolkit().beep();
            }
          }

          public void keyReleased(KeyEvent ke) {
            System.out.println(ke.paramString());
          }

          public void keyTyped(KeyEvent ke) {
            System.out.println(ke.paramString());
          }
      });

      JPanel panel = new JPanel();
      panel.add(new JLabel("Enter some text:"));
      panel.add(enterField);
      panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      this.setContentPane(panel);
   }

}
```

#### Listener con adaptador

- No hay que redefinir todos los métodos de `KeyListener`
- Los métodos de `KeyAdapter` están definidos pero vacíos

```java
class DemoKeyEvents4Frame extends JFrame {
   private JTextField enterField;

   // constructor
   public DemoKeyEvents4Frame()
   {
      enterField = new JTextField(10);

      enterField.addKeyListener(new KeyAdapter() {
          @Override
          public void keyPressed(KeyEvent ke) {
            System.out.println(ke.paramString());

            final KeyStroke BEL = KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK);
            KeyStroke k = KeyStroke.getKeyStroke(ke.getKeyCode(), ke.getModifiers());
            if (k == BEL) {
               System.out.println("Ctrl-A pressed!");
               Toolkit.getDefaultToolkit().beep();
            }
          }

          @Override
          public void keyReleased(KeyEvent ke) {
            System.out.println(ke.paramString());
          }

          @Override
          public void keyTyped(KeyEvent ke) {
            System.out.println(ke.paramString());
          }
      });

      JPanel panel = new JPanel();
      panel.add(new JLabel("Enter some text:"));
      panel.add(enterField);
      panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      this.setContentPane(panel);
   }

}
```

#### Listener con lambdas y dispatch table

```java
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class Test {

    @FunctionalInterface
    interface KeyAction {
        public void doAction(); //un sólo método abstracto
    }

    public static void main( String[] args) {

        HashMap<Integer, KeyAction> keyDispatcher = new HashMap<Integer, KeyAction>();

        //Crear instancias de FunctionalInterface mediante lambdas
        keyDispatcher.put(KeyEvent.VK_W, () -> moveUp());
        keyDispatcher.put(KeyEvent.VK_S, () -> moveDown());
        keyDispatcher.put(KeyEvent.VK_A, () -> moveLeft());
        keyDispatcher.put(KeyEvent.VK_D, () -> moveRight());

        // Using a JTextField out of simplicity
        JTextField field = new JTextField();
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent arg0) {
                try{
                    keyDispatcher.get(arg0.getKeyCode()).doAction();
                } catch (NullPointerException e) {
                    System.out.println("That button doesn't do anything yet...");
                }
            }
        });

        JFrame frame = new JFrame("Listener Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(field, BorderLayout.NORTH);
        frame.pack();
        frame.setVisible(true);
    }

    private static void moveUp() {
        System.out.println("Moving up");
    }
    private static void moveDown() {
        System.out.println("Moving down");
    }
    private static void moveLeft() {
        System.out.println("Moving left");
    }
    private static void moveRight() {
        System.out.println("Moving right");
    }
}
```

### Ejemplo de listener con y sin lambdas

```java
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This simple Swing program demonstrates how to use Lambda expressions in
 * action listener.
 *
 * @author www.codejava.net
 */
public class ListenerLambdaExample extends JFrame {

    private JButton button = new JButton("Click Me!");

    public ListenerLambdaExample() {
        super("Listener Lambda Example");

        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(button);

        // Java 7 - tradicional, sin lambdas
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.out.println("Handled by anonymous class listener");
            }
        });

        // Java 8 - con lambdas
        button.addActionListener(e -> System.out.println("Handled by Lambda listener"));

        button.addActionListener(e -> {
            System.out.println("Handled Lambda listener");
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 100);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ListenerLambdaExample().setVisible(true);
            }
        });
    }
}
```

### Ejercicio

- Refactorizar `DemoKeyEvents2` con adaptador, interfaces funcionales (lambdas) y tabla de dispatch
