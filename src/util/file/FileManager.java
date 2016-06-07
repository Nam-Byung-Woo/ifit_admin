package util.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import util.config.Code;
import util.system.GetDate;
import util.system.StringUtil;

public class FileManager {

	private Code code = new Code();
	private String dataServer_url;		// 데이터 서버 url
	private String dataMode;			// 업로드 mode
	private String FilePath_full; 		// 파일 전체 경로
	private String FilePath_basic;		// 저장경로
	private String FilePath_detail; 		// 파일 전체 경로
	private int maxSize = 10;			// MB단위

	public FileManager(String mode, String dir){
		this.dataServer_url = StringUtil.getPropertiesValue("path.properties", "dataServerURL");
		this.dataMode = mode + "/";
		this.FilePath_basic = StringUtil.getPropertiesValue("path.properties", "fileServerPath");	// 저장경로
		this.FilePath_detail = dir + "/";
	}
	
	public String fileUpload(List<String> fileName, List<File> getUploads, int idx, int option) throws IOException{
		FilePath_full = this.FilePath_basic + this.dataMode + this.FilePath_detail;
		GetDate yymmkk = new GetDate();
		String yyyyMMddHHmmss = yymmkk.yyyyMMddHHmmss();
		String saveFileName = yyyyMMddHHmmss + idx + option + fileName.get(idx).substring(fileName.get(idx).lastIndexOf("."));
		File destFile = new File(FilePath_full + yyyyMMddHHmmss + idx + option + fileName.get(idx).substring(fileName.get(idx).lastIndexOf(".")));			       
		FileUtils.copyFile(getUploads.get(idx), destFile);
		return this.dataServer_url + this.dataMode + FilePath_detail + saveFileName;
	}
	
	public boolean fileValidation(List<File> getUploads, List<String> fileName, List<File> uploads) throws IOException{			// 파일validation
		long size_byte = maxSize*1024*1024;
		int fileExtIdx = 0;
		String chkExt = "";
		for(int i=0; i<uploads.size(); i++){
			fileExtIdx = fileName.get(i).lastIndexOf(".");
			chkExt = fileName.get(i).substring(fileExtIdx+1);
			
			// 확장자 체크
			if(!code.getAttachImageFileExtMap().containsKey(chkExt.toLowerCase())){
				return false;
			}

			// 크기 체크
			if(getUploads.get(i).length() > size_byte){
				return false;
			}
		}

		return true;
	}
	
	public void fileDelete(String fileName) throws IOException{
		this.FilePath_full = this.FilePath_basic + this.dataMode + this.FilePath_detail;
		File selectFile = new File(FilePath_full + fileName);
		selectFile.delete();
	}
}
