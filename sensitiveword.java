import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class sensitiveword {
    File file = new File("/敏感词库大全.txt");
    ArrayList<String> al = new ArrayList();
    public void putin() throws Exception {//加载敏感词哭
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String str;
        while (true) {
            str = br.readLine();
            if (str == null) {
                break;
            }
            al.add(str);
        }
        fr.close();
        br.close();
    }

    public void highlighting(JTextArea showText) {
        Highlighter highLighter = showText.getHighlighter();//获取负责进行高亮显示的对象。
        String text = showText.getText();
        DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
        for (String keyWord : al) {
            int pos = 0;
            while ((pos = text.indexOf(keyWord, pos)) >= 0) { //出现的位置
                try {
                    highLighter.addHighlight(pos, pos + keyWord.length(), p);//如果是敏感词着色
                    pos += keyWord.length();
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void highlighting(JTextArea showText, String str) {
        Highlighter highLighter = showText.getHighlighter(); //让showtext变色，获取负责进行高亮显示的对象。
        String text = showText.getText();
        DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
        String sensitiveword = str;
        highLighter.removeAllHighlights();//消除之前的颜色
        int pos = 0;
        while ((pos = text.indexOf(sensitiveword, pos)) >= 0) {
            try {
                highLighter.addHighlight(pos, pos + sensitiveword.length(), p);//如果是敏感词着色
                pos += sensitiveword.length();
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    public void highlighting(JTextArea showText, File file) throws Exception{
        Highlighter highLighter = showText.getHighlighter(); //让showtext变色，获取负责进行高亮显示的对象。
        String text = showText.getText();
        DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
        for (String keyWord : al) {
            int pos = 0;
            int num = 0;
            while ((pos = text.indexOf(keyWord, pos)) >= 0) { //出现的位置
                try {
                    highLighter.addHighlight(pos, pos + keyWord.length(), p);//如果是敏感词着色
                    pos += keyWord.length();
                    num++;
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
            if (num > 0) {
                PrintStream ps=new PrintStream(new FileOutputStream(file.getPath(),true));//不覆盖
                ps.println(keyWord+"\t"+num+"次");
                ps.close();
            }
        }
    }
}


