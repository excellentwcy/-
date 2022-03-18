import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 扩展功能（扇形图绘制）
*/
public class Addition extends JFrame{
    static int ArcAns,TotalValue;
    class panel extends JPanel{
        protected void paintComponent(Graphics g){
            int CenterX,CenterY;
            int r;
            int Percent=(int)(360*((double)ArcAns/TotalValue)); //计算可容纳所占角度
            CenterX = this.getWidth();
            CenterY = this.getHeight();
            r = this.getWidth()-10;
            super.paintComponent(g);
            g.setColor(Color.pink);
            g.fillArc(5, 10, r, r, 0, Percent); //绘制可容纳部分
            g.setColor(Color.lightGray);
            g.fillArc(5,10,r,r,Percent,360-Percent); //绘制剩余部分
            Font font=new Font("Microsoft YaHei UI",Font.PLAIN,10); //修改字体
            g.setFont(font);
            g.setColor(Color.black);
            g.drawString("粉色：背包可容纳价值",5,300);
            g.drawString("灰色：物品剩余价值",150,300);
        }
    }

    public Addition(int Ans,int Value){
        panel p = new panel();
        ArcAns=Ans;
        TotalValue=Value;
        this.add(p);
        this.setLocationRelativeTo(null);
        this.setSize(300,350);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

    }
}
