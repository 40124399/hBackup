package hBackup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Holder
{
	private String[] tasked;
	private List<String[]> Group = new ArrayList<String[]>();
	private boolean change = true;
	
	public void  addTask(String[] t)
	{Group.add(t);}
	public void removeTask(int n)
	{Group.remove(n);}
	
	public void RunTask(String[] task)
	{
		this.tasked = task;
		Prepare();
	}
	
	public void Prepare()
	{
		int run = 0;
		if(Integer.parseInt(tasked[4]) > 10){run += 1;}
		if(Boolean.valueOf(tasked[7]).TRUE){run += 2;}
		if(run == 0){}
		else if(run == 1){}
		else if(run == 2){}
		else if(run == 3){}
	}
	
	public void runIt(int id)
	{
		for(String[] s : Group)
		{
			if(Integer.parseInt(s[0]) == id)
			{
				if(Integer.parseInt(s[4]) == 0)
				{
					tasked = s;
					RunMV(s);
				}
				else
				{
					tasked = s;
					RunCP(s);
				}
				for(int i = 0; i < Group.size(); i++)
				{
					if(Integer.parseInt(Group.get(i)[0]) == Integer.parseInt(tasked[0]))
					{
						String[] ss = {"","","","","","","","","",""};
						for(int j = 0; j < 10; j++)
						{
							ss[j] = Group.get(i)[j];
						}
						ss[9] = ""+(Integer.parseInt(ss[9])+1);
						Group.set(i, ss);
						break;
					}
				}
				change = true;
			}
		}
	}
	
	private void RunMV(String[] task)
	{
		task[5] = task[6];
		task[9] = "";
		RunCP(task);
		
		delete(new File(task[2]));
	}
	
	private void delete(File ff)
	{
		File[] files = ff.listFiles();
		for(File f : files)
		{
			if(f.isDirectory()){delete(f);}
			else{f.delete();}
		}
	}
	
	private void RunCP(String[] task)
	{
		try
		{
			File[] files = new File(task[2]).listFiles();
			for(File f : files)
			{
				File directory = new File(String.valueOf(task[3]+"\\"+task[5]+task[9]+"\\"));
			    if(!directory.exists()){directory.mkdir();}
				Files.copy(f.toPath(), Paths.get(task[3]+"\\"+task[5]+task[9]+"\\"+f.getName()), StandardCopyOption.REPLACE_EXISTING);
				if(f.isDirectory())
				{
					String[] ok = {"","","","","","","","","","","",""};
					ok[2] = f.toPath()+"";
					ok[3] = task[3]+"\\"+task[5]+task[9]+"\\"+f.getName()+"\\";
					ok[5] = "";
					RunCP(ok);
				}
			}
		}catch(IOException e){e.printStackTrace();}
	}
	
	//Getters and Setters
	public Object[][] getTable()
	{
		Object[][] o = new Object[Group.size()][10];
		int i = 0;
		for(String[] s : Group)
		{
			o[i++] = s;
		}
		return o;
	}

	public boolean changed()
	{return change;}
	public void setChanged(boolean change)
	{this.change = change;}
	public List<String[]> getGroup()
	{return Group;}
	public void setGroup(List<String[]> Group)
	{this.Group = Group;}
}
