import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.*;
import java.util.*;

public class main extends JPanel {
    static Scanner Cin=new Scanner(System.in);
    static int Weight[];
    static int Value[];
    static int Suffix[];
    static double PCR[]; //Price-Cost Ratio
    static int m,n;
    static int TotalValue=0;

    /*
     数据读入与处理
     */
    public static void ReadFile() {
        int Option;
        System.out.println("select data:");
        Option=Cin.nextInt();
        if (Option>10 || Option<0) {
            System.out.println("no this data");
        }
        String Data="data\\beibao"+String.valueOf(Option)+".in";
        System.out.println(Data);
        try {
            Scanner In=new Scanner(new FileReader(Data));
            m=In.nextInt();
            n=In.nextInt();
            Weight=new int[10010];
            Value=new int[10010];
            Suffix=new int[10010];
            PCR=new double[10010];
            for (int i=1;i<=n;i++) {
                Weight[i] = In.nextInt();
                Value[i] = In.nextInt();
                Suffix[i] = i;
                TotalValue+=Value[i];
                PCR[i] = (double) Value[i] / (double) Weight[i];
                System.out.printf("weight: %4d value: %4d cost performance: %4.3f\n", Weight[i], Value[i], PCR[i]);
            }
        } catch (IOException e) {
            System.out.println("no this file");
        }
    }

    /*
     int类型数据交换
     */
    public static void SwapInt(int Data[],int a,int b) {
        int temp=Data[a];
        Data[a]=Data[b];
        Data[b]=temp;
    }

    /*
     double类型数据交换
     */
    public static void SwapDouble(double Data[],int a,int b) {
        double temp=Data[a];
        Data[a]=Data[b];
        Data[b]=temp;
    }

    /*
     排序
     */
    public static void DataSort() {
        for (int i=1;i<=n-1;i++) {
            for (int j=i+1;j<=n;j++) {
                if (PCR[i]<PCR[j]) {
                    SwapDouble(PCR,i,j);
                    SwapInt(Weight,i,j);
                    SwapInt(Value,i,j);
                    SwapInt(Suffix,i,j);
                }
            }
        }
    }

