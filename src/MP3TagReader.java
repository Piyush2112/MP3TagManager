
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;


public class MP3TagReader {
	static int a=0;

	/**
	 * @param args
	 * @throws InvalidDataException 
	 * @throws UnsupportedTagException 
	 * @throws InterruptedException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws NotSupportedException 
	 */
	public static void main(String[] args)  throws IOException,  NumberFormatException, UnsupportedTagException, InvalidDataException, InterruptedException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NotSupportedException 
	{
		//final File folder = new File("C://Users/kumarp49/Music/iTunes/iTunes Media/Music");
		final File folder = new File("C://Users/kumarp49/Music/iTunes/iTunes Media");
		listFilesForFolder(folder);
	}

	public static void listFilesForFolder(final File folder) throws IOException,  UnsupportedTagException, InvalidDataException, InterruptedException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NotSupportedException 
	{


		int k = 0,j=0;
		Iterator i= FileUtils.iterateFiles(folder,  TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		while(i.hasNext())// && j<100000)
		{
			String s=i.next().toString();

			if(s.endsWith("mp3")||s.endsWith("MP3"))
			{
				getTags(s);	
				System.out.println("S:"+s);
				j++;
			}
			else{
				//System.out.println("Not a MP3 file:" + s);
			}

		}
		System.out.println("Total count "+ j+ "copied: ");
		//System.out.println(FileUtils.listFiles(folder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE));

	}


	public static void checkAndFile(String Foldername, String FileName, String sourcePath, String MoveOption) throws IOException
	{
		String targetFolder="C://Music/";
		File folderName=new File(targetFolder+Foldername);
		File fileName=new File(folderName.getAbsolutePath()+File.separator+FileName);
		//System.out.println(" New file path: "+fileName.getAbsolutePath());

		if(folderName.exists())
		{
			if(!(fileName.exists()))
			{
				FileUtils.copyFile(new File(sourcePath), fileName);
				//System.out.println("File Copied: "+fileName +"  Album:" +Foldername+ " Path:"+ sourcePath);
			}
			else
			{
				//System.out.println("File exists, picking the next one.");
			}
		}
		else
		{
			//System.out.println("Album Folder does not exist, creating Folder...");
			folderName.mkdir();
			if(!(fileName.exists()))
			{
				System.out.println("Source Path: "+sourcePath);

				FileUtils.copyFile(new File(sourcePath), fileName);
				//System.out.println("File Copied: "+fileName +"Album" +Foldername+ " Path:"+ sourcePath);

			}
		}
		//System.out.println("---------------------------");
	}

	public static void getTags(String mp3FileName) throws IOException,  UnsupportedTagException,NumberFormatException, InvalidDataException, InterruptedException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NotSupportedException{

		boolean is_modified=false;
		boolean copy_enabled=false;
		int counter=0;
		int skip_counter=0;
		int copy_counter=0;
		int yearStart=1900;
		int yearend=2016;
		//System.out.println("File name passed:"+mp3FileName);
		Mp3File mp3file = new Mp3File(mp3FileName);
		//System.out.println(mp3file.getFilename());
		if(mp3file.hasId3v1Tag() || mp3file.hasId3v2Tag())
		{
			String album= mp3file.getId3v2Tag().getAlbum();
			String title=  mp3file.getId3v2Tag().getTitle();
			String year=mp3file.getId3v2Tag().getYear();
			String sourcePath=mp3file.getFilename();
			ID3v1 id3v1Tag = mp3file.getId3v1Tag();
			String extension=mp3FileName.substring(mp3FileName.lastIndexOf(".")+1);
			/*System.out.println("Has custom tag?: " + (mp3file.hasCustomTag() ? "YES" : "NO"));
			System.out.println("Album: "+album);
			System.out.println("Title: "+title);
			 */

			String targetFolderName=album;
			//System.out.println("Album:" + album);
			//copy_enabled=true;
			try{
			//System.out.println("Album:" +targetFolderName +" Year:"+year + "Year"+id3v1Tag.getYear());
			//check if album name already has year appended
			if(year!=null  && album!=null)
			{
				//copy_enabled=true;
				try{
					if(Integer.parseInt(year)>=yearStart && Integer.parseInt(year)<=yearend)
					{
						if((!album.contains(year)))
						{
							copy_enabled=true;
							targetFolderName=album+" ("+year+")";
							//System.out.println("Found a song:" +title);
							copy_counter++;
						}
						else
						{
							copy_enabled=true;
							targetFolderName=album;
							//System.out.println("Found a song:" +title);
							copy_counter++;
						}
						
					}
					else
					{
						System.out.println("Criteria Not Satisfied in: "+targetFolderName);
						skip_counter++;
					}
				}
				catch(NumberFormatException n)
				{
					System.out.println("Exception Occured!!");
					n.printStackTrace();
				}
				catch(Exception n)
				{
					System.out.println("Exception Occured!!");
					n.printStackTrace();
				}

			}
			else
			{
				System.out.println("Something failed in year"+year+ "/ album"+album +" / album-year" +album.contains(year));
				skip_counter++;
			}
			}
			catch (NullPointerException N)
			{
				/*ID3v2 id3v2Tag;
				
				if (mp3file.hasId3v2Tag()) {
					  id3v2Tag = mp3file.getId3v2Tag();
					} else {
					  // mp3 does not have an ID3v2 tag, let's create one..
					  id3v2Tag = new ID3v24Tag();
					  mp3file.setId3v2Tag(id3v2Tag);
					}
				id3v2Tag.setYear(id3v1Tag.getYear());
					id3v2Tag.setTrack("5");
					id3v2Tag.setArtist("An Artist");
					id3v2Tag.setTitle("The Title");
					id3v2Tag.setAlbum("The Album");
					
					id3v2Tag.setGenre(12);
					id3v2Tag.setComment("Some comment");
					id3v2Tag.setComposer("The Composer");
					id3v2Tag.setPublisher("A Publisher");
					id3v2Tag.setOriginalArtist("Another Artist");
					id3v2Tag.setAlbumArtist("An Artist");
					id3v2Tag.setCopyright("Copyright");
					id3v2Tag.setUrl("http://foobar");
					id3v2Tag.setEncoder("The Encoder");
					//String name=mp3FileName
				System.out.println("Sending filename as:" +mp3FileName);
					mp3file.save(mp3FileName);
					System.out.println("Save complete.");*/
				N.printStackTrace();
			}


			//make filename for target mp3 file
			String targetFileName=title+"."+extension;

			//clean garbage values from all the tags
			Object object=mp3file.getId3v2Tag();



			Class idTag3v2Class = object.getClass();

			String[] garbageList = {"www.songs.pk","www.Songspk.name"};//,"Songs.PK","www",".Songs.name",".songs.pk"};

			for (Method method :  idTag3v2Class.getMethods()){
				//System.out.println("Method name: "+method.getName());

				if( method.getReturnType().toString().contains("String") && method.getName().contains("get"))
				{
					//counter++;
					//System.out.println("counter value:" +counter);
					String setterMethodName="set"+method.getName().substring(3);
					//System.out.println("New Name: "+setterMethodName);
					//System.out.println("GetterNAme: "+method.getName());

					// Invoke the method and read if the output contains value from garbage list
					//Buggy part - how to invoke these methods - don't know 
					try {
						String getterOutput = (String) method.invoke(object);
						if(getterOutput!=null) {
							String newTagValue = getterOutput;
							int foundindex = -1;
							for(int i=0; i<garbageList.length;i++)
							{
								if(getterOutput.toLowerCase().indexOf(garbageList[i].toLowerCase() )> -1) 
								{

									/*if(method.getName().equalsIgnoreCase("getYear")&&Integer.parseInt(getterOutput)>yearStart &&Integer.parseInt(getterOutput)<yearend)
									{
										copy_enabled=true;
										System.out.println("Found a song!!!");
									}*/

									foundindex = i;
									//System.out.println("Getter output:" +getterOutput.toLowerCase()+ "for : "+method.getName()+ " Garbage list: " +garbageList[i].toLowerCase());
									//System.out.println("current value:"+garbageList[i]);
									newTagValue=(getterOutput.toLowerCase().replace(garbageList[i].toLowerCase(), " "));

									//System.out.println(getterOutput.toLowerCase().replace(garbageList[i].toLowerCase(), " "));
									//System.out.println("New tag: "+ newTagValue);
									//System.out.println("Garbage found: "+garbageList[i] +" in method:" +method.getName());
								}
							}
							// if garbage value is present call setter method with new values
							if(foundindex>-1) {
								//TODO: Cleanup tag
								Method setterMethod = idTag3v2Class.getMethod(setterMethodName, String.class);
								if(!setterMethodName.equals("setGenreDescription")) {
									setterMethod.invoke(object, newTagValue);
									is_modified=true;
									//System.out.println("Setter method "+setterMethodName+" invoked with:" +newTagValue);			
								}
								else
								{
									setterMethod.invoke(object, newTagValue);
									is_modified=true;
									//System.out.println("Setter method "+setterMethodName+" invoked with:" +newTagValue);	
								}
							}
						}
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						System.out.println("No method found "+ setterMethodName);
					}

					//System.out.println("Methods return Type: "+method.getReturnType() +"  Name: "+method.getName());	
				}
			}

			/*if(is_modified)
			{
				mp3file.setId3v2Tag((ID3v2) object);
				mp3file.getId3v2Tag().setComment(" ");
				try {
					mp3file.save("c:\\Test.mp3");
				} catch (NotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			//File the songs in Albums
			if(copy_enabled){
				checkAndFile(targetFolderName, targetFileName,sourcePath, "copy");
				//System.out.println("Completed Copy function...");
			}
			/*checkAndFile(targetFolderName, targetFileName,sourcePath, "copy");
			System.out.println("Completed Copy function...");*/
			//Thread.sleep(1000);
		}
		else
		{
			System.out.println("Non IDV2/ IDV3 tags found!!");
			skip_counter++;
		}
	}
}
