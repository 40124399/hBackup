package hBackup;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Backend
{
	private List<String> line = new ArrayList<String>();
	private String path = System.getenv("APPDATA");
	private String name = "Tasks.txt";
	private String reading;
	private String[] sss;
	private Path p;
	
	//Rectified
	
	private int id;
	private String title;
	private String source;
	private String destination;
	private int versions;
	private String backup_name;
	private String archive_name;
	private Boolean automated;
	private int frequency;
	private boolean current = false;
	//Builders
	private String built;
	//Instances
	private CurrentTasks cur = null;
	private Holder held = new Holder();
	
	public void CreateTask(String[] vals)
	{
		Load();
		
		title = vals[0];
		source = vals[1];
		destination = vals[2];
		versions = Integer.parseInt(vals[3]);
		backup_name = vals[4];
		archive_name = vals[5];
		automated = Boolean.valueOf(vals[6]);
		frequency = Integer.parseInt(vals[7]);
		built = id+"";
		
		for(String ss : vals)
		{built += ","+ss;}
		built += ",0";
		line.add(built);
		Save();
	}
	
	public void PassTask(int id)
	{
		sss = PreparedLine(id);
		held.setChanged(true);
		held.addTask(sss);
		if(cur == null)
		{cur = new CurrentTasks(held, this);}
		else{cur.comeBack();}
		current = true;
	}
	
	public void Load()
	{
		try
		{
			File file = new File(path, name);
			if(file.exists())
			{
				 BufferedReader r = new BufferedReader(new FileReader(file));
				    try 
				    {
				    	line.clear();
				    	while((reading = r.readLine()) != null)
				    	{line.add(reading);}
				    	r.close();
				    	if(!line.isEmpty())
				    	{
				    		String[] t = line.get(line.size()-1).split("\\,");
					    	id = Integer.parseInt(t[0]) + 1;
				    	}
				    	else{id = 0;}
				    }catch(Exception e){}
			}
			else
			{
				file.createNewFile();
				Load();
			}
		   
		}catch(Exception e){}
	}
	
	public void edit(int id, String kp)
	{
		line.set(getPos(id), kp);
		Save();
	}
	
	public void delete(int id)
	{
		line.remove(getPos(id));
		Save();
	}
	
	public void Save()
	{
		try
		{
			File file = new File(path, name);
			FileWriter writer = new FileWriter(file, false);
			for(String l : line)
			{writer.write(l+"\n");}
			writer.close();
		} catch (IOException e){}
	}
	
	private String[] PreparedLine(int id)
	{
		String[] s = {};
		for(String l : line)
		{
			if(Integer.parseInt(l.split("\\,")[0]) == id)
			{
				s = l.split("\\,");
			}
		}
		return s;
	}
	
	private int getPos(int id)
	{
		int ii = 0;
		for(int i = 0; i < line.size(); i++)
		{
			if(id == Integer.parseInt(line.get(i).split("\\,")[0]))
			{
				ii = i;
				break;
			}
		}
		return ii;
	}
	
	public void setCurrent(boolean cur){this.current = cur;}
	public List<String> getList(){return line;}
	public void setList(List<String> line){this.line = line;}
	public void setPath(String p){this.path = p;}
	public String getPath(){return path;}
}
