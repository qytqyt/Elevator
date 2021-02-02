package finalexam;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class elevator extends Thread{    //�̳�Thread�࣬�õ��̹߳���
    private int currentState;//��ǰ״̬
    private int currentFloor;//��ǰ¥��
    private int upToMax;//��������ʱҪȥ�����¥��
    private int downToMin;//�����½�ʱҪȥ����С¥��
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
    
    //�̶߳���   �����������½�ֹͣ����
    private Queue<Integer> upStopList = new PriorityQueue<Integer>(20, cmpUp);
    private Queue<Integer> downStopList = new PriorityQueue<Integer>(20, cmpDown);
    private JButton[] buttonList;//elevatorFrm�еİ�ť�ؼ�����

    elevator(int state, JButton[] buttonList){// ��ɳ�ʼ����������������ֵ
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
            // ���ݴ�������״̬
            while (currentState == 1){
                boolean flag = false;
                for (int i = 1; i < 20; i++){
                    buttonList[i].setText("������������");
                }
                // �¿�
                if (!upStopList.isEmpty() && currentFloor  == upStopList.peek()) {
                    while (currentFloor  == upStopList.peek()) {
                        Integer a = upStopList.poll();//��upStopList�ĵ�һ��Ԫ�ص���
                        System.out.println("�����ڵ�" + (currentFloor + 1) + "¥" + "�¿�\n");
                        if(upStopList.isEmpty())
                            break;
                    }
                    buttonList[currentFloor].setBackground(Color.GRAY);
                    flag = true;
                }
                // ���ϵ�ǰ��Ҫ��¥�ĳ˿�
                if (!elevatorFrm.queue[currentFloor][0].isEmpty()) {
                    for (int i = 0; i < elevatorFrm.queue[currentFloor][0].size(); i++) {
                        if ((int) elevatorFrm.queue[currentFloor][0].get(i) - 1 > upToMax) {
                            upToMax = (int) elevatorFrm.queue[currentFloor][0].get(i) - 1;
                        }
                        addUp((Integer) elevatorFrm.queue[currentFloor][0].get(i) - 1);
                        System.out.println("�����ڵ�" + (currentFloor + 1) + "¥�ص�Ҫǰ����" + elevatorFrm.queue[currentFloor][0].get(i)
                        + "¥�ĳ˿�\n");
                    }
                    buttonList[currentFloor].setBackground(Color.GRAY);
                    flag = true;
                }
                elevatorFrm.queue[currentFloor][0].clear();
                // �����߿� ת������ȥ��Ҫ��¥�ĳ˿�
                if (upStopList.isEmpty() && !elevatorFrm.queue[currentFloor][1].isEmpty()){
                    for (int i = 0; i < elevatorFrm.queue[currentFloor][1].size();i++){
                        if ((int)elevatorFrm.queue[currentFloor][1].get(i) - 1 < downToMin){
                            downToMin = (int)elevatorFrm.queue[currentFloor][1].get(i) - 1;
                        }
                        addDown((Integer) elevatorFrm.queue[currentFloor][1].get(i) - 1);
                        System.out.println("�����ڵ�" + (currentFloor + 1) + "¥�ص�Ҫǰ����" + elevatorFrm.queue[currentFloor][1].get(i)
                                + "¥�ĳ˿�\n");
                    }
                    if (!downStopList.isEmpty()){
                        elevatorFrm.queue[currentFloor][1].clear();
                        setCurrentState(-1);
                        flag = true;
                        System.out.println("���ݿ�ʼ�½�\n");
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
                    System.out.println("����ֹͣ\n");
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
            // ���ݴ����½�״̬
            while(currentState == -1){
                boolean flag = false;
                for (int i = 1; i < 20; i++){
                    buttonList[i].setText("���������½�");
                }
                // �¿�
                if (!downStopList.isEmpty() && currentFloor  == downStopList.peek()) {
                    while (currentFloor  == downStopList.peek()) {
                        Integer a = downStopList.poll();
                        System.out.println("�����ڵ�" + (currentFloor + 1) + "¥" + "�¿�\n");
                        if(downStopList.isEmpty())
                            break;
                    }
                    buttonList[currentFloor].setBackground(Color.GRAY);
                    flag = true;
                }
                // ���ϵ�ǰ��Ҫ��¥�ĳ˿�
                if (!elevatorFrm.queue[currentFloor][1].isEmpty()) {
                    for (int i = 0; i < elevatorFrm.queue[currentFloor][1].size(); i++) {
                        if ((int) elevatorFrm.queue[currentFloor][1].get(i) - 1 < downToMin) {
                            downToMin = (int) elevatorFrm.queue[currentFloor][1].get(i) - 1;
                        }
                        addDown((Integer) elevatorFrm.queue[currentFloor][1].get(i) - 1);
                        System.out.println("�����ڵ�" + (currentFloor + 1) + "¥�ص�Ҫǰ����" + elevatorFrm.queue[currentFloor][1].get(i)
                                + "¥�ĳ˿�\n");
                    }
                    buttonList[currentFloor].setBackground(Color.GRAY);
                    flag = true;
                }
                elevatorFrm.queue[currentFloor][1].clear();
                // �����߿� ת������ȥ��Ҫ��¥�ĳ˿�
                if (downStopList.isEmpty() && !elevatorFrm.queue[currentFloor][0].isEmpty()){
                    for (int i = 0; i < elevatorFrm.queue[currentFloor][0].size();i++){
                        if ((int)elevatorFrm.queue[currentFloor][0].get(i) - 1 > upToMax){
                            upToMax = (int)elevatorFrm.queue[currentFloor][0].get(i) - 1;
                        }
                        addUp((Integer) elevatorFrm.queue[currentFloor][0].get(i) - 1);
                        System.out.println("�����ڵ�" + (currentFloor + 1) + "¥�ص�Ҫǰ����" + elevatorFrm.queue[currentFloor][0].get(i)
                                + "¥�ĳ˿�\n");
                    }
                    if (!upStopList.isEmpty()){
                        elevatorFrm.queue[currentFloor][0].clear();
                        setCurrentState(1);
                        flag = true;
                        System.out.println("���ݿ�ʼ����\n");
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
                // �����߿ջ򵽴�ײ㣬ֹͣ�ڵ�ǰ¥��
                if (downStopList.isEmpty() || currentFloor == 0){
                    buttonList[currentFloor].setBackground(Color.YELLOW);
                    setCurrentState(0);
                    upToMax = 0;
                    downToMin = 19;
                    System.out.println("����ֹͣ\n");
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
            // ���ݴ���ͣ��״̬
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