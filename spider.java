import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;


public class spider {
        public JTextArea showText=new JTextArea();
        public JScrollPane jsp =new JScrollPane(showText);
        public File content;
        public String url;
        public String filename;
        public spider(String url,String filename){
                jsp.setSize(500,480);
                showText.setLineWrap(true);
                showText.setWrapStyleWord(true);
                showText.setEditable(false);
               /* if(!(url.startsWith("https://www.")&&url.startsWith("https://"))){
                        url="https://www."+url;
                }
                else{
                        if(!url.startsWith("https://")){
                                url="https://"+url;
                        }
                }*/
                this.url=url;
                this.filename=filename;
                try {
                        gethtml();
                        analyze();
                }catch (Exception ex){
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null,"输入网址错误");
                        showText.setFont(new Font("斜体", Font.BOLD,100));
                        showText.append("\n"+"\n"+"ERROR！网址错误");
                }
        }
public void gethtml() { //抓取网页并保存
        try {
                URL url1 = new URL(url);
                URLConnection uc = url1.openConnection();
                uc.addRequestProperty("User-Agent", "Mozilla/5.0 (iPad; U; CPU OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5");
                //有些网页防爬，加头部来爬所有网页
                content = new File("/网页内容"+filename+".html");//多个网页时爬出结果很多
                content.createNewFile();
                InputStream is = url1.openStream();//从 URL 连接读入的输入流，字节输入流
                InputStreamReader isr = new InputStreamReader(is); //字节输入流，到字符
                BufferedReader br = new BufferedReader(isr);
                String str;
                str = br.readLine();//!(str.equals(null))
                while (str != null) {
                        filewriter(content, str);//读取网页中的内容后放入content
                        str = br.readLine();
                }
                isr.close();
                br.close();
           }catch (Exception ex){
                JOptionPane.showMessageDialog(null,"输入网址错误");
                //showText.append("网址错误");
                showText.append("\n"+"\n"+"ERROR！网址错误");
        }
        }

        public void analyze() {
                try {
                        Document doc = Jsoup.parse(content, "Utf-8");//将HTML解析成文档，不引入javax.swing，import org.jsoup.nodes.Document;
                       doc.getElementById("content");//返回对拥有指定 ID 的第一个对象的引用。查找特定元素
                        //分离出html下<a>...</a>之间的所有东西
                        // System.out.println(doc.text());
                        showText.append(doc.text());
                }catch (Exception ex){
                       // JOptionPane.showMessageDialog(null,"输入网址错误");
                       // showText.append("网址错误");
                }

        }
        public void filewriter(File file,String str) throws Exception{
                PrintStream psf =new PrintStream(new FileOutputStream(file,true));//写html
                psf.println(str);
                psf.close();
        }
        /*public static void main(String args[]){

                spider s=new spider("");
                sensitiveword w=new sensitiveword();
                try {
                        w.putin();
                        w.highlighting(s.showText);
                }catch (Exception ex){
                         ex.printStackTrace();
                }
        }*/
        }

