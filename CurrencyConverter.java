import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.text.DecimalFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyConverter extends JFrame implements ActionListener {

    private JComboBox<String> baseCurrencyBox;
    private JComboBox<String> targetCurrencyBox;

    private JTextField amountField;

    private JButton convertButton;
    private JButton clearButton;
    private JButton exitButton;

    private JTextArea resultArea;

    private JTable historyTable;
    private DefaultTableModel tableModel;

    private DecimalFormat df = new DecimalFormat("#,##0.00");

    public CurrencyConverter() {

        setTitle("Currency Converter - MCA Mini Project");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel(
                "REAL-TIME CURRENCY CONVERTER",
                JLabel.CENTER);

        title.setFont(new Font("Arial", Font.BOLD, 24));

        add(title, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(
                new TitledBorder("Conversion Details"));

        String[] currencies = {
                "USD",
                "INR",
                "EUR",
                "GBP",
                "JPY",
                "AUD",
                "CAD",
                "SGD"
        };

        inputPanel.add(new JLabel("Base Currency"));

        baseCurrencyBox = new JComboBox<>(currencies);
        inputPanel.add(baseCurrencyBox);

        inputPanel.add(new JLabel("Target Currency"));

        targetCurrencyBox = new JComboBox<>(currencies);
        targetCurrencyBox.setSelectedItem("INR");
        inputPanel.add(targetCurrencyBox);

        inputPanel.add(new JLabel("Amount"));

        amountField = new JTextField();
        inputPanel.add(amountField);

        convertButton = new JButton("Convert");
        clearButton = new JButton("Clear");

        convertButton.addActionListener(this);
        clearButton.addActionListener(this);

        inputPanel.add(convertButton);
        inputPanel.add(clearButton);

        add(inputPanel, BorderLayout.WEST);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane resultScroll =
                new JScrollPane(resultArea);

        resultScroll.setBorder(
                new TitledBorder("Conversion Result"));

        add(resultScroll, BorderLayout.CENTER);

        String[] columns = {
                "Date & Time",
                "Base",
                "Target",
                "Amount",
                "Converted"
        };

        tableModel = new DefaultTableModel(columns, 0);

        historyTable = new JTable(tableModel);

        JScrollPane tableScroll =
                new JScrollPane(historyTable);

        tableScroll.setBorder(
                new TitledBorder("Conversion History"));

        tableScroll.setPreferredSize(
                new Dimension(800, 180));

        add(tableScroll, BorderLayout.SOUTH);

        JPanel topButtonPanel = new JPanel();

        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);

        topButtonPanel.add(exitButton);

        add(topButtonPanel, BorderLayout.EAST);

        setVisible(true);
    }

    private double getExchangeRate(
            String baseCurrency,
            String targetCurrency) {

        try {

            String apiUrl =
                    "https://open.er-api.com/v6/latest/"
                            + baseCurrency;

            HttpClient client =
                    HttpClient.newHttpClient();

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(apiUrl))
                            .GET()
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString());

            String json = response.body();

            Pattern pattern =
                    Pattern.compile(
                            "\"" + targetCurrency + "\":([0-9.]+)");

            Matcher matcher =
                    pattern.matcher(json);

            if (matcher.find()) {

                return Double.parseDouble(
                        matcher.group(1));
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to fetch exchange rates.\n"
                            + e.getMessage());
        }

        return -1;
    }

    private String symbol(String currency) {

        switch (currency) {

            case "USD":
                return "$";

            case "INR":
                return "₹";

            case "EUR":
                return "€";

            case "GBP":
                return "£";

            case "JPY":
                return "¥";

            case "AUD":
                return "A$";

            case "CAD":
                return "C$";

            case "SGD":
                return "S$";

            default:
                return "";
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == clearButton) {

            amountField.setText("");
            resultArea.setText("");
            return;
        }

        if (e.getSource() == exitButton) {

            System.exit(0);
        }

        if (e.getSource() == convertButton) {

            try {

                double amount =
                        Double.parseDouble(
                                amountField.getText());

                if (amount <= 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Amount must be greater than zero.");

                    return;
                }

                String base =
                        baseCurrencyBox
                                .getSelectedItem()
                                .toString();

                String target =
                        targetCurrencyBox
                                .getSelectedItem()
                                .toString();

                double rate =
                        getExchangeRate(base, target);

                if (rate == -1) {

                    resultArea.setText(
                            "Exchange rate unavailable.");

                    return;
                }

                double converted =
                        amount * rate;

                String dateTime =
                        LocalDateTime.now()
                                .format(
                                        DateTimeFormatter.ofPattern(
                                                "dd-MM-yyyy HH:mm:ss"));

                resultArea.setText(
                        "====================================\n"
                                + "      CURRENCY CONVERSION\n"
                                + "\n====================================\n"
                                + "Date & Time : "
                                + dateTime
                                + "\n\nBase Currency : "
                                + base
                                + "\nTarget Currency : "
                                + target
                                + "\nExchange Rate : "
                                + rate
                                + "\nAmount Entered : "
                                + amount
                                + "\nConverted Amount : "
                                + symbol(target)
                                + df.format(converted)
                                + "\n====================================");

                tableModel.addRow(
                        new Object[]{
                                dateTime,
                                base,
                                target,
                                amount,
                                symbol(target)
                                        + df.format(converted)
                        });

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid amount.");
            }
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new CurrencyConverter());
    }
}