package NLPIR;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.io.UnsupportedEncodingException;


public class Nlpir {
	public static final int GBK = 0;
	public static final int UTF8 = 1;
	public Nlpir(int charset_type) throws UnsupportedEncodingException {
/*
#define GBK_CODE 0//??????GBK????
#define UTF8_CODE GBK_CODE+1//UTF8????
#define BIG5_CODE GBK_CODE+2//BIG5????
#define GBK_FANTI_CODE GBK_CODE+3//GBK?????????????????
#define UTF8_FANTI_CODE GBK_CODE+4//UTF8????
*/
		String argu = "z:/program/java/dataMining";
		int init_flag = CLibrary.Segmentation.NLPIR_Init(
				argu, charset_type, "0");
		if (0 == init_flag) {
			System.err.println("Nlpir init succeed");
			return;
		} else
			System.err.println("Nlpir init fail");
	}
	public void AddNewWord(String sSourceFilename) {
		System.out.println(sSourceFilename);
		CLibrary.Segmentation.NLPIR_NWI_Start();//?????????????
		int b1 = CLibrary.Segmentation.NLPIR_NWI_AddFile(sSourceFilename); //????????????????????????
		System.out.println(b1);
		CLibrary.Segmentation.NLPIR_NWI_Complete();//????????????????????
		int count = CLibrary.Segmentation.NLPIR_NWI_Result2UserDict();//??????????????????
		String result = CLibrary.Segmentation.NLPIR_NWI_GetResult(false);
		System.out.println(count + ": " + result);
	}

	public Double NLPIR_FileProcess(String sSourceFilename, String sResultFilename, int bPOSTagged) {
		//AddNewWord(sSourceFilename);
		return CLibrary.Segmentation.NLPIR_FileProcess(sSourceFilename, sResultFilename, bPOSTagged);
	}

	public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged) {
		return CLibrary.Segmentation.NLPIR_ParagraphProcess(sSrc, bPOSTagged);
	}

	interface CLibrary extends Library {
		CLibrary Segmentation = (CLibrary) Native.loadLibrary(
				"NLPIR", CLibrary.class);

		int NLPIR_Init(String sDataPath, int encoding, String sLicenceCode);

		int NLPIR_ImportKeyBlackList(String sFilename);

		String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

		Double NLPIR_FileProcess(String sSourceFilename, String sResultFilename, int bPOSTagged);

		int NLPIR_AddUserWord(String sWord);
		int NLPIR_SaveTheUsrDic();

		boolean NLPIR_NWI_Start();//?????????????

		int NLPIR_NWI_AddFile(String sInputFile); //????????????????????????

		boolean NLPIR_NWI_Complete();//????????????????????

		int NLPIR_NWI_Result2UserDict();//??????????????????
		String NLPIR_NWI_GetResult(boolean bWeightOut);//???????????

		String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
								 boolean bWeightOut);

		void NLPIR_Exit();
	}
	public static void main(String... args) throws UnsupportedEncodingException {
		String currentPath = System.getProperty("user.dir");
		System.out.println(currentPath);
		new Nlpir(1);
	}
}