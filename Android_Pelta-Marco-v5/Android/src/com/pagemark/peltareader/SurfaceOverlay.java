package com.pagemark.peltareader;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pagemark.Qrdecoder;

/*
 Autor: Paulo Castro
 Projeto: QRCODE - ECRYPT
 Data: 23/11/2016
 obs: A Essa classe é a principal classe para mostrar e captar o canvas da câmera - Serão setados os parâmetros. 
*/

public class SurfaceOverlay extends Activity {
		private CamPreview mPreview;
		private OnTopCanvas mDrawOnTop;
		private OnPreviewBox mOnPrevBox;

		boolean mFinished;
		Camera mCamera;
		BroadcastReceiver rcvr = new Rcvr();
		private CountDownTimer standbyTimer;
		private PowerManager.WakeLock pmWakeLock;
		//		private static boolean onScreenOn;

		/** Called when the activity is first created. */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Hide the window title.
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			/*
			  Método que seta a imagem na tela - main.XML 
			*/
			setContentView(R.layout.main);
			

			final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            this.pmWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Phone Wake Lock");
            this.pmWakeLock.acquire();
			
			final LinearLayout lltxt = (LinearLayout)findViewById(R.id.llayout_txt);
			Intent in = getIntent();
			Bundle bun = in.getExtras();
			
			mDrawOnTop = new OnTopCanvas(this);
			mOnPrevBox = new OnPreviewBox(this);
			mPreview = new CamPreview(this, mDrawOnTop, mOnPrevBox);
			
			final RelativeLayout rl = (RelativeLayout)findViewById(R.id.relativeLayout1);
			rl.addView(mPreview, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			addContentView(mOnPrevBox, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			new CountDownTimer(5 * 60000, 500) {
				public void onTick(long millisUntilFinished) {
				mPreview.UpdateAutoFocus();
					return;
				}
				public void onFinish() {
					start();
					return;
				}
			}.start();
			
			standbyTimer = new CountDownTimer(45000, 1000) {
				public void onTick(long millisUntilFinished) {}
				public void onFinish() {
					SurfaceOverlay.this.finish();
					Intent myIntent = new Intent(SurfaceOverlay.this, CamStandby.class);
					SurfaceOverlay.this.startActivity(myIntent);
					return;
				}
			};
		}

		@Override
		protected void onResume() {
			super.onResume();
			// Open the default i.e. the first rear facing camera.
			Log.i("BROADCAST","SurfaceOverlay onResume()");
			
			mCamera = Camera.open();
			mPreview.setCamera(mCamera);
			
			IntentFilter iFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
			iFilter.addAction(Intent.ACTION_SCREEN_OFF);
			iFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
			registerReceiver(rcvr, iFilter);
			
			standbyTimer.start();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			standbyTimer.cancel();
			standbyTimer.start();
			return super.onTouchEvent(event);
		}
		
		@Override
		protected void onPause() {
			super.onPause();
			// Open the default i.e. the first rear facing camera.
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mPreview.setCamera(null);
			
			standbyTimer.cancel();
		}

		@Override
		protected void onDestroy() {
			this.pmWakeLock.release();
			unregisterReceiver(rcvr);
			Log.i("SURFACEOVERLAY", "onDestroy() called in SurfaceOverlay");
			super.onDestroy();
		}
		

		public boolean onKeyDown(int keyCode, KeyEvent event)  {
			if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0) {
				unregisterReceiver(rcvr);
				Intent myIntent = new Intent(SurfaceOverlay.this, Settings.class);
				SurfaceOverlay.this.startActivity(myIntent);
		    	return true;
		    }
			
		    return super.onKeyDown(keyCode, event);
		}

		public void doSomething(){
//			final RelativeLayout rl = (RelativeLayout)findViewById(R.id.relativeLayout1);
//			rl.removeView(mPreview);
//			rl.addView(mPreview, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//			LinearLayout llHeader = (LinearLayout)findViewById(R.id.ll_header);
//			llHeader.bringToFront();
//			mOnPrevBox.bringToFront();
		}
		
	class Rcvr extends BroadcastReceiver{

		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF) || intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)){
//				doSomething();
				Log.i("BROADCAST","Screen OFF");
				Intent myIntent = new Intent(SurfaceOverlay.this, CamStandby.class);
				SurfaceOverlay.this.startActivity(myIntent);
				SurfaceOverlay.this.finish();
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
				Log.i("BROADCAST","Screen ON");
			}
		}
	}
}

class OnPreviewBox extends View {
	public int previewLeft;
	public int previewTop;
	public int previewRight;
	public int previewBottom;
	public int previewCtrHor;
	public int previewCtrVer;
	
	private Paint mTrans;
	private Paint mThickLine;
	private Paint mFullTrans;
	
