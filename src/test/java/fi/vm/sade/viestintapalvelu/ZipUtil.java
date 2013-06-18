package fi.vm.sade.viestintapalvelu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtil {
    private static final int BUFFER_SIZE = 1024;

    public static Map<String, byte[]> zipEntryNamesAndContents(byte[] mainZip)
            throws IOException {
        ZipInputStream in = new ZipInputStream(
                new ByteArrayInputStream(mainZip));
        ZipEntry zipEntry;
        Map<String, List<Byte>> entryNamesAndContents = new HashMap<String, List<Byte>>();
        while ((zipEntry = in.getNextEntry()) != null) {
            entryNamesAndContents
                    .put(zipEntry.getName(), new ArrayList<Byte>());
            byte[] buffer;
            while (in.available() != 0) {
                buffer = new byte[BUFFER_SIZE];
                int bytesRead = in.read(buffer, 0, 1024);
                for (int i = 0; i < bytesRead; i++) {
                    entryNamesAndContents.get(zipEntry.getName())
                            .add(buffer[i]);
                }
            }
            in.closeEntry();
        }
        in.close();
        Map<String, byte[]> output = new HashMap<String, byte[]>();
        for (Map.Entry<String, List<Byte>> entry : entryNamesAndContents
                .entrySet()) {
            output.put(entry.getKey(), toByteArray(entry.getValue()));
        }
        return output;
    }

    private static byte[] toByteArray(List<Byte> input) {
        byte[] output = new byte[input.size()];
        for (int i = 0; i < output.length; i++) {
            output[i] = input.get(i);
        }
        return output;
    }
}
