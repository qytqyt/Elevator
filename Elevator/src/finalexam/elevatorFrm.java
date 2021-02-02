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
        for (int i = 0; i < 20; i++){  //楼层初始化
            labels[i] = new JLabel(String.valueOf(i + 1));
            labels[i].setBackground(Color.WHITE);
            labels[i].setOpaque(true);// 设置这个面板在窗体中可见
        }

        for (int i = 0; i < 20; i++) {
            queue[i][0] = new ArrayList<Integer>();     // 上升等待队列初始化
            queue[i][1] = new ArrayList<Integer>();     // 下降等待队列初始化
        }
        
        for (int i = 0; i < 20; i++) {   // 上升列中下拉组件初始化
            up[i] = new JComboBox();
            up[i].addItem("-");
            for(int k = i + 2; k <= 20; k++){ 
                up[i].addItem(String.valueOf(k));     //把20个下拉组件添加到标签组件中
            }
            final int t = i;
            up[i].addItemListener(new ItemListener() {     //添加下拉组件的监听
                @Override
                public void itemStateChanged(ItemEvent e) {
                	
                    if (ItemEvent.SELECTED == e.getStateChange() && !up[t].getSelectedItem().toString().equals("-")) {
                        queue[t][0].add(Integer.parseInt(up[t].getSelectedItem().toString())); 
                        labels[t].setBackground(Color.GREEN);
                        up[t].setSelectedIndex(0); // 把当前选择的数字放在第一位
                        System.out.println("第" + (t + 1) + "楼有乘客要去" + queue[t][0] + "楼\n"); 
                    }
                }
            });
        }

        for (int i = 0; i < 20; i++) {   // 下降列中下拉组件初始化
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
                        System.out.println("第" + (t + 1) + "楼有乘客要去" + queue[t][1] + "楼\n");
                    }
                }
            });
        }
        // 电梯初始化
        for (int i = 0; i < 20; i++){
            liftOne[i] = new JButton("电梯");
            liftOne[i].setOpaque(true);
            liftOne[i].setBackground(Color.WHITE);
        }
        liftOne[0].setBackground(Color.YELLOW);

        JFrame frame = new JFrame("电梯");
        GridLayout grid = new GridLayout(21, 4);
        Container c = new Container();      //创建一个容器。  
        c.setLayout(grid);      //把网格布局放入容器中

        JLabel lbl1 = new JLabel("楼层");
        JLabel lbl2 = new JLabel("电梯");
        JLabel lbl3 = new JLabel("上");
        JLabel lbl4 = new JLabel("下");
        Font font = new Font("微软雅黑", Font.BOLD, 26);
        lbl1.setFont(font);
        lbl2.setFont(font);
        lbl3.setFont(font);
        lbl4.setFont(font);
        c.add(lbl1); 
        c.add(lbl2);
        c.add(lbl3);
        c.add(lbl4);

        for (int i = 20; i > 0; i--){    // 把4个数组对象添加到容器中
            c.add(labels[i - 1]);
            c.add(liftOne[i - 1]);
            c.add(up[i - 1]);
            c.add(down[i - 1]);
        }
        
        frame.add(c);    //把容器添加到JFrame中
        frame.setSize(2000, 1500);
        frame.setVisible(true);

        one = new elevator(0, liftOne);   //初始化电梯

    }

    static class refreshFloorStatus extends Thread{    //继承Thread类，得到线程功能
        refreshFloorStatus(){   
            start();    // 构造函数中添加start()方法
        }
        @Override
        public void run(){
            while (true) {      // 无限循环设置没有人选择的到达楼层的颜色为白色
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

    static class elevatorManager extends Thread{    //继承Thread类，得到线程功能
        elevatorManager(){
            start();    // 构造函数中添加start()方法
        }

        public void adjust(int n, int i) throws InterruptedException {
            // 电梯位于当前楼层下方
            if (one.getCurrentFloor()< i){
                one.setCurrentState(1);
                one.addUp(i);
                one.setMaxUp(i);//设置它当前最高能上升到哪一层
                System.out.println("电梯开始上升\n");
                Thread.sleep(500);
                return;
            }
            // 电梯位于当前楼层上方
            if (one.getCurrentFloor()> i){
            	one.setCurrentState(-1);
            	one.addDown(i);
            	one.setMinDown(i);;//设置它当前最低能下降到哪一层
                System.out.println("电梯开始下降\n");
                Thread.sleep(500);
                return;
            }
            // 最电梯位于当前楼层
            if (one.getCurrentFloor() == i){
            	one.setCurrentState(1);
                System.out.println("电梯启动\n");
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
                    // 监测上升队列
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
                    // 监测下降队列
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