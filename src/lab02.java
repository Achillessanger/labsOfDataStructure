import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class lab02 {
    public static void main(String[] args){
        Scanner inputSc = new Scanner(System.in);
        System.out.println("Please input the infix expression（press enter to translate it into postfix expression):");
        String inputStr = inputSc.nextLine();

        ArrayList<String> postfixArr = inToPost(inputStr);
        for (String postfixlist : postfixArr){
            System.out.print(postfixlist);
        }

        System.out.println("\nresult: "+calaulate(postfixArr));

    }

    public static ArrayList<String> inToPost(String inputStr){
        Stack<String> conversionStack = new Stack();
        ArrayList<String> infixArr = new ArrayList<>();
        ArrayList<String> postfixArr = new ArrayList<>();

        infixArr = checkExpression(inputStr); //check表达式正确性

        for (String infixlist : infixArr){
           if(isOper(infixlist)){
               if(conversionStack.empty()){
                   conversionStack.push(infixlist);
               }else {
                   if(infixlist.equals("(") || infixlist.equals("{") || infixlist.equals("[")){
                       conversionStack.push("(");
                   }else if(infixlist.equals(")") || infixlist.equals("}") || infixlist.equals("]")){
                       while (!conversionStack.empty()&&!conversionStack.peek().equals("(")){
                           postfixArr.add(conversionStack.pop());
                       }
                       if(!conversionStack.empty()){
                           conversionStack.pop();
                       }
                   }else {
                       if(ifPrior(infixlist,conversionStack)) conversionStack.push(infixlist);
                       else {
                           while (!conversionStack.empty()&&(!ifPrior(infixlist,conversionStack))){
                               postfixArr.add(conversionStack.pop());
                           }
                           conversionStack.push(infixlist);
                       }
                   }
               }
           }else if(isNum(infixlist)){
               postfixArr.add(infixlist);
           }else {
               System.out.println("wrong expression");
               System.exit(0);
           }
        }
        while (!conversionStack.empty()){
            postfixArr.add(conversionStack.pop());
        }
        for (int i = 0, len = postfixArr.size(); i <len; i++){
            if(postfixArr.get(i).equals("(") || postfixArr.get(i).equals("{") || postfixArr.get(i).equals("[")){
                postfixArr.remove(i);
                --len;
                --i;
            }
        }
        return postfixArr ;
    }

    public static boolean isNum(String str){
        if(str.matches("^(\\-|\\+)?\\d+(\\.\\d+)?$")) return true;
        else return false;
    }

    public static boolean isOper(String str){
        String pattern = ".*>>.*";
        String pattern2 = ".*>>>.*";
        String pattern3 = ".*<<.*";
        String pattern4 = ".*log.*";
        if(Pattern.matches(pattern,str)) return true;
        if(Pattern.matches(pattern2,str)) return true;
        if(Pattern.matches(pattern3,str)) return true;
        if(Pattern.matches(pattern4,str)) return true;
        if("+-*/(){}[]%'^&|".indexOf(str)!= -1) return true;
        else return false;
    }

    public static boolean ifPrior(String str, Stack<String> stack ){ //如果本符号比stack里的优先度高返true
        if("<<".equals(str)||">>>".equals(str)||">>".equals(str)){
            str = "<";
        }else if("log".equals(str)){
            str = "l";
        }else if("{".equals(str)||"[".equals(str)){
            str = "(";
        }
        String stra = stack.peek();
        if("<<".equals(stra)||">>>".equals(stra)||">>".equals(stra)){
            stra = "<";
        }else if("log".equals(stra)){
            stra = "l";
        }else if("{".equals(stra)||"[".equals(stra)){
            stra = "(";
        }

        String partern = "(^<+*l'0&0-/000|";
        if((partern.indexOf(str)%7) > (partern.indexOf(stra)%7)) return true;
        else return false;
    }


    public static double calaulate(ArrayList<String> postfixArr){
        Stack evaluationStack = new Stack();

        for (String postfixList : postfixArr){
            if(isNum(postfixList)){
                evaluationStack.push(postfixList);
            }else if(isOper(postfixList)){
                judgeOperAndCal(evaluationStack,postfixList);
            }
        }

        return Double.parseDouble(evaluationStack.pop().toString());
    }


    public static void judgeOperAndCal(Stack stack, String str){
        switch (str){
            case "+":
                stack.push(Double.parseDouble(stack.pop().toString()) +  Double.parseDouble(stack.pop().toString()));
                break;
            case "-":
                stack.push(Double.parseDouble(stack.pop().toString()) -  Double.parseDouble(stack.pop().toString()));
                break;
            case "*":
                stack.push(Double.parseDouble(stack.pop().toString()) *  Double.parseDouble(stack.pop().toString()));
                break;
            case "/":
                double a = Double.parseDouble(stack.pop().toString());
                double b = Double.parseDouble(stack.pop().toString());
                stack.push(b/a);
                break;
            case "%":
                double c = Double.parseDouble(stack.pop().toString());
                double d = Double.parseDouble(stack.pop().toString());
                stack.push(d%c);
                break;
            case ">>":
                double e = Double.parseDouble(stack.pop().toString());
                double f = Double.parseDouble(stack.pop().toString());
                if(((int)e==e)&&((int)f==f)){
                    stack.push(((int)f)>>((int)e));
                }else {
                    System.out.println("wrong expression(number problem)");
                }
                break;
            case ">>>":
                double g = Double.parseDouble(stack.pop().toString());
                double h = Double.parseDouble(stack.pop().toString());
                if(((int)g==g)&&((int)h==h)){
                    stack.push(((int)h)>>((int)g));
                }else {
                    System.out.println("wrong expression(number problem)");
                }
                break;
            case "<<":
                double i = Double.parseDouble(stack.pop().toString());
                double j = Double.parseDouble(stack.pop().toString());
                if(((int)i==i)&&((int)j==j)){
                    stack.push(((int)j)<<((int)i));
                }else {
                    System.out.println("wrong expression(number problem)");
                }
                break;
            case "'":
                double l = Double.parseDouble(stack.pop().toString());
                double k = Double.parseDouble(stack.pop().toString());
                stack.push(Math.pow(k,l));
                break;
            case "log":
                double n = Double.parseDouble(stack.pop().toString());
                double m = Double.parseDouble(stack.pop().toString());   //mlogn  logm n
                stack.push(Math.log(n)/Math.log(m));
                break;
            case "^":
                double p = Double.parseDouble(stack.pop().toString());
                double o = Double.parseDouble(stack.pop().toString());
                if(((int)o==o)&&((int)p==p)){
                    stack.push(((int)o)^((int)p));
                }else {
                    System.out.println("wrong expression(number problem)");
                }
                break;
            case "&":
                double r = Double.parseDouble(stack.pop().toString());
                double q = Double.parseDouble(stack.pop().toString());
                if(((int)q==q)&&((int)r==r)){
                    stack.push(((int)q)&((int)r));
                }else {
                    System.out.println("wrong expression(number problem)");
                }
                break;
            case "|":
                double t = Double.parseDouble(stack.pop().toString());
                double s = Double.parseDouble(stack.pop().toString());
                if(((int)s==s)&&((int)t==t)){
                    stack.push(((int)s)|((int)t));
                }else {
                    System.out.println("wrong expression(number problem)");
                }
                break;
        }
    }

    public static ArrayList<String> checkExpression(String inputStr){
        ArrayList<String> checkArr = new ArrayList<>();
        inputStr = inputStr.replaceAll(" ","");
        StringTokenizer stringTokenizer = new StringTokenizer(inputStr,"+-*/(){}%[]'^&|",true);
        while (stringTokenizer.hasMoreTokens()){
            String token = stringTokenizer.nextToken();
            String pattern = ".*>>.*";
            String pattern2 = ".*>>>.*";
            String pattern3 = ".*<<.*";
            String pattern4 = ".*log.*";
            if(Pattern.matches(pattern,token)){
                token = " "+token+" ";
                String[] splitOne = token.split(">>");
                if(!splitOne[0].equals(" ")){splitOne[0] = splitOne[0].replaceAll(" ",""); checkArr.add(splitOne[0]);}
                checkArr.add(">>");
                if(!splitOne[1].equals(" ")) {splitOne[1] = splitOne[1].replaceAll(" ",""); checkArr.add(splitOne[1]);}
            }else if(Pattern.matches(pattern2,token)){
                token = " "+token+" ";
                String[] splitOne = token.split(">>>");
                if(!splitOne[0].equals(" ")){splitOne[0] = splitOne[0].replaceAll(" ",""); checkArr.add(splitOne[0]);}
                checkArr.add(">>>");
                if(!splitOne[1].equals(" ")) {splitOne[1] = splitOne[1].replaceAll(" ",""); checkArr.add(splitOne[1]);}
            }else if(Pattern.matches(pattern3,token)){
                token = " "+token+" ";
                String[] splitOne = token.split("<<");
                if(!splitOne[0].equals(" ")){splitOne[0] = splitOne[0].replaceAll(" ",""); checkArr.add(splitOne[0]);}
                checkArr.add("<<");
                if(!splitOne[1].equals(" ")) {splitOne[1] = splitOne[1].replaceAll(" ",""); checkArr.add(splitOne[1]);}
            }else if(Pattern.matches(pattern4,token)){
                token = " "+token+" ";
                String[] splitOne = token.split("log");
                if(!splitOne[0].equals(" ")){splitOne[0] = splitOne[0].replaceAll(" ",""); checkArr.add(splitOne[0]);}
                checkArr.add("log");
                if(!splitOne[1].equals(" ")) {splitOne[1] = splitOne[1].replaceAll(" ",""); checkArr.add(splitOne[1]);}
            }
            else {
                checkArr.add(token);
            }
        }

        int isPreviousAnOper = 0;
        Stack bracesStack = new Stack(); //判断符号

        for (String checkList : checkArr){
            if(isOper(checkList)){
                if(isPreviousAnOper == 1 ){ //不能连续出现两个非括号操作符
                    switch (checkList){
//                        case "-":
//                            break;
                        case "(":
                            break;
                        case "{":
                            break;
                        case "[":
                            break;
                        default:
                            System.out.println("wrong expression(operators problem)");
                            System.exit(0);
                    }
                }
                if(checkList.equals(")")||checkList.equals("]")||checkList.equals("}")){
                    switch (checkList){
                        case ")":
                            if(bracesStack.pop().equals("(")){
                                isPreviousAnOper = 2;
                            }else {
                                System.out.println("wrong expression(braces problem)");
                                System.exit(0);
                            }
                            break;
                        case "]":
                            if(bracesStack.pop().equals("[")){
                                isPreviousAnOper = 2;
                            }else {
                                System.out.println("wrong expression(braces problem)");
                                System.exit(0);
                            }
                            break;
                        case "}":
                            if(bracesStack.pop().equals("{")){
                               isPreviousAnOper = 2;
                            }else {
                                System.out.println("wrong expression(braces problem)");
                                System.exit(0);
                            }
                            break;
                    }
                }else {
                    isPreviousAnOper = 1;
                }
                if("([{".indexOf(checkList)!= -1){
                    if(bracesStack.empty()){
                        bracesStack.push(checkList);
                    }else if(ifPush(checkList,bracesStack)){
                        bracesStack.push(checkList);
                    }else {
                        System.out.println("wrong expression(braces problem)");
                        System.exit(0);
                    }

                }

            }else if(isNum(checkList)){
                if(isPreviousAnOper == 2){
                    System.out.println("wrong expression(number&brace problem)");
                    System.exit(0);
                }else isPreviousAnOper = 0;
            }
        }
        return checkArr;
    }

    public static boolean ifPush(String str, Stack<String> stack ){ //如果本符号比stack里的优先度高返true
        String partern = "{[(";
        if((partern.indexOf(str)%3) >= (partern.indexOf(stack.peek())%3)) return true;
        else return false;
    }
}