    final int Space=20;

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D=(Graphics2D)g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        int Width=getWidth();
        int Height=getHeight();
        g2D.draw(new Line2D.Double(Space,Space,Space,Height-Space)); //绘制x轴
        g2D.draw(new Line2D.Double(Space,Height-Space,Width-Space,Height-Space)); //绘制y轴
        Font font=new Font("Microsoft YaHei UI",Font.PLAIN,10); //修改字体
        g2D.setFont(font);
        g2D.drawString("0",Space-10,Height-Space+10); //添加文字
        g2D.drawString("Weight",Width-Space-20,Height-Space+10);
        g2D.drawString("Value",Space-10,Space-5);
        double xAxis=(double)(Width-2*Space)/getMaxWeight();
        double yAxis=(double)(Height-2*Space)/getMaxValue();
        g2D.setPaint(Color.pink);
        for (int i=1;i<=n;i++) { //开始绘制点
            double x=Space+xAxis*Weight[i];
            double y=Height-Space-yAxis*Value[i];
            g2D.fill(new Ellipse2D.Double(x-2,y-2,4,4));
        }
    }

    /*
     获取最大重量
     */
    private int getMaxWeight() {
        int MaxW=-Integer.MAX_VALUE;
        for (int i=1;i<=n;i++) {
            if (Weight[i]>MaxW) MaxW=Weight[i];
        }
        return MaxW;
    }

    /*
     获取最大价值
     */
    private int getMaxValue() {
        int MaxV=-Integer.MAX_VALUE;
        for (int i=1;i<=n;i++) {
            if (Value[i]>MaxV) MaxV=Value[i];
        }
        return MaxV;
    }

    /*
     散点图绘制
     */
    public static void PlottingScatterPlots() {
        JFrame Frame=new JFrame();
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.add(new main());
        Frame.setSize(300,300);
        Frame.setLocation(200,200);
        Frame.setVisible(true);
    }

    static int Res;
    static int Vectors[];

    /*
     贪心算法
     */
    public static void Greedy() {
        Vectors=new int[10010];
        for (int i=1;i<=n;i++) Vectors[i]=0;
        int Size=m;
        int Ans=0;
        for (int i=1;i<=n;i++) {
            if (Size>Weight[i]) {
                Size-=Weight[i];
                Ans+=Value[i];
                Vectors[Suffix[i]]=1;
            } else {
                break;
            }
        }
        Res=Ans;
        System.out.println("answer:"+Ans);
        System.out.print("solution vector: {");
        for (int i=1;i<=n;i++) {
            if (i!=n) System.out.print(Vectors[i]+",");
            else System.out.println(Vectors[i]+"}");
        }
    }

    static int Path[];
    static int f[];

    public static void FindPath(int Size) {
        while (Size>0) {
            for (int i=1;i<=n;i++) {
                if (Path[Suffix[i]]==0) {
                    if (Size-Weight[i]>=0) {
                        if (f[Size-Weight[i]]+Value[i]==f[Size]) {
                            Path[Suffix[i]]=1;
                            Size-=Weight[i];
                            break;
                        }
                    }
                }
            }
            Size--;
        }
    }

    /*
     动态规划算法
     */
    public static void DP() {
        f=new int[10010];
        for (int i=1;i<=n;i++) {
            for (int j=m;j>=Weight[i];j--) {
                f[j]=Math.max(f[j],f[j-Weight[i]]+Value[i]); //优化后，仅使用一维
            }
        }
        Res=f[m];
        System.out.println("求得的解:"+f[m]);
        Path=new int[10010];
        for (int i=1;i<=n;i++) Path[i]=0;
        FindPath(m);
        System.out.print("解向量: {");
        for (int i=1;i<=n;i++) {
            if (i!=n) System.out.print(Path[i]+",");
            else System.out.println(Path[i]+"}");
        }
    }

    static int Ans=0;
    static int CW=0; //Current Weight
    static int CV=0; //Current Value
    static int Flag[];

    public static double Bound(int Index) {
        double RemainW=m-CW;
        double CurrentV=CV;
        while (Index<n && Weight[Index]<=RemainW) {
            RemainW-=Weight[Index];
            CurrentV+=Value[Index];
            Index++;
        }
        if (Index<=n) {
            CurrentV+=PCR[Index]*RemainW;
        }
        return CurrentV;
    }

    /*
     回溯算法
     */
    public static void BackTrack(int Index) {
        if (Index>n) {
            Ans=CV;
            return;
        }
        if (CW+Weight[Index]<=m) {
            CW+=Weight[Index];
            CV+=Value[Index];
            Flag[Suffix[Index]]=1;
            BackTrack(Index+1);
            CW-=Weight[Index];
            CV-=Value[Index];
        }
        if (Bound(Index+1)>Ans) {
            BackTrack(Index+1);
        }
    }

    static int ArcAns;

    /*
     文件保存
     */
    public static void WriteFile(int Ans, double RunTime, int AnsRoute[]) throws FileNotFoundException {
        PrintStream Cout=new PrintStream("res.txt");
        ArcAns=Ans;
        Cout.println("求得的解: "+Ans);
        Cout.println("运行时间: "+RunTime+"s");
        Cout.print("解向量: {");
        for (int i=1;i<=n;i++) {
            if (i!=n) Cout.print(AnsRoute[i]+",");
            else Cout.println(AnsRoute[i]+"}");
        }
        Cout.close();
    }

    public static void SelectSolution() {
        System.out.println("please select an algorithm2");
        System.out.println("1:贪心\t2:动态规划\t3:回溯");
        int Operation;
        Operation=Cin.nextInt();
        double RunTime=0.0;
        if (Operation==1) {
            long StartTime=System.nanoTime();
            Greedy();
            long EndTime=System.nanoTime();
            RunTime=(EndTime-StartTime)/1000000000.0;
            System.out.println("time: "+RunTime+"s");
            try {
                WriteFile(Res,RunTime,Vectors);
            } catch (IOException e) {

            }
        }
        if (Operation==2) {
            long StartTime=System.nanoTime();
            DP();
            long EndTime=System.nanoTime();
            RunTime=(EndTime-StartTime)/1000000000.0;
            System.out.println("运行时间: "+RunTime+"s");
            try {
                WriteFile(Res,RunTime,Path);
            } catch (IOException e) {

            }
        }
        if (Operation==3) {
            Flag=new int[10010];
            for (int i=1;i<=n;i++) Flag[i]=0;
            long StartTime=System.nanoTime();
            BackTrack(1);
            long EndTime=System.nanoTime();
            RunTime=(EndTime-StartTime)/1000000000.0;
            Res=Ans;
            System.out.print("解向量: {");
            for (int i=1;i<=n;i++) {
                if (i!=n) System.out.print(Flag[i]+",");
                else System.out.println(Flag[i]+"}");
            }
            System.out.println("求得的解: "+Ans);
            System.out.println("运行时间: "+RunTime+"s");
            try {
                WriteFile(Res,RunTime,Flag);
            } catch (IOException e) {

            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        ReadFile();
        PlottingScatterPlots();
        DataSort();
        SelectSolution();
        new Addition(ArcAns, TotalValue);
    }
}