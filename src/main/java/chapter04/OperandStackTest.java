package chapter04;

public class OperandStackTest {
    public void testAddOperation(){
        //boolean,byte,short,char,int 都以int型来保存
        byte i = 15;
        int j = 8;
        int k = i + j;
    }

    public int getSum(){
        int m = 10;
        int n = 20;
        int k = m + n;
        return k;
    }
    public void testGetSum(){
        //获取上一个栈帧返回的结果，并保存在操作数栈中
        int i = getSum();
        int j = 10;
    }
}
