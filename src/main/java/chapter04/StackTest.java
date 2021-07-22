package chapter04;

public class StackTest {
    public void methodA(){
        int i = 10;
        int j = 20;
        methodB();
    }
    public void methodB(){
        int m = 30;
        int n = 40;
    }

    public static void main(String[] args) {
        StackTest st = new StackTest();
        st.methodA();
    }
}