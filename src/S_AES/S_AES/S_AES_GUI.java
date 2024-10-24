package S_AES;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class S_AES_GUI {

    private JFrame mainFrame;
    private JPanel mainPanel, basicPanel, multiplyPanel, breakoutPanel, cbcPanel;

    public S_AES_GUI() {
        createUI();
    }

    private void createUI() {
        mainFrame = new JFrame("S-AES加解密系统");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(500, 350);

        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel("SAES加解密系统", SwingConstants.CENTER);
        label.setFont(new Font("楷体", Font.BOLD, 30));
        label.setForeground(new Color(6, 194, 194, 255));
        label.setBorder(BorderFactory.createEmptyBorder(0,0,50,0));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 10;
        gbc.gridheight = 4;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(label, gbc);

        JButton basicButton = new JButton("基础加解密");
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(basicButton, gbc);

        JButton multiplyButton = new JButton("多重加解密");
        gbc.gridx = 6;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(multiplyButton, gbc);

        JButton cbcButton = new JButton("cbc加解密");
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.gridwidth = 4;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(cbcButton, gbc);

        JButton breakoutButton = new JButton("中间相遇攻击");
        gbc.gridx = 6;
        gbc.gridy = 7;
        gbc.gridwidth = 4;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(breakoutButton, gbc);


        mainFrame.add(mainPanel);

        basicButton.addActionListener(e -> showBasicPanel());
        multiplyButton.addActionListener(e -> showMultiplyPanel());
        breakoutButton.addActionListener(e -> showBreakoutPanel());
        cbcButton.addActionListener(e -> showCbcPanel());

        createBasicPanel();
        createMultiplyPanel();
        createBreakoutPanel();
        createCbcPanel();

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void showBasicPanel() {
        mainFrame.remove(mainPanel);
        mainFrame.add(basicPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void showMultiplyPanel() {
        mainFrame.remove(mainPanel);
        mainFrame.add(multiplyPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void showBreakoutPanel() {
        mainFrame.remove(mainPanel);
        mainFrame.add(breakoutPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void showCbcPanel() {
        mainFrame.remove(mainPanel);
        mainFrame.add(cbcPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void createBasicPanel() {
        basicPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        basicPanel.add(new JLabel("选择加解密模式:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JRadioButton binaryRb = new JRadioButton("二进制加解密");
        basicPanel.add(binaryRb, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.WEST;
        JRadioButton asciiRb = new JRadioButton("ASCII加解密");
        basicPanel.add(asciiRb, gbc);

        ButtonGroup group = new ButtonGroup();
        group.add(binaryRb);
        group.add(asciiRb);
        binaryRb.setSelected(true);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        basicPanel.add(new JLabel("输入明文/密文:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField inputText = new JTextField();
        basicPanel.add(inputText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        basicPanel.add(new JLabel("输入密钥:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField keyText = new JTextField();
        basicPanel.add(keyText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        basicPanel.add(new JLabel("加密/解密后的结果:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        basicPanel.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton encryptButton = new JButton("加密");
        basicPanel.add(encryptButton, gbc);

        gbc.gridx = 1;
        JButton decryptButton = new JButton("解密");
        basicPanel.add(decryptButton, gbc);

        gbc.gridx = 2;
        JButton backButton = new JButton("返回主界面");
        basicPanel.add(backButton, gbc);

        encryptButton.addActionListener(e -> {
            String text = inputText.getText();
            String key = keyText.getText();
            if (binaryRb.isSelected()) {
                resultArea.setText(S_AES.SAES.enCrypt(text, key));
            } else {
                resultArea.setText(SAES.asciiEncrypt(text, key));
            }
        });

        decryptButton.addActionListener(e -> {
            String text = inputText.getText();
            String key = keyText.getText();
            if (binaryRb.isSelected()) {
                resultArea.setText(S_AES.SAES.deCrypt(text, key));
            } else {
                resultArea.setText(SAES.asciiDecrypt(text, key));
            }
        });

        backButton.addActionListener(e -> {
            mainFrame.remove(basicPanel);
            mainFrame.add(mainPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });
    }

    private void createMultiplyPanel() {
        multiplyPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        multiplyPanel.add(new JLabel("输入明文/密文:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField inputText = new JTextField();
        multiplyPanel.add(inputText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        multiplyPanel.add(new JLabel("输入密钥:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField keyText = new JTextField();
        multiplyPanel.add(keyText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        multiplyPanel.add(new JLabel("加密/解密后的结果:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        multiplyPanel.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        JButton encryptButton = new JButton("加密");
        multiplyPanel.add(encryptButton, gbc);

        gbc.gridx = 1;
        JButton decryptButton = new JButton("解密");
        multiplyPanel.add(decryptButton, gbc);

        gbc.gridx = 2;
        JButton backButton = new JButton("返回主界面");
        multiplyPanel.add(backButton, gbc);

        encryptButton.addActionListener(e -> {
            String text = inputText.getText();
            String key = keyText.getText();
            resultArea.setText(S_AES.SAES.multiplyEncrypt(text, key));
        });

        decryptButton.addActionListener(e -> {
            String text = inputText.getText();
            String key = keyText.getText();
            resultArea.setText(S_AES.SAES.multiplyDecrypt(text, key));
        });

        backButton.addActionListener(e -> {
            mainFrame.remove(multiplyPanel);
            mainFrame.add(mainPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });
    }

    private void createBreakoutPanel() {
        breakoutPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(1, 1, 1, 1);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        breakoutPanel.add(new JLabel("输入明文:"), gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField plainText = new JTextField();
        breakoutPanel.add(plainText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        breakoutPanel.add(new JLabel("输入密文:"), gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField cipherText = new JTextField();
        breakoutPanel.add(cipherText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        breakoutPanel.add(new JLabel("结果:"), gbc);

        JTextArea resultArea = new JTextArea(8, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 6;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH;
        breakoutPanel.add(scrollPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton breakoutButton = new JButton("破解");
        breakoutPanel.add(breakoutButton, gbc);

        gbc.gridx = 6;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton backButton = new JButton("返回主界面");
        breakoutPanel.add(backButton, gbc);

        breakoutButton.addActionListener(e -> {
            String plain = plainText.getText();
            String cipher = cipherText.getText();
            List<String> keys = SAES.breakOut(plain, cipher);
            for (String key : keys) {
                resultArea.append(key + "\n");
            }
        });

        backButton.addActionListener(e -> {
            mainFrame.remove(breakoutPanel);
            mainFrame.add(mainPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });
    }

    private void createCbcPanel() {
        cbcPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        cbcPanel.add(new JLabel("输入明文/密文:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField inputText = new JTextField();
        cbcPanel.add(inputText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        cbcPanel.add(new JLabel("输入密钥:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField keyText = new JTextField();
        cbcPanel.add(keyText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        cbcPanel.add(new JLabel("输入初始向量:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField ivText = new JTextField();
        cbcPanel.add(ivText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        cbcPanel.add(new JLabel("加密/解密后的结果:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        cbcPanel.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 10, 20, 10);
        JButton encryptButton = new JButton("加密");
        cbcPanel.add(encryptButton, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(20, 10, 20, 10);
        JButton decryptButton = new JButton("解密");
        cbcPanel.add(decryptButton, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(20, 10, 20, 10);
        JButton backButton = new JButton("返回主界面");
        cbcPanel.add(backButton, gbc);

        encryptButton.addActionListener(e -> {
            String text = inputText.getText();
            String key = keyText.getText();
            String iv = ivText.getText();
            resultArea.setText(SAES.cbcEncrypt(text, key, iv));
        });

        decryptButton.addActionListener(e -> {
            String text = inputText.getText();
            String key = keyText.getText();
            String iv = ivText.getText();
            resultArea.setText(SAES.cbcDecrypt(text, key, iv));
        });

        backButton.addActionListener(e -> {
            mainFrame.remove(cbcPanel);
            mainFrame.add(mainPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new S_AES_GUI();
        });
    }
}
