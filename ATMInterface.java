import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ATMInterface extends JFrame implements ActionListener {

    // ================= BANK ACCOUNT CLASS =================
    static class BankAccount {
        private double balance;

        public BankAccount(double balance) {
            this.balance = balance;
        }

        public boolean deposit(double amount) {
            if (amount > 0) {
                balance += amount;
                return true;
            }
            return false;
        }

        public boolean withdraw(double amount) {
            if (amount > 0 && amount <= balance) {
                balance -= amount;
                return true;
            }
            return false;
        }

        public double getBalance() {
            return balance;
        }
    }

    // ================= ATM CLASS =================
    static class ATM {
        private BankAccount account;

        public ATM(BankAccount account) {
            this.account = account;
        }

        public boolean deposit(double amount) {
            return account.deposit(amount);
        }

        public boolean withdraw(double amount) {
            return account.withdraw(amount);
        }

        public double checkBalance() {
            return account.getBalance();
        }
    }

    // ================= GUI COMPONENTS =================
    private JTextField amountField;
    private JLabel balanceLabel;
    private JTextArea transactionArea;

    private JButton depositBtn;
    private JButton withdrawBtn;
    private JButton balanceBtn;
    private JButton clearBtn;
    private JButton exitBtn;

    private ATM atm;

    // ================= CONSTRUCTOR =================
    public ATMInterface() {

        BankAccount account = new BankAccount(10000);
        atm = new ATM(account);

        setTitle("ATM Management System");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        Color darkBlue = new Color(15, 23, 42);
        Color blue = new Color(37, 99, 235);
        Color white = Color.WHITE;

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(darkBlue);

        JLabel title = new JLabel("ATM MANAGEMENT SYSTEM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(white);
        title.setBounds(170, 20, 450, 40);

        JLabel amountLabel = new JLabel("Enter Amount:");
        amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        amountLabel.setForeground(white);
        amountLabel.setBounds(70, 100, 150, 30);

        amountField = new JTextField();
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        amountField.setBounds(220, 100, 250, 35);

        depositBtn = new JButton("Deposit");
        withdrawBtn = new JButton("Withdraw");
        balanceBtn = new JButton("Check Balance");
        clearBtn = new JButton("Clear");
        exitBtn = new JButton("Exit");

        depositBtn.setBounds(70, 170, 180, 45);
        withdrawBtn.setBounds(300, 170, 180, 45);
        balanceBtn.setBounds(530, 170, 150, 45);

        clearBtn.setBounds(180, 240, 180, 45);
        exitBtn.setBounds(390, 240, 180, 45);

        JButton[] buttons = {
                depositBtn,
                withdrawBtn,
                balanceBtn,
                clearBtn,
                exitBtn
        };

        for (JButton btn : buttons) {
            btn.setBackground(blue);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btn.addActionListener(this);
        }

        balanceLabel = new JLabel("Current Balance : ₹10,000.00");
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        balanceLabel.setForeground(Color.GREEN);
        balanceLabel.setBounds(200, 320, 350, 30);

        JLabel historyLabel = new JLabel("Transaction History");
        historyLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        historyLabel.setForeground(Color.WHITE);
        historyLabel.setBounds(270, 370, 200, 30);

        transactionArea = new JTextArea();
        transactionArea.setEditable(false);
        transactionArea.setFont(new Font("Consolas", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(transactionArea);
        scrollPane.setBounds(120, 410, 500, 80);

        panel.add(title);
        panel.add(amountLabel);
        panel.add(amountField);

        panel.add(depositBtn);
        panel.add(withdrawBtn);
        panel.add(balanceBtn);
        panel.add(clearBtn);
        panel.add(exitBtn);

        panel.add(balanceLabel);
        panel.add(historyLabel);
        panel.add(scrollPane);

        add(panel);

        setVisible(true);
    }

    // ================= UPDATE BALANCE =================
    private void updateBalance() {
        balanceLabel.setText(
                "Current Balance : ₹" +
                String.format("%.2f", atm.checkBalance()));
    }

    // ================= BUTTON EVENTS =================
    @Override
    public void actionPerformed(ActionEvent e) {

        try {

            if (e.getSource() == depositBtn) {

                double amount =
                        Double.parseDouble(amountField.getText());

                if (atm.deposit(amount)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "₹" + amount +
                                    " Deposited Successfully");

                    transactionArea.append(
                            "Deposited : ₹" +
                                    amount + "\n");

                    updateBalance();
                }
            }

            if (e.getSource() == withdrawBtn) {

                double amount =
                        Double.parseDouble(amountField.getText());

                if (atm.withdraw(amount)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "₹" + amount +
                                    " Withdrawn Successfully");

                    transactionArea.append(
                            "Withdrawn : ₹" +
                                    amount + "\n");

                    updateBalance();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Insufficient Balance!",
                            "Transaction Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            if (e.getSource() == balanceBtn) {

                JOptionPane.showMessageDialog(
                        this,
                        "Available Balance : ₹" +
                                String.format("%.2f",
                                        atm.checkBalance()));
            }

            if (e.getSource() == clearBtn) {

                amountField.setText("");
            }

            if (e.getSource() == exitBtn) {

                int option =
                        JOptionPane.showConfirmDialog(
                                this,
                                "Do you want to exit?",
                                "Exit",
                                JOptionPane.YES_NO_OPTION);

                if (option ==
                        JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please Enter Valid Numeric Amount",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================= MAIN METHOD =================
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new ATMInterface();
        });
    }
}