
//
//Name:       Lu, Shun
//Project:    #3
//Due:        Thursday June 3, 2015
//Course:     cs-240-02-sp15
//
//Description:
//This program takes infix expression as string array 
//and converts it to prefix or postfix.
//

import java.util.Stack;

public class InfixExpression
{
   // infix to postfix
   public static String[] convertToPostfix(String[] infixExpression)
   {
      // counting length for postfix to allocate
      int countOperand = 0;
      int countOperator = 0;
      for (String token : infixExpression)
      {
         if (!token.equals("(") && !token.equals(")"))
         {
            if (token.equals("+") || token.equals("-") ||
                token.equals("*") || token.equals("/"))
               countOperator++;
            else
               countOperand++;
         }
      }
      if (countOperand <= countOperator)
         throw new RuntimeException("operands missing");
      String[] postfix = new String[countOperand + countOperator];
      Stack<String> stack = new Stack<>();
      // count to put new elements from beginning
      int count = 0;
      for (String token : infixExpression)
      {
         if (token.equals("("))
            stack.push(token);
         else if (token.equals(")"))
         {
            while (!stack.empty() && !stack.peek().equals("("))
            {
               postfix[count++] = stack.pop();
            }
            if (stack.empty())
               throw new RuntimeException("expression unbalanced");
            // discard left parentheses
            stack.pop();
         }
         else if (token.equals("+") || token.equals("-"))
         {
            while (!stack.empty() && !stack.peek().equals("("))
               postfix[count++] = stack.pop();
            stack.push(token);
         }
         else if (token.equals("*") || token.equals("/"))
         {
            while (!stack.empty() && !stack.peek().equals("(") &&
                   !stack.peek().equals("+") && !stack.peek().equals("-"))
               postfix[count++] = stack.pop();
            stack.push(token);
         }
         // directly copy variables
         else
         {
            postfix[count++] = token;
         }
      }
      // done scanning infix
      // pop rest of stack until empty
      while (!stack.empty())
      {
         if (stack.peek().equals("("))
            throw new RuntimeException("expression unbalanced");
         postfix[count++] = stack.pop();
      }
      // string array with all elements added
      return postfix;
   }
   
   // infix to prefix
   public static String[] convertToPrefix(String[] infixExpression)
   {
      int countOperand = 0;
      int countOperator = 0;
      for (String token : infixExpression)
      {
         if (!token.equals("(") && !token.equals(")"))
         {
            if (token.equals("+") || token.equals("-") ||
                token.equals("*") || token.equals("/"))
               countOperator++;
            else
               countOperand++;
         }
      }
      if (countOperand <= countOperator)
         throw new RuntimeException("operands missing");
      String[] prefix = new String[countOperand + countOperator];
      Stack<String> operand = new Stack<>();
      Stack<String> operator = new Stack<>();
      String op, left, right, prefixString;
      op = left = right = prefixString = "";
      
      for (String token : infixExpression)
      {
         if (token.equals("("))
            operator.push(token);
         else if (token.equals(")"))
         {
            while (!operator.empty() && !operator.peek().equals("("))
            {
               op = operator.pop();
               right = operand.pop();
               left = operand.pop();
               operand.push(op + left + right);
            }
            if (operator.empty())
               throw new RuntimeException("expression unbalanced");
            operator.pop();
         }
         else if (token.equals("+") || token.equals("-"))
         {
            while (!operator.empty() && !operator.peek().equals("("))
            {
               op = operator.pop();
               right = operand.pop();
               left = operand.pop();
               operand.push(op + left + right);
            }
            operator.push(token);
         }
         else if (token.equals("*") || token.equals("/"))
         {
            while (!operator.empty() && !operator.peek().equals("(") &&
                   !operator.peek().equals("+") && !operator.peek().equals("-"))
            {
               op = operator.pop();
               right = operand.pop();
               left = operand.pop();
               operand.push(op + left + right);
            }
            operator.push(token);
         }
         else
         {
            operand.push(token);
         }
      }
      while (!operator.empty())
      {
         if (operator.peek().equals("("))
            throw new RuntimeException("expression unbalanced");
         right = operand.pop();
         if (!operand.empty())
         {
            left = operand.pop();
            prefixString += operator.pop() + left + right;
         }
         else
         {
            String temp = operator.pop() + right;
            prefixString = temp + prefixString;
         }
      }
      // rebuilding prefix array
      for(int i = 0; i < prefixString.length(); i++)
         prefix[i] = prefixString.substring(i, i+1);
      return prefix;
   }
   
   public static void main(String[] args)
   {
      String[] expression1 =
      { "(", "a", "+", "b", ")", "*", "(", "c", "-", "d", ")" };
      String[] expression2 =
      { "a", "+", "b", "/", "c" };
      String[] expression3 =
      { "a", "+", "b", "+", "c" };
      String[] expression4 =
      { "(", "7", "+", "(", "3", "*", "5", ")", ")", "-", "4" };
      String[] unmatched =
      { "(", "a", "+", "b", "/", "c" };
      String[] missing = 
      { "a", "+" };
      
      String[] prefix1 = convertToPrefix(expression1);
      String[] postfix1 = convertToPostfix(expression1);
      for (String token : prefix1)
         System.out.print(token);
      System.out.println();
      for (String token : postfix1)
         System.out.print(token);
      System.out.println();
      
      String[] prefix2 = convertToPrefix(expression2);
      String[] postfix2 = convertToPostfix(expression2);
      for (String token : prefix2)
         System.out.print(token);
      System.out.println();
      for (String token : postfix2)
         System.out.print(token);
      System.out.println();
      
      String[] prefix3 = convertToPrefix(expression3);
      String[] postfix3 = convertToPostfix(expression3);
      for (String token : prefix3)
         System.out.print(token);
      System.out.println();
      for (String token : postfix3)
         System.out.print(token);
      System.out.println();
      
      String[] prefix4 = convertToPrefix(expression4);
      String[] postfix4 = convertToPostfix(expression4);
      for (String token : prefix4)
         System.out.print(token);
      System.out.println();
      for (String token : postfix4)
         System.out.print(token);
      System.out.println();
      
      try
      {
         String[] prefix5 = convertToPrefix(unmatched);
         for (String token : prefix5)
            System.out.print(token);
         System.out.println();
      } catch (RuntimeException e)
      {
         e.printStackTrace();
      }  
      try
      {
         String[] postfix5 = convertToPostfix(missing);
         for (String token : postfix5)
            System.out.print(token);
         System.out.println();
      } catch (RuntimeException e)
      {
         e.printStackTrace();
      }
   }
}
