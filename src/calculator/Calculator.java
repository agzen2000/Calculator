package calculator;
import java.util.*;
public class Calculator {
    public static double result;
    public static String converted;
    public static void main(String[] args) {
        //INTRODUCTION
        System.out.println("Welcome to the Console Calculator!");
        System.out.println("");
        if(wantRegular())
            regularCalc();
        else
            reversePolish();
        System.out.println("Thanks for stopping by!");
    }
    public static boolean wantRegular() {  //Asks for calculator mode
        Scanner scan=new Scanner(System.in);
        String str;
        while(true) {
            System.out.println("Do you want a 'regular' or 'RPN' calculator?");
            str=scan.next();
            if(str.equalsIgnoreCase("regular"))
                return true;
            else if(str.equalsIgnoreCase("rpn"))
                return false;
            else
                System.out.println("Invalid Statement");
        }
    }
    
    public static String input() { 
        System.out.print("Enter an expression (Type Quit to exit): ");
        Scanner input=new Scanner(System.in);
        String str=input.nextLine();
        return str;
    }
    
    public static void regularCalc() {
        //Variables
        String userIn;
        boolean i;
        //Loop
        while(true) {
            userIn=input();
            if(userIn.equalsIgnoreCase("quit"))
                break;  //quits the program
            else {
                i=convertToRpn(userIn); //Converts Equation to Reverse Polish Notation
                if(i)
                    i=stackCalc(converted);   //Calculates the result and returns true. Returns false is invalid
                if(i)
                    System.out.println(userIn+" = "+result);  //Displays the result
            }
        }
    }
    
    public static boolean convertToRpn(String input) {
        Scanner scan=new Scanner(input);
        String output="";
        Stack operator=new Stack();
        String in;
        if(!scan.hasNext())
            return false;
        while(scan.hasNext()) {
            if(scan.hasNextInt())
                output=output+scan.nextInt()+" ";
            else if(scan.hasNextDouble())
                output=output+scan.nextDouble()+" ";
            else if(scan.hasNext()) {
                in=scan.next();
                if(in.length()>1) {
                    System.out.println("Invalid Statement");
                    return false; }
                if(in.equals("("))
                    operator.add("(");
                else if(in.equals(")")) {
                    if(operator.empty()) {
                        System.out.println("Invalid Statement");
                        return false; 
                    }
                    while(!((String)operator.peek()).equals("(")) {
                        if(operator.empty()) {
                            System.out.println("Invalid Statement");
                            return false;
                        }
                        output=output+((String)operator.pop())+" ";
                    }
                    operator.pop();
                    if(!operator.peek().equals("("))
                        output=output+((String)operator.pop())+" ";
                }
                else {
                    if(operator.empty()||((String)operator.peek()).equals("("))
                        operator.push(in);
                    else {
                        while(presidence(in.charAt(0))<=presidence(((String)operator.peek()).charAt(0))) {
                            output=output+((String)operator.pop())+" ";
                            if(operator.empty()||((String)operator.peek()).equals("("))
                                break;
                        }
                        operator.push(in);
                        }
                    }
                        
            }
        }
        while(!operator.empty())
            output=output+((String)operator.pop())+" ";
        converted=output;
        return true;
    }
    
    public static int presidence(char c) { //Determines precidence of Operator
        if(c=='^'||c=='~'||c=='|'||c=='v')
            return 3;
        else  if(c=='*'||c=='/'||c=='s'||c=='c'||c=='t'||c=='%')
            return 2;
        else
            return 1;
    }
       
    public static void reversePolish() {
         //Variables
        String userIn;
        boolean i=false;
        //Loop
        while(true) {
            userIn=input();
            if(userIn.equalsIgnoreCase("quit"))
                break;  //quits the program
            else if(userIn.equals(""))
            {}
            else {
                i=stackCalc(userIn); //Calculates the result and returns true. Returns false is invalid
            }
            if(i)
                System.out.println(userIn+" = "+result);    //Prints the result
        }
        
    }
    
    public static boolean stackCalc(String user) {
        Scanner str=new Scanner(user);
        Stack equation=new Stack();
        String operator;
        boolean i;
        Double x;
        Double y;
        while(str.hasNext()) {
            if(str.hasNextInt())
                equation.push((double)(str.nextInt()));
            else if(str.hasNextDouble())
                equation.push(str.nextDouble());
            else {
                operator=str.next();
                if(operator.length()>1) {
                    System.out.println("Invalid statement");
                    return false;
                }
                if(operator.equals("+")||operator.equals("-")||operator.equals("*")||operator.equals("/")
                        ||operator.equals("%")||operator.equals("^")) {
                    if(equation.empty()) {
                        System.out.println("Invalid Statement");
                        return false; }
                    x=(Double)equation.pop();
                    if(equation.empty()) {
                        System.out.println("Invalid Statement");
                        return false; }
                    y=(Double)equation.pop();
                    i=result2Op(y,x,operator.charAt(0));
                    if(!i)
                        return false;
                    equation.push(result);
                }
                else if(operator.equals("|")||operator.equals("v")||operator.equals("~")||operator.equals("c")
                        ||operator.equals("s")||operator.equals("t")) {
                    x=(Double)equation.pop();
                    i=result1Op(operator.charAt(0),x);
                    if(!i)
                        return false;
                    equation.push(result);
                }
                else {
                    System.out.println("Invalid Statement");
                    return false;
                }
            }
        }
        result=(Double)equation.pop();
        if(equation.empty())
            return true;
        System.out.println("Invalid Statement");
        return false;
    }
    
    public static boolean result2Op(double x, double y, char c){
        if(c=='+') {
            result=x+y;
            return true; }
        else if(c=='-') {
            result=x-y;
            return true; }
        else if(c=='*') {
            result=x*y;
            return true; }
        else if(c=='/') {
            result=x/y;
            return true; }
        else if(c=='%') {
            result=x%y;
            return true; }
        else if(c=='^') {
            result=Math.pow(x, y);
            return true; }
        else
            System.out.println("Invalid statement");
            return false;
    }

    public static boolean result1Op(char c,double x) {
        if(c=='|') {
            result=Math.abs(x);
            return true; }
        else if(c=='v') {
            result=Math.sqrt(x);
            return true; }
        else if(c=='~') {
            result=Math.round(x);
            return true; }
        else if(c=='s') {
            result=Math.sin(x);
            return true; }
        else if(c=='c') {
            result=Math.cos(x);
            return true; }
        else if(c=='t') {
            result=Math.tan(x);
            return true; }
        else
            System.out.println("Invalid statement");
            return false;
    }
}