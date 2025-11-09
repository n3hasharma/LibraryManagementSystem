

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class final_rec extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Role selection components
    private JRadioButton adminRadio, userRadio;
    private JButton roleNextBtn;

    // Login components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel loginMsg;
    private JButton loginBtn, loginResetBtn;
    private String selectedRole;

    // Admin dashboard components
    private JTextField[] adminFields;
    private JLabel[] adminLabels;
    private JButton addBookBtn, resetBookBtn;
    private JLabel adminMsg;
    private String[] fieldNames = {"Book ID", "Title", "Author", "Publisher", "Quantity"};

    // User dashboard components
    private JTextArea userInfoArea;

    private Connection con;

    public final_rec() {
        setTitle("📚 Library Management System");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        // ===== Role Selection Panel =====
        JPanel rolePanel = new JPanel(new GridBagLayout());
        rolePanel.setBackground(new Color(240, 250, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JLabel roleLabel = new JLabel("Select your role:");
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        roleLabel.setForeground(new Color(0,51,153));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        rolePanel.add(roleLabel, gbc);

        adminRadio = new JRadioButton("Admin");
        userRadio = new JRadioButton("User/Student");
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(adminRadio);
        roleGroup.add(userRadio);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        rolePanel.add(adminRadio, gbc);
        gbc.gridx = 1;
        rolePanel.add(userRadio, gbc);

        roleNextBtn = new JButton("Next");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        rolePanel.add(roleNextBtn, gbc);

        mainPanel.add(rolePanel, "roleSelect");

        // ===== Login Panel =====
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(240,250,255));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JLabel loginTitle = new JLabel("Login");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        loginTitle.setForeground(new Color(0,51,153));
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        loginPanel.add(loginTitle, gbc);

        gbc.gridwidth=1; gbc.anchor=GridBagConstraints.EAST;
        gbc.gridy++;
        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx=1; gbc.anchor=GridBagConstraints.WEST;
        usernameField = new JTextField(15);
        loginPanel.add(usernameField, gbc);

        gbc.gridx=0; gbc.gridy++; gbc.anchor=GridBagConstraints.EAST;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx=1; gbc.anchor=GridBagConstraints.WEST;
        passwordField = new JPasswordField(15);
        loginPanel.add(passwordField, gbc);

        gbc.gridx=0; gbc.gridy++; gbc.gridwidth=2; gbc.anchor=GridBagConstraints.CENTER;
        loginBtn = new JButton("Login");
        loginPanel.add(loginBtn, gbc);

        gbc.gridy++;
        loginResetBtn = new JButton("Reset");
        loginPanel.add(loginResetBtn, gbc);

        gbc.gridy++;
        loginMsg = new JLabel("");
        loginPanel.add(loginMsg, gbc);

        mainPanel.add(loginPanel, "login");

        // ===== Admin Dashboard Panel =====
        JPanel adminPanel = new JPanel(new GridBagLayout());
        adminPanel.setBackground(new Color(240,250,255));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JLabel adminTitle = new JLabel("Admin Dashboard");
        adminTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        adminTitle.setForeground(new Color(0,51,153));
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        adminPanel.add(adminTitle, gbc);

        adminLabels = new JLabel[fieldNames.length];
        adminFields = new JTextField[fieldNames.length];
        gbc.gridwidth=1;

        for(int i=0;i<fieldNames.length;i++){
            adminLabels[i] = new JLabel(fieldNames[i]+":");
            gbc.gridx=0; gbc.gridy=i+1; gbc.anchor=GridBagConstraints.EAST;
            adminPanel.add(adminLabels[i], gbc);

            adminFields[i] = new JTextField(15);
            gbc.gridx=1; gbc.anchor=GridBagConstraints.WEST;
            adminPanel.add(adminFields[i], gbc);
        }

        gbc.gridx=0; gbc.gridy=fieldNames.length+1; gbc.gridwidth=2; gbc.anchor=GridBagConstraints.CENTER;
        addBookBtn = new JButton("Add Book");
        resetBookBtn = new JButton("Reset");
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(240,250,255));
        btnPanel.add(addBookBtn);
        btnPanel.add(resetBookBtn);
        adminPanel.add(btnPanel, gbc);

        gbc.gridy++;
        adminMsg = new JLabel("");
        adminPanel.add(adminMsg, gbc);

        mainPanel.add(adminPanel,"admin");

        // ===== User Dashboard Panel =====
        JPanel userPanel = new JPanel(new GridBagLayout());
        userPanel.setBackground(new Color(240,250,255));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JLabel userTitle = new JLabel("User Dashboard");
        userTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        userTitle.setForeground(new Color(0,51,153));
        gbc.gridx=0; gbc.gridy=0;
        userPanel.add(userTitle, gbc);

        gbc.gridy++;
        userInfoArea = new JTextArea(15,40);
        userInfoArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(userInfoArea);
        userPanel.add(scroll, gbc);

        mainPanel.add(userPanel,"user");

        // ===== DATABASE =====
        connectDatabase();

        // ===== EVENTS =====
        roleNextBtn.addActionListener(e -> {
            if(adminRadio.isSelected()) selectedRole="admin";
            else if(userRadio.isSelected()) selectedRole="user";
            else { JOptionPane.showMessageDialog(this,"Select a role!"); return; }
            cardLayout.show(mainPanel,"login");
        });

        loginBtn.addActionListener(e -> performLogin());

        loginResetBtn.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            loginMsg.setText("");
        });

        addBookBtn.addActionListener(e -> addBook());
        resetBookBtn.addActionListener(e -> {
            for(JTextField f: adminFields) f.setText("");
            adminMsg.setText("");
        });

        cardLayout.show(mainPanel,"roleSelect");
        setVisible(true);
    }

    private void connectDatabase(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/techhh","root","");
            System.out.println("DB Connected");
        }catch(Exception e){ e.printStackTrace(); }
    }

    private void performLogin(){
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();

        if(selectedRole.equals("admin")){
            // hardcoded admin credentials
            if(user.equals("admin") && pass.equals("admin123")){
                loginMsg.setText("✅ Admin login successful");
                cardLayout.show(mainPanel,"admin");
            } else {
                loginMsg.setText("❌ Invalid admin credentials");
            }
        } else {
            // For user: check issued_books table and show info
            try{
                PreparedStatement pst = con.prepareStatement(
                        "SELECT b.title, i.issue_date, i.return_date, i.fine FROM issued_books i JOIN books b ON i.book_id=b.book_id WHERE i.user_id=?");
                pst.setString(1,user);
                ResultSet rs = pst.executeQuery();
                StringBuilder sb = new StringBuilder();
                sb.append("Book Title\tIssued On\tReturn On\tFine\n");
                while(rs.next()){
                    sb.append(rs.getString("title")).append("\t")
                      .append(rs.getDate("issue_date")).append("\t")
                      .append(rs.getDate("return_date")).append("\t")
                      .append(rs.getDouble("fine")).append("\n");
                }
                userInfoArea.setText(sb.toString());
                cardLayout.show(mainPanel,"user");
            }catch(Exception e){ e.printStackTrace(); }
        }
    }

    private void addBook(){
        try{
            int id = Integer.parseInt(adminFields[0].getText().trim());
            String title = adminFields[1].getText().trim();
            String author = adminFields[2].getText().trim();
            String publisher = adminFields[3].getText().trim();
            int qty = Integer.parseInt(adminFields[4].getText().trim());

            PreparedStatement pst = con.prepareStatement("INSERT INTO books (book_id,title,author,publisher,quantity) VALUES (?,?,?,?,?)");
            pst.setInt(1,id);
            pst.setString(2,title);
            pst.setString(3,author);
            pst.setString(4,publisher);
            pst.setInt(5,qty);

            int res = pst.executeUpdate();
            if(res>0){
                adminMsg.setForeground(Color.GREEN);
                adminMsg.setText("✅ Book Added Successfully");
                for(JTextField f: adminFields) f.setText("");
            } else {
                adminMsg.setForeground(Color.RED);
                adminMsg.setText("❌ Failed to add book");
            }
        }catch(Exception e){
            adminMsg.setForeground(Color.RED);
            adminMsg.setText("⚠️ Check fields: "+e.getMessage());
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(final_rec::new);
    }
}
