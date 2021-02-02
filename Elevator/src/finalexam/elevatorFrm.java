package finalexam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;

public class elevatorFrm {
    public static JLabel[] labels = new JLabel[20];
    public static JButton[] liftOne = new JButton[20];
    public static JComboBox[] up = new JComboBox[20];
    public static JComboBox[] down = new JComboBox[20];

    public static elevator one;
    
    public static ArrayList[][] queue = new ArrayList[20][2];
    
    public static void init() {
        for (int i = 0; i < 20; i++){  //¥���ʼ��
            labels[i] = new JLabel(String.valueOf(i + 1));
            labels[i].setBackground(Color.WHITE);
            labels[i].setOpaque(true);// �����������ڴ����пɼ�
        }

        for (int i = 0; i < 20; i++) {
            queue[i][0] = new ArrayList<Integer>();     // �����ȴ����г�ʼ��
            queue[i][1] = new ArrayList<Integer>();     // �½��ȴ����г�ʼ��
        }
        
        for (int i = 0; i < 20; i++) {   // �����������������ʼ��
            up[i] = new JComboBox();
            up[i].addItem("-");
            for(int k = i + 2; k <= 20; k++){ 
                up[i].addItem(String.valueOf(k));     //��20�����������ӵ���ǩ�����
            }
            final int t = i;
            up[i].addItemListener(new ItemListener() {     //�����������ļ���
                @Override
                public void itemStateChanged(ItemEvent e) {
                	
                    if (ItemEvent.SELECTED == e.getStateChange() && !up[t].getSelectedItem().toString().equals("-")) {
                        queue[t][0].add(Integer.parseInt(up[t].getSelectedItem().toString())); 
                        labels[t].setBackground(Color.GREEN);
                        up[t].setSelectedIndex(0); // �ѵ�ǰѡ������ַ��ڵ�һλ
                        System.out.println("��" + (t + 1) + "¥�г˿�Ҫȥ" + queue[t][0] + "¥\n"); 
                    }
                }
            });
        }

        for (int i = 0; i < 20; i++) {   // �½��������������ʼ��
            down[i] = new JComboBox();
            down[i].addItem("-");
            for(int k = i; k > 0; k--){
                down[i].addItem(String.valueOf(k));
            }
            final int t = i;
            down[i].addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (ItemEvent.SELECTED == e.getStateChange() && !down[t].getSelectedItem().toString().equals("-")) {
                        queue[t][1].add(Integer.parseInt(down[t].getSelectedItem().toString()));
                        labels[t].setBackground(Color.GREEN);
                        down[t].setSelectedIndex(0);
                        System.out.println("��" + (t + 1) + "¥�г˿�Ҫȥ" + queue[t][1] + "¥\n");
                    }
                }
            });
        }
        // ���ݳ�ʼ��
        for (int i = 0; i < 20; i++){
            liftOne[i] = new JButton("����");
            liftOne[i].setOpaque(true);
            liftOne[i].setBackground(Color.WHITE);
        }
        liftOne[0].setBackground(Color.YELLOW);

        JFrame frame = new JFrame("����");
        GridLayout grid = new GridLayout(21, 4);
        Container c = new Container();      //����һ��������  
        c.setLayout(grid);      //�����񲼾ַ���������

        JLabel lbl1 = new JLabel("¥��");
        JLabel lbl2 = new JLabel("����");
        JLabel lbl3 = new JLabel("��");
        JLabel lbl4 = new JLabel("��");
        Font font = new Font("΢���ź�", Font.BOLD, 26);
        lbl1.setFont(font);
        lbl2.setFont(font);
        lbl3.setFont(font);
        lbl4.setFont(font);
        c.add(lbl1); 
        c.add(lbl2);
        c.add(lbl3);
        c.add(lbl4);

        for (int i = 20; i > 0; i--){    // ��4�����������ӵ�������
            c.add(labels[i - 1]);
            c.add(liftOne[i - 1]);
            c.add(up[i - 1]);
            c.add(down[i - 1]);
        }
        
        frame.add(c);    //��������ӵ�JFrame��
        frame.setSize(2000, 1500);
        frame.setVisible(true);

        one = new elevator(0, liftOne);   //��ʼ������

    }

    static class refreshFloorStatus extends Thread{    //�̳�Thread�࣬�õ��̹߳���
        refreshFloorStatus(){   
            start();    // ���캯�������start()����
        }
        @Override
        public void run(){
            while (true) {      // ����ѭ������û����ѡ��ĵ���¥�����ɫΪ��ɫ
                for (int i = 0; i < 20; i++) {
                    if (queue[i][0].isEmpty() && queue[i][1].isEmpty()) {
                        labels[i].setBackground(Color.WHITE); 
                    }
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class elevatorManager extends Thread{    //�̳�Thread�࣬�õ��̹߳���
        elevatorManager(){
            start();    // ���캯�������start()����
        }

        public void adjust(int n, int i) throws InterruptedException {
            // ����λ�ڵ�ǰ¥���·�
            if (one.getCurrentFloor()< i){
                one.setCurrentState(1);
                one.addUp(i);
                one.setMaxUp(i);//��������ǰ�������������һ��
                System.out.println("���ݿ�ʼ����\n");
                Thread.sleep(500);
                return;
            }
            // ����λ�ڵ�ǰ¥���Ϸ�
            if (one.getCurrentFloor()> i){
            	one.setCurrentState(-1);
            	one.addDown(i);
            	one.setMinDown(i);;//��������ǰ������½�����һ��
                System.out.println("���ݿ�ʼ�½�\n");
                Thread.sleep(500);
                return;
            }
            // �����λ�ڵ�ǰ¥��
            if (one.getCurrentFloor() == i){
            	one.setCurrentState(1);
                System.out.println("��������\n");
                Thread.sleep(500);
                return;
            }
        }

        public void run(){
            while (true){
                for (int i = 0; i < 20; i++){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // �����������
                    if (!queue[i][0].isEmpty()){
                        int index = -1,  distance = 99999;
                            if (one.getCurrentState() == 0 && !queue[i][0].isEmpty()){
                                if (Math.abs(one.getCurrentFloor() - i) < distance){
                                    index = 0;
                                    distance = Math.abs(one.getCurrentFloor() - i);
                                }
                            }
                            if (one.getCurrentFloor() >= i && one.getCurrentState() == -1
                                    && one.downMin() >= i){
                                if (Math.abs(one.getCurrentFloor() - i) < distance){
                                    index = -1;
                                    distance = Math.abs(one.getCurrentFloor() - i);
                                }
                            }
                            if (one.getCurrentFloor() <= i && one.getCurrentState() == 1
                                    && one.upMax() >= i){
                                if (Math.abs(one.getCurrentFloor() - i) < distance){
                                    index = -1;
                                    distance = Math.abs(one.getCurrentFloor() - i);
                                }
                            }
                        if (index != -1 && !queue[i][0].isEmpty()){
                            try {
                                adjust(index, i);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                for (int i = 0; i < 20; i++){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // ����½�����
                    if (!queue[i][1].isEmpty()){
                        int index = -1,  distance = 99999;
                            if (one.getCurrentState() == 0 && !queue[i][1].isEmpty()){
                                if (Math.abs(one.getCurrentFloor() - i) < distance){
                                    index = 0;
                                    distance = Math.abs(one.getCurrentFloor() - i);
                                }
                            }
                            if (one.getCurrentFloor() >= i && one.getCurrentState() == -1
                                    && one.downMin() <= i){
                                if (Math.abs(one.getCurrentFloor() - i) < distance){
                                    index = -1;
                                    distance = Math.abs(one.getCurrentFloor() - i);
                                }
                            }
                            if (one.getCurrentFloor() <= i && one.getCurrentState() == 1
                                    && one.upMax() <= i){
                                if (Math.abs(one.getCurrentFloor() - i) < distance){
                                    index = -1;
                                    distance = Math.abs(one.getCurrentFloor() - i);
                                }
                            }
                        if (index != -1 && !queue[i][1].isEmpty()){
                                try {
									adjust(index, i);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args){
        init();
        refreshFloorStatus refreshfloorstatus = new refreshFloorStatus();
        elevatorManager elevatormanager = new elevatorManager();
        one.start();
    }
}