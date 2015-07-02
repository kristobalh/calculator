package calculator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Irka on 09.06.2015.
 */
public class CalculatorGUI extends JFrame implements ActionListener{

    JButton b0 = new JButton("0");
    JButton b1 = new JButton("1");
    JButton b2 = new JButton("2");
    JButton b3 = new JButton("3");
    JButton b4 = new JButton("4");
    JButton b5 = new JButton("5");
    JButton b6 = new JButton("6");
    JButton b7 = new JButton("7");
    JButton b8 = new JButton("8");
    JButton b9 = new JButton("9");
    JButton b_comma = new JButton(".");
    JButton b_sqr = new JButton("^");
    JButton b_sign = new JButton("+/-");
    JButton b_plus = new JButton("+");
    JButton b_minus = new JButton("-");
    JButton b_mult = new JButton("*");
    JButton b_div = new JButton("/");
    JButton b_backspace = new JButton("Backspace");
    JButton b_CE = new JButton("CE");
    JButton b_C = new JButton("C");
    JButton b_equal = new JButton("=");
    JButton b_open = new JButton("(");
    JButton b_close = new JButton(")");

    JTextField in_out = new JTextField("0");
    JTextField expression = new JTextField();
    CalculatorConsole calcConsole = new CalculatorConsole();
    ArrayList<String> inputLine_ar = new ArrayList<String>();

