package chapter06;

public class EscapeAnalysis {
    public EscapeAnalysis obj;

    /**
     * 快速判断是否发生逃逸：
     * 检查new出来的对象实体是否有可能在方法外部被调用
     */

    //方法返回了EscapeAnalysis对象，发生了逃逸
    public EscapeAnalysis getInstance(){
        return obj == null ? new EscapeAnalysis() : obj;
    }

    //为成员属性赋值，发生了逃逸
    public void setObj() {
        this.obj = new EscapeAnalysis();
        //如果当前obj的声明为static，仍然会发生逃逸
    }

    //虽然new出来了一个对象，但是该对象的作用域，仅在当前方法中有效，没有发生逃逸
    public void useEscapeAnalysis(){
        EscapeAnalysis e = new EscapeAnalysis();
    }

    //引用成员变量的值，发生逃逸，因为成员变量对象的实体在这里被引用了
    public void useEscapeAnalysis1(){
        EscapeAnalysis e = getInstance();
    }
}