	public OnPreviewBox(Context context) {
		super(context);
	}
	
	protected void onDraw(Canvas canvas) {
		int prevWidth = previewRight - previewLeft;
		int prevHeight 	= previewBottom - previewTop;
		int squareHalfSide = 0;
		
		if(prevHeight < prevWidth){
			squareHalfSide = (int) (prevHeight * 0.35);
		}else {
			squareHalfSide = (int) (prevWidth * 0.35);
		}
//		squareHalfSide = 150;
		
		int lftInnerSqr	=	previewCtrHor - squareHalfSide;
		int topInnerSqr	=	previewCtrVer - squareHalfSide; 
		int rgtInnerSqr	=	previewCtrHor + squareHalfSide;
		int btmInnerSqr	=	previewCtrVer + squareHalfSide;
		
		mThickLine = new Paint();
		mThickLine.setColor(Color.BLACK);
		mThickLine.setStyle(Style.STROKE);
		mThickLine.setStrokeWidth(2);
		canvas.drawRect(lftInnerSqr, topInnerSqr, rgtInnerSqr, btmInnerSqr, mThickLine);
		
		mTrans = new Paint();
		mTrans.setColor(Color.BLACK);
		mTrans.setAlpha(75);
		mTrans.setStyle(Style.FILL);  
		canvas.drawRect(new Rect(previewLeft, previewTop, lftInnerSqr, previewBottom), mTrans); 
		canvas.drawRect(new Rect(rgtInnerSqr, previewTop, previewRight, previewBottom), mTrans); 
		canvas.drawRect(new Rect(lftInnerSqr, previewTop, rgtInnerSqr, topInnerSqr), mTrans); 
		canvas.drawRect(new Rect(lftInnerSqr, btmInnerSqr, rgtInnerSqr, previewBottom), mTrans); 

		super.onDraw(canvas);
	}
}

// ----------------------------------------------------------------------

	class OnTopCanvas extends View {
		List<Camera.Size> mCameraPreviewSize;
		Bitmap mBitmap;
		Paint mPaintYellow;
		Paint mPaintRed;
		Paint mPaintBlue;
		byte[] mYUVData;
		int[] mRGBData;
		int mImageWidth, mImageHeight;
		boolean mGrab;
		Context mContext;
		Camera mCamera;
		Bitmap mFrameImage;
		String mCovertMessage;
		String mMessage;
		int[] mPos;

		public OnTopCanvas(Context context) {
			super(context);

			mPaintYellow = new Paint();
			mPaintYellow.setStyle(Paint.Style.FILL);
			mPaintYellow.setColor(Color.YELLOW);
			mPaintYellow.setTextSize(25);

			mPaintRed = new Paint();
			mPaintRed.setStyle(Paint.Style.FILL);
			mPaintRed.setColor(Color.RED);
			mPaintRed.setTextSize(25);

			mPaintBlue = new Paint();
			mPaintBlue.setStyle(Paint.Style.FILL);
			mPaintBlue.setColor(Color.BLUE);
			mPaintBlue.setTextSize(15);
			
			mBitmap = null;
			mYUVData = null;
			mRGBData = null;
			mContext = getContext();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			int canvasWidth = canvas.getWidth();
			int canvasHeight = canvas.getHeight();

			if (mCamera != null) {

				long lTime = System.currentTimeMillis();
				lTime = lTime & 0xffff;

				if (mMessage != null && mMessage.length() != 0) {

					String overStr = String.format("%04d", lTime) + ":Over  : "
							+ mMessage;
					canvas.drawText(overStr, 30, canvasHeight - 75, mPaintBlue);
					float scx = (float) mImageWidth / canvasWidth;
					float scy = (float) mImageHeight / canvasHeight;

					Matrix m = new Matrix();
					float[] f = new float[8];
					for (int j = 0; j < 8; j++) {
						f[j] = mPos[j];
					}

					m.setScale(scx, scy);
					m.mapPoints(f);

					canvas.drawCircle(f[0], f[1], 5, mPaintYellow);
					canvas.drawCircle(f[2], f[3], 5, mPaintYellow);
					canvas.drawCircle(f[4], f[5], 5, mPaintYellow);
					canvas.drawCircle(f[6], f[7], 5, mPaintYellow);
					mMessage = null;
				}

				if (mCovertMessage != null && mCovertMessage.length() != 0) {
					String Str = String.format("%04d", lTime) + ":Covert : "
							+ mCovertMessage;
					canvas.drawText(Str, 30, canvasHeight - 40, mPaintBlue);
					mCovertMessage = null;
				}
			}
			// end if statement
			super.onDraw(canvas);
		} 	// end onDraw method

	}

	// ----------------------------------------------------------------------

