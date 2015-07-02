package calculator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by Irka on 09.06.2015.
 * https://ru.wikipedia.org/wiki/%D0%9E%D0%B1%D1%80%D0%B0%D1%82%D0%BD%D0%B0%D1%8F_%D0%BF%D0%BE%D0%BB%D1%8C%D1%81%D0%BA%D0%B0%D1%8F_%D0%B7%D0%B0%D0%BF%D0%B8%D1%81%D1%8C
 */
public class CalculatorConsole {

    public HashMap<String,Integer> map = new HashMap<String, Integer>(){
        {
            put("^", 4);
            put("*", 3);
            put("/", 3);
            put("+", 2);
            put("-", 2);
            put("(", 1);
            put(")", 1);
        }
    };

    //разбиваем входную строку на массив строк (операнды и операторы)
    public ArrayList<String> expressionSplitter(String str){
        ArrayList<String> array = new ArrayList<String>();
        int index_left = 0;
        int index_right = 0;
        for (int i = 0; i < str.length(); i++) {
            if (map.containsKey(str.substring(i, i+1))){
                index_right = i;
                if (index_left != index_right){
                    array.add(str.substring(index_left,index_right));
                }
                array.add(str.substring(i,i+1));
                index_left = index_right + 1;
            }else{
                while (map.containsKey(str.substring(i,i+1))){
                    continue;
                }
            }
            if (i == (str.length() - 1) && !map.containsKey(str.substring(i, i+1))){
                array.add(str.substring(index_left));
            }
        }
        return array;
    }

    //сортировочная станция (преобразование из инфиксной нотации в обратную польскую)
    public ArrayList<String> infixToPostfix(ArrayList<String> arrayList){
        final ArrayList<String> postfix_array = new ArrayList<String>();
        ArrayList<String> stack = new ArrayList<String>();
        for (int i = 0; i < arrayList.size(); i++){
            if (!map.containsKey(arrayList.get(i))){
                postfix_array.add(arrayList.get(i));
            }else{
                if (!(arrayList.get(i)).equals(")")) {
                    if (!arrayList.get(i).equals("(")) {
                        while (!stack.isEmpty() && map.get(arrayList.get(i)) <= map.get(stack.get(stack.size() - 1))) {
                            postfix_array.add(stack.get(stack.size() - 1));
                            stack.remove(stack.size() - 1);
                        }
                    }
                    stack.add(arrayList.get(i));
                }else {
                    if (stack.indexOf("(") == -1) {
                        System.out.println("В ВЫРАЖЕНИИ НЕ ХВАТАЕТ ОТКРЫВАЮЩЕЙ СКОБКИ");
                        break;
                    }
                    while (!(stack.get(stack.size()-1)).equals("(")){
                        postfix_array.add(stack.get(stack.size()-1));
                        stack.remove(stack.size()-1);
                    }
                    if ((stack.get(stack.size()-1)).equals("(")){
                        stack.remove(stack.size()-1);
                    }
                }
            }
        }
        while (!(stack.isEmpty())){
            postfix_array.add(stack.get(stack.size()-1));
            stack.remove(stack.size()-1);
        }
        return postfix_array;
    }

    public double onStackCalculator(ArrayList<String> opn_array){
        double result = 0;
        Stack<Double> stack = new Stack<Double>();
        for (int i = 0; i < opn_array.size(); i++){
            if (!map.containsKey(opn_array.get(i))){
                String st = opn_array.get(i);
                if (st.charAt(0) == '~'){
                    st = st.replace('~', '-');
                }
                Double operand = Double.parseDouble(st);
                stack.push(operand);
            }else{
                Double op1 = stack.pop();
                Double op2 = stack.pop();
                stack.push(f1(opn_array.get(i), op2, op1));
            }
        }
        result = stack.pop();
        return result;
    }

    private double f1(String operator, Double op1, Double op2){
        double result = 0;
        switch (operator.charAt(0)){
            case '+':
                result = op1+op2;
                break;
            case '-':
                result = op1-op2;
                break;
            case '*':
                result = op1*op2;
                break;
            case '/':
                if (op2 == 0){
                    JFrame frame = new JFrame("АЦАЦА!!!");
                    JPanel panel = new JPanel(new BorderLayout());
                    frame.getContentPane().add(panel);
                    panel.add(new JTextField("В данном калькуляторе делить на ноль нельзя! =)"));
                    frame.setResizable(false);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    break;
                }else {
                    result = op1 / op2;
                    break;
                }
            case '^':
                result = 1;
                for (int i = 1; i <= op2; i++){
                    result = result * op1;
                }
        }
        return result;
    }

    /*public static void main(String args[]){

        System.out.println("Enter your mathematical expression, then press ENTER:");
        Scanner scanner = new Scanner(System.in);
        String math_expression = scanner.nextLine();

        ArrayList<String> math_ex_array = expressionSplitter(math_expression);
        System.out.println(math_ex_array);

        ArrayList<String> opn_ex_array = infixToPostfix(math_ex_array);
        System.out.println(opn_ex_array);

        Double result = onStackCalculator(opn_ex_array);
        System.out.println(result);



    }*/
}
