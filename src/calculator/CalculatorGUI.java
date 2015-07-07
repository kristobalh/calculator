package calculator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by Irka on 09.06.2015.
 */
public class CalculatorGUI extends JFrame implements ActionListener
{
    enum ButtonCategory
    {
        Operand,
        Operator,
        Brackets,
        Control
    }

    final String backspace = "Backspace";
    final String cTitle = "C";
    final String ceTitle = "CE";
    final String plusTitle = "+";
    final String plusMinusTitle = "+/-";
    final String minusTitle = "-";
    final String mulTitle = "*";
    final String divTitle = "/";
    final String commaTitle = ".";
    final String equalTitle = "=";
    final String openBracketTitle = "(";
    final String closeBracketTitle = ")";
    final String powTitle = "^";
    final String zeroTitle = "0";

    final String errorMsg = "В выражении не хватает скобок";

    HashSet<JButton> operandsSet = new HashSet<JButton>();
    HashSet<JButton> operatorsSet = new HashSet<JButton>();
    HashSet<JButton> bracketsSet = new HashSet<JButton>();
    HashSet<JButton> controlsSet = new HashSet<JButton>();

    JTextField inOut = new JTextField("0");
    JTextField expression = new JTextField();
    CalculatorConsole calcConsole = new CalculatorConsole();
    ArrayList<String> commands = new ArrayList<String>();

    private void RiseError()
    {
        inOut.setText(errorMsg);
    }

    private boolean IsErrorState()
    {
        return inOut.getText().equals(errorMsg);
    }

    private void ResetError(String msg)
    {
        inOut.setText(msg);
    }

    private HashSet<JButton> GetCategory(final ButtonCategory category)
    {
        switch (category)
        {
            case Operand: return operandsSet;
            case Operator: return operatorsSet;
            case Brackets: return bracketsSet;
            case Control: return controlsSet;
            default:
                return null;
        }
    }

    private ButtonCategory GetCategory(JButton button)
    {
        for (ButtonCategory cat : ButtonCategory.values())
        {
            if (GetCategory(cat).contains(button))
                return cat;
        }

        return ButtonCategory.Operand;
    }

    private void CreateButton(JPanel panel, String buttonTittle, ButtonCategory category)
    {
        HashSet<JButton> buttons = GetCategory(category);
        JButton button = new JButton(buttonTittle);

        panel.add(button);
        button.addActionListener(this);
        buttons.add(button);
    }

    private void InitButtons(JPanel panel)
    {
        CreateButton(panel, backspace, ButtonCategory.Control);
        CreateButton(panel, ceTitle, ButtonCategory.Control);
        CreateButton(panel, cTitle, ButtonCategory.Control);
        CreateButton(panel, plusTitle, ButtonCategory.Operator);
        CreateButton(panel, plusMinusTitle, ButtonCategory.Operator);

        CreateButton(panel, "7", ButtonCategory.Operand);
        CreateButton(panel, "8", ButtonCategory.Operand);
        CreateButton(panel, "9", ButtonCategory.Operand);
        CreateButton(panel, minusTitle, ButtonCategory.Operator);
        CreateButton(panel, powTitle, ButtonCategory.Operator);

        CreateButton(panel, "4", ButtonCategory.Operand);
        CreateButton(panel, "5", ButtonCategory.Operand);
        CreateButton(panel, "6", ButtonCategory.Operand);
        CreateButton(panel, mulTitle, ButtonCategory.Operator);
        CreateButton(panel, openBracketTitle, ButtonCategory.Brackets);

        CreateButton(panel, "1", ButtonCategory.Operand);
        CreateButton(panel, "2", ButtonCategory.Operand);
        CreateButton(panel, "3", ButtonCategory.Operand);
        CreateButton(panel, divTitle, ButtonCategory.Operator);
        CreateButton(panel, closeBracketTitle, ButtonCategory.Brackets);

        CreateButton(panel, zeroTitle, ButtonCategory.Operand);
        CreateButton(panel, commaTitle, ButtonCategory.Operand);
        CreateButton(panel, equalTitle, ButtonCategory.Operator);
    }