class CamPreview extends ViewGroup implements SurfaceHolder.Callback {
//		private final String TAG = "Preview";

	SurfaceHolder mHolder;
	SurfaceView mSurfaceView;
	String mCovertMessage;
	String mMessage;

	Camera mCamera;
	OnTopCanvas mDrawOnTop;
	OnPreviewBox mOnPrevBox;
	Size mPreviewSize;
	List<Size> mSupportedPreviewSizes;
	List<Camera.Size> supportedSizes;
	int mFocusRetry;
	int mNextRead;
	Config conf;
	
	Context context;
	
	boolean mFinished;
	private Qrdecoder Symbol = new Qrdecoder();

	CamPreview(Context context, OnTopCanvas drawOnTop, OnPreviewBox onPrevBox) {
		super(context);
		this.context = context;
		conf = Config.getInstance(context);

		mDrawOnTop = drawOnTop;
		mOnPrevBox = onPrevBox;
		mFinished = false;
		mSurfaceView = new SurfaceView(context);
		
		addView(mSurfaceView);
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		
		mHolder = mSurfaceView.getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mFocusRetry = 0;
	}
	
	public void UpdateAutoFocus() {
		if (mCamera != null) {
			if (mFocusRetry > 0)
			{
				mFocusRetry = mFocusRetry - 1;
				if (mFocusRetry == 0) {
					mCamera.autoFocus(new AutoFocusCallback() {

						public void onAutoFocus(boolean arg0, Camera arg1) {
							
							
							mFocusRetry = 3;
						}
					});
				}
			}
		}
	}

	public void setCamera(Camera camera) {
		mCamera = camera;
		mDrawOnTop.mCamera = mCamera;
		if (mCamera != null) {
			
			
			mSupportedPreviewSizes = mCamera.getParameters()
					.getSupportedPreviewSizes();
			requestLayout();
		}
	}

	public void releaseCamera() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// We purposely disregard child measurements because act as a
		// wrapper to a SurfaceView that centers the camera preview instead
		// of stretching it.
		final int width = resolveSize(getSuggestedMinimumWidth(),
				widthMeasureSpec);
		final int height = resolveSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		setMeasuredDimension(width, height);

