package com.example.mp1v20;

public class Question {
    private int image_id;
    private String op1;
    private String op2;
    private String op3;
    private String op4;
    private String answer;

    public Question(int img, String[] options, String ans) {
        image_id = img;
        op1 = options[0];
        op2 = options[1];
        op3 = options[2];
        op4 = options[3];
        answer = ans;
    }

    public int getImg() {
        return image_id;
    }

    public String getOp1() {
        return op1;
    }

    public String getOp2() {
        return op2;
    }

    public String getOp3() {
        return op3;
    }

    public String getOp4() {
        return op4;
    }

    public String getAnswer() {
        return answer;
    }
}
