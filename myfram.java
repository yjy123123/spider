import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class myfram extends JFrame implements ActionListener, MouseListener {
    JPanel showPane = new JPanel();//可滚动
    JButton urlButton = new JButton("URL查询");
    JButton docButton = new JButton("文本查询");
    JButton closeButton = new JButton("退出");
    JButton start =new JButton("查询");
    JButton closeall =new JButton("关闭全部");
    DefaultListModel dlm = new DefaultListModel();
    JList   list=new JList();
    JScrollPane jsp = new JScrollPane(list);//可滚动
    JTextField find =new JTextField();
    HashMap<String ,spider> hashmap =new HashMap();
    int flag=0;
    public myfram() {
        this.setLayout(null);
        this.setSize(700, 522);
        showPane.setBackground(Color.WHITE);
        showPane.setBounds(200, 0, 500, 480);
        find.setBounds(198,480,448,18);
        jsp.setBounds(100,0,100,500);
        list.setSize(100,500);
        list.setBorder(BorderFactory.createTitledBorder("    结果     "));
        urlButton.setBounds(0,150,100,30);
        docButton.setBounds(0,200,100,30);
        closeButton.setBounds(0,250,100,30);
        start.setBounds(645,480,54,18);
        closeall.setBounds(0,300,100,30);
        list.addMouseListener(this);
        this.add(showPane);
        this.add(find);
        this.add(urlButton);
        this.add(docButton);
        this.add(closeButton);
        this.add(closeall);
        this.add(jsp);
        this.add(start);
        //add(closeButton);
        this.setVisible(true);
        //this.setFocusable(true);
        urlButton.addActionListener(this);
        docButton.addActionListener(this);
        closeButton.addActionListener(this);
        start.addActionListener(this);
        closeall.addActionListener(this);
        find.addActionListener(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==urlButton){
            String url=JOptionPane.showInputDialog("请输入网址");
            int index=list.getLastVisibleIndex();
            spider s =new spider(url,"网页"+(index+2+flag));
            sensitiveword w=new sensitiveword();
            try {
                w.putin();
                w.highlighting(s.showText);
            }catch (Exception ex) {
                ex.printStackTrace();
            }
            dlm.addElement("网页"+(index+2+flag));
            list.setModel(dlm);
            showPane.add(s.jsp);
            jsp.setVisible(true);
            Map <String,spider>map=hashmap;
            for(spider sp: map.values()){
                sp.jsp.setVisible(false);
            }
            hashmap.put("网页"+(index+2+flag),s);
        }else {

                if (e.getSource() == docButton) {
                    ArrayList <String> url=new ArrayList<>();
                    String path=JOptionPane.showInputDialog("请输入文本路径");
                    File file=new File(path);
                    File word=new File(""+file.getName()+"敏感词.txt");
                    try {
                        FileReader fr = new FileReader(file);
                        BufferedReader br = new BufferedReader(fr);
                        String str;
                        while (true) {
                            str = br.readLine();
                            if (str == null) {
                                break;
                            }
                            url.add(str);
                        }
                        fr.close();
                        br.close();

                for(String tmp:url) {
                    PrintStream ps=new PrintStream(new FileOutputStream(word.getPath(),true));//不覆盖
                    ps.println(tmp);
                    int index = list.getLastVisibleIndex();
                    spider s = new spider(tmp, "网页" + (index + 2+flag));
                    sensitiveword w = new sensitiveword();
                    try {
                        w.putin();
                        w.highlighting(s.showText,word);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    dlm.addElement("网页" + (index + 2+flag));
                    list.setModel(dlm);
                    showPane.add(s.jsp);
                    jsp.setVisible(true);
                    Map<String, spider> map = hashmap;
                    for (spider sp : map.values()) {
                        sp.jsp.setVisible(false);
                    }
                    hashmap.put("网页" + (index + 2+flag), s);
                }
                    }catch (Exception ex){
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(  this,"路径输入错误");
                    }

                } else {
                    if (e.getSource() == closeButton) {
                        int a = JOptionPane.showConfirmDialog(this, "您确认退出吗？", "确认", JOptionPane.YES_NO_OPTION);
                        if (a == JOptionPane.YES_NO_OPTION) {
                            Map<String, spider> map = hashmap;
                            for (spider s : map.values()) {
                                s.jsp.setVisible(false);
                                s.content.delete();
                            }
                            System.exit(0);
                        }
                    }else {
                            if (e.getSource() == closeall) {
                                dlm.clear();
                                list.setModel(dlm);
                                Map<String, spider> map = hashmap;
                                for (spider s : map.values()) {
                                    s.jsp.setVisible(false);
                                    s.content.delete();
                                }
                                hashmap.clear();
                            } else {
                                if (e.getSource() == start) {
                                    String str = (String) list.getSelectedValue();
                                    spider s = hashmap.get(str);
                                    new sensitiveword().highlighting(s.showText, find.getText());
                                }else {
                                    String str = (String) list.getSelectedValue();
                                    spider s = hashmap.get(str);
                                    new sensitiveword().highlighting(s.showText, find.getText());//第一次不能是乱输的数字
                                }
                            }
                        }
                    }
                }
            }
    public void mouseClicked(MouseEvent e){
        if(e.getClickCount() == 2&&!list.isSelectionEmpty()){
            int a=JOptionPane.showConfirmDialog(this,"您确认关闭该窗口吗？","确认",JOptionPane.YES_NO_OPTION);
            if(a==JOptionPane.YES_OPTION){
                int index = list.getSelectedIndex(); //获取选择项
                try {
                      String str=(String)list.getSelectedValue();
                      dlm.remove(index);
                      list.setModel(dlm);
                      hashmap.get(str).jsp.setVisible(false);
                      hashmap.get(str).content.delete();
                      hashmap.remove(str);
                      flag++;

                }catch (Exception ex){
                    ex.printStackTrace();
                }
                //对应用户收到消息退出，解除socket绑定
            }
        }else{
            if(e.getClickCount()==1&&!list.isSelectionEmpty()){
                Map <String,spider>map=hashmap;
                for(spider sp: map.values()){
                    sp.jsp.setVisible(false);
                }
                String str=(String)list.getSelectedValue();
                hashmap.get(str).jsp.setVisible(true);

            }
        }
    }

    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}

    public static void main(String[] args)
    {
        new myfram();
    }


}
