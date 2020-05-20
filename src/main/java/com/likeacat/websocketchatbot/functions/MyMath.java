package com.likeacat.websocketchatbot.functions;

public class MyMath {
    public static String compute(String expr) {
        String result;
        String regex = "[\\d|.]+";
        double[] terms;
        expr = expr.trim();
        //System.out.println(expr);
        if (expr.charAt(0) == '(' && expr.charAt(expr.length() - 1) == ')') {
            expr = expr.substring(1, expr.length() - 1);
        }
        if (expr.contains("(")) {
            int count1 = 0, count2 = 0;
            for(int i = 0; i < expr.length(); i++) {
                if(expr.charAt(i) == '(')
                    count1++;
                if(expr.charAt(i) == ')')
                    count2++;
            }
            if (count1 != count2)
                return "Invalid number of braces. Please check and try again.";
            String temp, t;
            int i;
            int ind1;
            while (expr.contains("(")) {
                ind1 = expr.indexOf("(");
                for (i = expr.length() - 1; expr.charAt(i) != ')'; i--) ;
                temp = expr.substring(ind1 + 1, i);
                if (temp.contains("("))
                    t = compute(temp);
                else
                    t = compute(parseToDouble(temp));
                expr = expr.substring(0, ind1) + t + expr.substring(i + 1);
            }
            return compute(parseToDouble(expr));
        }
        else {
            expr = parseToDouble(expr);
        }
        if (expr.contains("^")) {
            terms = getRightLeft(expr, "^");
            result = Double.toString(Math.pow(terms[0], terms[1]));
            expr = expr.replace(terms[0] + "^" + terms[1], result);
            //System.out.println("^ " + expr);
            return compute(expr);
        }
        if (expr.contains("/")) {
            terms = getRightLeft(expr, "/");
            result = Double.toString(terms[0] / terms[1]);
            expr = expr.replace(terms[0] + "/" + terms[1], result);
            //System.out.println("/ " + expr);
            return compute(expr);
        }
        if (expr.contains("*")) {
            terms = getRightLeft(expr, "*");
            result = Double.toString(terms[0] * terms[1]);
            expr = expr.replace(terms[0] + "*" + terms[1], result);
            //System.out.println("* " + expr);
            return compute(expr);
        }
        if (expr.contains("+")) {
            terms = getRightLeft(expr, "+");
            result = Double.toString(terms[0] + terms[1]);
            expr = expr.replace(terms[0] + "+" + terms[1], result);
            //System.out.println("+ " + expr);
            return compute(expr);
        }
        if (expr.contains("-")) {
            terms = getRightLeft(expr, "-");
            result = Double.toString(terms[0] - terms[1]);
            expr = expr.replace(terms[0] + "-" + terms[1], result);
            //System.out.println("- " + expr);
            return compute(expr);
        }
        if (expr.matches(regex)) {
            result = expr;
        } else {
            result = "Expression is invalid. Please check and try again.";
        }
        return "The result is " + result + ".";
    }

    public static double[] getRightLeft (String expr, String operator) {
        int indR, indL;
        int ind = expr.indexOf(operator);
        for (indL = ind - 1; (Character.isDigit(expr.charAt(indL)) || Character.toString(expr.charAt(indL)).equals(".")) && indL != 0; indL--);
        if (indL != 0)
            indL++;
        for (indR = ind + 1; (Character.isDigit(expr.charAt(indR)) || Character.toString(expr.charAt(indR)).equals(".")) && (indR != expr.length() - 1); indR++);
        if (indR == expr.length() - 1)
            indR++;
        String strterm1 = expr.substring(indL,ind);
        String strterm2 = expr.substring(ind+1,indR);
        double term1 = Double.parseDouble(strterm1);
        double term2 = Double.parseDouble(strterm2);

        return new double[]{term1, term2};
    }

    public static String parseToDouble (String expr) {
        int ind1, ind2;
        int i = 0;
        if (Character.isDigit(expr.charAt(0)) && Character.isDigit(expr.charAt(expr.length()-1))) {
            while (!(i >= expr.length() - 1)) {
                //System.out.println("while");
                for (ind1 = i; !(Character.isDigit(expr.charAt(ind1)) || Character.toString(expr.charAt(ind1)).equals(".")) && ind1 != expr.length() - 1; ind1++)
                    ;
                for (ind2 = i + 1; (Character.isDigit(expr.charAt(ind2)) || expr.charAt(ind2) == '.') && ind2 != expr.length() - 1; ind2++)
                    ;
                if (ind2 == expr.length() - 1)
                    ind2++;
                //System.out.println("ind1 = " + ind1 + " ind2 = " + ind2);
                String num = expr.substring(ind1, ind2);
                //System.out.println("num = " + num);
                if (!num.contains(".")) {
                    num = num + ".0";
                    expr = expr.substring(0, ind1) + num + expr.substring(ind2);
                    i = ind2 + 2;
                } else {
                    i = ind2;
                }
                //System.out.println("i = " + i);
            }
            return expr;
        }
        else
            return "parse Expression is invalid. Please check and try again.";
    }
}