    private void CreatePanels()
    {
        //создаем главную панель, в которой будут лежать панель с текстом и панель с кнопками
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        getContentPane().add(mainPanel);

        //создаем панель с текстом
        JPanel inOutPanel = new JPanel(new BorderLayout());
        mainPanel.add(inOutPanel, BorderLayout.NORTH);
        inOutPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
        inOutPanel.add(expression, BorderLayout.NORTH);
        expression.setHorizontalAlignment(JTextField.RIGHT);
        inOutPanel.add(inOut, BorderLayout.CENTER);
        inOut.setHorizontalAlignment(JTextField.RIGHT);

        //создаем панель с кнопками
        JPanel keysPanel = new JPanel();
        mainPanel.add(keysPanel, BorderLayout.CENTER); //The getContentPane method returns a Container object, not a JComponent object. This means that if you want to take advantage of the content pane's JComponent features, you need to either typecast the return value or create your own component to be the content pane.
        keysPanel.setBorder(new EmptyBorder(5, 10, 10, 10));
        keysPanel.setLayout(new GridLayout(5, 0, 5, 5));

        InitButtons(keysPanel);
    }

    private void CreateMenu()
    {
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
    }

    CalculatorGUI() {
        //создаем окно с иконкой и названием
        super("Калькулятор");
        setIconImage(new ImageIcon("E:\\IDEA Projects\\ConsoleApp\\qq.png").getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        CreateMenu();
        CreatePanels();

        pack(); //Size the frame. using pack is preferable to calling setSize
        setLocationRelativeTo(null);//the following code centers a frame onscreen:
    }

    private void ClearState(String inoutMsg)
    {
        commands.clear();
        expression.setText("");
        inOut.setText(inoutMsg);
    }

    private void ClearState()
    {
        ClearState("0");
    }

    private void FlushCommand(String msg)
    {
        commands.add(inOut.getText());
        inOut.setText(msg);
        expression.setText(arrayToString(commands));
    }

    private void ProcessControlButton(JButton button, String lastChar)
    {
        String title = button.getText();

        if (title.equals(cTitle) || IsErrorState())
            ClearState();
        else if (!calcConsole.map.containsKey(lastChar))
        {
            if (title.equals(backspace) && !expression.getText().contains(equalTitle))
            {
                inOut.setText((inOut.getText()).substring(0, (inOut.getText()).length() - 1));
                if ((inOut.getText()).equals(""))
                    inOut.setText(zeroTitle);
            }

            if (title.equals(ceTitle))
                inOut.setText(zeroTitle);
        }
    }

    private void ProcessBrackets(JButton button, String lastChar)
    {
        if (IsErrorState()) {
            ClearState();
            return;
        }

        String title = button.getText();

        if (title.equals(openBracketTitle))
        {
            if ((inOut.getText()).equals(zeroTitle))
                inOut.setText(title);
            else if (calcConsole.map.containsKey(lastChar) && !lastChar.equals(openBracketTitle))
                FlushCommand(button.getText());
        }
        else if (title.equals(closeBracketTitle))
        {
            boolean expressionStart = inOut.getText().equals(zeroTitle) && !commands.contains(openBracketTitle);
            boolean lastCharIsDelim = calcConsole.map.containsKey(lastChar);
            boolean lastCharIsCloseBracket = lastChar.equals(closeBracketTitle);
            boolean lastCharIsComma = lastChar.equals(commaTitle);
            if (!(expressionStart || lastCharIsComma || (lastCharIsDelim && !lastCharIsCloseBracket)))
                FlushCommand(title);
        }
    }

    private void ProcessOperator(JButton button, String lastChar) {
        String title = button.getText();

        if (IsErrorState()) {
            ClearState();
            return;
        }

        if (title.equals(commaTitle))
        {
            if (!calcConsole.map.containsKey(lastChar) && !inOut.getText().contains(commaTitle))
                inOut.setText(inOut.getText() + title);
        }
        else
        {
            if (expression.getText().contains(equalTitle))
                ClearState(title);
            else
            {
                if (!lastChar.equals(closeBracketTitle))
                {
                    if (calcConsole.map.containsKey(lastChar))
                        FlushCommand(title);
                    else
                    {
                        String srcText = inOut.getText();
                        if (srcText.equals(zeroTitle))
                            srcText = "";

                        inOut.setText(srcText + title);
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JButton source = (JButton)actionEvent.getSource(); //возвращается объект, который был нажат
        String operand;
        String lastChar = (inOut.getText()).substring((inOut.getText()).length() - 1);

        switch (GetCategory(source))
        {
            case Control:
                ProcessControlButton(source, lastChar);
                return;
            case Brackets:
                ProcessBrackets(source, lastChar);
                return;
            case Operand:
                ProcessOperator(source, lastChar);
                return;
        }

        //кнопка +/- (сменить знак)
        if ((source.getText()).equals("+/-")) {
            if (inOut.getText().equals("В выражении не хватает скобок")){
                commands.clear();
                expression.setText("");
                inOut.setText("0");
            }
            if (inOut.getText().equals("0") || calcConsole.map.containsKey(lastChar) || lastChar.equals(".")){
                return;
            }else{
                if(inOut.getText().contains("-")){
                    inOut.setText(inOut.getText().replace("-", ""));
                }else {
                    inOut.setText("-" + inOut.getText());
                }
            }
        //кнопка =
        }else if ((source.getText()).equals("=")) {
            if (inOut.getText().equals("В выражении не хватает скобок")){
                commands.clear();
                expression.setText("");
                inOut.setText("0");
            }
            if ((calcConsole.map.containsKey(lastChar) && !lastChar.equals(")")) || lastChar.equals(".") || (inOut.getText().equals("0") && commands.isEmpty()) || commands.size()<2){
                return;
            }else if (commands.contains("(") || commands.contains(")")) {
                commands.add(inOut.getText());
                int openCounter = 0;
                int closeCounter = 0;
                for (int i = 0; i < commands.size(); i++) {
                    if (commands.get(i).equals("(")) {
                        openCounter++;
                    }
                    if (commands.get(i).equals(")")) {
                        closeCounter++;
                    }
                }
                if (openCounter != closeCounter) {
                    inOut.setText("В выражении не хватает скобок");
                    return;
                } else {
                    ArrayList<String> postfix_ar = calcConsole.infixToPostfix(commands);
                    Double result = calcConsole.onStackCalculator(postfix_ar);
                    expression.setText(arrayToString(commands) + source.getText());
                    inOut.setText(result.toString());
                }
            }else{
                commands.add(inOut.getText());
                expression.setText(arrayToString(commands) + source.getText());
                ArrayList<String> postfix_ar = calcConsole.infixToPostfix(commands);
                Double result = calcConsole.onStackCalculator(postfix_ar);
                inOut.setText(result.toString());
            }

        //кнопки +, -, *, /, ^
        }else if (calcConsole.map.containsKey(source.getText()) && !(source.getText()).equals("(") && !(source.getText()).equals(")")) {
            if (inOut.getText().equals("В выражении не хватает скобок")) {
                commands.clear();
                expression.setText("");
                inOut.setText("0");
            }
            if (expression.getText().contains("=")) {
                expression.setText(inOut.getText());
                commands.clear();
                commands.add(inOut.getText());
                inOut.setText(source.getText());
            } else {
                if (((inOut.getText()).equals("0") && commands.isEmpty()) || lastChar.equals(".")) {
                    return;
                } else if (calcConsole.map.containsKey(lastChar) && !lastChar.equals(")") && !lastChar.equals("(")) {
                    inOut.setText(source.getText());
                } else if (lastChar.equals("(")) {
                    if (commands.isEmpty()) {
                        return;
                    }
                    if (!commands.isEmpty() && calcConsole.map.containsKey(commands.get(commands.size() - 1)) && !commands.get(commands.size() - 1).equals("(") && !commands.get(commands.size() - 1).equals(")")) {
                        return;
                    }
                } else {
                    commands.add(inOut.getText());
                    inOut.setText(source.getText());
                    expression.setText(arrayToString(commands));
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

