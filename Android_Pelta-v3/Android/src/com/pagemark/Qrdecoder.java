package com.pagemark;

//import android.widget.Toast;

public class Qrdecoder {
	private int mQrhandle;
	private int[] mSymbolPos;

	public void test() {
		jnitest();
	}

	public boolean Initilize(byte[] gray, int width, int height) {
		
		
		mQrhandle = OpenDecoder(gray, width, height);
		if (mQrhandle == 0) {
			return false;
		}
		return true;
	}
	public boolean Initilize(int version, int ecc, byte[] data) {
		

		
		mQrhandle = OpenDecoderData( version, ecc, data);
		if (mQrhandle == 0) {
			return false;
		}
		return true;
	}
	public int AnalyzeImage(int width, int height) {
		int ret;
		if (mQrhandle == 0)
			return 1;
		ret = Analyze(mQrhandle, width, height); // / zero return means found
													// code
		if (ret == 0) {
			if (mSymbolPos == null)
				mSymbolPos = new int[8];
			GetSymbolPosition(mQrhandle, mSymbolPos);
		}
		return ret;
	}

	public int[] GetReadPos() {
		return mSymbolPos;
	}

	public void Close() {
		if (mQrhandle != 0) {
			CloseDecoder(mQrhandle);
		}
	}
	
	public byte[] getHK(byte[] storedkey){
		byte[] hashedkey = new byte[16];
		qr000000000000(storedkey, hashedkey);
		return hashedkey;
	}

	public String GetMessage() {
		return GetDecodedMessage(mQrhandle);
	}

	public String GetCovertMessage() {
		
		 return GetDecodedCovertMessage(mQrhandle);
	}
	
	public String GetCovertMessageKeyed(byte[] hashedkey) {

		return GetDecodedCovertMessageKeyed(mQrhandle,hashedkey);
	}

	public native void jnitest();

	public native int OpenDecoder(byte[] gray, int width, int height);
	public native int OpenDecoderData(int width, int height,byte[] data );

	public native void CloseDecoder(int qrhandle);

	public native int Analyze(int qrhandle, int width, int height);

	public native String GetDecodedMessage(int qrhandle);

	public native String GetDecodedCovertMessage(int qrhandle);

	public native String GetDecodedCovertMessageKeyed(int qrhandle,byte[] key);
	public native void qr000000000000(byte[] key1,byte[] key2);

	public native int GetSymbolPosition(int qrhandle, int[] posarray);

	static {
		System.loadLibrary("pelta-jni");
	}
}