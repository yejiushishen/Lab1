//import java.util.Scanner;
import java.io.*;
import java.util.Random;//孙旭岩添加
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.*;
//import java.awt.event.*;
//import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.ImageIcon;
//import javax.swing.JButton; 
import javax.swing.JPanel; 
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent; 
import javax.swing.JFileChooser; 
import javax.swing.JFrame;
import javax.swing.JLabel; 
class  GraphViz{
    private String runPath = "";
    private String dotPath = ""; 
    private String runOrder="";
    private String dotCodeFile="dotcode.txt";
    private String resultGif="dotGif";
    private StringBuilder graph = new StringBuilder();

    Runtime runtime=Runtime.getRuntime();

    public void run() {
        File file=new File(runPath);
        file.mkdirs();
        writeGraphToFile(graph.toString(), runPath);
        creatOrder();
        try {
            runtime.exec(runOrder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void creatOrder(){
        runOrder+=dotPath+" ";
        runOrder+=runPath;
        runOrder+="\\"+dotCodeFile+" ";
        runOrder+="-T jpg ";
        runOrder+="-o ";
        runOrder+=runPath;
        runOrder+="\\"+resultGif+".jpg";
        System.out.println(runOrder);
    }

    public void writeGraphToFile(String dotcode, String filename) {
        try {
            File file = new File(filename+"\\"+dotCodeFile);
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(dotcode.getBytes());
            fos.close();
        } catch (java.io.IOException ioe) { 
            ioe.printStackTrace();
        }
     }  

    public GraphViz(String runPath,String dotPath,String nameGraph) {
        this.runPath=runPath;
        this.dotPath=dotPath;
		this.resultGif=nameGraph;
    }

    public void add(String line) {
        graph.append("\t"+line);
    }

    public void addln(String line) {
        graph.append("\t"+line + "\n");
    }

    public void addln() {
        graph.append('\n');
    }

    public void start_graph() {
        graph.append("digraph G {\n") ;
    }

    public void end_graph() {
        graph.append("}") ;
    }   
}

class graph {//图的结构
	static final int M=1000;
	int [][]link=new int[M][M];
	String []point=new String[M];
    int point_num;
    graph() {
    	int i,j;
    	this.point_num=0;
    	for(i=0;i<M;i++)
    	{
    		this.point[i]=null;
    		for(j=0;j<M;j++)
    		{
    			this.link[i][j]=0;
    		}
    	}
    }
    int Search(String ipt)
    {
    	int i;
    	for(i=0;i<this.point_num;i++)
    	{
    		if(this.point[i].equals(ipt))
    		   return i;
    	}
    	return -1;
    }
    int[] Show(String A)
    {
    	int[] ans=new int[M];
    	int i,posA,posAns=0;
    	for(i=0;i<this.point_num;i++)
    	{
    		ans[i]=-1;
    	}
    	posA=this.Search(A);
    	if(posA==-1)
    	{
    		return ans;
    	}
    	for(i=0;i<this.point_num;i++)
    	{
    		if(this.link[posA][i]>0) 
    		{
    			ans[posAns]=i;
    			posAns++;
    		}
    	}
    	return ans;
    }
}
class Directed_graph {//图的功能
	static final int N=1000;
	static graph G;
	static int random_pos;
	static int time=0;
	static String out="";
	static int[][]visted=new int[N][N];
	public static void CreateGraph(String path) throws IOException//生成有向图的函数
	{	
		int flag,point;
		char []temp_chars=new char[1000];
		FileReader fr=new FileReader(path);
		int num=fr.read(temp_chars);
		String temp_str=new String(temp_chars,0,num);
		fr.close();
		String []words=new String[1000];
		temp_str=temp_str.toLowerCase();
		words=temp_str.split("[^a-z]+");
		G = new graph();
		G.point_num=0;
		point=0;
		for (int i=0;i<words.length;i++)
		{
			flag=0;
			for (int j=0;j<G.point_num;j++)
			{
				if (G.point[j].equals(words[i])==true)
				{
					if (point!=j)
					{
						G.link[point][j]++;
						point=j;
					}
					flag=1;
					break;
				}
			}
			if (flag==0)
			{
				G.point[G.point_num]=new String(words[i]);
				if (i!=0)
				{
					G.link[point][G.point_num]++;
					point=G.point_num;
    			}
				G.point_num++;
			}
		}
	}
	public static void showDirectedGraph() {//展示图的函数主体
    	StringBuffer Order=new StringBuffer("A->t");
    	String Order_r;
    	int i,j;
    	GraphViz gViz=new GraphViz("E:\\JavaCode\\MainWindow\\src","D:\\New Folder\\bin\\dot.exe","graph");
        gViz.start_graph();
        for(i=0;i<G.point_num;i++)
        {
        	for(j=0;j<G.point_num;j++)
        	{
        		if(G.link[i][j]!=0)
        		{
        		Order.delete(0,Order.length());
        		Order.append(G.point[i]);
        		Order.append("->");
        		Order.append(G.point[j]);
        		Order_r=Order.toString();
        		gViz.addln(Order_r);
        		}
        	}
        }
        gViz.end_graph();
        try {
            gViz.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	public static String[] Get_bridege_words(String word1, String word2) {//获取桥接词的关键函数
	    int[]words_temp_pos;
	    int i,j,temp_num=0;
	    String []words_out=new String[1000];
		words_temp_pos=G.Show(word1);
		for(i=0;words_temp_pos[i]!=-1;i++)
		{
			for(j=0;G.Show(G.point[words_temp_pos[i]])[j]!=-1;j++)
			{
				if((G.point[
				           G.Show(
				        		   G.point[words_temp_pos[i]]
				        		 )[j]
				        ]).equals(word2)) {
					words_out[temp_num]=G.point[words_temp_pos[i]];
					temp_num++;
				}
			}
		}
		return words_out;
	}
	public static String queryBridgeWords(String word1, String word2)//获取桥接词的函数主体
	{
		String temp[];
		String str_answer,str_result;
		StringBuffer result;
    	int flag1,flag2,answer,len;
    	flag1=0;
    	flag2=0;
    	for (int i=0;i<G.point_num;i++)
    	{
    		if (G.point[i].equals(word1)==true)
    			flag1=1;
    		if (G.point[i].equals(word2)==true)
    			flag2=1;
    	}
    	temp=Get_bridege_words(word1,word2);
    	for (len=0;;len++)
    	{
    		if (temp[len]==null)
    			break;
    	}
    	if (flag1==0&&flag2==0)
    		answer=1;//两个词均不存在
    	else if (flag1==1&&flag2==0)
    		answer=2;//第一个词存在，第二个词不存在
    	else if (flag1==0&&flag2==1)
    		answer=3;//第一个词不存在，第二个词存在
    	else
    	{
    		if (len==0)
    			answer=4;//没有桥接词
    		else
    			answer=5;//有桥接词
    	}
		str_answer=String.valueOf(answer);
		result=new StringBuffer();
		result.append(str_answer);
		if (answer==5)
		{
			for (int i=0;i<len;i++)
			{
				result.append(" ");
				result.append(temp[i]);
			}
		}
		str_result=result.toString();
		return str_result;
	}
	public static String outPut2(String temp2,String word1,String word2)//格式化输出桥接词
	{
		String []words=new String[1000];
		String result;
		StringBuffer temp;
		temp=new StringBuffer();
		int answer;
		words=temp2.split("[^a-z0-9]+");
		answer=Integer.parseInt(words[0]);
		if (answer==1)
			temp.append("No “"+word1+"” and “"+word2+"” in the graph!");
		else if (answer==2)
			temp.append("No “"+word2+"” in the graph!");
		else if (answer==3)
			temp.append("No “"+word1+"” in the graph!");
		else if (answer==4)
			temp.append("No bridge words from “"+word1+"” to ”"+word2+"”!");
		else
		{
			temp.append("The bridge words from “"+word1+"” to ”"+word2+"”is:");
			for (int i=1;i<words.length;i++)
				temp.append(words[i]+" ");
		}
		result=temp.toString();
		return result;
	}
	public static String generateNewText(String word1) {//生成新文本的函数
    	int word_num;
    	int i,random_position,real_string_array_length;
    	Random random = new Random();
    	String word_out=new String();
    	String []words=new String[1000];
    	String []bridge_words=new String[1000];
    	word1=word1.toLowerCase();
		words=word1.split("[^a-z]+");
		word_num=words.length;
		if(word_num==1)
		{
			word_out+=words[0];
		}
		else
		{
			for(i=0;i<word_num-1;i++)
			{
				word_out+=words[i];
				word_out+=" ";
				if(G.Search(words[i])!=-1)
				{
					if(G.Search(words[i+1])!=-1)
					{
						bridge_words=Get_bridege_words(words[i],words[i+1]);
						real_string_array_length=Get_real_string_array_length(bridge_words);
						if(real_string_array_length!=0)
						{
						if(Get_real_string_array_length(bridge_words)==1)
						{
							random_position=0;
						}
						else
						{
						random_position=random.nextInt(real_string_array_length-1);
						}
						word_out+=bridge_words[random_position];
						word_out+=" ";
						}
					}
				}
			}
			word_out+=words[i];
			word_out+=" ";
		}
		return word_out;
	}
	
	public static String calcShortestPath(String word1, String word2)//计算最短路径的函数
    {
		int [][]matrix=new int[G.point_num][G.point_num];
    	int [][]temp=new int[G.point_num][G.point_num];
    	int int_word1,int_word2,answer;
    	StringBuffer result;
    	String str_answer,str_result;
    	int_word1=-1;
    	int_word2=-1;
    	for (int i=0;i<G.point_num;i++)
    	{
    		if (G.point[i].equals(word1)==true)
    			int_word1=i;
    		if (G.point[i].equals(word2)==true)
    			int_word2=i;
    	}
    	
    	if (int_word1==-1&&int_word2==-1)
    		answer=1;
    	else if(int_word1!=-1&&int_word2==-1)
    		answer=2;
    	else if ((int_word1==-1&&int_word2!=-1))
    		answer=3;
    	else
    	{
	    	if (G.link[int_word1][int_word2]!=0)
	    		answer=4;
	    	else
	    	{
	    		answer=5;
	    		for (int i=0;i<G.point_num;i++)
		    	{
		    		for (int j=0;j<G.point_num;j++)
		    		{
		    			if (G.link[i][j]!=0)
		    			{
		    				
		    				matrix[i][j]=G.link[i][j];
		    			}
		    			else
		    				matrix[i][j]=1000;
		    			if (i==j)
		    				matrix[i][j]=0;
		    			temp[i][j]=j;
		    		}
		    	}
	    		for (int i=0;i<G.point_num;i++)
	        	{
	        		System.out.print(G.point[i]);
	        		for (int j=0;j<G.point_num;j++)
	        		{
	        			System.out.print(" "+temp[i][j]);
	        		}
	        		System.out.println();
	        	}
		    	for (int k=0;k<G.point_num;k++)
		    	{
		    		for (int i=0;i<G.point_num;i++)
		    		{
		    			for (int j=0;j<G.point_num;j++)
		    			{
		    				if (matrix[i][k]!=1000&&matrix[k][j]!=1000&&matrix[i][k]+matrix[k][j]<matrix[i][j])
		    				{
		    					matrix[i][j]=matrix[i][k]+matrix[k][j];
		    					temp[i][j]=temp[i][k];
		    				}
		    			}
		    		}
		    	}
	    	}
    	}
    	for (int i=0;i<G.point_num;i++)
    	{
    		System.out.print(G.point[i]);
    		for (int j=0;j<G.point_num;j++)
    		{
    			System.out.print(" "+temp[i][j]);
    		}
    		System.out.println();
    	}
    	str_answer=String.valueOf(answer);
		result=new StringBuffer();
		result.append(str_answer);
		if (answer==5)
		{
			int i=int_word1;
			int j=int_word2;
			System.out.println(temp[i][j]);
			while (temp[i][j]!=j)
			{
				result.append(" ");
				result.append(G.point[temp[i][j]]);
		        i=temp[i][j];
			}
		}
		str_result=result.toString();
		System.out.println(str_result);
		return str_result;
	}
    public static String outPut4(String temp4,String word1,String word2)//最短路径的格式化输出函数
	{
		String []words=new String[1000];
		String result;
		StringBuffer temp;
		temp=new StringBuffer();
		result=new String();
		int answer,len;
		words=temp4.split("[^a-z0-9]+");
		answer=Integer.parseInt(words[0]);
		if (answer==1)
			temp.append("No “"+word1+"” and “"+word2+"” in the graph!");
		else if (answer==2)
			temp.append("No “"+word2+"” in the graph!");
		else if (answer==3)
			temp.append("No “"+word1+"” in the graph!");
		else if (answer==4)
		{
			temp.append("The shortest path from “"+word1+"” to ”"+word2+"”is:");
			temp.append(word1+"->"+word2+"||length:2\n");
		}
		else
		{
			if (words.length==1)
				temp.append("There is no way from“"+word1+"” to “"+word2+"”\n");
			else
			{
				temp.append("The shortest path from “"+word1+"” to ”"+word2+"”is:");
				temp.append(word1+"->");
				for (int i=1;i<words.length;i++)
					temp.append(words[i]+"->");
				len=words.length;
				temp.append(word2+" ||length:"+len+"\n");
			}
		}
		result=temp.toString();
		return result;
	}
    
	public static String randomWalk() throws IOException {//随机游走的函数
		int pre=random_pos;
		Random random1=new Random();
		FileWriter fw=new FileWriter("random walk.txt");
		if(G.point_num==0)
		{
			time=-1;//空图
		}
		else if(time==0)
		{
			if(G.point_num==1)
			{
				random_pos=0;
			}
			else
			{
			random_pos=random1.nextInt(G.point_num-1);
			}
			out=out+G.point[random_pos];
			out=out+" ---> ";
			time++;
		}
		else if(get_real_int_array_length(G.Show(G.point[random_pos]))==0)
		{
			time=-2;//无出边
		}
		else
		{
			if(get_real_int_array_length(G.Show(G.point[random_pos]))==1)
			{
				random_pos=G.Show(G.point[random_pos])[0];
			}
			else
			{
				random_pos=G.Show(G.point[random_pos])
						[random1.nextInt(get_real_int_array_length(G.Show(G.point[random_pos]))-1)];
			}
			if(visted[pre][random_pos]!=0)
			{
				time=-3;//边重复
			}
			visted[pre][random_pos]++;
			out=out+G.point[random_pos];
			out=out+" ---> ";
			time++;
		}
		fw.write(out);
		fw.close();
		return out;
	}
	public static int get_real_int_array_length(int []A)//获得数组实际长度的函数
	{
		int i;
		for(i=0;i<A.length;i++)
		{
			if(A[i]==-1)
			{
				return i;
			}
		}
		return A.length;
	}
    public static int Get_real_string_array_length(String[]A)//获取字符串实际长度的函数
    {
    	int i;
    	for(i=0;i<A.length;i++)
    	{
    		if(A[i]==null||A[i].length()<=0)
    		{
    			return i;
    		}
    	}
		return A.length;
    }
	       
}
class FunctionWindows extends Frame implements ActionListener//GUI主体框架定义
{
	private static final long serialVersionUID = 1L;
	static FunctionWindows frm1=new FunctionWindows();
	static FunctionWindows frm2=new FunctionWindows();
	static FunctionWindows frm3=new FunctionWindows();
	static FunctionWindows frm4=new FunctionWindows();
	static FunctionWindows frm5=new FunctionWindows();
	
	static Button btn11=new Button("展示");
	static Button btn12=new Button("退出");
	static TextArea txa11=new TextArea("",6,10,TextArea.SCROLLBARS_NONE);
	
	static TextArea txa211=new TextArea("",6,10,TextArea.SCROLLBARS_NONE);
	static TextArea txa212=new TextArea("",6,10,TextArea.SCROLLBARS_NONE);
	static TextArea txa22=new TextArea("",6,10,TextArea.SCROLLBARS_VERTICAL_ONLY);
	static Button btn21=new Button("确定");
	static Button btn22=new Button("退出");
	
	static TextArea txa31=new TextArea("",6,10,TextArea.SCROLLBARS_VERTICAL_ONLY);
	static TextArea txa32=new TextArea("",6,10,TextArea.SCROLLBARS_VERTICAL_ONLY);
	static Button btn31=new Button("开始生成");
	static Button btn32=new Button("退出");
	
	static TextArea txa411=new TextArea("",6,10,TextArea.SCROLLBARS_NONE);
	static TextArea txa412=new TextArea("",6,10,TextArea.SCROLLBARS_NONE);
	static TextArea txa42=new TextArea("",6,10,TextArea.SCROLLBARS_VERTICAL_ONLY);
	static Button btn41=new Button("确定");
	static Button btn42=new Button("退出");
	
	static TextArea txa51=new TextArea("",6,10,TextArea.SCROLLBARS_NONE);
	static TextArea txa52=new TextArea("",6,10,TextArea.SCROLLBARS_VERTICAL_ONLY);
	static Button btn51=new Button("游走");
	static Button btn52=new Button("退出");
	
	
	public static void CreateGraphWindow() throws IOException{//GUI：文件读取
		File f;
		String path ="";
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "仅限txt文件", "txt");//建立过滤器，只显示jpg和gif
		    chooser.setFileFilter(filter);//开始过滤
		int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	f=chooser.getSelectedFile();
	    	path=path+f.getPath();
	    }
	    path=path.replaceAll("\\\\","\\\\\\\\");
	    System.out.println(path);
	    Directed_graph.CreateGraph(path);		
	}
	public static void showDirectedGraph() {//GUI：展示图窗口
	    Directed_graph.showDirectedGraph();
		frm1.setLayout(null);
		frm1.setSize(640, 480);
		btn11.addActionListener(frm1);
		btn11.setBounds(280, 160, 80, 40);
		txa11.setBounds(200, 240, 240, 40);
		btn12.addActionListener(frm1);
		btn12.setBounds(280,320, 80, 40);
		txa11.setEditable(false);
		frm1.add(btn11);
		frm1.add(btn12);
		frm1.add(txa11);
		frm1.setVisible(true);
		
	}
	public static void queryBridgeWordsWindow()//GUI：获取桥接词的窗口
	{
		frm2.setLayout(null);
		frm2.setSize(640, 480);
		btn21.addActionListener(frm2);
		btn22.addActionListener(frm2);
		txa211.setBounds(220, 80, 80, 40);
		txa212.setBounds(340, 80, 80, 40);
		btn21.setBounds(280, 160, 80, 40);
		txa22.setBounds(200, 240, 240, 40);
		btn22.setBounds(280, 320, 80, 40);
		txa22.setEditable(false);
		frm2.add(txa211);
		frm2.add(txa212);
		frm2.add(txa22);
		frm2.add(btn21);
		frm2.add(btn22);
		frm2.setVisible(true);
	}
	public static void generateNewTextWindow() {//GUI：生成新文本窗口
		frm3.setLayout(null);
		frm3.setSize(640,480);
		btn31.addActionListener(frm3);
		btn32.addActionListener(frm3);
		txa31.setBounds(100, 80, 440, 100);
		btn31.setBounds(80, 350, 80, 40);
		txa32.setBounds(100, 240, 440, 100);
		btn32.setBounds(480, 350, 80, 40);
		
		txa32.setEditable(false);
		frm3.add(txa31);
		frm3.add(txa32);
		frm3.add(btn31);
		frm3.add(btn32);
		frm3.setVisible(true);
	}
	public static void calcShortestPathWindow()//GUI：计算最短路径窗口
	{
		frm4.setLayout(null);
		frm4.setSize(640, 480);
		btn41.addActionListener(frm4);
		btn42.addActionListener(frm4);
		txa411.setBounds(220, 80, 80, 40);
		txa412.setBounds(340, 80, 80, 40);
		btn41.setBounds(280, 160, 80, 40);
		txa42.setBounds(120, 240, 400, 120);
		btn42.setBounds(280, 400, 80, 40);
		txa42.setEditable(false);
		frm4.add(txa411);
		frm4.add(txa412);
		frm4.add(txa42);
		frm4.add(btn41);
		frm4.add(btn42);
		frm4.setVisible(true);
		
	}
	public static void randomWalkWindow() {//GUI：随机游走窗口
		frm5.setLayout(null);
		frm5.setSize(640, 480);
		btn51.addActionListener(frm5);
		btn52.addActionListener(frm5);
		txa51.setBounds(200, 80, 240, 40);
		txa51.setEditable(false);
		btn51.setBounds(280, 160, 80, 40);
		txa52.setBounds(100, 240, 440, 90);
		txa52.setEditable(false);
		btn52.setBounds(280, 420, 80, 40);
		frm5.add(btn51);
		frm5.add(btn52);
		frm5.add(txa51);
		frm5.add(txa52);
		frm5.setVisible(true);
	}
	public void actionPerformed(ActionEvent e)//GUI：主窗口
	{
		Button btn=(Button) e.getSource();
		if(btn==btn11)
		{
			ShowPhoto.showpho();
			txa11.setText("图片已保存于E:\\JavaCode\\MainWindow\\src\\graph.jpg");
		}
		else if(btn==btn12)
		{
			frm1.dispose();
		}
		else if (btn==btn21)
		{
			String temp2,word1,word2;
			word1=txa211.getText();
			word2=txa212.getText();
			if (word1!=null && word2!=null)
			{
				temp2=Directed_graph.queryBridgeWords(word1,word2);
				txa22.setText(Directed_graph.outPut2(temp2, word1, word2));
			}
			else
				txa22.setText("error!");
		}
		else if (btn==btn22)
			frm2.dispose();
		else if(btn==btn31)
		{
			txa32.setText(Directed_graph.generateNewText(txa31.getText()));
		}
		else if(btn==btn32)
		{
			frm3.dispose();
		}
		else if(btn==btn41)
		{
			String temp4,word1,word2;
			StringBuffer temp4_buf;
			word1=txa411.getText();
			word2=txa412.getText();
			int flag=-1;
			if (word1.length()!=0 && word2.length()!=0)
			{
				temp4=Directed_graph.calcShortestPath(word1,word2);
				txa42.setText(Directed_graph.outPut4(temp4, word1, word2));
			}
			else if(word1.length()!=0 && word2.length()==0)
			{
				for (int i=0;i<Directed_graph.G.point_num;i++)
				{
					if (Directed_graph.G.point[i].equals(word1)==true)
					{
						flag=i;
						break;
					}
				}
				if (flag==-1)
				{
					temp4=Directed_graph.calcShortestPath(word1,Directed_graph.G.point[0]);
					txa42.setText(Directed_graph.outPut4(temp4,word1,Directed_graph.G.point[0]));
				}
				else
				{
					temp4_buf=new StringBuffer(10000);
					for (int i=0;i<Directed_graph.G.point_num-1;i++)
					{
						temp4=new String();
						if (i!=flag)
						{
							temp4=Directed_graph.calcShortestPath(word1,Directed_graph.G.point[i]);
							temp4_buf.append(Directed_graph.outPut4(temp4,word1,Directed_graph.G.point[i]));
						}
					}
					txa42.setText(temp4_buf.toString());
				}
			}
			else if(word1.length()==0 && word2.length()!=0)
			{
				for (int i=0;i<Directed_graph.G.point_num;i++)
				{
					if (Directed_graph.G.point[i].equals(word2)==true)
					{
						flag=i;
						break;
					}
				}
				if (flag==-1)
				{
					temp4=Directed_graph.calcShortestPath(word2,Directed_graph.G.point[0]);
					txa42.setText(Directed_graph.outPut4(temp4,word2,Directed_graph.G.point[0]));
				}
				else
				{
					temp4_buf=new StringBuffer();
					for (int i=0;i<Directed_graph.G.point_num;i++)
					{
						temp4=new String();
						if (i!=flag)
						{
							temp4=Directed_graph.calcShortestPath(word2,Directed_graph.G.point[i]);
							temp4_buf.append(Directed_graph.outPut4(temp4,word2,Directed_graph.G.point[i]));
						}
					}
					txa42.setText(temp4_buf.toString());
				}
			}
			else
				txa42.setText("error!");
		}
		else if(btn==btn42)
			frm4.dispose();
		else if(btn==btn51)
		{
			if(Directed_graph.time==-1)
			{
				txa51.setText("这是空图  游走结束");
			}
			else if(Directed_graph.time==-2)
			{
				txa51.setText("无出边  游走结束");
			}
			else if(Directed_graph.time==-3)
			{
				txa51.setText("边重复  游走结束");
			}
			else
			{
				try {
					txa52.setText(Directed_graph.randomWalk());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		else if(btn==btn52)
		{
			frm5.dispose();
			//System.exit(0);
		}
	}
}
 public class MainWindow extends Frame implements ActionListener//GUI：监听器注册
{	
	private static final long serialVersionUID = 1L;
	   static MainWindow frm=new MainWindow();
	   static Button btn1=new Button("展示图");
	   static Button btn2=new Button("求桥接词");
	   static Button btn3=new Button("生成新文本");
	   static Button btn4=new Button("计算两个单词最短路径");
	   static Button btn5=new Button("随机游走");
	   static Button btn6=new Button("退出");
	   public static void main(String args[]) throws IOException
	   {
		   btn1.addActionListener(frm); // 把事件监听器frm向btn1注册
		   btn2.addActionListener(frm); // 把事件监听器frm向btn2注册
		   btn3.addActionListener(frm); // 把事件监听器frm向btn3注册
		   btn4.addActionListener(frm); // 把事件监听器frm向btn1注册
		   btn5.addActionListener(frm);
		   btn6.addActionListener(frm);// 把事件监听器frm向btn2注册
		   frm.setTitle("Action Event");
		   frm.setLayout(null);
		   frm.setSize(900,600);
		   btn1.setBounds(350,20,200,100);
		   btn2.setBounds(50,170,200,100);
		   btn3.setBounds(650,170,200,100);
		   btn4.setBounds(200,295,200,100);
		   btn5.setBounds(500,295,200,100);
		   btn6.setBounds(250,450,400,100);
		   frm.setVisible(true);
		   frm.add(btn1);
		   frm.add(btn2);
		   frm.add(btn3);
		   frm.add(btn4);
		   frm.add(btn5); 
		   frm.add(btn6); 
		   FunctionWindows.CreateGraphWindow();
	   }
	public void actionPerformed(ActionEvent e)
	   {
	      Button btn=(Button) e.getSource();  // 取得事件来源对象
	      if(btn==btn1) {           // 如果是按下btn1按钮
	    	  FunctionWindows.showDirectedGraph();
	      }
	      else if(btn==btn2)     // 如果是按下btn2按钮
	    	  FunctionWindows.queryBridgeWordsWindow();
	      else if(btn==btn3)                     // 如果是按下btn3按钮
	    	  FunctionWindows.generateNewTextWindow();
	      else if(btn==btn4)
	    	  FunctionWindows.calcShortestPathWindow();
	      else if(btn==btn5)
	    	  FunctionWindows.randomWalkWindow();
	      else if(btn==btn6)
	    	  frm.dispose();
	   }
	}
 class ShowPhoto extends JFrame {//GUI：打开有向图保存的文件
	private static final long serialVersionUID = 1L;
	public static void showpho() {
		JFrame mainframe = new JFrame("graph");
	    JPanel cp = (JPanel) mainframe.getContentPane();
	    cp.setLayout(new BorderLayout());
	    ImageIcon background = new ImageIcon("E:\\JavaCode\\MainWindow\\src\\graph.jpg");
	    JLabel label=new JLabel(background);
	    cp.add("Center", label);
	    mainframe.pack();
	    mainframe.setVisible(true);
		}
	}
