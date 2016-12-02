
//
//Name:       Lu, Shun
//Homework:   #3
//Due:        Thursday June 11, 2015
//Course:     cs-240-02-sp15
//
//Description:
//This program evaluates postfix and return
//result as integer.
//

import java.util.Stack;

public class Postfix
{
   public static int evalPostfix(String[] postfix, String[] variables, String[] values)
   {
      Stack<Integer> stack = new Stack<>();
      // returning value
      int result = 0;
      // convert string to integer
      int intNum = 0;
      // two operands
      int left = 0;
      int right = 0;
      // found variable
      boolean found;
      
      for (int i = 0; i < variables.length; i++)
      {
         found = false;
         for (int j = 0; j < postfix.length; j++)
         {
            if (postfix[j].equals(variables[i]))
            {
               postfix[j] = values[i];
               found = true;
            }
         }
         if (!found)
            throw new RuntimeException("variable missing");
      }
      
      for (String token : postfix)
      {
         switch (token)
         {
            case "+":
               if (stack.empty())
                  throw new RuntimeException("malformed expression");
               left = stack.pop();
               if (stack.empty())
                  throw new RuntimeException("malformed expression");
               right = stack.pop();
               result = left + right;
               stack.push(result);
               break;
            case "-":
               if (stack.empty())
                  throw new RuntimeException("malformed expression");
               left = stack.pop();
               if (stack.empty())
                  throw new RuntimeException("malformed expression");
               right = stack.pop();
               result = left - right;
               stack.push(result);
               break;
            case "*":
               if (stack.empty())
                  throw new RuntimeException("malformed expression");
               left = stack.pop();
               if (stack.empty())
                  throw new RuntimeException("malformed expression");
               right = stack.pop();
               result = left * right;
               stack.push(result);
               break;
            case "/":
               if (stack.empty())
                  throw new RuntimeException("malformed expression");
               left = stack.pop();
               if (stack.empty())
                  throw new RuntimeException("malformed expression");
               right = stack.pop();
               result = left / right;
               stack.push(result);
               break;
            default:
               intNum = Integer.parseInt(token);
               stack.push(intNum);
               break;
         }
      }
      result = stack.pop();
      if (!stack.empty())
         throw new RuntimeException("malformed expression");
      return result;
   }
   
   public static void main(String[] args)
   {
      String[] infix = 
      { "(", "a", "+", "b", ")", "*", "3" };
      String[] variables =
      { "a", "b" };
      String[] values =
      { "1", "2" };
      
      String[] postfix = InfixExpression.convertToPostfix(infix);
      
      System.out.println(evalPostfix(postfix, variables, values));
   }
}
