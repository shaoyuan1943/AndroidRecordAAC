package com.sinaapp.bashell;

public class AacEncoder {
	/* MPEG ID's */
	private final int MPEG2 = 1;
	private final int MPEG4 = 0;

	/* AAC object types */
	private final int MAIN = 1;
	private final int LOW = 2;
	private final int SSR = 3;
	private final int LTP = 4;

	/* Bitstream output format */
	private final int RAW = 0;
	private final int ADTS = 1;

	/* Input Formats */
	private final int FAAC_INPUT_NULL = 0;
	private final int FAAC_INPUT_16BIT = 1;
	private final int FAAC_INPUT_24BIT = 2;
	private final int FAAC_INPUT_32BIT = 3;
	private final int FAAC_INPUT_FLOAT = 4;

	public int inputSamples = 0;
	public int maxOutputBytes = 0;
	public int bitRate = 32000;
	public int quantqual = 100;
	public int bandWidth = 7200;
	public int aacObjectType = LOW;
	public int outputFormat = ADTS;
	public int mpegVersion = MPEG4;
	public int inputFormat = FAAC_INPUT_16BIT;
	static
	{
		System.loadLibrary("AacEncoder");
	}

	public native int AACEncoderOpen(int sampleRate, int channels);

	public native byte[] AACEncoderEncode(int hEncoder, byte[] inputBuffer, int inputBufferSize);

	public native int AACEncoderClose(int hEncoder);
}
