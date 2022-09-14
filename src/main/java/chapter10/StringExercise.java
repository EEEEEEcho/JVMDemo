package chapter10;

import java.util.Arrays;

public class StringExercise {
    String str = new String("good");
//    String str = "good";
    char[] ch = {'t','e','s','t'};

    public void change(String str,char[] ch){
        str = "test ok";
        ch[0] = 'b';
    }

    public static void main(String[] args) {
        StringExercise stringExercise = new StringExercise();
        stringExercise.change(stringExercise.str, stringExercise.ch);
        System.out.println(stringExercise.str); //good
        System.out.println(Arrays.toString(stringExercise.ch)); //[b, e, s, t]

        String s1 = "noChange";
        strChange(s1);
        System.out.println(s1); //noChange

        Student student = new Student("xxxx",20);
        System.out.println(student.name);   //xxxx
        System.out.println(student.age);    //20

        changeStudent(student);

        System.out.println(student.name);   //echo
        System.out.println(student.age);    //18

        changeStudentName(student.name);

        System.out.println(student.name);   //echo
        System.out.println(student.age);    //18
    }

    public static void strChange(String str){
        str = "changed";
    }

    public static void changeStudent(Student student){
        student.name = "echo";
        student.age = 18;
    }

    public static void changeStudentName(String name){
        name = "ekko";
    }
}
class Student{
    String name;
    int age;

    public Student(String name,int age){
        this.name = name;
        this.age = age;
    }
}