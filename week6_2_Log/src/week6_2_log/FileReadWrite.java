package week6_2_log;

import java.io.File;
import java.util.ArrayList;

public interface FileReadWrite {
	public ArrayList<String> readFile(File file);
	public void writeFile(File file, ArrayList<String> data);
}