    CalculatorGUI(){
        //создаем окно с иконкой и названием
        super("Калькулятор");
        setIconImage(new ImageIcon("E:\\IDEA Projects\\ConsoleApp\\qq.png").getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        //создаем меню
        JMenuBar mbar = new JMenuBar();
        setJMenuBar(mbar);//You can optionally add a menu bar to a top-level container. The menu bar is by convention positioned within the top-level container, but outside the content pane.
        JMenu view = new JMenu("Вид");
        view.add(new JMenuItem("Обычный"));
        view.add(new JMenuItem("других не дано :)"));
        mbar.add(view);
        JMenu edit = new JMenu("Правка");
        edit.add(new JMenuItem("Копировать Ctrl+C"));
        edit.add(new JMenuItem("Вставить Ctrl+V"));
        mbar.add(edit);
        JMenu about = new JMenu("О программе...");
        mbar.add(about);

        //создаем главную панель, в которой будут лежать панель с текстом и панель с кнопками
        JPanel main_panel = new JPanel();
        main_panel.setLayout(new BorderLayout());
        getContentPane().add(main_panel);

        //создаем панель с текстом
        JPanel inOut_panel = new JPanel(new BorderLayout());
        main_panel.add(inOut_panel, BorderLayout.NORTH);
        inOut_panel.setBorder(new EmptyBorder(10, 10, 0, 10));
        inOut_panel.add(expression, BorderLayout.NORTH);
        expression.setHorizontalAlignment(JTextField.RIGHT);
        inOut_panel.add(in_out, BorderLayout.CENTER);
        in_out.setHorizontalAlignment(JTextField.RIGHT);

        //создаем панель с кнопками
        JPanel keys_panel = new JPanel();
        main_panel.add(keys_panel, BorderLayout.CENTER); //The getContentPane method returns a Container object, not a JComponent object. This means that if you want to take advantage of the content pane's JComponent features, you need to either typecast the return value or create your own component to be the content pane.
        keys_panel.setBorder(new EmptyBorder(5, 10, 10, 10));
        keys_panel.setLayout(new GridLayout(5,0,5,5));

        keys_panel.add(b_backspace);
        b_backspace.addActionListener(this);
        keys_panel.add(b_CE);
        b_CE.addActionListener(this);
        keys_panel.add(b_C);
        b_C.addActionListener(this);
        keys_panel.add(b_plus);
        b_plus.addActionListener(this);
        keys_panel.add(b_sign);
        b_sign.addActionListener(this);

        keys_panel.add(b7);
        b7.addActionListener(this);
        keys_panel.add(b8);
        b8.addActionListener(this);
        keys_panel.add(b9);
        b9.addActionListener(this);
        keys_panel.add(b_minus);
        b_minus.addActionListener(this);
        keys_panel.add(b_sqr);
        b_sqr.addActionListener(this);

        keys_panel.add(b4);
        b4.addActionListener(this);
        keys_panel.add(b5);
        b5.addActionListener(this);
        keys_panel.add(b6);
        b6.addActionListener(this);
        keys_panel.add(b_mult);
        b_mult.addActionListener(this);
        keys_panel.add(b_open);
        b_open.addActionListener(this);

        keys_panel.add(b1);
        b1.addActionListener(this);
        keys_panel.add(b2);
        b2.addActionListener(this);
        keys_panel.add(b3);
        b3.addActionListener(this);
        keys_panel.add(b_div);
        b_div.addActionListener(this);
        keys_panel.add(b_close);
        b_close.addActionListener(this);

        keys_panel.add(b0);
        b0.addActionListener(this);
        keys_panel.add(b_comma);
        b_comma.addActionListener(this);
        keys_panel.add(b_equal);
        b_equal.addActionListener(this);

        pack(); //Size the frame. using pack is preferable to calling setSize
        setLocationRelativeTo(null);//the following code centers a frame onscreen:
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JButton source = (JButton)actionEvent.getSource(); //возвращается объект, который был нажат
        String operand;
        String last_char = (in_out.getText()).substring((in_out.getText()).length()-1);

        //кнопка Backspace
        if ((source.getText()).equals("Backspace")){
            if (in_out.getText().equals("В выражении не хватает скобок")){
                inputLine_ar.clear();
                expression.setText("");
                in_out.setText("0");
            }
            if (expression.getText().contains("=")){
                return;
            }
            if (calcConsole.map.containsKey(last_char)){
                return;
            }else {
                in_out.setText((in_out.getText()).substring(0, (in_out.getText()).length() - 1));
                if ((in_out.getText()).equals("")) {
                    in_out.setText("0");
                }
            }

        //кнопка СЕ (отменить последнее нажатие кнопки)
        }else if ((source.getText()).equals("CE")){
            if (in_out.getText().equals("В выражении не хватает скобок")){
                inputLine_ar.clear();
                expression.setText("");
                in_out.setText("0");
            }
            if (calcConsole.map.containsKey(last_char)){
                return;
            }else{
                in_out.setText("0");
            }

        //кнопка С (очистить поле ввода)
        }else if ((source.getText()).equals("C")){
            in_out.setText("0");
            expression.setText("");
            inputLine_ar.clear();

        //кнопка +/- (сменить знак)
        }else if ((source.getText()).equals("+/-")) {
            if (in_out.getText().equals("В выражении не хватает скобок")){
                inputLine_ar.clear();
                expression.setText("");
                in_out.setText("0");
            }
            if (in_out.getText().equals("0") || calcConsole.map.containsKey(last_char) || last_char.equals(".")){
                return;
            }else{
                if(in_out.getText().contains("-")){
                    in_out.setText(in_out.getText().replace("-", ""));
                }else {
                    in_out.setText("-" + in_out.getText());
                }
            }

        //кнопка =
        }else if ((source.getText()).equals("=")) {
            if (in_out.getText().equals("В выражении не хватает скобок")){
                inputLine_ar.clear();
                expression.setText("");
                in_out.setText("0");
            }
            if ((calcConsole.map.containsKey(last_char) && !last_char.equals(")")) || last_char.equals(".") || (in_out.getText().equals("0") && inputLine_ar.isEmpty()) || inputLine_ar.size()<2){
                return;
            }else if (inputLine_ar.contains("(") || inputLine_ar.contains(")")) {
                inputLine_ar.add(in_out.getText());
                int openCounter = 0;
                int closeCounter = 0;
                for (int i = 0; i < inputLine_ar.size(); i++) {
                    if (inputLine_ar.get(i).equals("(")) {
                        openCounter++;
                    }
                    if (inputLine_ar.get(i).equals(")")) {
                        closeCounter++;
                    }
                }
                if (openCounter != closeCounter) {
                    in_out.setText("В выражении не хватает скобок");
                    return;
                } else {
                    ArrayList<String> postfix_ar = calcConsole.infixToPostfix(inputLine_ar);
                    Double result = calcConsole.onStackCalculator(postfix_ar);
                    expression.setText(arrayToString(inputLine_ar) + source.getText());
                    in_out.setText(result.toString());
                }
            }else{
                inputLine_ar.add(in_out.getText());
                expression.setText(arrayToString(inputLine_ar) + source.getText());
                ArrayList<String> postfix_ar = calcConsole.infixToPostfix(inputLine_ar);
                Double result = calcConsole.onStackCalculator(postfix_ar);
                in_out.setText(result.toString());
            }

        //кнопки +, -, *, /, ^
        }else if (calcConsole.map.containsKey(source.getText()) && !(source.getText()).equals("(") && !(source.getText()).equals(")")){
            if (in_out.getText().equals("В выражении не хватает скобок")){
                inputLine_ar.clear();
                expression.setText("");
                in_out.setText("0");
            }
            if (expression.getText().contains("=")){
                expression.setText(in_out.getText());
                inputLine_ar.clear();
                inputLine_ar.add(in_out.getText());
                in_out.setText(source.getText());
            }else {
                if (((in_out.getText()).equals("0") && inputLine_ar.isEmpty()) || last_char.equals(".")) {
                    return;
                } else if (calcConsole.map.containsKey(last_char) && !last_char.equals(")") && ! last_char.equals("(")) {
                    in_out.setText(source.getText());
                }else if (last_char.equals("(")){
                    if (inputLine_ar.isEmpty()){
                        return;
                    }
                    if (!inputLine_ar.isEmpty() && calcConsole.map.containsKey(inputLine_ar.get(inputLine_ar.size()-1)) && !inputLine_ar.get(inputLine_ar.size()-1).equals("(") && !inputLine_ar.get(inputLine_ar.size()-1).equals(")")){
                        return;
                    }
                } else {
                    inputLine_ar.add(in_out.getText());
                    in_out.setText(source.getText());
                    expression.setText(arrayToString(inputLine_ar));
                }
            }

        //кнопка (
        }else if ((source.getText()).equals("(")) {
            if (in_out.getText().equals("В выражении не хватает скобок")){
                inputLine_ar.clear();
                expression.setText("");
                in_out.setText("0");
            }
            if ((in_out.getText()).equals("0")){
                in_out.setText("(");
            }else if (last_char.equals(")")){
                return;
            }else if ((calcConsole.map.containsKey(last_char) && !last_char.equals(")"))){
                inputLine_ar.add(in_out.getText());
                in_out.setText(source.getText());
                expression.setText(arrayToString(inputLine_ar));
            }

        //кнопка )
        }else if ((source.getText()).equals(")")) {
            if (in_out.getText().equals("В выражении не хватает скобок")){
                inputLine_ar.clear();
                expression.setText("");
                in_out.setText("0");
            }
            if ((in_out.getText()).equals("0") && inputLine_ar.isEmpty()){
                return;
            }else if ((calcConsole.map.containsKey(last_char) && !last_char.equals(")")) || last_char.equals(".")){
                return;
            }else if (!inputLine_ar.contains("(")) {
                return;
            }else{
                inputLine_ar.add(in_out.getText());
                in_out.setText(source.getText());
                expression.setText(arrayToString(inputLine_ar));
            }

        // кнопка ,
        }else if ((source.getText()).equals(".")){
            if (in_out.getText().equals("В выражении не хватает скобок")){
                inputLine_ar.clear();
                expression.setText("");
                in_out.setText("0");
            }
            if ((in_out.getText()).equals("0")){
                in_out.setText(0 + source.getText());
            }else if (last_char.equals(".") || last_char.equals("(") || (in_out.getText()).contains(".")) {
                return;
            }else {
                in_out.setText(in_out.getText() + source.getText());
            }

        // кнопки 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
        }else{
            if (in_out.getText().equals("В выражении не хватает скобок")){
                inputLine_ar.clear();
                expression.setText("");
                in_out.setText("0");
            }
            if (expression.getText().contains("=")){
                expression.setText("0");
                inputLine_ar.clear();
                in_out.setText(source.getText());
            }else {
                if ((in_out.getText()).equals("0")) {
                    in_out.setText(source.getText());
                } else if (last_char.equals(")")) {
                    return;
                } else {
                    if (calcConsole.map.containsKey(last_char)) {
                        inputLine_ar.add(in_out.getText());
                        expression.setText(arrayToString(inputLine_ar));
                        in_out.setText(source.getText());
                    } else {
                        in_out.setText(in_out.getText() + source.getText());
                    }
                }
            }
        }
    }

    private  String arrayToString(ArrayList arr){
        String result = "";
        Iterator iterator = arr.iterator();
        for (int i = 0; iterator.hasNext(); i++){
            result += iterator.next().toString();
        }
        return result;
    }
}

