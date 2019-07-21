package com.taoyuanx.ca.util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cn.hutool.core.util.ZipUtil;

public class CommonUtil 
{

    public static void main(String[] args) throws Exception
    {
    	ZipUtil.zip("E://client/1537106292562/", "E://client/1_client.zip");
    }
    
    
    
    public static void zip(File src,File dest) throws Exception {
    	
    }
    public static void zip(ZipOutputStream out,BufferedOutputStream bos,File srcFile,String base) throws Exception
    {
        //如果路径为目录（文件夹）
        if(srcFile.isDirectory())
        {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = srcFile.listFiles();
            if(null==flist||flist.length==0)//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
            {
                out.putNextEntry(  new ZipEntry(base+"/"));
            }
            else//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
            {
                for(int i=0;i<flist.length;i++)
                {
                    zip(out,bos,flist[i],base+"/"+flist[i].getName());
                }
            }
        }
        else//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
        {
            out.putNextEntry( new ZipEntry(base) );
            FileInputStream fos = new FileInputStream(srcFile);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int tag;
            //将源文件写入到zip文件中
            while((tag=bis.read())!=-1)
            {
                bos.write(tag);
            }
            bis.close();
            fos.close();
        }
    }
    
    /**参照log4j  模板匹配

	 * xx{}xx{} 1,2

	 * @param pattern 

	 * @param objects

	 * @return

	 */
    
    public static  boolean isEmpty(String str){
		return null==str||"".equals(str);
	}
	public static String log4jFormat(String pattern,Object ...objects){
		if(isEmpty(pattern))return null;
		char[] arr = pattern.toCharArray();
		StringBuilder temp=new StringBuilder();
		int count=0,objLen=objects.length,len=arr.length;
		char left="{".charAt(0);
		char right="}".charAt(0);
		for(int i=0;i<len;i++){
			if(count<objLen){
				if(i<len-1&&left==arr[i]&&right==arr[i+1]){
					temp.append(objects[count++]);
					i++;	
				}else{
					temp.append(arr[i]);
				}
			}else{
				temp.append(arr[i]);
			}	
		}
		return temp.toString();
		
	}
}