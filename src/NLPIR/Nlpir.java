package NLPIR;

import com.sun.jna.Library;
import com.sun.jna.Native;


public class Nlpir {
	public static final int GBK = 0;
	public static final int UTF8 = 1;
	public Nlpir(int charset_type) {
		/*
#define GBK_CODE 0//Ĭ��֧��GBK����
#define UTF8_CODE GBK_CODE+1//UTF8����
#define BIG5_CODE GBK_CODE+2//BIG5����
#define GBK_FANTI_CODE GBK_CODE+3//GBK���룬�������������
#define UTF8_FANTI_CODE GBK_CODE+4//UTF8����
*/
		String argu = "";
		int init_flag = CLibrary.Segmentation.NLPIR_Init(
				argu, charset_type, "0");
		if (0 == init_flag) {
			System.err.println("Nlpir init succeed");
			return;
		} else
			System.out.println("Nlpir init fail");
	}
	public void AddNewWord(String sSourceFilename) {
		System.out.println(sSourceFilename);
		CLibrary.Segmentation.NLPIR_NWI_Start();//�����´ʷ��ֹ���
		int b1 = CLibrary.Segmentation.NLPIR_NWI_AddFile(sSourceFilename); //����´�ѵ�����ļ����ɷ������
		System.out.println(b1);
		CLibrary.Segmentation.NLPIR_NWI_Complete();//����ļ�����ѵ�����ݽ���
		int count = CLibrary.Segmentation.NLPIR_NWI_Result2UserDict();//�´�ʶ�������뵽�û��ʵ�
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

		boolean NLPIR_NWI_Start();//�����´ʷ��ֹ���

		int NLPIR_NWI_AddFile(String sInputFile); //����´�ѵ�����ļ����ɷ������

		boolean NLPIR_NWI_Complete();//����ļ�����ѵ�����ݽ���

		int NLPIR_NWI_Result2UserDict();//�´�ʶ�������뵽�û��ʵ�
		String NLPIR_NWI_GetResult(boolean bWeightOut);//����´�ʶ����

		String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
								 boolean bWeightOut);

		void NLPIR_Exit();
	}
}