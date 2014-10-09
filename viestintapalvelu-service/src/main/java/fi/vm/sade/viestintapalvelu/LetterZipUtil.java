package fi.vm.sade.viestintapalvelu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class LetterZipUtil {

    public static byte[] unZip(byte[] content) throws IOException, DataFormatException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(content.length);
    
        Inflater inflater = new Inflater();
        inflater.setInput(content);
    
        byte[] buffer = new byte[1024];
    
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
    
        outputStream.close();
        return outputStream.toByteArray();
    }

    public static byte[] zip(byte[] content) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(content.length);
    
        Deflater deflater = new Deflater();
        deflater.setInput(content);
        deflater.finish();
    
        byte[] buffer = new byte[1024];
    
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
    
        outputStream.close();
        return outputStream.toByteArray();
    }
}
