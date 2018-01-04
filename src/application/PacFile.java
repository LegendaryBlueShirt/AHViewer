package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

public class PacFile {
	private static final byte[] MAGIC = {0x46, 0x50, 0x41, 0x43};
	
	RandomAccessFile raf;
	boolean loaded = false;
	
	long[] offsets;
	int[] sizes;
	String[] names;
	
	public PacFile(RandomAccessFile raf, long offset) {
		byte buffer[] = new byte[4];
		try {
			this.raf = raf;
			raf.seek(offset);
			raf.read(buffer);
			if(!new String(MAGIC).equals(new String(buffer))) {
				System.err.println("File was not a valid PAC file.");
				return;
			}
			int headerSize = readInt();
			int fileSize = readInt();
			int nFiles = readInt();
			if((offset + fileSize) > raf.length()) {
				System.err.println("Filesize mismatch!");
				return;
			}
			names = new String[nFiles];
			sizes = new int[nFiles];
			offsets = new long[nFiles];
			int unk = readInt();
			int stringSize = readInt();
			raf.skipBytes(8);
			
			byte[] stringBuffer = new byte[stringSize];
			int paddingBytes = (16 - (stringSize%16))%16;
			for(int n = 0;n < nFiles;n++) {
				raf.read(stringBuffer);
				names[n] = new String(stringBuffer).trim();
				int fileNum = readInt();
				if(fileNum != n) {
					System.err.println("File order mismatch? "+names[n]);
				}
				offsets[n] = readInt() + offset + headerSize;
				sizes[n] = readInt();
				int unk2 = readInt();
				raf.skipBytes(paddingBytes);
			}
			
			loaded = true;
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public RandomAccessFile getFileHandle() {
		if(!loaded)
			return null;
		return raf;
	}
	
	public String getFilename(int index) {
		if(!loaded)
			return null;
		return names[index];
	}
	
	public int getFileSize(int index) {
		if(!loaded)
			return 0;
		return sizes[index];
	}
	
	public long getFileOffset(int index) {
		if(!loaded)
			return 0;
		return offsets[index];
	}
	
	public int getNumFiles() {
		if(!loaded)
			return 0;
		return offsets.length;
	}
	
	public byte[] getFileData(int index) throws IOException {
		byte[] data = new byte[getFileSize(index)];
		raf.seek(getFileOffset(index));
		raf.readFully(data);
		return data;
	}
	
	public static PacFile unpack(File file) throws FileNotFoundException {
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		return new PacFile(raf, 0);
	}
	
	private int readInt() throws IOException {
		int a = raf.readUnsignedByte();
		int b = raf.readUnsignedByte();
		int c = raf.readUnsignedByte();
		int d = raf.readUnsignedByte();
		return a | (b<<8) | (c<<16) | (d<<24);
	}
	
	public static void main(String args[])throws IOException {
		System.out.println("Input file?");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = br.readLine();
		File inFile = new File(input);
		
		PacFile main = unpack(inFile);
		File outfolder = new File("output");
		outfolder.mkdir();
		for(int n = 0;n < main.getNumFiles();n++) {
			PacFile moreData = new PacFile(main.getFileHandle(), main.getFileOffset(n));
			File subfolder = new File(outfolder, main.getFilename(n));
			subfolder.mkdir();
			for(int x = 0;x < moreData.getNumFiles();x++) {
				File outfile = new File(subfolder, moreData.getFilename(x));
				FileOutputStream fos = new FileOutputStream(outfile);
				fos.write(moreData.getFileData(x));
				fos.close();
				System.out.println("Wrote file "+outfile.getPath());
			}
		}
		main.getFileHandle().close();
	}
}
