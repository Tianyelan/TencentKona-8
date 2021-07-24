import java.util.Random;

public class ExcludeLambdaTester {
   interface MathOperation {
      int operation(int a, int b);
   }
   
   static class MinusOperation implements MathOperation {
      @Override
      public int operation(int a, int b) {
         return a - b;
      }
   }

   static int operate(MathOperation op, int a, int b) {
      return op.operation(a, b);
   }

   public static void main(String args[]){
      MathOperation addition = (int a, int b) -> a + b;
      MathOperation or = (int a, int b) -> a | b;
      
      int res = 0;
      int N = Integer.parseInt(args[0]);
      Random rand = new Random();
      MinusOperation minus = new MinusOperation();

      // UseBimorphism
      for (int i = 1; i <= N; ++i) {
         int a = rand.nextInt(i);
         int b = rand.nextInt(i);
         res ^= operate(i % 2 == 0 ? minus : addition, a, b);
      }

      // Counted Loop
      for (int i = 1; i <= 10000; ++i) {
         int a = rand.nextInt(i);
         int b = rand.nextInt(i);
         res ^= or.operation(a, b);
      }

      System.out.println(res);
   }
    
}
