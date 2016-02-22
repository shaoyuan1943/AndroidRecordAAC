package lwx.linin.aac;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.sinaapp.bashell.AacEncoder;

public class AAC implements Runnable
{

	/**从默认的16000开始，若失败则会用其余的采样率*/
	private static int[] sampleRates = { 16000, 44100, 22050, 11025, 8000, 4000 };

	private boolean isStart = false;
	private FileOutputStream fos;
	private AudioRecord recordInstance;
	private int hAac;
	private AacEncoder aacEncoder;
	private int bufferSize,minBufferSize;
	private byte[] tempBuffer;

	public AAC(String fileName)
	{
		try
		{
			fos = new FileOutputStream(fileName);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**设置默认采样率，默认16000*/
	public AAC sampleRateInHz(int sampleRateInHz)
	{
		sampleRates[0] = sampleRateInHz;
		return this;
	}

	public void start()
	{
		isStart = true;
		Thread t = new Thread(this);
		t.start();
	}

	public void stop()
	{
		isStart = false;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < sampleRates.length; i++)
		{
			try
			{
				aacEncoder = new AacEncoder();
				hAac = aacEncoder.AACEncoderOpen(sampleRates[i], 1);
				minBufferSize = AudioRecord.getMinBufferSize(sampleRates[i], AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
				if(minBufferSize < 2048)
					minBufferSize = 2048;
				bufferSize = aacEncoder.inputSamples * 16 / 8;
				tempBuffer = new byte[minBufferSize];
				recordInstance = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRates[i], AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize);//bufferSize);
				recordInstance.startRecording();
				break;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		while (isStart)
		{
			int bufferRead = recordInstance.read(tempBuffer, 0, minBufferSize);//bufferSize);
			if (bufferRead > 0)
			{
				int n = minBufferSize/bufferSize;//算出需要循环几次取值
				for (int i = 0; i < n; i++)
				{
					byte[] temp = new byte[bufferSize];
					System.arraycopy(tempBuffer, i*bufferSize, temp, 0, bufferSize);
					byte[] ret = aacEncoder.AACEncoderEncode(hAac, temp, temp.length);
					try
					{
						fos.write(ret);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
			//计算出音量分贝值
			if (bufferRead>0)
			{
				long v = 0;
				for (int i = 0; i < tempBuffer.length; i++)
				{
					v += tempBuffer[i] * tempBuffer[i];//计算平方和
				}
				double mean = v/(double)bufferRead;//平方和/总长度，得到音量大小
				volume = 10*Math.log10(mean);//转换公式
				//L.e("volume","分贝值："+volume);
			}
		}

		try
		{
			recordInstance.stop();
			recordInstance.release();
			recordInstance = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		aacEncoder.AACEncoderClose(hAac);
		try
		{
			fos.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private double volume = 0;
	/**实时获取音量大小*/
	public double getVolume()
	{
		return volume;
	}

}
