package finalexam;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class elevator extends Thread{    //继承Thread类，得到线程功能
    private int currentState;//当前状态
    private int currentFloor;//当前楼层
    private int upToMax;//电梯上升时要去的最大楼层
    private int downToMin;//电梯下降时要去的最小楼层
    private Comparator<Integer> cmpUp = new Comparator<Integer>() {
        @Override
        public int compare(Integer f1, Integer f2) {
            return f1 - f2;
        }
    };
    private Comparator<Integer> cmpDown = new Comparator<Integer>() {
        @Override
        public int compare(Integer f1, Integer f2) {
            return f2 - f1;
        }
    };
    
    //线程队列   电梯上升与下降停止队列
    private Queue<Integer> upStopList = new PriorityQueue<Integer>(20, cmpUp);
    private Queue<Integer> downStopList = new PriorityQueue<Integer>(20, cmpDown);
    private JButton[] buttonList;//elevatorFrm中的按钮控件队列

    elevator(int state, JButton[] buttonList){// 完成初始化动作，给变量赋值
        upToMax = 0;
        downToMin = 19;
        currentState = state;
        currentFloor = 0;
        this.buttonList = buttonList; 
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }
    public void popUp() {
        upStopList.poll();
    }

    public void addUp(Integer pos){
        upStopList.add(pos);
    }

    public void popDown(Integer pos){
        downStopList.poll();
    }

    public void addDown(Integer pos){
        downStopList.add(pos);
    }

    public int upMax(){
    	return upToMax;
    	}

    public void setMaxUp(int maxUp){
    	this.upToMax = maxUp;
    	}

    public int downMin(){
    	return downToMin;
    	}

    public void setMinDown(int minDown){
    	this.downToMin = minDown;
    	}

    public void run() {
        while(true){
            // 电梯处于上升状态
            while (currentState == 1){
                boolean flag = false;
                for (int i = 1; i < 20; i++){
                    buttonList[i].setText("电梯正在上升");
                }
                // 下客
                if (!upStopList.isEmpty() && currentFloor  == upStopList.peek()) {
                    while (currentFloor  == upStopList.peek()) {
                        Integer a = upStopList.poll();//将upStopList的第一个元素弹出
                        System.out.println("电梯在第" + (currentFloor + 1) + "楼" + "下客\n");
                        if(upStopList.isEmpty())
                            break;
                    }
                    buttonList[currentFloor].setBackground(Color.GRAY);
                    flag = true;
                }
                // 载上当前需要上楼的乘客
                if (!elevatorFrm.queue[currentFloor][0].isEmpty()) {
                    for (int i = 0; i < elevatorFrm.queue[currentFloor][0].size(); i++) {
                        if ((int) elevatorFrm.queue[currentFloor][0].get(i) - 1 > upToMax) {
                            upToMax = (int) elevatorFrm.queue[currentFloor][0].get(i) - 1;
                        }
                        addUp((Integer) elevatorFrm.queue[currentFloor][0].get(i) - 1);
                        System.out.println("电梯在第" + (currentFloor + 1) + "楼载到要前往第" + elevatorFrm.queue[currentFloor][0].get(i)
                        + "楼的乘客\n");
                    }
                    buttonList[currentFloor].setBackground(Color.GRAY);
                    flag = true;
                }
                elevatorFrm.queue[currentFloor][0].clear();
                // 电梯走空 转换方向去载要下楼的乘客
                if (upStopList.isEmpty() && !elevatorFrm.queue[currentFloor][1].isEmpty()){
                    for (int i = 0; i < elevatorFrm.queue[currentFloor][1].size();i++){
                        if ((int)elevatorFrm.queue[currentFloor][1].get(i) - 1 < downToMin){
                            downToMin = (int)elevatorFrm.queue[currentFloor][1].get(i) - 1;
                        }
                        addDown((Integer) elevatorFrm.queue[currentFloor][1].get(i) - 1);
                        System.out.println("电梯在第" + (currentFloor + 1) + "楼载到要前往第" + elevatorFrm.queue[currentFloor][1].get(i)
                                + "楼的乘客\n");
                    }
                    if (!downStopList.isEmpty()){
                        elevatorFrm.queue[currentFloor][1].clear();
                        setCurrentState(-1);
                        flag = true;
                        System.out.println("电梯开始下降\n");
                        break;
                    }
                }
                
                if (flag){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    buttonList[currentFloor].setBackground(Color.YELLOW);
                }
                if (upStopList.isEmpty() || currentFloor == 19){
                    setCurrentState(0);
                    upToMax = 0;
                    downToMin = 19;
                    buttonList[currentFloor].setBackground(Color.YELLOW);
                    System.out.println("电梯停止\n");
                    break;
                }
                buttonList[currentFloor].setBackground(Color.WHITE);
                currentFloor++;
                buttonList[currentFloor].setBackground(Color.YELLOW);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 电梯处于下降状态
            while(currentState == -1){
                boolean flag = false;
                for (int i = 1; i < 20; i++){
                    buttonList[i].setText("电梯正在下降");
                }
                // 下客
                if (!downStopList.isEmpty() && currentFloor  == downStopList.peek()) {
                    while (currentFloor  == downStopList.peek()) {
                        Integer a = downStopList.poll();
                        System.out.println("电梯在第" + (currentFloor + 1) + "楼" + "下客\n");
                        if(downStopList.isEmpty())
                            break;
                    }
                    buttonList[currentFloor].setBackground(Color.GRAY);
                    flag = true;
                }
                // 载上当前需要下楼的乘客
                if (!elevatorFrm.queue[currentFloor][1].isEmpty()) {
                    for (int i = 0; i < elevatorFrm.queue[currentFloor][1].size(); i++) {
                        if ((int) elevatorFrm.queue[currentFloor][1].get(i) - 1 < downToMin) {
                            downToMin = (int) elevatorFrm.queue[currentFloor][1].get(i) - 1;
                        }
                        addDown((Integer) elevatorFrm.queue[currentFloor][1].get(i) - 1);
                        System.out.println("电梯在第" + (currentFloor + 1) + "楼载到要前往第" + elevatorFrm.queue[currentFloor][1].get(i)
                                + "楼的乘客\n");
                    }
                    buttonList[currentFloor].setBackground(Color.GRAY);
                    flag = true;
                }
                elevatorFrm.queue[currentFloor][1].clear();
                // 电梯走空 转换方向去载要上楼的乘客
                if (downStopList.isEmpty() && !elevatorFrm.queue[currentFloor][0].isEmpty()){
                    for (int i = 0; i < elevatorFrm.queue[currentFloor][0].size();i++){
                        if ((int)elevatorFrm.queue[currentFloor][0].get(i) - 1 > upToMax){
                            upToMax = (int)elevatorFrm.queue[currentFloor][0].get(i) - 1;
                        }
                        addUp((Integer) elevatorFrm.queue[currentFloor][0].get(i) - 1);
                        System.out.println("电梯在第" + (currentFloor + 1) + "楼载到要前往第" + elevatorFrm.queue[currentFloor][0].get(i)
                                + "楼的乘客\n");
                    }
                    if (!upStopList.isEmpty()){
                        elevatorFrm.queue[currentFloor][0].clear();
                        setCurrentState(1);
                        flag = true;
                        System.out.println("电梯开始上升\n");
                        break;
                    }
                }
                if (flag){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    buttonList[currentFloor].setBackground(Color.YELLOW);
                }
                // 电梯走空或到达底层，停止在当前楼层
                if (downStopList.isEmpty() || currentFloor == 0){
                    buttonList[currentFloor].setBackground(Color.YELLOW);
                    setCurrentState(0);
                    upToMax = 0;
                    downToMin = 19;
                    System.out.println("电梯停止\n");
                    break;
                }
                buttonList[currentFloor].setBackground(Color.WHITE);
                currentFloor--;
                buttonList[currentFloor].setBackground(Color.YELLOW);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 电梯处于停滞状态
            while(currentState == 0){
                for (int i = 1; i < 20; i++){
                    buttonList[i].setText("---------------------------");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}