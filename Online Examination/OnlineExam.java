import javax.swing.*;  
import java.awt.*;  
import java.awt.event.*;  
import java.lang.Exception; 
import java.util.Timer;
import java.util.TimerTask; 

// Class for the login window
class login extends JFrame implements ActionListener  
{  
    JButton b1;  
    JPanel newPanel;  
    JLabel emptyLabel, userLabel, passLabel;  
    final JTextField textField1, textField2;  

    login()  
    {     
        // Set background image for login window
        setContentPane(new JLabel(new ImageIcon("loginbg.png")));
        setLayout(new FlowLayout());
        
        // Components for login window
        emptyLabel = new JLabel("LOGIN FOR THE ONLINE EXAM");
        userLabel = new JLabel("Username :");
        textField1 = new JTextField(15);
        passLabel = new JLabel("Password :");
        textField2 = new JPasswordField(15);
        b1 = new JButton("   LOGIN   ");

        // Create Font objects for labels
        Font labelFont1 = new Font("Monospaced", Font.BOLD, 25);
        Font labelFont2 = new Font("Monospaced", Font.BOLD, 18);

        // Set font for labels
        emptyLabel.setFont(labelFont1);
        userLabel.setFont(labelFont2);
        passLabel.setFont(labelFont2);
        textField1.setFont(labelFont2);
        textField2.setFont(labelFont2);
        b1.setFont(labelFont2);

        // Create a panel with GridBagLayout
        newPanel = new JPanel(new GridBagLayout());
        newPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 300, 15, 5);
        newPanel.add(emptyLabel, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(30, 50, 5, 5);
        newPanel.add(userLabel, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(-30, 400, 0, 5);
        newPanel.add(textField1, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(10, 50, 5, 5);
        newPanel.add(passLabel, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(-30, 400, 0, 5);
        newPanel.add(textField2, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(40, 300, 5, 5);
        newPanel.add(b1, gbc);

        // Add the panel to the center of the frame
        add(newPanel, BorderLayout.CENTER);

        // Register ActionListener for the button
        b1.addActionListener(this);

        // Set properties for the frame
        setTitle("Exam Login ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }   

    public void actionPerformed(ActionEvent ae)     
    {  
        // Get entered username and password
        String userValue = textField1.getText();        
        String passValue = textField2.getText(); 
              
        // Check if the username and password are correct
        if (authenticate(userValue, passValue)) {
            // Open the exam window
            new OnlineTestBegin(userValue); 
        } else {
            // Show an error message, clear text fields on failure
            JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.");
            textField1.setText("");
            textField2.setText("");
        }
    }     

    // Simple authentication method
    private boolean authenticate(String username, String password) {
        return username.equals("user") && password.equals("pass");
    }
}  

// Class for the online exam window
class OnlineTestBegin extends JFrame implements ActionListener  
{  
    JLabel l;  
    JLabel l1;  
    JRadioButton jb[]=new JRadioButton[6];  
    JButton b1, b2, log;  
    ButtonGroup bg;  
    int count=0, current=0, x=1, y=1, now=0;  
    int m[]=new int[10];  
    Timer timer = new Timer();  

    // Create Font objects for labels
    Font labelFont1 = new Font("Monospaced", Font.BOLD, 11);
    Font labelFont2 = new Font("Monospaced", Font.BOLD, 14);

    OnlineTestBegin(String s)  
    {    
        super(s); 

        // Set background image for exam window
        setContentPane(new JLabel(new ImageIcon("exambg.png")));
        setLayout(new FlowLayout()); 

        l=new JLabel();
        l1 = new JLabel(); 
        l.setFont(labelFont2);
        l1.setFont(labelFont1);
        add(l);
        add(l1);  
        bg=new ButtonGroup();  
        for(int i=0;i<5;i++)  
        {  
            jb[i]=new JRadioButton();     
            add(jb[i]);  
            bg.add(jb[i]);  
        }  
        b1=new JButton("Save and Next");  
        b2=new JButton("Save for later");
        b1.setFont(labelFont1);
        b2.setFont(labelFont1);  
        b1.addActionListener(this);  
        b2.addActionListener(this);  
        add(b1); 
        add(b2);  
        set();  
        l.setBounds(30,40,450,20);
        l1.setBounds(20,20,450,20);
        jb[0].setBounds(50,80,500,20);  
        jb[1].setBounds(50,110,500,20);  
        jb[2].setBounds(50,140,500,20);  
        jb[3].setBounds(50,170,500,20);
        jb[0].setOpaque(false);
        jb[1].setOpaque(false);
        jb[2].setOpaque(false);
        jb[3].setOpaque(false);  
        b1.setBounds(95,240,140,30);  
        b2.setBounds(270,240,150,30);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        setLayout(null);  
        setLocation(250,100);  
        setVisible(true);  
        setSize(848,404);     
        setResizable(false);

        // Timer for the exam duration
        timer.scheduleAtFixedRate(new TimerTask() {
            int i = 900;
            public void run() {  
                int minutes = i / 60;
                int seconds = i % 60;
                String timeLeft = String.format("Time left: %02d:%02d", minutes, seconds);
                l1.setText(timeLeft);
                l1.setFont(labelFont1);
        
                i--;   
                if (i < 0) {
                    timer.cancel();
                    l1.setText("Time Out");  
                    l1.setFont(labelFont1);                   
                } 
            }
        }, 0, 1000);        
    }  
    
    public void actionPerformed(ActionEvent e)  
    {          
        if(e.getSource()==b1)  
        {  
            if(check())  
                count=count+1;  
            current++;  
            set();    
            if(current==9)  
            {  
                b1.setEnabled(false);  
                b2.setText("Result");
                b2.setFont(labelFont1);  
            }  
        }  
        if(e.getActionCommand().equals("Save for later"))  
        {  
            JButton bk=new JButton("Review"+x);  
            bk.setBounds(560,20+30*x,100,30);  
            bk.setFont(labelFont1);
            add(bk);  
            bk.addActionListener(this);  
            m[x]=current;  
            x++;  
            current++;  
            set();    
            if(current==9)  
            {
                b2.setText("Result");  
                b2.setFont(labelFont1);
            }
            setVisible(false);  
            setVisible(true);  
        }  
        for(int i=0,y=1;i<x;i++,y++)  
        {  
            if(e.getActionCommand().equals("Review"+y))  
            {  
                if(check())  
                    count=count+1;
                now=current;  
                current=m[y];  
                set();  
                ((JButton)e.getSource()).setEnabled(false);  
                current=now;  
            }  
        }      
        if(e.getActionCommand().equals("Result"))  
        {  
            if(check())  
                count=count+1;  
            current++;  
            JOptionPane.showMessageDialog(this,"Your Score : "+count+" out of 10");  
            System.exit(0);  
        }  
    }  
    
    void set() 
    {
        l.setFont(labelFont2);
        jb[0].setFont(labelFont2);
        jb[1].setFont(labelFont2);
        jb[2].setFont(labelFont2);
        jb[3].setFont(labelFont2);

        jb[4].setSelected(true);
        if (current == 0) {
            l.setText("Q1: What does JVM stand for?");
            jb[0].setText("Java Virtual Machine");
            jb[1].setText("Java Variable Machine");
            jb[2].setText("Java Virtual Method");
            jb[3].setText("Java Variable Method");
        }
        if (current == 1) {
            l.setText("Q2: Which keyword is used to define a constant in Java?");
            jb[0].setText("const");
            jb[1].setText("final");
            jb[2].setText("static");
            jb[3].setText("constant");
        }
        if (current == 2) {
            l.setText("Q3: What is the size of an int variable in Java?");
            jb[0].setText("16 bits");
            jb[1].setText("32 bits");
            jb[2].setText("64 bits");
            jb[3].setText("Depends on the platform");
        }
        if (current == 3) {
            l.setText("Q4: Which method is used to compare two strings in Java?");
            jb[0].setText("compare()");
            jb[1].setText("equals()");
            jb[2].setText("compareTo()");
            jb[3].setText("isEqual()");
        }
        if (current == 4) {
            l.setText("Q5: What is the return type of the 'main' method in Java?");
            jb[0].setText("void");
            jb[1].setText("int");
            jb[2].setText("String");
            jb[3].setText("boolean");
        }
        if (current == 5) {
            l.setText("Q6: Which of the following is not a Java keyword?");
            jb[0].setText("class");
            jb[1].setText("interface");
            jb[2].setText("implements");
            jb[3].setText("extend");
        }
        if (current == 6) {
            l.setText("Q7: What is the purpose of the 'super' keyword in Java?");
            jb[0].setText("To call the superclass constructor");
            jb[1].setText("To access the superclass variables");
            jb[2].setText("To define a superclass");
            jb[3].setText("To create an instance of the superclass");
        }
        if (current == 7) {
            l.setText("Q8: What is the output of 'System.out.println(5 / 2)'?");
            jb[0].setText("2.5");
            jb[1].setText("2.0");
            jb[2].setText("2");
            jb[3].setText("2.2");
        }
        if (current == 8) {
            l.setText("Q9: Which collection class is synchronized in Java?");
            jb[0].setText("ArrayList");
            jb[1].setText("LinkedList");
            jb[2].setText("HashSet");
            jb[3].setText("Vector");
        }
        if (current == 9) {
            l.setText("Q10: What is the use of the 'break' statement in Java?");
            jb[0].setText("To terminate the program");
            jb[1].setText("To exit a loop or switch statement");
            jb[2].setText("To skip the current iteration");
            jb[3].setText("To jump to a specific line in the code");
        }
        l.setBounds(30, 40, 550, 20);
        for (int i = 0, j = 0; i <= 90; i += 30, j++)
            jb[j].setBounds(50, 80 + i, 400, 20);
    }

    boolean check()  
    {  
        if(current==0)  
            return(jb[0].isSelected());  
        if(current==1)  
            return(jb[1].isSelected());  
        if(current==2)  
            return(jb[1].isSelected());  
        if(current==3)  
            return(jb[2].isSelected());  
        if(current==4)  
            return(jb[0].isSelected());  
        if(current==5)  
            return(jb[3].isSelected());  
        if(current==6)  
            return(jb[0].isSelected());  
        if(current==7)  
            return(jb[2].isSelected());  
        if(current==8)  
            return(jb[3].isSelected());  
        if(current==9)  
            return(jb[1].isSelected());  
        return false;  
    }    
} 

// Class to execute the application
class OnlineExam  
{  
    public static void main(String args[])  
    {  
        try  
        {  
            login form = new login();  
            form.setSize(848,360);  
            form.setVisible(true);
            form.setResizable(false);
        }  
        catch(Exception e)  
        {     
            JOptionPane.showMessageDialog(null, e.getMessage());  
        }  
    }  
}  