		if (mSupportedPreviewSizes != null) {
			mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width,
					height);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed && getChildCount() > 0) {
			final View child = getChildAt(0);

			final int width = r - l;
			final int height = b - t;

			int previewWidth = width;
			int previewHeight = height;
			if (mPreviewSize != null) {
				previewWidth = mPreviewSize.width;
				previewHeight = mPreviewSize.height;
			}

			// Center the child SurfaceView within the parent.
			if (width * previewHeight > height * previewWidth) {
				final int scaledChildWidth = (int) ((previewWidth * height
						/ previewHeight) * 0.8);
			
				// Code - Modificação feita por Paulo Castro - 29/11/2016
				// OBs: Qualquer modificação feita, favor permanecer o código antigo comentado logo a cima da modificação.
				// Seta o tamanho do canvas - Classe Importante no Projeto - Cuidado!!!
				//
				mOnPrevBox.previewLeft		=	(int) ((width - scaledChildWidth / 1) - (0.06 * width));
				
				//Code - Inserido para retorna mensagem 
	Toast.makeText(getContext(), VIEW_LOG_TAG, 1000);
				
				mOnPrevBox.previewTop		=	(int) (height * 0.1);
				mOnPrevBox.previewRight	=	(int) (((width + scaledChildWidth) / 2) - (0.06 * width));
				mOnPrevBox.previewBottom	=	(int) (height - (height * 0.1));
			} else {
				final int scaledChildHeight = (int)((previewHeight * width
						/ previewWidth) * 0.8);
				mOnPrevBox.previewLeft		=	(int) (width * 0.1);
				mOnPrevBox.previewTop		=	(int) (((height - scaledChildHeight) / 2) + (0.06 * height));
				mOnPrevBox.previewRight	=	(int) (width - (width * 0.1));
				mOnPrevBox.previewBottom	=	(int) (((height + scaledChildHeight) / 2) + (0.06 * height));
			}
			child.layout(mOnPrevBox.previewLeft, mOnPrevBox.previewTop, mOnPrevBox.previewRight, mOnPrevBox.previewBottom);
			mOnPrevBox.previewCtrHor = mOnPrevBox.previewLeft + ((mOnPrevBox.previewRight - mOnPrevBox.previewLeft) / 2);
			mOnPrevBox.previewCtrVer = mOnPrevBox.previewBottom + ((mOnPrevBox.previewTop - mOnPrevBox.previewBottom) / 2);

/*			
			if (width * previewHeight > height * previewWidth) {
				final int scaledChildWidth = previewWidth * height
						/ previewHeight;
				child.layout((width - scaledChildWidth) / 2, 0,
						(width + scaledChildWidth) / 2, height);
			} else {
				final int scaledChildHeight = previewHeight * width
						/ previewWidth;
				child.layout(0, (height - scaledChildHeight) / 2, width,
						(height + scaledChildHeight) / 2);
			}
*/
		}
	}

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
		
		// Paulo Castro - 29/11/2016	
		System.out.printf("Paulo Castro::::"+"%d,%d\n",size.width,size.height);
		
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// mCamera = Camera.open();
		try {
			mCamera.setPreviewDisplay(holder);
			mSupportedPreviewSizes = mCamera.getParameters()
					.getSupportedPreviewSizes();

// Preview callback

			mCamera.setPreviewCallback(new PreviewCallback() {
				public void onPreviewFrame(byte[] data, Camera camera) {

					if ((mDrawOnTop == null) || mFinished)
						return;

					// don't attempt to read the symbol yet
					if (mNextRead != 0) {
						mNextRead--;
						return;
					}

					if (mDrawOnTop.mYUVData == null) {
						// Initialize the draw-on-top companion
						Camera.Parameters params = camera.getParameters();
//						int w = params.getPreviewSize().width;
//						int h = params.getPreviewSize().height;
						mDrawOnTop.mImageWidth = params.getPreviewSize().width;
						mDrawOnTop.mImageHeight = params.getPreviewSize().height;
						mDrawOnTop.mYUVData = new byte[data.length];
					}
// DECODE CAPTURED IMAGE
					// Try to read the Pelta barcode
					// 1. Wait for focus to be complete
					// 2. Create the Qrdecoder object.
					// 3. Initialize the Object with image data and the width
					// and height
					// 4. Analyze the images
					// 5. If successful read the Overt and covert messages.

					System.arraycopy(data, 0, mDrawOnTop.mYUVData, 0,data.length);

//					if (mFocusRetry != 0) 
					{
						int success;
//						Qrdecoder Symbol = new Qrdecoder();
						Symbol.Initilize(mDrawOnTop.mYUVData,
								mDrawOnTop.mImageWidth, mDrawOnTop.mImageWidth);
						success = Symbol.AnalyzeImage(mDrawOnTop.mImageWidth,
								mDrawOnTop.mImageWidth);
						
						String ovMsg = "";
						String covMsg = "";

						if (success == 0) {
							String displayMsg = "";
							mMessage = Symbol.GetMessage();
							ovMsg = mMessage;
							// Log.v(TAG,"Successful QR decode: " + mMessage );
							
							displayMsg += "OVERT: " + mMessage;
							
							
							
							if(conf.getReadCov() == 1){
//								byte[] storedkey = conf.getArrEncKey();
//								byte[] key = Symbol.getHashedKey(storedkey);
								
								mCovertMessage = Symbol.GetCovertMessageKeyed(conf.getArrEncKey());
								covMsg = mCovertMessage;
								// Log.v(TAG,"Successful QR Cover decode: " +
								// mCovertMessage );
								displayMsg += "\nCOVERT: " + mCovertMessage;
							}

							mNextRead = 1;

							mDrawOnTop.mMessage = mMessage;
//							mDrawOnTop.mCovertMessage = mCovertMessage;
							mDrawOnTop.mPos = Symbol.GetReadPos();

				            Intent i = new Intent();
				            i.setClassName("com.pagemark.peltareader", "com.pagemark.peltareader.ShowResults");
//				            i.putExtra("BARCODE_CONTENT", displayMsg);
				            i.putExtra("BARCODE_CONTENT_OV", ovMsg);
				            i.putExtra("BARCODE_CONTENT_COV", covMsg);
				            i.putExtra("SAVE_TO_DB", "true");
				            i.putExtra("PKEY", "0");
				            i.putExtra("CALLER", "SurfaceOverlay");
				            context.startActivity(i);
						}
						Symbol.Close();
						mDrawOnTop.invalidate();
					}
				}
			});
		} catch (IOException exception) {
			
		} catch (Exception e) {
			
		}
	}

	public native void OpenQRDecode();

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
		if (mCamera != null) {
			mCamera.stopPreview();
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Now that the size is known, set up the camera parameters and begin
		// the preview.

		if(mCamera != null){
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
			requestLayout();
	
			parameters.setPreviewFrameRate(16);
			parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
	
			try {
			mCamera.setParameters(parameters);
			} catch (Exception e )
			{
				System.out.println("Exception setParameters");
			}
		
			mCamera.startPreview();
			mCamera.autoFocus(new AutoFocusCallback() {
				public void onAutoFocus(boolean arg0, Camera arg1) {
					mFocusRetry = 3;
				}
			});
		}
	}
}